import React, { Component } from 'react';
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import AppNavbar from './AppNavbar';
import { Link,withRouter } from 'react-router-dom';
import Form from "react-bootstrap/Form";
import AnnotationTaskList from "./AnmnotationTaskList";
import SampleList from "./SampleList";
import AnnotationList from "./AnnotationList";

class TaskEdit extends Component {

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
            const task = await (await fetch(`/tasks/${this.props.match.params.id}`)).json();
            this.setState({item: task});
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

        await fetch('/tasks' + (item.taskUUID ? '/' + item.taskUUID : ''), {
            method: (item.taskUUID) ? 'PUT' : 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item),
        });
        this.props.history.push('/tasks');
    }

    render() {
        const {item} = this.state;
        const title = <h2>{item.taskUUID ? 'Edit Task' : 'Add Task'}</h2>;

        return <div>
            <AppNavbar/>
            <Container className={'pt-5'}>
                {title}
                <Form onSubmit={this.handleSubmit}>
                    <Form.Group className={'py-2'}>
                        <Form.Label htmlFor="kind">Kind</Form.Label>
                        <Form.Select name="kind" id="kind" value={item.kind || ''} onChange={this.handleChange}>
                            <option>Open this select menu</option>
                            <option value="create">Create</option>
                            <option value="correct">Correct</option>
                        </Form.Select>
                    </Form.Group>

                    <Form.Group className={'py-2'}>
                        <Form.Check type={"checkbox"} label={"Read only"} name="readOnly" id="readOnly" checked={item.readOnly} onChange={this.handleChange}/>
                    </Form.Group>

                    <AnnotationTaskList tasks={item.annotationTasks}/>
                    <SampleList samples={item.samples}/>
                    <AnnotationList annotations={item.annotations}/>

                    <Form.Group>
                        <Button color="primary" type="submit">Save</Button>{' '}
                        <Link to="/tasks"><Button color="secondary" >Cancel</Button></Link>{' '}
                        <Button variant="success" onClick={()=> window.open("https://dev.visian.org/?origin=who&taskId=" + item.taskUUID, "_blank")}>Annotate</Button>
                    </Form.Group>
                </Form>
            </Container>
        </div>
    }
}
export default withRouter(TaskEdit);
