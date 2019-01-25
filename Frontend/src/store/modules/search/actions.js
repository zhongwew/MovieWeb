// Import user action types
import { SEARCH_START_SEARCH, SEARCH_SET_RESULTS, SEARCH_RESET } from './actionTypes';

function startSearch(payload) {
  return {
    type: SEARCH_START_SEARCH,
    payload: payload
  }
}

function setResults(payload) {
  return {
    type: SEARCH_SET_RESULTS,
    payload: payload
  }
}

function reset(payload) {
  return {
    type: SEARCH_RESET,
    payload: payload
  }
}

export default {
  startSearch,
  setResults,
  reset
}