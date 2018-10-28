INSERT INTO user (username, description, image, score, created) 
VALUES ("Community", "To an artificial mind all reality is virtual", "community.jpg", 0, 0);

INSERT INTO user_extra (username, password, email) 
VALUES ("Community", "$2a$10$dNvnLK3ZYSyjlwyyAc0Me.FbNvi2yEf6KPKKld62edoC8gChkVGpC", "me@lukas.fyi");

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Tree Climber", 
"Simple, just climb the tallest tree that you can find.",
"1_climbing.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Ice-cream Speed Date",
"Ask a stranger if you can buy them an ice cream and have a friendly ice cream speed date.",
"2_icecream.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Pyramidus Humanoidus",
"Create a human pyramid with as many strangers as you can find (you can use friends too, but find at least one stranger).", 
"3_pyramid.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Face Pimpin'", 
"Paint your face! Pens, glitter, fluorescent or whatever you feel like!", 
"4_facepaint.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Ass kissing", 
"Kiss someones ass in public, obviously someone that you have permission from.",
"5_ass.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"5 o' Clock Social Club", 
"Be in a public place, for example a bar, and when the clock turns full hour you get as many people as possible at your table to scream \"5 (or whatever time it is) o' clock social club\" and everybody joins different tables with other groups of people.",
"6_socialclub.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Hitchhiker", 
"Hitchhike to an unknown destination",
"7_hitchhike.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Banana War", 
"Organize a group of friends that will have a banana fight ongoing for one week. You get one point per kill (pointing the banana in the back of friend), you can only  kill the same person one time each day and you can not kill your killer the same day as that person killed you. Give a price to the one with the most points after a week!",
"8_banana.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Artsy Photographer", 
"Take images of the four following things: 
* Something abandoned
* An animal
* Water
* A dance

Then use some other app (\"Layout\" for example) to merge the images to one so that you can upload it.",
"9_photographer.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Shy Complimenter", 
"Give a compliment to a stranger and then run as fast as possible away from there.",
"10_compliment.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"New Style", 
"Find somebody that has as different of a style in clothes as possible and change outfits with that person, before and after photo is recommended.",
"11_style.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Skinny Dipper", 
"Quite simple if you can find some water source nearby, go skinny dipping!",
"12_skinnydipper.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Wiggly Portrait", 
"Draw a portrait of somebody with your non-dominant hand. (If you are right handed, draw with your left hand, and vice versa.)",
"13_portrait.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"The Trader", 
"Start with something small, a pen for example, and then trade the pen for something else with someone that you meet and then you try to trade the new thing that you got and just continue like this with the people that you meet during 3 hours and see what you end up with.",
"14_trader.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Story Time", 
"Ask somebody that you don't know that well to try to summarize their whole life in under ten minutes. Old people usually love this.",
"15_story.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Yoga Maestro", 
"Try your best to do the Super Soldier yoga pose, googling definitely needed!",
"16_yoga.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Get Lost", 
"Try, by any means necessary, to get as lost as possible.",
"17_lost.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Too Straight Walker", 
"Walk 5000 steps as straight as possible in any direction and see where you end up.",
"18_walker.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Goodish Drink", 
"Do an honest attempt to make a drink that tastes good but has most likely never been made before, and get someone to drink it.",
"19_drink.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Basic Communication", 
"Find someone that speaks a language that you don't know and ask them to teach you some basics.",
"20_communication.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Creatively Nice",
"This is an extremely wide challenge. Say something nice to somebody, but in a creative way.",
"21_creatively_nice.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Mall Freak",
"Bring a friend to the mall and then tell them an excuse to get away from them. Then go to the information desk and say that you have lost a kid and get them to call out your friends name.",
"22_mall.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Catographer",
"Go outside and find a cat, try to get that cat to strike a pose that you can photograph. Harder than it sounds!",
"23_catographer.jpg", "Community", True);

INSERT INTO challenge (title, description, image, creator, validated)
VALUES (
"Dressier",
"Go all in with dressing up for a halloween or masquerade party.",
"24_halloween.jpg", "Community", True);

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
(2, 2),
(3, 2),
(4, 1),
(5, 1),
(6, 3),
(7, 3),
(8, 3),
(9, 2),
(10, 2),
(11, 2),
(12, 1),
(13, 1),
(14, 3),
(15, 2),
(16, 3),
(17, 2),
(18, 1),
(19, 2),
(20, 2),
(21, 1),
(22, 2),
(23, 2);
(24, 1);
