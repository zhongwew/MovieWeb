// Import redux plugins
import { connect } from 'react-redux';
// Import actions
import cartActions from '@/store/modules/cart/actions';

// Import page components
import MovieIntro from '@/routes/MovieIntro/MovieIntro';

// Props mapping
const mapStateToProps = (state, ownProps) => {
  return {
    // Cart props
    items: state.cart.items,
    isLogin: state.auth.isLogin
  }
}

// Dispatch mapping
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    addItemToCart: (payload) => {
      dispatch(cartActions.addItem(payload));
    },
  }
}

export const MovieIntroContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(MovieIntro);