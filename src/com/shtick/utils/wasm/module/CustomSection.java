/**
 * 
 */
package com.shtick.utils.wasm.module;

/**
 * @author seanmcox
 *
 */
public class CustomSection extends Section {
	private byte[] data; 

	public CustomSection(byte[] data) {
		this.data = data;
	}
	
	public byte[] getData() {
		return data;
	}
}
