import React from 'react';

// Import Ant Design components
import Input from 'antd/lib/input';
import Form from 'antd/lib/form';
import Icon from 'antd/lib/icon';
import Button from 'antd/lib/button';
import message from 'antd/lib/message';

// Import API
import API from '@/api/index';

const FormItem = Form.Item;

class TransactionForm extends React.Component {
  componentDidMount() {
    // To disabled submit button at the beginning.
    this.props.form.validateFields();
  }

  confirm = (event, values) => {
    window.$axios(API.cart.CONFIRM_CREDIT_CARD({
      ...values
    }))
    .then((response) => {
      let data = response.data;

      if (data.type === 'success') {
        this.props.confirmCC();

        message.success('You have confirmed your credit card!');
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
    // Props from Ant Design
    const { getFieldDecorator, getFieldsValue } = this.props.form;
    // Props from Cart
    const { isConfirm } = this.props;

    // Form layout
    const formItemLayout = {
      labelCol: { span: 3 },
      wrapperCol: { span: 21 },
    };
    const style = {
      display: 'flex',
      justifyContent: 'center'
    };
    const buttonItemLayout = {
      wrapperCol: { span: 24 },
      style: {
        ...style,
        textAlign: 'right'
      }
    };

    return (
      <Form >
        <FormItem style={style} label="Credit Card" {...formItemLayout}>
          {getFieldDecorator('ccId', {})(
            <Input disabled={isConfirm} prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder="Credit card ID" />
          )}
        </FormItem>

        <FormItem style={style} label="First Name" {...formItemLayout}>
          {getFieldDecorator('firstName', {})(
            <Input disabled={isConfirm} prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />} type="text" placeholder="First Name" />
          )}
        </FormItem>

        <FormItem style={style} label="Last Name" {...formItemLayout}>
          {getFieldDecorator('lastName', {})(
            <Input disabled={isConfirm}  prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />} type="text" placeholder="Last Name" />
          )}
        </FormItem>

        <FormItem {...buttonItemLayout}>
          <Button disabled={isConfirm}  type="primary" onClick={(event) => this.confirm(event, getFieldsValue())}>Confirm Credit Card</Button>
        </FormItem>
      </Form>
    );
  }
}

export default TransactionForm = Form.create({})(TransactionForm);