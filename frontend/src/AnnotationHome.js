import React, {Component} from 'react';
import './App.css';
import AppNavbar from './AppNavbar';
import {Link as RouterLink, Link} from 'react-router-dom';
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import {Button, Card, CardActions, CardContent, Typography} from "@mui/material";


class AnnotationHome extends Component {
    render() {
        return (
            <div>
                <AppNavbar/>
                <Container className={'pt-5'}>
                    <Row>
                        <Col><h1 className="header">Welcome To The FG-AI4H Annotation Tool</h1></Col>
                    </Row>
                    <Row>
                        <Col><h5>Please choose an option</h5></Col>
                    </Row>
                    <Row xs={1} md={2} className="g-4">
                        <Col>
                    <Card>
                        <CardContent>
                            <Typography gutterBottom variant="h5" component="div">
                                Campaigns
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                Manage annotation campaigns.
                            </Typography>

                        </CardContent>
                        <CardActions>
                            <Button component={RouterLink} variant="primary" to="/campaigns">Campaigns</Button>
                        </CardActions>
                    </Card>
                        </Col>
                        <Col>
                            <Card>
                                <CardContent>
                                    <Typography gutterBottom variant="h5" component="div">
                                        My Tasks
                                    </Typography>
                                    <Typography variant="body2" color="text.secondary">
                                        View all my tasks and work on them.
                                    </Typography>

                                </CardContent>
                                <CardActions>
                                    <Button component={RouterLink} variant="primary" to="/myTasks">My Tasks</Button>
                                </CardActions>
                            </Card>
                        </Col>
                    <Col>

                        <Card>
                            <CardContent>
                                <Typography gutterBottom variant="h5" component="div">
                                    Tasks
                                </Typography>
                                <Typography variant="body2" color="text.secondary">
                                    List all tasks and get statistics about them.
                                </Typography>

                            </CardContent>
                            <CardActions>
                                <Button component={RouterLink} variant="primary" to="/tasks">Tasks</Button>
                            </CardActions>
                        </Card>
                    </Col>
                    </Row>
                </Container>
            </div>
        );
    }
}
export default AnnotationHome;
