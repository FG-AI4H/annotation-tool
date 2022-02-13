import React, {Component} from "react";
import {Link as RouterLink, Link} from "react-router-dom";
import {Auth} from "aws-amplify";
import UserClient from "../api/UserClient";
import Loader from "react-loader-spinner";
import {
    Button,
    IconButton,
    Paper,
    Stack,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow
} from "@mui/material";
import Title from "../Title";
import Grid from "@material-ui/core/Grid";
import {FaRedo} from "react-icons/fa";

class UserListComponent extends Component {

    constructor(props) {
        super(props);
        this.state = {users: [], isLoading: false};
    }

    componentDidMount() {
        this.setState({isLoading: true});
        Auth.currentAuthenticatedUser({
            bypassCache: false
        }).then(response => {
            const client = new UserClient(response.signInUserSession.accessToken.jwtToken);
            client.fetchUserList()
                .then(
                    response =>
                        this.setState(
                            {users: response?.data._embedded.user, isLoading: false}
                        ));
        }).catch(err => console.log(err));
    }

    render() {
        const {title} = this.props;
        const {users, isLoading} = this.state;

        if (isLoading) {
            return (<div className="loading"><Loader
                type="Puff"
                color="#00a5e3"
                height={100}
                width={100}
                timeout={3000} //3 secs
            /></div>);
        }

        const userList = users.map(user => {
            return <TableRow key={user.idpID} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                <TableCell style={{whiteSpace: 'nowrap'}}>{user.username}</TableCell>
                <TableCell style={{whiteSpace: 'nowrap'}}>{user.email}</TableCell>
                <TableCell>
                    <Stack direction={"row"} spacing={2} justifyContent="flex-end">
                        <Button component={RouterLink} size="small" to={"/users/" + user.userUUID}>Edit</Button>
                    </Stack>

                </TableCell>
            </TableRow>
        });

        return (
            <>
                <Title>{title}</Title>
                <Grid container justifyContent="flex-end">
                    <IconButton onClick={() => this.componentDidMount()}><FaRedo/></IconButton>{' '}
                    <Button color="success" tag={Link} to="/users/new">Add User</Button>
                </Grid>
                <TableContainer component={Paper}>
                    <Table sx={{ minWidth: 650 }} aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <TableCell width={"45%"}>Username</TableCell>
                                <TableCell width={"45%"}>Email</TableCell>
                                <TableCell width={"10%"} align={"right"}>Actions</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {userList}
                        </TableBody>
                    </Table>
                </TableContainer>

            </>
        );
    }
}
export default UserListComponent;
