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
import java.util.List;

@Slf4j
public class SimpleSocketServerTest {
	public static void main(String... args) throws IOException {
		final SimpleSocketServer socketServer
				= new SimpleSocketServer(new TextSocketServer.MessageListener<SimpleSocketClient>() {
			@Override
			public void onMessage(final TextSocketServer<SimpleSocketClient> server, final SimpleSocketClient client, final String message) {
				log.info("Rx: {}", message);
				String resp = "hello~, " + message;
				List<SimpleSocketClient> clients = server.getClients();
				boolean shouldClearClient = false;
				for (TextSocketClient c : clients) {
					try {
						if (!c.equals(client)) c.writeLine(resp);
					} catch (IOException e) {
						shouldClearClient = true;
					}
				}
				if (shouldClearClient) server.removeDeadClients();
				log.info("Tx: {}", resp);
			}
		});

		socketServer.open(9999);

	}

}
