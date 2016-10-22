"use strict";

import React from 'react';
import ReactDOM from 'react-dom';
import LoginForm from './Components/LoginForm/LoginForm';
import '../sass/common.sass';
import RegistrationForm from './Components/RegistrationForm';

const styles = { maxWidth: '400px', display: 'block', margin: 'auto', marginTop: '1em', verticalAlign: 'middle' };

class App extends React.Component {
	constructor(props) {
		super(props);
		window.addEventListener('togglePage', this.handleToggle.bind(this), false);
		this.state = {
			value: true,
		}
	}

	handleToggle() {
		this.setState({ value: !this.state.value });
	}

    render() {
        return (
            <div className="container-fluid">
                <div className="well" style={styles}>
                    { this.state.value ? <LoginForm /> : <RegistrationForm /> }
                </div>
            </div>
        )
    }
}

ReactDOM.render(<App />, document.getElementById('react-mount-point'));
