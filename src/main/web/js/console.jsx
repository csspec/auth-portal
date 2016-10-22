import ReactDOM from 'react-dom';
import React, { Component } from 'react';
import AdminConsole from './Components/AdminConsole';

import '../sass/common.sass';

class AdminApp extends Component {
    render() {
        return (
            <AdminConsole />
        )
    }
}

ReactDOM.render(<AdminApp />, document.getElementById('react-mount-point'));
