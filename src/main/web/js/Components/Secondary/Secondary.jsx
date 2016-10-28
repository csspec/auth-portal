import React from 'react';
import SlideInLeft from '../Transitions/SlideInLeft';

export default class Secondary extends React.Component {
    render() {
        return (
            <SlideInLeft>
                <div style={{textAlign: 'center', marginLeft: 'auto', marginRight: 'auto', marginTop: '1em', borderLeft: '2px solid lightgray', padding: '1em'}}>
                    <img src="/images/PEC-Logo.png" style={{ height: '70px' }} />
                    <h1 style={{marginBottom: 0, fontSize: '20px'}}>
                        One account.
                    </h1>
                    <h1 style={{marginTop: 0, fontSize: '20px'}}>
                        For everything inside PEC.
                    </h1>
                    <h4 style={{fontWeight: 'normal', fontSize: '14px'}}>
                        { this.props.text ? this.props.text : (this.props.signedIn ? <em>Signed In</em> : "Sign in to continue") }
                    </h4>
                </div>
            </SlideInLeft>
        );
    }
}
