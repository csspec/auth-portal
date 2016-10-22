import { Component } from 'react';

export default class Button extends Component {
    render() {
        return (
            <button className="btn btn-primary" onClick={this.props.onClick}>
                {this.props.label}
            </button>
        );
    }
}
