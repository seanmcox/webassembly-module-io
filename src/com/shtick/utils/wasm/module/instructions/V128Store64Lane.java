/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class V128Store64Lane extends VectorMemArgLaneInstruction {
	/**
	 * @param align
	 * @param offset
	 * @param laneIndex
	 */
	public V128Store64Lane(long align, long offset, int laneIndex) {
		super(align, offset, laneIndex);
	}
}
