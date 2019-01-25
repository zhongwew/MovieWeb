import React, { Component } from 'react'

// Import custom css styles
import './Loading.css';

export default class Loading extends Component {
  render() {
    return (
      <div className="loading-container">
        <div className="dot"></div>
        <div className="dot"></div>
        <div className="dot"></div>
        <div className="dot"></div>
        <div className="dot"></div>
      </div>
    )
  }
}
