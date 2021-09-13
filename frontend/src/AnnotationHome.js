import React, { Component } from 'react';
import './App.css';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";


class AnnotationHome extends Component {
    render() {
        return (
            <div>
                <AppNavbar/>
                <Container fluid>

                    <Button variant="link"><Link to="/campaigns">Campaigns</Link></Button>
                    <Button variant="link"><Link to="/tasks">Tasks</Link></Button>

                </Container>
            </div>
        );
    }
}
export default AnnotationHome;
