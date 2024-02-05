import { useState, useEffect } from "react";
import { Image, Container, Col, Row, Nav } from "react-bootstrap";
import { Link } from "react-router-dom";
import { Api } from "../misc/Api";
import { config } from "./ImageUrl";
import ItemsSearch from "./ItemsSearch";

function ItemsList() {
    const noPhotoAltImg = require("./nophoto.jpg")
    const status_map = {
        "BOUGHT_BIN": "Not bought",
        "BOUGHT_BIN": "Bought via bin",
        "BOUGHT_AUCTION": "Bought via auction"
    }
    const [items, setItems] = useState([])

    useEffect(() => {
        handleGetItems({isBought: "NOT_BOUGHT"});
    }, [])

    const handleGetItems = async (params) => {
        try {
            const response = await Api.getItems(params);
            const items = response.data
            console.log(items);
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
                    {items.map(item => (
                        <>
                        <Row key={item.id} className="mb-3">
                            <Col>
                                <Nav.Link href={`/item/${item.id}`}>
                                    <Image
                                        src={item.imagePath ? `${config.url.IMAGE_BASE_URL}${item.imagePath}` : noPhotoAltImg}
                                        fluid
                                        onError={({ currentTarget }) => {
                                            currentTarget.onerror = null; // prevents looping
                                            currentTarget.src = noPhotoAltImg;
                                        }}/>
                                </Nav.Link>
                            </Col>
                            <Col className="d-flex flex-column justify-content-between">
                                <span>
                                    <Nav.Link href={`/item/${item.id}`}><h2>{item.name}</h2></Nav.Link>
                                    <p>listed by: {item.listedBy}</p>
                                </span>
                                <span>
                                    {(item.startPrice && item.status === "NOT_BOUGHT") && <p>auction starting price: {item.startPrice}zł</p>}
                                    {(item.buyItNowPrice && item.status === "NOT_BOUGHT") && <p>buy it now for: {item.buyItNowPrice}zł</p>}
                                </span>
                                <p>expires at: {formatDate(item.expirationDate)}</p>
                            </Col>
                        </Row>
                        <hr/>
                        </>
                    ))}
                </Col>
            </Row>
        </Container>
    )

}

export default ItemsList;