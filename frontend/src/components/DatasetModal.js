import FormControl from "@material-ui/core/FormControl";
import InputLabel from "@material-ui/core/InputLabel";
import FilledInput from "@material-ui/core/FilledInput";
import TextField from "@material-ui/core/TextField";
import Accordion from "@material-ui/core/Accordion";
import MuiAccordionSummary from "@material-ui/core/AccordionSummary";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import AccordionDetails from "@material-ui/core/AccordionDetails";
import Grid from "@material-ui/core/Grid";
import Button from "@material-ui/core/Button";
import Box from "@material-ui/core/Box";
import Backdrop from "@material-ui/core/Backdrop";
import CircularProgress from "@material-ui/core/CircularProgress";
import React, {useEffect, useState} from "react";
import Modal from "@material-ui/core/Modal";

const modalMode = Object.freeze({ _EDIT: 'edit', _READ: 'read' })

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

const DatasetModal = (props) => {

    const [formState, setFormState] = useState(initialState)
    const [open, setOpen] = useState(false);
    const [readOnlyMode, setReadOnlyMode] = useState(true);
    const [backdropOpen, setBackdropOpen] = useState(false);

    useEffect(() => {
        setOpen(props?.modalOpen);
        setFormState(props?.formState);
    }, [props?.modalOpen])

    //Close "Add / Edit Dataset" modal
    const handleModalClose = () => {
        setOpen(false);
    };

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

    //HTML part of "Add Dataset" modal
    const modalBody = (
        <div >
            <h2>{readOnlyMode ? "View Dataset" : "Add Dataset"}</h2>
            <form noValidate autoComplete="off">
                <FormControl variant="filled" >
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

                    disabled={readOnlyMode}
                /><br />

                <Accordion>
                    <MuiAccordionSummary
                        expandIcon={<ExpandMoreIcon />}
                        aria-controls="panel1a-content"
                        id="panel1a-header"
                    >
                        <InputLabel style={{color: 'black'}}>General Metadata</InputLabel>
                    </MuiAccordionSummary>
                    <AccordionDetails>
                        <div style={{ width: '100%' }}>
                            <Grid container spacing={1}>
                                <Grid item xs={12}>
                                    <FormControl variant="filled" >
                                        <InputLabel required htmlFor="component-filled2">Data Owner</InputLabel>
                                        <FilledInput required id="component-filled2" value={formState.metadata?.dataOwner} onChange={event => setInputMetadata('dataOwner', event.target.value)} disabled={readOnlyMode}
                                        />
                                    </FormControl>
                                </Grid>
                                <Grid item xs={12}>
                                    <FormControl variant="filled" >
                                        <InputLabel required htmlFor="component-filled4">Data Source</InputLabel>
                                        <FilledInput required id="component-filled4" value={formState.metadata?.dataSource} onChange={event => setInputMetadata('dataSource', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>
                                <Grid item xs={12}>
                                    <FormControl variant="filled" >
                                        <InputLabel htmlFor="component-filled27">Data Registry URL</InputLabel>
                                        <FilledInput id="component-filled27" value={formState.metadata?.dataRegistryURL} onChange={event => setInputMetadata('dataRegistryURL', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>
                                <Grid item xs={6}><FormControl variant="filled" >
                                    <InputLabel required htmlFor="component-filled5">Data Sample Size</InputLabel>
                                    <FilledInput required id="component-filled5" value={formState.metadata?.dataSampleSize} onChange={event => setInputMetadata('dataSampleSize', event.target.value)} disabled={readOnlyMode} />
                                </FormControl><br />
                                </Grid>
                                <Grid item xs={6}>
                                    <FormControl variant="filled" >
                                        <InputLabel htmlFor="component-filled6">Data Type</InputLabel>
                                        <FilledInput required id="component-filled6" value={formState.metadata?.dataType} onChange={event => setInputMetadata('dataType', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>
                                <Grid item xs={12}>
                                    <FormControl variant="filled" >
                                        <InputLabel htmlFor="component-filled7">Data Update Version</InputLabel>
                                        <FilledInput id="component-filled7" value={formState.metadata?.dataUpdateVersion} onChange={event => setInputMetadata('dataUpdateVersion', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>

                                <Grid item xs={12}>
                                    <TextField
                                        id="filled-multiline-flexible"
                                        label="Data Assumptions/ Constraints/Dependencies"
                                        multiline
                                        rows={4}
                                        rowsMax={4}
                                        value={formState.metadata?.dataAssumptionsConstraintsDependencies}
                                        onChange={event => setInputMetadata('dataAssumptionsConstraintsDependencies', event.target.value)}
                                        variant="filled"

                                        disabled={readOnlyMode}
                                    /><br />
                                </Grid>
                            </Grid>

                        </div>
                    </AccordionDetails>
                </Accordion>

                <Accordion>
                    <MuiAccordionSummary
                        expandIcon={<ExpandMoreIcon />}
                        aria-controls="panel1a-content"
                        id="panel1a-header"
                    >
                        <InputLabel style={{color: 'black'}}>Data Collection</InputLabel>
                    </MuiAccordionSummary>
                    <AccordionDetails>
                        <div style={{ width: '100%' }}>
                            <Grid container spacing={1}>
                                <Grid item xs={6}>
                                    <FormControl variant="filled" >
                                        <InputLabel htmlFor="component-filled8">Data Acquisition / Sensing Modality </InputLabel>
                                        <FilledInput id="component-filled8" value={formState.metadata?.dataAcquisitionSensingModality} onChange={event => setInputMetadata('dataAcquisitionSensingModality', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>
                                <Grid item xs={6}>
                                    <FormControl variant="filled" >
                                        <InputLabel htmlFor="component-filled9">Data Acquisition / Sensing Device Type</InputLabel>
                                        <FilledInput id="component-filled9" value={formState.metadata?.dataAcquisitionSensingDeviceType} onChange={event => setInputMetadata('dataAcquisitionSensingDeviceType', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>
                                <Grid item xs={6}>
                                    <FormControl variant="filled" >
                                        <InputLabel htmlFor="component-filled10">Data Collection Place</InputLabel>
                                        <FilledInput id="component-filled10" value={formState.metadata?.dataCollectionPlace} onChange={event => setInputMetadata('dataCollectionPlace', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>
                                <Grid item xs={6}>
                                    <FormControl variant="filled" >
                                        <InputLabel htmlFor="component-filled11">Data Collection Period</InputLabel>
                                        <FilledInput id="component-filled11" value={formState.metadata?.dataCollectionPeriod} onChange={event => setInputMetadata('dataCollectionPeriod', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>
                                <Grid item xs={6}>
                                    <FormControl variant="filled" >
                                        <InputLabel htmlFor="component-filled12">Data Collection Author</InputLabel>
                                        <FilledInput id="component-filled12" value={formState.metadata?.datCollectionAuthorsAgency} onChange={event => setInputMetadata('datCollectionAuthorsAgency', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>
                                <Grid item xs={6}>
                                    <FormControl variant="filled" >
                                        <InputLabel htmlFor="component-filled13"> Data Collection Funding Agency</InputLabel>
                                        <FilledInput id="component-filled13" value={formState.metadata?.dataCollectionFundingAgency} onChange={event => setInputMetadata('dataCollectionFundingAgency', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>
                            </Grid>
                        </div>
                    </AccordionDetails>
                </Accordion>

                <Accordion>
                    <MuiAccordionSummary
                        expandIcon={<ExpandMoreIcon />}
                        aria-controls="panel1a-content"
                        id="panel1a-header"
                    >
                        <InputLabel style={{color: 'black'}}>Data Privacy</InputLabel>
                    </MuiAccordionSummary>
                    <AccordionDetails>
                        <div style={{ width: '100%' }}>
                            <Grid container spacing={1}>
                                <Grid item xs={12}>
                                    <FormControl variant="filled" >
                                        <InputLabel htmlFor="component-filled16">Data Resolution / Precision</InputLabel>
                                        <FilledInput id="component-filled16" value={formState.metadata?.dataResolutionPrecision} onChange={event => setInputMetadata('dataResolutionPrecision', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField
                                        id="filled-multiline-flexible"
                                        label="Data Privacy or De-identification Protocol"
                                        multiline
                                        rows={4}
                                        rowsMax={4}
                                        value={formState.metadata?.dataPrivacyDeIdentificationProtocol}
                                        onChange={event => setInputMetadata('dataPrivacyDeIdentificationProtocol', event.target.value)}
                                        variant="filled"

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
                                        value={formState.metadata?.dataSafetySecurityProtocol}
                                        onChange={event => setInputMetadata('dataSafetySecurityProtocol', event.target.value)}
                                        variant="filled"

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
                                        value={formState.metadata?.dataExclusionCriteria}
                                        onChange={event => setInputMetadata('dataExclusionCriteria', event.target.value)}
                                        variant="filled"

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
                                        value={formState.metadata?.dataAcceptanceStandardsCompliance}
                                        onChange={event => setInputMetadata('dataAcceptanceStandardsCompliance', event.target.value)}
                                        variant="filled"

                                        disabled={readOnlyMode}
                                    />
                                </Grid>
                            </Grid>
                        </div>
                    </AccordionDetails>
                </Accordion>

                <Accordion>
                    <MuiAccordionSummary
                        expandIcon={<ExpandMoreIcon />}
                        aria-controls="panel1a-content"
                        id="panel1a-header"
                    >
                        <InputLabel style={{color: 'black'}}>Data Preparation</InputLabel>
                    </MuiAccordionSummary>
                    <AccordionDetails>
                        <div style={{ width: '100%' }}>
                            <Grid container spacing={1}>
                                <Grid item xs={6}>
                                    <FormControl variant="filled" >
                                        <InputLabel htmlFor="component-filled14">Data Sampling Rate</InputLabel>
                                        <FilledInput id="component-filled14" value={formState.metadata?.dataSamplingRate} onChange={event => setInputMetadata('dataSamplingRate', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>
                                <Grid item xs={6}>
                                    <FormControl variant="filled" >
                                        <InputLabel htmlFor="component-filled15">Data Dimension</InputLabel>
                                        <FilledInput id="component-filled15" value={formState.metadata?.dataDimension} onChange={event => setInputMetadata('dataDimension', event.target.value)} disabled={readOnlyMode} />
                                    </FormControl><br />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField
                                        id="filled-multiline-flexible"
                                        label="Data Pre-processing Technique(s)"
                                        multiline
                                        rows={4}
                                        rowsMax={4}
                                        value={formState.metadata?.dataPreprocessingTechnique}
                                        onChange={event => setInputMetadata('dataPreprocessingTechnique', event.target.value)}
                                        variant="filled"

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
                                        value={formState.metadata?.dataAnnotationProcessTool}
                                        onChange={event => setInputMetadata('dataAnnotationProcessTool', event.target.value)}
                                        variant="filled"

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
                                        value={formState.metadata?.dataBiasAndVarianceMinimization}
                                        onChange={event => setInputMetadata('dataBiasAndVarianceMinimization', event.target.value)}
                                        variant="filled"

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
                                        value={formState.metadata?.trainTuningEvalDatasetPartitioningRatio}
                                        onChange={event => setInputMetadata('trainTuningEvalDatasetPartitioningRatio', event.target.value)}
                                        variant="filled"

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
                        <FormControl variant="filled" >
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
                    <div >
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

            </Grid>
            <Backdrop open={backdropOpen}>
                <CircularProgress color="inherit" />
            </Backdrop>
        </div >
    );

    //---------------- End of "Add Dataset" modal ------------------

    return (
        <React.Fragment>

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
export default DatasetModal;
