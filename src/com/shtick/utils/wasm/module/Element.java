/**
 * 
 */
package com.shtick.utils.wasm.module;

import java.security.InvalidParameterException;
import java.util.Vector;

/**
 * See: https://webassembly.github.io/spec/core/binary/modules.html#binary-elemsec
 * See: https://webassembly.github.io/spec/core/syntax/modules.html#syntax-elemmode
 * See: https://webassembly.github.io/spec/core/syntax/modules.html#syntax-elem
 * 
 * @author seanmcox
 *
 */
public class Element {
	public enum ElementMode { PASSIVE, ACTIVE, DECLARATIVE }
	
	private ReferenceType type;
	private Vector<Expression> init;
	private ElementMode mode;
	private TableIndex table;
	private Expression offset;
	
	/**
	 * @param type
	 * @param init
	 * @param mode
	 * @param table Ignored/meaningless if mode isn't ACTIVE
	 * @param offset Ignored/meaningless if mode isn't ACTIVE
	 */
	public Element(ReferenceType type, Vector<Expression> init, ElementMode mode, TableIndex table, Expression offset) {
		if(type==null)
			throw new InvalidParameterException("Type cannot be null.");
		if(init==null)
			throw new InvalidParameterException("Init cannot be null.");
		if(mode==null)
			throw new InvalidParameterException("Mode cannot be null.");
		this.type = type;
		this.init = init;
		this.mode = mode;
		this.table = table;
		this.offset = offset;
	}

	/**
	 * @return the type
	 */
	public ReferenceType getType() {
		return type;
	}

	/**
	 * @return the init
	 */
	public Vector<Expression> getInit() {
		return init;
	}

	/**
	 * @return the mode
	 */
	public ElementMode getMode() {
		return mode;
	}

	/**
	 * @return the table
	 */
	public TableIndex getTable() {
		return table;
	}

	/**
	 * @return the offset
	 */
	public Expression getOffset() {
		return offset;
	}

	@Override
	public String toString() {
		return "Element [type=" + type + ", init=[" + init.size() + " items], mode=" + mode + ", table=" + table + ", offset=" + offset
				+ "]";
	}
}
