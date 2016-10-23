import React from 'react';
import ErrorHandler from '../ErrorHandler';
import Loading from '../Loading';
import $ from 'jquery';
import GoogleLogin from 'react-google-login';

export default class RegistrationForm extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			googleProfile: {},
			googleSignedIn: false,
			authenticating: false,
            authenticated: false,
			username: '',
			password: '',
			validUsername: true,
			accountCreated: false,
			errorMessage: null
		};
	}

	componentDidMount() {
		gapi.signin2.render('g-signin2', {
			'width': 200,
			'height': 50,
			'longtitle': true,
			'theme': 'dark',
			'onsuccess': this.onGoogleSignIn.bind(this),
			'onfailure': (error) => console.log(error)
		}); 
	}

	validateAccount(token) {
		$.ajax({
            url: '/auth/googlesignin/verify',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                token: token
            }),
            success: status => this.setState({ authenticating: false, authenticated: true }),
            error: status => this.setState({ /* TODO: handle some error here */ })
        });
        console.log("Verifying the email id on server");
	}

	onGoogleSignIn(googleUser) {
        // Useful data for your client-side scripts:
        var profile = googleUser.getBasicProfile();

        let p = {
        	id: profile.getId().toString(),
        	name: profile.getName().toString(),
        	image: profile.getImageUrl().toString(),
        	email: profile.getEmail().toString()
        }
        console.log(p);
        // The ID token you need to pass to your backend:
        var id_token = googleUser.getAuthResponse().id_token;
        console.log("ID Token: " + id_token);
        this.setState({
        	googleProfile: p,
        	googleSignedIn: true,
        	authenticating: true,
        	username: p.email.match(/^([^@]*)@/)[1],
        	idToken: id_token,
        });

        this.validateAccount(id_token);
    };

    checkUsernameAvailability(name) {
        if (name.length === 0)
            return;
    	$.ajax({
    		url: '/accounts/exists/' + name,
    		success: status => {
                console.log(status);
    			this.setState({validUsername: !status.exists})
    		},
            error: () => {}
    	})
    }

    onChange(value, e) {
        var nextState = {};
        nextState[value] = e.target.value;

        if (value === 'username') {
        	this.checkUsernameAvailability(e.target.value);
        }
        this.setState(nextState);
    }

    handleSubmit(event) {
    	event.preventDefault();
    	if (!this.state.matchedPassword
    		|| !this.state.validUsername
    		|| (this.state.username.length < 6)
    		|| (this.state.password.length < 6)) {
    		// TODO: show some red boxes here
			console.log("Error creating account: ", this.state);
    		return;
    	}
    	const data = JSON.stringify({
    			username: this.state.username,
    			password: this.state.password,
    			email: this.state.googleProfile.email
		});
		console.log(data);
    	$.ajax({
    		url: '/accounts',
    		type: 'POST',
    		contentType: 'application/json',
    		success: response => {
    			this.setState({
    				accountCreated: true,
    				loading: false,
    			});
    		},
    		data: data,
    		error: message => {
    			this.setState({ loading: false, errorMessage: message });
    		}
    	});
    	this.setState({ loading: true });
    }

    verifyPassword(e) {
    	this.setState({
    		matchedPassword: e.target.value === this.state.password
    	})
    }

	render() {
		if (this.state.authenticating || this.state.loading) {
			return (
				<Loading />
			)
		} else if (!this.state.googleSignedIn) {
			return (
				<div id="g-signin2" />
			)
		} else if (this.state.errorMessage !== null) {
			return (
				<ErrorHandler code={ this.state.errorMessage.code } message={ this.state.errorMessage.message }/>
			)
		} else if (!this.state.authenticated) {
            return (
                <ErrorHandler code='Error' message="Cannot verify your email ID">
                    <p>Invalid email id</p>
                </ErrorHandler>
            )
        } else if (this.state.accountCreated) {
			return (
				<ErrorHandler code='OK' message="Account successfully created">
					<p>Please Login into your account now</p>
				</ErrorHandler>
			)
		}
		return (
            <div style={{textAlign: 'center', marginLeft: 'auto', marginRight: 'auto', marginTop: '1em'}}>
                <img src="/images/PEC-Logo.png" style={{ height: '70px' }} />
                <h1 style={{
                        fontWeight: '200',
                        fontSize: '3.2em'
                    }}
                >
                    One account. All of PEC
                </h1>

                <h4 style={{fontWeight: '300', fontSize: '20px'}}>
                    Register
                </h4>

    			<form onSubmit={this.handleSubmit.bind(this)}
                        style={{
                            width: '25em',
                            padding: '3em',
                            display: 'block',
                            backgroundColor: '#f7f7f7',
                            margin: 'auto',
                            marginTop: '1em',
                            marginBottom: '1em',
                            boxShadow: '0 0.3em 0.3em #ccc'
                        }}
                >
    				<fieldset>
                        <legend>Registration Form</legend>
                        <div className="form-group">
                            <div className="container-fluid">
                                <input className="form-control disabled" disabled={true} type="email" value={this.state.googleProfile.email}
                                    style={{padding: '1.5em 0.5em'}}
                                />
                            </div>
                        </div>
                        <div className="form-group">
                            <div className="container-fluid">
                                <input className="form-control" type="text" placeholder="Username" value={this.state.username}
                                       onChange={ this.onChange.bind(this, 'username')} style={{padding: '1.5em 0.5em'}} />
                            </div>
                        </div>
                        <div className="form-group">
                            <div className="container-fluid">
                                <input className="col-xs-10 form-control" type="password" placeholder="Password" value={this.state.password}
                                       onChange={this.onChange.bind(this, 'password')} style={{padding: '1.5em 0.5em'}} />
                            </div>
                        </div>
                        <div className="form-group">
                            <div className="container-fluid">
                                <input className="col-xs-10 form-control" type="password" placeholder="Retype Password"
                                       onChange={this.verifyPassword.bind(this)} style={{padding: '1.5em 0.5em'}} />
                            </div>
                        </div>
                        <button className="btn btn-primary" type="submit" style={{padding: '0.5em', width: '100%'}}>Register</button>
                    </fieldset>
    			</form>

                <button className="btn btn-default" onClick={(e) => (e.preventDefault(), window.dispatchEvent(new Event('togglePage')))}>Login</button>
            </div>
		)
	}
}
