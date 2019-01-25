import React from 'react';

import {
  withRouter
} from 'react-router-dom';

// Import Ant Design components
import message from 'antd/lib/message';

// Import custom components
import Movie from '@/components/Movie/Movie';
import Loading from '@/components/Loading/Loading';
import Empty from '@/components/Empty/Empty';
import Tab from '@/components/Tab/Tab';

// Import css styles
import './MovieList.css';

// Import movies API
import API from '@/api/index';

class MovieList extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      pageNum: 0,
      count: 20
    }
  }

  getMovieList = (page, pageSize) => {
    // Set state of starting searching
    this.props.startSearch();

    // Update current page
    this.props.updatePage({
      page: page
    });
    
    window.$axios(API.movies.GET_MOVIES({
      page: page,
      count: this.props.count,
      genre: this.props.searchGenre,
      title: this.props.searchTitle,
      sortType: this.props.searchSortType
    }))
      .then((response) => {
        let data = response.data;

        if (data.type === 'success') {
          // Get search results, and set it to store
          this.props.setSearchResults({
            results: data.message.movies,
            genre: this.props.searchGenre,
            title: this.props.searchTitle,
            sortType: this.props.searchSortType
          });
          this.setState({
            pageNum: data.message.length / this.state.count + 1,
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

  componentDidMount() {
    const { page } = this.props;

    this.getMovieList(page);
  }

  componentWillUnmount() {
    this.props.resetSearchState();
    this.props.resetScrollState();
  }

  render() {
    const { pageNum } = this.state;
    const { appendData, isScrollLoading, results, isSearching, isResultsEmpty, page, updateCount } = this.props;

    return (
      <div className='movies-list-page'>
        <div>
          <Tab updateCount={updateCount} getMovieList={this.getMovieList} page={page} pageNum={pageNum}></Tab>
        </div>

        <div className="movies-container" style={{ display: isSearching ? 'none' : 'flex' }}>
          {
            ([...results, ...appendData]).map((movie) => (
              <Movie key={movie.id} {...movie}></Movie>
            ))
          }
        </div>

        <div style={{ display: isSearching || isScrollLoading ? 'block' : 'none' }}>
          <Loading></Loading>
        </div>

        <div style={{ display: !isResultsEmpty ? 'none' : 'block' }}>
          <Empty></Empty>
        </div>
      </div>
    );
  }
}

export default withRouter(MovieList);
