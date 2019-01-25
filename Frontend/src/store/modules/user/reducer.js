// Import user action types
import { LOGIN, LOGOUT } from './actionTypes';

// User initial state
const initState = {
  isLogin: false
}

// Export this reducer
export default function user(state = initState, action) {
  switch (action.type) {
    case LOGIN:
      return {
        ...state,
        isLogin: true
      };
    case LOGOUT:
      return {
        ...state,
        isLogin: false       
      };
    default:
      return state;
  }
}