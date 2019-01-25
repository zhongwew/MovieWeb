// Import redux plugins
import { connect } from 'react-redux';
// Import actions
import searchActions from '@/store/modules/search/actions';

// Import Search component
import Search from '@/routes/Search/Search';

// Props mapping
const mapStateToProps = (state, ownProps) => {
  return {
    // Search props
    results: state.search.results,
    isResultsEmpty: state.search.results === 0,
    isSearching: state.search.isSearching,
    searchGenre: state.search.genre,
    searchTitle: state.search.title,
  }
}

// Dispatch mapping
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
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

export const SearchContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(Search);