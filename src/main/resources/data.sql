INSERT INTO users(name,email,profile_image_url,passwd) VALUES ('tester00','test00@gmail.com',null,'$2a$10$mzF7/rMylsnxxwNcTsJTEOFhh1iaHv3xVox.vpf6JQybEhE4jDZI.');
INSERT INTO users(name,email,profile_image_url,passwd) VALUES ('tester01','test01@gmail.com','http://test','$2a$10$Mu/akK4gI.2RHm7BQo/kAO1cng2TUgxpoP.zBbPOeccVGP4lKVGYy');
INSERT INTO users(name,email,profile_image_url,passwd) VALUES ('tester02','test02@gmail.com','http://test2','$2a$10$hO38hmoHN1k7Zm3vm95C2eZEtSOaiI/6xZrRAx8l0e78i9.NK8bHG');

INSERT INTO stores(name,zip_code,address,lat,lng,likes,user_seq) VALUES ('test store 1', '12345', 'test address0, 12345', '48.12560', '11.72610', 0, 3);
INSERT INTO stores(name,zip_code,address,lat,lng,likes,user_seq) VALUES ('test store 2', '35355', 'test address1, 35355', '33.38339', '128.23376', 220, 3);
INSERT INTO stores(name,zip_code,address,lat,lng,likes,user_seq) VALUES ('test store 3', '24251', 'test address2, 24251', '53.34935', '-8.76619', 15, 1);

INSERT INTO posts(store_seq,user_seq,contents,like_count,comment_count,create_at) VALUES (1, 1,'test01 first post',1,1,'2019-03-01 13:10:00');
INSERT INTO posts(store_seq,user_seq,contents,like_count,comment_count,create_at) VALUES (2, 1,'test01 second post',0,0,'2019-03-12 09:45:00');
INSERT INTO posts(store_seq,user_seq,contents,like_count,comment_count,create_at) VALUES (1, 1,'test01 third post',0,0,'2019-03-20 19:05:00');
INSERT INTO posts(store_seq,user_seq,contents,like_count,comment_count,create_at) VALUES (1, 2,'test02 post',0,1,'2019-03-20 15:13:20');

INSERT INTO connections(user_seq,target_seq,granted_at,create_at) VALUES (1,2,'2019-03-31 13:00:00','2019-03-31 00:10:00');
