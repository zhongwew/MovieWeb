import React, { Component } from 'react';
import {
  withRouter,
} from 'react-router-dom';

// Import custom components
import SearchForm from '@/components/SearchForm/SearchForm';
import Loading from '@/components/Loading/Loading';
import Movie from '@/components/Movie/Movie';
import Empty from '@/components/Empty/Empty';
import message from 'antd/lib/message';

// Import custom styles
import './Search.scss';

// Import API
import API from '@/api/index';

class Search extends Component {

  state = {
    startSearch: false,
    movies: []
  }

  constructor(props) {
    super(props)

    let keyword = decodeURI(this.props.location.search.split('=')[1]);
    if (keyword) {
      this.autoSearch(keyword);
    }
  }
  
  autoSearch(keyword) {
    window.$axios(API.autoComplete.GET_ITEMS({
      keyword
    }))
      .then((response) => {
        let data = response.data;
        if (data.type === 'success') {
          this.setState({
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
    const { startSearch, movies } = this.state;
    const { isSearching, results } = this.props;

    return (
      <div className="search-container">
        <div>
          <SearchForm start={() => this.setState({startSearch: true})} {...this.props}></SearchForm>
        </div>

        <div className="search-results-container" >
          <div style={{display: results.length === 0 && startSearch && !isSearching ? 'block': 'none'}}>
            <Empty></Empty>
          </div>

          <div className="search-movie-results">
            <h1>Movies</h1>
            <div>
              {
                (results.length !== 0? results : movies).map((movie) => (
                  <Movie key={movie.id} {...movie}></Movie>
                ))
              }
            </div>
          </div>
        </div>

        <div style={{ display: isSearching ? "block" : "none" }}>
          <Loading></Loading>
        </div>
      </div>
    )
  }
}

export default withRouter(Search);