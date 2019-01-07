package com.neobns.atm.client;

import java.net.InetSocketAddress;

import com.neobns.atm.client.model.ISO8583TCPMessage;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * Class wrapper for the send messages using the TCP protocol
 * @author Pavel Gaiduk
 *
 */
public class ISO8583Client {

	private ChannelFuture channelFuture;
	private final ISO8583ClientHandler clientHandler = new ISO8583ClientHandler();
	private EventLoopGroup group = new NioEventLoopGroup();
	private boolean isConnectionEstablished = false;
	private String host;
	private int port;

	public ISO8583Client(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	/**
	 * Method to initialize the connection using TCP protocol
	 * @return true if connection was established and false otherwise
	 */
	private boolean initConnection() {
    	try {
    	    Bootstrap clientBootstrap = new Bootstrap();
    	    clientBootstrap.group(group);
    	    clientBootstrap.channel(NioSocketChannel.class);
    	    clientBootstrap.remoteAddress(new InetSocketAddress(host, port));
    	    clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
    	        protected void initChannel(SocketChannel socketChannel) throws Exception {
    	            socketChannel.pipeline().addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));
    	            socketChannel.pipeline().addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8));
    	            socketChannel.pipeline().addLast(clientHandler);
    	        }
    	    });
    	    channelFuture = clientBootstrap.connect().sync();
    	    return true;
    	} catch(Exception e) {
    		e.printStackTrace();
    		return false;
    	}
	}
	
	/**
	 * Method to send the ISO8583TCPMessage object through the TCP protocol 
	 * @param iso8583TCPMessage
	 */
	public void sendSingleMessage(ISO8583TCPMessage iso8583TCPMessage) {
		if(!isConnectionEstablished) {
			isConnectionEstablished = initConnection();
		}
		if(isConnectionEstablished)
			clientHandler.sendISO8583Message(iso8583TCPMessage);
	}

	/**
	 * Method to close the established connection
	 */
	public void closeConnection() {
	    if(isConnectionEstablished) {
			try {
				channelFuture.channel().closeFuture().sync();
        	    group.shutdownGracefully().sync();    			
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    }
	}
	
}
