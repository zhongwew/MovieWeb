import React, { Component } from 'react';
// Import react router config
import RouteConfig from './routes/RoutesConfig.jsx';

// Import ant design components
import message from 'antd/lib/message';

// Import redux plugins
import { connect } from 'react-redux';
// Import actions
import { login, logout } from '@/store/modules/auth/actions';

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

class App extends Component {
  componentDidMount() {
    const { isLogin, login } = this.props;

    if (!isLogin) {
      window.$axios(API.user.GET())
        .then((response) => {
          let data = response.data

          if (data.type === 'success') {
            message.success('Welcome back');
            // Set store state
            login();
          }
          else {
            message.warning(`You haven't sign in`);
          }
        })
        .catch((error) => {
          message.error(`${error.request.status}: ${error.request.statusText}`);
        })
    }
  }

  render() {
    return (
      <div>
        <RouteConfig></RouteConfig>
      </div>
    );
  }
}

const AppContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(App);

export default AppContainer;
