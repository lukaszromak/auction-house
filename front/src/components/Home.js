import { useAuth } from "./context/AuthContext"
import { Container, Row, Image } from "react-bootstrap";
import { useState, useEffect } from "react";


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
        <Container className="d-flex flex-column align-items-center">
            <Row>
            {Auth.userIsAuthenticated() ? "" : "Not logged in."}
            </Row>
            <Image
                className="mt-5"
                src="favicon.png"
                fluid/>
            <h1>Hello {username} !</h1>
        </Container>
    )
}

export default Home