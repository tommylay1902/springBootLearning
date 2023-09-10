
import {
    Heading,
    Avatar,
    Box,
    Center,
    Image,
    Flex,
    Text,
    Stack,
    Tag,
    useColorModeValue,
    Button,
    Modal,
    ModalOverlay,
    ModalContent,
    ModalHeader,
    ModalCloseButton,
    ModalBody,
    ModalFooter,
    useDisclosure,
    HStack,
    Alert,
    AlertIcon,
    FormLabel,
    Input,
    Select
} from '@chakra-ui/react';
import * as PropTypes from "prop-types";
import {deleteCustomerWithId, updateCustomerWithId} from "../services/client.js";
import {successNotification, errorNotification} from "../services/notification.js";
import { useState } from 'react';
import {Form, Formik, useField} from 'formik';
import * as Yup from 'yup';


const MyTextInput = ({label, ...props}) => {
    // useField() returns [formik.getFieldProps(), formik.getFieldMeta()]
    // which we can spread on <input>. We can use field meta to show an error
    // message if the field is invalid and it has been touched (i.e. visited)
    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Input className="text-input" {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2}>
                    <AlertIcon/>
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
};

const MySelect = ({label, ...props}) => {
    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Select {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2}>
                    <AlertIcon/>
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
};


export default function CardWithImage({id, name, email, age, gender, fetchCustomers}) {
    const { isOpen, onOpen, onClose } = useDisclosure()
    const [isUpdateModalOpen, setIsUpdateModalOpen] = useState(false)
    const deleteCustomer = async (id) => {
        deleteCustomerWithId(id)
            .then( res => {
                successNotification("Success!", "Successfully deleted customer");
                fetchCustomers();
            }
            )
            .catch(e => {
                errorNotification(
                    e.code,
                    e.response.data.message
                )
            })
            .finally();
    }
    return (
        <Center py={6}>
            <Box
                maxW={'300px'}
                w={'full'}
                bg={useColorModeValue('white', 'gray.800')}
                boxShadow={'2xl'}
                rounded={'md'}
                overflow={'hidden'}
                width={"300px"} // Set a fixed width for the card
                height={"420px"} // Set a fixed height for the card
                margin={"16px"} // Add margin for spacing between cards
                display={"flex"} // Use Flexbox for the card layout
            
                flexDirection={"column"}
                >
                <Image
                    h={'120px'}
                    w={'full'}
                    src={
                        'https://images.unsplash.com/photo-1612865547334-09cb8cb455da?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80'
                    }
                    objectFit={'cover'}
                />
                <Flex justify={'center'} mt={-12}>
                    <Avatar
                        size={'xl'}
                        src={
                            'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?ixlib=rb-1.2.1&q=80&fm=jpg&crop=faces&fit=crop&h=200&w=200&ixid=eyJhcHBfaWQiOjE3Nzg0fQ'
                        }
                        alt={'Author'}
                        css={{
                            border: '2px solid white',
                        }}
                    />
                </Flex>

                <Box p={6}>
                    <Stack spacing={2} align={'center'} mb={5}>
                        <Tag borderRadius={"full"}>{id}</Tag>
                        <Heading fontSize={'2xl'} fontWeight={500} fontFamily={'body'}>
                            {name}
                        </Heading>
                        <Text color={'gray.500'}>{email}</Text>
                        <Text color={'gray.500'}>Age {age}</Text>
                        <Text color={'gray.500'}>Gender {gender}</Text>
                        <HStack>
                            <Button colorScheme={"red"} onClick={onOpen} >Delete</Button>
                            <Button colorScheme={"teal"} onClick={() => setIsUpdateModalOpen(true)}>Update</Button>
                        </HStack>
                    </Stack>
                </Box>
            </Box>

            <Modal isOpen={isOpen} onClose={onClose}>
                <ModalOverlay />
                <ModalContent>
                    <ModalHeader>Delete a customer</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody>
                        Are you sure you want to delete a customer?
                    </ModalBody>

                    <ModalFooter>
                        <Button colorScheme='red' mr={3} onClick={() => deleteCustomer(id)}>
                            Delete Customer
                        </Button>
                        <Button variant='ghost' onClick={onClose}>Cancel</Button>
                    </ModalFooter>
                </ModalContent>
            </Modal>

            
            <Modal isOpen={isUpdateModalOpen} onClose={() => setIsUpdateModalOpen(false)}>
                <ModalOverlay />
                <ModalContent>
                    <ModalHeader>Update a customer</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody>
                       {/* input form */}
                        <Formik
                            initialValues={{
                                name: name,
                                email: email,
                                age: age,
                                gender: gender.toUpperCase()
                            }}
                            validationSchema={Yup.object({
                                name: Yup.string()
                                    .max(15, 'Must be 15 characters or less')
                                    .required('Required'),
                                age: Yup.number()
                                    .min(16, 'Must be at least 16 years of age')
                                    .max(100, 'Must be less than 100 years of age')
                                    .required(),
                                email: Yup.string()
                                    .email('Must be 20 characters or less')
                                    .required('Required'),
                                gender: Yup.string()
                                    .oneOf(
                                        ['MALE', 'FEMALE', 'OTHER'],
                                        'Invalid gender'
                                    )
                                    .required('Required'),
                            })}
                            onSubmit={(customer, {setSubmitting}) => {
                                setSubmitting(true);
                                console.log(customer)
                                updateCustomerWithId(id,customer).then(() => {
                                    successNotification("Success", "Successfully updated customer")
                                }).catch(err => {
                                    errorNotification(err.code,err.response.message)
                                })
                                .finally(() => {
                                    setSubmitting(false)
                                })
                                fetchCustomers();
                            }}
                        >
                            {({isValid, isSubmitting}) => (
                                <Form>
                                    <Stack spacing={"24px"}>
                                        <MyTextInput
                                            label="Name"
                                            name="name"
                                            type="text"
                                            placeholder="Jane"
                                        />

                                        <MyTextInput
                                            label="Email Address"
                                            name="email"
                                            type="email"
                                            placeholder="jane@formik.com"
                                        />

                                        <MyTextInput
                                            label="Age"
                                            name="age"
                                            type="number"
                                            placeholder="20"
                                        />

                                        <MySelect label="Gender" name="gender">
                                            <option value="MALE">Male</option>
                                            <option value="FEMALE">Female</option>
                                            <option value="OTHER">Other</option>
                                        </MySelect>

                                        <Button disabled={!isValid || isSubmitting} type="submit">Submit</Button>
                                    </Stack>
                                </Form>
                            )}
                        </Formik>
                    </ModalBody>

                    <ModalFooter>
                    </ModalFooter>
                </ModalContent>
            </Modal>
        </Center>
    );
}
