import React, {Component} from "react";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Form from "react-bootstrap/Form";
import {Auth} from "aws-amplify";
import UserClient from "../api/UserClient";
import Loader from "react-loader-spinner";
import {
    Button,
    FormControl, Link,
    Paper,
    Stack,
    Table, TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Tabs,
    Tab,
    TextField, Box
} from "@mui/material";
import {Link as RouterLink} from "react-router-dom";
import {a11yProps} from "./allyProps";
import {TabPanel} from "./TabPanel";

class CampaignUsers extends Component {

    annotatorFilter = {
        yearsInPractice: null,
        selfAssessment: null,
    };

    constructor(props) {
        super(props);
        this.state = {
            isLoading: false,
            availableUsers: [],
            campaign: {},
            annotatorFilter: this.annotatorFilter,
            tabValue: 0
        };
        this.handleAnnotatorFilterChange = this.handleAnnotatorFilterChange.bind(this);
        this.handleFindAnnotators = this.handleFindAnnotators.bind(this);
    }

    async componentDidMount() {
        this.setState({ isLoading: true, campaign: this.props.campaign, tabValue: 0 });

        Auth.currentAuthenticatedUser({
            bypassCache: false
        }).then(response => {

            const userClient = new UserClient(response.signInUserSession.accessToken.jwtToken);
            userClient.fetchUserList()
                .then(
                    response =>
                        this.setState(
                            {availableUsers: response?.data._embedded.user
                                    .filter(i => !this.props.campaign.annotators?.some(a => a.idpID === i.idpID))
                                    .filter(i => !this.props.campaign.reviewers?.some(a => a.idpID === i.idpID))
                                    .filter(i => !this.props.campaign.supervisors?.some(a => a.idpID === i.idpID)),
                                isLoading: false}
                        ));

        }).catch(err => console.log(err));
    }

    componentDidUpdate(prevProps) {
        if (prevProps.campaign !== this.props.campaign) {
            let updatedUsers = [...this.state.availableUsers]
                .filter(i => !this.props.campaign.annotators?.some(a => a.idpID === i.idpID))
                .filter(i => !this.props.campaign.reviewers?.some(a => a.idpID === i.idpID))
                .filter(i => !this.props.campaign.supervisors?.some(a => a.idpID === i.idpID));

            this.setState({campaign: this.props.campaign, availableUsers: updatedUsers});
        }
    }

    handleTabChange = (event, newValue) => {
        if(newValue !== undefined && this.state.tabValue !== newValue) {
            this.setState({tabValue: newValue})
        }
    };

    handleAnnotatorFilterChange(event) {
        const target = event.target;
        let value = target.value;
        const name = target.name;

        if(target.type === 'checkbox'){
            value = target.checked;
        }

        let annotatorFilter = {...this.state.annotatorFilter};
        annotatorFilter[name] = value;
        this.setState({annotatorFilter: annotatorFilter});
    }

    handleFindAnnotators(event) {
        let updatedUsers = [...this.state.availableUsers].filter(i => i.annotatorRole?.yearsInPractice >= this.state.annotatorFilter['yearsInPractice']);
        this.setState({availableUsers: updatedUsers})
    }

    handleResetAnnotators(event) {
        this.componentDidMount();
        this.setState({annotatorFilter: {
                yearsInPractice: null,
                selfAssessment: null,
            }});
    }

    removeSelectUser(user) {
        this.state.availableUsers.push(user);
        let updatedUsers = [...this.state.campaign.annotators].filter(i => i.idpID !== user.idpID);
        this.state.campaign.annotators = updatedUsers;
        this.setState({item: this.state.campaign,availableUsers: this.state.availableUsers})
    }

    selectUser(user) {
        this.state.campaign.annotators.push(user);
        let availableUsers = [...this.state.availableUsers].filter(i => i.idpID !== user.idpID);
        this.setState({availableUsers: availableUsers});
    }

    removeSelectReviewer(user) {
        this.state.availableUsers.push(user);
        let updatedUsers = [...this.state.campaign.reviewers].filter(i => i.idpID !== user.idpID);
        this.state.campaign.reviewers = updatedUsers;
        this.setState({item: this.state.campaign,availableUsers: this.state.availableUsers})
    }

    selectReviewer(user) {
        this.state.campaign.reviewers.push(user);
        let availableUsers = [...this.state.availableUsers].filter(i => i.idpID !== user.idpID);
        this.setState({availableUsers: availableUsers});
    }

    removeSelectSupervisor(user) {
        this.state.availableUsers.push(user);
        let updatedUsers = [...this.state.campaign.supervisors].filter(i => i.idpID !== user.idpID);
        this.state.campaign.supervisors = updatedUsers;
        this.setState({item: this.state.campaign,availableUsers: this.state.availableUsers})
    }

    selectSupervisor(user) {
        this.state.campaign.supervisors.push(user);
        let availableUsers = [...this.state.availableUsers].filter(i => i.idpID !== user.idpID);
        this.setState({availableUsers: availableUsers});
    }

    render() {

        const {campaign, isLoading, availableUsers, annotatorFilter, tabValue} = this.state;

        if (isLoading) {
            return (<div className="loading"><Loader
                type="Puff"
                color="#00a5e3"
                height={100}
                width={100}
                timeout={3000} //3 secs
            /></div>);
        }

        const availableAnnotatorList = availableUsers.filter(user => user.annotatorRole !== undefined).map(user => {
            return <TableRow key={user.idpID} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                <TableCell style={{whiteSpace: 'nowrap'}}><Link component={RouterLink} to={"/users/" + user.userUUID}>{user.username}</Link></TableCell>
                <TableCell style={{whiteSpace: 'nowrap'}} align={"right"}>{user.annotatorRole.yearsInPractice}</TableCell>
                <TableCell style={{whiteSpace: 'nowrap'}} align={"right"}>{user.annotatorRole.selfAssessment}</TableCell>
                <TableCell>
                    <Stack direction="row" spacing={2}>
                        <Button size="small" color="success" onClick={() => this.selectUser(user)}>Select</Button>
                    </Stack>
                </TableCell>
            </TableRow>
        });

        const annotatorList = campaign.annotators?.map(user => {
            return <TableRow key={user.idpID} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                <TableCell style={{whiteSpace: 'nowrap'}}><Link component={RouterLink} to={"/users/" + user.userUUID}>{user.username}</Link></TableCell>
                <TableCell style={{whiteSpace: 'nowrap'}} align={"right"}>{user.annotatorRole.yearsInPractice}</TableCell>
                <TableCell style={{whiteSpace: 'nowrap'}} align={"right"}>{user.annotatorRole.selfAssessment}</TableCell>

                <TableCell>
                    <Stack direction="row" spacing={2}>
                        <Button size="small" color="error" onClick={() => this.removeSelectUser(user)}>Remove</Button>
                    </Stack>
                </TableCell>
            </TableRow>
        });

        const availableReviewerList = availableUsers.filter(user => user.reviewerRole !== undefined).map(user => {
            return <TableRow key={user.idpID} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                <TableCell style={{whiteSpace: 'nowrap'}}><Link component={RouterLink} to={"/users/" + user.userUUID}>{user.username}</Link></TableCell>
                <TableCell style={{whiteSpace: 'nowrap'}} align={"right"}>{user.annotatorRole.yearsInPractice}</TableCell>
                <TableCell style={{whiteSpace: 'nowrap'}} align={"right"}>{user.annotatorRole.selfAssessment}</TableCell>
                <TableCell>
                    <Stack direction="row" spacing={2}>
                        <Button size="small" color="success" onClick={() => this.selectReviewer(user)}>Select</Button>
                    </Stack>
                </TableCell>
            </TableRow>
        });

        const reviewerList = campaign.reviewers?.map(user => {
            return <TableRow key={user.idpID} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                <TableCell style={{whiteSpace: 'nowrap'}}><Link component={RouterLink} to={"/users/" + user.userUUID}>{user.username}</Link></TableCell>
                <TableCell style={{whiteSpace: 'nowrap'}} align={"right"}>{user.annotatorRole.yearsInPractice}</TableCell>
                <TableCell style={{whiteSpace: 'nowrap'}} align={"right"}>{user.annotatorRole.selfAssessment}</TableCell>

                <TableCell>
                    <Stack direction="row" spacing={2}>
                        <Button size="small" color="error" onClick={() => this.removeSelectReviewer(user)}>Remove</Button>
                    </Stack>
                </TableCell>
            </TableRow>
        });

        const availableSupervisorList = availableUsers.filter(user => user.supervisorRole !== undefined).map(user => {
            return <TableRow key={user.idpID} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                <TableCell style={{whiteSpace: 'nowrap'}}><Link component={RouterLink} to={"/users/" + user.userUUID}>{user.username}</Link></TableCell>
                <TableCell style={{whiteSpace: 'nowrap'}} align={"right"}>{user.annotatorRole.yearsInPractice}</TableCell>
                <TableCell style={{whiteSpace: 'nowrap'}} align={"right"}>{user.annotatorRole.selfAssessment}</TableCell>
                <TableCell>
                    <Stack direction="row" spacing={2}>
                        <Button size="small" color="success" onClick={() => this.selectSupervisor(user)}>Select</Button>
                    </Stack>
                </TableCell>
            </TableRow>
        });

        const supervisorList = campaign.supervisors?.map(user => {
            return <TableRow key={user.idpID} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                <TableCell style={{whiteSpace: 'nowrap'}}><Link component={RouterLink} to={"/users/" + user.userUUID}>{user.username}</Link></TableCell>
                <TableCell style={{whiteSpace: 'nowrap'}} align={"right"}>{user.annotatorRole.yearsInPractice}</TableCell>
                <TableCell style={{whiteSpace: 'nowrap'}} align={"right"}>{user.annotatorRole.selfAssessment}</TableCell>

                <TableCell>
                    <Stack direction="row" spacing={2}>
                        <Button size="small" color="error" onClick={() => this.removeSelectSupervisor(user)}>Remove</Button>
                    </Stack>
                </TableCell>
            </TableRow>
        });

        return (
            <div>
                <Box sx={{ width: '100%' }}>
                    <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
                    <Tabs
                        value={tabValue}
                        onChange={this.handleTabChange}
                        aria-label="wrapped label tabs example"
                    >
                        <Tab label="Supervisors" {...a11yProps(0)}/>
                        <Tab label="Annotators" {...a11yProps(1)}/>
                        <Tab label="Reviewers" {...a11yProps(2)}/>
                    </Tabs>
                    </Box>
                <TabPanel value={tabValue} index={0}>
                    <Row className='mt-5'>
                        <Col>
                            <h3>Find supervisors</h3>
                        </Col>
                    </Row>
                    <Row>
                        <Col>
                            <Form>
                                <Row>
                                    <Col>
                                        <FormControl fullWidth sx={{ mt: 5 }}>
                                            <TextField
                                                type="number"
                                                id="yearsInPractice"
                                                name="yearsInPractice"
                                                value={annotatorFilter.yearsInPractice || ''}
                                                label="Minimum years in practice"
                                                onChange={this.handleAnnotatorFilterChange}
                                            />
                                        </FormControl>

                                    </Col>
                                    <Col>
                                        <FormControl fullWidth sx={{ mt: 5 }}>
                                            <TextField
                                                type="number"
                                                id="selfAssessment"
                                                name="selfAssessment"
                                                value={annotatorFilter.selfAssessment || ''}
                                                label="Minimum self-assessment grade"
                                                onChange={this.handleAnnotatorFilterChange}
                                            />
                                        </FormControl>
                                    </Col>
                                </Row>
                                <Stack direction="row" spacing={2}>
                                    <Button color="primary" onClick={() => this.handleFindAnnotators()}>Search</Button>{' '}
                                    <Button color="primary" onClick={() => this.handleResetAnnotators()}>Reset</Button>
                                </Stack>
                            </Form>
                        </Col>
                    </Row>
                    <Row className='mt-5'>
                        <Col>
                            <h3>Available supervisors</h3>
                            <TableContainer component={Paper}>
                                <Table aria-label="simple table">
                                    <TableHead>
                                        <TableRow>
                                            <TableCell width="30%">Username</TableCell>
                                            <TableCell width="30%" align={"right"}>Years in practice</TableCell>
                                            <TableCell width="30%" align={"right"}>Self-Assessment</TableCell>
                                            <TableCell align={"right"} width="10%">Actions</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {availableSupervisorList}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        </Col>
                        <Col>
                            <h3>Selected supervisors</h3>
                            <TableContainer component={Paper}>
                                <Table aria-label="simple table">
                                    <TableHead>
                                        <TableRow>
                                            <TableCell width="30%">Username</TableCell>
                                            <TableCell width="30%" align={"right"}>Years in practice</TableCell>
                                            <TableCell width="30%" align={"right"}>Self-Assessment</TableCell>
                                            <TableCell align={"right"} width="10%">Actions</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {supervisorList}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        </Col>
                    </Row>
                </TabPanel>
                <TabPanel value={tabValue} index={1}>
                    <Row className='mt-5'>
                        <Col>
                            <h3>Find annotators</h3>
                        </Col>
                    </Row>
                    <Row>
                        <Col>

                            <Row>
                                <Col>
                                    <FormControl fullWidth sx={{ mt: 5 }}>
                                        <TextField
                                            type="number"
                                            id="yearsInPractice"
                                            name="yearsInPractice"
                                            value={annotatorFilter.yearsInPractice || ''}
                                            label="Minimum years in practice"
                                            onChange={this.handleAnnotatorFilterChange}
                                        />
                                    </FormControl>

                                </Col>
                                <Col>
                                    <FormControl fullWidth sx={{ mt: 5 }}>
                                        <TextField
                                            type="number"
                                            id="selfAssessment"
                                            name="selfAssessment"
                                            value={annotatorFilter.selfAssessment || ''}
                                            label="Minimum self-assessment grade"
                                            onChange={this.handleAnnotatorFilterChange}
                                        />
                                    </FormControl>
                                </Col>
                            </Row>
                            <Stack direction="row" spacing={2}>
                                <Button color="primary" onClick={() => this.handleFindAnnotators()}>Search</Button>
                                <Button color="primary" onClick={() => this.handleResetAnnotators()}>Reset</Button>
                            </Stack>

                        </Col>
                    </Row>
                    <Row className='mt-5'>
                        <Col>
                            <h3>Available annotators</h3>
                            <TableContainer component={Paper}>
                                <Table aria-label="simple table">
                                    <TableHead>
                                        <TableRow>
                                            <TableCell width="30%">Username</TableCell>
                                            <TableCell width="30%" align={"right"}>Years in practice</TableCell>
                                            <TableCell width="30%" align={"right"}>Self-Assessment</TableCell>
                                            <TableCell align={"right"} width="10%">Actions</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {availableAnnotatorList}
                                    </TableBody>
                                </Table>
                            </TableContainer>

                        </Col>
                        <Col>
                            <h3>Selected annotators</h3>
                            <TableContainer component={Paper}>
                                <Table aria-label="simple table">
                                    <TableHead>
                                        <TableRow>
                                            <TableCell width="30%">Username</TableCell>
                                            <TableCell width="30%" align={"right"}>Years in practice</TableCell>
                                            <TableCell width="30%" align={"right"}>Self-Assessment</TableCell>
                                            <TableCell align={"right"} width="10%">Actions</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {annotatorList}
                                    </TableBody>
                                </Table>
                            </TableContainer>

                        </Col>
                    </Row>
                </TabPanel>
                <TabPanel value={tabValue} index={2}>
                    <Row className='mt-5'>
                        <Col>
                            <h3>Find reviewers</h3>
                        </Col>
                    </Row>
                    <Row>
                        <Col>
                            <Form>
                                <Row>
                                    <Col>
                                        <FormControl fullWidth sx={{ mt: 5 }}>
                                            <TextField
                                                type="number"
                                                id="yearsInPractice"
                                                name="yearsInPractice"
                                                value={annotatorFilter.yearsInPractice || ''}
                                                label="Minimum years in practice"
                                                onChange={this.handleAnnotatorFilterChange}
                                            />
                                        </FormControl>

                                    </Col>
                                    <Col>
                                        <FormControl fullWidth sx={{ mt: 5 }}>
                                            <TextField
                                                type="number"
                                                id="selfAssessment"
                                                name="selfAssessment"
                                                value={annotatorFilter.selfAssessment || ''}
                                                label="Minimum self-assessment grade"
                                                onChange={this.handleAnnotatorFilterChange}
                                            />
                                        </FormControl>
                                    </Col>
                                </Row>
                                <Stack direction="row" spacing={2}>
                                    <Button color="primary" onClick={() => this.handleFindAnnotators()}>Search</Button>{' '}
                                    <Button color="primary" onClick={() => this.handleResetAnnotators()}>Reset</Button>
                                </Stack>
                            </Form>
                        </Col>
                    </Row>
                    <Row className='mt-5'>
                        <Col>
                            <h3>Available reviewers</h3>
                            <TableContainer component={Paper}>
                                <Table aria-label="simple table">
                                    <TableHead>
                                        <TableRow>
                                            <TableCell width="30%">Username</TableCell>
                                            <TableCell width="30%" align={"right"}>Years in practice</TableCell>
                                            <TableCell width="30%" align={"right"}>Self-Assessment</TableCell>
                                            <TableCell align={"right"} width="10%">Actions</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {availableReviewerList}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        </Col>
                        <Col>
                            <h3>Selected reviewers</h3>
                            <TableContainer component={Paper}>
                                <Table aria-label="simple table">
                                    <TableHead>
                                        <TableRow>
                                            <TableCell width="30%">Username</TableCell>
                                            <TableCell width="30%" align={"right"}>Years in practice</TableCell>
                                            <TableCell width="30%" align={"right"}>Self-Assessment</TableCell>
                                            <TableCell align={"right"} width="10%">Actions</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {reviewerList}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        </Col>
                    </Row>
                </TabPanel>
                </Box>





            </div>
        );
    }

}
export default CampaignUsers;
