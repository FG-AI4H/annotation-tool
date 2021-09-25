import React, { Component } from 'react';
import './App.css';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Card from "react-bootstrap/Card";
import CardGroup from "react-bootstrap/CardGroup";


class Home extends Component {
    render() {
        return (
            <div>
                <AppNavbar/>
                <Container className={'pt-5'}>
                    <Row>
                        <Col><h1 className="header">Welcome To The FG-AI4H Platform</h1></Col>
                    </Row>
                    <Row>
                        <Col><h5>Welcome To The FG-AI4H Platform</h5></Col>
                    </Row>

                    <CardGroup className={'gap-3'}>
                        <Card style={{ width: '18rem' }} >

                            <Card.Img variant="top" src="https://picsum.photos/id/341/286/180" />
                            <Card.Body>
                                <Card.Title>Annotation Platform</Card.Title>
                                <Card.Text>
                                    High-quality annotated data provide the basis for supervised learning. Unfortunately, production is challenging and labor intensive. Certain features must be considered when evaluating an annotation: the quality of labels, the number of expert opinions, and the handling of non-unanimous decisions. This Annotation Platform brings together leading health experts across the globe to produce the highest-quality annotations at maximum efficiency.
                                </Card.Text>
                                <Link to="/annotation"><Button variant="primary">Start Annotation</Button></Link>
                            </Card.Body>
                        </Card>

                        <Card style={{ width: '18rem' }}>

                            <Card.Img variant="top" src="https://picsum.photos/id/1073/286/180" />
                            <Card.Body>
                                <Card.Title>Data Platform</Card.Title>
                                <Card.Text>
                                    Provides safe and secure storage of medical data; serves as an interface to access this data; and offers data governance that complies with data protection laws. Medical data storage requires adherence to strict guidelines that preserve the privacy and safety of patients. The Data Platform provides data storage guidelines that consider these constraints.
                                </Card.Text>
                                <Link to="/dashboard"><Button variant="primary">Manage Data</Button></Link>
                            </Card.Body>
                        </Card>

                        <Card style={{ width: '18rem' }}>

                            <Card.Img variant="top" src="https://picsum.photos/id/36/286/180" />
                            <Card.Body>
                                <Card.Title>Evaluation Platform</Card.Title>
                                <Card.Text>
                                    Evaluating AI models in health - The ITU/WHO Focus Group on artificial intelligence for health (FG-AI4H) works in partnership with the World Health Organization (WHO) to establish a standardized assessment framework for the evaluation of AI-based methods for health, diagnosis, triage or treatment decisions. The group was established by ITU-T Study Group 16 at its meeting in Ljubljana, Slovenia, 9-20 July 2018.
                                </Card.Text>
                                <Link to="/benchmark"><Button variant="primary">Start Evaluation</Button></Link>
                            </Card.Body>
                        </Card>
                    </CardGroup>
                </Container>
            </div>
        );
    }
}
export default Home;
