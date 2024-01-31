import { useState, useEffect } from "react";
import { Table, Container, Col, Row, Nav } from "react-bootstrap";
import { Link } from "react-router-dom";
import { Api } from "../misc/Api";
import ItemsSearch from "./ItemsSearch";

function ItemsList() {
    const status_map = {
        "BOUGHT_BIN": "Not bought",
        "BOUGHT_BIN": "Bought via bin",
        "BOUGHT_AUCTION": "Bought via auction"
    }
    const [items, setItems] = useState([])

    useEffect(() => {
        handleGetItems({});
    }, [])

    const handleGetItems = async (params) => {
        try {
            const response = await Api.getItems(params);
            const items = response.data
            setItems(items)
        } catch (error) {
            console.log(error)
        }
    }

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return `${date.getHours() >= 10 ? date.getHours() : `0${date.getHours()}`}:${date.getMinutes() >= 10 ? date.getMinutes() : `0${date.getMinutes()}`} 
                ${date.getDate() >= 10 ? date.getDate() : `0${date.getDate()}`}.${date.getMonth() + 1 >= 10 ? date.getMonth() + 1 : `0${date.getMonth() + 1}`}.${date.getFullYear()}`
    }

    return (
        <Container>
            <Row>
                <ItemsSearch
                    handleGetItems={handleGetItems}/>
                <Col lg={9}>
                    <Table>
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Status</th>
                                <th>Start Price</th>
                                <th>Buy It Now Price</th>
                                <th>Expiration Date</th>
                                <th>Item Producers</th>
                                <th>Item Category</th>
                            </tr>
                        </thead>
                        <tbody>
                            {items.map(item => (
                                <tr key={item.id}>
                                    <td><Nav.Link href={`/item/${item.id}`}>{item.name}</Nav.Link></td>
                                    <td>{item.status && item.status.name}</td>
                                    <td>{item.startPrice}</td>
                                    <td>{item.buyItNowPrice}</td>
                                    <td>{item.expirationDate}</td>
                                    <td>{JSON.stringify(item.itemProducers.map(item => item.name))}</td>
                                    <td>{item.itemCategory && item.itemCategory.name}</td>
                                </tr>
                            ))}
                        </tbody>
                    </Table>
                    <Link to="/itemForm">Add item</Link>
                </Col>
            </Row>
        </Container>
    )

}

export default ItemsList;