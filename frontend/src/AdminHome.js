import React, {Component} from "react";
import AppNavbar from "./AppNavbar";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Container from "@material-ui/core/Container";
import Card from "react-bootstrap/Card";
import {Link} from "react-router-dom";
import Button from "react-bootstrap/Button";
import CardGroup from "react-bootstrap/CardGroup";

class AdminHome extends Component {
    render() {
        return (
            <>
                <AppNavbar/>
                <Container className={'pt-5'}>
                    <Row>
                        <Col><h1 className="header">Platform Administration</h1></Col>
                    </Row>
                    <Row>
                        <Col><h5>Please choose an option</h5></Col>
                    </Row>

                    <CardGroup className={'gap-3'}>
                        <Card style={{ width: '18rem' }} >

                            <Card.Header as="h5">Users & Groups</Card.Header>
                            <Card.Body>

                                <Card.Text>
                                    Manage users and groups, assign roles and more.
                                </Card.Text>
                                <Link to="/userManagement"><Button variant="primary">User management</Button></Link>
                            </Card.Body>
                        </Card>
                    </CardGroup>

                </Container>
            </>
        );
    }
}
export default AdminHome
