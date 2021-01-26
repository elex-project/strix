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
import org.jetbrains.annotations.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;

/**
 * 바이너리 통신 소켓
 * 전송 데이터 앞에, 데이터의 길이(byte)가 붙는다.
 */
@Slf4j
public class BinarySocketClient extends SocketClient {

	private final BufferedInputStream inputStream;
	private final BufferedOutputStream outputStream;

	private MessageListener listener;

	public BinarySocketClient(final @NotNull Socket socket) throws IOException {
		super(socket);

		this.inputStream = new BufferedInputStream(socket.getInputStream());
		this.outputStream = new BufferedOutputStream(socket.getOutputStream());

		new Thread(() -> {
			while (isConnected() && !isInputStreamShutdown()) {
				//L.v("waiting?");
				try {
					int len = readSize();
					//L.v("BinarySocketClient", "len = " + len + "");
					if (len < 0) {
						this.close();
					} else if (len == 0) {
						if (listener != null) listener.onMessage(null);
					} else {
						final byte[] data = read(len);
						if (listener != null) listener.onMessage(data);
					}

				} catch (SocketTimeoutException e) {
					log.warn("Socket timeout..", e);
					continue;

				} catch (Throwable e) {
					log.warn("Something's wrong..", e);
					try {
						this.close();
					} catch (IOException ex) {
						log.error("Couldn't close socket..", e);
					}

				}
			}
		}).start();
	}

	public BinarySocketClient(final @NotNull String host, final int port) throws IOException {
		this(new Socket(host, port));
	}

	public BinarySocketClient(final @NotNull InetAddress host, final int port) throws IOException {
		this(new Socket(host, port));
	}

	protected int readSize() throws IOException {
		return inputStream.read();//.readInt();
	}

	protected byte[] read(final int nBytes) throws IOException {
		//checkInputConnection();

		final byte[] data = new byte[nBytes];
		final int nRead = inputStream.read(data, 0, nBytes);
		//updateCommTime();
		if (nBytes != nRead) {
			return Arrays.copyOf(data, nRead);
		} else {
			return data;
		}

	}

	@Override
	public synchronized void write(@NotNull final byte[] bytes) throws IOException {
		//checkOutputConnection();

		outputStream.write(bytes.length);//.writeByte(bytes.length);
		outputStream.write(bytes);
		outputStream.flush();

		//updateCommTime();
	}

	@Override
	public synchronized void write(@NotNull final byte[] bytes, final int offset, final int length) throws IOException {
		//checkOutputConnection();

		outputStream.write(length);
		outputStream.write(bytes, offset, length);
		outputStream.flush();

		//updateCommTime();
	}

	public synchronized void write() throws IOException {
		//checkOutputConnection();

		outputStream.write(0);
		outputStream.flush();

		//updateCommTime();
	}

	public void setMessageListener(final MessageListener listener) {
		this.listener = listener;
	}

	/**
	 *
	 */
	public interface MessageListener {
		/**
		 * @param message 전달 받은 메시지
		 */
		public void onMessage(@Nullable final byte[] message);

	}
}
