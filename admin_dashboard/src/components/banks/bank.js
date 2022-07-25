import React from "react";
import { Row, Col, Button } from "react-bootstrap";
class Bank extends React.Component{

    render() {
        let props = this.props;
        const updateStatus = props.updateStatus;
        const enabled = props.enabled
            ? <span 
                onClick={()=> updateStatus(props.id, false)} 
                className="material-icons text-success fs-3">
                    check_circle
                </span>
            : <span
                onClick={()=> updateStatus(props.id, true)} 
                className="material-icons text-secondary fs-3">circle</span>
        return (
             <tr>
                <td>{props.name }</td>
                <td>{props.alias }</td>
                <td>{props.type }</td>
                <td>{props.code }</td>
                <td>{props.longCode }</td>
                <td>{enabled}</td>
                <td>
                    <Row className='justify-content-center'>
                        <Col className="d-flex justify-content-center">
                            <Button className="mx-1 p-1 action" variant="outline-primary" title="edit" onClick={() => props.showModal('edit', props.id)}>
                                <span className="material-icons pb-0">edit</span>
                            </Button>
                            <Button className="mx-1 p-1 action" variant="outline-danger" title="delete" onClick={() => props.showModal('delete', props.id)}>
                                <span className="material-icons pb-0">delete</span>
                            </Button>
                        </Col>
                    </Row>
                </td>
            </tr>
        );
    }
}

export default Bank;