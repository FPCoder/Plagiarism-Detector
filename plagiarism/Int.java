package plagiarism;

/**
 * A wrapper class for primitive type int.
 * Integer wrapper class takes too much data due to all of the methods associated with it.
 * int's are not acceptable for passing to data structures that require objects.
 * This should solve that problem.
 * @author Jacob Webb
 *
 */
public class Int {
	private int myInt;
	
	/**
	 * simple constructor takes in an int for our field
	 * @param oldInt
	 */
	public Int(int oldInt) {
		myInt = oldInt;
	}
	
	/**
	 * override the hashCode() method to work in the hash map.
	 * The hash code will just be the int value
	 * @return int myInt
	 */
	@Override 
	public int hashCode() {
		return myInt;
	}
	
	/**
	 * override the equals() method for each object
	 * @return boolean check whether the objects are the same class, if it is the same instance of an object,
	 * and whether or not the int values are identical.
	 */
	@Override
	public boolean equals(Object rhs) {
		if (rhs == null) return false;
		if (rhs == this) return true;
		if (!(rhs instanceof Int)) return false;
		Int test = (Int) rhs;
		return this.myInt == test.myInt;
	}
}
