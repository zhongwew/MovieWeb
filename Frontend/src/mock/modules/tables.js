import Mock from 'mockjs';

export const tables = [{
  path: '/MovieServer/Tables',
  type: 'get',
  data() {
    let results = {
      type: 'success',
      message: {tables: []}
    };

    for (let i = 0 ; i < 6 ; i++) {
      let table = {
        name: Mock.Random.title(2),
        attributes: []
      };

      for (let j = 0 ; j < 5  ; j++) {
        let attribute = {
          key: j,
          name: Mock.Random.title(2),
          type: Mock.Random.title(2),
        }

        table.attributes.push(attribute)
      }

      results.message.tables.push(table);
    }

    return results;
  }
}]