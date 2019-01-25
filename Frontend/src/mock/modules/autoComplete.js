import Mock from 'mockjs';

export const autoComplete = [
  {
    path: /\/MovieServer\/NormalSearch\?keyword=[a-z]*/,
    type: 'get',
    data(request) {
      let result = {
        type: 'success',
        message: {
          movies: []
        }
      };

      // Generate movies
      for (let i = 0; i < 8 ; i++) {
        let movie = {
          id: Mock.Random.id(),
          title: Mock.Random.title(2, 3),
          year: Mock.Random.natural(1996, 2018),
          director: Mock.Random.name(),
          rating: Mock.Random.natural(1, 10),
          genres: [Mock.Random.title(1), Mock.Random.title(1)],
        };

        let stars = [];

        for (let j = 1 ; j <= Mock.Random.natural(2, 3) ; j ++) {
          let star = {
            id: j,
            name: Mock.Random.name()
          };

          stars.push(star);
        }

        movie.stars = stars;

        result.message.movies.push(movie);
      }

      return result;
    }
  },
]