package test.server.types.datasrc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import test.server.types.Account;
import test.server.types.Transaction;
import test.server.types.Transaction.TxnStatus;

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

	public static List<Transaction> getTransactionsForAccount(long account) {

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


	public static synchronized void postTransaction(Transaction t) {
		if (t.getFromAccountId()!=t.getToAccountId() && t.getTxnAmount()>0) {

			
			//Find fromAccount
			Account fromAcctObj = AccountsDS.findAccount(t.getFromAccountId());

			//Find toAccount
			Account toAcctObj = AccountsDS.findAccount(t.getToAccountId());

			
			
			toAcctObj.setBalance(toAcctObj.getBalance()+t.getTxnAmount());
			fromAcctObj.setBalance(fromAcctObj.getBalance() - t.getTxnAmount());
			t.setTxnStatus(TxnStatus.CONFIRMED);
			t.setConfTime(new Date());
			t.setLastUpdatedTime(t.getConfTime());

			listTxn.add(t);
		}
	}

	public static synchronized void cancelTransaction(Transaction t) {

		t.setTxnStatus(TxnStatus.CANCELLED);
		t.setLastUpdatedTime(new Date());
		listTxn.add(t);

	}


	public static List<Transaction> getAllTransactions() {

		return listTxn;
	}


}
