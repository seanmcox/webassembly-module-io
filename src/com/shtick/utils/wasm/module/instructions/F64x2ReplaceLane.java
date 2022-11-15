/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class F64x2ReplaceLane implements LaneInstruction {
	private int laneIndex;

	/**
	 * @param laneIndex
	 */
	public F64x2ReplaceLane(int laneIndex) {
		this.laneIndex = laneIndex;
	}

	@Override
	public int getLaneIndex() {
		return laneIndex;
	}
}
