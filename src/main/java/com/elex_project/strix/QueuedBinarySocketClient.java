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
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 바이너리 기반 소켓 클라이언트.
 * 자신만의 쓰기 전용 싱글 스레드를 가지고 있다.
 */
@Slf4j
public class QueuedBinarySocketClient extends BinarySocketClient {

	private final ExecutorService executorService;

	public QueuedBinarySocketClient(final @NotNull Socket socket) throws IOException {
		super(socket);

		this.executorService = Executors.newSingleThreadExecutor();
	}

	public QueuedBinarySocketClient(final @NotNull String host, final int port) throws IOException {
		this(new Socket(host, port));
	}

	public QueuedBinarySocketClient(final @NotNull InetAddress host, final int port) throws IOException {
		this(new Socket(host, port));
	}

	@Override
	public void write(final int v) {

		executorService.submit(() -> {
			try {
				QueuedBinarySocketClient.super.write(v);
			} catch (SocketTimeoutException ignore){

			} catch (IOException e) {
				log.error("Something's wrong..", e);
				try {
					this.close();
				} catch (IOException ignore) {
				}
			}
		});
	}

	@Override
	public void write(final byte[] v) {
		executorService.submit(() -> {
			try {
				QueuedBinarySocketClient.super.write(v);
			} catch (SocketTimeoutException ignore){

			} catch (IOException e) {
				log.error("Something's wrong..", e);
				try {
					this.close();
				} catch (IOException ignore) {
				}
			}
		});
	}

	@Override
	public void write(final byte[] v, final int offset, final int len) {
		executorService.submit(() -> {
			try {
				QueuedBinarySocketClient.super.write(v, offset, len);
			} catch (SocketTimeoutException ignore){

			} catch (IOException e) {
				log.error("Something's wrong..", e);
				try {
					this.close();
				} catch (IOException ignore) {
				}
			}
		});
	}

	@Override
	public synchronized void write() {
		executorService.submit(() -> {
			try {
				QueuedBinarySocketClient.super.write();
			} catch (SocketTimeoutException ignore){

			} catch (IOException e) {
				log.error("Something's wrong..", e);
				try {
					this.close();
				} catch (IOException ignore) {
				}
			}
		});
	}

	@Override
	public void close() throws IOException {

		this.executorService.shutdown();
		super.close();
	}

}
