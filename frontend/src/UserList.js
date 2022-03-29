import React from "react";
import {Link} from "react-router-dom";
import AppNavbar from "./AppNavbar";
import Container from "react-bootstrap/Container";
import {FaRedo} from "react-icons/fa";

import UserListComponent from "./components/UserListComponent";
import {Button, IconButton, Paper} from "@mui/material";
import Grid from "@material-ui/core/Grid";
import {ociStyles} from "./customStyle";


const UserList = () => {

    const classes = ociStyles();

    return (
        <div>
            <AppNavbar/>
            <Container maxWidth="lg" className={classes.container}>
                <Grid container spacing={3}>
                    <Grid item xs={12}>
                        <Paper className={classes.paper}>
                            <UserListComponent title="Users"/>
                        </Paper>
                    </Grid>
                </Grid>
            </Container>
        </div>
    );

}
export default UserList;
