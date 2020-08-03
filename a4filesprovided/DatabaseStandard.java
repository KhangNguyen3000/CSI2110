import java.util.HashMap;
import java.util.Map;

public class DatabaseStandard implements DatabaseInterface {
	private Map<String, String> map;

	public DatabaseStandard() {
		map = new HashMap<String, String>();
	}

	public String save(String plainPassword, String encryptedPassword) {
		String tmp = map.get(encryptedPassword);
		map.put(encryptedPassword, plainPassword);
		return tmp;
	}

	public String decrypt(String encryptedPassword) {
		return map.get(encryptedPassword);
	}

	public int size() {
		return map.size();
	}

	public void printStatistics() {
		System.out.println("*** DatabaseStandard Statistics ***");
		System.out.println("Size is "+size()+" passwords");
		System.out.println("Initial number of index when created is "+PasswordCracker.getIndex());
		System.out.println("*** End DatabaseStandard Statistics ***");
	}

}
