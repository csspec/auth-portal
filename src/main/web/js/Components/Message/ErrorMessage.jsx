import React from 'react';

export default class ErrorMessage extends React.Component {
	render() {
		return (
			<p className="danger" style={{ visibility: this.props.display ? '' : 'hidden', padding: '3px'}}>
                <small>{this.props.message}</small>
            </p>
		)
	}
}
