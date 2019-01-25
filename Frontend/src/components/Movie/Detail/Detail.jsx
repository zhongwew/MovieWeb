import React from 'react';
import {
  Link
} from 'react-router-dom';

// Import Ant Design components
import Button from 'antd/lib/button';
import Rate from 'antd/lib/rate';

// Import custom comonents
import Tag from '@/components/Tag/Tag';

// Import css styles
import './Detail.css';

class Detail extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      // Styles for transition
      showStyle: {
        opacity: 1,
        transform: 'scale(1, 1)',
        transition: '.35s all',
        zIndex: 3
      },
      hiddenStyle: {
        opactiy: 0,
        transform: 'scale(0, 0)',
        transition: '.35s all',
        zIndex: -1
      }
    }
  }

  render() {
    const { showStyle, hiddenStyle } = this.state;

    return (
      <div onMouseLeave={this.props.trigger} className="detail-container" style={this.props.isShow ? showStyle : hiddenStyle}>
        <div className="poster-bg" style={{backgroundImage: `url(${this.props.poster})`}}>
        </div>

        <div className="info-container">
          {/* title of the movie */}
          <p className="title">
            <Link to={`/movie/${this.props.id}/${this.props.imgId}`}>{this.props.title} ({this.props.year})</Link>
          </p>

          {/* genres of the movie */}
          <div className="genres">
            {
              this.props.genres.map((genre) => (
                <Tag key={genre} genre="Genre" title={genre}></Tag>
              ))
            }
          </div>

          {/* directory of the movie */}
          <p className="director">Director: {this.props.director}</p>

          {/* stars of the movie */}
          <p className="stars">
            <span>Stars: </span>
            {
              this.props.stars.map((star) => (
                <span key={star.id}>
                  <Link className="star-name" to={`/star/${star.id}`}> # {star.name}</Link>, 
                </span>
              ))
            }
          </p>

          {/* rating of the movie */}
          <Rate allowHalf={true} disabled value={this.props.rating/2}></Rate>
        </div>

        <div className="footer">
          <Button className="close-btn" shape="circle" icon="close" onClick={this.props.trigger}></Button>
        </div>
      </div>
    )
  }
}

export default Detail;