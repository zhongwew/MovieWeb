var express = require('express');
var router = express.Router();

/* GET users listing. */
router.post('/', function(req, res, next) {
  console.log(req.body);

  res.json({
    type: 'success',
    message: {
      errorMessage: 'Fail to login'
    }
  })
});

module.exports = router;
