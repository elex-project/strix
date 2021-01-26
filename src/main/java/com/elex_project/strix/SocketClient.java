/******************************************************************************
 * Project Strix                                                              *
 * Socket Client-Server                                                       *
 *                                                                            *
 * Copyright (c) 2019. Elex. All Rights Reserved.                             *
 * https://www.elex-project.com/                                              *
 ******************************************************************************/

package com.elex_project.strix;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
@Slf4j
public abstract class SocketClient {

	private final Socket socket;
	private final InputStream inputStream;
	private final OutputStream outputStream;
	private Object userObject;
	private SocketServer<?> serverObject = null;

	public SocketClient(final @NotNull Socket socket) throws IOException {
		this.socket = socket;
		setKeepAlive(true);

		this.inputStream = socket.getInputStream();
		this.outputStream = socket.getOutputStream();

	}

	public SocketClient(final @NotNull String host, final int port) throws IOException {
		this(new Socket(host, port));
	}

	public SocketClient(final @NotNull InetAddress host, final int port) throws IOException {
		this(new Socket(host, port));
	}

	public Object getUserObject() {
		return userObject;
	}

	public void setUserObject(final Object userObject) {
		this.userObject = userObject;
	}

	void setServerObject(SocketServer<?> server) {
		this.serverObject = server;
	}

	public final InputStream getInputStream() {
		return inputStream;
	}

	public final OutputStream getOutputStream() {
		return outputStream;
	}

	protected final void checkInputConnection() throws IOException {
		if (isClosed()) {
			throw new IOException("Socket is closed.");
		}
		if (!isConnected()) {
			throw new IOException("Socket is not connected.");
		}
		if (isInputStreamShutdown()) {
			throw new IOException("Input stream has been shutdown.");
		}
	}

	protected final void checkOutputConnection() throws IOException {
		if (isClosed()) {
			throw new IOException("Socket is closed.");
		}
		if (!isConnected()) {
			throw new IOException("Socket is not connected.");
		}
		if (isOutputStreamShutdown()) {
			throw new IOException("Output stream has been shutdown.");
		}
	}

	protected int read() throws IOException {
		return inputStream.read();
	}

	protected int read(final byte[] buffer) throws IOException {
		return inputStream.read(buffer);
	}

	public synchronized void write(final int v) throws IOException {
		//checkOutputConnection();

		outputStream.write(v);
		outputStream.flush();

		//updateCommTime();
	}

	public synchronized void write(final byte[] v) throws IOException {
		//checkOutputConnection();

		outputStream.write(v);
		outputStream.flush();

		//updateCommTime();
	}

	public synchronized void write(final byte[] v, final int offset, final int len) throws IOException {
		//checkOutputConnection();

		outputStream.write(v, offset, len);
		outputStream.flush();

		//updateCommTime();
	}

	public void close() throws IOException {
		if (null != socket && !socket.isClosed()) {
			socket.close();
			//inputStream.close();
			//outputStream.close();
		}
		if (null != serverObject) {
			((SocketServer<SocketClient>)serverObject).removeClient(this);
		}
	}

	public final boolean isConnected() {
		return null != socket && !socket.isClosed() && socket.isConnected();
	}

	public final boolean isClosed() {
		return null != socket && socket.isClosed();
	}

	public final boolean isInputStreamShutdown() {
		return null != socket && socket.isInputShutdown();
	}

	public final boolean isOutputStreamShutdown() {
		return null != socket && socket.isOutputShutdown();
	}

	public final Socket getSocket() {
		return socket;
	}

	public final InetAddress getIpAddress() {
		return socket.getInetAddress();
	}

	public final int getPort() {
		return socket.getPort();
	}

	public final int getLocalPort() {
		return socket.getLocalPort();
	}

	public final void setKeepAlive(final boolean v) throws SocketException {
		socket.setKeepAlive(v);
	}

	public final void setTimeout(final int ms) throws SocketException {
		socket.setSoTimeout(ms);
	}

	public final void setRxBufferSize(final int s) throws SocketException {
		socket.setReceiveBufferSize(s);
	}

	public final void setTxBufferSize(final int s) throws SocketException {
		socket.setSendBufferSize(s);
	}
}
