import React, { Component } from 'react';

// Import Ant Design components
import Button from 'antd/lib/button';

// Import custom styles
import './Modal.css';

export default class Modal extends Component {
  render() {
    return (
      <div className="modal-container">
        <span className="delimiter left"></span>
        <span className="toggle-btn">
          <Button onClick={this.props.trigger} shape="circle" icon="search" ghost />
        </span>
        <span className="delimiter right"></span>
      </div>
    )
  }
}
