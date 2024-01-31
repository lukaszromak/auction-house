import { useState } from "react";
import { Form, Button, Alert } from "react-bootstrap";

function PostForm(props) {
    const {handleCreatePost, setPosts} = props;
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [isPending, setIsPending] = useState(false);
    const [isSuccess, setIsSuccess] = useState(false);
    const [isError, setIsError] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");

    const handleSubmit = async (e) => {
        setIsPending(true);
        e.preventDefault();
        e.stopPropagation();
        try {
            const response = await handleCreatePost({title: title, content: content});
            const post = response.data;
            setPosts(prev => [post].concat([...prev]))
            setIsSuccess(true);
            clearForm();
        } catch(error) {
            setIsError(true);
            if(error.data && error.response){
                setErrorMessage(error.data.response.message);
            } else {
                setErrorMessage("Failed to add post");
            }
        } finally {
            setIsPending(false);
        }
    }

    const handleInputChange = (e) => {
        const name = e.currentTarget.name;
        const value = e.currentTarget.value;
        setIsError(false);
        setIsSuccess(false);

        if(name === "title"){
            setTitle(value);
        } else if (name === "content"){
            setContent(value);
        }
    }

    const clearForm = () => {
        setTitle("");
        setContent("");
    }

    return (
        <>
        <h2 className="text-center">Add a new post</h2>
        <Form onSubmit={handleSubmit}>
            <Form.Group>Title</Form.Group>
                <Form.Control
                    type="text"
                    name="title"
                    value={title}
                    onChange={(e) => handleInputChange(e)} 
                    required/>
            <Form.Group>Content</Form.Group>
                <Form.Control
                    as="textarea"
                    name="content"
                    value={content}
                    onChange={(e) => handleInputChange(e)} 
                    required/>
                <Button type="submit" disabled={isPending}>Add new post</Button>
                {isSuccess && <Alert variant="success">Added new post</Alert>}
                {isError && <Alert variant="danger">{errorMessage}</Alert>}
            </Form>
        </>
    )
}

export default PostForm