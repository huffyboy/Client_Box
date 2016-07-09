package com.example.huff6.clientbox;

/**
 *
 */
public class Client {

    private String name;
    private String number;


    Client () {
        name = "";
        number = "";
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
}
