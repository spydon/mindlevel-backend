INSERT INTO user (username, description, image, score, `level`, created)
VALUES ("Community", "", "community.jpg", 0, 0, 0);

INSERT INTO user_extra (username, password, email) 
VALUES ("Community", "$2a$10$dNvnLK3ZYSyjlwyyAc0Me.FbNvi2yEf6KPKKld62edoC8gChkVGpC", "me@lukas.fyi");

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Selfie Tutorial",
"This is the first challenge of VegLevel, it will set your profile picture. Don't worry you can change it later.",
"0_selfie.jpg", "Community", True);

UPDATE challenge SET id = 0 WHERE title = "Selfie Tutorial";

INSERT INTO category (name, image)
VALUES
("Simple", "category_simple.jpg");

INSERT INTO category (name, image)
VALUES
("Medium", "category_medium.jpg");

INSERT INTO category (name, image)
VALUES
("Hard", "category_hard.jpg");

INSERT INTO challenge_category (challenge_id, category_id)
VALUES
(0, 1);
