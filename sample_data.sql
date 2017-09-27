INSERT INTO user (username, password, description, image, score, created) 
VALUES ("spydon", "$2a$10$8R65ZRsGLM4oQMVsq…LLFIXEoPtgUWQsORP2MVT9y", "Sample description", "1.jpg", 0, 0);

INSERT INTO mission (title, description, image, creator, validated) 
VALUES ("New mission with extremely long title, is it fine?", "The description is actually also quite long how will all the views handle such a long description? I don't know, I guess we will find out!", "2.jpg  ", "spydon", True);

INSERT INTO mission (title, description, image, creator, validated) 
VALUES ("Short and tall picture", "The description is short", "5.jpg", "spydon", True);

INSERT INTO mission (title, description, image, creator, validated) 
VALUES ("Medium length mission title, it is fine.", "The description is actually also quite medium how will all the views handle such a long description? I don't know, I guess we will find out!", "5.jpg", "spydon", True);

INSERT INTO accomplishment (title, description, image, mission_id, created)
VALUES ("Accomplishment Title!", "This was quite an accomplishment, wasn't it?", "2.jpg", 1, 0);

INSERT INTO user_accomplishment (username, accomplishment_id)
VALUES ("spydon", 1);
