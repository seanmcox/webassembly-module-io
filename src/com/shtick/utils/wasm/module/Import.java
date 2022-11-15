/**
 * 
 */
package com.shtick.utils.wasm.module;

/**
 * See: https://webassembly.github.io/spec/core/binary/modules.html#binary-importsec
 * See: https://webassembly.github.io/spec/core/syntax/modules.html#syntax-import
 * 
 * @author seanmcox
 *
 */
public class Import {
	private String module;
	private String name;
	private ImportDescriptor importDescriptor;
	
	/**
	 * @param module
	 * @param name
	 * @param importDescriptor
	 */
	public Import(String module, String name, ImportDescriptor importDescriptor) {
		this.module = module;
		this.name = name;
		this.importDescriptor = importDescriptor;
	}

	/**
	 * @return the module
	 */
	public String getModule() {
		return module;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the importDescriptor
	 */
	public ImportDescriptor getImportDescriptor() {
		return importDescriptor;
	}
}
