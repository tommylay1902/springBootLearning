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
    const [isLoading, setIsLoading] = useState(true)
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
        setIsLoading(true)
        setTimeout(() => {
            setIsLoading(false)
        }, 2000)
       
    }, [])
    if(isLoading){
        return "loading..."
    }
    return  (
    <>
        <UserProfiles users={users}/>
        <button onClick={handleCounterClick}>count: {counter}</button>
    </>)
}

export default App
