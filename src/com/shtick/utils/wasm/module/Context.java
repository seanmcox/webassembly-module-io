/**
 * 
 */
package com.shtick.utils.wasm.module;

import java.util.HashMap;
import java.util.LinkedList;

import com.shtick.utils.wasm.module.instructions.I32Const;

/**
 * @author seanmcox
 *
 */
public class Context {
	private LinkedList<FunctionDefinition> functionDefinitions = new LinkedList<>();
	private LinkedList<FunctionType> functionTypes = new LinkedList<>();
	private LinkedList<Import> functionImports = new LinkedList<>();
	private LinkedList<Import> otherImports = new LinkedList<>();
	private LinkedList<Memory> memories = new LinkedList<>();
	private LinkedList<Global> globals = new LinkedList<>();
	private LinkedList<Table> tables = new LinkedList<>();
	private LinkedList<Data> data = new LinkedList<>();
	private int numberOfBytesStoredInMemory = 0;

	private HashMap<FunctionType, TypeIndex> typeMap = new HashMap<>();
	private HashMap<FunctionDefinition, Integer> internalFunctionIndexMap = new HashMap<>();
	private HashMap<Import, Integer> importFunctionIndexMap = new HashMap<>();
	private HashMap<Memory, MemoryIndex> memoryIndexMap = new HashMap<>();
	private HashMap<Global, GlobalIndex> globalIndexMap = new HashMap<>();
	private HashMap<Table, TableIndex> tableIndexMap = new HashMap<>();
	
	/**
	 * Adds the function to the context, setting its index value, as well as the index value for its function type (if not already defined).
	 * @param functionDefinition
	 */
	public void addFunctionDefinition(FunctionDefinition functionDefinition) {
		if(getFunctionIndex(functionDefinition)!=null)
			return;
		internalFunctionIndexMap.put(functionDefinition, functionDefinitions.size());
		functionDefinitions.add(functionDefinition);
		getTypeIndex(functionDefinition.getFunctionType());
	}
	
	public Import addFunctionImport(String module, String name, FunctionType functionType) {
		TypeIndex typeIndex = getTypeIndex(functionType);
		Import imp = new Import(module, name, typeIndex);
		importFunctionIndexMap.put(imp, functionImports.size());
		functionImports.add(imp);
		return imp;
	}
	
	public void addNonFunctionImport(Import imp) {
		if(imp.getImportDescriptor() instanceof TypeIndex)
			throw new RuntimeException("Function imports cannot be added through addNonFunctionImport().");
		otherImports.add(imp);
	}

	public void addMemory(Memory memory) {
		if(getMemoryIndex(memory)!=null)
			return;
		memoryIndexMap.put(memory, new MemoryIndex(memories.size()));
		memories.add(memory);
	}

	public void addGlobal(Global global) {
		if(getGlobalIndex(global)!=null)
			return;
		globalIndexMap.put(global, new GlobalIndex(globals.size()));
		globals.add(global);
	}

	public void addTable(Table table) {
		if(getTableIndex(table)!=null)
			return;
		tableIndexMap.put(table, new TableIndex(tables.size()));
		tables.add(table);
	}

	/**
	 * 
	 * @param bytes
	 * @return The index of the first byte within the memory block.
	 */
	public int addData(byte[] bytes) {
		int index = numberOfBytesStoredInMemory;
		LinkedList<Instruction> instructions = new LinkedList<Instruction>();
		instructions.add(new I32Const(index));
		data.add(new Data(bytes, Data.DataMode.ACTIVE, new MemoryIndex(0), new Expression(instructions)));
		return index;
	}
	
	
	/**
	 * @return the functionDefinitions
	 */
	public LinkedList<FunctionDefinition> getFunctionDefinitions() {
		return functionDefinitions;
	}

	/**
	 * @return the functionTypes
	 */
	public LinkedList<FunctionType> getFunctionTypes() {
		return functionTypes;
	}

	/**
	 * 
	 * @return the imports
	 */
	public LinkedList<Import> getImports() {
		LinkedList<Import> retval = new LinkedList<>(functionImports);
		retval.addAll(otherImports);
		return retval;
	}

	/**
	 * @return the memories
	 */
	public LinkedList<Memory> getMemories() {
		return memories;
	}

	/**
	 * @return the globals
	 */
	public LinkedList<Global> getGlobals() {
		return globals;
	}

	/**
	 * @return the tables
	 */
	public LinkedList<Table> getTables() {
		return tables;
	}
	
	/**
	 * @return the data
	 */
	public LinkedList<Data> getData() {
		return data;
	}

	public FunctionIndex getFunctionIndex(FunctionDefinition functionDefinition) {
		Integer baseIndex = internalFunctionIndexMap.get(functionDefinition);
		if(baseIndex==null)
			return null;
		return new FunctionIndex(baseIndex+functionImports.size());
	}
	
	public FunctionIndex getFunctionIndex(Import imp) {
		return new FunctionIndex(importFunctionIndexMap.get(imp));
	}
	
	/**
	 * 
	 * @param functionType
	 * @return The typeIndex for the functionType. If there is no existing index for the FunctionType, then one is created.
	 *         Hence, this function should not be called unless the function type is one that is intended to be used.
	 */
	public TypeIndex getTypeIndex(FunctionType functionType) {
		if(!typeMap.containsKey(functionType)) {
			typeMap.put(functionType, new TypeIndex(functionTypes.size()));
			functionTypes.add(functionType);
		}
		return typeMap.get(functionType);
	}
	
	public MemoryIndex getMemoryIndex(Memory memory) {
		return memoryIndexMap.get(memory);
	}
	
	public GlobalIndex getGlobalIndex(Global global) {
		return globalIndexMap.get(global);
	}
	
	public TableIndex getTableIndex(Table table) {
		return tableIndexMap.get(table);
	}
}
