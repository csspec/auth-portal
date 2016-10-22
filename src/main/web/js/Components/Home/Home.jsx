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
        $.ajax({
            url: '/auth/logout',
            type: 'GET',
            success: (data) => {
                window.location = '/login'
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
                <button className="btn btn-danger" onClick={this.handleClick} disabled={this.state.disabled}>{this.state.event}</button>
            </div>
        )
    }
}

