import { useEffect, useState } from "react";
import { Container } from "react-bootstrap";
import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext"
import { Api } from "../misc/Api";
import ItemCategoryTable from "./ItemCategoryTable";
import ItemProducerTable from "./ItemProducerTable";

function AdminPage() {
    const Auth = useAuth();
    const userIsAdmin = Auth.userIsAdmin();
    const user = Auth.getUser();
    const [itemCategories, setItemCategories] = useState([]);
    const [itemProducers, setItemProducers] = useState([]);
    const [categoryName, setCategoryName] = useState("");
    const [producerName, setProducerName] = useState("");
    const [categoryValidated, setCategoryValidated] = useState(false);
    const [createCategoryErrorMessage, setCreateCategoryErrorMessage] = useState("");
    const [producerValidated, setProducerValidated] = useState(false);
    const [createProducerErrorMessage, setCreateProducerErrorMessage] = useState("");

    useEffect(() => {
        handleGetCategories();
        handleGetProducers();
    }, []);

    const handleInputChange = (e) => {
        const name = e.currentTarget.name;
        const value = e.currentTarget.value;

        if(name === "categoryName"){
            setCategoryName(value);
        } else if(name === "producerName"){
            setProducerName(value);
        }
    }

    const handleGetCategories = async () => {
        try {
            const response = await Api.getItemCategories();
            console.log(response);
            setItemCategories(response.data);
        } catch(error) {
            console.log(error);
        }
    }

    const handleGetProducers = async () => {
        try {
            const response = await Api.getItemProducers();
            setItemProducers(response.data);
        } catch(error) {
            console.log(error);
        }
    }

    const handleDeleteCategory = async (id) => {
        try {
            const response = await Api.deleteItemCategory(id, user);
            const categoryId = response.data.id;
            setItemCategories([...itemCategories].filter((itemCategory) => itemCategory.id !== categoryId));
        } catch(error) {
            console.log(error);
        }
    }

    const handleDeleteProducer = async (id) => {
        try {
            const response = await Api.deleteItemProducer(id, user);
            const producerId = response.data.id;
            setItemProducers([...itemProducers].filter((itemProducer) => itemProducer.id !== producerId));
        } catch(error) {
            console.log(error);
        }
    }

    const handleCreateCategory = async (event) => {
        const form = event.target;
        event.preventDefault();
        event.stopPropagation();
        setCategoryValidated(true);
        if(!form.checkValidity()){
            return;
        }

        try {
            const response = await Api.createItemCategory(categoryName, user);
            setItemCategories([...itemCategories].concat(response.data));
            setCreateCategoryErrorMessage("");
        } catch(error) {
            console.log(error);
            if(error.response){
                const errorData = error.response;
                if(errorData.status === 409) {
                    setCreateCategoryErrorMessage(errorData.data);
                } 
            }
        }
    }

    const handleCreateProducer = async (event) => {
        const form = event.target;
        event.preventDefault();
        event.stopPropagation();
        setProducerValidated(true);
        if(!form.checkValidity()){
            return;
        }

        try {
            const response = await Api.createItemProducer(producerName, user);
            setItemProducers([...itemProducers].concat(response.data))
            setCreateProducerErrorMessage("");
        } catch(error) {
            console.log(error);
            if(error.response){
                const errorData = error.response;
                if(errorData.status === 409) {
                    setCreateProducerErrorMessage(errorData.data);
                } 
            }
        }
        
    }

    if(!userIsAdmin){
        return <Navigate to="/"/>;
    }

    return (
        <Container className="mb-5">
            <ItemCategoryTable
                itemCategories={itemCategories}
                categoryName={categoryName}
                handleInputChange={handleInputChange}
                categoryValidated={categoryValidated}
                errorMessage={createCategoryErrorMessage}
                handleCreateCategory={handleCreateCategory}
                handleDeleteCategory={handleDeleteCategory}/>
            <ItemProducerTable
                itemProducers={itemProducers}
                producerName={producerName}
                handleInputChange={handleInputChange}
                producerValidated={producerValidated}
                errorMessage={createProducerErrorMessage}
                handleCreateProducer={handleCreateProducer}
                handleDeleteProducer={handleDeleteProducer}/>
        </Container>
    );
}

export default AdminPage