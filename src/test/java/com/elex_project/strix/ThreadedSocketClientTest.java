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
import java.time.LocalDateTime;

@Slf4j
public class ThreadedSocketClientTest {
	public static void main(String... args) throws IOException, InterruptedException {
		QueuedTextSocketClient socketClient
				= new QueuedTextSocketClient("127.0.0.1", 9999);
		socketClient.setMessageListener(new QueuedTextSocketClient.MessageListener() {
			@Override
			public void onMessage(String message) {
				log.info("Rx: {}", message);
			}
		});

		while (true) {
			Thread.sleep(1000);
			String message = "Test_" + LocalDateTime.now().toString();
			socketClient.writeLine(message);
			log.info("Tx: {}", message);
		}
	}
}
