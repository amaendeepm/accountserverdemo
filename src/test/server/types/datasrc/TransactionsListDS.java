package test.server.types.datasrc;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import test.server.types.Account;
import test.server.types.Transaction;


public class TransactionsListDS {

	private static TransactionsListDS txnListDS;

	private static List<Transaction> listTxn;

	private TransactionsListDS() {}


	static{
		txnListDS = new TransactionsListDS();
		txnListDS.init();
	}

	public static TransactionsListDS getInstance(){
		return txnListDS;
	}

	private void init() {
		listTxn = new ArrayList<Transaction>();

	}

	public synchronized List<Transaction> getTransactionsForAccount(long account) {

		ListIterator<Transaction> txnItr = listTxn.listIterator();

		List<Transaction> accountTxns = new ArrayList<Transaction>();

		while (txnItr.hasNext()) {
			Transaction t =  txnItr.next();

			if ( t.getToAccountId() == account ) {
				Transaction postTx = new Transaction(t);
				postTx.setTxnType(Transaction.TxnType.CREDIT);
				accountTxns.add(postTx);
			} else if ( t.getFromAccountId() == account ) {
				Transaction postTx = new Transaction(t);
				postTx.setTxnType(Transaction.TxnType.DEBIT);
				accountTxns.add(postTx);
			}  
		}

		return accountTxns;
	}


	public synchronized void logTransaction(Transaction t) {
		listTxn.add(t);

	}


	public Transaction findTransaction(String txnId) {
		int i = 0;
		while (i < listTxn.size() && listTxn.get(i).getId()!=txnId) {
			i++;
		}
		if (i < listTxn.size())  {
			return listTxn.get(i);
		}			
		return null;
	}



	public List<Transaction> getAllTransactions() {

		return listTxn;
	}


}
