import { Api } from "./Api";

export const handleGetCategories = async (setter) => {
    try {
        const response = await Api.getItemCategories();
        setter(response.data);
    } catch(error) {
        console.log(error);
    }
}

export const handleGetProducers = async (setter) => {
    try {
        const response = await Api.getItemProducers();
        const producers = response.data.map((producer) => (
            { label: producer.name, value: producer.id }
        ));
        setter(producers);
    } catch(error) {
        console.log(error);
    }
}