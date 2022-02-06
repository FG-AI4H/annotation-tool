import React, {Component} from 'react';
import Container from "react-bootstrap/Container";
import AppNavbar from './AppNavbar';
import {Link as RouterLink, Link, withRouter} from 'react-router-dom';
import AnnotationTaskList from "./AnmnotationTaskList";
import SampleList from "./SampleList";
import AnnotationList from "./AnnotationList";
import {Auth} from "aws-amplify";
import TaskClient from "./api/TaskClient";
import {
    Autocomplete, Button, ButtonGroup,
    Checkbox,
    FormControl,
    FormControlLabel,
    FormGroup,
    InputLabel,
    MenuItem,
    Select, Snackbar, Stack,
    TextField
} from "@mui/material";
import Alert from "@mui/material/Alert";

class TaskEdit extends Component {

    emptyItem = {
        name: '',
        email: ''
    };

    constructor(props) {
        super(props);
        this.state = {
            item: this.emptyItem,
            updated: false
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleClose = this.handleClose.bind(this);
    }

    async componentDidMount() {

        this.setState({ isLoading: true});
        if (this.props.match.params.id !== 'new') {
            Auth.currentAuthenticatedUser({
                bypassCache: false
            }).then(response => {
                const client = new TaskClient(response.signInUserSession.accessToken.jwtToken);

                client.fetchTaskById(this.props.match.params.id)
                    .then(
                        response =>
                            this.setState(
                                {item: response.data, isLoading: false}
                            ));

            }).catch(err => console.log(err));
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
            const client = new TaskClient(response.signInUserSession.accessToken.jwtToken);

            client.updateTask(item)
                .then(
                    response => this.setState(
                        {updated: true}
                    )
                );

        }).catch(err => console.log(err));


        this.props.history.push('/tasks');
    }

    render() {
        const {item, updated} = this.state;
        const title = <h2>{item.taskUUID ? 'Edit Task' : 'Add Task'}</h2>;

        return <div>
            <Snackbar open={updated} autoHideDuration={6000} onClose={this.handleClose} anchorOrigin={{
                vertical: 'top',
                horizontal: 'right'
            }}>
                <Alert severity="success" sx={{ width: '100%' }} onClose={this.handleClose}>
                    Campaign updated successfully!
                </Alert>
            </Snackbar>
            <AppNavbar/>
            <Container className={'pt-5'}>
                {title}

                <FormControl fullWidth sx={{ mt: 5 }}>
                        <InputLabel id="kind-label">Kind</InputLabel>
                        <Select
                            id="kind"
                            name="kind"
                            value={item.kind || ''}
                            label="Kind"
                            onChange={this.handleChange}
                        >
                            <MenuItem value={"create"}>Create</MenuItem>
                            <MenuItem value={"correct"}>Correct</MenuItem>
                        </Select>
                </FormControl>

                <FormControl fullWidth>
                    <FormControlLabel control={<Checkbox name="readOnly" id="readOnly" checked={item.readOnly} onChange={this.handleChange}/>} label="Read only" />
                </FormControl>

                <FormControl fullWidth>
                    <Autocomplete
                        disablePortal
                        id="combo-box-demo"
                        options={item?.campaign?.annotators}
                        getOptionLabel={(option) => option.username}
                        sx={{ width: 300 }}
                        renderInput={(params) => <TextField {...params} label="Assignee" />}
                    />
                </FormControl>




                    <AnnotationTaskList tasks={item.annotationTasks}/>
                    <SampleList samples={item.samples}/>
                    <AnnotationList annotations={item.annotations}/>

                <FormGroup sx={{ mt: 5 }}>

                    <Stack direction="row" spacing={2}>
                        <Button color="primary" onClick={e => this.handleSubmit(e)}>Save</Button>
                        <Button component={RouterLink} color="secondary" to={"/tasks"}>Cancel</Button>
                        <Button color="success" onClick={()=> window.open("https://dev.visian.org/?origin=who&taskId=" + item.taskUUID, "_blank")}>Annotate</Button>
                    </Stack>

                </FormGroup>



            </Container>
        </div>
    }
}
export default withRouter(TaskEdit);
