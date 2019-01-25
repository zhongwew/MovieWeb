import React, { Component } from 'react'

// Import custom css styles
import './Panel.css';

// Import custom comonents
import Tag from '@/components/Tag/Tag';

export default class componentName extends Component {
  constructor(props) {
    super(props);

    this.state = {
      showStyle: {
        maxHeight: 200,
        borderBottom: '1px solid rgba(30,35,42,.06)'
      },
      hiddenStyle: {
        maxHeight: 0,
        borderBottom: 'none'
      }
    }
  }

  render() {
    const { title, list, isShowPanel, genre } = this.props;
    const { showStyle, hiddenStyle } = this.state;

    return (
      <div className="nav-panel" style={isShowPanel ? showStyle : hiddenStyle}>
        <div className="wrapper flex-container">
          <div className="panel-item">{title}: </div> 
          {/* list of tags */}
          <div className="panel-lists">
            {
              list.map((item) => (
                <span key={item.id} className="panel-item">
                  <Tag genre={genre} title={item.genre}></Tag>
                </span>
              ))
            }
          </div>

        </div>
      </div>
    )
  }
}