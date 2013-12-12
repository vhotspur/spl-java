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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Scanner;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import de.erichseifert.gral.data.DataTable;

public class LoadAndDrawHandler implements HttpHandler {
	
	private static final String BASE_DIRECTORY = "./";
	private static final String DATA_FILE = "numbers.txt";

	private File root = null;
	
	public LoadAndDrawHandler() {
		try {
			root = new File(BASE_DIRECTORY).getCanonicalFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		long size = -1;
		Map<String, String> query = Utilities.parseQuery(exchange.getRequestURI());
		if (query.containsKey("size")) {
			size = Long.parseLong(query.get("size"));
		}
		if (size < 0) {
			size = Long.MAX_VALUE;
		}
		
		Monitor.announceHandleStart();
		
		try {
			exchange.sendResponseHeaders(200, 0);
			loadAndDrawGraph(exchange.getResponseBody(), size);
		} catch (Throwable t) {
			throw t;
		} finally {
			exchange.close();
			Monitor.announceHandleStop();
		}
	}
	
	public void loadAndDrawGraph(OutputStream os, long items) throws IOException {
		File file = new File(root, DATA_FILE).getCanonicalFile();
		
		@SuppressWarnings("unchecked") // varargs and Long.class don't mix well
		DataTable data = new DataTable(Long.class, Long.class);
		long index = 0;
		
		Scanner sc = new Scanner(file);
		
		while (sc.hasNextLong()) {
			long value = sc.nextLong();
			if (index < items) {
				data.add(index, value);
			}
			index++;
		}
		sc.close();
		
		Graph graph = new Graph(data);
		graph.render();
		
		graph.writeAsPng(os, 800, 600); 
	}
}
