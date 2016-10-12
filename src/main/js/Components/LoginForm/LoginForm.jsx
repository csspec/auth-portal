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

    componentDidMount() {
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

    onChange(value, e) {
        var nextState = {};
        nextState[value] = e.target.value;
        this.setState(nextState);
    }

    redirectHandler(data) {
        if (this.state.client_id.length === 0)
            window.location = '/home';

        // TODO: since grant_type will not be limited to just 'implicit' and 'code'
        const request_endpoint = window.grant_type === 'code' ? 'issue_code' : 'access_token';
        $.ajax({
            url: '/oauth/' + request_endpoint,
            type: 'GET',
            beforeSend: (request) => {
                let header = "client_id=" + this.state.client_id;
                header += '&grant_type=' + window.grant_type;
                request.setRequestHeader('x-auth-token', header);
            },
            success: (data) => {
                // will redirect the user to the actual link
                window.location = this.state.redirect_link.length === 0 ? '/home' : (this.state.redirect_link
                                        + "#" + (data.code != null ? data.code : data.access_token));
            },
            error: () => {
                this.setState({ unsatisfiedRequest: true, loading: false });
            }
        });
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
            success: this.redirectHandler.bind(this),
            error: () => {
                this.setState({ wrongDetailsFilled: true, loading: false });
            }
        });
    }

    render() {
        if (!this.state.loading && this.state.loggedIn && !this.state.unsatisfiedRequest) {
            this.redirectHandler();
            return (
                <LoggedInAs account={this.state.account} link={this.state.redirect_link} />
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
