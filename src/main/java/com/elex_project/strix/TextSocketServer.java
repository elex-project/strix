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

/**
 * 텍스트 기반 소켓 서버
 */
@Slf4j
public abstract class TextSocketServer<T extends TextSocketClient> extends SocketServer<T> {

	protected MessageListener<T> listener;

	public TextSocketServer() {
		this(null);
	}

	public TextSocketServer(@Nullable final MessageListener<T> listener) {
		super();

		this.setConnectionListener(new ConnectionListener<T>() {
			@Override
			public void onConnected(final T client) {
				log.info("[{}] New Client accepted: size={}", getServerName(), getClients().size());

				client.setMessageListener(message -> {
					if (null != TextSocketServer.this.listener) {
						TextSocketServer.this.listener.onMessage(TextSocketServer.this, client, message);
					}

				});

			}

			@Override
			public void onException(final Throwable e) {
				log.error("Something's wrong..", e);
			}

		});
		this.setMessageListener(listener);
	}

	public final void setMessageListener(@Nullable final MessageListener<T> listener) {
		this.listener = listener;
	}

	/**
	 * 소켓 클라이언트가 보낸 텍스트 메시지 리스너
	 */
	public interface MessageListener<T extends TextSocketClient> {
		/**
		 * @param client  소켓 클라이언트
		 * @param message 텍스트 메시지
		 */
		public void onMessage(final TextSocketServer<T> server, final T client, final String message);
	}

}
