import React, { Component } from 'react';
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import AppNavbar from './AppNavbar';
import { Link,withRouter } from 'react-router-dom';
import Form from "react-bootstrap/Form";
import CampaignChart from "./CampaignChart";
import Tabs from "react-bootstrap/Tabs";
import Tab from 'react-bootstrap/Tab'
import {ConfirmationNumber} from "@material-ui/icons";
import CampaignProgress from "./CampaignProgress";

class CampaignEdit extends Component {

    emptyItem = {
        name: '',
        email: ''
    };

    constructor(props) {
        super(props);
        this.state = {
            item: this.emptyItem
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    async componentDidMount() {
        if (this.props.match.params.id !== 'new') {
            const campaign = await (await fetch(`/campaigns/${this.props.match.params.id}`)).json();
            this.setState({item: campaign});
        }
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let item = {...this.state.item};
        item[name] = value;
        this.setState({item});
    }

    async handleSubmit(event) {
        event.preventDefault();
        const {item} = this.state;

        await fetch('/campaigns' + (item.campaignUUID ? '/' + item.campaignUUID : ''), {
            method: (item.campaignUUID) ? 'PUT' : 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item),
        });
        this.props.history.push('/campaigns');
    }

    render() {
        const {item} = this.state;
        const title = <h2>{item.campaignUUID ? 'Edit Campaign' : 'Add Campaign'}</h2>;

        return <div>
            <AppNavbar/>
            <Container className={'pt-5 h-100'}>
                {title}

                <Tabs defaultActiveKey="progress" id="uncontrolled-tab-example" className="mb-3">

                    <Tab eventKey="progress" title="Progress" className={'h-100'}>
                        <div className={'panel-wrapper'}>
                            <Form.Label for="name">Number of Annotations</Form.Label>
                        <CampaignChart/>
                            <div className={'panel-wrapper'}>
                                <Form.Label for="name">Campaign Progression</Form.Label>
                        <CampaignProgress/>
                            </div>
                            <Link to="/campaigns"><Button color="secondary" >Back</Button></Link>{' '}
                        </div>

                    </Tab>
                    <Tab eventKey="info" title="Info">
                        <Form onSubmit={this.handleSubmit}>
                            <Form.Group>
                                <Form.Label for="name">Name</Form.Label>
                                <Form.Control type="text" name="name" id="name" value={item.name || ''}
                                              onChange={this.handleChange} autoComplete="name"/>
                            </Form.Group>

                            <Form.Group>
                                <Form.Label for="description">Description</Form.Label>
                                <Form.Control as="textarea" rows={3} name="description" id="description" value={item.description || ''}
                                              onChange={this.handleChange} autoComplete="description"/>
                            </Form.Group>

                            <Form.Group className={'pt-5'}>
                                <Button color="primary" type="submit">Save</Button>{' '}
                                <Link to="/campaigns"><Button color="secondary" >Cancel</Button></Link>{' '}
                            </Form.Group>
                        </Form>
                    </Tab>

                </Tabs>

            </Container>
        </div>
    }
}
export default withRouter(CampaignEdit);
