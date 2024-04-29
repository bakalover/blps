curl -X POST -H "Content-Type: application/json" -d '{"username": "user1", "passwdString": "hahaha"}' http://localhost:35080/user/register
curl -X POST -H "Content-Type: application/json" -d '{"username": "user2", "passwdString": "sdfghjk"}' http://localhost:35080/user/register
curl -u user1:hahaha -X POST -H "Content-Type: application/json" -d '{"name": "album1", "description": "nice pics", "restrictMode": 2, "username": "user1"}' http://localhost:35080/album/add

curl -u user1:hahaha -X POST -F "path=@./testpic/avagz.jpg" -F "albumId=1" -F "face=true" http://localhost:35080/image/add
curl -u user1:hahaha -X POST -F "path=@./testpic/avagz.jpg" -F "albumId=1" -F "face=true" http://localhost:35080/image/add

curl -u user1:hahaha -X POST -H "Content-Type: application/json" -d '{"username": "user1", "description": "bad pic", "picId": "1"}' http://localhost:35080/image/complaint
curl -u user1:hahaha -X GET http://localhost:35080/image/complaints/1
curl -u admin1:admin -X GET http://localhost:35080/image/complaints/1
curl -u moderator:moderator -X GET http://localhost:35080/image/complaints/1


curl -u user1:hahaha -X POST http://localhost:35080/user/ban/user1 
curl -u admin1:admin -X POST http://localhost:35080/user/ban/user1 
curl -u moderator:moderator -X POST http://localhost:35080/user/ban/user1 


