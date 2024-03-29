import { Container, Row, Col, Table, Button, Alert } from "react-bootstrap";

function ReportList(props){
    const {reports, handleAddReport, addReportResponseOK} = props

    return (
        <Container>
            <Row>
                <Col>
                    <h1 className="text-center">Reports</h1>
                </Col>
                <Col>
                    <Button onClick={handleAddReport}>Generate new report</Button>
                </Col>
                {addReportResponseOK && <Alert>Report generation started, we will notify you via email when it's finished.</Alert>}
            </Row>
                {reports.length === 0 ? <p>No reports.</p> :
                <Table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>bids placed</th>
                            <th>generated at</th>
                            <th>generated by</th>
                        </tr>
                    </thead>
                    <tbody>
                    {reports.map(report => (
                        <tr key={report.id}>
                            <td>{report.id}</td>
                            <td>{report.bidsPlaced}</td>
                            <td>{report.generatedDate}</td>
                            <td>{(report.generatedBy && report.generatedBy.username) ? report.generatedBy.username : ""}</td>
                        </tr>
                    ))}
                    </tbody>
                </Table>
                }
        </Container>
    );
} 

export default ReportList