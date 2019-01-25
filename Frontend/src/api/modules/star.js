export default {
  // Get star data
  GET_STAR: function (queryParam) {

    let {
      id
    } = queryParam;

    let query = `starID=${id}`;

    return {
      url: `/MovieServer/Star?${query}`,
      method: 'get'
    }
  },
  // Add star data
  ADD_STAR: function (data) {
    return {
      url: `/MovieServer/Star`,
      method: 'post',
      data: data
    }
  },
}