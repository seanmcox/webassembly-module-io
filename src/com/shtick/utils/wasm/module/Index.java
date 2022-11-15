/**
 * 
 */
package com.shtick.utils.wasm.module;

/**
 * See: https://webassembly.github.io/spec/core/binary/modules.html#binary-typeidx
 * 
 * @author seanmcox
 *
 */
public class Index implements ImportDescriptor{
	private long index;

	/**
	 * @param index
	 */
	public Index(long index) {
		this.index = index;
	}

	/**
	 * @return the index
	 */
	public long getIndex() {
		return index;
	}
}
