import React, { Component } from 'react';
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import AppNavbar from './AppNavbar';
import { Link,withRouter } from 'react-router-dom';
import Form from "react-bootstrap/Form";

class CampaignEdit extends Component {

    emptyItem = {
        name: '',
        email: ''
    };

    constructor(props) {
        super(props);
        this.state = {
            item: this.emptyItem
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    async componentDidMount() {
        if (this.props.match.params.id !== 'new') {
            const campaign = await (await fetch(`/campaigns/${this.props.match.params.id}`)).json();
            this.setState({item: campaign});
        }
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let item = {...this.state.item};
        item[name] = value;
        this.setState({item});
    }

    async handleSubmit(event) {
        event.preventDefault();
        const {item} = this.state;

        await fetch('/campaigns' + (item.id ? '/' + item.id : ''), {
            method: (item.id) ? 'PUT' : 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item),
        });
        this.props.history.push('/campaigns');
    }

    render() {
        const {item} = this.state;
        const title = <h2>{item.id ? 'Edit Campaign' : 'Add Campaign'}</h2>;

        return <div>
            <AppNavbar/>
            <Container>
                {title}
                <Form onSubmit={this.handleSubmit}>
                    <Form.Group>
                        <Form.Label for="name">Name</Form.Label>
                        <Form.Control type="text" name="name" id="name" value={item.name || ''}
                               onChange={this.handleChange} autoComplete="name"/>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label for="email">Email</Form.Label>
                        <Form.Control type="text" name="email" id="email" value={item.email || ''}
                               onChange={this.handleChange} autoComplete="email"/>
                    </Form.Group>
                    <Form.Group>
                        <Button color="primary" type="submit">Save</Button>{' '}
                        <Button color="secondary" tag={Link} to="/campaigns">Cancel</Button>
                    </Form.Group>
                </Form>
            </Container>
        </div>
    }
}
export default withRouter(CampaignEdit);
