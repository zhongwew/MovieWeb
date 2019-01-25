import React, { Component } from 'react'

// Import store
import store from '@/store/index';

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
      },
      hiddenStyle: {
        maxHeight: 0,
      }
    }
  }

  render() {
    const { title, list, isShowPanel, genre } = this.props;
    const { showStyle, hiddenStyle } = this.state;

    return (
      <div className="nav-panel" style={isShowPanel ? showStyle : hiddenStyle}>
        <div className="wrapper">
          <div className="panel-item">{title}: </div> 
          {/* list of tags */}
          <div className="panel-lists">
            {
              list.map((item) => (
                <span key={item.id} className="panel-item">
                  <Tag isSelect={store.getState().search[title.toLowerCase()] === item.genre} genre={genre} title={item.genre}></Tag>
                </span>
              ))
            }
          </div>

        </div>
      </div>
    )
  }
}