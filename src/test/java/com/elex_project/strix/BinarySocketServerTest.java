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
import java.net.Socket;

@Slf4j
public class BinarySocketServerTest {
	public static void main(String... args) throws IOException {
		BinarySocketServer<BinarySocketClient> socketServer
				= new BinarySocketServer<BinarySocketClient>(new BinarySocketServer.MessageListener<BinarySocketClient>() {
			@Override
			public void onMessage(final BinarySocketServer<BinarySocketClient> server, final BinarySocketClient client,
			                      final byte[] message) {
				//L.i("Rx", ByteUtils.byteArrayToHex(message));
				log.info("Rx: {}", new String(message));
				try {
					byte[] resp = message;
					client.write(resp);
					log.info("Tx: {}", Bytez.toHex(resp));
				} catch (IOException e) {
					log.error("Something's wrong..", e);
				}
			}
		}) {
			@Override
			protected BinarySocketClient buildSocketClient(final Socket soc) throws IOException {
				return new BinarySocketClient(soc);
			}
		};

		socketServer.open(9999);

	}

}
