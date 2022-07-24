import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import {useEffect, useRef, useState } from 'react';
import Alert from 'react-bootstrap/Alert';
import { useNavigate } from 'react-router';
import axios from 'axios';
import { listFormData, SPINNERS_BORDER_HTML} from '../utilities';

const EditModal = ({ hideModal, edit, editBank, token }) => {
    const [form, setForm] = useState({});
    const url = process.env.REACT_APP_SERVER_URL + "/edit";
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
        if (edit?.bank) setForm({...edit.bank})
        
        abortControllerRef.current = new AbortController();
        return () => abortControllerRef.current.abort();
    }, [edit.bank]);

    const handleChange = (event) => {
        setForm(s => ({
            ...s,
            [event.target.id]: event.target.value
        }))
    }

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
                editBank(res.data);
                setAlert(s => ({ ...s, variant: "success", show: true, message: "Bank saved!" }));
            })
            .catch(error => {
                const response = error?.response;
                if (response.status === 406) navigate("/login/1");
                const message = response.data.message ?? "Something went wrong";
                setAlert(s => ({...s, variant: "danger", show: true, message}))
            })
            .finally(() => {
                button.innerHTML = text;
                button.disabled = false;
            })
    }

    return (
        <>
            <Modal show={edit.show} onHide={()=>hideModal('edit')}>
                <Modal.Header closeButton>
                    <Modal.Title>Edit Bank (ID : {edit.bank.id})</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Alert ref={alertRef} tabIndex={-1} variant={alert.variant} show={alert.show} dismissible onClose={toggleAlert}>
                        {alert.message}
                    </Alert>
                    <Form onSubmit={handleSubmit}>
                        <input type="hidden" name="id" value={form?.id} />
                        <Form.Group className="mb-3" controlId="fullName">
                            <Form.Label>Bank name</Form.Label>
                            <Form.Control value={form?.fullName ?? ""} name="fullName"  onChange={handleChange} type="text" placeholder="Enter full name" required minLength="3" />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="shortName">
                            <Form.Label>Short name</Form.Label>
                            <Form.Control value={form?.shortName ?? ""} name="shortName"  onChange={handleChange} type="text" placeholder="Enter short name" required minLength="3" />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="type">
                            <Form.Label>Bank type</Form.Label>
                            <Form.Control value={form?.type ?? ""} name="type"  onChange={handleChange} type="text" placeholder="Enter type" required minLength="3" />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="sortCode">
                            <Form.Label>Sort code</Form.Label>
                            <Form.Control value={form?.sortCode ?? ""} name="sortCode" onChange={handleChange} type="number" placeholder="Enter sort code" required minLength="3" />
                        </Form.Group>
                        <Button variant="primary" type="submit">
                            Save Changes
                        </Button>
                    </Form>
                </Modal.Body>
            </Modal> 
        </>
     );
}

export default EditModal;
