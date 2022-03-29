import React, {Component, useEffect, useState} from 'react';
import PropTypes from 'prop-types';
import AppNavbar from './AppNavbar';
import {Link as RouterLink, Link, withRouter} from 'react-router-dom';
import CampaignChart from "./CampaignChart";
import CampaignProgress from "./CampaignProgress";
import {Auth} from "aws-amplify";
import CampaignClient from "./api/CampaignClient";
import Loader from "react-loader-spinner";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import CampaignForm from "./components/CampaignForm";
import CampaignUsers from "./components/CampaignUsers";
import {Box, Button, Container, Tab, Tabs, Typography} from "@mui/material";
import CampaignTask from "./components/CampaignTask";
import CampaignData from "./components/CampaignData";
import {TabPanel} from "./components/TabPanel";
import {a11yProps} from "./components/allyProps";

const CampaignEdit = (props) => {

    const emptyItem = {
        name: '',
        description: '',
        annotators: [],
        reviewers: []
    };

    const [item, setItem] = useState(emptyItem);
    const [isLoading, setIsLoading] = useState(false);
    const [tabValue, setTabValue] = useState(0);

    useEffect( () =>{
        setIsLoading(true);

        Auth.currentAuthenticatedUser({
            bypassCache: false
        }).then(response => {
            if (props.match.params.id !== 'new') {
                const campaignClient = new CampaignClient(response.signInUserSession.accessToken.jwtToken);
                campaignClient.fetchCampaignById(props.match.params.id)
                    .then(
                        response => {
                            setIsLoading(false);
                            setItem(response?.data);
                        }
                    );
            }

        }).catch(err => console.log(err));

    }, [props.match.params.id])

    const title = <h2>{item.campaignUUID ? 'Edit Campaign' : 'Add Campaign'}</h2>;

    if (isLoading) {
        return (<div className="loading"><Loader
            type="Puff"
            color="#00a5e3"
            height={100}
            width={100}
            timeout={3000} //3 secs
        /></div>);
    }

    const handleChange = (event, newValue) => {
        setTabValue(newValue);
    };

    return <div>
        <AppNavbar/>
        <Container sx={{ mt: 5 }}>
            {title}

            {item.campaignUUID &&

                <Box sx={{ width: '100%' }}>
                    <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
                    <Tabs
                    value={tabValue}
                    onChange={handleChange}
                    aria-label="wrapped label tabs example"
                    >
                        <Tab label="Progress" {...a11yProps(0)}/>
                        <Tab label="Settings" {...a11yProps(1)}/>
                        <Tab label="Dataset" {...a11yProps(2)}/>
                        <Tab label="Tasks" {...a11yProps(3)}/>
                    </Tabs>
                    </Box>
                    <TabPanel value={tabValue} index={0}>
                        <div className={'panel-wrapper'}>
                            <Typography gutterBottom variant="h5" component="div">Number of Annotations</Typography>
                            <CampaignChart/>
                            <div className={'panel-wrapper'}>
                                <Typography gutterBottom variant="h5" component="div">Campaign Progression</Typography>
                                <CampaignProgress/>
                            </div>
                            <Button component={RouterLink} color="secondary" to="/campaigns">Back</Button>
                        </div>
                    </TabPanel>
                    <TabPanel value={tabValue} index={1}>
                        <Row><Col>
                            <CampaignForm campaign={item}/>
                        </Col>
                        </Row>
                        <CampaignUsers campaign={item}/>
                    </TabPanel>
                    <TabPanel value={tabValue} index={2}>
                        <CampaignData campaign={item}/>
                    </TabPanel>
                    <TabPanel value={tabValue} index={3}>
                        <CampaignTask campaign={item}/>
                    </TabPanel>
                </Box>

            }
            {!item.campaignUUID &&
            <>
                    <Row><Col>
                        <CampaignForm campaign={item}/>
                    </Col>
                    </Row>
                    <CampaignUsers campaign={item}/>
                </>

            }

        </Container>
    </div>
}
export default CampaignEdit;
