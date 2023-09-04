import { useEffect, useState } from "react"
import UserProfile from "./UserProfile"
import {Button} from "@chakra-ui/react"
import SidebarWithHeader from "./SideBar.jsx";
import {getCustomers} from "./services/client.js";


const App =() => {
    const [count, setCount] = useState(0)
    const [isLoading, setIsLoading] = useState(true);
    const [customers, setCustomers] = useState({})
    useEffect(() => {
        setIsLoading(true);
        getCustomers()
                .then(data => {
                setCustomers(data.data);

            })
            .catch((e) => console.log(e))
            .finally(() => setIsLoading(false))
    }, []);

    return (
       <>
        <SidebarWithHeader>
            {isLoading ? <p>"is loading..."</p>: customers.map(customer => <p id={customer.id}>{customer.name}</p>)}
            <Button onClick={() => setCount(count+1)} colorScheme={"blue"} variant={"outline"}>Click me</Button>
        </SidebarWithHeader>
       </>

    )
}

export default App
