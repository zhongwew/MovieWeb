import React from 'react';

// Import Ant Design components
import Input from 'antd/lib/input';
import Form from 'antd/lib/form';
import Icon from 'antd/lib/icon';

// Import reCaptcha component
import Recaptcha from 'react-recaptcha';

const FormItem = Form.Item;

class LoginForm extends React.Component {
  state = {
    recaptcha: ''
  };

  componentDidMount() {
    // To disabled submit button at the beginning.
    this.props.form.validateFields();
  }

  // Check all fields validation
  hasErrors = (fieldsError) => {
    return Object.keys(fieldsError).some(field => fieldsError[field]);
  }

  // Verfiy the reCaptcha component
  verifyCallback = (response) => {
    const { getFieldsError, getFieldsValue } = this.props.form;
    const { onLoginValuesChange } = this.props;

    onLoginValuesChange(null, this.hasErrors(getFieldsError()), getFieldsValue(), response);
  };

  render() {
    // State params
    const { recaptcha } = this.state;
    // Props from Ant Design
    const { getFieldDecorator, getFieldsError, getFieldError, isFieldTouched, getFieldsValue } = this.props.form;
    // Custom props
    const { onLoginValuesChange } = this.props;

    // Only show error after a field is touched.
    const emailError = isFieldTouched('email') && getFieldError('email');
    const passwordError = isFieldTouched('password') && getFieldError('password');

    return (
      <Form onChange={(event) => onLoginValuesChange(event, this.hasErrors(getFieldsError()), getFieldsValue(), recaptcha)}>
        <FormItem validateStatus={emailError ? 'error' : ''} help={emailError || ''}>
          {getFieldDecorator('email', {
            rules: [{ required: true, message: 'Invalid email address!', pattern: /^([\w-_]+(?:\.[\w-_]+)*)@((?:[a-z0-9]+(?:-[a-zA-Z0-9]+)*)+\.[a-z]{2,6})$/i }],
          })(
            <Input prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder="Email" />
            )}
        </FormItem>
        <FormItem validateStatus={passwordError ? 'error' : ''} help={passwordError || ''}>
          {getFieldDecorator('password', {
            rules: [{ required: true, message: 'Please input your Password!' }],
          })(
            <Input prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />} type="password" placeholder="Password" />
            )}
        </FormItem>

        {/* reCapthca component */}
        {/*<FormItem validateStatus={passwordError ? 'error' : ''} help={passwordError || ''}>*/}
          {/*<Recaptcha */}
            {/*sitekey="6Lcr6kUUAAAAAAFfbWQ1irEYOeTiYg2xZuyrYoni"*/}
            {/*render="explicit"*/}
            {/*// type="audio"*/}
            {/*verifyCallback={this.verifyCallback} />*/}
        {/*</FormItem>*/}
      </Form>
    );
  }
}

export default LoginForm = Form.create({})(LoginForm);