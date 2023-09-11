import axios from "axios";

export const getCustomers = async () => {
    try {
        console.log(import.meta.env.VITE_API_BASE_URL);
        return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`)

    }catch(e){
        throw e
    }
}

export const saveCustomer = async ( update) => {
    try {
        return await axios.post(
            `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`,
            update
        )
    } catch (e) {
        throw e;
    }
}

export const deleteCustomerWithId = async (id) => {
    try {
        return await axios.delete(
            `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${id}`,
        )
    } catch (e) {
        throw e;
    }
}

export const updateCustomerWithId = async (id, update) => {
    try {
        return await axios.put(
            `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${id}`,
            update
        )
    } catch (e) {
        throw e;
    }
}



