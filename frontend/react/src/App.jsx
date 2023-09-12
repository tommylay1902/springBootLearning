import { useEffect, useState } from "react"
// @ts-ignore
import UserProfile from "./components/shared/UserProfile.jsx"
import {Button, Spinner, Textarea, Wrap, WrapItem} from "@chakra-ui/react"
import SidebarWithHeader from "./components/shared/SideBar.jsx";
import {getCustomers} from "./services/client.js";
import CardWithImage from "./components/Card.jsx";
import {DrawerForm} from "./components/DrawerForm.jsx";
import React from "react";


const App =() => {
    const [count, setCount] = useState(0)
    const [isLoading, setIsLoading] = useState(true);
    const [customers, setCustomers] = useState([])

    const fetchCustomers = async () =>{
        setIsLoading(true);
        getCustomers()
            .then(data => {
                setCustomers(data.data);


            })
            .catch((e) => console.log(e))
            .finally(() => setIsLoading(false))
        }

    useEffect(() => {
        fetchCustomers().then(r => console.log("fetched the customers"))
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
                <DrawerForm fetchCustomers={fetchCustomers}/>
                <Wrap justify={"center"} spacing={"30px"}>
                {
                    customers.map(
                        (customer, index) =>
                            (
                            <WrapItem key={index}>
                                <CardWithImage {...customer} fetchCustomers={fetchCustomers}/>
                            </WrapItem>
                            )
                        )
                }
                </Wrap>
            </SidebarWithHeader>
        )


}

export default App
