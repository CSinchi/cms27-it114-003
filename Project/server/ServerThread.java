package Project.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Time;
import java.util.logging.Level;
import java.util.logging.Logger;

import Project.client.views.LetterGridPanel;
import Project.common.Constants;
import Project.common.LetterStatPayload;
import Project.common.Payload;
import Project.common.PayloadType;
import Project.common.Phase;
import Project.common.RankedPlayersPayload;
import Project.common.RoomResultPayload;
import Project.common.TextFX;
import Project.common.TextFX.Color;

/**
 * A server-side representation of a single client
 */
public class ServerThread extends Thread {
    private Socket client;
    private String clientName;
    private boolean isRunning = false;
    private ObjectOutputStream out;// exposed here for send()
    // private Server server;// ref to our server so we can call methods on it
    // more easily
    private Room currentRoom;
    private static Logger logger = Logger.getLogger(ServerThread.class.getName());
    private long myClientId;

    public void setClientId(long id) {
        myClientId = id;
    }

    public long getClientId() {
        return myClientId;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public ServerThread(Socket myClient, Room room) {
        logger.info(TextFX.colorize("ServerThread created", Color.YELLOW));
        // get communication channels to single client
        this.client = myClient;
        this.currentRoom = room;

    }

    protected void setClientName(String name) {
        if (name == null || name.isBlank()) {
            logger.warning(TextFX.colorize("Invalid name being set", Color.RED));
            return;
        }
        clientName = name;
    }

    public String getClientName() {
        return clientName;
    }

    protected synchronized Room getCurrentRoom() {
        return currentRoom;
    }

    protected synchronized void setCurrentRoom(Room room) {
        if (room != null) {
            currentRoom = room;
        } else {
            logger.info(TextFX.colorize("Passed in room was null, this shouldn't happen", Color.RED));
        }
    }

    public void disconnect() {
        sendConnectionStatus(myClientId, getClientName(), false);
        logger.info(TextFX.colorize("Thread being disconnected by server", Color.GREEN));
        isRunning = false;
        cleanup();
    }

    // send methods

    public boolean sendPhaseSync(Phase phase) { //Added from Ready Check    Cristian Sinchi cms27
        Payload p = new Payload();
        p.setPayloadType(PayloadType.PHASE);
        p.setMessage(phase.name());
        return send(p);
    }

    public boolean sendReadyStatus(long clientId) { //Added from Ready Check    Cristian Sinchi cms27
        Payload p = new Payload();
        p.setPayloadType(PayloadType.READY);
        p.setClientId(clientId);
        return send(p);
    }
    public boolean sendCurrentTurn(long clientId) { //Added from Duengon Project    Cristian Sinchi cms27
        Payload p = new Payload();
        p.setPayloadType(PayloadType.TURN);
        p.setClientId(clientId);
        return send(p);
    }

    public boolean sendTimer(String time) {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.TIME);
        p.setMessage(time);
        return send(p);
    }

    public boolean sendBlankWord(String word) {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.BLANK_WORD);
        p.setMessage(word);
        return send(p);
    }

    public boolean sendRoomName(String name) {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.JOIN_ROOM);
        p.setMessage(name);
        return send(p);
    }

    public boolean sendLetterStat(String letter, Boolean isCorrect) {
        LetterStatPayload payload = new LetterStatPayload();
        payload.setStat(isCorrect);
        payload.setMessage(letter);
        return send(payload);
    }

    public boolean sendRankedPlayers(String[] players) {
        RankedPlayersPayload payload = new RankedPlayersPayload();
        payload.setPlayers(players);
        return send(payload);
    }
    public boolean sendRoomsList(String[] rooms, String message) {
        RoomResultPayload payload = new RoomResultPayload();
        payload.setRooms(rooms);
        if (message != null) {
            payload.setMessage(message);
        }
        return send(payload);
    }

    public boolean sendExistingClient(long clientId, String clientName) {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.SYNC_CLIENT);
        p.setClientId(clientId);
        p.setClientName(clientName);
        return send(p);
    }

    public boolean sendResetUserList() {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.RESET_USER_LIST);
        return send(p);
    }

    public boolean sendClientId(long id) {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.CLIENT_ID);
        p.setClientId(id);
        return send(p);
    }

    public boolean sendMessage(long clientId, String message) {
        Payload p = new Payload();
        p.setPayloadType(PayloadType.MESSAGE);
        p.setClientId(clientId);
        p.setMessage(message);
        return send(p);
    }

    public boolean sendConnectionStatus(long clientId, String who, boolean isConnected) {
        Payload p = new Payload();
        p.setPayloadType(isConnected ? PayloadType.CONNECT : PayloadType.DISCONNECT);
        p.setClientId(clientId);
        p.setClientName(who);
        p.setMessage(String.format("%s the room %s", (isConnected ? "Joined" : "Left"), currentRoom.getName()));
        return send(p);
    }

    private boolean send(Payload payload) {
        try {
            logger.log(Level.FINE, "Outgoing payload: " + payload);
            out.writeObject(payload);
            logger.log(Level.INFO, TextFX.colorize("Sent payload: ",Color.GREEN) + payload);
            return true;
        } catch (IOException e) {
            logger.info("Error sending message to client (most likely disconnected)");
            // uncomment this to inspect the stack trace
            // e.printStackTrace();
            cleanup();
            return false;
        } catch (NullPointerException ne) {
            logger.info("Message was attempted to be sent before outbound stream was opened: " + payload);
            // uncomment this to inspect the stack trace
            // e.printStackTrace();
            return true;// true since it's likely pending being opened
        }
    }

    // end send methods
    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(client.getInputStream());) {
            this.out = out;
            isRunning = true;
            Payload fromClient;
            while (isRunning && // flag to let us easily control the loop
                    (fromClient = (Payload) in.readObject()) != null // reads an object from inputStream (null would
                                                                     // likely mean a disconnect)
            ) {

                logger.info(TextFX.colorize("Received from client: ", Color.BLUE) + fromClient);
                processPayload(fromClient);

            } // close while loop
        } catch (Exception e) {
            // happens when client disconnects
            e.printStackTrace();
            logger.info(TextFX.colorize("Client disconnected",Color.BLUE));
        } finally {
            isRunning = false;
            logger.info(TextFX.colorize("Exited thread loop. Cleaning up connection",Color.GREEN));
            cleanup();
        }
    }

    void processPayload(Payload p) {
        switch (p.getPayloadType()) {
            case CONNECT:
                setClientName(p.getClientName());
                break;
            case DISCONNECT:
                Room.disconnectClient(this, getCurrentRoom());
                break;
            case MESSAGE:
                if (currentRoom != null) {
                    currentRoom.sendMessage(this, p.getMessage());
                } else {
                    // TODO migrate to lobby
                    logger.log(Level.INFO, "Migrating to lobby on message with null room");
                    Room.joinRoom(Constants.LOBBY, this);
                }
                break;
            case GET_ROOMS:
                Room.getRooms(p.getMessage().trim(), this);
                break;
            case CREATE_ROOM:
                Room.createRoom(p.getMessage().trim(), this);
                break;
            case JOIN_ROOM:
                Room.joinRoom(p.getMessage().trim(), this);
                break;
            case GUESS_LETTER:
                ((GameRoom) currentRoom).handleGuessLetter(p.getMessage().toLowerCase().trim(), this);
                break;
            case GUESS_WORD:
                ((GameRoom) currentRoom).handleGuessWord(p.getMessage().toLowerCase().trim(), this);
                break;
            case SKIP:
                ((GameRoom) currentRoom).handleSkip(this);
                break;
            case READY: //Added from Ready Check    Cristian Sinchi cms27
                try {
                ((GameRoom) currentRoom).setReady(this);
                } catch (Exception e) {
                logger.severe(String.format("There was a problem during readyCheck %s", e.getMessage()));
                e.printStackTrace();
                }
                break;
            default:
                break;

        }

    }

    private void cleanup() {
        logger.info(TextFX.colorize("Thread cleanup() start", Color.GREEN));
        try {
            client.close();
        } catch (IOException e) {
            logger.info(TextFX.colorize("Client already closed",Color.RED));
        }
        logger.info(TextFX.colorize("Thread cleanup() complete",Color.GREEN));
    }
}