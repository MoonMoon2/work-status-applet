package utils;

/**
 * This interface serves to establish all of  methods needed for the
 * helper object responsible for hashing the keys in our hashtable. 
 *
 */
public interface Hasher<T> {
	
	/**
	 * Hashes the String key and produces an appropriate base index
	 * into a table array of capacity N.
	 * 
	 * @param key
	 * @param N - the current table capacity
	 * @return the index between 0 and N-1
	 */
	public int hashKey(T key, int N);

}
