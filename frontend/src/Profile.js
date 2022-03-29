import {Auth} from "aws-amplify";
import React, {Component} from 'react';
import AppNavbar from "./AppNavbar";
import Container from "react-bootstrap/Container";
import Loader from "react-loader-spinner";
import Form from "react-bootstrap/Form";
import {FaPlus} from 'react-icons/fa';

class Profile extends Component {
    constructor(props) {
        super(props);
        this.state = {isLoading: false};
        this.toggle = this.toggle.bind(this);
    }

    toggle() {
        this.setState({
            isOpen: !this.state.isOpen
        });
    }

    componentDidMount() {
        this.state = {isLoading: true};
        Auth.currentAuthenticatedUser({
            bypassCache: false
        }).then(data => this.setState({isLoading: false,username: data.username, ...data.attributes,}))
            .catch(err => console.log(err));
    }

    render() {

        const {isLoading, username} = this.state;

        if (isLoading) {
            return (<div className="loading"><Loader
                type="Puff"
                color="#00a5e3"
                height={100}
                width={100}
                timeout={3000} //3 secs
            /></div>);
        }

        return (
            <div>
                <AppNavbar/>
                <Container className={'pt-5'}>
                    <Form onSubmit={this.handleSubmit}>
            <div class="container mt-5 mb-5">
                <div class="row">
                    <div class="col-md-3 border-right">
                        <div class="d-flex flex-column align-items-center text-center p-3 py-5"><img class="rounded-circle mt-5" width="150px" src="https://st3.depositphotos.com/15648834/17930/v/600/depositphotos_179308454-stock-illustration-unknown-person-silhouette-glasses-profile.jpg"/><span class="font-weight-bold text-white">{username || ''}</span><span class="text-white-50">{this.state.email || ''}</span><span> </span></div>
                    </div>

                        <div class="col-md-5 border-right">
                            <div class="p-3 py-5">
                                <div class="d-flex justify-content-between align-items-center mb-3">
                                    <h4 class="text-right">Profile Settings</h4>
                                </div>
                                <div class="row mt-2">
                                    <div class="col-md-6"><Form.Label >Name</Form.Label><Form.Control type="text" class="form-control" placeholder="first name" value={this.state.username || ''} onChange={this.handleChange}/></div>
                                    <div class="col-md-6"><Form.Label >Surname</Form.Label><Form.Control type="text" class="form-control" value="" placeholder="surname"/></div>
                                </div>
                                <div class="row mt-3">
                                    <div class="col-md-12"><Form.Label >Mobile Number</Form.Label><Form.Control type="text" class="form-control" placeholder="enter phone number" value={this.state.phone_number || ''} onChange={this.handleChange}/></div>
                                    <div class="col-md-12"><Form.Label >Affiliation</Form.Label><Form.Control type="text" class="form-control" placeholder="enter affiliation" value=""/></div>
                                    <div class="col-md-12"><Form.Label >Postcode</Form.Label><Form.Control type="text" class="form-control" placeholder="enter postcode" value=""/></div>
                                    <div class="col-md-12"><Form.Label >State</Form.Label><Form.Control type="text" class="form-control" placeholder="enter state" value=""/></div>
                                    <div class="col-md-12"><Form.Label >Email</Form.Label><Form.Control type="text" class="form-control" placeholder="enter email" value={this.state.email || ''} onChange={this.handleChange}/></div>
                                    <div class="col-md-12"><Form.Label >Education</Form.Label><Form.Control type="text" class="form-control" placeholder="education" value=""/></div>
                                </div>
                                <div class="row mt-3">
                                    <div class="col-md-6"><Form.Label >Country</Form.Label><Form.Control type="text" class="form-control" placeholder="country" value=""/></div>
                                    <div class="col-md-6"><Form.Label >State/Region</Form.Label><Form.Control type="text" class="form-control" value="" placeholder="state"/></div>
                                </div>
                                <div class="mt-5 text-center"><button class="btn btn-primary profile-button" type="submit">Save Profile</button></div>
                            </div>
                        </div>

                    <div class="col-md-4">
                        <div class="p-3 py-5">
                            <div class="d-flex justify-content-between align-items-center experience"><span>Edit Expertise</span><span class="border px-3 p-1 add-experience"><FaPlus/>&nbsp;Expertise</span></div><br/>
                            <div class="col-md-12"><Form.Label >Experience in Annotation</Form.Label><Form.Control type="text" class="form-control" placeholder="experience" value=""/></div> <br/>
                            <div class="col-md-12"><Form.Label >Additional Details</Form.Label><Form.Control type="text" class="form-control" placeholder="additional details" value=""/></div>
                        </div>
                    </div>
                </div>
            </div>
                    </Form>
                </Container>
            </div>
        )
    }

}
export default Profile;
