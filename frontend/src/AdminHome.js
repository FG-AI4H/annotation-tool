import React, {Component} from "react";
import AppNavbar from "./AppNavbar";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import {Button, Card, CardActions, CardContent, CardMedia, Container, Grid, Typography} from "@mui/material";
import { Link as RouterLink, MemoryRouter } from 'react-router-dom';

class AdminHome extends Component {
    render() {
        return (
            <>
                <AppNavbar/>
                <Container sx={{ mt: 5 }}>
                    <Row>
                        <Col><h1 className="header">Platform Administration</h1></Col>
                    </Row>
                    <Row>
                        <Col><h5>Please choose an option</h5></Col>
                    </Row>

                    <Row xs={1} md={2} className="g-4">
                        <Col>
                        <Card>
                            <CardContent>
                                <Typography gutterBottom variant="h5" component="div">
                                    Users & Groups
                                </Typography>
                                <Typography variant="body2" color="text.secondary">
                                    Manage users and groups, assign roles and more.
                                </Typography>

                            </CardContent>
                            <CardActions>

                               <Button component={RouterLink} color="primary" to="/userManagement">User management</Button>
                            </CardActions>
                        </Card>
                        </Col>
                    </Row>

                </Container>
            </>
        );
    }
}
export default AdminHome
