import React, { useState } from 'react'
import { NavLink, Navigate } from 'react-router-dom'
import { Container, Form, Button, Alert } from 'react-bootstrap'
import { useAuth } from './context/AuthContext'
import { Api } from './misc/Api'
import { handleLogError } from './misc/Helpers'

function Signup() {
    const Auth = useAuth();
    const isLoggedIn = Auth.userIsAuthenticated();
    const [validated, setValidated] = useState(false);
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [isError, setIsError] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');

    const handleInputChange = (e) => {
        const name = e.target.name;
        const value = e.target.value;

        if (name === "username") {
            setUsername(value);
        } else if (name === "email") {
            setEmail(value);
        } else if (name === "password") {
            setPassword(value);
        }
    }

    const handleSubmit = (event) => {
        event.preventDefault();
        event.stopPropagation();
        const form = event.currentTarget;

        if(form.checkValidity() === false){
            setValidated(true);
        } else {
            handleCreateUser();
        }

    }

    const handleCreateUser = async() => {
        const user = { username, password, email }

        try {
            const response = await Api.signup(user);
            const { id, username, email, roles } = response.data;
            const authdata = window.btoa(username + ':' + password);
            const authenticatedUser = { id, username, email, roles, authdata };

            Auth.userLogin(authenticatedUser)

            setUsername('');
            setPassword('');
            setEmail('');
            setIsError(false);
            setErrorMessage('');
        } catch (error) {
            handleLogError(error);
            if (error.response) {
                const errorData = error.response.data;
                let errorMessage = 'Invalid fields';
                if (errorData.status === 409) {
                    errorMessage = errorData.message;
                } else if (errorData.status === 400) {
                    errorMessage = errorData.errors[0].defaultMessage;
                }
                setIsError(true);
                setErrorMessage(errorMessage);
            }
        }
    }

    if (isLoggedIn) {
        return <Navigate to='/' />;
    }

  return (
    <Container>
        <Form noValidate validated={validated} onSubmit={handleSubmit}>
            <Form.Group>
                <Form.Label>Username</Form.Label>
                <Form.Control
                    type="text"
                    name="username"
                    value={username}
                    required
                    minLength={4}
                    maxLength={20}
                    onChange={(e) => handleInputChange(e)}/>
                <Form.Control.Feedback type="invalid">Enter a username with 4-20 characters.</Form.Control.Feedback>
            </Form.Group>
            <Form.Group>
                <Form.Label>Email</Form.Label>
                <Form.Control
                    type="email"
                    name="email"
                    value={email}
                    required
                    onChange={(e) => handleInputChange(e)}/>
                <Form.Control.Feedback type="invalid">Enter valid email.</Form.Control.Feedback>
            </Form.Group>
            <Form.Group>
                <Form.Label>Password</Form.Label>
                <Form.Control
                    type="password"
                    name="password"
                    value={password}
                    required
                    minLength={4}
                    maxLength={20}
                    onChange={(e) => handleInputChange(e)}/>
                <Form.Control.Feedback type="invalid">Enter password with 4-20 characters</Form.Control.Feedback>
            </Form.Group>
            <Button type="submit" className="mb-3 mt-3">Register</Button>
        </Form>
        {isError &&
        <Alert variant="danger">
            {errorMessage}    
        </Alert>}
        <Alert>{`Already have an accout? `}
          <NavLink to="/login" as={NavLink} color='teal'>Login</NavLink>
        </Alert>
    </Container>
  );
}

export default Signup