import { Container } from 'react-bootstrap';
import { Nav, Navbar } from 'react-bootstrap';
import { useAuth } from "../context/AuthContext";

function NavbarMenu() {
    const Auth = useAuth()
    const user = Auth.getUser();
    const { userIsAdmin, userLogout, userIsAuthenticated } = useAuth();

    const logout = () => {
      userLogout();
    }
  
    return (
        <Navbar expand="lg" className="bg-body-tertiary">
          <Container>
            <Navbar.Brand href="/">Auction House</Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav" />
            <Navbar.Collapse id="basic-navbar-nav">
              <Nav className="me-auto">
                <Nav.Link href="/">Home</Nav.Link>
                <Nav.Link href="/items">Items</Nav.Link>
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