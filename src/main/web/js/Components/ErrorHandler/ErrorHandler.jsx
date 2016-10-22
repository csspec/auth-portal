import React from 'react';

export default class ErrorHandler extends React.Component {
	render() {
		return (
			<div className="alert alert-danger">
				<strong>Error: </strong><code>{this.props.code}</code>
				{ " " + this.props.message}
				<div>
					{this.props.children}
				</div>
			</div>
		)
	}
}
