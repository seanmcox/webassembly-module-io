/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

import com.shtick.utils.wasm.module.Instruction;

/**
 * 1) Assert: due to validation, a value of value type v128 is on the top of the stack.
 * 2) Pop the value v128.const c1 from the stack.
 * 3) Let i be the result of computing ine128(c1,0).
 * 4) Push the value i32.const i onto the stack.
 * 
 * @author seanmcox
 *
 */
public class V128AnyTrue implements Instruction {

}
