package test.server.types.datasrc;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import test.server.types.Account;

public class AccountsDS {
	
	private static List<Account> acctList;
	
	private static AccountsDS accounts;
	
	
	static{
		accounts = new AccountsDS();
		accounts.init();
    }
	
	private AccountsDS() {
		if(accounts!=null) {
			throw new RuntimeException("Use findAccount([id]) method to get Datasource singleton instance");
		}
	}
	
	private void init() {
		acctList = new CopyOnWriteArrayList<Account>();
		
		Account a = new Account(1001,"ACCT101","Apple","EUR", 500);
		Account b = new Account(1002,"ACCT102","Orange","EUR", 500);
		Account c = new Account(1003,"ACCT103","Mango","EUR", 500);
		
		acctList.add(a);
		acctList.add(b);
		acctList.add(c);
	}
	
	
	public static Account findAccount(long id) {

		int i = 0;
		
		while (i < acctList.size() && acctList.get(i).getId()!=id) {
			i++;
		}
		

		if (i < acctList.size())  {
				return acctList.get(i);
		}			
		
		
		return null;
	}
	
	
	public static void UpdateAccountBalance(long id, long newBalance) {
		Account acct = findAccount(id);
		
		if (acct!=null) {
			acct.setBalance(newBalance);
		}
	}
	
	//TODO: Unit Test above with a main:)
	
	public static List<Account> getAllAccounts() {
		return acctList;
	}
	

}
