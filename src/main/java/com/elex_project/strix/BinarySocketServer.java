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

@Slf4j
public abstract class BinarySocketServer<T extends BinarySocketClient> extends SocketServer<T> {

	private MessageListener<T> listener;

	public BinarySocketServer(@Nullable final MessageListener<T> listener) {
		super();

		this.setConnectionListener(new ConnectionListener<T>() {
			@Override
			public void onConnected(final T client) {
				log.info("[{}] New Client accepted: size={}", getServerName(), getClients().size());

				client.setMessageListener(new BinarySocketClient.MessageListener() {
					@Override
					public void onMessage(final byte[] message) {

						if (null != BinarySocketServer.this.listener) {
							BinarySocketServer.this.listener.onMessage(BinarySocketServer.this, client, message);
						}

					}

				});

			}

			@Override
			public void onException(final Throwable e) {
				log.warn("Something's wrong", e);
			}
		});
		setMessageListener(listener);
	}

	public final void setMessageListener(@Nullable final MessageListener<T> listener) {
		this.listener = listener;
	}


	/**
	 * 소켓 클라이언트가 보낸 바이너리 메시지 리스너
	 */
	public interface MessageListener<T extends BinarySocketClient> {
		/**
		 * @param client  소켓 클라이언트
		 * @param message 바이너리 메시지
		 */
		public void onMessage(final BinarySocketServer<T> server, final T client, final byte[] message);
	}
}
