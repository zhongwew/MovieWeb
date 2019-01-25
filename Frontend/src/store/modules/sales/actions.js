// Import cart action types
import {
  SALES_CREATE_SALE,
} from './actionTypes';

// action creators
function createSale(payload) {
  return {
    type: SALES_CREATE_SALE,
    payload: payload
  }
}

export default {
  createSale,
}