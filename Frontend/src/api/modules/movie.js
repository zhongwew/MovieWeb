export default {
  // Get movies data
  GET_MOVIE: function(queryParam){

    let { id } = queryParam;

    let query = `id=${id}`;

    return {
      url: `/MovieServer/Movie?${query}`,
      method: 'get'
    }
  },
  ADD_MOVIE: function(data) {
    return {
      url: '/MovieServer/Movie',
      method: 'post',
      data: data
    }
  }
}