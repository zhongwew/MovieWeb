// Import scroll action types
import {
  SCROLL_START_LOAD,
  SCROLL_SET_RESULTS,
  SCROLL_RESET,
  SCROLL_UPDATE_PAGE,
  SCROLL_UPDATE_COUNT
} from './actionTypes';

// action creators
function startLoad(payload) {
  return {
    type: SCROLL_START_LOAD,
    payload
  }
}

function setResults(payload) {
  return {
    type: SCROLL_SET_RESULTS,
    payload
  }
}

function updatePage(payload) {
  return {
    type: SCROLL_UPDATE_PAGE,
    payload
  }
}

function updateCount(payload) {
  return {
    type: SCROLL_UPDATE_COUNT,
    payload
  }
}

function reset(payload) {
  return {
    type: SCROLL_RESET,
    payload
  }
}

export default {
  startLoad,
  setResults,
  updatePage,
  updateCount,
  reset
}