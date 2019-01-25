import React, { Component } from 'react';
// Import redux plugins
import { connect } from 'react-redux';

// Import actions
import { login, logout } from '@/store/modules/auth/actions';

// Import ant design components
import Button from 'antd/lib/button';
import message from 'antd/lib/message';
// Import custom components
import LoginForm from '@/components/LoginForm/LoginForm';

// Import styles
import './Login.scss';

// Import API
import API from '@/api/index';

// Props mapping
const mapStateToProps = (state, ownProps) => {
  return {
    isLogin: state.auth.isLogin
  }
}

// Dispatch mapping
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    login: () => {
      dispatch(login());
    },
    logout: () => {
      dispatch(logout());
    }
  }
}

class Login extends Component {
  state = {
    loginForm: {
      email: '',
      password: '',
    },
  }

  // Listen to login form values
  onLoginValuesChange = (event, isError, values) => {
    this.setState({
      isLoginFormValid: !isError,
      loginForm: values
    })
  }

  // Send http request to login
  login = () => {
    // Check the login form validation
    if (this.state.isLoginFormValid) {
      window.$axios(API.auth.LOGIN({
        email: this.state.loginForm.email,
        password: this.state.loginForm.password
      }))
        .then((response) => {
          let data = response.data

          if (data.type === 'success') {
            message.success('Welcome back');
            // Set local state
            this.setState({
              isShowLogin: false
            });
            // Set store state
            this.props.login();
            // Go to index
            this.props.history.push('/');
          }
          else {
            message.error(`${data.message.errorMessage}`);
          }
        })
        .catch((error) => {
          message.error(`${error.request.status}: ${error.request.statusText}`);
        })
    }
    else {
      message.error('Please check your input!');
    }
  }

  render() {
    return (
      <div className="login-form-wrapper">
        <LoginForm onLoginValuesChange={this.onLoginValuesChange}></LoginForm>
        <Button type="primary" style={{width: '100%'}} icon="key" onClick={this.login}>Login</Button>
      </div>
    )
  }
}

const LoginContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(Login);

export default LoginContainer;