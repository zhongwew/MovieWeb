import {
  combineReducers
} from 'redux';

// Import all reducers
import auth from './auth/reducer';
import scroll from './scroll/reducer';
import search from './search/reducer';
import cart from './cart/reducer';
import sales from './sales/reducer';

// Combine all reducers
export default combineReducers({
  auth,
  scroll,
  search,
  cart,
  sales
})