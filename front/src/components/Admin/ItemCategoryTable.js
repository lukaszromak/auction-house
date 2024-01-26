import { Row, Col, Table, Button, Form, Alert } from "react-bootstrap";

function ItemCategoryTable(props) {
    const {itemCategories, categoryName, handleInputChange, categoryValidated, errorMessage, handleCreateCategory, handleDeleteCategory} = props

    return (
        <Row className="pt-5">
            <h2>Item Categories</h2>
            <Table>
                <thead>
                    <tr>
                        <th></th>
                        <th>Category ID</th>
                        <th>Category Name</th>
                    </tr>
                </thead>
                <tbody>
                    {itemCategories.map((itemCategory => (
                        <tr key={itemCategory.id}>
                            <td><Button variant="danger" onClick={() => handleDeleteCategory(itemCategory.id)}>Delete</Button></td>
                            <td>{itemCategory.id}</td>
                            <td>{itemCategory.name}</td>
                        </tr>
                    )))}
                </tbody>
            </Table>
            <Form noValidate validated={categoryValidated} onSubmit={handleCreateCategory}>
                <Form.Control 
                    type="text"
                    name="categoryName"
                    value={categoryName} 
                    required
                    onChange={(e) => handleInputChange(e)} 
                    minLength={1} 
                    maxLength={30}/>
                <Form.Control.Feedback type="invalid">Enter a category name with 1-30 characters.</Form.Control.Feedback>
                <Button type="submit" className="mt-1">Add category</Button>
            </Form>
            <Col className="mt-1">
                {errorMessage !== "" && <Alert variant="danger">{errorMessage}</Alert>}
            </Col>
        </Row>
    );
}

export default ItemCategoryTable;