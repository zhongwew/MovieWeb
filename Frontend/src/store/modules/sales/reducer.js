// Import sales action types
import {
  SALES_CREATE_SALE,
} from './actionTypes';

// Sales initial state
const initState = {

}

// Export this reducer
export default function scroll(state = initState, action) {
  switch (action.type) {
    case SALES_CREATE_SALE:
      return {
        ...state,
      };
    default:
      return state;
  }
}