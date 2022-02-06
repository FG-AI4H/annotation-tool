import React, {Component} from "react";
import Alert from "@mui/material/Alert";
import {Snackbar} from "@mui/material";
import {Auth} from "aws-amplify";
import CampaignClient from "../api/CampaignClient";

class CampaignTask extends Component {

    constructor(props) {
        super(props);
        this.state = {isLoading: false, campaign: {}, updated: false};
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.handleClose = this.handleClose.bind(this);
    }

    componentDidMount() {
        this.setState({campaign: this.props.campaign});
    }

    componentDidUpdate(prevProps) {
        if (prevProps.campaign !== this.props.campaign) {
            this.setState({campaign: this.props.campaign});
        }
    }

    handleClose(event, reason){
        if (reason === 'clickaway') {
            return;
        }

        this.setState(
            {updated: false}
        );
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;

        if(target.type === 'checkbox'){
            value = target.checked;
        }

        let campaign = {...this.state.campaign};
        campaign[name] = value;
        this.setState({campaign: campaign});
    }

    async handleSubmit(event) {
        event.preventDefault();
        const {campaign} = this.state;

        Auth.currentAuthenticatedUser({
            bypassCache: false
        }).then(response => {
            const client = new CampaignClient(response.signInUserSession.accessToken.jwtToken);
            if(campaign.campaignUUID) {
                client.updateCampaign(campaign)
                    .then(
                        response => this.setState(
                            {updated: true}
                        ))
            }
            else{
                client.addCampaign(campaign)
                    .then(
                        response => {
                            this.setState(
                                {campaign: response?.data, isLoading: false}
                            );

                        });
            }
        }).catch(err => console.log(err));

    }

    render() {
        const {campaign, updated} = this.state;

        return (
            <div>
                <Snackbar open={updated} autoHideDuration={6000} onClose={this.handleClose} anchorOrigin={{
                    vertical: 'top',
                    horizontal: 'right'
                }}>
                    <Alert severity="success" sx={{ width: '100%' }} onClose={this.handleClose}>
                        Campaign updated successfully!
                    </Alert>
                </Snackbar>

            </div>
        );
    }
}
export default CampaignTask;
