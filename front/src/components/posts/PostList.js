import { useState, useEffect } from "react";
import { Container, Row, Button, Alert, Form } from "react-bootstrap";
import PostForm from "./PostForm";
import { Api } from "../misc/Api";

function PostList(props) {
    const {isMod, handleCreatePost, handleUpdatePost, handleDeletePost} = props
    const [posts, setPosts] = useState([]);
    const [updated, setUpdated] = useState([]);
    const [isError, setIsError] = useState(false);

    useEffect(() => {
        handleGetPosts();
    }, [])

    const handleGetPosts = async () => {
        try {
            const response = await Api.getPosts();
            const posts = response.data;
            setPosts(posts);
        } catch(error) {
            console.log(error);
            setIsError(true);
        }
    }

    const handleInputChange = (e, postId) => {
        const name = e.currentTarget.name;
        const value = e.currentTarget.value;
        const index = posts.findIndex((post) => post.id === postId);
        if(index !== -1){
            setUpdated([...updated].filter((id) => id !== postId))
            setPosts(
                [
                    ...posts.slice(0, index), 
                    Object.assign({}, posts[index], name === "title" ? {title: value} : {content: value}),
                    ...posts.slice(index + 1)
                ]
            );
        }
    }

    return (
        <Container>
            {posts &&
            !isMod ? 
            posts.map((post) => (
                <Row key={post.id}>
                    <h2>{post.title}</h2>
                    <p>{post.content}</p>
                    <p>Created at: {post.createdDate}</p>
                </Row>                
            ))
            :
            <>
            <PostForm
                handleCreatePost={handleCreatePost}
                setPosts={setPosts}/>
            <h2 className="text-center">Menage old posts</h2>
            {posts.map((post) => (   
                <Row className="mt-5" key={post.id}>
                    <Form.Control
                        type="text"
                        name="title"
                        value={post.title}
                        onChange={(e) => handleInputChange(e, post.id)}/>
                    <Form.Control
                        as="textarea"
                        name="content" 
                        value={post.content}
                        onChange={(e) => handleInputChange(e, post.id)}/>
                    <p>Created at: {post.createdDate} by {post.createdBy}</p>
                    <Button variant="primary" onClick={() => handleUpdatePost(post.id, {title: post.title, content: post.content}, setUpdated)}>Save</Button>
                    <Button variant="danger" onClick={() => handleDeletePost(post.id, setPosts)}>Delete</Button>
                    {updated.find((id) => id === post.id) && <Alert variant="success">Updated post.</Alert>}
                </Row>
            ))}
            </>
            }
            {isError && <h2>Error while fetching posts.</h2>}
        </Container>
    );

}

export default PostList