import React from "react";
import { Row, Col, Button } from "react-bootstrap";
class Bank extends React.Component{

    constructor(props) {
        super(props);
        this.updateStatus = props.updateStatus;
        this.enabled = props.enabled
            ? <span 
                onClick={()=> this.updateStatus(props.id, false)} 
                className="material-icons text-success fs-3">
                    check_circle
                </span>
            : <span
                onClick={()=> this.updateStatus(props.id, true)} 
                className="material-icons text-secondary fs-3">circle</span>

    }

    tableItem(prop) {
        return (
             <tr>
                <td>{prop.name }</td>
                <td>{prop.alias }</td>
                <td className="d-md-none d-lg-table-cell">{prop.type }</td>
                <td>{prop.code }</td>
                <td className="d-md-none d-lg-table-cell">{prop.longCode }</td>
                <td>{this.enabled}</td>
                <td>
                    <Row className='justify-content-center'>
                        <Col className="d-flex justify-content-center">
                            <Button className="mx-1 p-1 action" variant="outline-primary" title="edit" onClick={() => prop.showModal('edit', prop.id)}>
                                <span className="material-icons pb-0">edit</span>
                            </Button>
                            <Button className="mx-1 p-1 action" variant="outline-danger" title="delete" onClick={() => prop.showModal('delete', prop.id)}>
                                <span className="material-icons pb-0">delete</span>
                            </Button>
                        </Col>
                    </Row>
                </td>
            </tr>
        );
    }

    rowItem(prop) {
          return (
            <Row className="my-2 justify-content-between">
                <Col xs="5" className="text-center fw-bold">
                    <div>{prop.code}</div>
                </Col>
                <Col xs="7">
                    <div className="d-block mb-3">{prop.name}</div>
                    <div className="d-block mb-3">{prop.tye}</div>
                    <div className="justify-content-start d-flex">
                        <div className="me-2">{this.enabled}</div>
                        <Button className="me-2 p-1 action" variant="outline-primary" title="edit" onClick={() => prop.showModal('edit', prop.id)}>
                            <span className="material-icons pb-0">edit</span>
                        </Button>
                        <Button className="me-2 p-1 action" variant="outline-danger" title="delete" onClick={() => prop.showModal('delete', prop.id)}>
                            <span className="material-icons pb-0">delete</span>
                        </Button>
                    </div>
                </Col>
            </Row>
        )
    }

    render() {
        return (this.props.viewType === 'detailed') ? this.tableItem(this.props) : this.rowItem(this.props);
    }
}

export default Bank;