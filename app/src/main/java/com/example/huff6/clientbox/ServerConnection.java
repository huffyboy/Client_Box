package com.example.huff6.clientbox;

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
        return "";
    }

    public void writeClient(String id){

    }

    public ArrayList<String> retrieveLog(String id){
        ArrayList<String> temp = new ArrayList<String>();

        return temp;
    }

    public ArrayList<String> retrieveClient(){
        ArrayList<String> temp = new ArrayList<String>();

        return temp;
    }

    public boolean haveInternetConnection(){
        return true;
    }
}
