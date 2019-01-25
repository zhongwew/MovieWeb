// Import redux plugins
import { connect } from 'react-redux';

// Import StarIntro component
import StarIntro from '@/routes/StarIntro/StarIntro';

// Props mapping
const mapStateToProps = (state, ownProps) => {
  return {
    // isLogin: state.auth.isLogin
  }
}

// Dispatch mapping
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    // login: () => {
    //   dispatch(login());
    // },
    // logout: () => {
    //   dispatch(logout());
    // }
  }
}

export const StarIntroContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(StarIntro);
