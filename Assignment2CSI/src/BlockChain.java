
/*
 *number
 *time
 *sender
 *receiver
 *amount
 *nonce
 *hash
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * this blockchain class is used to contain a list of blocks and verify them and
 * make new transaction/blocks
 * 
 **/
public class BlockChain {

	private ArrayList<Block> blockChain = new ArrayList<Block>(3);
	private ArrayList<User> users = new ArrayList<User>(3);

	// creates a blockchain instance from a file name
	public static BlockChain fromFile(String fileName) {
		return new BlockChain(fileName);
	}

	// validates the blockchain from a file to make sure it has not been tampered
	// with
	public BlockChain(String fileName) {
		boolean validated = false;
		try {
			readFile(fileName);
		} catch (IOException e) {
			System.out.println("Error reading file");
			System.exit(0);
		}
		try {
			validated = validateBlockchain();
		} catch (UnsupportedEncodingException e) {
			System.out.println("Error validating Block Chain");
			System.exit(0);
		}if(validateTransaction() !=true) {
			validated = false;
		}
		if (validated != true) {
			System.out.println("Transaction not validated");
			System.exit(0);
		} else {
			System.out.println("Transaction validated");
		}
	}

	// validates blockchain and returns true if it is validated
	private boolean validateBlockchain() throws UnsupportedEncodingException {
		boolean t = true;
		for (int x = 0; x < blockChain.size(); x++) {
			if (!Sha1.hash(blockChain.get(x).toString()).equals(blockChain.get(x).getHash())) {
				t = false;
				System.out.println("Problem with hash");
			}if(blockChain.get(x).getIndex() != x) {
				t = false;
				System.out.println("Problem with index");
			}
		}
		return t;
	}

	// writes to a file containing the filename, writes the blocks so that it can be
	// validated next time it is opened
	private void toFile(String fileName) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(fileName));
			for (int i = 0; i < blockChain.size(); i++) {
				writer.write(blockChain.get(i).getIndex() + System.getProperty("line.separator"));
				writer.write("" + blockChain.get(i).getTimeStamp() + System.getProperty("line.separator"));
				writer.write(blockChain.get(i).getTransaction().getSender() + System.getProperty("line.separator"));
				writer.write(blockChain.get(i).getTransaction().getReceiver() + System.getProperty("line.separator"));
				writer.write(blockChain.get(i).getTransaction().getAmount() + System.getProperty("line.separator"));
				writer.write(blockChain.get(i).getNonce() + System.getProperty("line.separator"));
				writer.write(blockChain.get(i).getHash() + System.getProperty("line.separator"));
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Error writing to file");
			System.exit(0);
		}
	}

	// this methode validates all existing blocks and their transactions to make
	// sure that no one has ever been negative
	// if at some point a user has gone negative this will return false even if the
	// user is positive at the end of the blockChain
	private boolean validateTransaction() {
		boolean t = true;
		for (int x = 0; x < blockChain.size(); x++) {
			boolean t1 = true;
			boolean t2 = true;
			for (int i = 0; i < users.size(); i++) {
				if (blockChain.get(x).getTransaction().getSender().equals(users.get(i).getName()))
					t1 = false;
				if (blockChain.get(x).getTransaction().getReceiver().equals(users.get(i).getName()))
					t2 = false;
			}
			if (t1 == true) {
				User user = new User(blockChain.get(x).getTransaction().getSender());
				users.add(user);
			}
			if (t2 == true) {
				User user = new User(blockChain.get(x).getTransaction().getReceiver());
				users.add(user);
			}
		}

		for (int x = 0; x < blockChain.size(); x++) {
			boolean t1 = false;
			boolean t2 = false;
			int i = 0;
			while (t1 == false || t2 == false) {
				if (blockChain.get(x).getTransaction().getSender().equals(users.get(i).getName())) {
					t1 = true;
					users.get(i).add(-blockChain.get(x).getTransaction().getAmount());
					if ((getBalance(users.get(i).getName()) < 0 && !users.get(i).getName().equals("bitcoin"))
							|| (users.get(i).getName().equals("bitcoin")) && getBalance(users.get(i).getName()) < -50) {
						System.out.println(x+" "+users.get(i).getName());
						t = false;
					}
				}
				if (blockChain.get(x).getTransaction().getReceiver().equals(users.get(i).getName())) {
					t2 = true;
					users.get(i).add(blockChain.get(x).getTransaction().getAmount());
				}
				i++;
			}
		}

		return t;
	}

	// returns the balance of the user which is the input
	private int getBalance(String username) {
		int balance = 0;
		boolean found = false;
		for (int x = 0; x < users.size(); x++) {
			if (users.get(x).getName().equals(username)) {
				balance = users.get(x).getBalance();
				found = true;
			}
		}
		if (found != true) {
			System.out.println("User not Found. Exiting program");
			System.exit(0);
		}
		return balance;

	}

	// reads the file and puts the information into the correct block variable
	private void readFile(String fileName) throws IOException {
		Scanner sc = new Scanner(new File(fileName));
		String line[] = new String[7];
		while (sc.hasNextLine()) {
			for (int i = 0; i < 7; i++) {
				line[i] = sc.nextLine();
			}
			Transaction transaction = new Transaction(line[2], line[3], Integer.parseInt(line[4]));
			Block block = new Block(Integer.parseInt(line[0]), Long.parseLong(line[1]), transaction, line[5], line[6]);
			blockChain.add(block);
			try {
				blockChain.get(blockChain.size() - 1).setPHash(blockChain.get(blockChain.size() - 2).getHash());
			} catch (IndexOutOfBoundsException e) {
				blockChain.get(blockChain.size() - 1).setPHash("00000");
			}
		}
		sc.close();
	}

	// the main methode where everything starts
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("File name please.");
		BlockChain blockChainInstance = fromFile(sc.next());
		while (true) {
			System.out.println("Another transaction? (y/n) ");
			if (sc.next().contains("n")) {
				blockChainInstance.toFile("BlockChain_knguy082123123.txt");
				break;
			}
			System.out.println("Username of Sender: ");
			String sender = sc.next();
			System.out.println("Username of receiver: ");
			String receiver = sc.next();
			System.out.println("Amount: ");
			try {
				int amount = Integer.parseInt(sc.next());
				if (blockChainInstance.getBalance(sender) < amount) {
					System.out.println("Not enough currency.");
				} else {
					blockChainInstance.blockChain.add(new Block(blockChainInstance.blockChain.size(),
							java.lang.System.currentTimeMillis(), new Transaction(sender, receiver, amount)));
					blockChainInstance.blockChain.get(blockChainInstance.blockChain.size() - 1).setPHash(
							blockChainInstance.blockChain.get(blockChainInstance.blockChain.size() - 2).getHash());
					blockChainInstance.blockChain.get(blockChainInstance.blockChain.size() - 1).setHash();
				}
				blockChainInstance.validateTransaction();
			} catch (NumberFormatException e) {
				System.out.println("Not an integer");
			}
		}
		sc.close();
	}
}
