import React, { Component } from 'react';
import Button from '../Bootstrap/Button';

export default class LoginLink extends Component {
    render() {
        return (
            <a className="btn btn-primary" href="/login?redirect_uri=/home">Login</a>
        )
    }
}
