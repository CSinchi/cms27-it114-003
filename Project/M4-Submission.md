<table><tr><td> <em>Assignment: </em> IT114 Hangman Milestone4</td></tr>
<tr><td> <em>Student: </em> Cristian Sinchi (cms27)</td></tr>
<tr><td> <em>Generated: </em> 12/13/2023 8:50:43 PM</td></tr>
<tr><td> <em>Grading Link: </em> <a rel="noreferrer noopener" href="https://learn.ethereallab.app/homework/IT114-003-F23/it114-hangman-milestone4/grade/cms27" target="_blank">Grading</a></td></tr></table>
<table><tr><td> <em>Instructions: </em> <p>Implement the features from Milestone4 from the proposal document:&nbsp;<a href="https://docs.google.com/document/d/1QkF94ar-x9LjnYjQfkjgKx7NfhJsailXF10o7GcQAd4/view">https://docs.google.com/document/d/1QkF94ar-x9LjnYjQfkjgKx7NfhJsailXF10o7GcQAd4/view</a></p>
</td></tr></table>
<table><tr><td> <em>Deliverable 1: </em> Client can mark themselves “away” to be skipped in the turn flow but still be in the game </td></tr><tr><td><em>Status: </em> <img width="100" height="20" src="https://user-images.githubusercontent.com/54863474/211707773-e6aef7cb-d5b2-4053-bbb1-b09fc609041e.png"></td></tr>
<tr><td><table><tr><td> <em>Sub-Task 1: </em> Add screenshot(s) of the visual representation of someone "away"</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-12-13T23.18.04image.png.webp?alt=media&token=8dd2ea09-e757-405a-a982-89ccba8c9909"/></td></tr>
<tr><td> <em>Caption:</em> <p>codesnippet of a handle method in gameroom class the rejects clients who are<br>marked away<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-12-13T23.23.56image.png.webp?alt=media&token=d275dc5b-cf5f-41ea-859c-5e66a0b27bd7"/></td></tr>
<tr><td> <em>Caption:</em> <p>codesnippet of processPayload method in Severthread that determines whether to reject the message<br>from a client based on if their marked away or not.<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-12-13T23.29.19image.png.webp?alt=media&token=28f79a00-4511-4c85-b65c-396d69b47726"/></td></tr>
<tr><td> <em>Caption:</em> <p>codesnippet of handleAway method in Gameroom class that toggles Away status of client<br>and sends back an Away payload<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-12-14T00.02.16image.png.webp?alt=media&token=2645a440-7a0c-468a-9e8f-fd295381226e"/></td></tr>
<tr><td> <em>Caption:</em> <p>Screenshot of a game PT1 where Bob marked themselves as away. While they&#39;re<br>away, Bob couldn&#39;t send any game actions or messages, and his turns were<br>auto skipped<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-12-14T00.03.56image.png.webp?alt=media&token=255ac28d-e471-4529-98a0-c194d6ef0887"/></td></tr>
<tr><td> <em>Caption:</em> <p>Screenshot of a game PT2 where Bob unmarks as away and is able<br>to participate in the game again<br></p>
</td></tr>
</table></td></tr>
<tr><td> <em>Sub-Task 2: </em> Briefly explain the code logic</td></tr>
<tr><td> <em>Response:</em> <p>Clients can mark themselves as ready by selecting the &quot;Mark Away&quot; Button in<br>the game panel. This will send an AWAY payload to the server to<br>handle the AWAY request. The server will reject any clients that are spectating.<br>By default, every player is unmarked as away (isAway = false) so if<br>a client marks themselves away, isAway for that client is set to True.&nbsp;<br>If isAway is already set to True, it will become false. Sever will<br>announce the client marking themselves away or them coming back depending on isAway<br>bool. Ranking will refresh to include (AWAY) tag for whoever marked themselves away.<br>If the client marked themselves away during their turn, they&#39;re automatically skipped. During<br>the nextTurn() method, the server will auto-skip the currentTurnPlayer if they are marked<br>away.<br></p><br></td></tr>
</table></td></tr>
<table><tr><td> <em>Deliverable 2: </em> Client can join as spectator </td></tr><tr><td><em>Status: </em> <img width="100" height="20" src="https://user-images.githubusercontent.com/54863474/211707773-e6aef7cb-d5b2-4053-bbb1-b09fc609041e.png"></td></tr>
<tr><td><table><tr><td> <em>Sub-Task 1: </em> Add screenshot(s) of what a spectator can see</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-12-13T22.12.15image.png.webp?alt=media&token=1c072311-ad01-4168-92f1-542b3b30a6dc"/></td></tr>
<tr><td> <em>Caption:</em> <p>Screenshot of client Jacky joining in gameroom &quot;test&quot; and is automatically spectating Greg<br>and Bob&#39;s game. Jacky can see most game events, but is unable to<br>send any game actions or messages.<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-12-13T22.21.03image.png.webp?alt=media&token=a3bb8ca3-d272-4001-8529-68c39881999e"/></td></tr>
<tr><td> <em>Caption:</em> <p>screenshot of default isSpectating boolean in player class (which is true)<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-12-13T22.43.46image.png.webp?alt=media&token=29689786-a7eb-4baa-8727-59d63f667dcc"/></td></tr>
<tr><td> <em>Caption:</em> <p>code snippet of setReady in Gameroom class that sets isSpectating to false to<br>whoever mark themselves as ready. <br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-12-13T22.54.27image.png.webp?alt=media&token=9cafa687-7542-46a3-9e15-1ca9e094203d"/></td></tr>
<tr><td> <em>Caption:</em> <p>codesnippet of processPayload method in Severthread that determines whether to reject the message<br>from a client based on if their spectating or not.<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-12-13T23.17.33image.png.webp?alt=media&token=0ad0d508-0755-460a-85c4-2c6a71a63315"/></td></tr>
<tr><td> <em>Caption:</em> <p>codesnippets of a handle method in gameroom class demonstrating server rejecting spectator&#39;s actions<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-12-14T01.42.04image.png.webp?alt=media&token=0de93a54-e31a-4ee8-adb4-2b1d22c16420"/></td></tr>
<tr><td> <em>Caption:</em> <p>codesnippets of updating and sync spectatorlist to clients.<br></p>
</td></tr>
</table></td></tr>
<tr><td> <em>Sub-Task 2: </em> Briefly explain how the code handles spectators</td></tr>
<tr><td> <em>Response:</em> <div>By default, everyone is set as a spectator when joining the room ("isSpectating"<br>= true) even outside the Ready Phase. Only when the client marks themselves<br>as ready during READY phase is when they are no longer considered spectating<br>("isSpectating" = false). With this logic, spectators are never included in turnOrder list.&nbsp;<br>When the game initializes, ready players and spectators are separated into turnOrder and<br>spectators "ServerPlayer" lists. Server sends out a string list of all spectators during<br>the initialization process. When spectators try to send game-related requests to the server<br>(guessing, skip, away),&nbsp; the server will reject that request and send a reject<br>msg to that spectator. Same thing occurs when spectators try to send a<br>msg during a game (except during the Ready Phase).&nbsp; <br></div><div><br></div><div>If a client joins<br>the room during a game session, game sync will allow the client to<br>receive all current information about the game (strikes, current round, blank word, etc).<br>They're also automatically set as spectating. Any time a spectator joins the room,&nbsp;<br>the spectators list updates and the server syncs spectator list to all clients<br>in the room.<br></div><br></td></tr>
</table></td></tr>
<table><tr><td> <em>Deliverable 3: </em> As a server option or game configuration, the group may allow a correct match to remove a strike </td></tr><tr><td><em>Status: </em> <img width="100" height="20" src="https://user-images.githubusercontent.com/54863474/211707773-e6aef7cb-d5b2-4053-bbb1-b09fc609041e.png"></td></tr>
<tr><td><table><tr><td> <em>Sub-Task 1: </em> Add screenshot(s) of how to enable/disable this feature</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-12-13T17.48.20image.png.webp?alt=media&token=5bde97e8-0a31-436e-938f-bd11968dfd8c"/></td></tr>
<tr><td> <em>Caption:</em> <p>Pre Game Screen (READY phase) displaying ForgiveOption Toggle Button and ForgiveOption getting enabled<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-12-13T20.59.33image.png.webp?alt=media&token=d8039dd0-aee3-4400-a530-0fefa3c55258"/></td></tr>
<tr><td> <em>Caption:</em> <p>In game screenshot of Bob guessing a right letter and removing one strike<br></p>
</td></tr>
</table></td></tr>
<tr><td> <em>Sub-Task 2: </em> Add a screenshot of the code snippet</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-12-13T22.28.11image.png.webp?alt=media&token=139680d8-f6a6-4507-b421-c455f6e3fca5"/></td></tr>
<tr><td> <em>Caption:</em> <p>screenshot of wantForgiveOp&#39;s default boolean value (false) <br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-12-14T00.36.43image.png.webp?alt=media&token=c6f8503e-1e13-447f-91f3-3efaaffaeecc"/></td></tr>
<tr><td> <em>Caption:</em> <p>codesnippet from handleLetterGuess that shows logic of removing a strikes when forgiveOption is<br>true<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-12-14T01.12.37image.png.webp?alt=media&token=9e0bc5b4-4d35-489d-81af-24579333ccd6"/></td></tr>
<tr><td> <em>Caption:</em> <p>Codesnippets in Gameroom class that handle logic for enabling Forgive Option<br></p>
</td></tr>
</table></td></tr>
<tr><td> <em>Sub-Task 3: </em> Briefly explain the code logic</td></tr>
<tr><td> <em>Response:</em> <p>Forgive Option by default is set to false (forgiveOption = false). It becomes<br><br>true when enough players (entire room if player count &lt; 4 or 4<br>when <br>player count is or is over 4 players ) want Forgive Option<br>(forgiveOption = <br>true). When a client sends a Forgive Option request, the server<br>will set <br>wantForgiveOp to that client to true. Then it will do a<br>check to see if <br>enough players want Forgive Option, and if there is,<br>forgiveOption is set to <br>true.<div><br></div><div>During a game with forgive option enabled , each<br>time a player successfully guesses a letter correctly, a single strike is removed<br>if the amount of strikes is greater than zero. Strike removed is done<br>by a method from HangmanGame Option<br></div><br></p><br></td></tr>
</table></td></tr>
<table><tr><td> <em>Deliverable 4: </em> Include bonus points for certain letters </td></tr><tr><td><em>Status: </em> <img width="100" height="20" src="https://user-images.githubusercontent.com/54863474/211707773-e6aef7cb-d5b2-4053-bbb1-b09fc609041e.png"></td></tr>
<tr><td><table><tr><td> <em>Sub-Task 1: </em> Add screenshot(s) of the effect being visible</td></tr>
<tr><td> <em>Response:</em> <p>Screenshots are suppose to go here so I&#39;ll included those in Sub-Task 2;<br><br></p><br></td></tr>
<tr><td> <em>Sub-Task 2: </em> Add screenshot(s) of related code snippet</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-12-13T21.29.40image.png.webp?alt=media&token=005a306b-e8be-4eab-9206-183fe4ce884c"/></td></tr>
<tr><td> <em>Caption:</em> <p>Constants char Array of Letters that are considered for extra points<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-12-13T21.34.16image.png.webp?alt=media&token=6f2d38d8-6e2d-4d46-b14b-ccbd211400ff"/></td></tr>
<tr><td> <em>Caption:</em> <p>checkExtraPoint method inside GameRoom to announce a player earning extra points if they<br>earned so.<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-12-13T21.39.54image.png.webp?alt=media&token=385832ef-d2b4-406a-9455-c54014e22f4c"/></td></tr>
<tr><td> <em>Caption:</em> <p>modified guessLetterScore method inside Hangman Object that applies a 2x multiplier to the<br>score if the guessed letter is in Extra Point Letter List <br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-12-13T22.07.25image.png.webp?alt=media&token=cf58aeab-5ff6-4862-b5f0-6794d48de246"/></td></tr>
<tr><td> <em>Caption:</em> <p>screenshot of a game where Bob guessed a uncommon letter (Extra Point Letter<br>List letter) and earned x2 points.<br></p>
</td></tr>
</table></td></tr>
<tr><td> <em>Sub-Task 3: </em> Briefly explain the code logic</td></tr>
<tr><td> <em>Response:</em> <p>Extra Points Letter List contains an array of specific chars (uncommon letters found<br>in words). Extra Points are award towards a player only if their guessed<br>letter is found in the Extra Points Letter List, and their rewarded with<br>2x Multiplier to their original score. Server will announce a player earning extra<br>points. <br><br></p><br></td></tr>
</table></td></tr>
<table><tr><td> <em>Deliverable 5: </em> Implement a “hard mode” togglable by the group to hide the “used letters” </td></tr><tr><td><em>Status: </em> <img width="100" height="20" src="https://user-images.githubusercontent.com/54863474/211707773-e6aef7cb-d5b2-4053-bbb1-b09fc609041e.png"></td></tr>
<tr><td><table><tr><td> <em>Sub-Task 1: </em> Add screenshot(s) of how to enable/disable this feature</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-12-13T16.59.17image.png.webp?alt=media&token=77743547-0250-4577-a75e-b4b203bf0699"/></td></tr>
<tr><td> <em>Caption:</em> <p>Pre Game Screen (READY phase) displaying Hardmode Toggle Button and HardMode getting enabled<br></p>
</td></tr>
</table></td></tr>
<tr><td> <em>Sub-Task 2: </em> Add screenshot(s) of hard mode activated</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-12-13T17.03.38image.png.webp?alt=media&token=da9052d5-0dbb-4ca3-a797-cc5c1ae94cee"/></td></tr>
<tr><td> <em>Caption:</em> <p>Screenshot of Hard Mode Game PT1. All types of letter guesses does not<br>change the letter panel.<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-12-13T17.07.37image.png.webp?alt=media&token=cfcb7f74-d862-413e-ab97-7859d28a54e2"/></td></tr>
<tr><td> <em>Caption:</em> <p>Screenshot of Hard Mode PT2.  Dan tried to guess an already correct<br>guess (a) and is rejected. Dan tried to guess an already wrong guess<br>(j) and got a strike.<br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-12-13T22.30.47image.png.webp?alt=media&token=a08ea4d2-7ba5-4357-b684-93d4e599291e"/></td></tr>
<tr><td> <em>Caption:</em> <p>Screenshot of default value of wantHardMode (false) <br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-12-14T01.13.49image.png.webp?alt=media&token=028b25b7-782f-41f2-b686-4beb2552048b"/></td></tr>
<tr><td> <em>Caption:</em> <p>Codesnippets in Gameroom class that handle logic for enabling Hard Mode<br></p>
</td></tr>
</table></td></tr>
<tr><td> <em>Sub-Task 3: </em> Briefly explain the code logic</td></tr>
<tr><td> <em>Response:</em> <p>Hard Mode by default is set to false (isHardMode = false). It becomes<br>true when enough players (entire room if player count &lt; 4 or 4<br>when player count is or is over 4 players ) want HardMode (wantHardMode<br>= true). When a client sends a Hard Mode request, the server will<br>set wantHardMode to that client to true. Then it will do a check<br>to see if enough players want hardMode, and if there is, isHardMode is<br>set to true.<div><br></div><div>During a game with hard mode. handleGuessLetter function will not add<br>wrong guesses in the guessedLetter Character list (so if a player sends an<br>already wrongly guessed letter, they will still get the strike).&nbsp; Also, syncLetterStat will<br>not send payloads to clients during hard mode.</div><br></p><br></td></tr>
</table></td></tr>
<table><tr><td><em>Grading Link: </em><a rel="noreferrer noopener" href="https://learn.ethereallab.app/homework/IT114-003-F23/it114-hangman-milestone4/grade/cms27" target="_blank">Grading</a></td></tr></table>