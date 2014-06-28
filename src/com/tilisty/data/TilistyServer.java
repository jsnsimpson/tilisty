package com.tilisty.data;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * Initiates the socket listener - listens on port 19266 
 * 
 * Runs in its own thread.
 * 
 * @author Jason Simpson <jsnsimpson@gmail.com>
 * @version 1.0
 */
public class TilistyServer extends Thread implements Runnable {


	private IoAcceptor acceptor;
	private TilistyServerHandler tilistyHandler;

	public TilistyServer(TilistyServerHandler handler) 
	{
		this.tilistyHandler = handler;
	}
	
    public void run() 
    {
        this.startServer();
    }
    
    public void startServer() 
    {
        this.acceptor = (IoAcceptor)new NioSocketAcceptor();
        TextLineCodecFactory codec = new TextLineCodecFactory(Charset.forName("UTF-8"));
        codec.setDecoderMaxLineLength(250000);
        codec.setEncoderMaxLineLength(250000);
        this.acceptor.getFilterChain().addLast("codec", (IoFilter)new ProtocolCodecFilter((ProtocolCodecFactory)codec));
        this.acceptor.setHandler((IoHandler)this.tilistyHandler);
        this.acceptor.getSessionConfig().setMaxReadBufferSize(250000);
        this.acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 15);
        try {
        	this.acceptor.bind((SocketAddress)new InetSocketAddress(19266));
        }
        catch (IOException e) {
        	try {
                Thread.sleep(2000L);
            }
            catch (InterruptedException ex) {}
            this.acceptor.dispose();
            new TilistyServer(this.tilistyHandler).start();
        }
    }
}
