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
                    <CardGroup className={'gap-3'}>
                    <Card style={{ width: '18rem' }} >

                        <Card.Header as="h5">Campaigns</Card.Header>
                        <Card.Body>

                            <Card.Text>
                                Some quick example text to build on the card title and make up the bulk of
                                the card's content.
                            </Card.Text>
                            <Link to="/campaigns"><Button variant="primary">Campaigns</Button></Link>
                        </Card.Body>
                    </Card>

                    <Card style={{ width: '18rem' }}>

                        <Card.Header as="h5">Tasks</Card.Header>
                        <Card.Body>

                            <Card.Text>
                                Some quick example text to build on the card title and make up the bulk of
                                the card's content.
                            </Card.Text>
                            <Link to="/tasks"><Button variant="primary">Tasks</Button></Link>
                        </Card.Body>
                    </Card>
                    </CardGroup>

                </Container>
            </div>
        );
    }
}
export default AnnotationHome;
