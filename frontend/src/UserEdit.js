import React, {Component} from "react";
import AppNavbar from "./AppNavbar";
import Container from "react-bootstrap/Container";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import {Link, withRouter} from "react-router-dom";
import {Auth} from "aws-amplify";
import UserClient from "./api/UserClient";

class UserEdit extends Component {

    emptyItem = {
        name: '',
        email: ''
    };

    constructor(props) {
        super(props);
        this.state = {
            item: this.emptyItem,
            isLoading: false
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    async componentDidMount() {
        if (this.props.match.params.id !== 'new') {
            this.setState({isLoading: true});
            Auth.currentAuthenticatedUser({
                bypassCache: false
            }).then(response => {
                const client = new UserClient(response.signInUserSession.accessToken.jwtToken);
                client.fetchUserById(this.props.match.params.id)
                    .then(
                        response =>
                            this.setState(
                                {item: response?.data, isLoading: false}
                            ));
            }).catch(err => console.log(err));
        }
    }

    handleChange(event) {
        const target = event.target;
        let value = target.value;
        const name = target.name;

        if(target.type === 'checkbox'){
            value = target.checked;
        }

        let item = {...this.state.item};
        item[name] = value;
        this.setState({item});
    }

    async handleSubmit(event) {
        event.preventDefault();
        const {item} = this.state;

        Auth.currentAuthenticatedUser({
            bypassCache: false
        }).then(response => {
            const client = new UserClient(response.signInUserSession.accessToken.jwtToken);
            if(item.userUUID) {
                client.updateUser(item)
                    .then(
                        response =>
                            this.setState(
                                {item: response?.data, isLoading: false}
                            ));
            }
            else{
                client.addUser(item)
                    .then(
                        response =>
                            this.setState(
                                {item: response?.data, isLoading: false}
                            ));
            }
        }).catch(err => console.log(err));


        this.props.history.push('/userManagement');
    }

    render() {
        const {item} = this.state;
        const title = <h2>{item.userUUID ? 'Edit User' : 'Add User'}</h2>;

        return <div>
            <AppNavbar/>
            <Container className={'pt-5'}>
                {title}
                <Form onSubmit={this.handleSubmit}>
                    <Form.Group className={'py-2'}>
                        <Form.Label htmlFor="kind">Username</Form.Label>
                        <Form.Control type="text" name="username" id="username" value={item.username} onChange={this.handleChange}/>
                    </Form.Group>

                    <Form.Group>
                        <Button color="primary" type="submit">Save</Button>{' '}
                        <Link to="/users"><Button color="secondary" >Cancel</Button></Link>{' '}
                    </Form.Group>
                </Form>
            </Container>
        </div>
    }
}
export default withRouter(UserEdit);
