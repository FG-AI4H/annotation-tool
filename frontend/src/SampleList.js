import React, {Component} from 'react';
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import {Link} from 'react-router-dom';
import ButtonGroup from "react-bootstrap/ButtonGroup";
import Table from "react-bootstrap/Table";
import Form from "react-bootstrap/Form";

class SampleList extends Component {

    constructor(props) {
        super(props);
        this.state = {samples: []};
        this.remove = this.remove.bind(this);
    }


    async remove(id) {
        await fetch(`https://annotation.ai4h.net/samples/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedTasks = [...this.state.samples].filter(i => i.id !== id);
            this.setState({samples: updatedTasks});
        });
    }

    render() {
        const samples = this.props.samples;

        if(!samples){
            return (<div></div>)
        }

        const sampleList = samples.map(sample => {
            return <tr key={sample.sampleUUID}>
                <td>{sample.title}</td>
                <td><Form.Group controlId="formFile" className="mb-3">
                    <Form.Control type="file" />
                </Form.Group></td>
                <td>
                    <ButtonGroup >
                        <Link to={"/samples/" + sample.sampleUUID}><Button size="sm" variant="primary">Edit</Button></Link>{' '}
                        <Button size="sm" variant="danger" onClick={() => this.remove(sample.sampleUUID)}>Delete</Button>
                        <Link to={"/imageViewer/" + sample.sampleUUID}><Button size="sm" variant="success">View</Button></Link>{' '}
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <div>
                <Container className={'pt-5'}>

                    <h3>Samples</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="40%">Title</th>
                            <th width="30%">Data</th>
                            <th width="30%">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {sampleList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );
    }
}
export default SampleList;
