var express = require('express');
var router = express.Router();
var db= require('../db');

/* GET users listing. */
router.get('/', function(req, res, next) {
  var sql='select * from user order by id';
  db.get().query(sql, function(err,rows){
    res.send(rows);
  })
});

router.post('/insert',function(req,res){
  var id=req.body.id;
  var pass=req.body.pass;
  var name=req.body.name;
  var sql='insert into user(id,pass,name) values(?,?,?)'
  db.get().query(sql,[id,pass,name],function(err,rows){
    res.sendStatus(200);

  })

})

router.get('/read',function(req,res){
  var id=req.query.id;
  var sql="select * from user where id=?"
  db.get().query(sql,[id],function(err,rows){
    res.send(rows);

  })
})

module.exports = router;
