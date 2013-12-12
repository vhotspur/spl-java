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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.SeekableByteChannel;
import java.util.Map;
import java.util.Random;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import de.erichseifert.gral.data.DataTable;

public class IOAndDrawHandler implements HttpHandler {
	
	private static final String BASE_DIRECTORY = "./";
	private static final String DATA_FILE = "big_file";
	private static final int BYTES_TO_READ = 32 * 4096;

	private File root = null;
	
	public IOAndDrawHandler() {
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
			int seek = doTheSeek();
			
			/* Make sure the compiler does not optimize the I/O out. */
			if ((seek % 1) == 0) {
				if (size > 0) {
					size++;
				}
			}
			
			exchange.sendResponseHeaders(200, 0);
			drawTheGraph(exchange.getResponseBody(), size);
		} catch (Throwable t) {
			throw t;
		} finally {
			exchange.close();
			Monitor.announceHandleStop();
		}
	}
	
	private int doTheSeek() throws IOException {
		File file = new File(root, DATA_FILE).getCanonicalFile();
		
		FileInputStream is = new FileInputStream(file);
		SeekableByteChannel chan = is.getChannel();
		
		Random rnd = new Random();
		
		long filesize = chan.size();
		long seek = rnd.nextLong() % filesize;
		int size = BYTES_TO_READ;
		seek -= size;
		if (seek < 0) {
			seek = 0;
		}
		
		chan.position(seek);
		
		byte data[] = new byte[size];
		
		is.read(data);
		
		is.close();
		
		int sum = 0;
		for (byte b : data) {
			sum += b;
		}
		
		chan.close();
		
		return sum;
	}
	
	private void drawTheGraph(OutputStream os, long size) throws IOException {
		DataTable values = DataToVisualize.getData(size);
		
		Graph graph = new Graph(values);
		graph.render();
		
		graph.writeAsPng(os, 800, 600);
	}
}
