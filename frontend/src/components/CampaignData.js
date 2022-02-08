import React, {useEffect, useState} from 'react'
import { DataGrid, GridActionsCellItem } from '@mui/x-data-grid';
import Grid from '@material-ui/core/Grid';
import Accordion from '@mui/material/Accordion';
import AccordionSummary from '@mui/material/AccordionSummary';
import AccordionDetails from '@mui/material/AccordionDetails';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import {API, Auth, graphqlOperation} from 'aws-amplify'
import {listDatasets} from '../graphql/queries'
import {Col, Row} from "react-bootstrap";
import VisibilityIcon from '@mui/icons-material/Visibility';
import {
    Backdrop,
    Box,
    Button,
    CircularProgress,
    FilledInput,
    FormControl,
    InputLabel, Link,
    Modal, Paper, Snackbar, Stack, Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
    TextField,
    Typography
} from "@mui/material";
import {Link as RouterLink} from "react-router-dom";
import CampaignClient from "../api/CampaignClient";
import Alert from "@mui/material/Alert";

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



export default function Datasets(props) {

    const [campaign, setCampaign] = useState(props.campaign);
    const [updated, setUpdated] = useState(false);
    const [datasets, setDatasets] = useState();
    const [formState, setFormState] = useState(initialState);
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
            setDatasets(datasetData.data.listDatasets.items)
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

    const viewDataset = React.useCallback(
        (dataset) => () => {
            handleModalOpen(modalMode._READ, dataset)
        },
        [],
    );

    //Open "Add / Edit Dataset" modal
    const handleModalOpen = (mode, state) => {

        if (mode === modalMode._EDIT) {
            setReadOnlyMode(false)
        } else if (mode === modalMode._READ) {
            setReadOnlyMode(true)
        }

        setOpen(true);
        setFormState(state);
    };

    //Close "Add / Edit Dataset" modal
    const handleModalClose = () => {
        setOpen(false);
    };

    async function handleSubmit(event) {
        event.preventDefault();

        Auth.currentAuthenticatedUser({
            bypassCache: false
        }).then(response => {
            const client = new CampaignClient(response.signInUserSession.accessToken.jwtToken);

            client.updateCampaign(campaign)
                .then(
                    response => setUpdated(true))

        }).catch(err => console.log(err));

    }

    function handleClose(event, reason){
        if (reason === 'clickaway') {
            return;
        }
        setUpdated(false);

    }

    const style = {
        position: 'absolute',
        top: '50%',
        left: '50%',
        transform: 'translate(-50%, -50%)',
        width: "50%",
        bgcolor: 'background.paper',
        border: '2px solid #000',
        boxShadow: 24,
        p: 4,
    };
    

    //HTML part of "Add Dataset" modal
    const modalBody = (
        <Box sx={style}>
            <Typography id="modal-modal-title" variant="h6" component="h2">
                {readOnlyMode ? "View Dataset" : "Add Dataset"}
            </Typography>
            <form noValidate autoComplete="off">

                <TextField fullWidth sx={{ mt: 5 }}
                           label="Name"
                           required
                           value={formState.name}
                           onChange={event => setInput('name', event.target.value)}

                           disabled={readOnlyMode}
                />

                <TextField fullWidth sx={{ mt: 5 }}
                    label="Description"
                    multiline
                    required
                    rows={4}
                    rowsMax={4}
                    value={formState.description}
                    onChange={event => setInput('description', event.target.value)}

                    disabled={readOnlyMode}
                />

                <Accordion sx={{ mt: 5 }}>
                    <AccordionSummary
                        expandIcon={<ExpandMoreIcon />}
                        aria-controls="panel1a-content"
                        id="panel1a-header"
                    >
                        <InputLabel >General Metadata</InputLabel>
                    </AccordionSummary>
                    <AccordionDetails>
                        <div style={{ width: '100%' }}>
                            <Grid container spacing={1}>
                                <Grid item xs={12}>
                                    <TextField fullWidth label={"Data Owner"} required id="component-filled2" value={formState.metadata?.dataOwner} onChange={event => setInputMetadata('dataOwner', event.target.value)} disabled={readOnlyMode} />
                                </Grid>
                                <Grid item xs={12}>
                                        <TextField fullWidth label={"Data Source"} required id="component-filled4" value={formState.metadata?.dataSource} onChange={event => setInputMetadata('dataSource', event.target.value)} disabled={readOnlyMode} />
                                </Grid>
                                <Grid item xs={12}>
                                        <TextField fullWidth label={"Data Registry URL"} id="component-filled27" value={formState.metadata?.dataRegistryURL} onChange={event => setInputMetadata('dataRegistryURL', event.target.value)} disabled={readOnlyMode} />
                                </Grid>
                                <Grid item xs={6}>
                                    <TextField fullWidth label={"Data Sample Size"} required id="component-filled5" value={formState.metadata?.dataSampleSize} onChange={event => setInputMetadata('dataSampleSize', event.target.value)} disabled={readOnlyMode} />
                                </Grid>
                                <Grid item xs={6}>
                                        <TextField fullWidth label={"Data Type"} required id="component-filled6" value={formState.metadata?.dataType} onChange={event => setInputMetadata('dataType', event.target.value)} disabled={readOnlyMode} />
                                </Grid>
                                <Grid item xs={12}>
                                        <TextField fullWidth label={"Data Update Version"} id="component-filled7" value={formState.metadata?.dataUpdateVersion} onChange={event => setInputMetadata('dataUpdateVersion', event.target.value)} disabled={readOnlyMode} />
                                </Grid>

                                <Grid item xs={12}>
                                    <TextField fullWidth
                                        id="filled-multiline-flexible"
                                        label="Data Assumptions/ Constraints/Dependencies"
                                        multiline
                                        rows={4}
                                        rowsMax={4}
                                        value={formState.metadata?.dataAssumptionsConstraintsDependencies}
                                        onChange={event => setInputMetadata('dataAssumptionsConstraintsDependencies', event.target.value)}
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
                        <InputLabel >Data Collection</InputLabel>
                    </AccordionSummary>
                    <AccordionDetails>
                        <div style={{ width: '100%' }}>
                            <Grid container spacing={1}>
                                <Grid item xs={6}>
                                        <TextField fullWidth label={"Data Acquisition / Sensing Modality"} id="component-filled8" value={formState.metadata?.dataAcquisitionSensingModality} onChange={event => setInputMetadata('dataAcquisitionSensingModality', event.target.value)} disabled={readOnlyMode} />
                                </Grid>
                                <Grid item xs={6}>
                                        <TextField fullWidth label={"Data Acquisition / Sensing Device Type"} id="component-filled9" value={formState.metadata?.dataAcquisitionSensingDeviceType} onChange={event => setInputMetadata('dataAcquisitionSensingDeviceType', event.target.value)} disabled={readOnlyMode} />
                                </Grid>
                                <Grid item xs={6}>
                                        <TextField fullWidth label={"Data Collection Place"} id="component-filled10" value={formState.metadata?.dataCollectionPlace} onChange={event => setInputMetadata('dataCollectionPlace', event.target.value)} disabled={readOnlyMode} />
                                </Grid>
                                <Grid item xs={6}>
                                        <TextField fullWidth label={"Data Collection Period"} id="component-filled11" value={formState.metadata?.dataCollectionPeriod} onChange={event => setInputMetadata('dataCollectionPeriod', event.target.value)} disabled={readOnlyMode} />
                                </Grid>
                                <Grid item xs={6}>
                                        <TextField fullWidth label={"Data Collection Author"} id="component-filled12" value={formState.metadata?.datCollectionAuthorsAgency} onChange={event => setInputMetadata('datCollectionAuthorsAgency', event.target.value)} disabled={readOnlyMode} />
                                </Grid>
                                <Grid item xs={6}>
                                        <TextField fullWidth label={"Data Collection Funding Agency"} id="component-filled13" value={formState.metadata?.dataCollectionFundingAgency} onChange={event => setInputMetadata('dataCollectionFundingAgency', event.target.value)} disabled={readOnlyMode} />
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
                        <InputLabel >Data Privacy</InputLabel>
                    </AccordionSummary>
                    <AccordionDetails>
                        <div style={{ width: '100%' }}>
                            <Grid container spacing={1}>
                                <Grid item xs={12}>

                                        <TextField fullWidth label={"Data Resolution / Precision"} id="component-filled16" value={formState.metadata?.dataResolutionPrecision} onChange={event => setInputMetadata('dataResolutionPrecision', event.target.value)} disabled={readOnlyMode} />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField fullWidth
                                        id="filled-multiline-flexible"
                                        label="Data Privacy or De-identification Protocol"
                                        multiline
                                        rows={4}
                                        rowsMax={4}
                                        value={formState.metadata?.dataPrivacyDeIdentificationProtocol}
                                        onChange={event => setInputMetadata('dataPrivacyDeIdentificationProtocol', event.target.value)}

                                        disabled={readOnlyMode}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField fullWidth
                                        id="filled-multiline-flexible"
                                        label="Data Safety & Security Protocol"
                                        multiline
                                        rows={4}
                                        rowsMax={4}
                                        value={formState.metadata?.dataSafetySecurityProtocol}
                                        onChange={event => setInputMetadata('dataSafetySecurityProtocol', event.target.value)}

                                        disabled={readOnlyMode}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField fullWidth
                                        id="filled-multiline-flexible"
                                        label="Data Exclusion Criteria"
                                        multiline
                                        rows={4}
                                        rowsMax={4}
                                        value={formState.metadata?.dataExclusionCriteria}
                                        onChange={event => setInputMetadata('dataExclusionCriteria', event.target.value)}

                                        disabled={readOnlyMode}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField fullWidth
                                        id="filled-multiline-flexible"
                                        label="Data Acceptance-Standards Compliance"
                                        multiline
                                        rows={4}
                                        rowsMax={4}
                                        value={formState.metadata?.dataAcceptanceStandardsCompliance}
                                        onChange={event => setInputMetadata('dataAcceptanceStandardsCompliance', event.target.value)}

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
                        <InputLabel >Data Preparation</InputLabel>
                    </AccordionSummary>
                    <AccordionDetails>
                        <div style={{ width: '100%' }}>
                            <Grid container spacing={1}>
                                <Grid item xs={6}>
                                        <TextField fullWidth label={"Data Sampling Rate"} id="component-filled14" value={formState.metadata?.dataSamplingRate} onChange={event => setInputMetadata('dataSamplingRate', event.target.value)} disabled={readOnlyMode} />
                                </Grid>
                                <Grid item xs={6}>

                                        <TextField fullWidth label={"Data Dimension"} id="component-filled15" value={formState.metadata?.dataDimension} onChange={event => setInputMetadata('dataDimension', event.target.value)} disabled={readOnlyMode} />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField fullWidth
                                        id="filled-multiline-flexible"
                                        label="Data Pre-processing Technique(s)"
                                        multiline
                                        rows={4}
                                        rowsMax={4}
                                        value={formState.metadata?.dataPreprocessingTechnique}
                                        onChange={event => setInputMetadata('dataPreprocessingTechnique', event.target.value)}

                                        disabled={readOnlyMode}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField fullWidth
                                        id="filled-multiline-flexible"
                                        label="Data Annotation Process / Tool"
                                        multiline
                                        rows={4}
                                        rowsMax={4}
                                        value={formState.metadata?.dataAnnotationProcessTool}
                                        onChange={event => setInputMetadata('dataAnnotationProcessTool', event.target.value)}

                                        disabled={readOnlyMode}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField fullWidth
                                        id="filled-multiline-flexible"
                                        label="Data Bias & Variance Minimization"
                                        multiline
                                        rows={4}
                                        rowsMax={4}
                                        value={formState.metadata?.dataBiasAndVarianceMinimization}
                                        onChange={event => setInputMetadata('dataBiasAndVarianceMinimization', event.target.value)}

                                        disabled={readOnlyMode}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField fullWidth
                                        id="filled-multiline-flexible"
                                        label="Train: Tuning(validation) : Test (evaluation) Dataset Partitioning Ratio"
                                        multiline
                                        rows={4}
                                        rowsMax={4}
                                        value={formState.metadata?.trainTuningEvalDatasetPartitioningRatio}
                                        onChange={event => setInputMetadata('trainTuningEvalDatasetPartitioningRatio', event.target.value)}

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

                            <TextField fullWidth sx={{ mt: 5 }}
                                required
                                label="Storage Location"
                                id="component-filled2"
                                value={formState.storageLocation}
                                disabled={readOnlyMode}
                            />

                    </div>
                    :
                    <div>
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

            <Stack direction="row" spacing={2} sx={{ mt: 5 }}>
                <Button variant="contained" onClick={handleModalClose}>{readOnlyMode ? "Close" : "Cancel"}</Button>
            </Stack>


            <Backdrop open={backdropOpen}>
                <CircularProgress color="inherit" />
            </Backdrop>
        </Box>
    );

    //---------------- End of "Add Dataset" modal ------------------

    const columns = [
        {
            field: 'name',
            headerName: 'Name',
            flex: 0.5,
            editable: false,
        },
        {
            field: 'description',
            headerName: 'Description',
            flex: 1,
            editable: false,
        },
        {
            field: 'description',
            headerName: 'Description',
            flex: 0.5,
            editable: false,
        },
        {
            field: 'createdAt',
            headerName: 'Created',
            flex: 0.5,
            editable: false,
            valueFormatter: ({ value }) => { return (new Date(Date.parse(value))).toLocaleString(navigator.language)},
        },
        {
            field: 'updatedAt',
            headerName: 'Updated',
            flex: 0.5,
            editable: false,
            valueFormatter: ({ value }) => { return (new Date(Date.parse(value))).toLocaleString(navigator.language)},
        },
        {
            field: 'actions',
            type: 'actions',
            flex: 0.2,
            getActions: (params) => [
                <GridActionsCellItem
                    icon={<VisibilityIcon />}
                    label="Details"
                    onClick={viewDataset(params.row)}
                />
            ]
        }
    ]



    const datasetList = campaign.datasets?.map(datasetId => {
        const dataset = datasets?.find((dataset) => dataset.id === datasetId);

        if(dataset) {
            return <TableRow key={datasetId} sx={{'&:last-child td, &:last-child th': {border: 0}}}>
                <TableCell style={{whiteSpace: 'nowrap'}}><Link
                    onClick={viewDataset(dataset)}>{dataset.name}</Link></TableCell>
                <TableCell style={{whiteSpace: 'nowrap'}}>{dataset.description}</TableCell>

                <TableCell>
                    <Stack direction="row" spacing={2}>
                        <Button size="small" color="warning"
                                onClick={() => removeSelectDataset(datasetId)}>Remove</Button>
                    </Stack>
                </TableCell>
            </TableRow>
        }
        else {
            return <TableRow sx={{'&:last-child td, &:last-child th': {border: 0}}}>
                <TableCell colSpan={3} style={{whiteSpace: 'nowrap'}}>No dataset selected</TableCell>
            </TableRow>
        }

    });

    function removeSelectDataset(datasetId) {
        let updatedDatasets = [...campaign.datasets].filter(i => i !== datasetId);
        campaign.datasets = updatedDatasets;
    }

    function handleRowSelected(selections) {
        campaign.datasets = selections;
    }

    return (
        <React.Fragment>

            <Snackbar open={updated} autoHideDuration={6000} onClose={handleClose} anchorOrigin={{
                vertical: 'top',
                horizontal: 'right'
            }}>
                <Alert severity="success" sx={{ width: '100%' }} onClose={handleClose}>
                    Campaign updated successfully!
                </Alert>
            </Snackbar>

            <Row className='mt-5'>
                <Col>
                    <Typography gutterBottom variant="h5" component="div">Available datasets</Typography>

                    <DataGrid
                        autoHeight
                        rows={datasets}
                        columns={columns}
                        onSelectionModelChange={handleRowSelected}
                        checkboxSelection
                    />
                </Col>
            </Row>

            <Row className='mt-5'>
                <Col>
                    <Typography gutterBottom variant="h5" component="div">Selected datasets</Typography>
                    <TableContainer component={Paper}>
                        <Table aria-label="simple table">
                            <TableHead>
                                <TableRow>
                                    <TableCell width="30%">Name</TableCell>
                                    <TableCell width="80%">Description</TableCell>
                                    <TableCell align={"right"} width="10%">Actions</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {datasetList}
                            </TableBody>
                        </Table>
                    </TableContainer>

                </Col>
            </Row>

            <Stack direction="row" spacing={2}>
                <Button color="primary" onClick={e => handleSubmit(e)}>Save</Button>
                <Button component={RouterLink} color="secondary" to="/campaigns">Cancel</Button>
            </Stack>

            <Modal
                open={open}
                onClose={handleModalClose}
                aria-labelledby="simple-modal-title"
                aria-describedby="simple-modal-description"
            >
                {modalBody}
            </Modal>
        </React.Fragment>
    );
}
