package com.neobns.atm.client.model;

import java.io.Serializable;

/**
 * 
 * @author Pavel Gaiduk
 * Class for representing raw ISO8583 message
 */
public class ISO8583TCPMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private String rawMessage;

	/**
	 * Constructor for ISO8583TCPMessage setting up the raw ISO8583 message
	 * @param raw
	 */
	public ISO8583TCPMessage(String raw) {
		this.rawMessage = raw;
	}
	
	@Override
	public String toString() {
		return rawMessage.concat("\r\n");
	}
	
}
