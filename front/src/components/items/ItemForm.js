import { useEffect, useState } from "react";
import { Form, Button, Container, Row, Col, Alert } from "react-bootstrap";
import { Navigate, useNavigate } from "react-router-dom";
import { MultiSelect } from "react-multi-select-component";
import { useAuth } from "../context/AuthContext";
import { Api } from "../misc/Api";
import { handleGetCategories, handleGetProducers } from "../misc/ItemHelpers";
import { responsivePropType } from "react-bootstrap/esm/createUtilityClasses";

function ItemForm() {
    const Auth = useAuth();
    const user = Auth.getUser();
    const navigate = useNavigate();
    const [validated, setValidated] = useState(false);
    const [name, setName] = useState('');
    const [startPrice, setStartPrice] = useState('');
    const [buyItNowPrice, setbuyItNowPrice] = useState('');
    const [description, setDescription] = useState('');
    const [expirationDate, setexpirationDate] = useState(Date());
    const [itemCategory, setItemCategory] = useState({});
    const [itemCategories, setItemCategories] = useState([]);
    const [itemProducers, setItemProducers] = useState([]);
    const [imageFile, setImageFile] = useState('');
    const [availableItemProducers, setAvailableItemProducers] = useState([]);
    const [failedToAdd, setFailedToAdd] = useState(false);
    const [failedToAddMessage, setFailedToAddMessage] = useState("");
    const [isPending, setIsPending] = useState(false);

    const handleSubmit = (event) => {
        event.preventDefault();
        event.stopPropagation();
        const form = event.currentTarget;

        if(form.checkValidity() === false){
            setValidated(true);
        } else {
            setIsPending(true);
            handleAddItem();
        }
    }

    const handleAddItem = async () => {
        let item = {}
        item.name = name;
        item.startPrice = startPrice;
        item.buyItNowPrice = buyItNowPrice;
        item.description = description;
        item.expirationDate = new Date(expirationDate).toISOString();
        item.itemCategory = itemCategory;
        item.itemProducers = itemProducers.map(itemProducer => (
            { id: itemProducer.value, name: itemProducer.name }
        ));

        let responseAddItem = null;
        try {
            responseAddItem = await Api.addItem(item, user);
            const savedItemId = responseAddItem.data.id;
            if(imageFile) {
                await Api.uploadItemImage(imageFile, savedItemId, user);
            }
            navigate("/items");
        } catch(error) {
            if(responseAddItem && responseAddItem.status === 201){
                navigate("/items");
            } else {
                if(error.response && error.response.data){
                    setFailedToAddMessage(error.response.data.message);
                } else {
                    setFailedToAddMessage("Failed to add item.");
                }
                setFailedToAdd(true);
            }
        } finally {
            setIsPending(false);
        }
    }

    const handleInputChange = (e) => {
        const name = e.target.name;
        const value = e.target.value

        if(name === "name") {
            setName(value);
        } else if(name === "buyItNowPrice") {
            setbuyItNowPrice(value)
        } else if(name === "startPrice") {
            setStartPrice(value);
        } else if(name === "description") {
            setDescription(value)
        } else if(name === "expirationDate") {
            setexpirationDate(value)
        } else if(name === "itemCategory") {
            setItemCategory({ id: value.split(";")[0], name: value.split(";")[1] })
        } else if(name === "imageFile") {
            console.log(e.target.files[0])
            setImageFile(e.target.files[0])
        }
    }

    useEffect(() => {
        console.log("effect");
        handleGetCategories(setItemCategories);
        handleGetProducers(setAvailableItemProducers);
    }, [])


    const getTwoDayLater = () => {
        const now = new Date();
        const oneDayLater = new Date(now.getTime() + 2 * 60 * 60 * 24 * 1000);
        return oneDayLater.toISOString().split("T")[0];
    }

    if(user === null) {
        return <Navigate to='/login'></Navigate>;
    }

    return (
        <Container>
            <Row className="d-flex justify-content-center mb-3">
                <Col className="col-md-6">
                    <h1>List item</h1>
                </Col>
            </Row>
            <Row className="d-flex justify-content-center">
                <Col className="col-lg-6">
                    <Form noValidate validated={validated} onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Item name</Form.Label>
                            <Form.Control 
                                type="text"
                                name="name" 
                                value={name} 
                                onChange={(e) => handleInputChange(e)}
                                required
                                minLength={3}
                                maxLength={100}/>
                            <Form.Control.Feedback type="invalid">Enter a name with 3-100 characters.</Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Auction start price (leave empty if you don't want to create auction)</Form.Label>
                            <Form.Control 
                                type="number" 
                                name="startPrice" 
                                value={startPrice} 
                                onChange={(e) => handleInputChange(e)}/>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Buy It now price (leave empty if you don't want item to have buy it now option)</Form.Label>
                            <Form.Control 
                                type="number" 
                                name="buyItNowPrice" 
                                value={buyItNowPrice} 
                                onChange={(e) => handleInputChange(e)}/>
                            <Form.Control.Feedback type="invalid">Enter buy it now price.</Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Description (optional)</Form.Label>
                            <Form.Control 
                                as="textarea"
                                rows={3} 
                                name="description" 
                                value={description} 
                                onChange={(e) => handleInputChange(e)}
                                />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Expiration date</Form.Label>
                            <Form.Control 
                                type="date" 
                                name="expirationDate" 
                                value={expirationDate} 
                                onChange={(e) => handleInputChange(e)}
                                required
                                min={getTwoDayLater()}/>
                            <Form.Control.Feedback type="invalid">Choose expiration date. (minimum one day later than today)</Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Item category</Form.Label>
                            <Form.Select 
                                name="itemCategory" 
                                onChange={(e) => handleInputChange(e)}
                                required>
                                {itemCategories.length > 0 && <option value="">Choose category</option>}
                                {itemCategories.length === 0 ? <option>Fetching categories</option> :
                                itemCategories.map(itemCategory => (
                                    <option key={itemCategory.id} value={`${itemCategory.id};${itemCategory.name}`}>{itemCategory.name}</option>
                                ))}
                            </Form.Select>
                            <Form.Control.Feedback type="invalid">Choose item category.</Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Item producer/s</Form.Label>
                            <MultiSelect
                                options={availableItemProducers}
                                value={itemProducers}
                                onChange={setItemProducers}
                                labelledBy="Select"
                                hasSelectAll={false}/>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Item image (optional)</Form.Label>
                            <Form.Control
                                type="file"
                                name="imageFile"
                                onChange={(e) => handleInputChange(e)}/>
                        </Form.Group>
                        <Button type="submit" disabled={isPending}>Add item</Button>
                    </Form>
                    {!failedToAdd ? "" : <Alert variant="danger">{failedToAddMessage}</Alert>}
                </Col>
                </Row>
        </Container>
    );
}

export default ItemForm;