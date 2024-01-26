import { Row, Col, Table, Button, Form, Alert } from "react-bootstrap";

function ItemProducerTable(props) {
    const {itemProducers, producerName, handleInputChange, producerValidated, errorMessage, handleCreateProducer, handleDeleteProducer} = props

    return (
        <Row className="pt-5">
            <h2>Item Producers</h2>
            <Table>
            <thead>
                    <tr>
                        <th></th>
                        <th>Producer ID</th>
                        <th>Producer Name</th>
                    </tr>
                </thead>
                <tbody>
                    {itemProducers.map((itemProducer => (
                        <tr key={itemProducer.id}>
                            <td><Button variant="danger" onClick={() => handleDeleteProducer(itemProducer.id)}>Delete</Button></td>
                            <td>{itemProducer.id}</td>
                            <td>{itemProducer.name}</td>
                        </tr>
                    )))}
                </tbody>
            </Table>
            <Form noValidate validated={producerValidated} onSubmit={handleCreateProducer}>
                <Form.Control 
                    type="text"
                    name="producerName"
                    value={producerName} 
                    required
                    onChange={(e) => handleInputChange(e)}
                    minLength={1}
                    maxLength={30}/>
                <Form.Control.Feedback type="invalid">Enter a producer name with 1-30 characters.</Form.Control.Feedback>
                <Button type="submit" className="mt-1">Add producer</Button>
            </Form>
            <Col className="mt-1">
                {errorMessage !== "" && <Alert variant="danger">{errorMessage}</Alert>}
            </Col>
        </Row>
    );
}

export default ItemProducerTable;