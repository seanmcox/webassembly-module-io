/**
 * 
 */
package com.shtick.utils.wasm.module;

/**
 * @see https://webassembly.github.io/spec/core/syntax/types.html#syntax-limits
 * 
 * @author seanmcox
 *
 */
public class Limits {
	/**
	 *  In units of page sizes (65536 bytes is a page size)
	 */
	private int min;
	/**
	 *  In units of page sizes (65536 bytes is a page size)
	 */
	private int max;
	
	/**
	 * @param min Should be a positive number less than 2^32. The units are page sizes (65536 bytes is a page size),
	 * @param max If there is no upper bound, then this should be -1. The units are page sizes.
	 */
	public Limits(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	/**
	 * @return the min in units of page sizes (65536 bytes is a page size)
	 */
	public int getMin() {
		return min;
	}
	
	/**
	 * @return the max in units of page sizes (65536 bytes is a page size)
	 */
	public int getMax() {
		return max;
	}
}
