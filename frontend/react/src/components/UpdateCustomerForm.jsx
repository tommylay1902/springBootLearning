import {Form, Formik, useField} from 'formik';
import * as Yup from 'yup';
import {Alert, AlertIcon, Box, Button, FormLabel, Input, Select, Stack} from "@chakra-ui/react";
import {updateCustomerWithId} from "../services/client.js";
import {successNotification, errorNotification} from "../services/notification.js";
import React from 'react';

const MyTextInput = ({label, ...props}) => {
    // useField() returns [formik.getFieldProps(), formik.getFieldMeta()]
    // which we can spread on <input>. We can use field meta to show an error
    // message if the field is invalid and it has been touched (i.e. visited)
    // @ts-ignore
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
    // @ts-ignore
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

export const UpdateCustomerForm = ({id, name, email, age, gender, fetchCustomers}) =>
    {
        return (
            <Formik
            initialValues={{
                name: name,
                email: email,
                age: age,
                gender: gender.toUpperCase()
            }}
            validationSchema={Yup.object({
                name: Yup.string()
                    .max(25, 'Must be 25 characters or less')
                    .required('Required'),
                age: Yup.number()
                    .min(16, 'Must be at least 16 years of age')
                    .max(100, 'Must be less than 100 years of age')
                    .required(),
                email: Yup.string()
                    .email('Must be 25 characters or less')
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
                        <Button disabled={!isValid || isSubmitting} type="submit" colorScheme='teal'>Submit</Button>
                    </Stack>
                </Form>
            )}
        </Formik>
        )
  
}
                            