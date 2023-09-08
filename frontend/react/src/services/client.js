import axios from "axios";

export const getCustomers = async () => {
    try {
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

