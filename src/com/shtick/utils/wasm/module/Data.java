/**
 * 
 */
package com.shtick.utils.wasm.module;

/**
 * See: https://webassembly.github.io/spec/core/syntax/modules.html#syntax-data
 * See: https://webassembly.github.io/spec/core/binary/modules.html#binary-data
 * See: https://webassembly.github.io/spec/core/binary/modules.html#data-section
 * 
 * @author seanmcox
 *
 */
public class Data {
	public enum DataMode { PASSIVE, ACTIVE }
	
	private byte[] data;
	private DataMode dataMode;
	private MemoryIndex memoryIndex;
	private Expression expression;
	
	/**
	 * @param data
	 * @param dataMode
	 * @param memoryIndex Ignored/meaningless for passive data mode. However, also, SHOULD always be 0 because the current specification for WebAssembly only allows for one usable memory segment.
	 * @param expression
	 */
	public Data(byte[] data, DataMode dataMode, MemoryIndex memoryIndex, Expression expression) {
		this.data = data;
		this.dataMode = dataMode;
		this.memoryIndex = memoryIndex;
		this.expression = expression;
	}

	/**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * @return the dataMode
	 */
	public DataMode getDataMode() {
		return dataMode;
	}

	/**
	 * @return the memoryIndex
	 */
	public MemoryIndex getMemoryIndex() {
		return memoryIndex;
	}

	/**
	 * @return the expression
	 */
	public Expression getExpression() {
		return expression;
	}
}
