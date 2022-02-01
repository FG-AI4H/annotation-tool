import React, {Component} from 'react';
import './App.css';
import AppNavbar from './AppNavbar';
import {Link} from 'react-router-dom';
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
                    <Row xs={1} md={2} className="g-4">
                        <Col>
                    <Card>

                        <Card.Header as="h5">Campaigns</Card.Header>
                        <Card.Body>

                            <Card.Text>
                                Manage annotation campaigns.
                            </Card.Text>
                            <Link to="/campaigns"><Button variant="primary">Campaigns</Button></Link>
                        </Card.Body>
                    </Card>
                        </Col>
<Col>
                    <Card>

                        <Card.Header as="h5">My Tasks</Card.Header>
                        <Card.Body>

                            <Card.Text>
                                View all my tasks and work on them.
                            </Card.Text>
                            <Link to="/myTasks"><Button variant="primary">My Tasks</Button></Link>
                        </Card.Body>
                    </Card>
</Col>
                    <Col>

                        <Card>

                            <Card.Header as="h5">Tasks</Card.Header>
                            <Card.Body>

                                <Card.Text>
                                    List all tasks and get statistics about them    .
                                </Card.Text>
                                <Link to="/tasks"><Button variant="primary">Tasks</Button></Link>
                            </Card.Body>
                        </Card>
                    </Col>
                    </Row>
                </Container>
            </div>
        );
    }
}
export default AnnotationHome;
