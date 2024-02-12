import { useState, useEffect } from "react";
import { Image, Container, Col, Row, Nav } from "react-bootstrap";
import { useSearchParams } from "react-router-dom";
import { Api } from "../misc/Api";
import { config } from "./ImageUrl";
import ItemsSearch from "./ItemsSearch";
import ItemPagination from "./ItemPagination";

function ItemsList() {
    const noPhotoAltImg = require("./nophoto.jpg")
    const [urlParams, setUrlParams] = useSearchParams();
    const [searchParams, setSearchParams] = useState({});
    const [numPages, setNumPages] = useState(0);
    const [currentPage, setCurrentPage] = useState(0);
    const [items, setItems] = useState([]);
    const status_map = {
        "NOT_BOUGHT": "Not bought",
        "BOUGHT_BIN": "Bought via bin",
        "BOUGHT_AUCTION": "Bought via auction"
    }

    useEffect(() => {
        const params = {
            isBought: "NOT_BOUGHT"
        };

        if(urlParams.get("page") && !isNaN(urlParams.get("page"))){
            const pageNumber = Number(urlParams.get("page")) - 1;
            params.page = pageNumber;
        }

        setSearchParams(params);
    }, [])

    useEffect(() => {
        if(Object.keys(searchParams).length > 0){
            handleGetItems();
        }
    }, [searchParams]);

    const handleGetItems = async () => {
        try {
            console.log(searchParams);
            const response = await Api.getItems(searchParams);
            if(response.data && response.data.pageable) {
                const items = response.data.content;
                const numPages = response.data.totalPages;
                const currentPage = response.data.pageable.pageNumber;
                setItems(items);
                setNumPages(numPages);
                setCurrentPage(currentPage);
                const urlSearchParams = new URLSearchParams();
                urlSearchParams.append("page", currentPage + 1);
                setUrlParams(urlSearchParams);
            }
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
                    setSearchParams={setSearchParams}/>
                <Col lg={9}>
                    {items.map(item => (
                        <span key={item.id}>
                        <Row className="mb-3">
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
                        </span>
                    ))}
                    <ItemPagination
                        numPages={numPages}
                        currentPage={currentPage}
                        setSearchParams={setSearchParams}/>
                </Col>
            </Row>
        </Container>
    )

}

export default ItemsList;