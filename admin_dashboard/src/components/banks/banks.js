import React from 'react';
import axios from "axios";
import Table from "react-bootstrap/Table";
import Bank from './bank';
import Container from 'react-bootstrap/Container';
import AddBank from './addBank'
import NavBar from '../navbar';
import MyPagination from '../traces/pagination';
import { Navigate } from 'react-router-dom';
import { SPINNERS_BORDER, SPINNERS_BORDER_HTML } from '../utilities';
import EditModal from './edit_bank';
import AddModal from './add_bank';
import DeleteModal  from '../delete_modal';

class Banks extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            banks: [],
            countries: [],
            edit: {show: false,bank: {}},
            delete: {bool: false,id: 0 },
            add: false,
            pageInfo: {
                number: 1, totalPages: 1, startCount: 1,
                endCount: null, totalElements: null, numberPerPage: 1
            },
            user: JSON.parse(localStorage.getItem('user')),
            loading: true
        };
        this.serverURl = process.env.REACT_APP_SERVER_URL;
        this.abortController = new AbortController();
    }

    showModal = (which, id) => {
        if (which === "edit") {
            const bank = this.state.banks.find(b => b.id === id);
            this.setState( state => ({
                    edit: {show:true, bank}
                })
            )
        }
        if (which === "delete") {
            this.setState( state => ({
                    delete: {bool:true, id}
                })
            )
        }
        if (which === "add") {
            this.setState( state => ({
                    add: true
                })
            )
        }
    }
    hideModal = (which) => {
        if (which === "edit") {
            this.setState(s => ({...s, edit : {...s.edit, show:false}}))
        } else if (which === "delete") {
            this.setState(s => ({...s, edit : {...s.delete, show:false}}))
        } else if (which === "add") {
            this.setState(state => (
                { add: false }
            ))
        }
    }
    delete = (id, button) =>  {
        button.innerHTML = SPINNERS_BORDER_HTML;
        button.disabled = true;
        axios.delete(`${this.serverURl}/delete/${id}`, {
            headers: {
                "Authorization" : "Bearer " + this.state.user?.access_token
            },
            signal: this.abortController.signal
        })
            .then(() => { 
                this.setState(state => ({
                    banks: state.banks.filter(bank => bank.id !== id),
                    delete: {bool:false, id: 0}
                }))
                button.innerHTML = "Delete";
                button.disabled = false;
            })
            .catch(error => {
                if (error.response) {
                    if (error.response.status === 406) this.setState(() => ({ user: {} }))
                    else {
                        this.setState(state => ({
                            delete: {...state.delete, message: error.response.data.error
                            }
                        }))
                    }
                    
                }
                button.innerHTML = "Delete";
                button.disabled = false;
            })
    }
    edit = (bank) => {
        const banks = this.state.banks;
        const index = banks.findIndex(b => b.id === bank.id);
        banks[index] = bank;
        this.setState(s => ({...s, banks: [...s.banks]}))
    }
    add = (bank) => {
       this.setState(s => ({...s, banks: [...s.banks, bank]}))
    }

    componentDidMount() {
        this.fetchBanks(1);
    }
    gotoPage = (number) => {
        this.fetchBanks(number);
    }

    fetchCountries() {
        if (this.state.countries.length < 1) {
            const countryUrl = process.env.REACT_APP_URL + "country/all";
            this.setState(s => ({ loading: true }));
            axios.get(countryUrl,{signal: this.abortController.signal})
                .then(res => {
                    const data = res.data;
                    this.setState({ countries: data });
            })
            .catch(error => {
                if (error?.response?.status === 406) this.setState(() => ({ user: { } } ))
            })
            .finally(() => {
                this.setState(s => ({loading: false}))
            })
        }
    }

    fetchBanks(pageNumber) {
        this.setState(s => ({ loading: true }));
        const url = `${this.serverURl}/page/${pageNumber}`;
         axios.get(url,{signal: this.abortController.signal})
             .then(res => {
                const data = res.data;
                 this.setState(s => ({
                     banks: data.banks,
                     pageInfo: {
                         endCount: data.endCount,
                         startCount: data.startCount,
                         totalPages: data.totalPages,
                         totalElements: data.totalElements,
                         numberPerPage: data.numberPerPage,
                         number: data.currentPage
                     }
                 }));
                 this.fetchCountries();
            })
            .catch(error => {
                if (error?.response?.status === 406) this.setState(() => ({ user: { } } ))
            })
            .finally(() => {
                this.setState(s => ({loading: false}))
            })
    }
    componentWillUnmount() {
        this.abortController.abort();
    }

    render() {
        if(!this.state.user?.access_token) return (<Navigate to={'/login'} />) 
        return (
            <>
                {
                    (this.state.loading)
                        ? <div className="mx-auto" style={{ height: "40vh", display: "grid" }}>{SPINNERS_BORDER}</div>
                        :
                        <React.Fragment>
                            <NavBar />
                            <AddBank showModal={this.showModal} />
                            <Container>
                                <Table striped bordered hover size="sm">
                                    <thead className="bg-light text-dark">
                                        <tr>
                                            <th>Name</th>
                                            <th>Alias</th>
                                            <th>Type</th>
                                            <th>Code</th>
                                            <th>Long Code</th>
                                            <th>Enabled</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody className='border-top-0'>
                                        {(this.state.banks.length < 1)
                                            ? <tr><td colSpan={5} className="text-center">No Bank found</td></tr>
                                            : this.state.banks.map(bank => <Bank key={bank.id} {...bank} showModal={this.showModal} />)}
                                    </tbody>
                                </Table>
                                <MyPagination pageInfo={this.state.pageInfo} go={this.gotoPage} />
                                <AddModal countries={this.state.countries} show={this.state.add} hideModal={this.hideModal} addBank={this.add} token={this.state.user?.access_token} />
                                <EditModal countries={this.state.countries} edit={this.state.edit} hideModal={this.hideModal} editBank={this.edit} token={this.state.user?.access_token} />
                                <DeleteModal delete={this.state.delete} hideModal={this.hideModal} />
                            </Container>
                        </React.Fragment>
                }
                
            </>
            
        );
    }
}

export default Banks;