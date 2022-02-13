import React, {Component} from 'react';
import Container from "react-bootstrap/Container";
import Loader from "react-loader-spinner";
import {Button, Paper, Stack, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";
import {Link as RouterLink} from "react-router-dom";

class AnnotationTaskList extends Component {

    constructor(props) {
        super(props);
        this.state = {tasks: []};
        this.remove = this.remove.bind(this);
    }


    async remove(id) {
        await fetch(`https://annotation.ai4h.net/annotationtasks/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedTasks = [...this.state.tasks].filter(i => i.id !== id);
            this.setState({tasks: updatedTasks});
        });
    }

    render() {
        const tasks = this.props.tasks;

        if(!tasks){
            return (<div className="loading"><Loader
                type="ThreeDots"
                color="#00a5e3"
                height={80}
                width={80}
                timeout={3000} //3 secs
            /></div>);
        }

        const taskList = tasks.map(task => {
            return <TableRow key={task.annotationTaskUUID} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                <TableCell style={{whiteSpace: 'nowrap'}}>{task.kind}</TableCell>
                <TableCell style={{whiteSpace: 'nowrap'}}>{task.title}</TableCell>
                <TableCell style={{whiteSpace: 'nowrap'}}>{task.description}</TableCell>
                <TableCell>
                    <Stack direction={"row"} spacing={2} justifyContent="flex-end">
                        <Button component={RouterLink} size="small" to={"/annotations/" + task.annotationTaskUUID}>Edit</Button>
                        <Button component={RouterLink} size="small" color={"error"} onClick={() => this.remove(task.annotationTaskUUID)}>Delete</Button>
                    </Stack>

                </TableCell>
            </TableRow>
        });

        return (
            <div>
                <Container className={'pt-5'}>

                    <h3>Annotation Tasks</h3>
                    <TableContainer component={Paper}>
                        <Table sx={{ minWidth: 650 }} aria-label="simple table">
                            <TableHead>
                                <TableRow>
                                    <TableCell width={"20%"}>Kind</TableCell>
                                    <TableCell width={"20%"}>Title</TableCell>
                                    <TableCell width={"30%"}>Description</TableCell>
                                    <TableCell width={"30%"} align={"right"}>Actions</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {taskList}
                            </TableBody>
                        </Table>
                    </TableContainer>

                </Container>
            </div>
        );
    }
}
export default AnnotationTaskList;
