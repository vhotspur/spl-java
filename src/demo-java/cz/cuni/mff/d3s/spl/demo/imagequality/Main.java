package cz.cuni.mff.d3s.spl.demo.imagequality;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import cz.cuni.mff.d3s.spl.demo.imagequality.html.GraphHandler;

public class Main {
	public static void main(String[] args) throws IOException {
		InetSocketAddress addr = new InetSocketAddress(8888);
		HttpServer server = HttpServer.create(addr, 10);
		server.createContext("/", new GraphHandler());
		server.setExecutor(null);
		
		server.start();
		
		try {
			System.in.read();
		} catch (IOException e) {
		}
		
		server.stop(0);
	}
}
