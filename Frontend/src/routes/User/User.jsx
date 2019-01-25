import React, { Component } from 'react'
import {
  withRouter,
} from 'react-router-dom';

// Import Ant Design components
import message from 'antd/lib/message';

// Import custom scss styles
import './User.scss';

// Import API
import API from '@/api/index';

class userIntro extends Component {

  constructor(props) {
    super(props)

    // Generate avatar
    const Mock = require('mockjs');
    this.avatar = require(`@/assets/avatars/${Mock.Random.natural(1, 5)}.jpg`);

    this.state = {
      user: {},
    }
  }

  // Listen to the changes of this route
  componentDidUpdate(prevProps) {
    if (this.props.location !== prevProps.location) {
      this.getuserInfo();
    }
  }

  componentDidMount() {
    window.$axios(API.user.GET())
      .then((response) => {
        let data = response.data;

        if (data.type === 'success') {
          this.setState({
            user: data.message
          })
        }
        else {
          message.error(data.message.errorMessage);
        }
      })
      .catch((error) => {
        message.error(`${error.request.status}: ${error.request.statusText}`);
      })
  }

  render() {
    const { user } = this.state;

    return (
      <div className="user-container">
        {/* Avatar of this user */}
        <div className="avatar">
          <img src={this.avatar} alt="" />
        </div>

        {/* Details */}
        <div className="user-info">
          <p className="name">{user.firstName} {user.lastName}</p>
          <p className="birthYear">{user.address}</p>
        </div>
      </div>
    )
  }
}

export default withRouter(userIntro);