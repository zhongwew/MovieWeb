import React, { Component } from 'react';

// Import Ant Design components
import message from 'antd/lib/message';

// Import store
import store from '@/store/index';
import searchActions from '@/store/modules/search/actions';
import scrollActions from '@/store/modules/scroll/actions';

// Import custom css files
import './Tag.css';

// Import API
import API from '@/api/index';

export default class Tag extends Component {
  // Send request to get list of movies
  search = async () => {
    const { genre, title } = this.props;

    // Start searcing
    store.dispatch(searchActions.startSearch());

    await store.dispatch(scrollActions.updatePage({
      page: 1
    }));

    window.$axios(API.movies.GET_MOVIES({
      page: store.getState().scroll.page,
      title: '',
      genre: '',
      [genre.toLowerCase()]: title,
      sortType: '',
      count: store.getState().scroll.count
    }))
      .then((response) => {
        let data = response.data;
        if (data.type === 'success') {
          // Get search results, and set it to store
          store.dispatch(searchActions.setResults({
            results: data.message.movies,
            title: '',
            genre: '',
            [genre.toLowerCase()]: title,
            sortType: ''
          }))
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
    const { title } = this.props;

    return (
      <span onClick={this.search} className='tag-container'>
        {title}
      </span>
    )
  }
}
