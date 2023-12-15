<table><tr><td> <em>Assignment: </em> It114 Milestone1</td></tr>
<tr><td> <em>Student: </em> Cristian Sinchi (cms27)</td></tr>
<tr><td> <em>Generated: </em> 10/23/2023 11:52:39 PM</td></tr>
<tr><td> <em>Grading Link: </em> <a rel="noreferrer noopener" href="https://learn.ethereallab.app/homework/IT114-003-F23/it114-milestone1/grade/cms27" target="_blank">Grading</a></td></tr></table>
<table><tr><td> <em>Instructions: </em> <ol><li>Create a new branch called Milestone1</li><li>At the root of your repository create a folder called Project if one doesn't exist yet</li><ol><li>You will be updating this folder with new code as you do milestones</li><li>You won't be creating separate folders for milestones; milestones are just branches</li></ol><li>Create a milestone1.md file inside the Project folder</li><li>Git add/commit/push it to Github (yes it'll be blank for now)</li><li>Create a pull request from Milestone1 to main (don't complete/merge it yet, just have it in open status)</li><li>Copy in the latest Socket sample code from the most recent Socket Part example of the lessons</li><ol><li>Recommended Part 5 (clients should be having names at this point and not ids)</li><li><a href="https://github.com/MattToegel/IT114/tree/Module5/Module5">https://github.com/MattToegel/IT114/tree/Module5/Module5</a>&nbsp;<br></li></ol><li>Fix the package references at the top of each file (these are the only edits you should do at this point)</li><li>Git add/commit the baseline</li><li>Ensure the sample is working and fill in the below deliverables</li><ol><li>Note: The client commands likely are different in part 5 with the /name and /connect options instead of just connect</li></ol><li>Get the markdown content or the file and paste it into the milestone1.md file or replace the file with the downloaded version</li><li>Git add/commit/push all changes</li><li>Complete the pull request merge from step 5</li><li>Locally checkout main</li><li>git pull origin main</li></ol></td></tr></table>
<table><tr><td> <em>Deliverable 1: </em> Startup </td></tr><tr><td><em>Status: </em> <img width="100" height="20" src="https://user-images.githubusercontent.com/54863474/211707773-e6aef7cb-d5b2-4053-bbb1-b09fc609041e.png"></td></tr>
<tr><td><table><tr><td> <em>Sub-Task 1: </em> Add screenshot showing your server being started and running</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-10-24T01.10.39image.png.webp?alt=media&token=cd018feb-6608-4539-b768-99fec5024d8b"/></td></tr>
<tr><td> <em>Caption:</em> <p>Server Running and waiting for clients to join via ports<br></p>
</td></tr>
</table></td></tr>
<tr><td> <em>Sub-Task 2: </em> Add screenshot showing your client being started and running</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-10-24T01.15.05image.png.webp?alt=media&token=b3f0f319-7969-4d18-b792-370c20f4c5bf"/></td></tr>
<tr><td> <em>Caption:</em> <p>Client waiting for input and then setting Name and Connecting to Server via<br>commands<br></p>
</td></tr>
</table></td></tr>
<tr><td> <em>Sub-Task 3: </em> Briefly explain the connection process</td></tr>
<tr><td> <em>Response:</em> <p>The &quot;Server&quot; creates a ServerSocket obj inside the method &quot;start&quot; to be able<br>to listen for connections on a specific port. When the code first initializes,<br>the Socket obj is false. On the &quot;Client&quot; side,&nbsp; when the user puts<br>the command to connect to a port (/connect localhost:300) it creates a Socket<br>obj that takes in the port from the command and attempts to connect<br>to the ServerSocket from &quot;Server&quot;. If it successfully connects, the &quot;server&quot; will make<br>a thread for that client and wait for its next input.<br></p><br></td></tr>
</table></td></tr>
<table><tr><td> <em>Deliverable 2: </em> Sending/Receiving </td></tr><tr><td><em>Status: </em> <img width="100" height="20" src="https://user-images.githubusercontent.com/54863474/211707773-e6aef7cb-d5b2-4053-bbb1-b09fc609041e.png"></td></tr>
<tr><td><table><tr><td> <em>Sub-Task 1: </em> Add screenshot(s) showing evidence related to the checklist</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-10-24T03.21.41image.png.webp?alt=media&token=e5fad12a-3d66-43d7-b120-991851e257ef"/></td></tr>
<tr><td> <em>Caption:</em> <p>two clients connected and sending messages<br></p>
</td></tr>
</table></td></tr>
<tr><td> <em>Sub-Task 2: </em> Briefly explain how the messages are sent, broadcasted (sent to all connected clients), and received</td></tr>
<tr><td> <em>Response:</em> <p>Both the client and the server use the payloads to send objects (in<br>this case messages) to each other. &quot;Client&quot; first sends out a payload containing<br>a string with a message and it&#39;s received by &quot;ServerThreads&quot; which then has<br>to determine what type of payload was it (if it was as msg<br>or not). Then it illiterates through every client that is currently in the<br>sender&#39;s room and sends out a payload containing the message to each client<br>and then the payload in each client is processed and printed out in<br>their screens.<br></p><br></td></tr>
</table></td></tr>
<table><tr><td> <em>Deliverable 3: </em> Disconnecting / Terminating </td></tr><tr><td><em>Status: </em> <img width="100" height="20" src="https://user-images.githubusercontent.com/54863474/211707773-e6aef7cb-d5b2-4053-bbb1-b09fc609041e.png"></td></tr>
<tr><td><table><tr><td> <em>Sub-Task 1: </em> Add screenshot(s) showing evidence related to the checklist</td></tr>
<tr><td><table><tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-10-24T03.37.18image.png.webp?alt=media&token=99bb3145-0d0e-402e-ac34-96632f594ba7"/></td></tr>
<tr><td> <em>Caption:</em> <p>User1 disconnected from the server. The server and other client got notified about<br>the user&#39;s disconnection and did not terminated <br></p>
</td></tr>
<tr><td><img width="768px" src="https://firebasestorage.googleapis.com/v0/b/learn-e1de9.appspot.com/o/assignments%2Fcms27%2F2023-10-24T03.40.25image.png.webp?alt=media&token=cccfa641-76b2-4147-b472-1f2bcec63088"/></td></tr>
<tr><td> <em>Caption:</em> <p>Server got terminated and the clients got disconnected from the server, but they<br>didn&#39;t get terminated<br></p>
</td></tr>
</table></td></tr>
<tr><td> <em>Sub-Task 2: </em> Briefly explain how the various disconnects/terminations are handled</td></tr>
<tr><td> <em>Response:</em> <p>When a client is disconnected, the &quot;ServerThread&quot; has it handled (it catches an<br>exception and then sets an isRunning boolean to false to let the server<br>know the client&#39;s thread is no longer running and needs to be cleaned<br>up, this way the Server won&#39;t have to recognize the disconnected client from<br>its list and can continue ). Something similar happens vice versa when the<br>server gets disconnected. An exception gets caught and its handled property.&nbsp;<br></p><br></td></tr>
</table></td></tr>
<table><tr><td> <em>Deliverable 4: </em> Misc </td></tr><tr><td><em>Status: </em> <img width="100" height="20" src="https://user-images.githubusercontent.com/54863474/211707773-e6aef7cb-d5b2-4053-bbb1-b09fc609041e.png"></td></tr>
<tr><td><table><tr><td> <em>Sub-Task 1: </em> Add the pull request for this branch</td></tr>
<tr><td> <a rel="noreferrer noopener" target="_blank" href="https://github.com/CSinchi/cms27-it114-003/pull/7">https://github.com/CSinchi/cms27-it114-003/pull/7</a> </td></tr>
<tr><td> <em>Sub-Task 2: </em> Talk about any issues or learnings during this assignment</td></tr>
<tr><td> <em>Response:</em> <p>It was a bit difficult trying to keep track of what was going<br>on, Especially when it came to Sockets. (Also because this project is a<br>bit big compared to my previous experiences)<br></p><br></td></tr>
</table></td></tr>
<table><tr><td><em>Grading Link: </em><a rel="noreferrer noopener" href="https://learn.ethereallab.app/homework/IT114-003-F23/it114-milestone1/grade/cms27" target="_blank">Grading</a></td></tr></table>