INSERT INTO user (username, description, image, score, created) 
VALUES ("Community", "To an artificial mind all reality is virtual", "4.jpg", 0, 0);

INSERT INTO user_extra (username, password, email) 
VALUES ("Community", "$2a$10$dNvnLK3ZYSyjlwyyAc0Me.FbNvi2yEf6KPKKld62edoC8gChkVGpC", "me@lukas.fyi");

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Tree Climbing", 
"Easy, just find a tree and climb as far up as you can!", "1.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Magic Marble",
"When you get a magic marble, convince somebody that it is magic and tell them to do the same to somebody else", "2.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Cozy Corner",
"Find a random corner and make it a cozy hangout, pillows and blankets for example", 
"3.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Face pimpin'", 
"Paint your faces, glitter, flourescent colour or whatever you feel like!", 
"4.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Eat some nice ice cream", 
"What ice cream are you eating tonight?", "5.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Sit-Dancing", 
"Sit-dance on a dance floor for at least five minutes", "6.jpg", "Community", True);

#INSERT INTO accomplishment (title, description, image, challenge_id, created)
#VALUES ("", "", "2.jpg", 1, 0);
#
#INSERT INTO user_accomplishment (username, accomplishment_id)
#VALUES ("Community", 1);
#
#INSERT INTO session (username, session)
#VALUES ("Community", null);
