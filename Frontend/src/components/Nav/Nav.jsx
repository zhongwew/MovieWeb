import React, { Component } from 'react';
import {
  Link,
} from 'react-router-dom';

// Import Ant Design components
import Modal from 'antd/lib/modal';
import message from 'antd/lib/message';
import Dropdown from 'antd/lib/dropdown';
import Menu from 'antd/lib/menu';
import Button from 'antd/lib/button';
import Icon from 'antd/lib/icon';

// Import custom components
import LoginForm from '../LoginForm/LoginForm';

// Import custom css styles
import './Nav.css';

// Import API
import API from '@/api/index';

class Nav extends Component {
  constructor(props) {
    super(props);

    this.state = {
      logoSrc: require(`../../assets/img/utils/logo.png`),
      genres: [],
      loginForm: {
        email: '',
        password: '',
        recaptcha: ''
      },
      isShowLogin: false,
      isLoginFormValid: false,
      panelTitle: '',
      keyword: ''
    }
  }

  triggerLogin = () => {
    this.setState({
      isShowLogin: !this.state.isShowLogin
    })
  }

  // Send http request to login
  login = () => {
    // Check the login form validation
    if (this.state.isLoginFormValid) {
      window.$axios(API.auth.LOGIN({
        ...this.state.loginForm
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

  // Logout
  logout = (event) => {
    event.preventDefault();

    // Send http request to logout
    window.$axios(API.auth.LOGOUT({
      email: this.state.loginForm.email,
      password: this.state.loginForm.password
    }))
      .then((response) => {
        let data = response.data

        if (data.type === 'success') {
          message.success('You have logout!');
          // Set store state
          this.props.logout();
        }
        else {
          message.error(`${data.message.errorMessage}`);
        }
      })
      .catch((error) => {
        message.error(`${error.request.status}: ${error.request.statusText}`);
      })
  }

  // Listen to login form values
  onLoginValuesChange = (event, isError, values, recaptcha) => {
    // Combine login form calues with recaptchas
    values = {
      ...values,
      recaptcha
    };

    this.setState({
      isLoginFormValid: !isError,
      loginForm: values
    })
  }

  render() {
    const { logoSrc, isShowLogin } = this.state;
    const { isLogin } = this.props;

    // avatar path
    const avatarSrc = require('../../assets/img/user/avatar.jpg');

    // menu
    const loginMenu = (
      <Menu>
        <Menu.Item key="0">
          <Link style={{ padding: '10px 15px' }} to="/user">
            <Icon style={{ marginRight: 5 }} type="user" />
            <span>Profile</span>
          </Link>
        </Menu.Item>
        <Menu.Item key="1">
          <Link to="/sales" href="" style={{ padding: '10px 15px' }}>
            <Icon style={{ marginRight: 5 }} type="area-chart" />
            <span>Sales</span>
          </Link>
        </Menu.Item>
        <Menu.Item key="2">
          <a href="" style={{ padding: '10px 15px' }} onClick={this.logout}>
            <Icon style={{ marginRight: 5 }} type="logout" />
            <span>Logout</span>
          </a>
        </Menu.Item>
      </Menu>
    )

    return (
      <div onMouseLeave={this.hidePanel} className="nav-container">
        <div className="nav-bar flex-container">
          <div>
            {/* logo img */}
            <Link to="/"><img className="logo-img" src={logoSrc} alt="logo" /></Link>

            {/* list of nav items */}
            <nav>
              <Link className="nav-item" to="/">Home</Link>

              <Link className="nav-item" to="/cart">Cart</Link>

              <Link className="nav-item" to="/search">Search</Link>

              <Link className="nav-item" to="/reports/like-predicate">Report</Link>

              <Link className="nav-item" to="/_dashboard">Employee</Link>
            </nav>
          </div>

          <div className="login">
            {/* login button */}
            <Button style={{ display: !isLogin ? 'block' : 'none' }} onClick={this.triggerLogin} icon="key">
              Login
            </Button>

            {/* Dropdown with avatar */}
            <Dropdown overlay={loginMenu}>
              <img style={{ display: isLogin ? 'block' : 'none' }} className="avatar" src={avatarSrc} alt="Avatar" />
            </Dropdown>
          </div>

          {/* login modal */}
          <Modal title="Basic Modal" visible={isShowLogin} onOk={this.login} onCancel={this.triggerLogin}>
            {/* login form */}
            <LoginForm onLoginValuesChange={this.onLoginValuesChange}></LoginForm>
          </Modal>
        </div>
      </div>
    )
  }
}

export default Nav;
