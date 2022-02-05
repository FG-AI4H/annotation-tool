import React, {Component} from "react";
import AppNavbar from "./AppNavbar";
import Container from "react-bootstrap/Container";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import {Link, withRouter} from "react-router-dom";
import {Auth} from "aws-amplify";
import UserClient from "./api/UserClient";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import {Tab, Tabs} from "react-bootstrap";
import {FaPlus} from "react-icons/fa";

class UserEdit extends Component {

    emptyItem = {
        name: '',
        email: ''
    };

    constructor(props) {
        super(props);
        this.state = {
            item: this.emptyItem,
            isLoading: false
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    async componentDidMount() {
        if (this.props.match.params.id !== 'new') {
            this.setState({isLoading: true});
            Auth.currentAuthenticatedUser({
                bypassCache: false
            }).then(response => {
                const client = new UserClient(response.signInUserSession.accessToken.jwtToken);
                client.fetchUserById(this.props.match.params.id)
                    .then(
                        response => {

                            let item = response?.data;
                            item.annotation_annotator_role = item.annotatorRole != undefined
                            item.yearsInPractice = item.annotatorRole?.yearsInPractice
                            item.degree = item.annotatorRole?.degree
                            item.workCountry = item.annotatorRole?.workCountry
                            item.studyCountry = item.annotatorRole?.studyCountry
                            item.selfAssessment = item.annotatorRole?.selfAssessment
                            item.expectedSalary = item.annotatorRole?.expectedSalary


                            item.annotation_reviewer_role = item.reviewerRole != undefined
                            this.setState(
                                {item: item, isLoading: false}
                            )
                        });

            }).catch(err => console.log(err));
        }
    }

    handleChange(event) {
        const target = event.target;
        let value = target.value;
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

        if(item.annotation_annotator_role){
            item.annotatorRole = {
                yearsInPractice: item.yearsInPractice,
                workCountry: item.workCountry,
                studyCountry: item.studyCountry,
                selfAssessment: item.selfAssessment,
                expectedSalary: item.expectedSalary,
                degree: item.degree
            }
        }
        if(item.annotation_reviewer_role){
            item.reviewerRole = {

            }
        }

        Auth.currentAuthenticatedUser({
            bypassCache: false
        }).then(response => {
            const client = new UserClient(response.signInUserSession.accessToken.jwtToken);
            if(item.userUUID) {
                client.updateUser(item)
                    .then(
                        response =>
                            this.setState(
                                {item: response?.data, isLoading: false}
                            ));
            }
            else{
                client.addUser(item)
                    .then(
                        response =>
                            this.setState(
                                {item: response?.data, isLoading: false}
                            ));
            }
        }).catch(err => console.log(err));


        this.props.history.push('/userManagement');
    }

    render() {
        const {item} = this.state;

        const title = <h2>{item.userUUID ? 'Edit User' : 'Add User'}</h2>;

        return <div>
            <AppNavbar/>
            <Container className={'pt-5'}>
                {title}
                <Row>
                    <Col>
                        <Form onSubmit={this.handleSubmit}>
                            <Form.Group className={'py-2'}>
                                <Form.Label htmlFor="kind">Username</Form.Label>
                                <Form.Control type="text" name="username" id="username" value={item.username} onChange={this.handleChange}/>
                            </Form.Group>

                            <Form.Group className={'py-2'}>
                                <Form.Label htmlFor="kind">Email</Form.Label>
                                <Form.Control type="text" name="email" id="email" value={item.email} onChange={this.handleChange}/>
                            </Form.Group>

                            <Form.Group>
                                <Button color="primary" type="submit">Save</Button>{' '}
                                <Link to="/userManagement"><Button color="secondary" >Back</Button></Link>{' '}
                            </Form.Group>
                        </Form>
                    </Col>
                </Row>
                <Row className={'pt-5'}>
                    <Col>
                        <h3>Roles</h3>

                        <Row className={'gap-3'}>
                            <Col >
                                <h4>Annotation Platform</h4>
                                <Form.Check checked={item.annotation_annotator_role} type="checkbox" name="annotation_annotator_role" id="annotation_annotator_role" label="Annotator" onChange={this.handleChange}/>
                                <Form.Check checked={item.annotation_reviewer_role} type="checkbox" name="annotation_reviewer_role" id="annotation_reviewer_role" label="Reviewer" onChange={this.handleChange}/>
                                <Form.Check checked={item.annotation_supervisor_role} type="checkbox" name="annotation_supervisor_role" id="annotation_supervisor_role" label="Supervisor" onChange={this.handleChange}/>
                                <Form.Check checked={item.annotation_manager_role} type="checkbox" name="annotation_manager_role" id="annotation_manager_role" label="Campaign Manager" onChange={this.handleChange}/>
                                <Form.Check checked={item.annotation_admin_role} type="checkbox" name="annotation_admin_role" id="annotation_admin_role" label="Admin" onChange={this.handleChange}/>
                            </Col>
                            <Col>


                                <h4>Data Platform</h4>
                                <Form.Check checked={item.data_admin_role} type="checkbox" name="data_admin_role" id="data_admin_role" label="Admin" onChange={this.handleChange}/>
                            </Col>
                        </Row>

                    </Col>
                </Row>
                <Row className={'pt-5'}>
                    <Col>
                        <Tabs defaultActiveKey="profile" id="uncontrolled-tab-example" className="mb-3">
                            <Tab eventKey="profile" title="Profile">
                                <Row>
                                    <Col>
                                        <Form.Group className={'py-2'}>
                                            <Form.Label htmlFor="birthdate">Birthdate</Form.Label>
                                            <Form.Control type="date" name="birthdate" id="birthdate" value={item.birthdate} onChange={this.handleChange}/>
                                        </Form.Group>
                                    </Col>
                                    <Col>

                                    </Col>
                                </Row>


                            </Tab>
                            {(item.annotation_annotator_role || item.annotation_reviewer_role || item.annotation_supervisor_role || item.annotation_manager_role || item.annotation_admin_role) &&
                                <Tab eventKey="annotation_annotator_role" title="Annotation Platform">
                                    <Row>
                                        <Col>
                                            <Form.Group className={'py-2'}>
                                                <Form.Label htmlFor="degree">Degree</Form.Label>
                                                <Form.Select  name="degree" id="degree" value={item.degree} onChange={this.handleChange}>
                                                    <option value="1">Associate</option>
                                                    <option value="2">Bachelor</option>
                                                    <option value="3">Master</option>
                                                    <option value="4">Doctoral</option>
                                                </Form.Select>
                                            </Form.Group>

                                        </Col>
                                        <Col>
                                            <Form.Group className={'py-2'}>
                                                <Form.Label htmlFor="studyCountry">Study Country</Form.Label>
                                                <Form.Control type="text" name="studyCountry" id="studyCountry" value={item.studyCountry} onChange={this.handleChange}/>
                                            </Form.Group>

                                        </Col>
                                    </Row>
                                    <Row>
                                        <Col>
                                            <Form.Group className={'py-2'}>
                                                <Form.Label htmlFor="workCountry">Working Country</Form.Label>
                                                <Form.Control type="text" name="workCountry" id="workCountry" value={item.workCountry} onChange={this.handleChange}/>
                                            </Form.Group>
                                        </Col>
                                        <Col>
                                            <Form.Group className={'py-2'}>
                                                <Form.Label htmlFor="yearsInPractice">Years in practice</Form.Label>
                                                <Form.Control type="number" name="yearsInPractice" id="yearsInPractice" value={item.yearsInPractice} onChange={this.handleChange}/>
                                            </Form.Group>
                                        </Col>
                                    </Row>
                                    <Row>
                                        <Col className="d-flex align-items-end">
                                            <Form.Group className={'py-2'}>
                                                <Button color="primary"><FaPlus/>&nbsp;Expertise</Button>
                                            </Form.Group>
                                        </Col>
                                    </Row>
                                    <Row>
                                        <Col>
                                            <Form.Group className={'py-2'}>
                                                <Form.Label htmlFor="timezone">Timezone</Form.Label>
                                                <Form.Control type="text" name="timezone" id="timezone" value={item.timezone} onChange={this.handleChange}/>
                                            </Form.Group>
                                        </Col>
                                        <Col>
                                            <Form.Group className={'py-2'}>
                                                <Form.Label htmlFor="availabilityPerWeek">Availability per week (hours)</Form.Label>
                                                <Form.Control type="number" name="availabilityPerWeek" id="availabilityPerWeek" value={item.availabilityPerWeek} onChange={this.handleChange}/>
                                            </Form.Group>
                                        </Col>
                                    </Row>
                                    <Row>
                                        <Col>
                                            <Form.Group className={'py-2'}>
                                                <Form.Label htmlFor="selfAssessment">Self Assessment</Form.Label>
                                                <Form.Control type="text" name="selfAssessment" id="selfAssessment" value={item.selfAssessment} onChange={this.handleChange}/>
                                            </Form.Group>
                                        </Col>
                                        <Col>
                                            <Form.Group className={'py-2'}>
                                                <Form.Label htmlFor="expectedSalary">Expected compensation ($ per hour)</Form.Label>
                                                <Form.Control type="number" name="expectedSalary" id="expectedSalary" value={item.expectedSalary} onChange={this.handleChange}/>
                                            </Form.Group>
                                        </Col>
                                    </Row>


                                </Tab>
                            }
                            {item.data_admin_role &&
                            <Tab eventKey="data" title="Data Platform">
                                <Row>
                                    <Col>
                                    </Col>
                                </Row>
                            </Tab>
                            }
                        </Tabs>
                        <Form.Group>
                            <Button color="primary" onClick={this.handleSubmit}>Save</Button>{' '}
                            <Link to="/userManagement"><Button color="secondary" >Back</Button></Link>{' '}
                        </Form.Group>
                    </Col>
                </Row>
            </Container>
        </div>
    }
}
export default withRouter(UserEdit);
