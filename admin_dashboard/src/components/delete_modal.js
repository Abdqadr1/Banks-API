import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import { useRef } from 'react';
const DeleteModal = ({show, message, hideModal, id, deleteBank}) => {
    const buttonRef = useRef(null);
    const msgRef = useRef(null);
    const handleDelete = (id) => {
        deleteBank(id)
    }
    return ( 
        <>
            <Modal show={show} onHide={() => hideModal('delete')}>
                <Modal.Header closeButton>
                    <Modal.Title>Confirmation</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p ref={msgRef}>{message ?? "Are you sure you want to delete this item?"}</p>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary"  onClick={() => hideModal('delete')}>Cancel</Button>
                    <Button variant="danger" ref={buttonRef}  onClick={() => handleDelete(id)}>Delete</Button>
                </Modal.Footer>
            </Modal>
        </>
        
     );
}

export default DeleteModal;
