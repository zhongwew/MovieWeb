export default {
  // Get cart data
  GET_ITEMS: function(queryParam){
    let {
      keyword
    } = queryParam;

    let query = `keyword=${keyword}&count=100`;

    return {
      url: `/MovieServer/NormalSearch?${query}`,
      method: 'get'
    }
  },
}