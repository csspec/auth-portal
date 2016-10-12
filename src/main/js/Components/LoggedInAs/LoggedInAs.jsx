import React from 'react';

export default class LoggedInAs extends React.Component {
    render() {
        return (
            <div className="account" style={{textAlign: 'center'}}>
                <div className="material-icons" style={{fontSize: "10em"}}>
                    account_circle
                </div>
                <h3>{this.props.account.username}</h3>
                <a className="btn btn-primary" href={this.props.link}>
                    Continue
                </a>
            </div>
        )
    }
}
