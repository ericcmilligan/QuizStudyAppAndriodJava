**Mobile Development 2022/23 Portfolio**
# API description

Student ID: `C21040245`

I have decided to use a navigation drawer to separate the three separate sections of my application:
the question manager, quiz game and high score sections. Using a navigation drawer made sense to
me for the navigation as it divides the three sections and allows easy switching between these 
sections within the app. Using a navigation drawer also allows the menu to be hidden and activated
by a hamburger menu to not let the navigation section get in the way of the quiz gameplay.

The app will make use of database features as it is designed to save questions and their answers, high scores and 
tags. The high score will be saved when achieved to allow the student to continue playing and improving their high scores 
for a given tag. The database will be separated into quiz question, quiz answers, quiz tag and quiz high score tables 
to save and store the required data for the app. I will be using Room to interact and save to the database as it is 
Google's default persistence library for Android and therefore has abundant documentation.

To send alerts to the users on their high scores, quiz set creation save and end of game
quiz score I will be using toasts as it is a standard convention in android. I have used 
notifications for this feature as well, with toasts as the back-up alert option, if notifications
are not enabled on their device.

To share the student's high scores I will use intents to allow the student to send an email 
containing the high score. I decided to use intents as they are built into android and there
is ample documentation. I could have also used JavaMail API using Gmail authentication, 
but I feel this would be overkill for the required purpose.
 