export default {
  // Get genres data
  SEARCH: function(data){
    return {
      url: '/MovieServer/Search',
      method: 'post',
      data: data
    }
  },
}