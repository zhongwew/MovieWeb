export default {
  // Get movies data
  GET_MOVIES: function(queryParam){

    let { page, genre, title, sortType, count } = queryParam;

    let query = `page=${page}`;
    
    if (genre) {
      query = query + `&genre=${genre}`;
    }
    if (title) {
      query = query + `&title=${title}`;
    }
    if (sortType) {
      query = query + `&sortType=${sortType}`;
    }
    if (count) {
      query = query + `&count=${count}`
    }

    return {
      url: `/MovieServer/MovieList?${query}`,
      method: 'get'
    }
  },
}