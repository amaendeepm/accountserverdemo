package test.server;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.ReentrantLock;

import test.server.types.Account;
import test.server.types.Transaction;
import test.server.types.Transaction.TxnStatus;
import test.server.types.datasrc.AccountsDS;
import test.server.types.datasrc.TransactionsListDS;

public class TransactionProcessor {

	
	TransactionProcessor(){}
	
	
	protected String postTransaction(long fromAccount, long toAccount, double amount) {
		
		BlockingQueue<Transaction> txnLifeQueue = new LinkedBlockingDeque<>(10000);
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Transaction txn = new Transaction(fromAccount,toAccount,amount);
		ReentrantLock txnLock = new ReentrantLock();
		
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
					
					txnLock.lock();
					
					//Find fromAccount
					Account fromAcctObj = AccountsDS.findAccount(fromAccount);
					
					//Find toAccount
					Account toAcctObj = AccountsDS.findAccount(toAccount);
					
					//Check balance in fromAccount
					if (fromAcctObj!=null && toAcctObj!=null && fromAcctObj.getId()!= toAcctObj.getId() && fromAcctObj.getBalance() > amount) {
						
							toAcctObj.setBalance(toAcctObj.getBalance()+incTxn.getTxnAmount());
							fromAcctObj.setBalance(fromAcctObj.getBalance() - incTxn.getTxnAmount());
							incTxn.setTxnStatus(TxnStatus.CONFIRMED);
							incTxn.setConfTime(new Date());
							incTxn.setLastUpdatedTime(incTxn.getConfTime());

					} else {
						incTxn.setTxnStatus(TxnStatus.CANCELLED);
						incTxn.setLastUpdatedTime(new Date());						
					}
					
					TransactionsListDS.getInstance().logTransaction(incTxn);
					
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				txnLock.unlock();
			}
		};
		
		executor.execute(createTxn);
        executor.execute(redactTxn);
        executor.shutdown();

		return txn.getId();
	}      

	
}
