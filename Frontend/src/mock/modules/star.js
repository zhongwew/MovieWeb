import Mock from 'mockjs';

export const star = [{
  path: /\/MovieServer\/Star\?starID=[A-Za-z0-9%]*/,
  type: 'get',
  data() {
    let result = {
      type: 'success',
      message: {
        star: {},
        movies: []
      }
    };

    // Dummy data for star information
    let star = {
      id: Mock.Random.id(),
      name: Mock.Random.name(1, 2),
      birthYear: Mock.Random.date('yyyy')
    };

    // Dummy data for movies
    let movies = [];

    for (let i = 0; i < 8; i++) {
      let movie = {
        id: Mock.Random.id(),
        title: Mock.Random.title(2, 3),
        year: Mock.Random.natural(1996, 2018),
        director: Mock.Random.name(),
        rating: Mock.Random.natural(1, 10),
        genres: [Mock.Random.title(1), Mock.Random.title(1)],
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

      movies.push(movie);
    }

    result.message.star = star;
    result.message.movies = movies;

    return result;
  }
}, {
  path: '/MovieServer/Star',
  type: 'post',
  data(request) {
    console.log(JSON.parse(request.body))
    let result = {
      type: 'success',
      message: {
      }
    };

    return result;
  }
}]