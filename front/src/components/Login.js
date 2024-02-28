import React, { useState } from 'react'
import { NavLink, Navigate } from 'react-router-dom'
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import { Container, Alert } from 'react-bootstrap';
import { useAuth } from './context/AuthContext'
import { Api } from './misc/Api'
import { handleLogError } from './misc/Helpers'

function Login() {
  const Auth = useAuth();
  const isLoggedIn = Auth.userIsAuthenticated();

  const [usernameInput, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [isError, setIsError] = useState(false);
  const [validated, setValidated] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();
    event.stopPropagation();
    const form = event.currentTarget;

    if (form.checkValidity() === false) {
      setValidated(true)
    } else {
      handleLogin();
    }
  }

  const handleLogin = async () => {
    try {
      const response = await Api.authenticate(usernameInput, password);
      const { id, username, email, roles } = response.data;
      const authdata = window.btoa(username + ':' + password);
      const authenticatedUser = { id, username, email, roles, authdata };
      console.log(authenticatedUser);
      Auth.userLogin(authenticatedUser);

      setUsername('');
      setPassword('');
      setIsError(false);
    } catch (error) {
      handleLogError(error);
      setIsError(true);
    }
  }

  if (isLoggedIn) {
    return <Navigate to={'/'} />;
  }

  return (
    <Container>
      <Form noValidate validated={validated} onSubmit={handleSubmit}>
        <Form.Group className="mb-3" controlId="formBasicEmail">
          <Form.Label>Username</Form.Label>
          <Form.Control 
            type="text" 
            placeholder="enter username"  
            value={usernameInput}
            required
            minLength={4}
            maxLength={20}
            onInput={(e) => setUsername(e.target.value)}/>
          <Form.Control.Feedback type="invalid">Enter a username with 4-20 characters.</Form.Control.Feedback>
        </Form.Group>
        <Form.Group controlId="formBasicPassword">
          <Form.Label>Password</Form.Label>
          <Form.Control type="password"
            placeholder="enter password" 
            value={password}
            required
            minLength={4}
            maxLength={20}
            onInput={(e) => setPassword(e.target.value)}/>
          <Form.Control.Feedback type="invalid">Enter a password with 4-20 characters.</Form.Control.Feedback>
        </Form.Group>
        <Button type="submit" className="mb-3 mt-3">
          Submit
        </Button>
      </Form>
      {isError ? <Alert variant="danger">Failed to login</Alert> : ""}
      <Alert>{`Don't have already an account? `}
          <NavLink to="/signup" as={NavLink} color='teal'>Sign Up</NavLink>
      </Alert>
    </Container>
  )
}

export default Login