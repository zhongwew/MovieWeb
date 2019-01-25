import React from 'react';
import {
  Link
} from 'react-router-dom';

// Import custom components
import Modal from '@/components/Movie/Modal/Modal';
import Detail from './Detail/Detail';

// import css styles
import './Movie.css';

class Movie extends React.Component {

  constructor(props) {
    super(props);

    const Mock = require('mockjs');
    this.imgId = Mock.Random.natural(1, 14);

    this.state = {
      isShow: false,
      poster: require(`@/assets/img/movies/${this.imgId}.webp`),
    };

    // bind methods
    this.trigger = this.trigger.bind(this);
  }

  trigger() {
    this.setState({
      isShow: !this.state.isShow
    });
  }

  render() {
    const poster = this.state.poster;
    const isShow = this.state.isShow;

    return (
      <div className='movie-container'>
        <div className="wrapper">
          <div className="poster-container">
            <img className='poster' src={poster} alt="" />

            {/* Modal for triggering outer layer */}
            <Modal trigger={this.trigger}></Modal>
          </div>

          {/* Movie title */}
          <p className='title'>
            <Link to={`/movie/${this.props.id}/${this.imgId}`}>{this.props.title}</Link>
          </p>

          {/* The director of movie */}
          <p className='director'>{this.props.director}</p>

          {/* Outer layer for showing movie details */}
          <Detail {...this.props} isShow={isShow} trigger={this.trigger} imgId={this.imgId} poster={poster}></Detail>
        </div>
      </div>
    )
  }
}

export default Movie;