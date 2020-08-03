import java.io.UnsupportedEncodingException;

/**
 * this class is used to contain the information about the blocks
 * 
 **/
public class Block {
	private int index; // the index of the block in the list
	private java.sql.Timestamp timestamp; // time at which transaction
	// has been processed
	private Transaction transaction; // the transaction object
	private String nonce = ""; // random string (for proof of work)
	private String previousHash; // previous hash (set to "" in first block)
	// (in first block, set to string of zeroes of size of complexity "00000")
	private String hash; // hash of the block (hash of string obtained
	private long timeL;

	// constructor for when reading blocks from file since all the information is
	// already available sets them as global variables
	public Block(int index, long timestampL, Transaction transaction, String nonce, String hash) {
		this.index = index;
		timeL = timestampL;
		java.sql.Timestamp timestamp = new java.sql.Timestamp(timestampL);
		this.timestamp = timestamp;
		this.transaction = transaction;
		this.nonce = nonce;
		this.hash = hash;
	}

	// constructor for when making new blocks from transactions as not all the
	// information is available, runs through methods then sets them as global
	// variables
	public Block(int index, long timestampL, Transaction transaction) {
		this.index = index;
		timeL = timestampL;
		java.sql.Timestamp timestamp = new java.sql.Timestamp(timestampL);
		this.timestamp = timestamp;
		this.transaction = transaction;

	}

	// this sets the hash then runs nonce to get a hash starting with "00000"
	public void setHash() {
		try {
			hash = Sha1.hash(toString());
			nonce();
		} catch (UnsupportedEncodingException e) {
			System.out.println("Error hashing");
		}
	}

	// this method makes a random string between 1 and 20 characters for ASCII 33 -
	// 126, then checks if it starts with "00000" if so it continues on keeping
	// track of loop and time
	private void nonce() throws UnsupportedEncodingException {
		int loop = 0;
		long start = System.currentTimeMillis();
		do {
			loop++;
			for (int i = 1; i < (int) (Math.random() * 19) + 1; i++) {
				this.nonce = this.nonce + Character.toString((char) ((int) (Math.random() * 93) + 33));
			}
			hash = Sha1.hash(toString());
			if (!hash.startsWith("00000")) {
				this.nonce = "";
			}
		} while (!hash.startsWith("00000"));
		long finish = System.currentTimeMillis();
		long timeElapsed = finish - start;
		System.out.println("RUN TIME: " + timeElapsed);
		System.out.println("LOOPS: " + loop);
	}

	//sets previous blocks hash
	public void setPHash(String PHash) {
		previousHash = PHash;
	}

	// returns previous hash
	public String getPHash() {
		return previousHash;
	}

	// return index of block
	public int getIndex() {
		return index;
	}

	// get the timestamp in long
	public long getTimeStamp() {
		return timeL;
	}

	// get the current blocks hash
	public String getHash() {
		return hash;
	}

	// returns the nonce
	public String getNonce() {
		return nonce;
	}

//returns the transaction that happened in this block
	public Transaction getTransaction() {
		return transaction;
	}

//returns a string of all the blocks information
	public String toString() {
		return timestamp.toString() + ":" + transaction.toString() + "." + nonce + previousHash;
	}
}