import React, {Component} from 'react';
import './App.css';
import AppNavbar from './AppNavbar';
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";

import {Button, Card, CardActions, CardContent, CardMedia, Container, Grid, Stack, Typography} from "@mui/material";
import {Link as RouterLink, Link} from "react-router-dom";


class Home extends Component {
    render() {
        return (
            <div>
                <AppNavbar/>
                <Container maxWidth="xl" sx={{ mt: 5 }}>
                    <Row>
                        <Col><h1 className="header">Welcome To The FG-AI4H Platform</h1></Col>
                    </Row>
                    <Row>
                        <Col><h5>Welcome To The FG-AI4H Platform</h5></Col>
                    </Row>

                    <Grid container
                          spacing={2}
                          direction="row"
                          justifyContent="space-evenly"
                          alignItems="stretch" sx={{ mt: 5 }}>
                        <Grid item xs={12} md={4}>
                            <Card>
                                <CardMedia
                                    component="img"
                                    height="180"
                                    image="https://picsum.photos/id/341/286/180"
                                />
                                <CardContent>
                                    <Typography gutterBottom variant="h5" component="div">
                                        Annotation Platform
                                    </Typography>
                                    <Typography variant="body2" color="text.secondary">
                                        High-quality annotated data provide the basis for supervised learning. Unfortunately, production is challenging and labor intensive. Certain features must be considered when evaluating an annotation: the quality of labels, the number of expert opinions, and the handling of non-unanimous decisions. This Annotation Platform brings together leading health experts across the globe to produce the highest-quality annotations at maximum efficiency.
                                    </Typography>

                                </CardContent>
                                <CardActions>
                                    <Button component={RouterLink} color="primary" to="/annotation">Start Annotation</Button>
                                </CardActions>
                            </Card>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <Card>
                                <CardMedia
                                    component="img"
                                    height="180"
                                    image="https://picsum.photos/id/1073/286/180"
                                />
                                <CardContent>
                                    <Typography gutterBottom variant="h5" component="div">
                                        Data Platform
                                    </Typography>
                                    <Typography variant="body2" color="text.secondary">
                                        Provides safe and secure storage of medical data; serves as an interface to access this data; and offers data governance that complies with data protection laws. Medical data storage requires adherence to strict guidelines that preserve the privacy and safety of patients. The Data Platform provides data storage guidelines that consider these constraints.
                                    </Typography>

                                </CardContent>
                                <CardActions>
                                    <Button component={RouterLink} color="primary" to="/dashboard">Manage Data</Button>
                                </CardActions>
                            </Card>
                        </Grid>
                        <Grid item xs={12} md={4}>
                        <Card>
                            <CardMedia
                                component="img"
                                height="180"
                                image="https://picsum.photos/id/36/286/180"
                            />
                            <CardContent>
                                <Typography gutterBottom variant="h5" component="div">
                                    Evaluation Platform
                                </Typography>
                                <Typography variant="body2" color="text.secondary">
                                    Evaluating AI models in health - The ITU/WHO Focus Group on artificial intelligence for health (FG-AI4H) works in partnership with the World Health Organization (WHO) to establish a standardized assessment framework for the evaluation of AI-based methods for health, diagnosis, triage or treatment decisions. The group was established by ITU-T Study Group 16 at its meeting in Ljubljana, Slovenia, 9-20 July 2018.
                                </Typography>

                            </CardContent>
                            <CardActions>
                                <Button component={RouterLink} color="primary" to="/benchmark">Start Evaluation</Button>
                            </CardActions>
                        </Card>
                        </Grid>
                    </Grid>

                </Container>
            </div>
        );
    }
}
export default Home;
