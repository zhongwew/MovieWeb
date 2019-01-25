import Mock from 'mockjs';

export const movie = [{
  path: /\/MovieServer\/Movie\?id=[A-Za-z0-9%]*/,
  type: 'get',
  data() {
    let result = {
      type: 'success',
      message: {}
    };

    let movie = {
      id: Mock.Random.id(),
      title: Mock.Random.title(2, 3),
      year: Mock.Random.natural(1996, 2018),
      director: Mock.Random.name(),
      rating: Mock.Random.natural(1, 10),
      genres: [Mock.Random.title(1), Mock.Random.title(1)],
      stars: []
    };

    let stars = [];

    for (let j = 1; j <= Mock.Random.natural(2, 3); j++) {
      let star = {
        id: j,
        name: Mock.Random.name()
      };

      stars.push(star);
    }

    movie.stars = stars;

    result.message.movie = movie;

    return result;
  }
},{
  path: '/MovieServer/Movie',
  type: 'post',
  data(request) {
    console.log(JSON.parse(request.body))
    let result = {
      type: 'success',
      message: {}
    };

    return result;
  }
}]