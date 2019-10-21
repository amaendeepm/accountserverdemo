package test.server.types;

import java.util.Date;
import java.util.Random;

public class Transaction {

	private final String id;
	private final Date initTime;
	private Date confTime;
	private Date lastUpdatedTime;
	private final long fromAccountId;
	private final long toAccountId;
	private TxnStatus txnStatus;
	private final double txnAmount;
	private TxnType txnType;

	public enum TxnStatus {
		INIT,
		QUEUED,
		CANCELLED,
		CONFIRMED
	}

	public enum TxnType {
		DEBIT,
		CREDIT
		//Could be more viz. DEBIT_AUTHORIZATION, AUTO_DEBIT etc.
	}

	public Transaction(long fromAcct, long toAcct, double amount) {
		id = getRandomHexString();
		txnStatus =TxnStatus.INIT;
		initTime = new Date();
		lastUpdatedTime = null;
		fromAccountId = fromAcct;
		toAccountId = toAcct;
		txnAmount = amount;
	}

	public TxnStatus getTxnStatus() {
		return txnStatus;
	}

	public void setTxnStatus(TxnStatus txnStatus) {
		this.txnStatus = txnStatus;
	}

	public Date getInitTime() {
		return initTime;
	}

	public Date getConfTime() {
		return confTime;
	}

	public void setConfTime(Date confTime) {
		this.confTime = confTime;
	}

	public Date getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(Date lastUpdatedTime) { //Placeholder: Any last Retry time
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public long getFromAccountId() {
		return fromAccountId;
	}

	public long getToAccountId() {
		return toAccountId;
	}

	private String getRandomHexString(){
		Random r = new Random();
		StringBuffer sb = new StringBuffer();
		while(sb.length() < 64){
			sb.append(Integer.toHexString(r.nextInt()));
		}
		return sb.toString().substring(0, 64);
	}

	public String getId() {
		return id;
	}

	public double getTxnAmount() {
		return txnAmount;
	}
	
	//Copy Constructor
	public Transaction(Transaction another) {
		this.id = another.id;
		this.initTime = another.initTime;
		this.confTime = another.confTime;
		this.lastUpdatedTime = another.lastUpdatedTime;
		this.fromAccountId = another.fromAccountId;
		this.toAccountId = another.toAccountId;
		this.txnStatus = another.txnStatus;
		this.txnAmount = another.txnAmount;   
	  }

	public TxnType getTxnType() {
		return txnType;
	}

	public void setTxnType(TxnType txnType) {
		this.txnType = txnType;
	}
	
	
	public String toString() {
		return "\n\r[Transaction"+ "id="+id+ " fromAcct="+ fromAccountId + " toAccountId="+toAccountId+ " Amount= "+txnAmount + " Type="+ txnType+ " Status="+txnStatus+"]";
	}

}

