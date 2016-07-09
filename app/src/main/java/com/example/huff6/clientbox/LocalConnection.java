package com.example.huff6.clientbox;

import java.util.AbstractQueue;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by joshu on 6/6/2016
 */
public class LocalConnection {
    Queue<Log> logs;
    Queue<Integer> ids;
    ServerConnection serverConnection;


    LocalConnection() {
        logs = new ArrayDeque<Log>();
        ids = new ArrayDeque<Integer>();
        serverConnection = new ServerConnection();
    }


    public void updateQueue(){

    }


    public void syncDatabase(){

    }


    public Queue<Log> getLogs() {
        return logs;
    }


    public Queue<Integer> getIds() {
        return ids;
    }


    public ServerConnection getServerConnection(){
        return serverConnection;
    }

}
