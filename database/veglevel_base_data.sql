INSERT INTO user (username, description, image, score, `level`, created)
VALUES ("Community", "", "community.jpg", 0, 0, 0);

INSERT INTO user_extra (username, password, email) 
VALUES ("Community", "$2a$10$dNvnLK3ZYSyjlwyyAc0Me.FbNvi2yEf6KPKKld62edoC8gChkVGpC", "me@lukas.fyi");

INSERT INTO challenge (title, description, image, creator, validated, level_restriction)
VALUES (
"Selfie Tutorial",
"This is the first challenge of VegLevel, it will set your profile picture. Don't worry you can change it later.",
"v0_selfie.jpg", "Community", True, 0);

UPDATE challenge SET id = 0 WHERE title = "Selfie Tutorial";

INSERT INTO challenge (title, description, image, creator, validated, level_restriction)
VALUES (
"Grass Breakfast?!",
"Simply make a vegan breakfast.\nIt is easy once you know how to, but in a lot of countries breakfasts are bases around dairy, sausages and eggs.\nExample recipe: https://www.silviacooks.org/guacamole/",
"v1_breakfast.jpg", "Community", True, 1);

INSERT INTO challenge (title, description, image, creator, validated, level_restriction)
VALUES (
"Speedy Lunch",
"This challenge is all about speed.\nCook a vegan lunch, but you have a maximum of 30 minutes to do it.\n\nIf you need some inspiration; why not throw some root veggies into the oven and make a sauce to have with it.\nExample recipe: http://www.silviacooks.org/sweet-potatoes-tahini/",
"v2_lunch.jpg", "Community", True, 2);

INSERT INTO challenge (title, description, image, creator, validated, level_restriction)
VALUES (
"Proper Dinner",
"This dinner should be something that you feel really stuffed after you have finished eating it.\nA lot of people complain about them \"not feeling full\" after having eaten something vegetarian, your challenge is to prove those people wrong and make a dish that will floor you.",
"v3_dinner.jpg", "Community", True, 3);

INSERT INTO challenge (title, description, image, creator, validated, level_restriction)
VALUES (
"Fika Time",
"Fika is a Swedish word for \"a coffee/tea and cake break\" and it is also a lot about socializing.\nSo invite a friend (or stranger) to a vegan fika, you don't have to make the fika yourself but if you want to I can recommend this recipe which the picture is from: https://www.silviacooks.org/nutella-strawberries-tart/",
"v4_fika.jpg", "Community", True, 4);

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
(1, 1),
(2, 1),
(3, 2),
(4, 1),
(0, 1);
