export default {
  // Get genres data
  LOGIN: function(data){
    return {
      url: '/MovieServer/Login',
      method: 'post',
      data: data
    }
  },
  LOGOUT: function(data) {
    return {
      url: '/MovieServer/Logout',
      method: 'post',
      data: data
    }
  },
  ISLOGIN: function() {
    return {
      url: '/MovieServer/IsLogin',
      method: 'get',
    }
  }
}