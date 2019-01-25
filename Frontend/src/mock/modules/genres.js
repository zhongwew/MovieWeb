import Mock from 'mockjs';

export const genres = [
  {
    path: '/MovieServer/Genres',
    type: 'get',
    data() {
      let result = {
        type: 'success',
        message: {}
      };

      let genres = [];

      for (let i = 0; i < 6 ; i++) {
        let genre = {
          genre: Mock.Random.title(1),
          id: i+1
        };

        genres.push(genre);
      }
      
      result.message.genres = genres;

      return result;
    }
  }
]