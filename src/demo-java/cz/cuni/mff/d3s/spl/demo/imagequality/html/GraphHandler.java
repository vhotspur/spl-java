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
package cz.cuni.mff.d3s.spl.demo.imagequality.html;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import cz.cuni.mff.d3s.spl.core.Formula;
import cz.cuni.mff.d3s.spl.core.Result;
import cz.cuni.mff.d3s.spl.core.impl.SimpleFormulas;
import cz.cuni.mff.d3s.spl.demo.imagequality.DataToVisualize;
import cz.cuni.mff.d3s.spl.demo.imagequality.Graph;
import cz.cuni.mff.d3s.spl.sources.SystemLoad;
import de.erichseifert.gral.data.DataTable;

public class GraphHandler implements HttpHandler {
	private Formula machineIdle;
	private Formula smallLoad;
	
	public GraphHandler() {
		cz.cuni.mff.d3s.spl.core.Data load = SystemLoad.INSTANCE;
		machineIdle = SimpleFormulas.createSmallerThanConst("load", 0.2);
		machineIdle.bind("load", load);
		smallLoad = SimpleFormulas.createSmallerThanConst("load", 0.6);
		smallLoad.bind("load", load);
	}
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		DataTable values = null;
		if (machineIdle.evaluate() == Result.TRUE) {
			values = DataToVisualize.getHourly();
		} else if (smallLoad.evaluate() == Result.TRUE) {
			values = DataToVisualize.getDaily();
		} else {
			values = DataToVisualize.getWeekly();
		}
		
		Graph graph = new Graph(values);
		graph.render();
		
		exchange.sendResponseHeaders(200, 0);
		
		OutputStream outputStream = new BufferedOutputStream(exchange.getResponseBody());
		graph.writeAsPng(outputStream, 800, 600);
		
		exchange.close();
	}

}
