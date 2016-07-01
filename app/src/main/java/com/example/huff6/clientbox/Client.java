package com.example.huff6.clientbox;

/**
 *
 */
public class Client {

    private String name;
    private String number;
    //private int id;

    Client () {
        name = "";
        number = "";
        //id = 0;
    }

    public Client(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNum() {
        return number;
    }

    public void setNum(String num) {
        this.number = num;
    }

    //public int getId() {
    //    return id;
    //}
}
