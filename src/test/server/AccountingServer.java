package test.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;


public class AccountingServer {


	public static void main(String[] args) throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress(8500), 0);
		HttpContext context = server.createContext("/");
		context.setHandler(AccountingServer::handleRequest);
		server.start();
	}

	private static void handleRequest(HttpExchange exchange) throws IOException {
		System.out.println(exchange.getRequestURI());
		StringTokenizer tokens = new StringTokenizer(exchange.getRequestURI().toString(),"/");
		List<String> vars = new ArrayList<String>();
		while(tokens.hasMoreTokens()){
			vars.add(tokens.nextToken());
		}
		String from = "", to = "", amount = "", response = "";
		System.out.println("Tokens = " + vars.get(0));
		if (vars.get(0).equalsIgnoreCase("transfer") && vars.size() > 3 ) {
			try {
				from = vars.get(1);
				to = vars.get(2);
				amount = vars.get(3);
				} catch(Exception e) {
				response = "Wrong Params! Correct syntax: /transfer/[from-account-id]/[to-account-id]/[amount] " + e.getMessage();
				exchange.sendResponseHeaders(409, response.getBytes().length);
			}

			//Now Transfer TXN begins
			System.out.println("Ready for Transaction");
			
			TransactionProcessor tpxr = new TransactionProcessor();
			String txnID = tpxr.postTransaction(from, to, Long.parseLong(amount));
			response = "Success - " + txnID + " .Check status at <server>/txnstatus/<txn-id>";
			exchange.sendResponseHeaders(200, response.getBytes().length);
		} else {
			response = "Wrong Params! Correct syntax: /transfer/[from-account-id]/[to-account-id]/[amount]";
			exchange.sendResponseHeaders(400, response.getBytes().length);                             
		}

		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}

}