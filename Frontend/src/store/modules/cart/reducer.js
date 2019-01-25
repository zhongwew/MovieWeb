// Import cart action types
import {
  CART_ADD_ITEM,
  CART_UPDATE_ITEM,
  CART_REMOVE_ITEM,
  CART_CLEAR_CART,
  CART_UPDATE_CART,
  CART_CONFIRM_CC
} from './actionTypes';

// Cart initial state
const initState = {
  items: [],
  isConfirm: false,
}

// Export this reducer
export default function scroll(state = initState, action) {
  let newItems = [],
      newItem = {},
      tempItems = [],
      isExit = false;

  switch (action.type) {
    case CART_ADD_ITEM:
      // Add an itme to cart
      newItem = action.payload.item;
      for (let item of state.items) {
        if (item.id === newItem.id) {
          isExit = true;
          item.quantity++;
          break;
        }
      }
      // Def new items
      newItems = isExit ? state.items : [...state.items, { ...action.payload.item, quantity: 1 }]

      return {
        ...state,
        items: newItems
      };
    case CART_UPDATE_ITEM:
      // Update an item from cart
      tempItems = state.items;
      newItem = action.payload.item;
      // To find the item
      for (let item of tempItems) {
        if (item.id === newItem.id) {
          item.quantity = newItem.quantity;
          break;
        }
      }
      return {
        ...state,
        items: tempItems
      };
    case CART_REMOVE_ITEM:
      // Remove an item from cart
      newItems = state.items.filter((item) => {
        return item.id !== action.payload.item.id;
      })
      return {
        ...state,
        items: newItems
      };
    case CART_CONFIRM_CC:
      return {
        ...state,
        isConfirm: true,
      };
    case CART_CLEAR_CART:
      return initState;
    case CART_UPDATE_CART:
      return {
        ...state,
        items: action.payload.items
      }
    default:
      return state;
  }
}