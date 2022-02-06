import React, {Component} from "react";
import Alert from "@mui/material/Alert";
import {
    Button,
    FormControl,
    Paper,
    Snackbar,
    Stack,
    Table, TableBody, TableCell,
    TableContainer,
    TableHead,
    TableRow,
    TextField
} from "@mui/material";
import {API, Auth, graphqlOperation} from "aws-amplify";
import CampaignClient from "../api/CampaignClient";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import { DataGrid } from '@mui/x-data-grid';
import {Link as RouterLink} from "react-router-dom";
import Datasets from "../Datasets";
import {listDatasets} from "../graphql/queries";

class CampaignData extends Component {

    constructor(props) {
        super(props);
        this.state = {isLoading: false, campaign: {}, updated: false, datasets: []};
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.handleClose = this.handleClose.bind(this);
    }

    componentDidMount() {
        this.setState({campaign: this.props.campaign});
        try {
            API.graphql(graphqlOperation(listDatasets)).then( datasetData =>
                this.setState({datasets: datasetData.data.listDatasets.items})
            );

        } catch (err) {
            console.log(err)
            console.log('error fetching datasets')
        }

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
        const {campaign, datasets, updated} = this.state;

        const columns = [
            {
                field: 'name',
                headerName: 'Name',
                width: 150,
                editable: false,
            },
            {
                field: 'description',
                headerName: 'Description',
                width: 600,
                editable: false,
            },
            {
                field: 'description',
                headerName: 'Description',
                width: 150,
                editable: false,
            },
            {
                field: 'createdAt',
                headerName: 'Created',
                width: 150,
                editable: false,
                valueFormatter: ({ value }) => { return (new Date(Date.parse(value))).toLocaleString(navigator.language)},
            },
            {
                field: 'updatedAt',
                headerName: 'Updated',
                width: 150,
                editable: false,
                valueFormatter: ({ value }) => { return (new Date(Date.parse(value))).toLocaleString(navigator.language)},
            }
        ];

        return (
            <div>
                <Snackbar open={updated} autoHideDuration={6000} onClose={this.handleClose} anchorOrigin={{
                    vertical: 'top',
                    horizontal: 'right'
                }}>
                    <Alert severity="success" sx={{ width: '100%' }} onClose={this.handleClose}>
                        Dataset updated successfully!
                    </Alert>
                </Snackbar>


                <Row className='mt-5'>
                    <Col>
                        <h3>Available datasets</h3>

                        <DataGrid
                            autoHeight
                            rows={datasets}
                            columns={columns}
                            pageSize={5}
                            rowsPerPageOptions={[5]}
                            checkboxSelection
                        />


                    </Col>
                </Row>


                <Stack direction="row" spacing={2}>
                    <Button color="primary" onClick={e => this.handleSubmit(e)}>Save</Button>
                    <Button component={RouterLink} color="secondary" to="/campaigns">Cancel</Button>
                </Stack>

            </div>
        );
    }
}
export default CampaignData;
