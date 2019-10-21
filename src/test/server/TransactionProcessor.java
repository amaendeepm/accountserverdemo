package test.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import test.server.types.Account;
import test.server.types.Transaction;
import test.server.types.Transaction.TxnStatus;
import test.server.types.datasrc.AccountsDS;
import test.server.types.datasrc.TransactionsListDS;

public class TransactionProcessor {

	
	TransactionProcessor(){}
	
	
	protected synchronized String postTransaction(String fromAccount, String toAccount, long amount) {
		
		BlockingQueue<Transaction> txnLifeQueue = new LinkedBlockingDeque<>(10000);
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Transaction txn = new Transaction(fromAccount,toAccount,amount);
		
		Runnable createTxn = () -> {
			try {
				
					txnLifeQueue.put(txn);
					System.out.println("Posted-Transaction " + txn);
					Thread.sleep(1000);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};

		Runnable redactTxn = () -> {
			try {
				while (true) {
					Transaction incTxn = txnLifeQueue.take();
					System.out.println("Redacting-Transaction " + incTxn);
					incTxn.setTxnStatus(TxnStatus.QUEUED);
					
					//Find fromAccount
					Account fromAcctObj = AccountsDS.findAccount(fromAccount);
					
					//Find toAccount
					Account toAcctObj = AccountsDS.findAccount(toAccount);
					
					//Check balance in fromAccount
					if (fromAcctObj!=null && toAcctObj!=null && fromAcctObj.getId()!= toAcctObj.getId() && fromAcctObj.getBalance() > amount) {
						
						//TXN START
						TransactionsListDS.postTransaction(incTxn);
						//TXN END
						
					} else {
						incTxn.setTxnStatus(TxnStatus.CANCELLED);
					}
					
					Thread.sleep(1000);
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
		
		executor.execute(createTxn);
        executor.execute(redactTxn);
        executor.shutdown();

		return txn.getId();
	}      

	
}
