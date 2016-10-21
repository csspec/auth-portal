import React from 'react';
import $ from 'jquery';
import MainPage from '../MainPage/MainPage';
import ReactDOM from 'react-dom';
import LoggedInAs from '../LoggedInAs/LoggedInAs';
import Loading from '../Loading/Loading';

export default class LoginForm extends React.Component {
    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);

        const params = window.location.search.slice(1).split('&');

        let redirect_uri = "";
        let client_id = "";
        for (const index in params) {
            let param = params[index];
            let query = param.split('=');
            if (query.length != 2) {
                continue;
            }

            if (query[0] === 'redirect_uri') {
                redirect_uri = query[1];
                continue;
            }

            if (query[0] === 'client_id') {
                client_id = query[1];
            }
        }

        this.state = {
            name: "",
            password: "",
            loggedIn: false,
            wrongDetailsFilled: false,
            unsatisfiedRequest: false,
            redirect_link: redirect_uri,
            client_id: client_id,
            loading: true
        };
    }

    checkAccount() {
        $.ajax({
            url: '/auth/check',
            method: 'GET',
            success: account => {
                this.setState({ account: account, loggedIn: true, loading: false });
            },
            error: e => {
                this.setState({ loggedIn: false, loading: false });
            }
        });
    }

    componentDidMount() {
        this.checkAccount();
    }

    onChange(value, e) {
        var nextState = {};
        nextState[value] = e.target.value;
        this.setState(nextState);
    }

    handleSubmit(e) {
        e.preventDefault();
        $.ajax({
            url: "/auth/token",
            type: "POST",
            beforeSend: request => {
                request.setRequestHeader("Authorization",
                    "Basic " + btoa(this.state.name + ':' + this.state.password));
            },
            success: this.checkAccount.bind(this),
            error: () => {
                this.setState({ wrongDetailsFilled: true, loading: false });
            }
        });
    }

    render() {
        if (!this.state.loading && this.state.loggedIn && !this.state.unsatisfiedRequest) {
            return (
                <LoggedInAs account={this.state.account}
                            link={this.state.redirect_link}
                            redirect_link={this.state.redirect_link}
                            client_id={this.state.client_id}
                />
            )
        }

        if (this.state.loading) {
            return (
                <Loading />
            )
        }
        return (
            <form className="form-horizontal" onSubmit={this.handleSubmit.bind(this)}>
                <fieldset>
                    <legend>Login Form</legend>
                    <p className="bg-danger" style={{ display: this.state.wrongDetailsFilled ? 'block' : 'none', padding: '5px'}}>
                        Incorrect username or password
                    </p>
                    <p className="bg-danger" style={{ display: this.state.loggedIn && this.state.unsatisfiedRequest ? 'block' : 'none', padding: '5px'}}>
                        Unknown client
                    </p>
                    <p className="bg-danger" style={{ display: this.state.unsatisfiedRequest ? 'block' : 'none', padding: '5px'}}>
                        Unknown error occurred
                    </p>
                    <div className="form-group">
                        <div className="container-fluid">
                            <input className="form-control" type="text" placeholder="Username or EmailID" value={this.state.name}
                                   onChange={ this.onChange.bind(this, 'name')} />
                        </div>
                    </div>
                    <div className="form-group">
                        <div className="container-fluid">
                            <input className="col-xs-10 form-control" type="password" placeholder="Password" value={this.state.password}
                                   onChange={this.onChange.bind(this, 'password')} />
                        </div>
                    </div>
                    <button className="btn btn-primary" type="submit">Submit</button>
                </fieldset>
            </form>
        );
    }
}
