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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class StaticHandler implements HttpHandler {

	private static final String BASE_DIRECTORY = "./";

	private File root = null;

	public StaticHandler() {
		try {
			root = new File(BASE_DIRECTORY).getCanonicalFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		Monitor.announceHandleStart();
		try {
			handleInternal(exchange);
		} catch (Throwable t) {
			t.printStackTrace();
			throw t;
		} finally {
			Monitor.announceHandleStop();
		}
	}

	private void handleInternal(HttpExchange exchange) throws IOException {
		String path = getRelativePath(exchange);
		File file = new File(root, path).getCanonicalFile();
		
		OutputStream os = exchange.getResponseBody();
		FileInputStream fs;
		try {
			fs = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			String response = "404 (Not found)";
			exchange.sendResponseHeaders(400, response.length());
			os.write(response.getBytes());
			os.close();
			throw e;
		}

		exchange.sendResponseHeaders(200, 0);
		
		final byte[] buffer = new byte[4096];
		int count = 0;
		while ((count = fs.read(buffer)) >= 0) {
			os.write(buffer, 0, count);
		}
		fs.close();
		os.close();
	}

	private String getRelativePath(HttpExchange exchange) {
		URI uri = exchange.getRequestURI();
		String fullPath = uri.getPath();
		String baseDir = exchange.getHttpContext().getPath();
		return fullPath.substring(baseDir.length());
	}
}
