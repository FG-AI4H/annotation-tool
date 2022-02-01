import React, {Component} from "react";
import UserList from "./UserList";
import Button from "react-bootstrap/Button";
import {Link} from "react-router-dom";
import Container from "@material-ui/core/Container";

class UserManagement extends Component {

    render() {
        return (
            <>
                <UserList/>
                <div className="container">
                    <Link to="/admin"><Button color="secondary" >Back</Button></Link>
                </div>
            </>
        );
    }
}
export default UserManagement;
