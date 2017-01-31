package plagiarism;

/**
 * Organize entry of output list into a single object.
 * @author Evan
 *
 */
public class MatchListEntry implements Comparable<MatchListEntry> {
	private int numMatches;	// number of matches between docA and docB
	private String docNameA;	// string name of document A
	private String docNameB;	// string name of document B
	
	/**
	 * Default constructor.
	 * Empty entry, should not be used.
	 */
	MatchListEntry() {
		numMatches = 0;
		docNameA = "NA";
		docNameB = "NA";
	}
	
	/**
	 * Parametized Constructor.
	 * Create a new object representing the number of matches
	 * between documentA and documentB.
	 * @param matches - number of matching phrases between documents
	 * @param A - document name for first matching document
	 * @param B - document name for second matching document
	 */
	MatchListEntry(int matches, String A, String B) {
		numMatches = matches;
		docNameA = A;
		docNameB = B;
	}
	
	/**
	 * Return number of matches recorded in this entry.
	 * @return number of matches
	 */
	public int getMatches() {
		return numMatches;
	}
	
	/**
	 * returns string in order: numMatches, docNameA, docNameB
	 */
	public String toString() {
		String ret = new String();
		ret += String.format("%-8s", numMatches + ":");
		ret += String.format("%-24s", docNameA);
		ret += String.format("%-24s", docNameB);
		return ret;
	}
	
	/**
	 * right minus left: to order from largest to smallest.
	 * Unnecessary for TreeMap, but still potentially useful.
	 */
	public int compareTo(MatchListEntry rhs) {
		return rhs.numMatches - this.numMatches;
	}
}
