// Import scroll action types
import {
  SCROLL_START_LOAD,
  SCROLL_SET_RESULTS,
  SCROLL_RESET,
  SCROLL_UPDATE_PAGE,
  SCROLL_UPDATE_COUNT
} from './actionTypes';

// Scroll initial state
const initState = {
  isloading: false,
  page: 1,
  count: 20,
  appendData: [],
}

// Export this reducer
export default function scroll(state = initState, action) {
  switch (action.type) {
    case SCROLL_START_LOAD:
      // Increase number of page, and set lazy loading to true
      return {
        ...state,
        isloading: true,
        page: state.page + 1,
      };
    case SCROLL_SET_RESULTS:
      // After get the results, set append data and the lazy loading to false
      return {
        ...state,
        isloading: false,
        appendData: [...state.appendData, ...action.payload.appendData],
      };
    case SCROLL_UPDATE_PAGE:
      return {
        ...state,
        page: action.payload.page
      };
    case SCROLL_UPDATE_COUNT:
      return {
        ...state,
        count: action.payload.count
      };
    case SCROLL_RESET:
      return initState;
    default:
      return state;
  }
}