import React, {Component} from 'react';
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import {Link} from 'react-router-dom';
import ButtonGroup from "react-bootstrap/ButtonGroup";
import Table from "react-bootstrap/Table";
import Loader from "react-loader-spinner";

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
            return <tr key={task.annotationTaskUUID}>
                <td>{task.kind}</td>
                <td>{task.title}</td>
                <td>{task.description}</td>
                <td>
                    <ButtonGroup >
                        <Link to={"/annotationtasks/" + task.annotationTaskUUID}><Button size="sm" variant="primary">Edit</Button></Link>{' '}
                        <Button size="sm" variant="danger" onClick={() => this.remove(task.annotationTaskUUID)}>Delete</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <div>
                <Container className={'pt-5'}>

                    <h3>Annotation Tasks</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="20%">Kind</th>
                            <th width="20%">Title</th>
                            <th width="30%">Description</th>
                            <th width="30%">Actions</th>
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
export default AnnotationTaskList;
