<table><tr><td> <em>Assignment: </em> IT114 Hangman Milestone 2</td></tr>
<tr><td> <em>Student: </em> Cristian Sinchi (cms27)</td></tr>
<tr><td> <em>Generated: </em> 11/17/2023 10:40:35 AM</td></tr>
<tr><td> <em>Grading Link: </em> <a rel="noreferrer noopener" href="https://learn.ethereallab.app/homework/IT114-003-F23/it114-hangman-milestone-2/grade/cms27" target="_blank">Grading</a></td></tr></table>
<table><tr><td> <em>Instructions: </em> <p>Implement the features from Milestone2 from the proposal document:&nbsp;<a href="https://docs.google.com/document/d/1QkF94ar-x9LjnYjQfkjgKx7NfhJsailXF10o7GcQAd4/view">https://docs.google.com/document/d/1QkF94ar-x9LjnYjQfkjgKx7NfhJsailXF10o7GcQAd4/view</a></p>
</td></tr></table>
<table><tr><td> <em>Deliverable 1: </em> Payload </td></tr><tr><td><em>Status: </em> <img width="100" height="20" src="https://user-images.githubusercontent.com/54863474/211707773-e6aef7cb-d5b2-4053-bbb1-b09fc609041e.png"></td></tr>
<tr><td><table><tr><td> <em>Sub-Task 1: </em> Payload Screenshots</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-16T21.53.13image.png.webp?alt=media&token=137ab034-f0e6-4e5f-9823-8f75ec13e7c1"/></td></tr>
<tr><td> <em>Caption:</em> <p>Code From Payload Part 1<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-16T21.55.37image.png.webp?alt=media&token=65a9e53a-08ca-4182-8c35-17cafe04c840"/></td></tr>
<tr><td> <em>Caption:</em> <p>Code From Payload Part 2<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-16T22.02.17image.png.webp?alt=media&token=723b4d66-d150-4a31-893c-d7c60fe0281b"/></td></tr>
<tr><td> <em>Caption:</em> <p>Example of Various Server-Side Payloads being Sent or Received <br></p>
</td></tr>
</table></td></tr>
</table></td></tr>
<table><tr><td> <em>Deliverable 2: </em> Game Play Code </td></tr><tr><td><em>Status: </em> <img width="100" height="20" src="https://user-images.githubusercontent.com/54863474/211707773-e6aef7cb-d5b2-4053-bbb1-b09fc609041e.png"></td></tr>
<tr><td><table><tr><td> <em>Sub-Task 1: </em> Show the code related to the word/phrase</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-16T22.10.53image.png.webp?alt=media&token=6eea40c8-9ad0-4d1b-8d2d-f2c82146d3e3"/></td></tr>
<tr><td> <em>Caption:</em> <p>A default list of words is stored in abstract class Constants. <br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-17T01.28.15image.png.webp?alt=media&token=63868ac5-c150-462b-91d2-a06c81576934"/></td></tr>
<tr><td> <em>Caption:</em> <p>An arraylist is generated inside a Hangman object from the Constant wordlist. It&#39;s<br>then shuffled with &quot;shuffleList&quot; function when it first gets instantiated. (so every time<br>a game is started, the word list would be ordered at random)<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-17T01.54.53image.png.webp?alt=media&token=2ffb5617-f106-499a-beb8-0a99dfc43d8f"/></td></tr>
<tr><td> <em>Caption:</em> <p>Part 1 of sending blanks to clients. Hangman method getBlank will return a<br>string of the blanks(from char array blank CurrentWords)<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-17T01.54.12image.png.webp?alt=media&token=6096d97d-9c48-4c25-9702-8d3a3e927efa"/></td></tr>
<tr><td> <em>Caption:</em> <p>Part 2 of sending blanks to clients. In this example:<br>&quot;Gameroom&quot; would use getBlankStr<br>method to add that string into its super&#39;s sendMessage parameter to send to<br>all clients in the room <br></p>
</td></tr>
</table></td></tr>
<tr><td> <em>Sub-Task 2: </em> Show the code related to players guessing</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-17T03.41.23image.png.webp?alt=media&token=d0564cd8-e877-465a-8339-3adb03165413"/></td></tr>
<tr><td> <em>Caption:</em> <p>Commands to process /guessletter or /guessword inside of bool function &quot;processClientCommand&quot; in client<br>class<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-17T03.54.01image.png.webp?alt=media&token=0198113d-5311-4f1d-8e09-64f5b1e1aef9"/></td></tr>
<tr><td> <em>Caption:</em> <p>Part 1 of handling guessing letters. This function acts as a bridge between<br>Gameroom and the hangman game object<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-17T04.00.06image.png.webp?alt=media&token=4b25ce2a-6819-437e-8307-5a477adbbd81"/></td></tr>
<tr><td> <em>Caption:</em> <p>Part 2 of handling guessing letters. These are the function inside of Hangman<br>object that are used in gameroom.<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-17T15.35.48image.png.webp?alt=media&token=007b7288-1b9d-425f-b381-1848bebc2e68"/></td></tr>
<tr><td> <em>Caption:</em> <p>Part 1 of handling guessing words. This is simliar to guessLetters in that<br>it bridges between Gameroom and Hangman object<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-17T15.36.28image.png.webp?alt=media&token=219e1cc1-f456-4a0f-ba82-689762b83f87"/></td></tr>
<tr><td> <em>Caption:</em> <p>Part 2 of handling guessing words. Similar to guessing letters, these are the<br>methods inside of Hangman object that are used in  gameroom<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-17T04.22.49image.png.webp?alt=media&token=d53197c0-cc6d-409d-8c92-ef81b5710cc8"/></td></tr>
<tr><td> <em>Caption:</em> <p>This code in gameroom is from project Dungeon Prep <br></p>
</td></tr>
</table></td></tr>
<tr><td> <em>Sub-Task 3: </em> Show the code related to end of game</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-17T04.54.24image.png.webp?alt=media&token=2ff05335-e41a-40c0-840b-feb4e9084d59"/></td></tr>
<tr><td> <em>Caption:</em> <p>Part 1 of handling all round outcomes. These functions run inside both guess<br>handling functions and check for if the game has finished (all rounds completed)<br>and if either boolean isBlankCompleted (round win) or isHangmanCompleted (round lost) is true<br>and can go to the next round.  <br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-17T05.29.28image.png.webp?alt=media&token=bc893592-4465-489d-8a02-d6008113593d"/></td></tr>
<tr><td> <em>Caption:</em> <p>Part 2 of handling all round outcomes. These are booleans inside of Hangman<br>class that are used in Gameroom<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-17T05.04.06image.png.webp?alt=media&token=62798dc8-a0f0-4e73-98e4-6bb412900176"/></td></tr>
<tr><td> <em>Caption:</em> <p>Part 3 of handling all round outcomes. Getters for the booleans<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-17T05.21.03image.png.webp?alt=media&token=462034b8-87a3-4dd0-8951-c963bbf4f330"/></td></tr>
<tr><td> <em>Caption:</em> <p>Function to broadcast scores of players in &quot;Gameroom&quot; <br></p>
</td></tr>
</table></td></tr>
<tr><td> <em>Sub-Task 4: </em> Explain the Game flow code at a high level</td></tr>
<tr><td> <em>Response:</em> <p>To start the game, a client has to mark themselves ready, and if<br>they do, the ready timer begins. Every time a client marks themselves as<br>ready, the server will go through every client in the room and let<br>them know that this certain client has marked themselves as ready (syncing ready<br>status). The hangman game will start up (go the phase in progress )<br>when everyone in the game room is ready or when enough clients (MIN<br>2 players) are ready when the ready timer is up (30 seconds). If<br>none of those conditions are met,&nbsp; the server will announce that it cannot<br>begin and reset the current session (which will go back to waiting for<br>a client to be ready ).<div><br><div>&nbsp;During start-up, clients who were ready will be<br>put on a player turn-order list, the hangman game will be created and<br>started, and the server will announce that the game has been started and<br>follow up with the current round and the blank word to be guessed.<br>Inside the hangman game during start-up, a list of words is generated from<br>a premade list of words and is shuffled and the first round is<br>set up by getting the first word from that shuffled list. After startup,<br>the server will go to the turn phase.</div></div><div><br></div><div>During each turn phase, the next<br>player in the turn-order list is selected as the current player who can<br>do actions in the game (if the game starts, it will choose the<br>first player in the list). Every time a player has been selected for<br>the turn, the server will go through every client in the room (regardless<br>if they were marked ready or not) and let them know that it&#39;s<br>that player&#39;s turn (syncing player turn). Other players are not able to do<br>any actions related to the game and will get a rejection message from<br>the server if they try to do something.&nbsp;</div><div><br></div><div>During a current player&#39;s turn, the<br>current turn player can guess a letter or word, or skip their turn.<br>If they choose to guess, the server will process that guess and determine<br>if it is correct or not. If the guess was not a proper<br>one (ex: sent a number or special character in it), the server will<br>reject that guess and let the client know that their guess was invalid.<br>The server will announce the letter or word that the player guessed and<br>if the current player&#39;s guess is right, the server will announce that the<br>player got their guess right, and score them based on how many blanks<br>they completed (if they guess a word, they get points based on how<br>many empty blanks they completed), show the new blank word that was filled<br>in, and choose the next player in the turn-order list. If the player&#39;s<br>guess is wrong, the server will announce that the player got their guess<br>wrong, set a strike, display the number of strikes in the current round,<br>redisplay the blank word, and choose the next player in the turn-order list.<br>If the player decides to skip their turn, the server will announce that<br>the player skipped their turn and choose the next player in the turn-order<br>list. If the player takes too long to guess or skip, the server<br>will auto-skip them and announce that the player got auto-skipped and choose the<br>next player in the turn-order list.</div><div><br></div><div>Each time a player successfully guesses are word<br>(right or wrong) the server will check if either the blanks have been<br>completed, or if the maximum amount of strikes has been reached, or if<br>a player hit the max score to win and broadcasts the outcome of<br>the round/game corresponding to whatever condition has been met. If none of those<br>conditions are met, the server will proceed as normal and choose the next<br>player in the turn-order list (this loop will continue until one of the<br>conditions has been met). If the blank word was completed or if the<br>hangman is completed (max strikes), the server will announce that either the blank<br>word has been completed or the hangman has been completed,&nbsp; send out the<br>solution, and send out the current rankings of players based on their score.&nbsp;<br>After that, the server checks if the game has been completed, and if<br>it has, the server determines a winner (player with the most points), announces<br>the winner with their final score, and resets the current session (end of<br>game flow). Otherwise, the server will set up the next round (clearing strikes,<br>selecting the next word in the shuffled list, announcing the round and the<br>blank word) and choose the next player in the turn-order list.&nbsp; If a<br>player has hit the maximum score to win (right after guessing right), the<br>server will announce the player winning the game and show their final score,<br>then the server will reset the current session (end of game flow).</div><div><br></div><br></p><br></td></tr>
</table></td></tr>
<table><tr><td> <em>Deliverable 3: </em> Game Evidence </td></tr><tr><td><em>Status: </em> <img width="100" height="20" src="https://user-images.githubusercontent.com/54863474/211707773-e6aef7cb-d5b2-4053-bbb1-b09fc609041e.png"></td></tr>
<tr><td><table><tr><td> <em>Sub-Task 1: </em> Show screenshots of the terminal output of a working game flow</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-17T05.42.09image.png.webp?alt=media&token=cf6fda8f-f17a-453e-8d0c-e025d891c9ae"/></td></tr>
<tr><td> <em>Caption:</em> <p>Screenshot of client Bob trying to guess the letter h and got it<br>right and got 1 point. After that the server sent back the new<br>blank with h being filled in .<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-17T05.46.45image.png.webp?alt=media&token=a2daba71-60cf-4259-834d-e7ccc5477cdc"/></td></tr>
<tr><td> <em>Caption:</em> <p>Screenshot of client Tom trying to guess after Bob with the word &quot;homework&quot;<br>and got it correct a earned 7 points. Blank word gets completed and<br>triggers follow-up functions that display the word of that round and rankings of<br>players, and then onto the next round<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-17T05.55.31image.png.webp?alt=media&token=4adffac2-bc73-49e6-bb5b-f50d2abfa06c"/></td></tr>
<tr><td> <em>Caption:</em> <p>Screenshot of client Tom trying to guess the word on the final strike<br>and fails.  Game Recognized the Max amount of strikes and  hangman<br>completed  is set to true, triggering follow-up functions that display the word<br>of that round and ranking of players and then on to the next<br>round<br></p>
</td></tr>
</table></td></tr>
</table></td></tr>
<table><tr><td> <em>Deliverable 4: </em> Misc </td></tr><tr><td><em>Status: </em> <img width="100" height="20" src="https://user-images.githubusercontent.com/54863474/211707773-e6aef7cb-d5b2-4053-bbb1-b09fc609041e.png"></td></tr>
<tr><td><table><tr><td> <em>Sub-Task 1: </em> Include the pull request for Milestone2 to main</td></tr>
<tr><td> <a rel="noreferrer noopener" target="_blank" href="https://github.com/CSinchi/cms27-it114-003/pull/8">https://github.com/CSinchi/cms27-it114-003/pull/8</a> </td></tr>
</table></td></tr>
<table><tr><td><em>Grading Link: </em><a rel="noreferrer noopener" href="https://learn.ethereallab.app/homework/IT114-003-F23/it114-hangman-milestone-2/grade/cms27" target="_blank">Grading</a></td></tr></table>