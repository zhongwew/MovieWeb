import React, { Component } from 'react'
import {
  withRouter,
  Link
} from 'react-router-dom';

// Import Ant Design components
import Button from 'antd/lib/button';
import message from 'antd/lib/message';
import Rate from 'antd/lib/rate';

// Import custom scss styles
import './MovieIntro.scss';

// Import API
import API from '@/api/index';

class MovieIntro extends Component {

  constructor(props) {
    super(props)

    // Get poster
    this.imgId = this.props.match.params.imgId;
    this.poster = require(`../../assets/img/movies/${this.imgId}.webp`);

    this.state = {
      movie: {
        rating: 0,
        stars: [],
        genres: [],
      },
      isAdd: false
    }
  }

  componentDidMount() {
    // Require image
    let movieId = this.props.match.params.id;

    window.$axios(API.movie.GET_MOVIE({
      id: movieId
    }))
      .then((response) => {
        let data = response.data;

        if (data.type === 'success') {
          this.setState({
            movie: data.message.movie
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

  addItemToCart = () => {
    if (!this.props.isLogin) {
      message.warning(`You have to login first`);
      return ;
    }

    window.$axios(API.cart.UPDATE_ITEMS({
      items: [...this.props.items, this.state.movie]
    }))
      .then((response) => {
        let data = response.data;

        if (data.type === 'success') {
          message.success('Shopping cart updated!');
          this.setState({
            isAdd: true
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
    const { movie, isAdd } = this.state;

    return (
      <div className="movie-intro-container">
        {/* Poster */}
        <div className="poster">
          <img src={this.poster} alt="" />
        </div>

        {/* Movie info */}
        <div className="movie-info">
          <p className="title">{movie.title}({movie.year})</p>

          <div className="content">
            <p>Director: {movie.director}</p>
            <p className="stars">
              <span>Stars: </span>
              {
                movie.stars.map((star) => (
                  <span key={star.id}>
                    <Link className="star-name" to={`/star/${star.id}`}> # {star.name}</Link>,
                  </span>
                ))
              }
            </p>
            <p>Genres: {movie.genres.join(', ')}</p>
          </div>

          {/* rating of the movie */}
          <Rate allowHalf={true} disabled value={movie.rating / 2}></Rate>

          <div className="buy">
            <Button disabled={isAdd} onClick={this.addItemToCart} type="primary">Add to Cart</Button>
          </div>
        </div>
      </div>
    )
  }
}

export default withRouter(MovieIntro);