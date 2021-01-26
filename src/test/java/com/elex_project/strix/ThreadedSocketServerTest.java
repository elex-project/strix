/******************************************************************************
 * Project Strix                                                              *
 * Socket Client-Server                                                       *
 *                                                                            *
 * Copyright (c) 2020. Elex. All Rights Reserved.                             *
 * https://www.elex-project.com/                                              *
 ******************************************************************************/

package com.elex_project.strix;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;

@Slf4j
public class ThreadedSocketServerTest {

	public static void main(String... args) throws IOException {
		TextSocketServer<QueuedTextSocketClient> socketserver
				= new TextSocketServer<QueuedTextSocketClient>((server, client, message) -> {
			log.info("Rx: {}", message);

			client.writeLine("Hello-> " + message);

			log.info("Tx: {}", message);
		}) {
			@Override
			protected QueuedTextSocketClient buildSocketClient(final Socket soc) throws IOException {
				return new QueuedTextSocketClient(soc);
			}
		};
		socketserver.open(9999);


	}

}
