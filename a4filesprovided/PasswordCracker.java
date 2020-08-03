import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class PasswordCracker {

	private String year = "2018";
	private static int index = 176921;

	public String crackPassword(String cryptic, DatabaseInterface database) {
		String tmp = "";
		if (database.decrypt(cryptic) != (null)) {
			tmp = database.decrypt(cryptic);
		}
		return tmp;
	}

	void createDatabase(ArrayList<String> commonPasswords, DatabaseInterface database) {
		ArrayList<String> passwords = new ArrayList<String>(index);
		passwords.addAll(commonPasswords);
		passwords.addAll(capitalize(passwords));
		passwords.addAll(addYear(passwords));
		passwords.addAll(theWord(passwords, 'a', "@"));
		passwords.addAll(theWord(passwords, 'e', "3"));
		passwords.addAll(theWord(passwords, 'i', "1"));
		for (int i = 0; i < passwords.size(); i++) {
			try {
				database.save(passwords.get(i), Sha1.hash(passwords.get(i))); // **
			} catch (UnsupportedEncodingException e) {
				System.out.println("error encoding");
			}
		}
	}

	public static int getIndex() {
		return index;
	}
	
	private ArrayList<String> capitalize(ArrayList<String> passwords) {
		ArrayList<String> cryptedPasswords = new ArrayList<String>(passwords.size());
		for (int i = 0; i < passwords.size(); i++) {
			if (!Character.isDigit(passwords.get(i).charAt(0))) {
				cryptedPasswords.add(passwords.get(i).substring(0, 1).toUpperCase() + passwords.get(i).substring(1));
				// System.out.println(passwords.get(i).substring(0, 1).toUpperCase() +
				// passwords.get(i).substring(1));
			}
		}
		return cryptedPasswords;
	}

	private ArrayList<String> addYear(ArrayList<String> passwords) {
		ArrayList<String> cryptedPasswords = new ArrayList<String>(passwords.size());
		for (int i = 0; i < passwords.size(); i++) {
			cryptedPasswords.add(passwords.get(i) + year);
			// System.out.println(passwords.get(i) + year);
		}
		return cryptedPasswords;
	}

	private ArrayList<String> theWord(ArrayList<String> passwords, char before, String after) {
		ArrayList<String> cryptedPasswords = new ArrayList<String>(passwords.size());
		for (int i = 0; i < passwords.size(); i++) {
			if (passwords.get(i).contains(before + "")) {
				cryptedPasswords.addAll(swap(passwords.get(i), before, after));
			}
		}
		return cryptedPasswords;
	}

	private ArrayList<String> swap(String password, char before, String after) {
		ArrayList<String> tmp = new ArrayList<String>();
		tmp.add(password);
		for (int i = 0; i < password.length(); i++) {
			if (password.charAt(i) == before) {
				int len = tmp.size();
				for (int x = 0; x < len; x++) {
					tmp.add(tmp.get(x).substring(0, i) + after + tmp.get(x).substring(i + 1));
				}
			}
		}
		tmp.remove(0);
		return tmp;
	}
}
