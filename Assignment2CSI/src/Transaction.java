/**
 * instance of transaction used to store and simplify transaction information
 * 
 * 
 * **/
public class Transaction {
	private String sender;
	private String receiver;
	private int amount;
//constructor with 3 string inputs to set as global variables
	public Transaction(String sender, String receiver, int amount){
		this.sender = sender;
		this.receiver = receiver;
		this.amount = amount;
	}
	//returns the name of person sending money
	public String getSender() {
		return sender;
	}
	//returns the name of person receiving money
	public String getReceiver() {
		return receiver;
	}
	//returns amount of money being transfered
	public int getAmount() {
		return amount;
	}
	//returns a string of the transaction information
	public String toString() {
		return sender + ":" + receiver + "=" + amount;
	}
}