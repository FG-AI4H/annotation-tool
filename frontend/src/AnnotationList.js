import React, {Component} from 'react';
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import {Link} from 'react-router-dom';
import ButtonGroup from "react-bootstrap/ButtonGroup";
import Table from "react-bootstrap/Table";

class AnnotationList extends Component {

    constructor(props) {
        super(props);
        this.state = {annotations: []};
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        this.setState({annotations: this.props.annotations});
    }

    componentDidUpdate(prevProps) {
        if (prevProps.annotations !== this.props.annotations) {
            this.setState({annotations: this.props.annotations});
        }
    }

    async remove(id) {
        await fetch(`http://localhost:8080/annotations/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedTasks = [...this.props.annotations].filter(i => i.annotationUUID !== id);
            this.setState({annotations: updatedTasks});
        });
    }

    render() {
        const annotations = this.state.annotations;

        if(!annotations){
            return (<div></div>)
        }

        const annotationList = annotations.map(annotation => {
            return <tr key={annotation.annotationUUID}>
                <td>{annotation.status}</td>
                <td>{new Intl.DateTimeFormat("en-GB", {
                    year: "numeric",
                    month: "long",
                    day: "2-digit"
                }).format(Date.parse(annotation.submittedAt))}</td>
                <td>{annotation.annotator.username}</td>
                <td>
                    <ButtonGroup >
                        <Link to={"/annotations/" + annotation.annotationUUID}><Button size="sm" variant="primary">Edit</Button></Link>{' '}
                        <Button size="sm" variant="danger" onClick={() => this.remove(annotation.annotationUUID)}>Delete</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <div>
                <Container className={'pt-5'}>

                    <h3>Annotations</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="10%">Status</th>
                            <th width="30%">Submitted At</th>
                            <th width="30%">Annotator</th>
                            <th width="30%">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {annotationList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );
    }
}
export default AnnotationList;