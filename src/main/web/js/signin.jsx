"use strict";

import React from 'react';
import ReactDOM from 'react-dom';
import LoginForm from './Components/LoginForm/LoginForm';
const styles = { maxWidth: '400px', display: 'block', margin: 'auto', marginTop: '1em', verticalAlign: 'middle' };

class App extends React.Component {
    render() {
        return (
            <div className="container-fluid">
                <div className="well" style={styles}>
                    <LoginForm/>
                </div>
            </div>
        )
    }
}

ReactDOM.render(<App />, document.getElementById('react-mount-point'));
