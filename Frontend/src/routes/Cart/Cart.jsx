import React, { Component } from 'react';
import {
  withRouter
} from 'react-router-dom';

// Import Ant Design components
import message from 'antd/lib/message';
import Button from 'antd/lib/button';

// Import custom components
import Item from './Item/Item';
import TransactionForm from '@/components/TransactionForm/TransactionForm';
import Empty from '@/components/Empty/Empty';
import Loading from '@/components/Loading/Loading';

// Import custom scss styles
import './Cart.scss';

// Import API
import API from '@/api/index';

class Cart extends Component {

  constructor(props) {
    super(props);
    
    this.state = {
      isLoading: false,
    }
  }
  
  componentDidMount() {
    this.setState({
      isLoading: true
    })

    // Get user's cart items
    window.$axios(API.cart.GET_ITEMS())
      .then((response) => {
        let data = response.data;

        if (data.type === 'success') {
          // Update cart items from redux
          this.props.updateCart({
            items: data.message.items
          });

          this.setState({
            isLoading: false,
          })
        }
        else {
          message.error(data.message.errorMessage);
        }

      })
      .catch((error) => {
        message.error(`${error.request.status}: ${error.request.statusText}`);
      })
  }

  updateCart = () => {
    window.$axios(API.cart.UPDATE_ITEMS({
      items: this.props.items
    }))
      .then((response) => {
        let data = response.data;

        if (data.type === 'success') {
          message.success('Shopping cart updated!');
        }
        else {
          message.error(data.message.errorMessage);
        }
      })
      .catch((error) => {
        message.error(`${error.request.status}: ${error.request.statusText}`);
      })
  }

  placeOrder = () => {
    const { items, clearCart } = this.props;

    // Extract data from items
    let tempArr = [];
    for (let i = 0 ; i < items.length ; i++) {
      for (let j = 0 ; j < items[i].quantity ; j++) {
        tempArr = [...tempArr, items[i].id]
      }
    }

    // Empty cart
    window.$axios(API.cart.UPDATE_ITEMS({
      items: []
    }))
      .then((response) => {
      })
      .catch((error) => {
        message.error(`${error.request.status}: ${error.request.statusText}`);
      })

    // Send request to place an order
    window.$axios(API.sales.PLACE_ORDER({
      movies: tempArr,
      saleDate: new Date().getTime()
    }))
      .then((response) => {
        let data = response.data;

        if (data.type === 'success') {
          // Clear shopping cart data from redux
          clearCart();
          // Redirect to order page
          this.props.history.push('/order');
        }
        else {
          message.error(data.message.errorMessage);
        }
      })
      .catch((error) => {
        message.error(`${error.request.status}: ${error.request.statusText}`);
      })
  }

  render() {
    const { items, total, updateItemFromCart, removeItemFromCart, confirmCC, isConfirm, isEmpty } = this.props;
    const { isLoading } = this.state;

    return (
      <div className="cart-container">
        <div style={{display: items.length === 0 ? 'none': 'block'}}>
          {/* Title */}
          <h1 style={{ textAlign: 'center' }}>Shopping Cart</h1>

          {/* Table Header */}
          <div className="cart-header">
            <span className="title-item">Item</span>

            <span>Quantity</span>

            <span>Operation</span>
          </div>

          {/* Shopping car items */}
          {
            items.map((item) => (
              <Item updateCart={this.updateCart} key={item.id} item={item} updateItemFromCart={updateItemFromCart} removeItemFromCart={removeItemFromCart}></Item>
            ))
          }

          {/* Checkout */}
          <div className="checkout-container">
            <span>Total: {total()} $ </span>
            <Button onClick={this.updateCart} type="primary">Update Cart</Button>
          </div>

          {/* Transaction */}
          <div className="transaction-container">
            <h1 style={{ textAlign: 'center' }}>Transaction Information</h1>

            <TransactionForm isConfirm={isConfirm} confirmCC={confirmCC}></TransactionForm>
          </div>

          <div className="place-order-container">
            <Button disabled={!isConfirm} onClick={this.placeOrder} type="primary">Place your order</Button>
          </div>
        </div>

        <div style={{display: isLoading ? 'block': 'none'}}>
          <Loading></Loading>
        </div>

        <div style={{display: isEmpty ? 'block': 'none'}}>
          <Empty></Empty>
        </div>
      </div>
    )
  }
}

export default withRouter(Cart);