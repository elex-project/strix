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

/**
 * 텍스트 기반 소켓 클라이언트
 */
@Slf4j
public class SimpleSocketClient extends TextSocketClient {

	public SimpleSocketClient(final @NotNull Socket socket) throws IOException {
		super(socket);
	}

	public SimpleSocketClient(final @NotNull String host, final int port) throws IOException {
		this(new Socket(host, port));
	}

	public SimpleSocketClient(final @NotNull InetAddress host, final int port) throws IOException {
		this(new Socket(host, port));
	}

}
