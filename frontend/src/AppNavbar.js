import React, {Component} from 'react';
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';
import NavDropdown from 'react-bootstrap/NavDropdown';
import Container from 'react-bootstrap/Container';

import {Link} from 'react-router-dom';


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

    render() {
        return  <Navbar collapseOnSelect expand="lg" bg="dark" variant="dark">
            <Container>
                <Navbar.Brand as={Link} to="/">
                    <img
                        alt=""
                        src="logo192.png"
                        width="30"
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
                        <NavDropdown title="Dropdown" id="collasible-nav-dropdown">
                            <NavDropdown.Item href="#action/3.1">Action</NavDropdown.Item>
                            <NavDropdown.Item href="#action/3.2">Another action</NavDropdown.Item>
                            <NavDropdown.Item href="#action/3.3">Something</NavDropdown.Item>
                            <NavDropdown.Divider />
                            <NavDropdown.Item href="#action/3.4">Separated link</NavDropdown.Item>
                        </NavDropdown>
                    </Nav>
                    <Nav>
                        <Nav.Link as={Link} to="/about">About us</Nav.Link>
                        <Nav.Link eas={Link} to="/join">
                            Join the team
                        </Nav.Link>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>

    }
}
