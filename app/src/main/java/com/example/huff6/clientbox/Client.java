package com.example.huff6.clientbox;

/**
 * The client object for all data concerning a client
 */
public class Client {

    private String name;
    private String number;

    /**
     * Make sure the strings are not null
     */
    public Client () {
        name = "";
        number = "";
    }

    /**
     * Simple constructor
     *
     * @param name the client's name
     * @param number the client's phone number
     */
    public Client(String name, String number) {
        this.name = name;
        this.number = number;
    }

    /**
     * Getter for name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for phone number
     *
     * @return phone number
     */
    public String getNum() {
        return number;
    }

    /**
     * Setter for phone number
     *
     * @param num the phone number
     */
    public void setNum(String num) {
        this.number = num;
    }
}
