import Mock from 'mockjs';

export const user = [{
  path: '/MovieServer/User',
  type: 'get',
  data() {
    let result = {
      type: 'fail',
      message: {
        user: {
          firstName: Mock.Random.name(),
          lastName: Mock.Random.name(),
          address: Mock.Random.region()
        }
      }
    };

    return result;
  }
}]