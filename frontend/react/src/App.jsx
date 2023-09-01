import { useEffect, useState } from "react"
import UserProfile from "./UserProfile"

const UserProfiles = ({users}) => (
    <div>
        {users.map((user, index) => (
            <UserProfile
                key={index}
                name={user.name}
                age={user.age}
                gender={user.gender}
                imageNumber={index}
            />
        ))}
    </div>
)

function App() {
    const [isLoading, setIsLoading] = useState(false)
    const [counter, setCounter] = useState(0);
       // Update the counter when the button is clicked
    const handleCounterClick = () => {
        setCounter(counter + 1);
    }
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
            setIsLoading(true)
            alert("loaded");
        }, 1000)
       
    }, [])
    return <h1>{
        isLoading ? 
        <>
            <UserProfiles users={users}/>
            <button onClick={handleCounterClick}>count: {counter}</button>
        </>
    
        : 
        "loading"
    }
    </h1>
}

export default App
