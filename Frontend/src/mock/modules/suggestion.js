import Mock from 'mockjs';

export const suggestion = [{
  path: /\/MovieServer\/Suggestion\?[a-z]*/,
  type: 'get',
  data() {
    let results = {
      type: 'success',
      message: {suggestions: [
        {
          title: 'Movies',
          children: [
            {
              title: Mock.Random.title(1),
              id: Mock.Random.id()
            },
            {
              title: Mock.Random.title(1),
              id: Mock.Random.id()
            },
            {
              title: Mock.Random.title(1),
              id: Mock.Random.id()
            }
          ]
        },
        {
          title: 'Stars',
          children: [
            {
              title: Mock.Random.title(1),
              id: Mock.Random.id()
            },
            {
              title: Mock.Random.title(1),
              id: Mock.Random.id()
            },
            {
              title: Mock.Random.title(1),
              id: Mock.Random.id()
            }
          ]
        }
      ]}
    };

    return results;
  }
}]