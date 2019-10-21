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
		long from = 0, to = 0;
		double amount = 0.0;
		String response = "";
		System.out.println("Tokens = " + vars.get(0));

		if (vars.get(0).equalsIgnoreCase("transfer") ) {

			if ( vars.size() > 3 ) {

				try {
					from = Long.parseLong(vars.get(1));
					to = Long.parseLong(vars.get(2));
					amount = Double.parseDouble(vars.get(3));

					//Now Transfer TXN begins
					System.out.println("Ready for Transaction");

					TransactionProcessor tpxr = new TransactionProcessor();
					String txnID = tpxr.postTransaction(from, to, amount);
					response = "Transaction Submitted: " + txnID + " .Check status at <server>/txnstatus/<txn-id>";
					exchange.sendResponseHeaders(201, response.getBytes().length);
				} catch(NumberFormatException e) {
					response = "Wrong Params! Correct syntax: /transfer/[from-account-id]/[to-account-id]/[amount] " + e.getMessage();
					exchange.sendResponseHeaders(400, response.getBytes().length);
				}
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

			if ( vars.size() > 1 && Long.parseLong(vars.get(1)) > 0) {

				long accountID ;
				try {
					accountID = Long.parseLong(vars.get(1));

					System.out.println("Find account id="+ accountID);
					Account acct = AccountsDS.findAccount(Long.parseLong(vars.get(1)));
					System.out.println("Found " + acct);

					if(acct!= null) {
						List<Transaction> txnList = TransactionsListDS.getInstance().getTransactionsForAccount(acct.getId());

						response = acct.toString() + "\n\r Transactions: "+  txnList;
						exchange.sendResponseHeaders(200, response.getBytes().length);

					} else {
						response = vars.get(1) + ": Account Not Found";
						exchange.sendResponseHeaders(404, response.getBytes().length);
					}

				} catch(NumberFormatException e) {
					response = vars.get(1) + ": Invalid Account ID";
					exchange.sendResponseHeaders(400, response.getBytes().length);
				}


			}
		} else if (vars.get(0).equalsIgnoreCase("txnstatus") ) {

				if ( vars.size() > 1 ) {
					
					Transaction t = TransactionsListDS.getInstance().findTransaction(vars.get(1));
					
					if (t!=null) {
						response = t.toString();
						exchange.sendResponseHeaders(200, response.getBytes().length);
					} else {
						response = vars.get(1) + ": Transaction ID not Found";
						exchange.sendResponseHeaders(404, response.getBytes().length);
					}


				}
			else {
				response = "Wrong Params! Correct syntax: /account/[account-id]";
				exchange.sendResponseHeaders(200, response.getBytes().length);
			}

		} else if (vars.get(0).equalsIgnoreCase("ledger") ) {

			List<Transaction> txnList = TransactionsListDS.getInstance().getAllTransactions();

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
