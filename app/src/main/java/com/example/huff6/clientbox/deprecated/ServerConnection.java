package com.example.huff6.clientbox.deprecated;

import java.util.ArrayList;

/**
 * Created by joshu on 6/6/2016
 */
public class ServerConnection {

    private String username;
    private String number;

    ServerConnection () {
        username = "";
        number   = "";
    }

    public String retreiveClient(String id){
        return username + number;
    }

    public void writeClient(String id){
    }

    public ArrayList<String> retrieveLog(String id){
        return new ArrayList<>();
    }

    public ArrayList<String> retrieveClient(){
        return new ArrayList<>();
    }

    public boolean haveInternetConnection(){
        return true;
    }
}
