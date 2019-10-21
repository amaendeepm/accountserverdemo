package test.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;



public class AccountingServer {


	public static void main(String[] args) throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress(8500), 0);
		HttpContext context = server.createContext("/");
		
		AccountingDispatcher acctDsipatcher = new AccountingDispatcher();
		
		context.setHandler(acctDsipatcher::handleRequest);
		server.start();
	}
	
	



}