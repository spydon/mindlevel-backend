INSERT INTO user (username, description, image, score, created) 
VALUES ("spydon", "Sample description", "1.jpg", 0, 0);

INSERT INTO user_extra (username, password, email) 
VALUES ("spydon", "$2a$10$dNvnLK3ZYSyjlwyyAc0Me.FbNvi2yEf6KPKKld62edoC8gChkVGpC", "me@lukas.fyi");

INSERT INTO challenge (title, description, image, creator, validated)
VALUES ("New challenge with extremely long title, is it fine?", "The description is actually also quite long how will all the views handle such a long description? I don't know, I guess we will find out!", "2.jpg", "spydon", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES ("Short and tall picture", "The description is short", "5.jpg", "spydon", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES ("Medium length challenge title, it is fine.", "The description is actually also quite medium how will all the views handle such a long description? I don't know, I guess we will find out!", "5.jpg", "spydon", True);

INSERT INTO accomplishment (title, description, image, mission_id, created)
VALUES ("Accomplishment Title!", "This was quite an accomplishment, wasn't it?", "2.jpg", 1, 0);

INSERT INTO user_accomplishment (username, accomplishment_id)
VALUES ("spydon", 1);

INSERT INTO session (username, session)
VALUES ("spydon", null);
