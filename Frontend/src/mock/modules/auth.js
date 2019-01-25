export const auth = [
  {
    path: '/MovieServer/Login',
    type: 'post',
    data(request) {
      let body = JSON.parse(request.body);

      let result = {
        type: 'success',
        message: {
          email: body.email,
          password: body.password
        }
      }

      return result;
    }
  },
  {
    path: '/MovieServer/Logout',
    type: 'get',
    data(request) {
      let body = JSON.parse(request.body);

      let result = {
        type: 'success',
        message: {
          email: body.email,
          password: body.password
        }
      }

      return result;
    }
  },
  {
    path: '/MovieServer/IsLogin',
    type: 'get',
    data() {
      let result = {
        type: 'fail',
        message: {
          errorMessage: `You haven't sign in`
        }
      }

      return result;
    }
  }
]