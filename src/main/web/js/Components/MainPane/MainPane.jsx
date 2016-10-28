import React from 'react';
import LoginForm from '../LoginForm/LoginForm';
import Secondary from '../Secondary';
import './pane.sass';

export default class MainPane extends React.Component {
	constructor(props) {
		super(props);

		this.state = {
			signedIn: false
		}
	}

	handleSignIn(account) {
		this.setState({ signedIn: true });
	}

	handleSignOut() {
		this.setState({ signedIn: false });
	}

	render() {
		return (
				<div className="mainPane">
					<div className="leftPane">
						<LoginForm onSignIn={this.handleSignIn.bind(this)} onSignOut={this.handleSignOut.bind(this)}/>
					</div>
					<div className="rightPane">
						<Secondary signedIn={this.state.signedIn} />
					</div>
				</div>
		)
	}
}
