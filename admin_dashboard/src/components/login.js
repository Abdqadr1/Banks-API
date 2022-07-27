import { useNavigate, useParams } from "react-router-dom";
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Alert from 'react-bootstrap/Alert';
import Button from 'react-bootstrap/Button';
import axios from 'axios';
import '../css/login.css'
import {useEffect, useRef, useState } from "react";
import { listFormData, SPINNERS_BORDER_HTML } from "./utilities";

const Login = () => {
    const abortControllerRef = useRef();
    const { id } = useParams();
    const [alert, setAlert] = useState({ show: false, message: "", variant: "danger" });
    const alertRef = useRef();
    const toggleAlert = () => {
        setAlert({...alert, show: !alert.show})
    }
    const navigate = useNavigate();

    const handleSubmit = event => {
        event.preventDefault();
        const target = event.target;
        const data = new FormData(target);
        listFormData(data);
        const button = target.querySelector("button");
        button.disabled = true;
        const text = button.textContent;
        button.innerHTML = SPINNERS_BORDER_HTML;
        // on error
        axios.post("/bank/authenticate", data, {
            signal: abortControllerRef.current.signal
        })
            .then(response => {
                sessionStorage.setItem("user", JSON.stringify(response.data))
                navigate('/banks');
        }).catch(error => {
            setAlert({
                message: error?.response.data.message,
                variant: 'danger',
                class: 'text-center py-2'
            })
            button.disabled = false;
            button.textContent = text;
        })
    }

     useEffect(() => {
        if (!alert.show) return;
        alertRef.current && alertRef.current.focus()
    }, [alert])

    useEffect(() => {
        if (id && id === "1") {
            setAlert(s=>({...s, show: true, message: "Session expired" }))
        }
    }, [id])

    useEffect(() => {
        abortControllerRef.current = new AbortController();
        return () => abortControllerRef.current.abort();
    },[])

    return ( 
        <Container>
            <Row className="parent">
                <Col xs={9} md={7} lg={6} className='border p-3 rounded'>
                    <h3 className='text-center fw-bold'>Login</h3>
                    <Alert ref={alertRef} tabIndex={-1} variant={alert.variant} show={alert.show} dismissible onClose={toggleAlert}>
                        {alert.message}
                    </Alert>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3" controlId="username">
                            <Form.Label className='text-start d-block'>Username</Form.Label>
                            <Form.Control name="username" type="text" placeholder="Enter username" required/>
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="password">
                            <Form.Label className='text-start d-block'>Password</Form.Label>
                            <Form.Control name="password" type="password" placeholder="Enter password" required/>
                        </Form.Group>
                        <Button className='d-block px-4' variant="secondary" type="submit">
                            Log in
                        </Button>
                    </Form>
                </Col>
            </Row>
        </Container>
    )
}
 
export default Login;