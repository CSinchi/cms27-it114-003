<table><tr><td> <em>Assignment: </em> IT114 Hangman Milestone3</td></tr>
<tr><td> <em>Student: </em> Cristian Sinchi (cms27)</td></tr>
<tr><td> <em>Generated: </em> 11/30/2023 9:09:24 PM</td></tr>
<tr><td> <em>Grading Link: </em> <a rel="noreferrer noopener" href="https://learn.ethereallab.app/homework/IT114-003-F23/it114-hangman-milestone3/grade/cms27" target="_blank">Grading</a></td></tr></table>
<table><tr><td> <em>Instructions: </em> <p>Implement the features from Milestone3 from the proposal document:&nbsp;<a href="https://docs.google.com/document/d/1QkF94ar-x9LjnYjQfkjgKx7NfhJsailXF10o7GcQAd4/view">https://docs.google.com/document/d/1QkF94ar-x9LjnYjQfkjgKx7NfhJsailXF10o7GcQAd4/view</a></p>
</td></tr></table>
<table><tr><td> <em>Deliverable 1: </em> Connection Screens </td></tr><tr><td><em>Status: </em> <img width="100" height="20" src="https://user-images.githubusercontent.com/54863474/211707773-e6aef7cb-d5b2-4053-bbb1-b09fc609041e.png"></td></tr>
<tr><td><table><tr><td> <em>Sub-Task 1: </em> Add screenshots showing the screens with the following data</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-30T02.01.43image.png.webp?alt=media&token=aeca1835-5b7f-4191-b9ff-bac78b210883"/></td></tr>
<tr><td> <em>Caption:</em> <p>Java Swing panel When entering host and port info<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-30T02.18.02image.png.webp?alt=media&token=8a0233b2-60be-49ea-8c75-017eeab7ac5d"/></td></tr>
<tr><td> <em>Caption:</em> <p>Java Swing panel when entering a username (in this case entering Tommy as<br>a username)<br></p>
</td></tr>
</table></td></tr>
<tr><td> <em>Sub-Task 2: </em> Briefly explain the code for each step of the process</td></tr>
<tr><td> <em>Response:</em> <div>1. Client UI initializes "Connection" jpanel and "UserInput" jpanel objects each having java<br>swing components to allow user input (JTextfield) and JButtons to set those values<br>when clicked</div><div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; - for Host and Port, the JTextfields of both of them<br>have preset values that allow to join the local server<br></div><div>2. Each object has<br>its own way to validate their inputs and rejects setting the value if<br>it's not valid</div><div>3. After the values have been set and the user clicks<br>"Connect" , ClientUI's method connect() gets the values from each jpanels and invokes<br>the Client's original connect() using values from ClientUI to connect to the server<br></div><br></td></tr>
</table></td></tr>
<table><tr><td> <em>Deliverable 2: </em> Game view </td></tr><tr><td><em>Status: </em> <img width="100" height="20" src="https://user-images.githubusercontent.com/54863474/211707773-e6aef7cb-d5b2-4053-bbb1-b09fc609041e.png"></td></tr>
<tr><td><table><tr><td> <em>Sub-Task 1: </em> Add screenshots showing the related UI</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-30T03.02.49image.png.webp?alt=media&token=16fa6953-9d1f-4f86-9e2a-ee8f34090b70"/></td></tr>
<tr><td> <em>Caption:</em> <p>Full screenshot of a game in round 1 from player Tommy where the<br>players are trying to guess the word liability (answer is not showed to<br>client until round is over). Each player got one letter right (they get<br>points based on how many letters they revealed). Each player also guessed one<br>letter wrong (adding strikes and changes the hangman image). Saber skipped their turn<br>once and Tommy got auto skipped for not guessing in 30 seconds.<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-30T03.16.48image.png.webp?alt=media&token=8c962de4-bdaf-4eb5-b028-bd107ba70d0b"/></td></tr>
<tr><td> <em>Caption:</em> <p>Windowed screenshot of the same game scenario<br></p>
</td></tr>
</table></td></tr>
</table></td></tr>
<table><tr><td> <em>Deliverable 3: </em> Game Logic </td></tr><tr><td><em>Status: </em> <img width="100" height="20" src="https://user-images.githubusercontent.com/54863474/211707773-e6aef7cb-d5b2-4053-bbb1-b09fc609041e.png"></td></tr>
<tr><td><table><tr><td> <em>Sub-Task 1: </em> Show the code snippets for part of the game flow</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-30T03.49.56image.png.webp?alt=media&token=3afc5c14-05c7-4c1a-af41-a8ffdccd7427"/></td></tr>
<tr><td> <em>Caption:</em> <p>It all starts inside HangmanGame object where the currentRound value is set to<br>1. HangmanGame initializes a word list from Constants and shuffles that new List,<br>and invokes setting up a new round.<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-30T04.10.46image.png.webp?alt=media&token=3e6aa444-be4c-41b9-88e2-0a89c2fbd05c"/></td></tr>
<tr><td> <em>Caption:</em> <p>Announce method that sends out the Round and BlankWord<br>also invokes sync methods for<br>Round, Strike, and BlankWord <br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-30T04.34.08image.png.webp?alt=media&token=0f6e70b3-56c0-455a-bb21-9d001a638f7b"/></td></tr>
<tr><td> <em>Caption:</em> <p>Sync methods to send data from Server to all Clients in the room.<br>Type of data include TIME, BLANK_WORD, ROUND, STRIKE, AND LETTER_STAT <br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-30T05.01.48image.png.webp?alt=media&token=2714bcc3-8543-412c-aef9-5130b1f51a0c"/></td></tr>
<tr><td> <em>Caption:</em> <p>Server Thread Methods to make new Payloads types from Sever and send them<br>to corresponding client.<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-30T16.24.00image.png.webp?alt=media&token=f0fa91dd-3ebc-4ff6-bc9a-6c8a407a4dd3"/></td></tr>
<tr><td> <em>Caption:</em> <p>Client methods that handle incoming payload from server corresponding to its type (most<br>cases invoking game panel methods)<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-30T16.42.30image.png.webp?alt=media&token=e8f88ed0-156a-432c-aa79-36105b0662e8"/></td></tr>
<tr><td> <em>Caption:</em> <p>Code snippet for initializing top and center panels for game panel ui which<br>contain information for Rounds, Turns, Strikes, Blank word, hangman, and letters used.<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-30T17.15.27image.png.webp?alt=media&token=c1537f94-c5e2-4af2-bd68-ef66e2cee2d8"/></td></tr>
<tr><td> <em>Caption:</em> <p>Code snippets relating to turns including syncing and shuffling turnOrder on start<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-30T17.38.48image.png.webp?alt=media&token=8eee547f-2ee1-4a59-85cb-0b3bc254332d"/></td></tr>
<tr><td> <em>Caption:</em> <p>Code snippet for initalizing bottom part of game panel which have guessing and<br>skip invokers<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-30T18.46.04image.png.webp?alt=media&token=6043cf23-b490-432d-8893-e7dfe149ad86"/></td></tr>
<tr><td> <em>Caption:</em> <p>Code snippet of Handling Letter guesses. Will also invoke methods to check for<br>next round and/or end game<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-30T19.20.57image.png.webp?alt=media&token=491b29b0-5214-4e1b-b15e-73212f9e10be"/></td></tr>
<tr><td> <em>Caption:</em> <p>Code snippet of Handling Word guesses. Will also invoke methods to check for<br>next round and/or end game. Code snippet for boolean has letter is also<br>included<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-30T19.49.30image.png.webp?alt=media&token=402d3b81-1bc5-4f61-8e07-141194f66fa0"/></td></tr>
<tr><td> <em>Caption:</em> <p>Code snippet of round checking invoked in guess handling methods. Will either end<br>the game if it is completed or continue to a new round<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-30T19.57.06image.png.webp?alt=media&token=27b0324c-4172-4d96-b3e1-9629a9d71459"/></td></tr>
<tr><td> <em>Caption:</em> <p>Code snippets of GameCompleted and PlayerWon checks. GameCompleted check is trigger after 6<br>second delay in round check and playerwon is triggered when a player gets<br>a guess right and gets points.<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-30T20.12.37image.png.webp?alt=media&token=935eae81-282a-4288-a726-b156f7e769db"/></td></tr>
<tr><td> <em>Caption:</em> <p>Code snippet for scoring a player and ranking players. scoring is invoked from<br>guess correctly and ranking is invoked after scoring.<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-30T20.29.03image.png.webp?alt=media&token=08c8b9f1-ef4c-42a9-b0e0-15bf0471345b"/></td></tr>
<tr><td> <em>Caption:</em> <p>code snippets inside hangman game object used in server round  and game<br>end checking<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-11-30T20.30.25image.png.webp?alt=media&token=d66386c3-ace9-4cad-abe5-88a24d5fb661"/></td></tr>
<tr><td> <em>Caption:</em> <p>code snippets inside hangman game object used in server guess and score handling<br><br></p>
</td></tr>
</table></td></tr>
<tr><td> <em>Sub-Task 2: </em> Briefly explain the code flow for each of the steps of the game flow mentioned previously</td></tr>
<tr><td> <em>Response:</em> <p>When Start() is invoked, the server sets up the turn order and shuffles<br>it (for random first). Then, it sets up player data and syncs out<br>player rankings (in this case it will just send everyone on turnOrder and<br>their scores). HangmanGame object is initialized, creates a new word list from the<br>word list constant, and shuffles it. Then word is determined by iterator&#39;s position<br>and makes a char array of that selected word. Sever announces the new<br>round, syncing round, strikes and blank word to clients. NextTurn gets the first<br>player and set them as the current player eligible to make a guess<br>or skip their own turn and set the phase the TURN. A 30-second<br>timed event will auto-skip the current player and go to the next one.<br>Current player must make an action to stop the timed event if they<br>don&#39;t want the server to skip them. During the timed event, TIME data<br>is synced to clients every tick (second).<div><br></div><div>If the current player guesses right, the<br>server will grant that player points (and any actions after scoring a player),<br>fill out the blanks, and sync them to clients. Otherwise, the server will<br>mark up a strike, and sync that strike to the clients. If the<br>current player skips their turn, the server will invoke NexTurn() and proceed to<br>the next player. If the player made a guess, the server will later<br>check after scoring or setting a strike if a player has achieved MAX<br>score, check if the game is completed, and if the game can go<br>to the next round. Game is completed when server finds a player who<br>has reached or exceed MAX score or when the boolean isGameCompleted from HangmanGame<br>object is true (when round # is MAX ROUNDs or higher). Next round<br>can only proceed if isGameCompleted is false and if is, then it will<br>announce the next round and sync round, strike, and bland word to clients.<br>If none of the checks went through, then it will just invoke Nexturn()<br>for the next player to make an action.</div><div><br></div><div>On the Client UI side, components<br>inside gamepanel Jpanel will change based on the type of payload it receives<br>from server (methods get triggered from interface Igameevents).</div><div>-TIME: change the timer label inside<br>toppanel to time number received <br></div><div>-TURN: change the turnStatus to show currentTurn player<br>inside toppanel<br></div><div>-ROUND: change the round label inside toppanel to current round number</div><div>-STRIKE change<br>strike label inside toppanel to current strike count and also changes image of<br>hangmanimage labelicon in centerpanel<br></div><div>-BLANK_WORD change the blankword label inside centerpanel to blanks send<br>from server</div><div>-LETTER_STAT change the grid background to either green or red depending on<br>boolean from payload to corresponding letter</div><div>Client UI can also send guesses or normal<br>skip to the server using the corresponding JButtons</div><div>-Letter: select from a option of<br>letters in a option window to send guessLetter<br></div><div>-Word: type input option window to<br>send guessWord<br></div><div>-Skip: sends out SKIP to server when clicked<br></div><br></p><br></td></tr>
</table></td></tr>
<table><tr><td> <em>Deliverable 4: </em> Misc </td></tr><tr><td><em>Status: </em> <img width="100" height="20" src="https://user-images.githubusercontent.com/54863474/211707773-e6aef7cb-d5b2-4053-bbb1-b09fc609041e.png"></td></tr>
<tr><td><table><tr><td> <em>Sub-Task 1: </em> Pull request from milestone3 to main</td></tr>
<tr><td> <a rel="noreferrer noopener" target="_blank" href="https://github.com/CSinchi/cms27-it114-003/pull/9">https://github.com/CSinchi/cms27-it114-003/pull/9</a> </td></tr>
</table></td></tr>
<table><tr><td><em>Grading Link: </em><a rel="noreferrer noopener" href="https://learn.ethereallab.app/homework/IT114-003-F23/it114-hangman-milestone3/grade/cms27" target="_blank">Grading</a></td></tr></table>