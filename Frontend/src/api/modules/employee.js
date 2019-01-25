export default {
  // Get genres data
  LOGIN: function(data){
    return {
      url: '/MovieServer/Employee/Login',
      method: 'post',
      data
    }
  },
}