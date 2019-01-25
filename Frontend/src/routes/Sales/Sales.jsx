import React, { Component } from 'react';

// Import Ant Design components
import message from 'antd/lib/message';
import Table from 'antd/lib/table';

import API from '@/api/index';

export default class Sales extends Component {

  state = {
    sales: []
  }

  componentDidMount() {
    window.$axios(API.sales.GET())
      .then((response) => {
        let data = response.data;

        if (data.type === 'success') {
          this.setState({
            sales: data.message.movies
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
  
  render() {
    const columns = [{
      title: 'Sale Date',
      dataIndex: 'saleDate',
      key: 'saleDate',
    }, {
      title: 'Title',
      dataIndex: 'title',
      key: 'title',
    }];

    return (
      <div>
        <h1>Sales history</h1>

        <Table columns={columns} dataSource={this.state.sales} pagination={false} />
      </div>
    )
  }
}
