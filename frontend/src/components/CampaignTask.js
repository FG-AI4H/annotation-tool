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
                    <FormControl fullWidth>
                        <InputLabel >Task type</InputLabel>
                        <Select
                            id="taskType"
                            value={campaign.kind}
                            label="Task type"
                            onChange={handleChange}
                        >
                            <MenuItem value={"3DIS"}>3D Image Segmentation</MenuItem>
                            <MenuItem value={"2DIS"}>2D Image Segmentation</MenuItem>
                            <MenuItem value={"TS"}>Time Series</MenuItem>
                        </Select>
                    </FormControl>

                </Col>
            </Row>

        </div>
    );

}
export default CampaignTask;
