// Import redux plugins
import { connect } from 'react-redux';

// Import store cart actions
import cartActions from '@/store/modules/cart/actions';

// Import Cart component
import Cart from '@/routes/Cart/Cart';

// Props mapping
const mapStateToProps = (state, ownProps) => {
  return {
    items: state.cart.items,
    isConfirm: state.cart.isConfirm,
    isLogin: state.auth.isLogin,
    total: () => {
      let tempItems = state.cart.items, total = 0;

      tempItems.forEach(item => {
        total = total + item.quantity * item.price;
      });

      return total.toFixed(2);
    },
    isEmpty: state.cart.items.length === 0
  }
}

// Dispatch mapping
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    addItemToCart: (payload) => {
      dispatch(cartActions.addItem(payload));
    },

    updateItemFromCart: (payload) => {
      dispatch(cartActions.updateItem(payload));
    },

    removeItemFromCart: (payload) => {
      dispatch(cartActions.removeItem(payload));
    },

    confirmCC: (payload) => {
      dispatch(cartActions.confirmCC(payload));
    },

    clearCart: (payload) => {
      dispatch(cartActions.clearCart(payload));
    },

    updateCart: (payload) => {
      dispatch(cartActions.updateCart(payload));
    }
  }
}

export const CartContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(Cart);
