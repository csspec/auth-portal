import $ from 'jquery';
import LoginForm from '../LoginForm/LoginForm';
import Loading from '../Loading/Loading';
import Home from '../Home/Home';
import React from 'react';

export default class MainPage extends React.Component {
    constructor(props) {
        console.log("Creating MainPage");
        super(props);
        this.state = {
            loading: true,
            account: null,
            loggedIn: false
        };
        this.handleTryAgain = this.handleTryAgain.bind(this);
    }

    componentDidMount() {
        $.ajax({
            url: '/auth/check',
            method: 'GET',
            success: account => {
                this.setState({ loading: false, account: account, loggedIn: true });
            },
            error: e => {
                this.setState({ loading: false, loggedIn: false });
            }
        });

        $(document).on('try-again', this.handleTryAgain);
    }

    handleTryAgain() {
        $.ajax({
            url: '/auth/check',
            method: 'GET',
            success: account => {
                this.setState({ loading: false, account: account, loggedIn: true });
            },
            error: e => {
                this.setState({ loading: false, loggedIn: false });
            }
        });
    }

    render() {
        if (this.state.loading) {
            return (
                <Loading />
            );
        } else if (this.state.loggedIn) {
            return (
                <Home account={this.state.account} />
            );
        } else {
            return (
                <LoginForm />
            )
        }

    }
}
