import { createStore } from 'redux';

// Import combined reducers
import reducers from './modules/index';

// Export singleton store
export default createStore(reducers);