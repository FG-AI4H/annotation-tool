import React, {Component} from "react";
import {Link} from "react-router-dom";
import Button from "react-bootstrap/Button";
import AppNavbar from "./AppNavbar";
import Container from "react-bootstrap/Container";
import {FaRedo} from "react-icons/fa";

import UserListComponent from "./components/UserListComponent";

class UserList extends Component {

    constructor(props) {
        super(props);
        this.state = {users: [], isLoading: false};
    }

    render(){


        return (
            <div>
                <AppNavbar/>
                <Container className={'pt-5'}>
                    <div className={'float-end'}>
                        <Button variant={'light'} onClick={() => this.componentDidMount()}><FaRedo /></Button>{' '}
                        <Button variant="success" tag={Link} to="/users/new">Add User</Button>
                    </div>
                    <UserListComponent title="Users"/>
                </Container>
            </div>
        );
    }
}
export default UserList;
