import { useState, useEffect } from "react";
import { json, useParams } from "react-router-dom";
import { Button, Container, Row, Col, FormControl, Alert, Figure } from "react-bootstrap";
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
    const [buyItemSuccess, setBuyItemSuccess] = useState(false);
    const [buyItemError, setBuyItemError] = useState("");
    const [buyItemErrorMessage, setBuyItemErrorMessage] = useState("");
    const [placeBidSuccess, setPlaceBidSuccess] = useState(false);
    const [isNotFound, setIsNotFound] = useState(false);
    const noPhotoAltImg =  require("./nophoto.jpg");

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
            console.log(bid);
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

    const handleBuyItem = async() => {
        try {
            if(!Auth.userIsAuthenticated()){
                setPlaceBidResponse("You need to be logged in to place bid.");
                return;
            }

            const user = Auth.getUser();
            const response = await Api.buyItem(itemId, user);
            if(response.status === 200){
                setBuyItemSuccess(true);
            }
        } catch(error) {
            setBuyItemError(true);
            if(error.response && error.response.data){
                const errorData = error.response.data;
                setBuyItemErrorMessage(errorData.message);
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
                        src={item.imagePath ? `${config.url.IMAGE_BASE_URL}${item.imagePath}` : require("./nophoto.jpg")}
                        onError={({ currentTarget }) => {
                            currentTarget.onerror = null; // prevents looping
                            currentTarget.src = noPhotoAltImg;
                          }}/>
                        <Figure.Caption>
                            listed by {item.listedBy}
                        </Figure.Caption>
                    </Figure>
                </Col>
                <Col md={6}>
                    {
                    // display info about auction
                    buyItemSuccess ?
                    <Alert variant="success">You bought the item.</Alert>
                    :
                    (bid && item.status && item.status === "BOUGHT_AUCTION") ?
                    <Alert>That item was sold for {bid.currentPrice}zł</Alert> 
                    :
                    bid && item.status === "NOT_BOUGHT" ?      
                    <>           
                        <h3>Current price - {bid.currentPrice}zł</h3>
                        <h4>current winner - {bid.username}</h4>
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
                    (!bid && item.status === "NOT_BOUGHT") && <Alert>That item has no auction.</Alert>
                    }
                    {
                    (item.status === "BOUGHT_BIN") ? <Alert>That item was sold for {item.buyItNowPrice}zł</Alert> 
                    :
                    (item.buyItNowPrice && item.status === "NOT_BOUGHT" && !buyItemSuccess) ?
                    <>
                    <h3>Buy it now price - {item.buyItNowPrice}</h3> 
                    <Button onClick={handleBuyItem}>Buy</Button>
                    </>
                    :
                    (!item.buyItNowPrice && item.status === "NOT_BOUGHT") && <Alert className="mt-2">This item has no buy it now option.</Alert> 
                    }
                </Col>
            </Row>
            <Row className="p-4">
                <h2>Description</h2>
                {item.description.length === 0 ? "No description provided." : <p>{item.description}</p>}
                <h2 className="mt-5">Additional Info</h2>
                <Row>
                    <p>Categories: {item.itemCategory}</p>
                </Row>
                <Row>
                    <p>Producers: {(JSON.stringify(item.itemProducers))}</p>
                </Row>
            </Row>
            </>
            }
        </Container>
    );
}

export default ItemPage;