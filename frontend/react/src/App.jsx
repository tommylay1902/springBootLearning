import { useEffect, useState } from "react"
import UserProfile from "./components/shared/UserProfile.jsx"
import {Button, Spinner, Textarea, Wrap, WrapItem} from "@chakra-ui/react"
import SidebarWithHeader from "./components/shared/SideBar.jsx";
import {getCustomers} from "./services/client.js";
import CardWithImage from "./components/Card.jsx";


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
                <Wrap justify={"center"} spacing={"30px"}>
                {
                    customers.map(
                        (customer, index) =>
                            (
                            <WrapItem key={index}>
                                <CardWithImage {...customer}/>
                            </WrapItem>
                            )
                        )
                }
                <Button onClick={() => setCount(count+1)} colorScheme={"blue"} variant={"outline"}>Click me</Button>
                </Wrap>
            </SidebarWithHeader>
        )


}

export default App
