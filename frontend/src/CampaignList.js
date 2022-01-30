import React, {Component} from 'react';
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import AppNavbar from './AppNavbar';
import {Link} from 'react-router-dom';
import ButtonGroup from "react-bootstrap/ButtonGroup";
import Table from "react-bootstrap/Table";
import {Auth} from "aws-amplify";
import CampaignClient from "./api/CampaignClient";
import {FaRedo} from "react-icons/fa";

class CampaignList extends Component {

    constructor(props) {
        super(props);
        this.state = {campaigns: []};
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        this.setState({isLoading: true});
        Auth.currentAuthenticatedUser({
            bypassCache: false
        }).then(response => {
            const client = new CampaignClient(response.signInUserSession.accessToken.jwtToken);
            client.fetchCampaignList()
                .then(
                    response =>
                        this.setState(
                            {campaigns: response?.data._embedded.campaign, isLoading: false}
                        ));
        }).catch(err => console.log(err));

    }

    async remove(id) {
        Auth.currentAuthenticatedUser({
            bypassCache: false
        }).then(response => {
            const client = new CampaignClient(response.signInUserSession.accessToken.jwtToken);
            client.removeCampaign(id)
                .then(
                    response => {
                        let updatedCampaigns = [...this.state.campaigns].filter(i => i.campaignUUID !== id);
                        this.setState({campaigns: updatedCampaigns});
                    });
        }).catch(err => console.log(err));

    }

    render() {
        const {campaigns, isLoading} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        const campaignList = campaigns.map(campaign => {
            return <tr key={campaign.campaignUUID}>
                <td style={{whiteSpace: 'nowrap'}}>{campaign.name}</td>
                <td>{campaign.description}</td>
                <td>
                    <ButtonGroup>
                        <Link to={"/campaigns/" + campaign.campaignUUID}><Button size="sm" variant="primary">Edit</Button></Link>{' '}
                        <Button size="sm" variant="danger" onClick={() => this.remove(campaign.campaignUUID)}>Delete</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <div>
                <AppNavbar/>
                <Container className={'pt-5'}>
                    <div className="float-end">
                        <Button variant={'light'} onClick={() => this.componentDidMount()}><FaRedo /></Button>{' '}
                        <Link to={"/campaigns/new"}><Button variant="success">Add Campaign</Button></Link>
                    </div>
                    <h3>Campaigns</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="30%">Name</th>
                            <th width="30%">Description</th>
                            <th width="40%">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {campaignList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );
    }
}
export default CampaignList;
