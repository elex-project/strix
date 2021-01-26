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
import java.net.Socket;

/**
 * 텍스트 기반 소켓 서버
 */
@Slf4j
public class SimpleSocketServer extends TextSocketServer<SimpleSocketClient> {

	public SimpleSocketServer(@Nullable final MessageListener<SimpleSocketClient> listener) {
		super(listener);
	}

	@Override
	protected SimpleSocketClient buildSocketClient(final Socket soc) throws IOException {
		return new SimpleSocketClient(soc);
	}

}
