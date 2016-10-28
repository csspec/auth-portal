"use strict";

import React from 'react';
import ReactDOM from 'react-dom';
import MainPane from './Components/MainPane';
import RegistrationPane from './Components/RegistrationPane';
import '../sass/common.sass';
import Button from './Components/Button';

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
        	<div style={{width: '100%', height: '100%'}}>
	        	{ this.state.value ? <MainPane /> : <RegistrationPane /> }
	        	<div style={{width: '10em', display: 'block', margin: 'auto'}}>
	        		<Button onClick={this.handleToggle.bind(this)} style={{
	        			fontSize: '12px',
	        			width: '100%',
	        			textAlign: 'center',
	        			fontWeight: '900'
	        		}}>{(this.state.value ? 'New' : 'Old') + ' User?'}</Button>
	        	</div>
        	</div>
        )
    }
}

ReactDOM.render(<App />, document.getElementById('react-mount-point'));
