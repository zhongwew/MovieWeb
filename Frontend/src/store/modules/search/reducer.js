// Import search action types
import { SEARCH_START_SEARCH, SEARCH_SET_RESULTS, SEARCH_RESET } from './actionTypes';

// Search initial state
const initState = {
  genre: '',
  title: '',
  sortType: '',
  isSearching: false,
  results: []
}

// Export this reducer
export default function search(state = initState, action) {
  switch (action.type) {
    case SEARCH_START_SEARCH:
      return {
        ...state,
        isSearching: true,
      }
    case SEARCH_SET_RESULTS:
      return {
        ...state,
        isSearching: false,
        results: action.payload.results,
        genre: action.payload.genre,
        title: action.payload.title,
        sortType: action.payload.sortType,
      };
    case SEARCH_RESET:
      return initState;
    default:
      return state;
  }
}