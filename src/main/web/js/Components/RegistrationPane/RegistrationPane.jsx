import React from 'react';
import RegistrationForm from '../RegistrationForm';
import Secondary from '../Secondary';
import './pane.sass';

export default class RegistrationPane extends React.Component {
	constructor(props) {
		super(props);
	}

	render() {
		return (
				<div className="mainPane">
					<div className="leftPane">
						<RegistrationForm />
					</div>
					<div className="rightPane">
						<Secondary text="Register to have numerous benefits" />
					</div>
				</div>
		)
	}
}

