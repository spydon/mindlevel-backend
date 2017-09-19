INSERT INTO user (username, password, description, image, score, created) 
VALUES ("spydon", "$2a$10$8R65ZRsGLM4oQMVsq…LLFIXEoPtgUWQsORP2MVT9y", "Sample description", "http://www.gstatic.com/webp/gallery/1.jpg", 0, 0);

INSERT INTO mission (title, description, image, creator, validated) 
VALUES ("New mission with extremely long title, is it fine?", "The description is actually also quite long how will all the views handle such a long description? I don't know, I guess we will find out!", "https://upload.wikimedia.org/wikipedia/commons/d/d1/Mount_Everest_as_seen_from_Drukair2_PLW_edit.jpg", "spydon", True);

INSERT INTO mission (title, description, image, creator, validated) 
VALUES ("Short and tall picture", "The description is short", "https://i.pinimg.com/736x/9b/a2/e9/9ba2e9ebc7ddf81e8f4fc5cc9dd4fdd9--vintage-tins-vintage-stuff.jpghttps://i.pinimg.com/736x/9b/a2/e9/9ba2e9ebc7ddf81e8f4fc5cc9dd4fdd9--vintage-tins-vintage-stuff.jpg", "spydon", True);

INSERT INTO mission (title, description, image, creator, validated) 
VALUES ("Medium length mission title, it is fine.", "The description is actually also quite medium how will all the views handle such a long description? I don't know, I guess we will find out!", "http://www.gstatic.com/webp/gallery/5.jpg", "spydon", True);

INSERT INTO accomplishment (title, description, image, mission_id, created)
VALUES ("Accomplishment Title!", "This was quite an accomplishment, wasn't it?", "http://www.gstatic.com/webp/gallery/2.jpg", 1, 0);

INSERT INTO user_accomplishment (username, accomplishment_id)
VALUES ("spydon", 1);
