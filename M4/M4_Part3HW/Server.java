package M4.M4_Part3HW;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Server {
    int port = 3001;
    // connected clients
    private List<ServerThread> clients = new ArrayList<ServerThread>();

    private void start(int port) {
        this.port = port;
        // server listening
        try (ServerSocket serverSocket = new ServerSocket(port);) {
            Socket incoming_client = null;
            System.out.println("Server is listening on port " + port);
            do {
                System.out.println("waiting for next client");
                if (incoming_client != null) {
                    System.out.println("Client connected");
                    ServerThread sClient = new ServerThread(incoming_client, this);
                    
                    clients.add(sClient);
                    sClient.start();
                    incoming_client = null;
                    
                }
            } while ((incoming_client = serverSocket.accept()) != null);
        } catch (IOException e) {
            System.err.println("Error accepting connection");
            e.printStackTrace();
        } finally {
            System.out.println("closing server socket");
        }
    }
    protected synchronized void disconnect(ServerThread client) {
		long id = client.getId();
        client.disconnect();
		broadcast("Disconnected", id);
	}
    
    protected synchronized void broadcast(String message, long id) {
        if(processCommand(message, id)){

            return;
        }
        // let's temporarily use the thread id as the client identifier to
        // show in all client's chat. This isn't good practice since it's subject to
        // change as clients connect/disconnect
        message = String.format("User[%d]: %s", id, message);
        // end temp identifier
        
        // loop over clients and send out the message
        Iterator<ServerThread> it = clients.iterator();
        while (it.hasNext()) {
            ServerThread client = it.next();
            boolean wasSuccessful = client.send(message);
            if (!wasSuccessful) {
                System.out.println(String.format("Removing disconnected client[%s] from list", client.getId()));
                it.remove();
                broadcast("Disconnected", id);
            }
        }
    }

    private boolean processCommand(String message, long clientId){
        System.out.println("Checking command: " + message);
        if(message.equalsIgnoreCase("disconnect")){
            Iterator<ServerThread> it = clients.iterator();
            while (it.hasNext()) {
                ServerThread client = it.next();
                if(client.getId() == clientId){
                    it.remove();
                    disconnect(client);
                    
                    break;
                }
            }
            return true;
        }
        //New Command "flipCoin" UCID:cms27 Date: 10/16/2023
        if(message.equals("flipCoin")) {
            Random coinGen = new Random(); //Creating Int Generator
            int coinVal = coinGen.nextInt(2); //Generate Result
            String coinFace; //String Value to either turns heads or tails
            String resultMsg; //String Message to be broadcasted
            if(coinVal == 1){
                coinFace = "heads"; //becomes heads when generator got 1
                resultMsg = String.format("has flipped a coin! They landed on %s", coinFace);//formatting results onto string
                broadcast(resultMsg, clientId);//broadcast that string 
                return true;//boolean result for this class
            }
            else {
                coinFace = "tails";//becomes tails when generator got 0
                resultMsg = String.format("has flipped a coin! They landed on %s", coinFace);//formatting results onto string
                broadcast(resultMsg, clientId);//broadcast that string 
                return true;//boolean result for this class
            }   
        }
        //New Command "rollDie" UCID:cms27 Date: 10/16/2023
        if(message.contains("rollDie")){
            Random dieGen = new Random();
            int diceAmount; //Amount of dices that will be rolled
            int dieFaces;//Amount of faces each die has
            String dieCommand = message.replace("rollDie ", ""); //removes the begining part of the command

            int divider = dieCommand.indexOf("d") -1 ; //int of divider is the position of the "d"
            if (divider >= 0 ) {  //if divider exists in the command
                String d1 = dieCommand.substring(0, (divider +1)); //selects everything before d
                String d2 = dieCommand.substring((divider + 2), dieCommand.length());//selects everything after d
                diceAmount = Integer.parseInt(d1); //converts  to int
                //System.out.println(divider); <<-Used for debugging
                //System.out.println(d1);
                //System.out.println(d2);
                dieFaces = Integer.parseInt(d2);// converst to int
                StringBuilder sb = new StringBuilder(); //creating a StringBuilder to assemble the message

                for(int i = 0; i < diceAmount ; i++) { //Generates and appends results to sb based on "diceAmount"(amount of results) and "dieFaces" (range of results)
                    int r = dieGen.nextInt(dieFaces) + 1;
                    sb.append(r);
                    if (i < diceAmount -1 ) { //seperator for multi result prints
                        sb.append(", ");
                    }
                }

                String resultMsg = String.format("Rolled a %1$s! They got %2$s", dieCommand, sb.toString());//String that assembles the results message 
                broadcast(resultMsg, clientId);
                return true; 
            }

            else {
                diceAmount = 1; //default value
                dieFaces = 6; //default value
                int r = dieGen.nextInt(dieFaces) + 1; //generate value
                //String that assembles the result message alongside the improber input message
                String resultMsg = String.format("Rolled a %1$s! That's an Invalid format. Instead They rolled a 1d6 and got %2$s", dieCommand, r);
                broadcast(resultMsg, clientId);
                return true;
            }
            


        }
        return false;
    }
    public static void main(String[] args) {
        System.out.println("Starting Server");
        Server server = new Server();
        int port = 3000;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            // can ignore, will either be index out of bounds or type mismatch
            // will default to the defined value prior to the try/catch
        }
        server.start(port);
        System.out.println("Server Stopped");
    }
}