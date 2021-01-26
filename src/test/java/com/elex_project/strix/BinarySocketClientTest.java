/******************************************************************************
 * Project Strix                                                              *
 * Socket Client-Server                                                       *
 *                                                                            *
 * Copyright (c) 2020. Elex. All Rights Reserved.                             *
 * https://www.elex-project.com/                                              *
 ******************************************************************************/

package com.elex_project.strix;

import com.elex_project.abraxas.Bytez;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class BinarySocketClientTest {
	public static void main(String... args) throws IOException, InterruptedException {
		BinarySocketClient socketClient = new BinarySocketClient("127.0.0.1", 9999);
		socketClient.setMessageListener(new BinarySocketClient.MessageListener() {
			@Override
			public void onMessage(byte[] message) {
				if (null == message) {
					log.info("Rx: ~~");
				} else {
					log.info("Rx: {}, {}", new String(message), Bytez.toHex(message));
					//L.i("Rx", );
				}
			}

		});

		while (true) {
			Thread.sleep(1000);
			String message = "test__" + System.currentTimeMillis();
			socketClient.write(message.getBytes());
			log.info("Tx: {}", message);
		}
	}
}
