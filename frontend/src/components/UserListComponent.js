import React, {Component} from "react";
import Table from "react-bootstrap/Table";
import ButtonGroup from "react-bootstrap/ButtonGroup";
import {Link} from "react-router-dom";
import Button from "react-bootstrap/Button";
import {Auth} from "aws-amplify";
import UserClient from "../api/UserClient";
import Loader from "react-loader-spinner";

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
            return <tr key={user.userUUID}>
                <td style={{whiteSpace: 'nowrap'}}>{user.username}</td>
                <td style={{whiteSpace: 'nowrap'}}>{user.email}</td>
                <td>
                    <ButtonGroup >
                        <Link to={"/users/" + user.userUUID}><Button size="sm" variant="primary">Edit</Button></Link>{' '}
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <>
                <h3>{title}</h3>
                <Table className="mt-4">
                    <thead>
                    <tr>
                        <th width="45%">Username</th>
                        <th width="45%">Email</th>
                        <th width="10%">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {userList}
                    </tbody>
                </Table>
            </>
        );
    }
}
export default UserListComponent;
