import React from 'react';
import $ from 'jquery';
import MainPage from '../MainPage/MainPage';
import ReactDOM from 'react-dom';
import LoggedInAs from '../LoggedInAs/LoggedInAs';
import Loading from '../Loading/Loading';
import SlideInRight from '../Transitions/SlideInRight';
import Button from '../Button';
import ErrorMessage from '../Message/ErrorMessage';

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
                this.props.onSignIn(account);
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
                            onSignOut={() => {
                                this.setState({loggedIn: false})
                                this.props.onSignOut()
                            }}
                />
            )
        }

        if (this.state.loading) {
            return (
                <div style={{textAlign: 'center', marginLeft: 'auto', marginRight: 'auto', marginTop: '1em', verticalAlign: 'middle'}}>
                    <Loading />
                </div>
            )
        }
        return (
            <SlideInRight>
                <div style={{textAlign: 'center', marginLeft: 'auto', marginRight: 'auto', marginTop: '1em'}}>

                    <form className="form-horizontal" onSubmit={this.handleSubmit.bind(this)}
                        style={{
                            maxWidth: '25em',
                            padding: '3em',
                            paddingBottom: 0,
                            display: 'block',
                            margin: 'auto',
                            marginTop: '1em',
                            marginBottom: '1em',
                        }}
                    >
                        <div className="material-icons primary" style={{fontSize: "5em", textAlign: 'center', color: 'gray'}}>
                            account_circle
                        </div>
                        <ErrorMessage display={this.state.wrongDetailsFilled} message="Incorrect username or password" />
                        <div className="form-group">
                            <input className="form-control" type="text" placeholder="Enter your username" value={this.state.name}
                                   onChange={ this.onChange.bind(this, 'name')}
                                   style={{padding: '1em 0.5em'}} />
                        </div>
                        <div className="form-group">
                            <input className="col-xs-10 form-control" type="password" placeholder="Enter your password" value={this.state.password}
                                   onChange={this.onChange.bind(this, 'password')}
                                   style={{padding: '1em 0.5em'}} />
                        </div>
                        <Button className="paper-button" type="submit" style={{padding: '0.5em', width: '100%', display: 'flex', alignItems: 'center', textAlign: 'center'}}>
                            <span>Continue</span> <span style={{width: '1em'}} className="material-icons">arrow_forward</span>
                        </Button>
                    </form>
                </div>
            </SlideInRight>
        );
    }
}
