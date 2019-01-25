// Import user action types
import { LOGIN, LOGOUT } from './actionTypes';

// action creators
export function login(payload) {
  return {
    type: LOGIN,
    payload: payload
  }
}

export function logout(payload) {
  return {
    type: LOGOUT,
    payload: payload
  }
}