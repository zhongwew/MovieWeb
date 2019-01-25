export default {
  // Get cart data
  GET_ITEMS: function(queryParam){
    let {
      keyword
    } = queryParam;

    let query = `keyword=${keyword}`;

    return {
      url: `/MovieServer/Suggestion?${query}`,
      method: 'get'
    }
  },
}