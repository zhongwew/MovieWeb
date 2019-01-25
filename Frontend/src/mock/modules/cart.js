import Mock from 'mockjs';

export const cart = [{
    path: '/MovieServer/Cart',
    type: 'get',
    data() {
      let result = {
        type: 'success',
        message: {
          items: []
        }
      };

      let items = [];

      for (let i = 0; i < 4; i++) {
        let item = {
          id: Mock.Random.id(),
          title: Mock.Random.title(2, 3),
          year: Mock.Random.natural(1996, 2018),
          director: Mock.Random.name(),
          rating: Mock.Random.natural(1, 10),
          quantity: Mock.Random.natural(1, 10),
          price: Mock.Random.float(10, 50),
        };

        items.push(item);
      }

      result.message.items = items;

      return result;
    }
  },
  {
    path: '/MovieServer/Cart',
    type: 'post',
    data(request) {
      let body = JSON.parse(request.body);

      let result = {
        type: 'success',
        message: {
          items: []
        }
      };

      result.message.items = body.items;

      return result;
    }
  },
  {
    path: '/MovieServer/CreditCard',
    type: 'post',
    data() {
      let result = {
        type: 'success',
      }

      return result;
    }
  }
]