import React, {Component, useEffect, useState} from 'react';
import AppNavbar from './AppNavbar';
import {Link as RouterLink, Link, withRouter} from 'react-router-dom';
import CampaignChart from "./CampaignChart";
import Tabs from "react-bootstrap/Tabs";
import Tab from 'react-bootstrap/Tab'
import CampaignProgress from "./CampaignProgress";
import {Auth} from "aws-amplify";
import CampaignClient from "./api/CampaignClient";
import Loader from "react-loader-spinner";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import CampaignForm from "./components/CampaignForm";
import CampaignUsers from "./components/CampaignUsers";
import {Button, Container, Typography} from "@mui/material";
import CampaignTask from "./components/CampaignTask";
import CampaignData from "./components/CampaignData";

const CampaignEdit = (props) => {

    const emptyItem = {
        name: '',
        description: '',
        annotators: [],
        reviewers: []
    };

    const [item, setItem] = useState(emptyItem);
    const [isLoading, setIsLoading] = useState(false);

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

    return <div>
        <AppNavbar/>
        <Container sx={{ mt: 5 }}>
            {title}

            {item.campaignUUID &&
                <Tabs defaultActiveKey="progress" id="uncontrolled-tab-example" className="mb-3">

                    <Tab eventKey="progress" title="Progress" className={'h-100'}>
                        <div className={'panel-wrapper'}>
                            <Typography gutterBottom variant="h5" component="div">Number of Annotations</Typography>
                            <CampaignChart/>
                            <div className={'panel-wrapper'}>
                                <Typography gutterBottom variant="h5" component="div">Campaign Progression</Typography>
                                <CampaignProgress/>
                            </div>
                            <Button component={RouterLink} color="secondary" to="/campaigns">Back</Button>
                        </div>

                    </Tab>
                    <Tab eventKey="settings" title="Settings">
                        <Row><Col>
                            <CampaignForm campaign={item}/>
                        </Col>
                        </Row>
                        <CampaignUsers campaign={item}/>
                    </Tab>
                    <Tab eventKey="data" title="Dataset">
                        <CampaignData campaign={item}/>
                    </Tab>
                    <Tab eventKey="tasks" title="Tasks">
                        <CampaignTask campaign={item}/>
                    </Tab>

                </Tabs>
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
