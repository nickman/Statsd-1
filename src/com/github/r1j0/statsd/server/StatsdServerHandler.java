package com.github.r1j0.statsd.server;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StatsdServerHandler extends IoHandlerAdapter {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final LinkedBlockingQueue<String> linkedBlockingQueue;


	public StatsdServerHandler(LinkedBlockingQueue<String> linkedBlockingQueue) {
		this.linkedBlockingQueue = linkedBlockingQueue;
	}


	@Override
	public void sessionOpened(IoSession session) {
		session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		session.setAttribute("Values: ");
	}


	@Override
	public void messageReceived(IoSession session, Object message) {
		logger.info("Message received in the server..");
		logger.info("Message is: " + message.toString());
		
		try {
			linkedBlockingQueue.put(message.toString());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		session.close(true);
	}


	@Override
	public void sessionIdle(IoSession session, IdleStatus status) {
		logger.info("Disconnecting idle session.");
		session.close(true);
	}


	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		session.close(true);
	}
}
