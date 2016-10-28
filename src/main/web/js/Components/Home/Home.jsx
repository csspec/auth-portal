import React from 'react';
import $ from 'jquery';
import Button from '../Button';

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
        if (this.state.disabled)
            return;
        $.ajax({
            url: '/auth/logout',
            type: 'GET',
            success: (data) => {
                this.props.onSignOut();
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
                <Button onClick={this.handleClick} disabled={this.state.disabled}>{this.state.event}</Button>
            </div>
        )
    }
}

