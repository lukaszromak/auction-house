import { useState, useEffect } from "react";
import { json, useParams } from "react-router-dom";
import { Button, Container, Row, Col, Image, FormControl, Alert, Figure } from "react-bootstrap";
import { Api } from "../misc/Api";
import { useAuth } from "../context/AuthContext";
import { config } from "./ImageUrl";

function ItemPage() {
    const Auth = useAuth();
    let { itemId } = useParams();
    const [item, setItem] = useState();
    const [bid, setBid] = useState(null);
    const [offer, setOffer] = useState(0);
    const [bidNotFound, setBidNotFound] = useState(false);
    const [getBidErrorResponse, setGetBidErrorResponse] = useState("");
    const [placeBidResponse, setPlaceBidResponse] = useState(null);
    const [placeBidSuccess, setPlaceBidSuccess] = useState(false);
    const [isNotFound, setIsNotFound] = useState(false);

    useEffect(() => {
        if(itemId !== null){
            handleGetItem();
        }
    }, [])


    const handleGetItem = async () => {
        try {
            const response = await Api.getItem(itemId);
            const item = response.data;
            console.log(item);
            setItem(item);
            handleGetBid();
        } catch(error) {
            if(error.response && error.response.data){
                const errorData = error.response.data;
                if(errorData.status === 404){
                    setIsNotFound(true);
                }
            }
        }
    }

    const handleGetBid = async() => {
        try {
            const response = await Api.getBid(itemId);
            const bid = response.data;
            setBid(bid);
            setOffer(bid.currentPrice);
        } catch(error) {
            if(error.response && error.response.data){
                const errorData = error.response.data;
                if (errorData.status === 404){
                    setBidNotFound(true);
                    setGetBidErrorResponse("That item has no auction.");
                }
            }
        }
    }

    const handlePlaceBid = async() => {
        try {
            if(!Auth.userIsAuthenticated()){
                setPlaceBidResponse("You need to be logged in to place bid.");
                return;
            }

            const user = Auth.getUser();

            if(bid && offer && user) {
                const bidRequest = {}
                bidRequest.bidId = bid.id;
                bidRequest.offer = offer;
                const response = await Api.placeBid(bidRequest, user);
                setPlaceBidResponse("Bid placed.");
                setBid(response.data);
                setPlaceBidSuccess(true);
            }
        } catch(error) {
            setPlaceBidSuccess(false);
            console.log(error);
            if(error.response && error.response.data){
                const errorData = error.response.data;
                if(errorData.status === 409) {
                    setBidNotFound(true);
                    setPlaceBidResponse(errorData.message);
                } else if (errorData.status === 404){
                    setBidNotFound(true);
                    setPlaceBidResponse("That item has no auction.");
                }
            }
        }
    }

    return (
        <Container className="p-4">
            {isNotFound && <h1>Sorry, we couldn't find that item.</h1>}
            {(item && !isNotFound) &&
            <>
            <Row>
                <Col md={6}>
                    <h1>{item.name}</h1>
                    <Figure>
                        <Figure.Image fluid
                        src={item.imagePath ? `${config.url.IMAGE_BASE_URL}${item.imagePath}` : require("./nophoto.jpg")}/>
                        <Figure.Caption>
                            listed by {item.listedBy && item.listedBy.username}
                        </Figure.Caption>
                    </Figure>
                </Col>
                <Col md={6}>
                    {(bid && item.bought) ?
                    <Alert>That item was sold for {bid.currentPrice}zł</Alert> 
                    :
                    bid ?      
                    <>           
                        <h3>Current price - {bid.currentPrice}zł</h3>
                        <h4>current winner - {bid.user && bid.user.username}</h4>
                        {placeBidSuccess &&
                        <Alert variant="success">
                        {placeBidResponse}
                        </Alert>}
                        {(!placeBidSuccess && placeBidResponse) &&
                        <Alert variant="danger">
                        {placeBidResponse}
                        </Alert>}
                        <FormControl type="number" placeholder="Your offer" min={bid.currentPrice} value={offer} onChange={(e) => setOffer(e.target.value)}/>
                        <Button className="mt-2" onClick={() => {handlePlaceBid()}}>Bid</Button>
                    </> 
                    :
                    <Alert>That item has no auction.</Alert>
                    }
                    {(item.buyItNowPrice && !item.bought) ?
                    <>
                    <h3>Buy it now price - {item.buyItNowPrice}</h3> 
                    <Button>Buy</Button>
                    </>
                    :
                    (!item.buyItNowPrice && !item.bought) ?
                    <Alert className="mt-2">This item has no buy it now option.</Alert> 
                    :
                    ""
                    }
                </Col>
            </Row>
            <Row className="p-4">
                <h2>Description</h2>
                {item.description.length === 0 ? "No description provided." : <p>item.description</p>}
                <h2 className="mt-5">Additional Info</h2>
                <Row>
                    <p>Categories: {item.itemCategory && item.itemCategory.name}</p>
                </Row>
                <Row>
                    <p>Producers: {JSON.stringify(item.itemProducers)}</p>
                </Row>
            </Row>
            </>
            }
        </Container>
    );
}

export default ItemPage;