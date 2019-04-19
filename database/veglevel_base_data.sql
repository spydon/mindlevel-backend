INSERT INTO user (username, description, image, score, `level`, created)
VALUES ("Community", "", "community.jpg", 0, 0, 0);

INSERT INTO user_extra (username, password, email) 
VALUES ("Community", "$2a$10$dNvnLK3ZYSyjlwyyAc0Me.FbNvi2yEf6KPKKld62edoC8gChkVGpC", "me@lukas.fyi");

INSERT INTO challenge (title, description, image, creator, validated, level_restriction)
VALUES (
"Selfie Tutorial",
"This is the first challenge of VegLevel, it will set your profile picture. Don't worry you can change it later.",
"v0_selfie.jpg", "Community", True, 0);

INSERT INTO challenge (title, description, image, creator, validated, level_restriction)
VALUES (
"Grass Breakfast?!",
"Simply make a vegan breakfast.\nIt is easy once you know how to, but in a lot of countries breakfasts are based around dairy, sausages and eggs.\nExample recipe: https://www.silviacooks.org/guacamole/",
"v1_breakfast.jpg", "Community", True, 1);

INSERT INTO challenge (title, description, image, creator, validated, level_restriction)
VALUES (
"Animalographer",
"If you don't eat breakfast, this challenge is for you (To get to the next level)! If you do eat breakfast it is fine too...\n\nIt's simple, find an animal and photograph it.",
"v1_2_animal.jpg", "Community", True, 1);

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
"Cult Interests",
"Invite a friend to VegLevel, the picture can be of your friend or their/your phone phone for example.",
"v3_2_cult.jpg", "Community", True, 3);

INSERT INTO challenge (title, description, image, creator, validated, level_restriction)
VALUES (
"Fika Time",
"Fika is a Swedish word for \"a coffee/tea and cake break\" and it is also a lot about socializing.\nSo invite a friend (or stranger) to a vegan fika, you don't have to make the fika yourself but if you want to I can recommend this recipe which the picture is from: https://www.silviacooks.org/nutella-strawberries-tart/",
"v4_fika.jpg", "Community", True, 4);

INSERT INTO challenge (title, description, image, creator, validated, level_restriction)
VALUES (
"Lawn mower",
"Sometimes people like to make fun of vegans so it is good to have a bit of self distance.\nIn this challenge find a joke or a meme and realize it in a picture.\nFor example with this picture which is connected to the joke \"That moment when your steak is on the grill and you feel your mouth watering.\n\nDo vegans feel the same way when mowing their lawn?\".",
"v5_wheelbarrow.jpg", "Community", True, 5);

INSERT INTO challenge (title, description, image, creator, validated, level_restriction)
VALUES (
"Documentary",
"Watch a documentary about veganism. Examples are Cowspiracy, Forks over Knifes, Earthlings, What The Health, Maximum Tolerated Dose, Live and Let Live or The Ghosts in Our Machine",
"v6_documentary.jpg", "Community", True, 6);

INSERT INTO challenge (title, description, image, creator, validated, level_restriction)
VALUES (
"Daily Change",
"When you need to buy a new daily product, buy a vegan one.\nFor example toothpaste, shampoo, make-up, glue, washing liquid or something similar.",
"v7_daily.jpg", "Community", True, 7);

INSERT INTO challenge (title, description, image, creator, validated, level_restriction)
VALUES (
"Creative Food",
"Create your own vegan dish, be crazy and creative.\nDon't forget to write down the recipe meanwhile you do it, if it actually turns out alright!",
"v8_creative.jpg", "Community", True, 8);

INSERT INTO challenge (title, description, image, creator, validated, level_restriction)
VALUES (
"Protein Master",
"It is quite common to get the question \"But where do you get your protein from?!\" as a vegan, so this challenge is about collecting some facts.\nFind a few ingredients with a lot of protein and write down in the description how much protein they have per 100g.\n\n(Hint: it is not hard to get your daily protein, you usually don't even have to think about it)",
"v9_protein.jpg", "Community", True, 9);

INSERT INTO challenge (title, description, image, creator, validated, level_restriction)
VALUES (
"Relatively Vego",
"Cook a vegan dish for a parent or a relative.",
"v10_relative.jpg", "Community", True, 10);

INSERT INTO challenge (title, description, image, creator, validated, level_restriction)
VALUES (
"Dessert (No sand)",
"Make a dessert and remember to post the recipe in the description!\n\nInspiration: https://www.silviacooks.org/vegan-chocolate-mousse",
"v10_2_dessert.jpg", "Community", True, 10);

INSERT INTO challenge (title, description, image, creator, validated, level_restriction)
VALUES (
"How I met your Hummus",
"Do a creative hummus, with for examples different spices or vegetables in it.",
"v11_hummus.jpg", "Community", True, 11);

INSERT INTO challenge (title, description, image, creator, validated, level_restriction)
VALUES (
"Store Influencer",
"Ask your local grocery store to take in some vegan product that they don't have in stock. Usually they are more willing that you thing to take in new products!\n\nA good way is to bring a note with the name and/or website of the product so that they can find it.",
"v12_store.jpg", "Community", True, 12);

INSERT INTO challenge (title, description, image, creator, validated, level_restriction)
VALUES (
"Squirrel Milk",
"Make milk out of for example nuts, beans or oats. It is much easier than it sounds and it is a lot cheaper than buying it.",
"v13_nutmilk.jpg", "Community", True, 13);

INSERT INTO challenge (title, description, image, creator, validated, level_restriction)
VALUES (
"Cultural Vegoppriation",
"Take a non-vegan cultural dish from your culture/country and veganize it.",
"v13_2_cultural.jpg", "Community", True, 13);

INSERT INTO challenge (title, description, image, creator, validated, level_restriction)
VALUES (
"Restaurant",
"We have to show that there is interest in vegan food out in the restraurants right? So this one is simply about ordering a vegan dish from a restaurant.",
"v14_restaurant.jpg", "Community", True, 14);

# Shoes, Upcycle, ask local food store for vegan product, make tempeh, make vegan milk, make cheese
# spaghetti etc, Find protein source, Make parents or relatives food, Fool somebody that mostly eats meat

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
(2, 1),
(3, 1),
(4, 2),
(5, 1),
(6, 3),
(7, 3),
(8, 1),
(9, 2),
(10, 1),
(11, 2),
(1, 1);
