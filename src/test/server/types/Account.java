package test.server.types;
import java.util.List;

 

public class Account {
	
	private String acctId;
	private String acctNum;
	private String custName;
	private String acctCurrency;
	private List<Transaction> acctTransactions;
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
		
		//DO SOMETHING HERE
		return acctTransactions;
	}
	
	public long getBalance() {
		return balance;
	}
	public void setBalance(long balance) {
		this.balance = balance;
	}
}

 

