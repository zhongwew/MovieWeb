import React, { Component } from 'react'

// Import custom styles
import './Order.scss';

export default class Order extends Component {
  constructor(props) {
    super(props);
    
    this.smileFace = require('@/assets/icons/success.png');
  }
  
  render() {
    return (
      <div className="order-result-container">
        <p>Your order is generated!</p>

        <img src={this.smileFace} alt=""/>
      </div>
    )
  }
}
