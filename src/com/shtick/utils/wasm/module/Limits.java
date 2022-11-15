/**
 * 
 */
package com.shtick.utils.wasm.module;

/**
 * @author seanmcox
 *
 */
public class Limits {
	private int min;
	private int max;
	
	/**
	 * @param min Should be a positive number less than 2^32.
	 * @param max If there is no upper bound, then this should be -1.
	 */
	public Limits(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	/**
	 * @return the min
	 */
	public int getMin() {
		return min;
	}
	
	/**
	 * @return the max
	 */
	public int getMax() {
		return max;
	}
}
