/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class V128Load64Lane extends VectorMemArgLaneInstruction {
	/**
	 * @param align
	 * @param offset
	 * @param laneIndex
	 */
	public V128Load64Lane(long align, long offset, int laneIndex) {
		super(align, offset, laneIndex);
	}
}
