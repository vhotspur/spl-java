/*
 * Copyright 2015 Charles University in Prague
 * Copyright 2015 Vojtech Horky
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
package cz.cuni.mff.d3s.spl.demo.libselection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import cz.cuni.mff.d3s.spl.demo.libselection.html.GraphHandler;

/** Main server class.
 */
public class Main {
	public static void main(String[] args) throws IOException {
		String plotterName = System.getProperty("demo.libselection.plotter", "default");
		HttpHandler graphHandler = new GraphHandler(PlotterFactory.getPlotter(plotterName));
		
		InetSocketAddress addr = new InetSocketAddress(8888);
		HttpServer server = HttpServer.create(addr, 100);
		server.createContext("/graph.png", graphHandler);
		Executor executor = new ScheduledThreadPoolExecutor(20);
		server.setExecutor(executor);
		
		server.start();
		
		System.out.println("Listening on http://localhost:8888/. Hit <Enter> to terminate.");
		
		try {
			System.in.read();
		} catch (IOException e) {
		}
		
		System.out.println("Terminating...");
		
		server.stop(0);
	}
}
