
public class DatabaseMine implements DatabaseInterface {
	private int N;
	private String[][] map;
	private int collision = 0;
	private float average = 0;
	private int probeAmounts = 0;

	public DatabaseMine() {
		N = 176921;
		map = new String[N][2];
	}

	public DatabaseMine(int N) {
		this.N = N;
		map = new String[N][2];
	}

	public String save(String plainPassword, String encryptedPassword) {
		Boolean done = false;
		int probeAmount = 0;
		int tmp = hashFunction(encryptedPassword);
		while (done != true) {
			probeAmount++;
			if (map[tmp][0] != null) {
				collision++;
				tmp = (tmp + 2) % N;
			} else {
				done = true;
			}
		}
		probeAmounts++;
		average = average + (float) (probeAmount - average) / (probeAmounts);
		map[tmp][0] = encryptedPassword;
		map[tmp][1] = plainPassword;
		return plainPassword;
	}

	public String decrypt(String encryptedPassword) {
		Boolean done = false;
		int tmp = hashFunction(encryptedPassword);
		while (done != true) {
			if (!map[tmp][0].equals(encryptedPassword) && map[tmp][0] != null) {
				tmp = (tmp + 2) % N;
			} else {
				done = true;
			}
		}
		return map[tmp][1];
	}

	public int size() {
		int tmp = 0;
		for (int i = 0; i < map.length; i++) {
			if (map[i][0] != null) {
				tmp++;
			}
		}
		return tmp;
	}

	private int hashFunction(String key) {
		int address = key.hashCode() % N;
		return (address >= 0) ? address : (address + N);
	}

	public void printStatistics() {
		System.out.println("*** DatabaseStandard Statistics ***");
		System.out.println("Size is " + size() + " passwords");
		System.out.println("Number of indexes is " + N);
		System.out.println("Load factor is " + (float) size() / N);
		System.out.println("Average number of probes is " + average);
		System.out.println("Number of displacements (due to collions) " + collision);
		System.out.println("*** End DatabaseStandard Statistics ***");
	}

}
