export default {
  // Get cart data
  GET_ITEMS: function(){
    return {
      url: '/MovieServer/Cart',
      method: 'get'
    }
  },
  UPDATE_ITEMS: function(data) {
    return {
      url: '/MovieServer/Cart',
      method: 'post',
      data: data
    }
  },
  CONFIRM_CREDIT_CARD: function(data) {
    return {
      url: '/MovieServer/CreditCard',
      method: 'post',
      data: data
    }
  }
}