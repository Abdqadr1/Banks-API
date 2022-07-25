import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Container from 'react-bootstrap/Container';

const AddBank = (props) => {
    return ( 
        <Container className='my-3'>
            <Row className="justify-content-start">
                <Col xs={6} md={4} className="d-flex justify-content-start">
                    <span className="material-icons fs-1 text-secondary" onClick={() => props.showModal("add")}>
                        note_add
                    </span>
                </Col>
            </Row>
        </Container>
        
     );
}
 
export default AddBank;