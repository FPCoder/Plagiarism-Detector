package dataStructure;

/*
 * Copyright 2014, Michael T. Goodrich, Roberto Tamassia, Michael H. Goldwasser
 *
 * Developed for use with the book:
 *
 *    Data Structures and Algorithms in Java, Sixth Edition
 *    Michael T. Goodrich, Roberto Tamassia, and Michael H. Goldwasser
 *    John Wiley & Sons, 2014
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.ArrayList;

/*
 * Map implementation using hash table with separate chaining.
 *
 * @author Michael T. Goodrich
 * @author Roberto Tamassia
 * @author Michael H. Goldwasser
 */
public class ChainHashMap<K,V> extends AbstractHashMap<K,V> {
  // a fixed capacity array of UnsortedTableMap that serve as buckets
  private UnsortedTableMap<K,V>[] table;   // initialized within createTable

  // provide same constructors as base class
  /** Creates a hash table with capacity 11 and prime factor 109345121. */
  public ChainHashMap() { super(); }

  /** Creates a hash table with given capacity and prime factor 109345121. */
  public ChainHashMap(int cap) { super(cap); }

  /** Creates a hash table with the given capacity and prime factor. */
  public ChainHashMap(int cap, int p) { super(cap, p); }

  /** Creates an empty table having length equal to current capacity. */
  @Override
  @SuppressWarnings({"unchecked"})
  protected void createTable() {
    table = (UnsortedTableMap<K,V>[]) new UnsortedTableMap[capacity];
  }

  /**
   * Returns value associated with key k in bucket with hash value h1 or h2.
   * If no such entry exists, returns null.
   * @param h1  the hash value of the relevant bucket
   * @param h2  the secondary hash value of the bucket
   * @param k  the key of interest
   * @return   associate value (or null, if no such entry)
   */
  @Override
  protected V bucketGet(int h1, int h2, K k) {
    UnsortedTableMap<K,V> bucket1 = table[h1];
    UnsortedTableMap<K,V> bucket2 = table[h2];
    
    if (bucket1 == null && bucket2 == null) return null;
    if (bucket1.get(k) == null){
    	return bucket2.get(k);
    }else{
    	return bucket1.get(k);
    }
  }

  /**
   * Associates key k with value v in bucket with hash value h1 or h2, returning
   * the previously associated value, if any.
   * @param h1  the hash value of the relevant bucket
   * @param h2  the secondary hash value if the first bucket has more entries
   * @param k  the key of interest
   * @param v  the value to be associated
   * @return   previous value associated with k (or null, if no such entry)
   */
  @Override
  protected V bucketPut(int h1,int h2, K k, V v) {
    UnsortedTableMap<K,V> bucket1 = table[h1];
    UnsortedTableMap<K,V> bucket2 = table[h2];
    if (bucket1 == null || bucket2 == null) {
    	if (bucket1 == null) {
    		bucketRemove(h1, h2, k);
    		bucket1 = table[h1] = new UnsortedTableMap<>();
    		return bucket1.put(k, v);
    	}
    	else if (bucket2 == null) {
    		bucketRemove(h1, h2, k);
    		bucket2 = table[h2] = new UnsortedTableMap<>();
    		return bucket2.put(k, v);
    	}
    }
    else {
    	if (bucket1.size() <= bucket2.size()) {
    		bucketRemove(h1, h2, k);
    		int oldSize = bucket1.size();
    	    V answer = bucket1.put(k,v);
    	    n += (bucket1.size() - oldSize);   // size may have increased
    	    return answer;
    	}
    	else {
    		bucketRemove(h1, h2, k);
    		int oldSize = bucket2.size();
    	    V answer = bucket2.put(k,v);
    	    n += (bucket2.size() - oldSize);   // size may have increased
    	    return answer;
    	}
    }
	return null;
    
  }

  /**
   * Removes entry having key k from bucket with hash value h1 or h2, returning
   * the previously associated value, if found.
   * @param h1  the hash value of the relevant bucket
   * @param h2  the secondary hash value
   * @param k  the key of interest
   * @return   previous value associated with k (or null, if no such entry)
   */
  @Override
  protected V bucketRemove(int h1, int h2, K k) {
    UnsortedTableMap<K,V> bucket1 = table[h1];
    UnsortedTableMap<K,V> bucket2 = table[h2];
    if (bucket1 == null && bucket2 == null) return null;
    
    if (bucket1 == null && bucket2 != null){
    	 int oldSize = bucket2.size();
    	    V answer = bucket2.remove(k);
    	    n -= (oldSize - bucket2.size());   // size may have decreased
    	    return answer;
    }else if (bucket1 != null && bucket2 == null){
    	 int oldSize = bucket1.size();
    	    V answer = bucket1.remove(k);
    	    n -= (oldSize - bucket1.size());   // size may have decreased
    	    return answer;
    }
    else if (bucket1 != null && bucket2 != null){
    	if (bucket1.get(k) == null){
    		int oldSize = bucket2.size();
    	    V answer = bucket2.remove(k);
    	    n -= (oldSize - bucket2.size());   // size may have decreased
    	    return answer;
    	}
    	else if(bucket2.get(k) == null){
    		int oldSize = bucket1.size();
    	    V answer = bucket1.remove(k);
    	    n -= (oldSize - bucket1.size());   // size may have decreased
    	    return answer;
    	}
    }
    return null;
    
   
  }

  /**
   * Returns an iterable collection of all key-value entries of the map.
   *
   * @return iterable collection of the map's entries
   */
  @Override
  public Iterable<Entry<K,V>> entrySet() {
    ArrayList<Entry<K,V>> buffer = new ArrayList<>();
    for (int h=0; h < capacity; h++){
      System.out.print(h + ": ");
      if (table[h] != null) { 
        for (Entry<K,V> entry : table[h].entrySet()){
          System.out.print(entry);	
          buffer.add(entry);
        }
      }
      System.out.println();
    }
    return buffer;
  }
}
