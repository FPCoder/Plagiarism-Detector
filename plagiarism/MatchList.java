package plagiarism;

import java.util.Comparator;
import java.util.Map.Entry;
import java.util.TreeMap;


/**
 * During output of results, add new data to the list,
 * then after the whole process is completed call
 * printList().
 * 
 * Use singular list to keep track of all output.
 * Should print an organized list after filling it
 * with valid entries.
 * @author Evan
 *
 */
public class MatchList {
	
	/**
	 * private class used to reverse the sorting order.
	 * @author Evan
	 *
	 */
	private class MatchComp implements Comparator<Integer> {
		@Override
		public int compare(Integer o1, Integer o2) {
			return -1 * o1.compareTo(o2);
		}
		
	}

	/**
	 * TreeMap is ideal for the list to minimize overall insertion
	 * and removal time. Key type matches the number of matches
	 * for a given Entry.
	 */
	private TreeMap<Integer, MatchListEntry> list = new TreeMap<>(new MatchComp());
	
	/**
	 * Combine the results of matching two documents together,
	 * and add the new entry to the list.
	 * @param match - number of matching phrases between A and B
	 * @param A - document name for first matching document
	 * @param B - document name for second matching document
	 */
	public void addEntry(int match, String A, String B) {
		list.put(match, new MatchListEntry(match, A, B));
	}
	
	/**
	 * print from highest number of matches to lowest,
	 * except matches below initial threshold.
	 * Is a TreeMap, so the data is contained in the
	 * "value" portion of the entry.
	 */
	public void printList() {
		System.out.println("Match\tTest File A\t\tTest File B");
		for (Entry<Integer, MatchListEntry> m : list.entrySet()) {
			System.out.println(m.getValue().toString());
		}
	}
	
	/*public static void main(String[] args) { // used for debugging
		MatchList ls = new MatchList();
		ls.addEntry(400, "A", "B");
		ls.addEntry(500, "C", "B");
		ls.addEntry(450, "C", "D");
		ls.addEntry(150, "2", "3");
		
		ls.printList();
	}*/
}
