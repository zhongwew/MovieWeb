// Import cart action types
import {
  CART_ADD_ITEM,
  CART_UPDATE_ITEM,
  CART_REMOVE_ITEM,
  CART_CLEAR_CART,
  CART_UPDATE_CART,
  CART_CONFIRM_CC
} from './actionTypes';

// action creators
function addItem(payload) {
  return {
    type: CART_ADD_ITEM,
    payload: payload
  }
}

function updateItem(payload) {
  return {
    type: CART_UPDATE_ITEM,
    payload: payload
  }
}

function removeItem(payload) {
  return {
    type: CART_REMOVE_ITEM,
    payload: payload
  }
}

function clearCart(payload) {
  return {
    type: CART_CLEAR_CART,
    payload: payload
  }
}

function updateCart(payload) {
  return {
    type: CART_UPDATE_CART,
    payload: payload
  }
}

function confirmCC(payload) {
  return {
    type: CART_CONFIRM_CC,
    payload: payload
  }
}

export default {
  addItem,
  updateItem,
  removeItem,
  clearCart,
  updateCart,
  confirmCC,
}