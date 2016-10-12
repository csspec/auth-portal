import React from 'react';
import $ from 'jquery';

export default class Home extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            event: "Logout",
            disabled: false
        };
        this.handleClick = this.handleClick.bind(this);
    }
    handleClick() {
        console.log("Logging out " + this.props.account.username);

        $.ajax({
            url: '/auth/logout',
            type: 'GET',
            success: (data) => {
                document.dispatchEvent(new Event('try-again'))
            },
            error: (error) => {
                console.log(error);
                this.setState({event: "Logout", disabled: false});
            }
        });
        this.setState({event: "Logging out", disabled: true});
    }

    render() {
        return (
            <div className="homepage">
                <h1 className="header">Hello {this.props.account.username}</h1>
                <button className="btn btn-primary" onClick={this.handleClick} disabled={this.state.disabled}>{this.state.event}</button>
            </div>
        )
    }
}

