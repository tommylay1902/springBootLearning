import { useEffect, useState } from "react"
import UserProfile from "./UserProfile"
function App() {
    let [loading, setLoading] = useState(false)

    const users = [
        {
            name:"jamila",
            age:22,
            gender:"female",
        },
        {
            name:"Tommy",
            age:24,
            gender:"male",
        },  
        {
            name:"apple cheese",
            age:30,
            gender:"male",
        },
    ]
    useEffect(() => {
        setTimeout(() => {
            setLoading(true)
        }, 5000)
       
    }, [])
    return <h1>{
        loading ? 
        <>
            {
                users.map((user, index) => {
                    return <UserProfile 
                            name={user.name} 
                            age={user.age} 
                            gender={user.gender} 
                            key={index} 
                            imageNumber={index}
                        />

                })
            }
        </>
    
        : 
        "loading"
    }
    </h1>
}

export default App
