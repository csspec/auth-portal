import ReactDOM from 'react-dom';
import React, { Component } from 'react';

class AdminApp extends Component {
    render() {
        return (
            <h1>This is admin page. Only accessible to admin</h1>
        )
    }
}

ReactDOM.render(<AdminApp />, document.getElementById('react-mount-point'));
