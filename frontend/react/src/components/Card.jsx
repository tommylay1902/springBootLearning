
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
import React from 'react';
import { UpdateCustomerForm } from './UpdateCustomerForm.jsx';



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
                       <UpdateCustomerForm name={name} age={age} gender={gender} email={email} fetchCustomers={fetchCustomers} id={id}/>
                    </ModalBody>

                    <ModalFooter>
                    </ModalFooter>
                </ModalContent>
            </Modal>
        </Center>
    );
}
