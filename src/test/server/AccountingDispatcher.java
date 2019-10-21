package test.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.sun.net.httpserver.HttpExchange;

import test.server.types.Account;
import test.server.types.Transaction;
import test.server.types.datasrc.AccountsDS;
import test.server.types.datasrc.TransactionsListDS;

public class AccountingDispatcher {

	
	public AccountingDispatcher() {}
	
	public void handleRequest(HttpExchange exchange) throws IOException {
		System.out.println(exchange.getRequestURI());
		StringTokenizer tokens = new StringTokenizer(exchange.getRequestURI().toString(),"/");
		List<String> vars = new ArrayList<String>();
		while(tokens.hasMoreTokens()){
			vars.add(tokens.nextToken());
		}
		String from = "", to = "", amount = "", response = "";
		System.out.println("Tokens = " + vars.get(0));
		
		if (vars.get(0).equalsIgnoreCase("transfer") ) {
			
			if ( vars.size() > 3 ) {
				
			
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
			
		} else if (vars.get(0).equalsIgnoreCase("accounts") ) {
			List<Account> acctList = AccountsDS.getAllAccounts();
			System.out.println(acctList);
			
			response = acctList.toString();
			exchange.sendResponseHeaders(200, response.getBytes().length); 
			
		} else if (vars.get(0).equalsIgnoreCase("account") ) {
			
			if ( vars.size() > 1 ) {
				System.out.println("Find account id="+vars.get(1));
				Account acct = AccountsDS.findAccount(vars.get(1));
				System.out.println("Found " + acct);
				
				if(acct!= null) {
					List<Transaction> txnList = TransactionsListDS.getTransactionsForAccount(acct.getId());
					
					response = acct.toString() + "\n\r Transactions: "+  txnList;
					
					exchange.sendResponseHeaders(200, response.getBytes().length);
					
				} else {
					response = vars.get(1) + ": Account Not Found";
					
					exchange.sendResponseHeaders(404, response.getBytes().length);
				}
				
								
			} else {
				response = "Wrong Params! Correct syntax: /account/[account-id]";
				exchange.sendResponseHeaders(200, response.getBytes().length);
			}
			
		} else if (vars.get(0).equalsIgnoreCase("ledger") ) {
			
			List<Transaction> txnList = TransactionsListDS.getAllTransactions();
			
			response = txnList.toString();
			exchange.sendResponseHeaders(200, response.getBytes().length);
			
		} else {
			response = "Correct syntax: (1) /transfer/[from-account-id]/[to-account-id]/[amount]  OR (2) /accounts OR  /account/[account-id] OR (3) /ledger";
			exchange.sendResponseHeaders(400, response.getBytes().length);
		}

		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
}
