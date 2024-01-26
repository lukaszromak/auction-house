import React, { useState } from 'react'
import { NavLink, Navigate } from 'react-router-dom'
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import { Container, Alert } from 'react-bootstrap';
import { useAuth } from './context/AuthContext'
import { Api } from './misc/Api'
import { handleLogError } from './misc/Helpers'

function Login() {
  const Auth = useAuth()
  const isLoggedIn = Auth.userIsAuthenticated()

  const [usernameInput, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [isError, setIsError] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()

    if (!(usernameInput && password)) {
      setIsError(true)
      return
    }

    try {
      const response = await Api.authenticate(usernameInput, password)
      const { id, username, email, roles } = response.data
      const authdata = window.btoa(username + ':' + password)
      const authenticatedUser = { id, username, email, roles, authdata }
      console.log(authenticatedUser)
      Auth.userLogin(authenticatedUser)

      setUsername('')
      setPassword('')
      setIsError(false)
    } catch (error) {
      handleLogError(error)
      setIsError(true)
    }
  }

  if (isLoggedIn) {
    return <Navigate to={'/'} />
  }

  return (
    <Container>
      <Form>
        <Form.Group className="mb-3" controlId="formBasicEmail">
          <Form.Label>Email address</Form.Label>
          <Form.Control type="text" placeholder="Enter username" onInput={(e) => setUsername(e.target.value)} value={usernameInput}/>
        </Form.Group>
        <Form.Group className="mb-3" controlId="formBasicPassword">
          <Form.Label>Password</Form.Label>
          <Form.Control type="password" placeholder="Password" onInput={(e) => setPassword(e.target.value)} value={password}/>
        </Form.Group>
        <Button variant="primary" type="submit" onClick={(e) => handleSubmit(e)}>
          Submit
        </Button>
      </Form>
      {isError ? <span>Failed to login</span> : ""}
      <Alert>{`Don't have already an account? `}
          <NavLink to="/signup" as={NavLink} color='teal'>Sign Up</NavLink>
      </Alert>
    </Container>
  )
}

export default Login