package test.server.types;
import java.util.List;

import test.server.types.datasrc.TransactionsListDS;

 

public class Account {
	
	private String acctId;
	private String acctNum;
	private String custName;
	private String acctCurrency;
	private long balance;
	
	public Account(String id, String number, String customer, String currency, long startbalance) {
		acctId = id;
		acctNum = number;
		custName = customer;
		acctCurrency = currency;
		balance = startbalance;
	}
	
	public String getId() {
		return acctId;
	}
	public void setId(String id) {
		this.acctId = id;
	}
	public String getAcctNum() {
		return acctNum;
	}
	public void setAcctNum(String acctNum) {
		this.acctNum = acctNum;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getAcctCurrency() {
		return acctCurrency;
	}
	public void setAcctCurrency(String acctCurrency) {
		this.acctCurrency = acctCurrency;
	}
	public List<Transaction> getAcctTransactions() {
		
		return TransactionsListDS.getTransactionsForAccount(this.acctId);
	}
	
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}
	
	
	public String toString() {
		return "[AccountID " +  acctId + " AccountNumber " + acctNum + " Name " + custName + " Currency="+ acctCurrency + " Balance="+ balance+"]";
	}
}

 

