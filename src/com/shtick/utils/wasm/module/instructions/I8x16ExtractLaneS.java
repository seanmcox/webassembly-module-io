/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

/**
 * @author seanmcox
 *
 */
public class I8x16ExtractLaneS implements LaneInstruction {
	private int laneIndex;

	/**
	 * @param laneIndex
	 */
	public I8x16ExtractLaneS(int laneIndex) {
		this.laneIndex = laneIndex;
	}

	@Override
	public int getLaneIndex() {
		return laneIndex;
	}
}
