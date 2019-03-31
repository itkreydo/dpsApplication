const express = require('express'),
http = require('http'),
app = express(),
server = http.createServer(app),
mysql = require('mysql'),
io = require('socket.io').listen(server);

var connectionsCount=0;

//var connection = mysql.createConnection({
//  host     : 'localhost',
//  user     : 'root',
//  password : '',
//  database : 'dps'
//}); 

//connection.connect(function(err) {
//  if (err) {
//    console.error('error connecting: ' + err.stack);
//    return;
//  }
// 
//  console.log('connected as id ' + connection.threadId);
//});

var raw = [];
app.get('/', (req, res) => {
res.send('Chat Server is running on port 3000')
});

io.on('connection', (socket) => {
connectionsCount++;
console.log('user connected')

socket.on('join', function(userNickname) {
console.log(userNickname +" : has joined the chat "  );
socket.broadcast.emit('userjoinedthechat',userNickname +" : has   joined the chat ");

var connection = mysql.createConnection({
  host     : 'localhost',
  user     : 'root',
  password : '',
  database : 'dps'
}); 
connection.connect(function(err) {
  if (err) {
    console.error('error connecting: ' + err.stack);
    return;
  }
  console.log('connected as id ' + connection.threadId);
});
 connection.query('SELECT * FROM message', function (error, results, fields) {
  if (error) throw error;
    
     
    var objs = [];
     var jsonres = {};
    for (var i = 0;i < results.length; i++) {
        objs.push({username: results[i].nickname,message: results[i].message});
    }
     jsonres = {messages:objs};
     console.log(JSON.stringify(jsonres));
    socket.emit("updateDialog",JSON.stringify(jsonres));
     
  // connected!
});
connection.end();

    })
    
socket.on('messagedetection', (senderNickname,messageContent) => {       
       //log the message in console 
       console.log(senderNickname+" : " +messageContent)       
      //create a message object       
      let  message = {"message":messageContent, 
                      "senderNickname":senderNickname} 
      //add to database
      var connection = mysql.createConnection({
  host     : 'localhost',
  user     : 'root',
  password : '',
  database : 'dps'
}); 
connection.connect(function(err) {
  if (err) {
    console.error('error connecting: ' + err.stack);
    return;
  }
  console.log('connected as id ' + connection.threadId);
});
var post  = {message: messageContent, nickname: senderNickname};
 connection.query("INSERT INTO message SET ?",post, function (error, results, fields) {
  if (error) throw error;
     
  // connected!
});
connection.end();
      
      
// send the message to all users including the sender  using io.emit         
      socket.broadcast.emit('message', message )   
      })
     socket.on('disconnect', function() {
         connectionsCount--;
        //console.log(userNickname +' has left ')
        socket.broadcast.emit( "userdisconnect" ,' user has left')
    })

})



server.listen(3000,()=>{
console.log('Node app is running on port 3000')
})