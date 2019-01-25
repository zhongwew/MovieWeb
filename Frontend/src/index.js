import React from 'react';
import ReactDOM from 'react-dom';

// Import plugins
import './plugins';
import store from './store/index';
import { Provider } from 'react-redux';

// Import main css file
import './index.css';

// Import App Component
import App from './App';

// Render the whole app
ReactDOM.render(
  <Provider store={store}>
    <App />
  </Provider>, 
  document.getElementById('root')
);