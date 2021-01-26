/******************************************************************************
 * Project Strix                                                              *
 * Socket Client-Server                                                       *
 *                                                                            *
 * Copyright (c) 2019. Elex. All Rights Reserved.                             *
 * https://www.elex-project.com/                                              *
 ******************************************************************************/

package com.elex_project.strix;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class SocketServer<T extends SocketClient> {

	private String serverName = "SocketServer";
	private final List<T> clients;
	private ServerSocket socket;
	private ConnectionListener<T> connectionListener;

	public SocketServer() {
		clients = new ArrayList<>();
	}

	public SocketServer(final @Nullable SocketServer.ConnectionListener<T> listener) {
		this();
		this.setConnectionListener(listener);
	}

	public final void setConnectionListener(final @Nullable SocketServer.ConnectionListener<T> listener) {
		this.connectionListener = listener;
	}

	public void setServerName(String name) {
		this.serverName = name;
	}

	public String getServerName() {
		return serverName;
	}

	/**
	 * 소켓 서버 시작
	 *
	 * @param port 포트 번호
	 * @throws IOException 서버 소켓 생성 불가. 포트를 이미 사용 중이라던지..
	 */
	public void open(final int port) throws IOException {
		socket = new ServerSocket(port);
		log.info("[{}] Opening socket server: {}", getServerName(), port);
		while (!socket.isClosed()) {
			try {
				Socket soc = socket.accept();
				final T client = buildSocketClient(soc);
				client.setServerObject(this);

				clients.add(client);
				if (null != connectionListener) {
					connectionListener.onConnected(client);
				}

			} catch (Throwable e) {
				if (null != connectionListener) connectionListener.onException(e);
			}
		}
	}

	protected abstract T buildSocketClient(final Socket soc) throws IOException;

	public synchronized void removeDeadClients() {
		List<T> deadPool = new ArrayList<>();
		for (T client : clients) {
			if (client.isClosed() || client.isInputStreamShutdown() || client.isOutputStreamShutdown()) {
				deadPool.add(client);
			}
		}

		if (deadPool.size() > 0) {
			for (T client : deadPool) {
				clients.remove(client);
			}
			log.trace("[{}] Currently connected clients: {}", getServerName(), clients.size());
		}
	}

	public synchronized void removeClient(T client) {
		clients.remove(client);
		log.trace("[{}] Currently connected clients: {}", getServerName(), clients.size());
	}

	public final List<T> getClients() {
		//removeDeadClients();
		return clients;
	}

	public final ServerSocket getServerSocket() {
		return socket;
	}

	public final void close() {
		if (null != socket && !socket.isClosed()) {
			try {
				socket.close();
			} catch (IOException e) {
				log.warn("Couldn't close socket..", e);
			}
		}
	}

	/**
	 *
	 */
	public interface ConnectionListener<T> {
		/**
		 * 서버에 클라이언트가 접속되면 알려줌.
		 *
		 * @param client
		 */
		public void onConnected(final T client);

		/**
		 * 예외 발생시 알림
		 *
		 * @param e
		 */
		public void onException(final Throwable e);
	}
}
