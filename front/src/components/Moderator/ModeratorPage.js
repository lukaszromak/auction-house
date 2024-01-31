import { useEffect } from "react";
import { Container } from "react-bootstrap";
import { Navigate } from "react-router-dom";
import PostList from "../posts/PostList";
import { useAuth } from "../context/AuthContext";
import { Api } from "../misc/Api";

function ModeratorPage() {
    const Auth = useAuth();
    const userIsMod = Auth.userIsMod();
    const user = Auth.getUser();

    const handleCreatePost = async(post) => {
        return await Api.addPost(post, user);
    }

    const handleUpdatePost = async(postId, post, setter) => {
        try {
            await Api.updatePost(postId, post, user);
            setter(prev => [...prev].concat(postId));
        } catch(error){}
    }

    const handleDeletePost = async(postId, setter) => {
        try {
            await Api.deletePost(postId, user);
            setter(prev => [...prev].filter((post) => post.id !== postId))
        } catch(error){}
    }


    if(!userIsMod){
        return <Navigate to="/"/>;
    }

    return (
        <Container>
            <PostList
                isMod={userIsMod}
                handleDeletePost={handleDeletePost}
                handleCreatePost={handleCreatePost}
                handleUpdatePost={handleUpdatePost}/>
        </Container>
    );

}

export default ModeratorPage;