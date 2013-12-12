/*
 * Copyright 2013 Charles University in Prague
 * Copyright 2013 Vojtech Horky
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cz.cuni.mff.d3s.spl.demo.sandbox.priorities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ClientCounter implements HttpHandler {
	
	private static AtomicInteger clients = new AtomicInteger(0);
	private static long startTime = System.nanoTime();

	private static class Monitor implements Runnable {
		private ThreadPoolExecutor executor;
		
		public Monitor(ThreadPoolExecutor exec) {
			executor = exec;
		}
		
		@Override
		public void run() {
			String resultFilename = System.getProperty("client.counter.filename");
			if (resultFilename == null) {
				return;
			}
		
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(resultFilename);
				
				int clientsBefore = -10;
				long lastTime = -1;
				while (true) {
					int clientsNow = clients.get() + executor.getQueue().size();
					long timeNow = System.nanoTime();
					long timeOffset = (timeNow - startTime) / (1000 * 1000);
					
					if (clientsBefore != clientsNow) {
						if (clientsBefore >= 0) {
							writer.printf("%d %d\n", lastTime, clientsBefore);
						}
						writer.printf("%d %d\n", timeOffset, clientsNow);
						writer.flush();
						clientsBefore = clientsNow;
					}
					lastTime = timeOffset;
					
					/*try {
						Thread.sleep(1, 0);
					} catch (InterruptedException e) {
					}*/
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return;
			} finally {
				if (writer != null) {
					writer.close();
				}
			}
		}

	}

	public ClientCounter(ThreadPoolExecutor executor) {
		Thread monitoringThread = new Thread(new Monitor(executor));
		monitoringThread.setDaemon(true);
		monitoringThread.start();
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		clients.addAndGet(1);
		try {
			GraphHandler.drawGraph(exchange);
		} catch (Throwable t) {
			t.printStackTrace();
			throw t;
		} finally {
			clients.addAndGet(-1);
		}
	}
}
