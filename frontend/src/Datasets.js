import React, {useEffect, useState} from 'react'
import {makeStyles, withStyles} from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Grid from '@material-ui/core/Grid';
import Button from '@material-ui/core/Button';
import Modal from '@material-ui/core/Modal';
import FilledInput from '@material-ui/core/FilledInput';
import FormControl from '@material-ui/core/FormControl';
import InputLabel from '@material-ui/core/InputLabel';
import TextField from '@material-ui/core/TextField';
import Backdrop from '@material-ui/core/Backdrop';
import CircularProgress from '@material-ui/core/CircularProgress';
import Link from '@material-ui/core/Link';
import Accordion from '@material-ui/core/Accordion';
import MuiAccordionSummary from '@material-ui/core/AccordionSummary';
import AccordionDetails from '@material-ui/core/AccordionDetails';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import Box from '@material-ui/core/Box';
import axios from 'axios';
import {API, graphqlOperation} from 'aws-amplify'
import {createDataset} from './graphql/mutations'
import {listDatasets} from './graphql/queries'
import uuid from 'react-uuid'
import Auth from '@aws-amplify/auth';

import Title from './Title';

const modalMode = Object.freeze({ _EDIT: 'edit', _READ: 'read' })

//dataset metadata specification as per
// p.19: https://extranet.itu.int/sites/itu-t/focusgroups/ai4h/docs/FGAI4H-J-049.pdf
// and p. 4: https://extranet.itu.int/sites/itu-t/focusgroups/ai4h/_layouts/15/WopiFrame.aspx?sourcedoc=%7B3DAE32A1-24FF-4F4D-A735-F378056BA6CF%7D&file=FGAI4H-J-048.docx&action=default&CT=1610025396926&OR=DocLibClassicUI

const initialState = {
    name: '',
    description: '',
    selectedFile: undefined,
    metadata: {
        //General Metadata
        dataOwner: '',
        dataSource: '',
        dataSampleSize: '',
        dataType: '',
        dataRegistryURL: '',
        dataUpdateVersion: '',
        dataAssumptionsConstraintsDependencies: '',
        //Data Collection
        dataAcquisitionSensingModality: '',
        dataAcquisitionSensingDeviceType: '',
        dataCollectionPlace: '',
        dataCollectionPeriod: '',
        datCollectionAuthorsAgency: '',
        dataCollectionFundingAgency: '',
        //Data Privacy
        dataResolutionPrecision: '',
        dataPrivacyDeIdentificationProtocol: '',
        dataSafetySecurityProtocol: '',
        dataExclusionCriteria: '',
        dataAcceptanceStandardsCompliance: '',
        //Data Preparation
        dataSamplingRate: '',
        dataDimension: '',
        dataPreprocessingTechniques: '',
        dataAnnotationProcessTool: '',
        dataBiasAndVarianceMinimization: '',
        trainTuningEvalDatasetPartitioningRatio: ''
    }
}

const useStyles = makeStyles((theme) => ({
    modal: {
        display: 'flex',
        justifyContent: 'center',
        overflow: 'scroll',
    },
    paper: {
        backgroundColor: theme.palette.background.paper,
        border: '2px solid #000',
        boxShadow: theme.shadows[5],
        padding: theme.spacing(2, 4, 3),
    },
    backdrop: {
        zIndex: theme.zIndex.drawer + 1,
        color: '#fff',
    },
}));

const styles = {
    modal: {
        width: '70%',
        position: 'absolute',
        marginTop: 10
    },
    input: {
        width: '100%',
        marginBottom: 8
    }
}

const AccordionSummary = withStyles({
    root: {
        backgroundColor: 'rgba(0, 0, 0, .03)',
        borderBottom: '1px solid rgba(0, 0, 0, .125)',
        marginBottom: -1,
        minHeight: 56,
        '&$expanded': {
            minHeight: 56,
        },
    },
    content: {
        '&$expanded': {
            margin: '12px 0',
        },
    },
    expanded: {},
})(MuiAccordionSummary);

export default function Datasets(props) {
    const classes = useStyles();
    const [datasets, setDatasets] = useState([])
    const [formState, setFormState] = useState(initialState)
    const [open, setOpen] = useState(false);
    const [readOnlyMode, setReadOnlyMode] = useState(false);
    const [backdropOpen, setBackdropOpen] = useState(false);

    //Load at page load
    useEffect(() => {
        fetchDatasets()
    }, [])

    //Fetch all datasets from backend
    async function fetchDatasets() {
        try {
            const datasetData = await API.graphql(graphqlOperation(listDatasets))
            const datasets = datasetData.data.listDatasets.items
            setDatasets(datasets)
        } catch (err) {
            console.log(err)
            console.log('error fetching datasets')
        }
    }

    //---------------- "Add Dataset" modal ------------------
    //TODO: move modal code in separate React component AddDatasetModal.js

    //Update input field in "Add Dataset" Modal
    function setInput(key, value) {
        setFormState({ ...formState, [key]: value })
    }

    function setInputMetadata(key, value) {
        setFormState({ ...formState, metadata: { ...formState.metadata, [key]: value, } })
    }

    //Select file to upload for a dataset
    const selectFile = (event) => {
        setInput('selectedFile', event.target.files)
    }

    //Add a new dataset in backend
    //TODO: check that required fields are filled
    async function addDataset() {
        try {
            if (!formState.name || !formState.description) return
            const dataset = { ...formState }
            const authSession = await Auth.currentSession()

            const headers = {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + authSession.accessToken.jwtToken,
                'X-Api-Key': 'fmIhJxclgb3GKFdoOG0tB4ZvvV2K4GeZab3WgZLr'
            }

            const file = dataset.selectedFile[0]

            setBackdropOpen(true)

            //push zip file to S3 using FHIR Binary API endpoint
            axios.post('https://vno8vyh8x5.execute-api.eu-central-1.amazonaws.com/dev/Binary', {
                'resourceType': 'Binary',
                'contentType': 'application/zip',
                'securityContext': {
                    'reference': 'DocumentReference/benchmarking-data'
                }
            }, {
                headers: headers
            })
                .then(res => {
                    //use presigned S3 URL from FHIR Binary API endpoint to push data to actual S3 bucket
                    return axios.put(res.data.presignedPutUrl, file, {
                        headers: {
                            'Content-Type': file.type
                        }
                    })
                })
                .then(res => {
                    const url = new URL(res.config.url)
                    const filepath = url.hostname + '/' + file.name.replace('.zip', '/')

                    const datasetUpload = {
                        id: uuid(),
                        name: dataset.name,
                        description: dataset.description,
                        storageLocation: filepath,
                        metadata: {
                            version: '1.0',
                            dataOwner: dataset.metadata.dataOwner,
                            dataSource: dataset.metadata.dataSource,
                            dataSampleSize: dataset.metadata.dataSampleSize,
                            dataType: dataset.metadata.dataType,
                            dataUpdateVersion: dataset.metadata.dataUpdateVersion,
                            dataAcquisitionSensingModality: dataset.metadata.dataAcquisitionSensingModality,
                            dataAcquisitionSensingDeviceType: dataset.metadata.dataAcquisitionSensingDeviceType,
                            dataCollectionPlace: dataset.metadata.dataCollectionPlace,
                            dataCollectionPeriod: dataset.metadata.dataCollectionPeriod,
                            datCollectionAuthorsAgency: dataset.metadata.datCollectionAuthorsAgency,
                            dataCollectionFundingAgency: dataset.metadata.dataCollectionFundingAgency,
                            dataSamplingRate: dataset.metadata.dataSamplingRate,
                            dataDimension: dataset.metadata.dataDimension,
                            dataResolutionPrecision: dataset.metadata.dataResolutionPrecision,
                            dataPrivacyDeIdentificationProtocol: dataset.metadata.dataPrivacyDeIdentificationProtocol,
                            dataSafetySecurityProtocol: dataset.metadata.dataSafetySecurityProtocol,
                            dataAssumptionsConstraintsDependencies: dataset.metadata.dataAssumptionsConstraintsDependencies,
                            dataExclusionCriteria: dataset.metadata.dataExclusionCriteria,
                            dataAcceptanceStandardsCompliance: dataset.metadata.dataAcceptanceStandardsCompliance,
                            dataPreprocessingTechniques: dataset.metadata.dataPreprocessingTechniques,
                            dataAnnotationProcessTool: dataset.metadata.dataAnnotationProcessTool,
                            dataBiasAndVarianceMinimization: dataset.metadata.dataBiasAndVarianceMinimization,
                            trainTuningEvalDatasetPartitioningRatio: dataset.metadata.trainTuningEvalDatasetPartitioningRatio,
                            dataRegistryURL: dataset.metadata.dataRegistryURL
                        }
                    }

                    return API.graphql(graphqlOperation(createDataset, { input: datasetUpload }))
                }).then(res => {
                setBackdropOpen(false)
                alert('Dataset uploaded to S3 and data is saved')
                fetchDatasets()
                setFormState(initialState)
                handleModalClose()
            })

        } catch (err) {
            setBackdropOpen(false)
            alert('error creating dataset')
            setFormState(initialState)
            handleModalClose()
        }
    }

    function viewDataset(datasetID) {
        const datasetState = datasets.find((dataset) => dataset.id === datasetID)
        handleModalOpen(modalMode._READ, datasetState)
    }

    //Open "Add / Edit Dataset" modal
    const handleModalOpen = (mode, state) => {

        if (mode === modalMode._EDIT) {
            setReadOnlyMode(false)
        } else if (mode === modalMode._READ) {
            setReadOnlyMode(true)
        }

        setOpen(true);
        setFormState(state)
    };

    //Close "Add / Edit Dataset" modal
    const handleModalClose = () => {
        setOpen(false);
    };

    //HTML part of "Add Dataset" modal
    const modalBody = (
        <div className={classes.paper} style={styles.modal}>
            <h2>{readOnlyMode ? "View Dataset" : "Add Dataset"}</h2>
            <form className={classes.root} noValidate autoComplete="off">
                <FormControl variant="filled" style={styles.input}>
                    <InputLabel required htmlFor="component-filled">Name</InputLabel>
                    <FilledInput required id="component-filled" value={formState.name} onChange={event => setInput('name', event.target.value)} disabled={readOnlyMode} />
                </FormControl><br />

                <TextField
                    id="filled-multiline-flexible"
                    label="Description"
                    multiline
                    required
                    rows={4}
                    rowsMax={4}
                    value={formState.description}
                    onChange={event => setInput('description', event.target.value)}
                    variant="filled"
                    style={styles.input}
                    disabled={readOnlyMode}
                /><br />

                <Accordion>
                    <AccordionSummary
                        expandIcon={<ExpandMoreIcon />}
                        aria-controls="panel1a-content"
                        id="panel1a-header"
                    >
                        <InputLabel style={{color: 'black'}}>General Metadata</InputLabel>
                    </AccordionSummary>
                    <AccordionDetails>
                        <div style={{ width: '100%' }}>
                            <Grid container spacing={1}>
                                <Grid item xs={12}>
                                    <FormControl variant="filled" style={styles.input}>
                                        <InputLabel required htmlFor="component-filled2">Data Owner</InputLabel>
                                        <FilledInput required id="component-filled2" value={formState.metadata.dataOwner} onChange={event => setInputMetadata('dataOwner', event.target.value)} disabled={readOnlyMode}
                                        />
                                    </FormControl>
                                </Grid>
                                <Grid item xs={12}>
                                    <FormControl variant="filled" style={styles.input}>
                                        <InputLabel required htmlFor="component-filled4">Data Source</InputLabel>
                                        <FilledInput required id="component-filled4" value={formState.metadata.dataSource} onChange={event => setInputMetadata('dataSource', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>
                                <Grid item xs={12}>
                                    <FormControl variant="filled" style={styles.input}>
                                        <InputLabel htmlFor="component-filled27">Data Registry URL</InputLabel>
                                        <FilledInput id="component-filled27" value={formState.metadata.dataRegistryURL} onChange={event => setInputMetadata('dataRegistryURL', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>
                                <Grid item xs={6}><FormControl variant="filled" style={styles.input}>
                                    <InputLabel required htmlFor="component-filled5">Data Sample Size</InputLabel>
                                    <FilledInput required id="component-filled5" value={formState.metadata.dataSampleSize} onChange={event => setInputMetadata('dataSampleSize', event.target.value)} disabled={readOnlyMode} />
                                </FormControl><br />
                                </Grid>
                                <Grid item xs={6}>
                                    <FormControl variant="filled" style={styles.input}>
                                        <InputLabel htmlFor="component-filled6">Data Type</InputLabel>
                                        <FilledInput required id="component-filled6" value={formState.metadata.dataType} onChange={event => setInputMetadata('dataType', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>
                                <Grid item xs={12}>
                                    <FormControl variant="filled" style={styles.input}>
                                        <InputLabel htmlFor="component-filled7">Data Update Version</InputLabel>
                                        <FilledInput id="component-filled7" value={formState.metadata.dataUpdateVersion} onChange={event => setInputMetadata('dataUpdateVersion', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>

                                <Grid item xs={12}>
                                    <TextField
                                        id="filled-multiline-flexible"
                                        label="Data Assumptions/ Constraints/Dependencies"
                                        multiline
                                        rows={4}
                                        rowsMax={4}
                                        value={formState.metadata.dataAssumptionsConstraintsDependencies}
                                        onChange={event => setInputMetadata('dataAssumptionsConstraintsDependencies', event.target.value)}
                                        variant="filled"
                                        style={styles.input}
                                        disabled={readOnlyMode}
                                    /><br />
                                </Grid>
                            </Grid>

                        </div>
                    </AccordionDetails>
                </Accordion>

                <Accordion>
                    <AccordionSummary
                        expandIcon={<ExpandMoreIcon />}
                        aria-controls="panel1a-content"
                        id="panel1a-header"
                    >
                        <InputLabel style={{color: 'black'}}>Data Collection</InputLabel>
                    </AccordionSummary>
                    <AccordionDetails>
                        <div style={{ width: '100%' }}>
                            <Grid container spacing={1}>
                                <Grid item xs={6}>
                                    <FormControl variant="filled" style={styles.input}>
                                        <InputLabel htmlFor="component-filled8">Data Acquisition / Sensing Modality </InputLabel>
                                        <FilledInput id="component-filled8" value={formState.metadata.dataAcquisitionSensingModality} onChange={event => setInputMetadata('dataAcquisitionSensingModality', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>
                                <Grid item xs={6}>
                                    <FormControl variant="filled" style={styles.input}>
                                        <InputLabel htmlFor="component-filled9">Data Acquisition / Sensing Device Type</InputLabel>
                                        <FilledInput id="component-filled9" value={formState.metadata.dataAcquisitionSensingDeviceType} onChange={event => setInputMetadata('dataAcquisitionSensingDeviceType', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>
                                <Grid item xs={6}>
                                    <FormControl variant="filled" style={styles.input}>
                                        <InputLabel htmlFor="component-filled10">Data Collection Place</InputLabel>
                                        <FilledInput id="component-filled10" value={formState.metadata.dataCollectionPlace} onChange={event => setInputMetadata('dataCollectionPlace', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>
                                <Grid item xs={6}>
                                    <FormControl variant="filled" style={styles.input}>
                                        <InputLabel htmlFor="component-filled11">Data Collection Period</InputLabel>
                                        <FilledInput id="component-filled11" value={formState.metadata.dataCollectionPeriod} onChange={event => setInputMetadata('dataCollectionPeriod', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>
                                <Grid item xs={6}>
                                    <FormControl variant="filled" style={styles.input}>
                                        <InputLabel htmlFor="component-filled12">Data Collection Author</InputLabel>
                                        <FilledInput id="component-filled12" value={formState.metadata.datCollectionAuthorsAgency} onChange={event => setInputMetadata('datCollectionAuthorsAgency', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>
                                <Grid item xs={6}>
                                    <FormControl variant="filled" style={styles.input}>
                                        <InputLabel htmlFor="component-filled13"> Data Collection Funding Agency</InputLabel>
                                        <FilledInput id="component-filled13" value={formState.metadata.dataCollectionFundingAgency} onChange={event => setInputMetadata('dataCollectionFundingAgency', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>
                            </Grid>
                        </div>
                    </AccordionDetails>
                </Accordion>

                <Accordion>
                    <AccordionSummary
                        expandIcon={<ExpandMoreIcon />}
                        aria-controls="panel1a-content"
                        id="panel1a-header"
                    >
                        <InputLabel style={{color: 'black'}}>Data Privacy</InputLabel>
                    </AccordionSummary>
                    <AccordionDetails>
                        <div style={{ width: '100%' }}>
                            <Grid container spacing={1}>
                                <Grid item xs={12}>
                                    <FormControl variant="filled" style={styles.input}>
                                        <InputLabel htmlFor="component-filled16">Data Resolution / Precision</InputLabel>
                                        <FilledInput id="component-filled16" value={formState.metadata.dataResolutionPrecision} onChange={event => setInputMetadata('dataResolutionPrecision', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField
                                        id="filled-multiline-flexible"
                                        label="Data Privacy or De-identification Protocol"
                                        multiline
                                        rows={4}
                                        rowsMax={4}
                                        value={formState.metadata.dataPrivacyDeIdentificationProtocol}
                                        onChange={event => setInputMetadata('dataPrivacyDeIdentificationProtocol', event.target.value)}
                                        variant="filled"
                                        style={styles.input}
                                        disabled={readOnlyMode}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField
                                        id="filled-multiline-flexible"
                                        label="Data Safety & Security Protocol"
                                        multiline
                                        rows={4}
                                        rowsMax={4}
                                        value={formState.metadata.dataSafetySecurityProtocol}
                                        onChange={event => setInputMetadata('dataSafetySecurityProtocol', event.target.value)}
                                        variant="filled"
                                        style={styles.input}
                                        disabled={readOnlyMode}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField
                                        id="filled-multiline-flexible"
                                        label="Data Exclusion Criteria"
                                        multiline
                                        rows={4}
                                        rowsMax={4}
                                        value={formState.metadata.dataExclusionCriteria}
                                        onChange={event => setInputMetadata('dataExclusionCriteria', event.target.value)}
                                        variant="filled"
                                        style={styles.input}
                                        disabled={readOnlyMode}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField
                                        id="filled-multiline-flexible"
                                        label="Data Acceptance-Standards Compliance"
                                        multiline
                                        rows={4}
                                        rowsMax={4}
                                        value={formState.metadata.dataAcceptanceStandardsCompliance}
                                        onChange={event => setInputMetadata('dataAcceptanceStandardsCompliance', event.target.value)}
                                        variant="filled"
                                        style={styles.input}
                                        disabled={readOnlyMode}
                                    />
                                </Grid>
                            </Grid>
                        </div>
                    </AccordionDetails>
                </Accordion>

                <Accordion>
                    <AccordionSummary
                        expandIcon={<ExpandMoreIcon />}
                        aria-controls="panel1a-content"
                        id="panel1a-header"
                    >
                        <InputLabel style={{color: 'black'}}>Data Preparation</InputLabel>
                    </AccordionSummary>
                    <AccordionDetails>
                        <div style={{ width: '100%' }}>
                            <Grid container spacing={1}>
                                <Grid item xs={6}>
                                    <FormControl variant="filled" style={styles.input}>
                                        <InputLabel htmlFor="component-filled14">Data Sampling Rate</InputLabel>
                                        <FilledInput id="component-filled14" value={formState.metadata.dataSamplingRate} onChange={event => setInputMetadata('dataSamplingRate', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>
                                <Grid item xs={6}>
                                    <FormControl variant="filled" style={styles.input}>
                                        <InputLabel htmlFor="component-filled15">Data Dimension</InputLabel>
                                        <FilledInput id="component-filled15" value={formState.metadata.dataDimension} onChange={event => setInputMetadata('dataDimension', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField
                                        id="filled-multiline-flexible"
                                        label="Data Pre-processing Technique(s)"
                                        multiline
                                        rows={4}
                                        rowsMax={4}
                                        value={formState.metadata.dataPreprocessingTechnique}
                                        onChange={event => setInputMetadata('dataPreprocessingTechnique', event.target.value)}
                                        variant="filled"
                                        style={styles.input}
                                        disabled={readOnlyMode}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField
                                        id="filled-multiline-flexible"
                                        label="Data Annotation Process / Tool"
                                        multiline
                                        rows={4}
                                        rowsMax={4}
                                        value={formState.metadata.dataAnnotationProcessTool}
                                        onChange={event => setInputMetadata('dataAnnotationProcessTool', event.target.value)}
                                        variant="filled"
                                        style={styles.input}
                                        disabled={readOnlyMode}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField
                                        id="filled-multiline-flexible"
                                        label="Data Bias & Variance Minimization"
                                        multiline
                                        rows={4}
                                        rowsMax={4}
                                        value={formState.metadata.dataBiasAndVarianceMinimization}
                                        onChange={event => setInputMetadata('dataBiasAndVarianceMinimization', event.target.value)}
                                        variant="filled"
                                        style={styles.input}
                                        disabled={readOnlyMode}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField
                                        id="filled-multiline-flexible"
                                        label="Train: Tuning(validation) : Test (evaluation) Dataset Partitioning Ratio"
                                        multiline
                                        rows={4}
                                        rowsMax={4}
                                        value={formState.metadata.trainTuningEvalDatasetPartitioningRatio}
                                        onChange={event => setInputMetadata('trainTuningEvalDatasetPartitioningRatio', event.target.value)}
                                        variant="filled"
                                        style={styles.input}
                                        disabled={readOnlyMode}
                                    />
                                </Grid>
                            </Grid>
                        </div>
                    </AccordionDetails>
                </Accordion>

                <br/>

                {readOnlyMode ?
                    <div>
                        <FormControl variant="filled" style={styles.input}>
                            <InputLabel required htmlFor="component-filled2">Storage Location</InputLabel>
                            <FilledInput
                                required
                                id="component-filled2"
                                value={formState.storageLocation}
                                disabled={readOnlyMode}
                            />
                        </FormControl><br />
                    </div>
                    :
                    <div style={styles.input}>
                        <input
                            id="btn-upload"
                            name="btn-upload"
                            style={{ display: 'none' }}
                            type="file"
                            accept="application/zip"
                            onChange={selectFile} />
                        <label htmlFor="btn-upload">
                            <Button
                                className="btn-choose"
                                variant="outlined"
                                component="span"
                                disabled={readOnlyMode}
                            >
                                Choose Dataset File (.zip)
                            </Button>
                        </label>
                        <div className="file-name">
                            <Box color="text.disabled">{formState.selectedFile !== undefined ? formState.selectedFile[0].name : ''}</Box>
                        </div>
                    </div>
                }

            </form>


            <Grid container>
                <Grid item xs={10}>
                    <Button variant="contained" onClick={handleModalClose}>{readOnlyMode ? "Close" : "Cancel"}</Button>
                </Grid>
                <Grid item xs={2}>
                    {readOnlyMode ? null : <Button variant="contained" onClick={addDataset}>Add Dataset</Button>}
                </Grid>
            </Grid>
            <Backdrop className={classes.backdrop} open={backdropOpen}>
                <CircularProgress color="inherit" />
            </Backdrop>
        </div >
    );

    //---------------- End of "Add Dataset" modal ------------------

    return (
        <React.Fragment>
            <Title>My Datasets</Title>
            <Grid container justifyContent="flex-end">
                <Button variant="contained" onClick={() => handleModalOpen(modalMode._EDIT, initialState)}>Add Dataset</Button>
            </Grid>

            <Table size="small">
                <TableHead>
                    <TableRow>
                        <TableCell><b>Name</b></TableCell>
                        <TableCell><b>Data Owner</b></TableCell>
                        <TableCell><b>Last Updated</b></TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>

                    {datasets.map((dataset) => (
                        <TableRow key={dataset.id}>
                            <TableCell><Link href="#" onClick={() => viewDataset(dataset.id)}>{dataset.name}</Link></TableCell>
                            <TableCell>{dataset.metadata.dataOwner}</TableCell>
                            <TableCell>{(new Date(Date.parse(dataset.updatedAt))).toLocaleString(navigator.language)}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>

            <Modal
                open={open}
                onClose={handleModalClose}
                aria-labelledby="simple-modal-title"
                aria-describedby="simple-modal-description"
                className={classes.modal}
            >
                {modalBody}
            </Modal>


        </React.Fragment>
    );
}
