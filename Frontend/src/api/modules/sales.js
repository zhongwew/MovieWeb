export default {
  GET: function() {
    return {
      url: '/MovieServer/Sales',
      method: 'get',
    }
  },
  // Get genres data
  PLACE_ORDER: function(data){
    return {
      url: '/MovieServer/Sales',
      method: 'post',
      data: data
    }
  },
}