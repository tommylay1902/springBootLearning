import React from 'react'

const UserProfile = ({name, age, gender, ...props}) => {
  return (
    <div>
        <p>{name}</p>
        <p>{age}</p>
        <img src={`https://randomuser.me/api/portraits/${gender}/75.jpg`}/>
        {props.children}
    </div>
  )
}

export default UserProfile