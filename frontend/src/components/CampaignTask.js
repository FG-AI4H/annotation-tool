import React, {Component, useState} from "react";
import Alert from "@mui/material/Alert";
import {InputLabel, MenuItem, Select, Snackbar, Typography} from "@mui/material";
import {Auth} from "aws-amplify";
import CampaignClient from "../api/CampaignClient";
import {Col, Row} from "react-bootstrap";
import {FormControl} from "@material-ui/core";

const CampaignTask = (props) => {

    const [campaign, setCampaign] = useState(props.campaign);
    const [updated, setUpdated] = useState(false);
    const [loading, setLoading] = useState(false);


    function handleClose(event, reason){
        if (reason === 'clickaway') {
            return;
        }

        this.setState(
            {updated: false}
        );
    }

    function handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;

        if(target.type === 'checkbox'){
            value = target.checked;
        }

        campaign[name] = value;
    }

    async function handleSubmit(event) {
        event.preventDefault();
        const {campaign} = this.state;

        Auth.currentAuthenticatedUser({
            bypassCache: false
        }).then(response => {
            const client = new CampaignClient(response.signInUserSession.accessToken.jwtToken);

                client.updateCampaign(campaign)
                    .then(
                        response => setUpdated(true))

        }).catch(err => console.log(err));

    }

    return (
        <div>
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
                    <Typography gutterBottom variant="h5" component="div">Define your tasks</Typography>
                    <FormControl fullWidth margin={"normal"}>
                        <InputLabel >Task type</InputLabel>
                        <Select
                            id="annotationKind"
                            name="annotationKind"
                            value={campaign.annotationKind}
                            label="Task type"
                            onChange={handleChange}
                        >
                            <MenuItem value={"semantic_segmentation"}>[Image] - Semantic Segmentation</MenuItem>
                            <MenuItem value={"bounding_boxes"}>[Image] - Bounding boxes</MenuItem>
                            <MenuItem value={"3d_cuboids"}>[Image] - 3D cuboids</MenuItem>
                            <MenuItem value={"polygons"}>[Image] - Polygons</MenuItem>
                            <MenuItem value={"lines_splines"}>[Image] - Lines and splines</MenuItem>
                            <MenuItem value={"time_series"}>Time Series</MenuItem>
                        </Select>
                    </FormControl>

                    <FormControl fullWidth margin={"normal"}>
                        <InputLabel >Annotation tool</InputLabel>
                        <Select
                            id="annotationTool"
                                name="annotationTool"
                            value={campaign.annotationTool}
                            label="Annotation tool"
                            onChange={handleChange}
                        >
                            <MenuItem value={"visian"}>[Image] - Visian</MenuItem>
                        </Select>
                    </FormControl>

                </Col>
            </Row>

        </div>
    );

}
export default CampaignTask;
