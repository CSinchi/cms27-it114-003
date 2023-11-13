package Project.common ;
//Added guessing and skip payload types     Cristian Sinchi cms27
public enum PayloadType {
    CONNECT, DISCONNECT, MESSAGE, CLIENT_ID, RESET_USER_LIST,
    SYNC_CLIENT, CREATE_ROOM, JOIN_ROOM, GET_ROOMS,
    READY,PHASE,GUESS_LETTER, GUESS_WORD, SKIP
}