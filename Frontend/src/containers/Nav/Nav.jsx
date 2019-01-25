// Import redux plugins
import { connect } from 'react-redux';
// Import actions
import { login, logout } from '@/store/modules/auth/actions';

// Import Nav component
import Nav from '@/components/Nav/Nav';

// Props mapping
const mapStateToProps = (state, ownProps) => {
  return {
    isLogin: state.auth.isLogin
  }
}

// Dispatch mapping
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    login: () => {
      dispatch(login());
    },
    logout: () => {
      dispatch(logout());
    }
  }
}

const NavContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(Nav);

export default NavContainer;