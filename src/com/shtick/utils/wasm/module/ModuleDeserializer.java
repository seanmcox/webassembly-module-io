/**
 * 
 */
package com.shtick.utils.wasm.module;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Vector;

import com.shtick.utils.wasm.module.Data.DataMode;
import com.shtick.utils.wasm.module.instructions.Nop;
import com.shtick.utils.wasm.module.instructions.Unreachable;

/**
 * @author seanmcox
 *
 */
public class ModuleDeserializer {
	static final int CUSTOM_SECTION_ORDER = -1;
	static final int TYPE_SECTION_ORDER = 1;
	static final int IMPORT_SECTION_ORDER = 2;
	static final int FUNCTION_SECTION_ORDER = 3;
	static final int TABLE_SECTION_ORDER = 4;
	static final int MEMORY_SECTION_ORDER = 5;
	static final int GLOBAL_SECTION_ORDER = 6;
	static final int EXPORT_SECTION_ORDER = 7;
	static final int START_SECTION_ORDER = 8;
	static final int ELEMENT_SECTION_ORDER = 9;
	static final int DATA_COUNT_SECTION_ORDER = 10;
	static final int CODE_SECTION_ORDER = 11;
	static final int DATA_SECTION_ORDER = 12;

	public Module readModule(InputStream in) throws IOException {
		byte[] magic = readNBytes(4,in);
		byte[] version = readNBytes(4,in);
		if(!Arrays.equals(magic, ModuleSerializer.MAGIC_NUMBER))
			throw new IOException("Magic number not found in data.");
		if(!Arrays.equals(version, ModuleSerializer.VERSION_NUMBER))
			throw new IOException("Supported version number not found in data.");
		
		LinkedList<CustomSection> typeMetaSections = new LinkedList<>();
		TypeSection typeSection = null;
		LinkedList<CustomSection> importMetaSections = new LinkedList<>();
		ImportSection importSection = null;
		LinkedList<CustomSection> functionMetaSections = new LinkedList<>();
		FunctionSection functionSection = null;
		LinkedList<CustomSection> tableMetaSections = new LinkedList<>();
		TableSection tableSection = null;
		LinkedList<CustomSection> memoryMetaSections = new LinkedList<>();
		MemorySection memorySection = null;
		LinkedList<CustomSection> globalMetaSections = new LinkedList<>();
		GlobalSection globalSection = null;
		LinkedList<CustomSection> exportMetaSections = new LinkedList<>();
		ExportSection exportSection = null;
		LinkedList<CustomSection> startMetaSections = new LinkedList<>();
		StartSection startSection = null;
		LinkedList<CustomSection> elementMetaSections = new LinkedList<>();
		ElementSection elementSection = null;
		LinkedList<CustomSection> dataCountMetaSections = new LinkedList<>();
		DataCountSection dataCountSection = null;
		LinkedList<CustomSection> codeMetaSections = new LinkedList<>();
		CodeSection codeSection = null;
		LinkedList<CustomSection> dataMetaSections = new LinkedList<>();
		DataSection dataSection = null;
		LinkedList<CustomSection> moduleMetaSections = new LinkedList<>();
		
		LinkedList<CustomSection> currentMetaSections = new LinkedList<>();
		int currentOrderPosition = 0;
		while(true) {
			Section section = readSection(in);
			if(section==null) {
				moduleMetaSections = currentMetaSections;
				break;
			}
			if(section instanceof CustomSection) {
				currentMetaSections.add((CustomSection)section);
				continue;
			}
			int sectionOrder = -1;
			if(section instanceof TypeSection) {
				sectionOrder = TYPE_SECTION_ORDER;
				typeSection = (TypeSection)section;
				typeMetaSections = currentMetaSections;
			}
			else if(section instanceof ImportSection) {
				sectionOrder = IMPORT_SECTION_ORDER;
				importSection = (ImportSection)section;
				importMetaSections = currentMetaSections;
			}
			else if(section instanceof FunctionSection) {
				sectionOrder = FUNCTION_SECTION_ORDER;
				functionSection = (FunctionSection)section;
				functionMetaSections = currentMetaSections;
			}
			else if(section instanceof TableSection) {
				sectionOrder = TABLE_SECTION_ORDER;
				tableSection = (TableSection)section;
				tableMetaSections = currentMetaSections;
			}
			else if(section instanceof MemorySection) {
				sectionOrder = MEMORY_SECTION_ORDER;
				memorySection = (MemorySection)section;
				memoryMetaSections = currentMetaSections;
			}
			else if(section instanceof GlobalSection) {
				sectionOrder = GLOBAL_SECTION_ORDER;
				globalSection = (GlobalSection)section;
				globalMetaSections = currentMetaSections;
			}
			else if(section instanceof ExportSection) {
				sectionOrder = EXPORT_SECTION_ORDER;
				exportSection = (ExportSection)section;
				exportMetaSections = currentMetaSections;
			}
			else if(section instanceof StartSection) {
				sectionOrder = START_SECTION_ORDER;
				startSection = (StartSection)section;
				startMetaSections = currentMetaSections;
			}
			else if(section instanceof ElementSection) {
				sectionOrder = ELEMENT_SECTION_ORDER;
				elementSection = (ElementSection)section;
				elementMetaSections = currentMetaSections;
			}
			else if(section instanceof DataCountSection) {
				sectionOrder = DATA_COUNT_SECTION_ORDER;
				dataCountSection = (DataCountSection)section;
				dataCountMetaSections = currentMetaSections;
			}
			else if(section instanceof CodeSection) {
				sectionOrder = CODE_SECTION_ORDER;
				codeSection = (CodeSection)section;
				codeMetaSections = currentMetaSections;
			}
			else if(section instanceof DataSection) {
				sectionOrder = DATA_SECTION_ORDER;
				dataSection = (DataSection)section;
				dataMetaSections = currentMetaSections;
			}
			if(sectionOrder<=currentOrderPosition)
				throw new IOException("Sections out of order.");
			currentOrderPosition = sectionOrder;
			currentMetaSections = new LinkedList<>();
		}
		return new Module(
				typeMetaSections, typeSection, importMetaSections, importSection, functionMetaSections, functionSection, tableMetaSections,
				tableSection, memoryMetaSections, memorySection, globalMetaSections, globalSection, exportMetaSections, exportSection,
				startMetaSections, startSection, elementMetaSections, elementSection, dataCountMetaSections, dataCountSection, codeMetaSections,
				codeSection, dataMetaSections, dataSection, moduleMetaSections
		);
	}
	
	public Section readSection(InputStream in) throws IOException {
		int sectionID = in.read();
		if(sectionID<0)
			return null;
		switch(sectionID) {
		case ModuleSerializer.CUSTOM_SECTION_ID:
			return readCustomSection(in);
		case ModuleSerializer.TYPE_SECTION_ID:
			return readTypeSection(in);
		case ModuleSerializer.DATA_COUNT_SECTION_ID:
			return readDataCountSection(in);
		case ModuleSerializer.DATA_SECTION_ID:
			return readDataSection(in);
		// TODO
		default:
			throw new IOException("Found unrecognized Section ID.");
		}
	}
	
	public CustomSection readCustomSection(InputStream in) throws IOException {
		long dataSize = readUnsignedLEB128(in);
		return new CustomSection(readNBytes((int)dataSize,in));
	}
	
	public TypeSection readTypeSection(InputStream in) throws IOException {
		long dataSize = readUnsignedLEB128(in);
		byte[] bytes = readNBytes((int)dataSize,in);
		ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
		return new TypeSection(readVector(ModuleDeserializer::readFunctype, bin));
	}
	
	public DataCountSection readDataCountSection(InputStream in) throws IOException {
		long dataSize = readUnsignedLEB128(in);
		long dataCount = readUnsignedLEB128(in);
		if(ModuleSerializer.getUnsignedLEB128Size(dataCount)!=dataSize)
			throw new IOException("Data Count secton size does not match length of the serialized data count value.");
		return new DataCountSection(dataCount);
	}
	
	public DataSection readDataSection(InputStream in) throws IOException {
		long dataSize = readUnsignedLEB128(in);
		byte[] bytes = readNBytes((int)dataSize,in);
		ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
		return new DataSection(readVector(ModuleDeserializer::readData, bin));
	}

	/********************************************
	 * 
	 * Routines for writing sub-section elements
	 * 
	 ********************************************/

	private static FunctionType readFunctype(InputStream in) throws IOException{
		int b = in.read();
		if(b<0)
			throw new IOException("End of input found when trying to read Functype.");
		if(b!=0x060)
			throw new IOException("Functype marker, 0x60 expected, but 0x"+Integer.toHexString(b)+" found.");
		ResultType parameterType = readResultType(in);
		ResultType resultType = readResultType(in);
		return new FunctionType(parameterType, resultType);
	}
	
	/**
	 * See: https://webassembly.github.io/spec/core/binary/types.html#binary-resulttype
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static ResultType readResultType(InputStream in) throws IOException{
		return new ResultType(readVector(ModuleDeserializer::readValueType, in));
	}

	/**
	 * See: https://webassembly.github.io/spec/core/binary/types.html#binary-valtype
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static ValueType readValueType(InputStream in) throws IOException{
		int type = in.read();
		if(type<0)
			throw new IOException("End of input found when trying to read ValueType.");
		for(ValueType v:ValueType.values())
			if(v.getType()==type)
				return v;
		throw new IOException("Unrecognized value type found: 0x"+Integer.toHexString(type));
	}

	private static Data readData(InputStream in) throws IOException{
		int encoding = in.read();
		if(encoding<0)
			throw new IOException("End of input found when trying to read Data encoding.");
		if(encoding>2)
			throw new IOException("Unrecognized Data encoding marker: "+encoding);
		int memoryIndex = 0;
		Expression expression = null;
		if(encoding!=1) {
			if(encoding==2) {
				memoryIndex = in.read();
				if(memoryIndex<0)
					throw new IOException("End of input found when trying to read Data memory index.");
			}
			expression = readExpression(in);
		}
		byte[] data = readByteVector(in);
		return new Data(data, (encoding==1)?DataMode.PASSIVE:DataMode.ACTIVE, new MemoryIndex(memoryIndex), expression);
	}

	/**
	 * https://webassembly.github.io/spec/core/binary/instructions.html#binary-expr
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static Expression readExpression(InputStream in) throws IOException{
		Instruction instruction = readInstruction(in);
		LinkedList<Instruction> instructions = new LinkedList<>();
		while(instruction!=null) {
			instructions.add(instruction);
			instruction = readInstruction(in);
		}
		return new Expression(instructions);
	}

	/**
	 * See: https://webassembly.github.io/spec/core/binary/instructions.html#binary-instr
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static Instruction readInstruction(InputStream in) throws IOException{
		int identifier = in.read();
		if(identifier<0)
			throw new IOException("End of stream found when trying to identify Instruction.");
		if(identifier==0x0B)
			return null;
		if(identifier==0)
			return new Unreachable();
		if(identifier==1)
			return new Nop();
		// TODO
		throw new IOException("Unrecoginized instruction identifier found.");
	}
	
	private static <T> Vector<T> readVector(ElementReader<T> elementReader, InputStream in) throws IOException{
		long count = readUnsignedLEB128(in);
		Vector<T> retval = new Vector<T>((int)count);
		for(int i = 0; i<count; i++)
			retval.add(elementReader.read(in));
		return retval;
	}
	
	private static byte[] readByteVector(InputStream in) throws IOException{
		long count = readUnsignedLEB128(in);
		return readNBytes((int)count, in);
	}

	/**
	 * 
	 * @param size
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static byte[] readNBytes(int size, InputStream in) throws IOException {
		byte[] retval = new byte[size];
		int bytesRead = 0;
		int r;
		while(bytesRead<size) {
			r = in.read(retval, bytesRead, size - bytesRead);
			if(r<0)
				throw new IOException("End of stream encountered while reading array of bytes.");
			bytesRead+=r;
		}
		return retval;
	}
	
	/**
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static long readUnsignedLEB128(InputStream in) throws IOException {
		long b_in;
		int shifts = 0;
		long retval = 0;
		do {
			if(shifts>=64)
				throw new IOException("Overly large number encountered while reading unsigned LEB128.");
			b_in = in.read();
			if(b_in<0)
				throw new IOException("End of stream encountered while reading unsigned LEB128.");
			retval |= (b_in&0x07FL) << shifts;
			shifts+=7;
		} while(b_in>=0x080);
		return retval;
	}

	
	interface ElementReader<ER>{
		ER read(InputStream out) throws IOException;
	};
}
