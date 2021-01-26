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
import java.net.SocketException;

@Slf4j
public class SimpleSocketClientTest {
	public static void main(String... args) throws IOException, InterruptedException {
		SimpleSocketClient socketClient
				= new SimpleSocketClient("127.0.0.1", 9999);
		socketClient.setMessageListener(new SimpleSocketClient.MessageListener() {
			@Override
			public void onMessage(String message) {
				log.info("Rx: {}", message);
			}
		});

		try {
			while (true) {
				Thread.sleep(1000);
				String message = "Test_" + System.currentTimeMillis();
				socketClient.writeLine(message);
				log.info("Tx: {}", message);
			}
		} catch (SocketException e) {

		} finally {
			socketClient.close();
		}
	}
}
