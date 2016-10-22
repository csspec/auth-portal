import React from 'react';
import Loading from '../Loading';
import ErrorHandler from '../ErrorHandler';
import $ from 'jquery';

export default class AdminConsole extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			loading: true,
			accountList: [],
			error: false,
			errorMessage: {}
		}
	}

	componentDidMount() {
		$.ajax({
			url: '/accounts',
			success: list => {
				this.setState({ accountList: list, loading: false });
			},
			error: message => {
				this.setState({ error: true, errorMessage: message, loading: false });
			}
		});
	}

    render() {
    	if (this.state.loading) {
    		return (
    			<Loading width="1em" />
    		)
    	} else if (this.state.error) {
    		return (
    			<ErrorHandler code={this.state.errorMessage.code} message={this.state.errorMessage.message} />
    		)
    	} else {
    		const list = this.state.accountList.map((account, key) => {
    			return (
    				<li key={key} className="list-group-item">
    					<div className="row">
	    					<div className="col-xs-1">
	    						{key + 1}
	    					</div>
	    					<div className="col-xs-4">
		    					{account.username}
		    				</div>
		    				<div className="col-xs-2">
		    					{account.role}
		    				</div>
		    				<div className="col-xs-5">
		    					List of actions...
		    				</div>
	    				</div>
    				</li>
    			)
    		});

    		return (
    			<ul className="list-group">
    				{list}
    			</ul>
    		)
    	}
    }
}
