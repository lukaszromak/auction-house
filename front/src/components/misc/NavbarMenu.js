import { Container } from 'react-bootstrap';
import { Nav, Navbar, Image } from 'react-bootstrap';
import { useAuth } from "../context/AuthContext";

function NavbarMenu() {
    const Auth = useAuth()
    const user = Auth.getUser();
    const { userIsAdmin, userIsMod, userLogout, userIsAuthenticated } = useAuth();

    const logout = () => {
      userLogout();
    }
  
    return (
        <Navbar expand="lg" className="mb-4 bg-body-tertiary">
          <Container>
            <Navbar.Brand href="/">
                <Image
                  width={30}
                  height={30}
                  className="d-inline-block align-top"
                  src="favicon.png"/>
              Auction House
            </Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav" />
            <Navbar.Collapse id="basic-navbar-nav">
              <Nav className="me-auto">
                <Nav.Link href="/">Home</Nav.Link>
                <Nav.Link href="/items">Items</Nav.Link>
                <Nav.Link href="/itemForm">Add item</Nav.Link>
                {userIsMod() && <Nav.Link href="/moderator">Moderator Page</Nav.Link>}
                {userIsAdmin() && <Nav.Link href="/admin">Admin Page</Nav.Link>}
                {userIsAuthenticated() ? <Nav.Link onClick={logout}>Logout</Nav.Link> : <Nav.Link href="/login">Login</Nav.Link>}
              </Nav>
              {userIsAuthenticated() && <Navbar.Text>Logged in as {user && user.username}</Navbar.Text>}
            </Navbar.Collapse>
          </Container>
        </Navbar>
      );
}

export default NavbarMenu;