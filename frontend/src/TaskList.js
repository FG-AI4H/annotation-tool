import React, {Component} from 'react';
import Container from "react-bootstrap/Container";
import AppNavbar from './AppNavbar';
import {FaRedo} from 'react-icons/fa';
import Loader from "react-loader-spinner";
import {Auth} from "aws-amplify";
import TaskClient from "./api/TaskClient";
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
import {Link as RouterLink, Link} from "react-router-dom";

class TaskList extends Component {

    constructor(props) {
        super(props);
        this.state = {tasks: [], isLoading: false};
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        this.setState({ isLoading: true });

        Auth.currentAuthenticatedUser({
            bypassCache: false
        }).then(response => {
            const client = new TaskClient(response.signInUserSession.accessToken.jwtToken);
            if(this.props.me){
                client.fetchMyTaskList()
                    .then(
                        response =>
                            this.setState(
                                {tasks:  response?.data._embedded ? response?.data._embedded?.task : [], isLoading: false}
                            ));
            }
            else {
                client.fetchTaskList()
                    .then(
                        response =>
                            this.setState(
                                {tasks:  response?.data._embedded ? response?.data._embedded?.task : [], isLoading: false}
                            ));
            }
        }).catch(err => console.log(err));
    }

    async remove(id) {
        Auth.currentAuthenticatedUser({
            bypassCache: false
        }).then(response => {
            const client = new TaskClient(response.signInUserSession.accessToken.jwtToken);
            client.removeTask(id)
                .then(
                    response => {
                        let updatedTasks = [...this.state.tasks].filter(i => i.taskUUID !== id);
                        this.setState({tasks: updatedTasks});
                    });
        }).catch(err => console.log(err));
    }

    render() {
        const {tasks, isLoading} = this.state;
        const {me} = this.props;

        if (isLoading) {
            return (<div className="loading"><Loader
                type="Puff"
                color="#00a5e3"
                height={100}
                width={100}
                timeout={3000} //3 secs
            /></div>);
        }

        const taskList = tasks.map(task => {

            return <TableRow key={task.taskUUID} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                <TableCell style={{whiteSpace: 'nowrap'}}>{task.kind}</TableCell>
                <TableCell>{task.annotationTasks[0].kind}</TableCell>
                <TableCell>{task.annotationTasks[0].title}</TableCell>
                <TableCell>{task.annotationTasks[0].description}</TableCell>
                <TableCell align={"right"}>
                    <Stack direction={"row"} spacing={2} justifyContent="flex-end">
                        <Button component={RouterLink} size="small" color="primary" to={"/tasks/" + task.taskUUID}>Edit</Button>
                        <Button size="small" color="warning" onClick={() => this.remove(task.taskUUID)}>Delete</Button>
                        <Button size="small" color="success" onClick={()=> window.open("https://dev.visian.org/?origin=who&taskId=" + task.taskUUID, "_blank")}>Annotate</Button>
                    </Stack>
                </TableCell>
            </TableRow>
        });

        return (
            <div>
                <AppNavbar/>
                <Container className={'pt-5'}>

                        <div className={'float-end'}>
                            <IconButton onClick={() => this.componentDidMount()} size={"medium"}><FaRedo fontSize="inherit"/></IconButton>{' '}
                            {!me &&
                            <Button component={RouterLink} color="success" to={"/tasks/me"}>Add Task</Button>
                            }
                        </div>

                    <h3>{me ? 'My Tasks' : 'Tasks'}</h3>

                    <TableContainer component={Paper}>
                        <Table sx={{ minWidth: 650 }} aria-label="simple table">
                            <TableHead>
                                <TableRow>
                                    <TableCell>Kind</TableCell>
                                    <TableCell>Annotation type</TableCell>
                                    <TableCell>Title</TableCell>
                                    <TableCell>Description</TableCell>
                                    <TableCell align={"right"}>Actions</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {taskList.length > 0 ? taskList : <tr><td colSpan={5}>No task available</td></tr>
                                }
                            </TableBody>
                        </Table>
                    </TableContainer>


                    <Button component={RouterLink} color="secondary" to={"/annotation"}>Back</Button>
                </Container>
            </div>
        );
    }
}
export default TaskList;
