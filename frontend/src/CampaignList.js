import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';

class CampaignList extends Component {

    constructor(props) {
        super(props);
        this.state = {campaigns: []};
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        fetch('/campaigns')
            .then(response => response.json())
            .then(data => this.setState({campaigns: data._embedded.campaign}));
    }

    async remove(id) {
        await fetch(`/campaigns/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedCampaigns = [...this.state.campaigns].filter(i => i.id !== id);
            this.setState({campaigns: updatedCampaigns});
        });
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
                        <Button size="sm" color="primary" tag={Link} to={"/campaigns/" + campaign.campaignUUID}>Edit</Button>
                        <Button size="sm" color="danger" onClick={() => this.remove(campaign.campaignUUID)}>Delete</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <div className="float-right">
                        <Button color="success" tag={Link} to="/campaigns/new">Add Campaign</Button>
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
