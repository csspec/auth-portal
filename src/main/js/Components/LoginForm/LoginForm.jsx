import React from 'react';
import $ from 'jquery';
import MainPage from '../MainPage/MainPage';
import ReactDOM from 'react-dom';

export default class LoginForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            name: "",
            password: "",
        };

        this.handleSubmit = this.handleSubmit.bind(this);

        this.event = new Event('try-again');
    }

    onChange(value, e) {
        var nextState = {};
        nextState[value] = e.target.value;
        this.setState(nextState);
    }

    handleSubmit(e) {
        function redirectHandler(data) {
            if (window.redirect_to == null) {
                document.dispatchEvent(new Event('try-again'));
            } else {
                $.ajax({
                    url: '/oauth/issue_code',
                    type: 'GET',
                    beforeSend: (request) => {
                        request.setRequestHeader('x-auth-token', 'client_id=' + window.client_id);
                    },
                    success: (data) => {
                        window.location = window.redirect_to + '?code=' + data.code;
                    },
                    error: () => {
                    }
                });
            }
        }

        e.preventDefault();
        $.ajax({
            url: "/auth/token",
            type: "POST",
            beforeSend: request => {
                request.setRequestHeader("Authorization",
                    "Basic " + btoa(this.state.name + ':' + this.state.password));
            },
            success: redirectHandler,
            error: () => {}
        });
    }

    render() {
        return (
            <div className="form">
                <form onSubmit={this.handleSubmit.bind(this)}>
                    <input type="text" placeholder="Username or EmailID" value={this.state.name}
                           onChange={ this.onChange.bind(this, 'name')} />
                    <input type="password" placeholder="Password" value={this.state.password}
                           onChange={this.onChange.bind(this, 'password')} />
                    <button type="submit">Submit</button>
                </form>
            </div>
        );
    }
}
