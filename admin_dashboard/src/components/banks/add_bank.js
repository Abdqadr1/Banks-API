import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import { useEffect, useRef, useState } from 'react';
import Alert from 'react-bootstrap/Alert';
import { listFormData, SPINNERS_BORDER_HTML } from '../utilities';
import axios from 'axios';
import { useNavigate } from 'react-router';

const AddModal = ({ hideModal, show, addBank, token }) => {
    const url = process.env.REACT_APP_SERVER_URL + "/add";
    const navigate = useNavigate();
    const abortControllerRef = useRef();
    const alertRef = useRef();
    const [alert, setAlert] = useState({ show: false, message: "", variant: "success" });
    const toggleAlert = () => {
        setAlert({...alert, show: !alert.show})
    }

    useEffect(() => {
        if (!alert.show) return;
        alertRef.current && alertRef.current.focus()
    }, [alert]);

    useEffect(() => {
        abortControllerRef.current = new AbortController();
        return () => abortControllerRef.current.abort();
    }, []);

    const handleSubmit = (event) => {
        event.preventDefault();
        const target = event.target;
        const data = new FormData(target);
        listFormData(data);
        const button = target.querySelector("button");
        button.disabled = true;
        const text = button.textContent;
        button.innerHTML = SPINNERS_BORDER_HTML;

        axios.post(url, data, {
            headers: {
                "Authorization" : "Bearer " + token
            },
            signal: abortControllerRef.current.signal
        })
        .then(res => {
            addBank(res.data);
            setAlert(s => ({ ...s, variant: "success", show: true, message: "Bank saved!" }));
        }).catch(error => {
            const response = error?.response;
            if (response.status === 406) navigate("/login/1");
            const message = response.data.message ?? "Something went wrong";
            setAlert(s => ({...s, variant: "danger", show: true, message}))
        })
        .finally(() => {
            button.disabled = false;
            button.textContent = text;
        })
    }

    return (
        <>
            <Modal show={show} onHide={()=>hideModal('add')}>
                <Modal.Header closeButton>
                    <Modal.Title>Add Bank</Modal.Title>
                </Modal.Header>
                <Modal.Body >
                    <Alert className="text-center" ref={alertRef} tabIndex={-1} variant={alert.variant} show={alert.show} dismissible onClose={toggleAlert}>
                        {alert.message}
                    </Alert>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3" controlId="fullName">
                            <Form.Label>Bank name</Form.Label>
                            <Form.Control name="fullName" type="text" placeholder="Enter full name" required minLength="3"/>
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="shortName">
                            <Form.Label>Short name</Form.Label>
                            <Form.Control name="shortName" type="text" placeholder="Enter short name" required minLength="3"/>
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="type">
                            <Form.Label>Bank type</Form.Label>
                            <Form.Control name="type" type="text" placeholder="Enter type" required minLength="3"/>
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="sortCode">
                            <Form.Label>Sort code</Form.Label>
                            <Form.Control name="sortCode" type="number" placeholder="Enter sort code" required minLength="3"/>
                        </Form.Group>
                        <Button variant="primary" type="submit">
                            Save
                        </Button>
                    </Form>
                </Modal.Body>
            </Modal> 
        </>
     );
}
export default AddModal;