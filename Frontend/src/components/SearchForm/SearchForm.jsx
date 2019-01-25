import React from 'react';

// Import Ant Design components
import message from 'antd/lib/message';
import Form from 'antd/lib/form';
import Input from 'antd/lib/input';
import Button from 'antd/lib/button';
import DatePicker from 'antd/lib/date-picker';

// Import API
import API from '@/api/index';

const FormItem = Form.Item;
const RangePicker = DatePicker.RangePicker;

class SearchForm extends React.Component {
  constructor() {
    super();
    this.state = {
      genres: [],
      yearRange: [0, 10000]
    };
  }

  componentDidMount() {
    window.$axios(API.genres.GET_GENRES())
      .then((response) => {
        let data = response.data;

        if (data.type === 'success') {
          this.setState({
            genres: data.message.genres
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

  // Method to get the year range
  onChange = (date, dateString) => {
    let dateArr = dateString.map((date) => {
      return Number.parseInt(date.split('-')[0], 10);
    });
    
    this.setState({
      yearRange: dateArr
    })
  }

  // Method to search
  search = (event, values) => {
    this.props.start();

    let searchForm = {
      title: values.title ? values.title : '',
      director: values.director ? values.director : '',
      genres: values.genres ? values.genres : '',
      year: this.state.yearRange,
      star: values.star ? values.star : ''
    }

    // Clear search redux data
    this.props.resetSearchState();
    this.props.startSearch();

    // Send request to search movie
    window.$axios(API.search.SEARCH({
      ...searchForm
    }))
      .then((response) => {
        let data = response.data;

        if (data.type === 'success') {
          // Set search results to redux
          this.props.setSearchResults({
            results: data.message.results
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
    // Get state variables
    // Get props variables
    const { getFieldDecorator, getFieldsValue } = this.props.form;

    const formItemLayout = {
      labelCol: { span: 2 },
      wrapperCol: { span: 16 },
    };

    const buttonItemLayout = {
      wrapperCol: { span: 16, offset: 2 },
    };

    const style = {
      display: 'flex',
      justifyContent: 'center'
    }

    return (
      <Form onSubmit={this.onSubmit} layout={'horizontal'}>
        <h1 style={{ textAlign: 'center' }}>Search Form</h1>

        {/* Title input */}
        <FormItem style={style} label="Title" {...formItemLayout}>
          {getFieldDecorator('title', {})(
            <Input placeholder="Title of the movie" />
          )}
        </FormItem>

        {/* Director input */}
        <FormItem style={style} label="Director" {...formItemLayout}>
          {getFieldDecorator('director', {})(
            <Input placeholder="Name of Director" />
          )}
        </FormItem>

        {/* Genres selector */}
        <FormItem style={style} label="Genres" {...formItemLayout}>
          {getFieldDecorator('genres', {})(
            <Input placeholder="Input a genre" />
          )}
        </FormItem>

        {/* Director input */}
        <FormItem style={style} label="Star" {...formItemLayout}>
          {getFieldDecorator('star', {})(
            <Input placeholder="Name of Star" />
          )}
        </FormItem>

        {/* Year range selector */}
        <FormItem style={style} label="Year" {...formItemLayout}>
          {getFieldDecorator('year', {})(
            <RangePicker onChange={this.onChange} />
          )}
        </FormItem>

        <FormItem style={style} {...buttonItemLayout}>
          <Button type="primary" onClick={(event) => this.search(event, getFieldsValue())}>Search</Button>
        </FormItem>
      </Form>
    );
  }
}

export default SearchForm = Form.create({})(SearchForm);