/**
 * 
 */
package com.shtick.utils.wasm.module;

/**
 * See: https://webassembly.github.io/spec/core/binary/modules.html#binary-datacountsec
 * 
 * @author seanmcox
 *
 */
public class DataCountSection extends Section {
	private long dataCount;

	/**
	 * @param dataCount
	 */
	public DataCountSection(long dataCount) {
		this.dataCount = dataCount;
	}

	/**
	 * @return the dataCount
	 */
	public long getDataCount() {
		return dataCount;
	}
}
