import React, { Component } from 'react'
import {
  withRouter,
} from 'react-router-dom';

// Import Ant Design components
import message from 'antd/lib/message';

// Import custom components
import Movie from '@/components/Movie/Movie';

// Import custom scss styles
import './StarIntro.scss';

// Import API
import API from '@/api/index';

class StarIntro extends Component {

  constructor(props) {
    super(props)

    // Generate avatar
    const Mock = require('mockjs');
    this.avatar = require(`@/assets/avatars/${Mock.Random.natural(1, 5)}.jpg`);

    this.state = {
      star: {},
      movies: []
    }
  }

  // Listen to the changes of this route
  componentDidUpdate(prevProps) {
    if (this.props.location !== prevProps.location) {
      this.getStarInfo();
    }
  }

  componentDidMount() {
    this.getStarInfo();
  }

  getStarInfo = () => {
    let id = this.props.match.params.id;

    window.$axios(API.star.GET_STAR({
      id: id
    }))
      .then((response) => {
        let data = response.data;

        if (data.type === 'success') {
          this.setState({
            star: data.message.star,
            movies: data.message.movies
          });
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
    const { star, movies } = this.state;

    return (
      <div className="star-intro-container">
        {/* Avatar of this star */}
        <div className="avatar">
          <img src={this.avatar} alt="" />
        </div>

        {/* Details */}
        <div className="star-info">
          <p className="name">{star.name}</p>
          <p className="birthYear">Birth: {star.birthYear}</p>
        </div>

        {/* Some movies of this star */}
        <div className="work">
          <div className="delimiter">
            <div></div>
            <p>Current Work</p>
            <div></div>
          </div>

          {/* Some works of the star */}
          <div className="movies-container">
            {
              ([...movies]).map((movie) => (
                <Movie key={movie.id} {...movie}></Movie>
              ))
            }
          </div>
        </div>

      </div>
    )
  }
}

export default withRouter(StarIntro);