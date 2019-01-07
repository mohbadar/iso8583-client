package com.neobns.atm.client;

import com.neobns.atm.client.model.ISO8583TCPMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * Class for send ISO8583 messages using tcp protocol
 * @author Pavel Gaiduk
 *
 */
public class ISO8583ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

	private ChannelHandlerContext channelHandlerContext;
	
    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
    	this.channelHandlerContext = channelHandlerContext;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause){
        cause.printStackTrace();
        channelHandlerContext.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	if(msg instanceof ByteBuf) {
    		ByteBuf bb = (ByteBuf)msg;
        	String response = bb.toString(CharsetUtil.UTF_8);
        	System.out.println("Response: " + response);
    	} else {
        	System.out.println("Response: " + msg.toString());
    	}
    }
    
    /**
     * Method sends the String representation of the ISO8583TCPMessage object to the server using TCP protocol
     * @param msg
     */
	public void sendISO8583Message(ISO8583TCPMessage msg) {
    	System.out.println("Request: " + msg.toString());
		channelHandlerContext.writeAndFlush(msg.toString());
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
    	System.out.println("Response ByteBuf: " + msg.toString());
	}

}