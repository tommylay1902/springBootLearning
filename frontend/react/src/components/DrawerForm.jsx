import {Button} from "@chakra-ui/react"

const AddIcon = () => "+"

export const DrawerForm = () => {

    return (
        <Button
            leftIcon={<AddIcon/>}
            onClick={() => alert("customer button clicked")}
        >
            Create Customer
        </Button>
    )
}