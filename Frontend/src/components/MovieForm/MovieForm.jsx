import React from 'react';

// Import Ant Design components
import Input from 'antd/lib/input';
import Form from 'antd/lib/form';
import Icon from 'antd/lib/icon';

const FormItem = Form.Item;

class MovieForm extends React.Component {
  componentDidMount() {
    // To disabled submit button at the beginning.
    this.props.form.validateFields();
  }

  // Check all fields validation
  hasErrors = (fieldsError) => {
    return Object.keys(fieldsError).some(field => fieldsError[field]);
  }

  render() {
    // Props from Ant Design
    const { getFieldDecorator, getFieldsError, getFieldError, isFieldTouched, getFieldsValue } = this.props.form;
    // Custom props
    const { onMovieValuesChange } = this.props;

    // Only show error after a field is touched.
    const titleError = isFieldTouched('title') && getFieldError('title');
    const yearError = isFieldTouched('year') && getFieldError('year');
    const directorError = isFieldTouched('director') && getFieldError('director');
    const starError = isFieldTouched('star') && getFieldError('star');
    const genreError = isFieldTouched('genre') && getFieldError('genre');

    return (
      <Form onChange={(event) => onMovieValuesChange(event, this.hasErrors(getFieldsError()), getFieldsValue())}>
        <FormItem validateStatus={titleError ? 'error' : ''} help={titleError || ''}>
          {getFieldDecorator('title', {
            rules: [{ required: true, message: 'Movie title is required' }],
          })(
            <Input prefix={<Icon type="laptop" style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder="Movie title" />
            )}
        </FormItem>
        <FormItem validateStatus={yearError ? 'error' : ''} help={yearError || ''}>
          {getFieldDecorator('year', {
            rules: [{ required: true, message: 'Invalid year', pattern: /^[0-9]*$/ }],
          })(
            <Input prefix={<Icon type="calendar" style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder="Movie year" />
            )}
        </FormItem>
        <FormItem validateStatus={directorError ? 'error' : ''} help={directorError || ''}>
          {getFieldDecorator('director', {
            rules: [{ required: true, message: 'Invalid year'}],
          })(
            <Input prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder="Movie director" />
            )}
        </FormItem>
        <FormItem validateStatus={starError ? 'error' : ''} help={starError || ''}>
          {getFieldDecorator('star', {
            rules: [{ required: true, message: 'Invalid year'}],
          })(
            <Input prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder="Movie star" />
            )}
        </FormItem>        
        <FormItem validateStatus={genreError ? 'error' : ''} help={genreError || ''}>
          {getFieldDecorator('genre', {
            rules: [{ required: true, message: 'Invalid year'}],
          })(
            <Input prefix={<Icon type="appstore" style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder="Movie genre" />
            )}
        </FormItem>
      </Form>
    );
  }
}

export default MovieForm = Form.create({})(MovieForm);