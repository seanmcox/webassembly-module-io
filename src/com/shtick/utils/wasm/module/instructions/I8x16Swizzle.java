/**
 * 
 */
package com.shtick.utils.wasm.module.instructions;

import com.shtick.utils.wasm.module.Instruction;

/**
 * 1) Assert: due to validation, two values of value type v128 are on the top of the stack.
 * 2) Pop the value v128.const c2 from the stack.
 * 3) Let i* be the sequence lanesi8x16(c2).
 * 4) Pop the value v128.const c1 from the stack.

from the stack.

Let
be the sequence

.

Let
be the concatenation of the two sequences

Let
be the result of

.

Push the value
onto the stack.

 * @author seanmcox
 *
 */
public class I8x16Swizzle implements Instruction {

}
