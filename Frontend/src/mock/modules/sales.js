import Mock from 'mockjs';

export const sales = [
  {
    path: '/MovieServer/Sales',
    type: 'post',
    data() {
      let result = {
        type: 'success',
        message: {
          id: Mock.Random.id()
        }
      };

      return result;
    }
  },
  {
    path: '/MovieServer/Sales',
    type: 'get',
    data: {
      type: 'success',
      message: {
        'movies|5': [{
          'key|+1': 1,
          saleDate: '2017',
          title: Mock.Random.title(2),
        }]
      }
    }
  }
]