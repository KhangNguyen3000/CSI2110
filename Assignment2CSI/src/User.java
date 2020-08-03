/**
 * 
 * this class keeps the users data, it is here for the sake of making the program run faster
 * 
 * **/

public class User {
	private String name;
	private int balance;
	//constructor for name and balance
	public User(String name) {
		this.name = name;
		balance = 0;
	}
	//returns balance
	public int getBalance() {
		return balance;
	}
	//returns name
	public String getName() {
		return name;
	}
	//add amount  to user
	public void add(int amount) {
		balance = balance + amount;
	}
}
