import React from 'react';

// Import Ant Design components
import Input from 'antd/lib/input';
import Form from 'antd/lib/form';
import Icon from 'antd/lib/icon';

const FormItem = Form.Item;

class StarForm extends React.Component {
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
    const { onStarValuesChange } = this.props;

    // Only show error after a field is touched.
    const nameError = isFieldTouched('name') && getFieldError('name');
    const birthYearError = isFieldTouched('birthYear') && getFieldError('birthYear');

    return (
      <Form onChange={(event) => onStarValuesChange(event, this.hasErrors(getFieldsError()), getFieldsValue())}>
        <FormItem validateStatus={nameError ? 'error' : ''} help={nameError || ''}>
          {getFieldDecorator('name', {
            rules: [{ required: true, message: 'Star name is required' }],
          })(
            <Input prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder="Star name" />
            )}
        </FormItem>
        <FormItem validateStatus={birthYearError ? 'error' : ''} help={birthYearError || ''}>
          {getFieldDecorator('birthYear', {
            rules: [{ required: true, message: 'Invalid birth year', pattern: /^[0-9]*$/ }],
          })(
            <Input prefix={<Icon type="calendar" style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder="Star birth year" />
            )}
        </FormItem>
      </Form>
    );
  }
}

export default StarForm = Form.create({})(StarForm);