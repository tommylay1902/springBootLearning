import { useEffect, useState } from "react"
import UserProfile from "./UserProfile"
import {Button, Spinner, Textarea} from "@chakra-ui/react"
import SidebarWithHeader from "./SideBar.jsx";
import {getCustomers} from "./services/client.js";


const App =() => {
    const [count, setCount] = useState(0)
    const [isLoading, setIsLoading] = useState(true);
    const [customers, setCustomers] = useState({})
    useEffect(() => {
        setIsLoading(true);
        setTimeout(() => {
            getCustomers()
                .then(data => {
                    setCustomers(data.data);

                })
                .catch((e) => console.log(e))
                .finally(() => setIsLoading(false))
        }, 2000)

    }, []);

        if(isLoading){
            return(
                <SidebarWithHeader>
                    <Spinner size={"xl"}/>
                </SidebarWithHeader>
            )
        }

        if(customers.length <= 0){
            return (
                <SidebarWithHeader>
                    <Textarea>No Customer available</Textarea>
                    <Button
                        onClick={() => setCount(count+1)}
                        colorScheme={"blue"}
                        variant={"outline"}>
                            Click me
                    </Button>
                </SidebarWithHeader>
            )
        }

        return (
            <SidebarWithHeader>
                {
                    customers.map(
                        (customer) => {return <p key={customer.id}>{customer.name}</p>})

                }
                <Button onClick={() => setCount(count+1)} colorScheme={"blue"} variant={"outline"}>Click me</Button>
            </SidebarWithHeader>
        )


}

export default App
