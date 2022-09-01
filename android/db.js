var mysql=require('mysql');
var con;

exports.connect=function(){
    con=mysql.createPool({
        connectionLimit:100,
        host:'localhost',
        user:'node',
        password:'pass',
        database:'nodedb',
        port:'3306'
    })
}

exports.get=function(){
    return con;
}