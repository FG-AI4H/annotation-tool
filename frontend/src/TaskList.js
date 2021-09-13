import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';

class TaskList extends Component {

    constructor(props) {
        super(props);
        this.state = {tasks: []};
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        fetch('/tasks')
            .then(response => response.json())
            .then(data => this.setState({tasks: data._embedded.task}));
    }

    async remove(id) {
        await fetch(`/tasks/${id}`, {
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
        const {tasks, isLoading} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        const taskList = tasks.map(task => {
            return <tr key={task.taskUUID}>
                <td style={{whiteSpace: 'nowrap'}}>{task.kind}</td>
                <td>{task.email}</td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="primary" tag={Link} to={"/tasks/" + task.taskUUID}>Edit</Button>
                        <Button size="sm" color="danger" onClick={() => this.remove(task.taskUUID)}>Delete</Button>
                        <Button size="sm" color="success" onClick={()=> window.open("https://app.visian.org/?origin=who&taskId=\" + task.taskUUID", "_blank")}>Annotate</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <div className="float-right">
                        <Button color="success" tag={Link} to="/tasks/new">Add Task</Button>
                    </div>
                    <h3>Tasks</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="30%">Name</th>
                            <th width="30%">Email</th>
                            <th width="40%">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {taskList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );
    }
}
export default TaskList;
