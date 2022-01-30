import React, {Component} from 'react';
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import AppNavbar from './AppNavbar';
import {Link, withRouter} from 'react-router-dom';
import Form from "react-bootstrap/Form";
import CampaignChart from "./CampaignChart";
import Tabs from "react-bootstrap/Tabs";
import Tab from 'react-bootstrap/Tab'
import CampaignProgress from "./CampaignProgress";
import {Auth} from "aws-amplify";
import CampaignClient from "./api/CampaignClient";
import Loader from "react-loader-spinner";
import UserListComponent from "./components/UserListComponent";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import ButtonGroup from "react-bootstrap/ButtonGroup";
import UserClient from "./api/UserClient";
import Table from "react-bootstrap/Table";

class CampaignEdit extends Component {

    emptyItem = {
        name: '',
        description: '',
        users: []
    };

    constructor(props) {
        super(props);
        this.state = {
            item: this.emptyItem,
            isLoading: false,
            availableUsers: []
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    async componentDidMount() {
        this.setState({ isLoading: true });

        Auth.currentAuthenticatedUser({
            bypassCache: false
        }).then(response => {
            if (this.props.match.params.id !== 'new') {
                const campaignClient = new CampaignClient(response.signInUserSession.accessToken.jwtToken);
                campaignClient.fetchCampaignById(this.props.match.params.id)
                    .then(
                        response =>
                            this.setState(
                                {item: response?.data, isLoading: false}
                            ));
            }

            const userClient = new UserClient(response.signInUserSession.accessToken.jwtToken);
            userClient.fetchUserList()
                .then(
                    response =>
                        this.setState(
                            {availableUsers: response?.data._embedded.user, isLoading: false}
                        ));

        }).catch(err => console.log(err));


    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;

        if(target.type === 'checkbox'){
            value = target.checked;
        }
        
        let item = {...this.state.item};
        item[name] = value;
        this.setState({item});
    }

    async handleSubmit(event) {
        event.preventDefault();
        const {item} = this.state;

        Auth.currentAuthenticatedUser({
            bypassCache: false
        }).then(response => {
            const client = new CampaignClient(response.signInUserSession.accessToken.jwtToken);
            if(item.campaignUUID) {
                client.updateCampaign(item)
                    .then(
                        response => {
                            this.setState(
                                {item: response?.data, isLoading: false}
                            );
                            this.props.history.push('/campaigns');
                        })
            }
            else{
                client.addCampaign(item)
                    .then(
                        response => {
                            this.setState(
                                {item: response?.data, isLoading: false}
                            );
                            this.props.history.push('/campaigns');
                        });
            }
        }).catch(err => console.log(err));


    }

    render() {
        const {availableUsers, item, isLoading} = this.state;
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

        const availableUserList = availableUsers.map(user => {
            return <tr key={user.idpID}>
                <td style={{whiteSpace: 'nowrap'}}>{user.username}</td>
                <td style={{whiteSpace: 'nowrap'}}>{user.email}</td>
                <td>
                    <ButtonGroup >
                       <Button size="sm" variant="success" onClick={() => this.selectUser(user)}>Select</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        const userList = item.users.map(user => {
            return <tr key={user.idpID}>
                <td style={{whiteSpace: 'nowrap'}}>{user.username}</td>
                <td style={{whiteSpace: 'nowrap'}}>{user.email}</td>
                <td>
                    <ButtonGroup >
                       <Button size="sm" variant="danger" onClick={() => this.removeSelectUser(user)}>Remove</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return <div>
            <AppNavbar/>
            <Container className={'pt-5 h-100'}>
                {title}

                {item.campaignUUID &&
                    <Tabs defaultActiveKey="progress" id="uncontrolled-tab-example" className="mb-3">

                        <Tab eventKey="progress" title="Progress" className={'h-100'}>
                            <div className={'panel-wrapper'}>
                                <Form.Label htmlFor="name">Number of Annotations</Form.Label>
                                <CampaignChart/>
                                <div className={'panel-wrapper'}>
                                    <Form.Label htmlFor="name">Campaign Progression</Form.Label>
                                    <CampaignProgress/>
                                </div>
                                <Link to="/campaigns"><Button color="secondary">Back</Button></Link>{' '}
                            </div>

                        </Tab>
                        <Tab eventKey="info" title="Info">
                            <Form onSubmit={this.handleSubmit}>
                                <Form.Group>
                                    <Form.Label htmlFor="name">Name</Form.Label>
                                    <Form.Control type="text" name="name" id="name" value={item.name || ''}
                                                  onChange={this.handleChange} autoComplete="name"/>
                                </Form.Group>

                                <Form.Group>
                                    <Form.Label htmlFor="description">Description</Form.Label>
                                    <Form.Control as="textarea" rows={3} name="description" id="description"
                                                  value={item.description || ''}
                                                  onChange={this.handleChange} autoComplete="description"/>
                                </Form.Group>

                                <Form.Group className={'pt-5'}>
                                    <Button color="primary" type="submit">Save</Button>{' '}
                                    <Link to="/campaigns"><Button color="secondary">Cancel</Button></Link>{' '}
                                </Form.Group>
                            </Form>
                        </Tab>

                    </Tabs>
                }
                {!item.campaignUUID &&
                <>
                        <Row><Col>
                            <Form onSubmit={this.handleSubmit}>
                                <Form.Group>
                                    <Form.Label htmlFor="name">Name</Form.Label>
                                    <Form.Control type="text" name="name" id="name" value={item.name || ''}
                                                  onChange={this.handleChange} autoComplete="name"/>
                                </Form.Group>

                                <Form.Group>
                                    <Form.Label htmlFor="description">Description</Form.Label>
                                    <Form.Control as="textarea" rows={3} name="description" id="description"
                                                  value={item.description || ''}
                                                  onChange={this.handleChange} autoComplete="description"/>
                                </Form.Group>

                                <Form.Group className={'pt-5'}>
                                    <Button color="primary" type="submit">Save</Button>{' '}
                                    <Link to="/campaigns"><Button color="secondary">Cancel</Button></Link>{' '}
                                </Form.Group>
                            </Form>
                        </Col>
                        </Row>
                        <Row className='mt-5'>
                            <Col>
                                <h3>Available users</h3>
                                <Table className="mt-4">
                                    <thead>
                                    <tr>
                                        <th width="45%">Username</th>
                                        <th width="45%">Email</th>
                                        <th width="10%">Actions</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {availableUserList}
                                    </tbody>
                                </Table>
                            </Col>
                            <Col>
                                <h3>Selected users</h3>
                                <Table className="mt-4">
                                    <thead>
                                    <tr>
                                        <th width="45%">Username</th>
                                        <th width="45%">Email</th>
                                        <th width="10%">Actions</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {userList}
                                    </tbody>
                                </Table>
                            </Col>
                        </Row>
                    </>

                }

            </Container>
        </div>
    }

    removeSelectUser(user) {
        this.state.availableUsers.push(user);
        let updatedUsers = [...this.state.item.users].filter(i => i.idpID !== user.idpID);
        this.state.item.users = updatedUsers;
        this.setState({item: this.state.item,availableUsers: this.state.availableUsers})
    }

    selectUser(user) {
        this.state.item.users.push(user);
        let availableUsers = [...this.state.availableUsers].filter(i => i.idpID !== user.idpID);
        this.setState({availableUsers: availableUsers});
    }
}
export default withRouter(CampaignEdit);
