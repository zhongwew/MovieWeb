// Import redux plugins
import { connect } from 'react-redux';
// Import actions
import scrollActions from '@/store/modules/scroll/actions';
import searchActions from '@/store/modules/search/actions';

// Import page components
import MovieList from '@/routes/MovieList/MovieList';

// Props mapping
const mapStateToProps = (state, ownProps) => {
  return {
    // Scroll props
    isScrollLoading: state.scroll.isloading,
    appendData: state.scroll.appendData,
    page: state.scroll.page,
    count: state.scroll.count,
    
    // Search props
    results: state.search.results,
    isResultsEmpty: state.search.results === 0,
    isSearching: state.search.isSearching,
    searchGenre: state.search.genre,
    searchTitle: state.search.title,
    searchSortType: state.search.sortType,
  }
}

// Dispatch mapping
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    // Scroll dispatcher
    startScrollLoad: (payload) => {
      dispatch(scrollActions.startLoad(payload));
    },
    setScrollLoadResults: (payload) => {
      dispatch(scrollActions.setResults(payload));
    },
    updatePage: (payload) => {
      dispatch(scrollActions.updatePage(payload));
    },
    updateCount: (payload) => {
      dispatch(scrollActions.updateCount(payload));
    },
    resetScrollState: (payload) => {
      dispatch(scrollActions.reset(payload));
    },

    // Search dispatcher
    startSearch: (payload) => {
      dispatch(searchActions.startSearch(payload));
    },
    setSearchResults: (payload) => {
      dispatch(searchActions.setResults(payload));
    },
    resetSearchState: (payload) => {
      dispatch(searchActions.reset(payload));
    },
  }
}

export const MoviePageContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(MovieList);