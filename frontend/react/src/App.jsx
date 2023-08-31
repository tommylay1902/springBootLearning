import { useEffect, useState } from "react"
import UserProfile from "./UserProfile"
function App() {
    let [loading, setLoading] = useState(false)
    useEffect(() => {
        setTimeout(() => {
            setLoading(true)
        }, 5000)
       
    }, [])
    return <h1>{loading ? <>
        <UserProfile name={"Tommy"} age={"22"} gender={"men"}>
            <p>Testing children prop</p>
        </UserProfile>
        <UserProfile name={"Apple Cheese"} age={"22"} gender={"women"}/>
    </>
   
    : "loading"}
    </h1>
}

export default App
