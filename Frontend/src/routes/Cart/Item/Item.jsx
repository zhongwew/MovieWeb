import React, { Component } from 'react';
import {
  Link
} from 'react-router-dom';

// Import Ant Design components
import Rate from 'antd/lib/rate';
import Button from 'antd/lib/button';
import InputNumber from 'antd/lib/input-number';

// Import store
import store from '@/store/index';

// Import custom styles
import './Item.scss';

export default class Item extends Component {
  constructor(props) {
    super(props);

    const Mock = require('mockjs');
    this.imgId = Mock.Random.natural(1, 14);
    this.poster = require(`@/assets/img/movies/${this.imgId}.webp`);
  }

  updateItem = async (newValue) => {
    const { updateItemFromCart, updateCart } = this.props;
    
    await updateItemFromCart({
      item: {
        ...this.props.item,
        quantity: newValue
      }
    })

    // Update cart
    updateCart(store.getState().cart.items);
  }

  remove = async () => {
    const { removeItemFromCart, updateCart } = this.props;
    
    await removeItemFromCart({
      item: this.props.item
    });

    // Update cart
    updateCart(store.getState().cart.items);
  }

  render() {
    const { item } = this.props;

    return (
      <div className="cart-item-container">
        <div className="movie-info">
          {/* Poster */}
          <div className="poster">
            <Link to={`/movie/${item.id}/${this.imgId}`}>
              <img src={this.poster} alt="Poster" />
            </Link>
          </div>

          {/* Basic info */}
          <div className="movie-detail">
            <p className="title">
              <Link to={`/movie/${item.id}/${this.imgId}`}>{item.title}</Link>
            </p>

            <p>{item.price.toFixed(2)} $</p>

            <Rate allowHalf={true} disabled value={item.rating / 2}></Rate>
          </div>
        </div>

        <div className="quantity">
          <InputNumber min={1} max={10} defaultValue={item.quantity} onChange={this.updateItem}></InputNumber>
        </div>

        <div onClick={this.remove} className="operation">
          <Button type="danger">Remove</Button>
        </div>
      </div>
    )
  }
}