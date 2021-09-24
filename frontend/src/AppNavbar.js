import React, {Component} from 'react';
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';
import NavDropdown from 'react-bootstrap/NavDropdown';
import Container from 'react-bootstrap/Container';

import {Link} from 'react-router-dom';
import {Auth, Sign} from "aws-amplify";
import {AmplifySignOut} from "@aws-amplify/ui-react";
import {FaUser} from "react-icons/all";


export default class AppNavbar extends Component {
    constructor(props) {
        super(props);
        this.state = {isOpen: false};
        this.toggle = this.toggle.bind(this);
    }

    toggle() {
        this.setState({
            isOpen: !this.state.isOpen
        });
    }

    componentDidMount() {
        Auth.currentAuthenticatedUser({
            bypassCache: false
        }).then(data => this.setState({ username: data.username, ...data.attributes, }))
            .catch(err => console.log(err));
    }

    async signOut() {
        try {
            await Auth.signOut();
        } catch (error) {
            console.log('error signing out: ', error);
        }
    }

    render() {

        const {username, isLoading} = this.state;

        return  <Navbar collapseOnSelect expand="lg" bg="dark" variant="dark">
            <Container>
                <Navbar.Brand as={Link} to="/">
                    <img
                        alt=""
                        src="/AI4H_logo_blue_transparent.png"
                        height="30"
                        className="d-inline-block align-top"
                    />{' '}
                    FG-AI4H Platform

                </Navbar.Brand>
                <Navbar.Toggle aria-controls="responsive-navbar-nav" />
                <Navbar.Collapse id="responsive-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link as={Link} to="/annotation">Annotation</Nav.Link>
                        <Nav.Link as={Link} to="/dashboard">Data Store</Nav.Link>
                        <Nav.Link as={Link} to="/benchmark">Benchmarking</Nav.Link>
                    </Nav>
                    <Nav>
                        <Nav.Link><FaUser/></Nav.Link>
                        <NavDropdown title={username} id="collasible-nav-dropdown">
                            <NavDropdown.Item as={Link} to="/profile">Profile</NavDropdown.Item>
                        </NavDropdown>
                    </Nav>
                    <Nav>

                        <Nav.Link><AmplifySignOut/></Nav.Link>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>

    }
}
