import { useState, useEffect } from "react";
import { Form, Button, Col } from "react-bootstrap";
import { MultiSelect } from "react-multi-select-component";
import { handleGetCategories, handleGetProducers } from "../misc/ItemHelpers";

function ItemsSearch(props) {
    const {handleGetItems} = props;
    const [namePhrase, setNamePhrase] = useState("");
    const [descriptionPhrase, setDescriptionPhrase] = useState("");
    const [minPrice, setMinPrice] = useState(0);
    const [maxPrice, setMaxPrice] = useState(0);
    const [categoryPhrase, setCategoryPhrase] = useState("");
    const [itemCategories, setItemCategories] = useState([]);
    const [itemProducers, setItemProducers] = useState([]);
    const [availableItemProducers, setAvailableItemProducers] = useState([]);
    const [dateMin, setMinDate] = useState("");
    const [dateMax, setMaxDate] = useState("");
    const [isBought, setIsBought] = useState("");
    const [isPending, setIsPending] = useState(false);

    useEffect(() => {
        handleGetCategories(setItemCategories);
        handleGetProducers(setAvailableItemProducers);
    }, [])

    const handleSubmit = async(event) => {
        setIsPending(true)
        event.preventDefault();
        event.stopPropagation();
        
        try {
            const params = mapStateToParams();
            console.log(params);
            handleGetItems(params);            
        } catch(error) {

        } finally {
            setIsPending(false);
        }
    }

    const mapStateToParams = () => {
        const params = {}

        if(namePhrase !== ""){
            params.namePhrase = namePhrase;
        }
        if(descriptionPhrase !== ""){
            params.descriptionPhrase = descriptionPhrase;
        }
        if(minPrice > 0){
            params.minPrice = minPrice;
        }
        if(maxPrice > 0){
            params.maxPrice = maxPrice;
        }
        if(itemProducers.length > 0){
            params.producerNames = itemProducers.map((itemProducer) => itemProducer.label)
        }
        if(categoryPhrase !== ""){
            params.categoryPhrase = categoryPhrase;
        }
        if(dateMin !== ""){
            params.dateMin = dateMin;
        }
        if(dateMax !== ""){
            params.dateMax = dateMax;
        }
        if(isBought !== ""){
            params.isBought = isBought;
        }

        return params;
    }

    const handleParamsChange = (event) => {
        const name = event.target.name;
        const value = event.target.value;

        if(name === "namePhrase"){
            setNamePhrase(value);
        } else if(name === "descriptionPhrase"){
            setDescriptionPhrase(value);
        } else if(name === "minPrice"){
            setMinPrice(value);
        } else if(name === "maxPrice"){
            setMaxPrice(value);
        } else if(name === "categoryPhrase"){
            setCategoryPhrase(value);
        } else if(name === "dateMin"){
            setMinDate(value);
        } else if(name === "dateMax"){
            setMaxDate(value);
        } else if(name === "isBought"){
            setIsBought(value);
        }
    }

    const handleClearParams = () => {
        setNamePhrase("");
        setDescriptionPhrase("");
        setMinPrice(0);
        setMaxPrice(0);
        setCategoryPhrase("");
        setItemProducers([]);
        setMinDate("");
        setMaxDate("");
        setIsBought("");
    }

    return (
        <Col lg={3}>
            <h1>Search for items</h1>
            <Form onSubmit={handleSubmit}>
                <Form.Group>
                    <Form.Label>name phrase</Form.Label>
                    <Form.Control
                    type="text"
                    placeholder="eg. item"
                    name="namePhrase"
                    value={namePhrase}
                    onChange={(e) => handleParamsChange(e)}/>
                </Form.Group>
                <Form.Group>
                    <Form.Label>description phrase</Form.Label>
                    <Form.Control
                    type="text"
                    placeholder="eg. good item"
                    name="descriptionPhrase"
                    value={descriptionPhrase}
                    onChange={(e) => handleParamsChange(e)}/>
                </Form.Group>
                <Form.Group>
                    <Form.Label>Price range</Form.Label>
                    <Form.Control
                    type="number"
                    name="minPrice"
                    value={minPrice}
                    onChange={(e) => handleParamsChange(e)}/>
                    <Form.Control
                    type="number"
                    name="maxPrice"
                    value={maxPrice}
                    onChange={(e) => handleParamsChange(e)}/>
                </Form.Group>
                <Form.Group>
                    <Form.Label>Item producer/s</Form.Label>
                    <MultiSelect
                        options={availableItemProducers}
                        value={itemProducers}
                        onChange={setItemProducers}
                        labelledBy="Select"
                        hasSelectAll={false}/>
                </Form.Group>
                <Form.Group>
                    <Form.Label>category phrase</Form.Label>
                    <Form.Control
                    type="text"
                    placeholder="eg. Computers"
                    name="categoryPhrase"
                    value={categoryPhrase}
                    onChange={(e) => handleParamsChange(e)}/>
                </Form.Group>
                <Form.Group>
                    <Form.Label>date range</Form.Label>
                    <Form.Control 
                        type="date" 
                        name="dateMin" 
                        value={dateMin} 
                        onChange={(e) => handleParamsChange(e)}/>
                    <Form.Control 
                        type="date" 
                        name="dateMax" 
                        value={dateMax} 
                        onChange={(e) => handleParamsChange(e)}/>
                </Form.Group>
                <Form.Group>
                    <Form.Label>is bought</Form.Label>
                    <Form.Select 
                        name="isBought"
                        value={isBought}
                        onChange={(e) => handleParamsChange(e)}>
                        <option value={""}></option>
                        <option value={"NOT_BOUGHT"}>Not bought</option>
                        <option value={"BOUGHT_AUCTION"}>Bought via auction</option>
                        <option value={"BOUGHT_BIN"}>Bought via bin</option>
                    </Form.Select>
                </Form.Group>
                <Form.Group>
                    <Button type="submit" disabled={isPending}>Search</Button>
                    <Button variant="danger" onClick={handleClearParams}>clear filters</Button>
                </Form.Group>
            </Form>
        </Col>
    );
}

export default ItemsSearch