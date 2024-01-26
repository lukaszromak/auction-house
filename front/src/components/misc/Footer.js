import { render } from "@testing-library/react";
import { Container, Navbar } from "react-bootstrap";

function Footer() {
    return (
        <Navbar>
            <Container>
                <Navbar.Text>Created by Łukasz Romak</Navbar.Text>
            </Container>
        </Navbar>
    );
}

export default Footer;