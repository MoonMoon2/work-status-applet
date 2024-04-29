package utils;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

public class LookupSet<T, E> {
	
	public static final Hasher<Integer> DEFAULT_INTEGER_HASHER = new Hasher<Integer>() {

		@Override
		public int hashKey(Integer key, int N) {
			return key % N;
		}
	};
	
	public static final Hasher<String> DEFAULT_STRING_HASHER = new Hasher<String>() {

		public static final int Z = 33;
		
		@Override
		public int hashKey(String key, int N) {
			if (key == null) { return 0; }				// protection for null key
			
			int[] valueArray = key.chars().toArray();	// chars returns an intstream of the key, and we cut it into an array
			
			int sum = 0;
			for (int i = valueArray.length-1; i >=0; i--) {		// from the last element to the first
				sum = (sum*Z + valueArray[i]) % N;		// multiply the previous term by Z and add the current value, then compress it down to N
			}
			
			return sum;
		}
		
	};
	
	private final int INITIAL_N = 23;  // initial size of hashtable
	
	protected class Entry {
		T entryId;
		E entryValue;

		public Entry(T entryId, E entryValue) {
			super();
			this.entryId = entryId;
			this.entryValue = entryValue;
		}
		
		public T getEntryId() {
			return entryId;
		}

		public void setEntryId(T entryId) {
			this.entryId = entryId;
		}

		public E getEntryValue() {
			return entryValue;
		}

		public void setEntryValue(E entryValue) {
			this.entryValue = entryValue;
		}
	}
	
	protected List<Entry> table;
	protected Set<T> keys;
	protected Set<E> values;
	protected Hasher<T> hasher;
	
	
	private int numValues;
	
	protected static double MAX_LOAD_FACTOR = 0.6;
	
	protected final Entry VACATED_NODE = new Entry(null, null);
	
	public LookupSet(Hasher<T> hashHelper) {
		this.table = new ArrayList<Entry>(INITIAL_N);
		this.hasher = hashHelper;
		this.numValues = 0;
	}
	
	public int size() {
		return numValues;
	}
	
	public boolean isEmpty() {
		return numValues == 0;
	}
	
	
	
	public List<E> values() {
		if (isEmpty()) { return new ArrayList<>();}
		
		return new ArrayList<>(values);
	}

	/**
	 * Returns an array of all of the keys for values in the Hashtable using
	 * a private method which gathers all of the entries in the table
	 */
	public List<T> keys() {
		if (isEmpty()) { return new ArrayList<>(); }			// if the array is empty

		return new ArrayList<>(keys);
	}

	/**
	 * Private method to collect an array of all of the entries within our hash table
	 * 
	 * 
	 * @return An array of all of the valid entries from the table
	 */
	private ArrayList<Entry> entries() {
		ArrayList<Entry> entries = new ArrayList<Entry>((int)(MAX_LOAD_FACTOR+0.1)*table.size());		// make an array slightly larger than the maximum number of values we could potentially need

		int slot = -1;
		for (Entry e : table) {
			if (e != null && e.getEntryId() != null) {				// check if there is a key (not a vacated slot)
				slot++;
				entries.add(slot, e);								// if the entry is not a vacated slot, 
			}
		}

		return entries;
	}
	
	public E contains(E object) {
		if (values.contains(object)) {
			return object;
		}
		
		return null;
	}

	public E get(T key) {
		if (isEmpty()) { return null; }							// just in case the array is empty

		int index = indexOfKey(key, true);

		if (index != -1) {
			return table.get(index).getEntryValue();
		} else {
			return null;
		}
	}
	

	/**
	 * Checks to see if table needs expanded and does so first.
	 * <p>
	 * Locates where the supplied key should be stored.  If 
	 * an entry with the same key already exists, then replaces
	 * the associated entry value.  If another entry with a different
	 * key occupies this slot (a collision), then uses  
	 * probing until an empty or available (VACATED) slot is found.  
	 * Creates an stores an new entry at the at the desired slot.
	 * </p>
	 * @param key
	 * @param value
	 * @return the entry value added if successful or null if not
	 */
	public Object put(T key, E value) {
		/*
		 * Resizing the array to a new prime number with N % 4 == 3 and re-hashing all of the values in it
		 */
		if ((double)(numValues) / (double)(table.size()) >= MAX_LOAD_FACTOR) {
			expand();
		}
		
		keys.add(key);
		values.add(value);

		table.set(indexOfKey(key, false), new Entry(key, value));
		
		numValues++;
		return value;

	}

	/**
	 * Finds the associated entry and removes it  
	 * from the table by replacing the entry with the VACATED constant. 
	 * 
	 * @param key
	 * @return the entry value removed from the hash table if successful
	 * or null otherwise
	 */
	public E remove(T key) {
		if (isEmpty()) { return null; }			// just in case the array is empty

		int index = indexOfKey(key, true);

		E value;

		if (index != -1) {
			value = table.get(index).getEntryValue();	// store the value for export
		} else {
			return null;
		}
		
		keys.remove(key);
		values.remove(value);

		numValues--;

		table.set(index, VACATED_NODE);		// set the node as vacated to deleted it

		return value;
	}

	/**
	 * Finds the index at which a given key should exist. Uses a boolean
	 * to switch between whether it should find the item or if it is placing 
	 * the item in the array.
	 * 
	 * @param key
	 * @param find
	 * @return
	 */
	private int indexOfKey(T key, boolean find) {
		int origIdx = hasher.hashKey(key, table.size());		// 	index of our initial guess

		int numAttempts = 0;									// counter for how many times we have checked an index for the value
		int idx = origIdx;										// initialize an index holder 

		/*
		 * Uses the find boolean to determine whether we are inserting or finding a value
		 * 
		 * If we are inserting, so find = False, we can insert into vacated nodes
		 * 
		 * If we are finding, so find = True, we search until we find a blank slot
		 */
		while (numAttempts < table.size()) {
			if (table.get(idx) == null) {				// check if the current index has no node (vacated counts as a node)
				return find ? -1 : idx;									// if there is no node, we either found an empty slot (idx), or the search failed (-1)
			} else if (!find && table.get(idx).getEntryId() == VACATED_NODE.getEntryId()) {			// locate a vacated node, and not trying to find a value
				return idx;																// return the index for insertion
			} else if(table.get(idx).getEntryId() != VACATED_NODE.getEntryId() && table.get(idx).getEntryId().equals(key)) {		// check the current index for the key
				return idx;										// if found, return the index - updates matching items and inserts new items
			}

			numAttempts++;										// another attempt made
			idx = collisionAddress(origIdx, numAttempts);		// update the index with a new quadratic collision address
		}
		
		return -1;			// if the search failed (too many iterations) return -1 for failure
	}

	/**
	 * Small helper algorithm to find the address of a collision using our Quadratic Probing function
	 * 
	 * <p>
	 * idx = (idx + (-1)^(n+1)* i^2 + N) % N
	 * </p>
	 * 
	 * 
	 * 
	 * @param idx current base index (no probing)
	 * @param numAttempts how many addresses we've already tried
	 * @return a new index to try, sized down to our table length, using quadratic probing
	 */
	private int collisionAddress(int idx, int numAttempts) {

		return (int)(idx + Math.pow(-1, numAttempts+1)*Math.pow(numAttempts, 2) + table.size()) % table.size();		// idx = (N + idx + (-1)^(i+1)*i^2) % N

	}

	/**
	 * Sets all table entry handles in the hash table to null.  An empty
	 * table consists of nothing but null handles. 
	 */
	public void clear() {
		this.table = new ArrayList<Entry>(INITIAL_N);
		numValues = 0;
	}

	/**
	 * Expands the table to the next appropriate size table array
	 * and rehashes all existing entries into the new table.  This is 
	 * called by the put algorithm when needed to keep the hashtable 
	 * efficient.
	 */
	public void expand() {
		ArrayList<Entry> entries = entries();			// grabs all the current entries

		int newLength = table.size() + 4;

		while (!isPrime(newLength)) {			// tests next values which satisfy num%4 = 3 until we find a prime one
			newLength += 4;
		}

		table = new ArrayList<>(newLength);			// creates a new table with the prime length we found

		for (Entry e : entries) {
			table.set(indexOfKey(e.getEntryId(), false), e);
		}

	}

	/**
	 * Checks if a given number is prime in O(sqrt(n))
	 * 
	 * @param num number to be checked
	 * @return whether or not the number is prime (True = prime)
	 */
	private boolean isPrime(int num) {
		int n = 2;
		while (n <= Math.sqrt(num)) {
			if (num%n == 0) {
				return false;
			}
			n++;
		}
		return true;
	}
	
	
	
	
	
	

}
