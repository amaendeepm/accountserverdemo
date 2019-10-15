package test.server.types.datasrc;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import test.server.types.Account;

public class AccountsDS {
	
	private static List<Account> acctList;
	
	private static AccountsDS accounts;
	
	private AccountsDS() {
		if(accounts!=null) {
			throw new RuntimeException("Use findAccount([id]) method to get Datasource singleton instance");
		}
	}
	
	
	public static Account findAccount(String id) {
		
		if(accounts==null) {
			//Build some accounts here STX
			
			acctList = new CopyOnWriteArrayList<Account>();
			
			Account a = new Account("id0","101","Apple","EUR", 500);
			Account b = new Account("id1","102","Orange","EUR", 500);
			Account c = new Account("id1","103","Mango","EUR", 500);
			
			acctList.add(a);
			acctList.add(b);
			acctList.add(c);
			
			//End building accounts here ETX
			accounts =  new AccountsDS();
		
		}
		
		int i = 0;
		while (i < acctList.size() && acctList.get(i).getId()!=id) {
			i++;
		}
		
		if (acctList.get(i).getId()==id) {
			return acctList.get(i);
		}
		return null;
	}
	
	
	public static void UpdateAccountBalance(String id, long newBalance) {
		Account acct = findAccount(id);
		
		if (acct!=null) {
			acct.setBalance(newBalance);
		}
	}
	
	//TODO: Unit Test above with a main:)

}
