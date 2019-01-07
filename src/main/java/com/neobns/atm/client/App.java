package com.neobns.atm.client;

import com.neobns.atm.client.model.ISO8583TCPMessage;

/**
 * Main class of the application to run it using command line or IDE
 * @author Pavel Gaiduk
 *
 */
public class App {
    public static void main( String[] args ) {
    	ISO8583Client client = new ISO8583Client("localhost", 9090);
    	client.sendSingleMessage(new ISO8583TCPMessage("0100420040010000180031314299200551444597122117063460113036888888844C0B72ADC0871D2001010100"));
    	client.closeConnection();
    }
}
