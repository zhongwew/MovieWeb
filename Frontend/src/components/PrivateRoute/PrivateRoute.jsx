// Import redux plugins
import { Route, Redirect } from 'react-router-dom';
import React from 'react';

import store from '@/store/index';

const PrivateRoute = ({ component: Component, ...rest }) => {
  return (
    <Route {...rest} render={props => {
      return (
        store.getState().auth.isLogin ? (
          <Component {...props}/>
        ) : (
          <Redirect to={{
            pathname: '/login',
            state: { from: props.location }
          }}/>
        )
      )
    }}/>
)}

export default PrivateRoute;