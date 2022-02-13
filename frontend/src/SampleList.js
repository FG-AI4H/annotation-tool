import React, {Component} from 'react';
import Container from "react-bootstrap/Container";
import Form from "react-bootstrap/Form";
import {Button, Paper, Stack, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";
import {Link as RouterLink} from "react-router-dom";

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
            return <TableRow key={sample.annotationUUID} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                <TableCell style={{whiteSpace: 'nowrap'}}>{sample.title}</TableCell>
                <TableCell style={{whiteSpace: 'nowrap'}}><Form.Group controlId="formFile" className="mb-3">
                    <Form.Control type="file" />
                </Form.Group></TableCell>
                <TableCell>
                    <Stack direction={"row"} spacing={2} justifyContent="flex-end">
                        <Button component={RouterLink} size="small" to={"/samples/" + sample.sampleUUID}>Edit</Button>
                        <Button component={RouterLink} size="small" color={"error"} onClick={() => this.remove(sample.sampleUUID)}>Delete</Button>
                        <Button component={RouterLink} size="small" color={"info"} to={"/imageViewer/" + sample.sampleUUID}>View</Button>
                    </Stack>

                </TableCell>
            </TableRow>


        });

        return (
            <div>
                <Container className={'pt-5'}>

                    <h3>Samples</h3>
                    <TableContainer component={Paper}>
                        <Table sx={{ minWidth: 650 }} aria-label="simple table">
                            <TableHead>
                                <TableRow>
                                    <TableCell width={"40%"}>Title</TableCell>
                                    <TableCell width={"30%"}>Data</TableCell>
                                    <TableCell width={"30%"} align={"right"}>Actions</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {sampleList}
                            </TableBody>
                        </Table>
                    </TableContainer>

                </Container>
            </div>
        );
    }
}
export default SampleList;
