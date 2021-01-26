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

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

/**
 * 텍스트 기반 소켓 클라이언트
 */
@Slf4j
public class TextSocketClient extends SocketClient {

	private final BufferedReader reader;
	private final BufferedWriter writer;

	private MessageListener listener;

	public TextSocketClient(final @NotNull Socket socket) throws IOException {
		super(socket);

		this.reader = new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));//IOUtils.getReader(getInputStream());
		this.writer = new BufferedWriter(new OutputStreamWriter(getOutputStream(), StandardCharsets.UTF_8));//IOUtils.getWriter(getOutputStream());

		new Thread(() -> {
			while (isConnected() && !isInputStreamShutdown()) {
				//L.v("waiting?");
				try {
					final String line = readLine();
					if (null != line) {
						if (null != listener) {
							//updateCommTime();
							listener.onMessage(line);
						}
					} else {
						this.close();

					}

				} catch (SocketTimeoutException e) {
					log.trace("Socket timeout", e);
					continue;

				} catch (Throwable e) {
					log.info("Something's wrong..", e);
					try {
						this.close();
					} catch (IOException ex) {
						log.error("Couldn't close socket..", ex);
					}
				}
			}

			/*while (isConnected()) {
				try {
					if (listener != null) {
						final String line = readLine();
						if (null != line) {
							//updateCommTime();
							listener.onMessage(line);
						}
					}
				} catch (IOException e) {
					L.w(TAG, e);
				}
			}*/
		}).start();
	}

	public TextSocketClient(final @NotNull String host, final int port) throws IOException {
		this(new Socket(host, port));
	}

	public TextSocketClient(final @NotNull InetAddress host, final int port) throws IOException {
		this(new Socket(host, port));
	}

	public void setMessageListener(final MessageListener listener) {
		this.listener = listener;
	}

	@Nullable
	protected String readLine() throws IOException {
		return reader.readLine();
	}

	public synchronized void writeLine(final @NotNull String line) throws IOException {
		//checkOutputConnection();

		writer.write(line);
		writer.newLine();
		writer.flush();

		//updateCommTime();
	}

	/**
	 *
	 */
	public interface MessageListener {
		/**
		 * @param message 전달 받은 텍스트 메시지
		 */
		public void onMessage(final String message);
	}
}
