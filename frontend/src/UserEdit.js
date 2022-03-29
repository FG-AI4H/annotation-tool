import React, {Component, useEffect, useState} from "react";
import AppNavbar from "./AppNavbar";
import Container from "react-bootstrap/Container";
import {Link as RouterLink, Link, withRouter} from "react-router-dom";
import {API, Auth, graphqlOperation} from "aws-amplify";
import UserClient from "./api/UserClient";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import {Tab, Tabs} from "react-bootstrap";
import {FaPlus} from "react-icons/fa";
import {
    Button,
    Checkbox,
    FormControl,
    FormControlLabel,
    IconButton,
    InputLabel, MenuItem,
    Select,
    Stack,
    TextField
} from "@mui/material";
import Loader from "react-loader-spinner";

const UserEdit = (props) => {

    const emptyItem = {
        name: '',
        email: ''
    };

    const [item, setItem] = useState(emptyItem);
    const [isLoading, setIsLoading] = useState(false);

    //Load at page load
    useEffect(() => {
        setIsLoading(true);
        Auth.currentAuthenticatedUser({
            bypassCache: false
        }).then(response => {
            if (props.match.params.id !== 'new') {
                const client = new UserClient(response.signInUserSession.accessToken.jwtToken);
                client.fetchUserById(props.match.params.id)
                    .then(
                        response => {

                            let user = response?.data;
                            user.annotation_annotator_role = user.annotatorRole != undefined
                            user.yearsInPractice = user.annotatorRole?.yearsInPractice
                            user.degree = user.annotatorRole?.degree
                            user.workCountry = user.annotatorRole?.workCountry
                            user.studyCountry = user.annotatorRole?.studyCountry
                            user.selfAssessment = user.annotatorRole?.selfAssessment
                            user.expectedSalary = user.annotatorRole?.expectedSalary


                            user.annotation_reviewer_role = user.reviewerRole != undefined
                            user.annotation_supervisor_role = user.supervisorRole != undefined

                            setItem(user);
                            setIsLoading(false);
                        });
            }

        }).catch(err => console.log(err));

    }, [props.match.params.id])


    function handleChange(event) {
        const target = event.target;
        let value = target.value;
        const name = target.name;

        if(target.type === 'checkbox'){
            value = target.checked;
        }

        let user = {...item};
        user[name] = value;
        setItem(user);
    }

    async function handleSubmit(event) {
        event.preventDefault();

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
        if(item.annotation_supervisor_role){
            item.supervisorRole = {

            }
        }

        Auth.currentAuthenticatedUser({
            bypassCache: false
        }).then(response => {
            const client = new UserClient(response.signInUserSession.accessToken.jwtToken);
            if(item.userUUID) {
                client.updateUser(item)
                    .then(
                        response => setItem(response?.data)
                        );
            }
            else{
                client.addUser(item)
                    .then(
                            response => setItem(response?.data));
            }
        }).catch(err => console.log(err));


        props.history.push('/userManagement');
    }

    if (isLoading) {
        return (<div className="loading"><Loader
            type="Puff"
            color="#00a5e3"
            height={100}
            width={100}
            timeout={3000} //3 secs
        /></div>);
    }

    return (
        <div>
        <AppNavbar/>
        <Container className={'pt-5'}>
            <h2>{item.userUUID ? 'Edit User' : 'Add User'}</h2>
            <Row>
                <Col>
                    <FormControl fullWidth margin={"normal"} >
                        <TextField
                            id="username"
                            name="username"
                            value={item.username || ''}
                            label="Username"
                            onChange={handleChange}
                        />
                    </FormControl>

                    <FormControl fullWidth margin={"normal"} >
                        <TextField
                            type={"email"}
                            id="email"
                            name="email"
                            value={item.email || ''}
                            label="Email"
                            onChange={handleChange}
                        />
                    </FormControl>

                    <Stack direction="row" spacing={2}>
                        <Button color="primary" onClick={e => handleSubmit(e)}>Save</Button>
                        <Button component={RouterLink} color="secondary" to="/userManagement">Back</Button>
                    </Stack>

                </Col>
            </Row>
            <Row className={'pt-5'}>
                <Col>
                    <h3>Roles</h3>

                    <Row className={'gap-3'}>
                        <Col >
                            <h4>Annotation Platform</h4>
                            <FormControl fullWidth margin={"normal"}>
                                <FormControlLabel control={<Checkbox name="annotation_annotator_role" id="annotation_annotator_role" checked={item.annotation_annotator_role} onChange={handleChange}/>} label="Annotator" />
                                <FormControlLabel control={<Checkbox name="annotation_reviewer_role" id="annotation_reviewer_role" checked={item.annotation_reviewer_role} onChange={handleChange}/>} label="Reviewer" />
                                <FormControlLabel control={<Checkbox name="annotation_supervisor_role" id="annotation_supervisor_role" checked={item.annotation_supervisor_role} onChange={handleChange}/>} label="Supervisor" />
                                <FormControlLabel control={<Checkbox name="annotation_manager_role" id="annotation_manager_role" checked={item.annotation_manager_role} onChange={handleChange}/>} label="Campaign Manager" />
                                <FormControlLabel control={<Checkbox name="annotation_admin_role" id="annotation_admin_role" checked={item.annotation_admin_role} onChange={handleChange}/>} label="Admin" />
                            </FormControl>

                        </Col>
                        <Col>


                            <h4>Data Platform</h4>
                            <FormControl fullWidth>
                                <FormControlLabel control={<Checkbox name="data_admin_role" id="data_admin_role" checked={item.data_admin_role} onChange={handleChange}/>} label="Admin" />
                            </FormControl>
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
                                    <FormControl fullWidth margin={"normal"} >
                                        <TextField
                                            type={"date"}
                                            id="birthdate"
                                            name="birthdate"
                                            value={item.birthdate}
                                            label="Birthdate"
                                            onChange={handleChange}
                                        />
                                    </FormControl>

                                </Col>
                                <Col>

                                </Col>
                            </Row>


                        </Tab>
                        {(item.annotation_annotator_role || item.annotation_reviewer_role || item.annotation_supervisor_role || item.annotation_manager_role || item.annotation_admin_role) &&
                            <Tab eventKey="annotation_annotator_role" title="Annotation Platform">
                                <Row>
                                    <Col>
                                        <FormControl fullWidth margin={"normal"}>
                                            <InputLabel id="kind-label">Degree</InputLabel>
                                            <Select
                                                id="degree"
                                                name="degree"
                                                value={item.degree}
                                                label="Kind"
                                                onChange={handleChange}
                                            >
                                                <MenuItem value={"1"}>Associate</MenuItem>
                                                <MenuItem value={"2"}>Bachelor</MenuItem>
                                                <MenuItem value={"3"}>Master</MenuItem>
                                                <MenuItem value={"4"}>Doctoral</MenuItem>
                                            </Select>
                                        </FormControl>



                                    </Col>
                                    <Col>
                                        <TextField fullWidth margin={"normal"}
                                            id="studyCountry"
                                            name="studyCountry"
                                            value={item.studyCountry}
                                            label="Study Country"
                                            onChange={handleChange}
                                        />
                                    </Col>
                                </Row>
                                <Row>
                                    <Col>
                                        <TextField fullWidth margin={"normal"}
                                            id="workCountry"
                                            name="workCountry"
                                            value={item.studyCountry}
                                            label="Working Country"
                                            onChange={handleChange}
                                        />
                                    </Col>
                                    <Col>
                                        <TextField fullWidth margin={"normal"}
                                            type={"number"}
                                            id="yearsInPractice"
                                            name="yearsInPractice"
                                            value={item.yearsInPractice}
                                            label="Years in practice"
                                            onChange={handleChange}
                                        />

                                    </Col>
                                </Row>
                                <Row>
                                    <Col className="d-flex align-items-end">
                                        <Stack direction="row" spacing={2}>
                                            <IconButton ><FaPlus/>&nbsp;Expertise</IconButton>
                                        </Stack>

                                    </Col>
                                </Row>
                                <Row>
                                    <Col>
                                        <TextField fullWidth margin={"normal"}
                                            id="timezone"
                                            name="timezone"
                                            value={item.timezone}
                                            label="Timezone"
                                            onChange={handleChange}
                                        />
                                    </Col>
                                    <Col>
                                        <TextField fullWidth margin={"normal"}
                                            type={"number"}
                                            id="availabilityPerWeek"
                                            name="availabilityPerWeek"
                                            value={item.availabilityPerWeek}
                                            label="Availability per week (hours)"
                                            onChange={handleChange}
                                        />
                                    </Col>
                                </Row>
                                <Row>
                                    <Col>
                                        <TextField fullWidth margin={"normal"}
                                            type={"number"}
                                            id="selfAssessment"
                                            name="selfAssessment"
                                            value={item.selfAssessment}
                                            label="Self Assessment"
                                            onChange={handleChange}
                                        />
                                    </Col>
                                    <Col>
                                        <TextField fullWidth margin={"normal"}
                                            type={"number"}
                                            id="expectedSalary"
                                            name="expectedSalary"
                                            value={item.expectedSalary}
                                            label="Expected compensation ($ per hour)"
                                            onChange={handleChange}
                                        />

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

                    <Stack direction="row" spacing={2}>
                        <Button color="primary" onClick={e => handleSubmit(e)}>Save</Button>
                        <Button component={RouterLink} color="secondary" to="/userManagement">Back</Button>
                    </Stack>


                </Col>
            </Row>
        </Container>
    </div>
    );

}
export default UserEdit;
