var express = require('express');
var router = express.Router();

const faker = require('faker');
const Mock  = require('mockjs');

/* GET users listing. */
router.get('/', function (req, res, next) {
  console.log(req.query);
  let {
    count,
    keyword
  } = req.query;

  let result = {
    type: 'success',
    message: {
      movies: [],
    }
  }

  for (let i = 0; i < count * 10; i++) {
    let movie = {
      stars: [{
          id: 1,
          name: "Joji Matsuoka",
        },
        {
          id: 2,
          name: "Yui Natsukawa",
        }
      ],
      year: faker.random.number(),
      director: faker.name.findName(),
      genres: ["Family", "Drama"],
      id: i,
      title: faker.lorem.word()
    };

    result.message.movies.push(movie);
  }

  console.log(result);

  res.json(result);
});

module.exports = router;