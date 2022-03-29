import React, {Component} from "react";
import UserList from "./UserList";
import {Link as RouterLink, Link} from "react-router-dom";
import {Button} from "@mui/material";

class UserManagement extends Component {

    render() {
        return (
            <>
                <UserList/>
                <div className="container">
                   <Button component={RouterLink} color="secondary" to={"/admin"}>Back</Button>
                </div>
            </>
        );
    }
}
export default UserManagement;
