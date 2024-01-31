import { useAuth } from "./context/AuthContext"
import { Container, Row, Col, Image } from "react-bootstrap";
import { useState, useEffect } from "react";
import PostList from "./posts/PostList";


function Home(){
    const Auth = useAuth();
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [roles, setRoles] = useState('');

    useEffect(() => {
        if(Auth.userIsAuthenticated()){
            const user = Auth.getUser();
            console.log(user);
            setUsername(user.username);
            setEmail(user.email);
            setRoles(user.roles);
        }
    }, [])

    return (
        <Container>
            <Row>
            <Col>
                <PostList
                    isMod={false}/>
            </Col>
            <Col>
                {/* <h2 className="text-center">{Auth.userIsAuthenticated() ? `Hello ${username}` : "Not logged in."}</h2> */}
                <Image
                    className="mt-5"
                    src="favicon.png"
                    fluid/>
            </Col>
            </Row>
        </Container>
    )
}

export default Home