import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';

// Import container components
import NavContainer from '@/containers/Nav/Nav';

// Import page component
import { MoviePageContainer } from '@/containers/MovieList/MovieList';
import { MovieIntroContainer } from '@/containers/MovieIntro/MovieIntro';
import { StarIntroContainer } from '@/containers/StarIntro/StarIntro';
import { CartContainer } from '@/containers/Cart/Cart';
import { SearchContainer } from '@/containers/Search/Search';
import User from '@/routes/User/User';
import Order from '@/routes/Order/Order';
import PrivateRoute from '@/components/PrivateRoute/PrivateRoute';
import Login from '@/routes/Login/Login';
import Report from '@/routes/Report/Report';
import Sales from '@/routes/Sales/Sales';
import Dashboard from '@/routes/Dashboard/Dashboard';

function RoutesConfig() {
  return (
    <Router >
      <div>
        {/* Navigator */}
        <NavContainer></NavContainer>
        {/* Page routes */}
        <Switch>
          <Route path='/' exact component={MoviePageContainer} />
          <Route path='/login' component={Login}/>
          <Route path='/movie/:id/:imgId' component={MovieIntroContainer}/>
          <Route path='/star/:id' component={StarIntroContainer}/>
          <Route path='/reports/like-predicate' component={Report}/>
          <Route path='/sales' component={Sales}/>
          <Route path='/search' component={SearchContainer}/>
          <Route path='/_dashboard' component={Dashboard}/>
          <PrivateRoute path="/cart" component={CartContainer}></PrivateRoute>
          <PrivateRoute path='/order' component={Order}/>
          <PrivateRoute path='/user' component={User}/>
          <Route path='/*' component={MoviePageContainer}/>
        </Switch>
      </div>
    </Router>
  )
}

export default RoutesConfig;