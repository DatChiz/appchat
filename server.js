var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var mongodb = require('mongodb');
var ObjectId = require('mongodb').ObjectID;

var MongoClient = mongodb.MongoClient;
var url = 'mongodb://localhost:27017/ChatApp';


MongoClient.connect(url, function (err, db) {
    if (err) {
        console.log('Unable to connect to the mongoDB server. Error:', err);
    } else {
        //HURRAY!! We are connected. :)
        console.log('Connection established to', url);
        _user = db.collection('User');
        _conversation = db.collection('Conversation');
    }
});



app.get('/', function (req, res){
    res.sendfile('index.html');
});


io.on('connection', function (socket) {
    socket.on('login', function (username, password) {
        console.log(username + " login");

        var cursor = _user.find({name:username});
        cursor.each(function (err, doc) {
            if (err) {
                
                socket.emit('login', false);
            } else {
                if(doc != null){
                    if(doc.password == password){
                        var objContact =  {"_id" :  doc._id , "contact" : []};
                        
                        var cursorContact = _conversation.find({"members" : {$elemMatch :{"id" : (doc._id).toString()}} });
                        cursorContact.toArray(function (err, result) {
                            for (i = 0, count = result.length; i < count; i++) {
                                var doc = result[i];
                                objContact['contact'].push(doc);
                                
                                // if(doc['members'].length == 2){
                                    // objContact['contact'].push(doc);
                                // }else{
                                    // objContact['contactgroup'].push(doc);
                                // }
                            }
                            console.log(objContact);
                            
                            socket.emit('login', true, objContact);
                        });
                        
                    }else{
                        socket.emit('login', false, "check user or pass");
                    }

                }
            }
        });

    });

    socket.on('register', function (name, password) {
        var cursor = _user.find({name : name});
        cursor.count(function (err, count) {
            if(count == 0){
                console.log(name + "register");

                var user = {name: name, password: password, state: {online: false, available: true}, contacts: []};

                _user.insert(user, function (err, result) {
                    if (err) {
                        console.log(err);
                        socket.emit('register', false);
                    } else {
                        console.log('Inserted new user ok');
                        socket.emit('register', true);
                    }
                });
            }else{
                socket.emit('register', false);
            }
        });
        
    });
    
    socket.on('conversation', function (id, body, contactID ) {
        console.log(body);
        console.log(contactID);
        
        if(!id || !body || !contactID){
            console.log('id or body or contactID is null');
        }else{
            var message = {id : id, body : body};
            _conversation.update({_id : ObjectId(contactID)}, { $push: {message : message}});
            io.sockets.emit('conversation', message, contactID);
        }
        // function (err, result) {
            // if (err) {
                // console.log(err);
                // socket.emit('conversation', false);
            // } else {
                // console.log('Inserted');
                // socket.emit('conversation', message);
            // }
        // });
    });
    
    
    socket.on('update_message', function (contactID) {
        if(!contactID){
            console.log('contactID is null');
        }else{
            console.log(contactID);
            
            var cursor = _conversation.find({_id : ObjectId(contactID)});
            cursor.each(function(error, result){
                if(error){
                    console.log(error);
                }else{
                    if(result){
                        socket.emit('update_message', result);
                    }
                }
            });     
        }
    });
    
    socket.on('delete_contact', function (contactID) {
        if(!contactID){
            console.log('contactID is null');
        }else{
            console.log('delete_contact: ' + contactID);
            
            _conversation.remove({_id : ObjectId(contactID)});
            io.sockets.emit('delete_contact', contactID); 
        }
    });
    
    socket.on('add_contact', function (userName, id) {
        if(!userName){
            console.log('userName is null');
        }else{
            console.log('add_contact: ' + userName);
            
            var contact = {"members" : [ ],"message" : [ ], "color" : false};
            var cursor = _user.find({$or : [{name : userName} , {_id : ObjectId(id)}]});
            cursor.toArray(function (err, result) {
                if(err){
                    console.log(err);
                }else{
                    var count = result.length;
                    if(count == 2){
                        for (i = 0; i < count; i++) {
                            var doc = {"id" : (result[i]._id).toString(), "name" : result[i].name};
                            contact['members'].push(doc);
                        }
                        
                        console.log('contact: ' + contact);
                        
                        var cursor = _conversation.find({"members" : {$in : [contact['members']]}});
                        cursor.count(function (err, count) {
                            if(err){
                                console.log(err);
                            }else{
                                console.log(count);
                                if(count == 0){
                                    _conversation.insert(contact, function (err, result) {
                                        if (err) {
                                            console.log(err);
                                        } else {
                                            console.log('Inserted new conversation ok');
                                            io.sockets.emit('add_contact', contact);
                                        }
                                    });
                                }
                            }
                        });
                    }else{
                        console.log('don\'t find contact');
                    }
                }
            });             
        }
    });

    socket.on('make_color', function (contactID, id) {
        console.log(contactID);
        _conversation.update({ _id: contactID },{$set: {"color" : true}});
        io.sockets.emit('make_color', contactID, id);
    });
    
    socket.on('make_color_default', function (contactID) {
        console.log(contactID);
        _conversation.update({ _id: contactID },{$set: {"color" : false}});
        socket.emit('make_color_default', contactID);
    });

});

http.listen(3000, function(){
    console.log('listening on *:3000');
});