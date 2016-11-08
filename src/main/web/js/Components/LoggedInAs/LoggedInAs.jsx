import React from 'react';
import Home from '../Home/Home';
import $ from 'jquery';
import Button from '../Button';
import SlideInRight from '../Transitions/SlideInRight';

export default class LoggedInAs extends React.Component {
    handleClick() {
        if (this.props.client_id.length === 0)
            window.location = '/home';

        // TODO: since grant_type will not be limited to just 'token' and 'code'
        const request_endpoint = window.response_type === 'code' ? 'issue_code' : 'access_token';
        $.ajax({
            url: '/oauth/' + request_endpoint,
            type: 'GET',
            beforeSend: (request) => {
                let header = "client_id=" + this.props.client_id;
                header += '&response_type=' + window.response_type;
                request.setRequestHeader('x-auth-token', header);
            },
            success: (data) => {
                // will redirect the user to the actual link
                console.log(data);
                window.location = this.props.redirect_link.length === 0 ? '/home' : (this.props.redirect_link
                                        + "#" + (data.code != null ? data.code : data.access_token));
            },
            error: () => {
                this.setState({ unsatisfiedRequest: true, loading: false });
            }
        });
    }

    render() {
        let admin_page = '';
        console.log(this.props.account);
        return (
            <SlideInRight>
                <div key="loggedInAs" style={{display: 'flex', alignContent: 'center', width: '100%'}}>
                    <div className="account" style={{textAlign: 'center', display: 'block', margin: 'auto'}}>
                        <div className="material-icons" style={{fontSize: "5em", color: 'gray'}}>
                            account_circle
                        </div>
                        <h3>{this.props.account.username}</h3>
                        <Button onClick={this.handleClick.bind(this)} style={{ marginBottom: '1em' }}>
                            Continue
                        </Button>
                        <Home onSignOut={this.props.onSignOut} />
                        {admin_page}
                    </div>
                </div>
            </SlideInRight>
        );
    }
}
