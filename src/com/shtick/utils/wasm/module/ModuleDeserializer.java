/**
 * 
 */
package com.shtick.utils.wasm.module;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Vector;

import com.shtick.utils.wasm.module.Data.DataMode;
import com.shtick.utils.wasm.module.Element.ElementMode;
import com.shtick.utils.wasm.module.instructions.*;

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

	/**
	 * See: https://webassembly.github.io/spec/core/binary/modules.html#binary-module
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static Module readModule(InputStream in) throws IOException {
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
	
	private static Section readSection(InputStream in) throws IOException {
		int sectionID = in.read();
		if(sectionID<0)
			return null;
		switch(sectionID) {
		case ModuleSerializer.CUSTOM_SECTION_ID:
			return readCustomSection(in);
		case ModuleSerializer.TYPE_SECTION_ID:
			return readTypeSection(in);
		case ModuleSerializer.IMPORT_SECTION_ID:
			return readImportSection(in);
		case ModuleSerializer.FUNCTION_SECTION_ID:
			return readFunctionSection(in);
		case ModuleSerializer.TABLE_SECTION_ID:
			return readTableSection(in);
		case ModuleSerializer.MEMORY_SECTION_ID:
			return readMemorySection(in);
		case ModuleSerializer.GLOBAL_SECTION_ID:
			return readGlobalSection(in);
		case ModuleSerializer.EXPORT_SECTION_ID:
			return readExportSection(in);
		case ModuleSerializer.START_SECTION_ID:
			return readStartSection(in);
		case ModuleSerializer.ELEMENT_SECTION_ID:
			return readElementSection(in);
		case ModuleSerializer.DATA_COUNT_SECTION_ID:
			return readDataCountSection(in);
		case ModuleSerializer.CODE_SECTION_ID:
			return readCodeSection(in);
		case ModuleSerializer.DATA_SECTION_ID:
			return readDataSection(in);
		default:
			throw new IOException("Found unrecognized Section ID.");
		}
	}
	
	/**
	 * See: https://webassembly.github.io/spec/core/binary/modules.html#binary-customsec
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static CustomSection readCustomSection(InputStream in) throws IOException {
		long dataSize = readUnsignedLEB128(in);
		return new CustomSection(readNBytes((int)dataSize,in));
	}
	
	/**
	 * See: https://webassembly.github.io/spec/core/binary/modules.html#binary-typesec
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static TypeSection readTypeSection(InputStream in) throws IOException {
		long dataSize = readUnsignedLEB128(in);
		byte[] bytes = readNBytes((int)dataSize,in);
		ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
		TypeSection retval = new TypeSection(readVector(ModuleDeserializer::readFunctype, bin));
		if(bin.available()!=0)
			throw new IOException("Leftover bytes found in Code.");
		return retval;
	}
	
	/**
	 * See: https://webassembly.github.io/spec/core/binary/modules.html#binary-importsec
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static ImportSection readImportSection(InputStream in) throws IOException {
		long dataSize = readUnsignedLEB128(in);
		byte[] bytes = readNBytes((int)dataSize,in);
		ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
		ImportSection retval = new ImportSection(readVector(ModuleDeserializer::readImport, bin));
		if(bin.available()!=0)
			throw new IOException("Leftover bytes found in Code.");
		return retval;
	}
	
	private static FunctionSection readFunctionSection(InputStream in) throws IOException {
		long dataSize = readUnsignedLEB128(in);
		byte[] bytes = readNBytes((int)dataSize,in);
		ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
		FunctionSection retval = new FunctionSection(readVector(ModuleDeserializer::readTypeIndex, bin));
		if(bin.available()!=0)
			throw new IOException("Leftover bytes found in Code.");
		return retval;
	}
	
	private static TableSection readTableSection(InputStream in) throws IOException {
		long dataSize = readUnsignedLEB128(in);
		byte[] bytes = readNBytes((int)dataSize,in);
		ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
		TableSection retval = new TableSection(readVector(ModuleDeserializer::readTable, bin));
		if(bin.available()!=0)
			throw new IOException("Leftover bytes found in Code.");
		return retval;
	}
	
	private static MemorySection readMemorySection(InputStream in) throws IOException {
		long dataSize = readUnsignedLEB128(in);
		byte[] bytes = readNBytes((int)dataSize,in);
		ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
		MemorySection retval = new MemorySection(readVector(ModuleDeserializer::readMemory, bin));
		if(bin.available()!=0)
			throw new IOException("Leftover bytes found in Code.");
		return retval;
	}
	
	private static GlobalSection readGlobalSection(InputStream in) throws IOException {
		long dataSize = readUnsignedLEB128(in);
		byte[] bytes = readNBytes((int)dataSize,in);
		ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
		GlobalSection retval = new GlobalSection(readVector(ModuleDeserializer::readGlobal, bin));
		if(bin.available()!=0)
			throw new IOException("Leftover bytes found in Code.");
		return retval;
	}

	private static ExportSection readExportSection(InputStream in) throws IOException {
		long dataSize = readUnsignedLEB128(in);
		byte[] bytes = readNBytes((int)dataSize,in);
		ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
		ExportSection retval = new ExportSection(readVector(ModuleDeserializer::readExport, bin));
		if(bin.available()!=0)
			throw new IOException("Leftover bytes found in Code.");
		return retval;
	}

	private static StartSection readStartSection(InputStream in) throws IOException {
		long dataSize = readUnsignedLEB128(in);
		byte[] bytes = readNBytes((int)dataSize,in);
		ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
		StartSection retval = new StartSection(readStart(bin));
		if(bin.available()!=0)
			throw new IOException("Leftover bytes found in Code.");
		return retval;
	}

	/**
	 * See: https://webassembly.github.io/spec/core/binary/modules.html#binary-elemsec
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static ElementSection readElementSection(InputStream in) throws IOException {
		long dataSize = readUnsignedLEB128(in);
		byte[] bytes = readNBytes((int)dataSize,in);
		ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
		ElementSection retval = new ElementSection(readVector(ModuleDeserializer::readElement, bin));
		if(bin.available()!=0)
			throw new IOException("Leftover bytes found in Code.");
		return retval;
	}

	private static DataCountSection readDataCountSection(InputStream in) throws IOException {
		long dataSize = readUnsignedLEB128(in);
		long dataCount = readUnsignedLEB128(in);
		if(ModuleSerializer.getUnsignedLEB128Size(dataCount)!=dataSize)
			throw new IOException("Data Count secton size does not match length of the serialized data count value.");
		return new DataCountSection(dataCount);
	}
	
	private static DataSection readDataSection(InputStream in) throws IOException {
		long dataSize = readUnsignedLEB128(in);
		byte[] bytes = readNBytes((int)dataSize,in);
		ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
		DataSection retval = new DataSection(readVector(ModuleDeserializer::readData, bin));
		if(bin.available()!=0)
			throw new IOException("Leftover bytes found in Code.");
		return retval;
	}
	
	/**
	 * See: https://webassembly.github.io/spec/core/binary/modules.html#binary-codesec
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static CodeSection readCodeSection(InputStream in) throws IOException {
		long dataSize = readUnsignedLEB128(in);
		byte[] bytes = readNBytes((int)dataSize,in);
		ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
		CodeSection retval = new CodeSection(readVector(ModuleDeserializer::readCode, bin));
		if(bin.available()!=0)
			throw new IOException("Leftover bytes found in Code.");
		return retval;
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
	 * See: https://webassembly.github.io/spec/core/binary/modules.html#binary-importsec
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static Import readImport(InputStream in) throws IOException{
		String module = readName(in);
		String name = readName(in);
		ImportDescriptor importDescriptor;
		int descriptorType = in.read();
		if(descriptorType<0)
			throw new IOException("End of input found when trying to read Import.");
		switch(descriptorType){
		case 0:
			importDescriptor = new TypeIndex(readUnsignedLEB128(in));
			break;
		case 1:
			importDescriptor = readTableType(in);
			break;
		case 2:
			importDescriptor = readMemoryType(in);
			break;
		case 3:
			importDescriptor = readGlobalType(in);
			break;
		default:
			throw new IOException("Unrecognized decriptor type marker in Import: "+descriptorType);
		}
		return new Import(module, name, importDescriptor);
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
	 * See: https://webassembly.github.io/spec/core/binary/values.html#binary-name
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static String readName(InputStream in) throws IOException{
		long length = readUnsignedLEB128(in);
		byte[] bytes = in.readNBytes((int)length);
		if(bytes.length<length)
			throw new IOException("Unexpected end of stream encountered while reading name.");
		return new String(bytes, "UTF8");
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

	/**
	 * See: https://webassembly.github.io/spec/core/binary/types.html#binary-tabletype
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static TableType readTableType(InputStream in) throws IOException{
		return new TableType(readReferenceType(in), readLimits(in));
	}

	/**
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static MemoryType readMemoryType(InputStream in) throws IOException{
		return new MemoryType(readLimits(in));
	}
	
	private static Limits readLimits(InputStream in) throws IOException{
		int limitType = in.read();
		if(limitType<0)
			throw new IOException("End of input found when trying to read Limits.");
		if(limitType==0)
			return new Limits((int)readUnsignedLEB128(in),-1);
		if(limitType==1)
			return new Limits((int)readUnsignedLEB128(in),(int)readUnsignedLEB128(in));
		throw new IOException("Unexpected Limits type found when reading Limits: "+limitType);
	}
	
	private static ExportDescriptor readExportDescriptor(InputStream in) throws IOException{
		int type = in.read();
		if(type<0)
			throw new IOException("End of input found when trying to read ExportDescriptor.");
		if(type==0)
			return new FunctionIndex(readUnsignedLEB128(in));
		if(type==1)
			return new TableIndex(readUnsignedLEB128(in));
		if(type==2)
			return new MemoryIndex(readUnsignedLEB128(in));
		if(type==3)
			return new GlobalIndex(readUnsignedLEB128(in));
		throw new IOException("Unrecognized ExportDescriptor type found: "+type);
	}

	/**
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static GlobalType readGlobalType(InputStream in) throws IOException{
		ValueType valueType = readValueType(in);
		int mutability = in.read();
		if(mutability<0)
			throw new IOException("End of input found when trying to read GlobalType.");
		if(mutability>1)
			throw new IOException("Unrecognized mutability found when reading GlobalType: "+mutability);
		return new GlobalType(valueType, (mutability==0)?Mutability.CONSTANT:Mutability.VARIABLE);
	}

	private static ReferenceType readReferenceType(InputStream in) throws IOException{
		int type = in.read();
		if(type<0)
			throw new IOException("End of input found when trying to read ReferenceType.");
		for(ReferenceType v:ReferenceType.values())
			if(v.getType()==type)
				return v;
		throw new IOException("Unrecognized reference type found: 0x"+Integer.toHexString(type));
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
	 * See: https://webassembly.github.io/spec/core/binary/modules.html#binary-code
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static Code readCode(InputStream in) throws IOException{
		long dataSize = readUnsignedLEB128(in);
		byte[] bytes = readNBytes((int)dataSize,in);
		ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
		Code retval = new Code(readVector(ModuleDeserializer::readLocals,bin), readExpression(bin));
		if(bin.available()!=0)
			throw new IOException("Leftover bytes cound in Code.");
		return retval;
	}

	private static Locals readLocals(InputStream in) throws IOException{
		return new Locals((int)readUnsignedLEB128(in), readValueType(in));
	}

	/**
	 * https://webassembly.github.io/spec/core/binary/instructions.html#binary-expr
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static Expression readExpression(InputStream in) throws IOException{
		Instruction instruction = readInstruction(in, true);
		LinkedList<Instruction> instructions = new LinkedList<>();
		while(instruction!=null) {
			instructions.add(instruction);
			instruction = readInstruction(in, true);
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
	private static Instruction readInstruction(InputStream in, boolean elseIsError) throws IOException {
		int identifier = in.read();
		if(identifier<0)
			throw new IOException("End of stream found when trying to identify Instruction.");
		// Control Instructions
		if(identifier==0x0B)
			return null;
		if(identifier==5) {
			if(elseIsError)
				throw new IOException("Unexpcted else instruction found.");
			else
				return new Else();
		}
		if(identifier==0)
			return new Unreachable();
		if(identifier==1)
			return new Nop();
		if(identifier==2) {
			BlockType blockType = readBlockType(in);
			Instruction instruction;
			ArrayList<Instruction> instructions = new ArrayList<>();
			while (true){
				instruction = readInstruction(in, true);
				if(instruction==null)
					break;
				instructions.add(instruction);
			}
			return new Block(blockType, instructions.toArray(new Instruction[instructions.size()]));
		}
		if(identifier==3) {
			BlockType blockType = readBlockType(in);
			Instruction instruction;
			ArrayList<Instruction> instructions = new ArrayList<>();
			while (true){
				instruction = readInstruction(in, true);
				if(instruction==null)
					break;
				instructions.add(instruction);
			}
			return new Loop(blockType, instructions.toArray(new Instruction[instructions.size()]));
		}
		if(identifier==4) {
			BlockType blockType = readBlockType(in);
			Instruction instruction;
			ArrayList<Instruction> ifInstructions = new ArrayList<>();
			ArrayList<Instruction> elseInstructions = new ArrayList<>();
			while (true){
				instruction = readInstruction(in, false);
				if(instruction==null)
					break;
				if(instruction instanceof Else) {
					while (true){
						instruction = readInstruction(in, false);
						if(instruction==null)
							break;
						elseInstructions.add(instruction);
					}
					break;
				}
				ifInstructions.add(instruction);
			}
			return new If(blockType, ifInstructions.toArray(new Instruction[ifInstructions.size()]), elseInstructions.toArray(new Instruction[elseInstructions.size()]));
		}
		if(identifier==0x0C)
			return new Branch(readIndex(in));
		if(identifier==0x0D)
			return new BranchIf(readIndex(in));
		if(identifier==0x0E)
			return new BranchTable(readVector(ModuleDeserializer::readIndex, in), readIndex(in));
		if(identifier==0x0F)
			return new Return();
		if(identifier==0x10)
			return new CallByIndex(new FunctionIndex(readUnsignedLEB128(in)));
		if(identifier==0x11)
			return new CallIndirect(new TypeIndex(readUnsignedLEB128(in)), new TableIndex(readUnsignedLEB128(in)));
		// Reference Instructions
		if(identifier==0xD0)
			return new ReferenceNull(readReferenceType(in));
		if(identifier==0xD1)
			return new ReferenceIsNull();
		if(identifier==0xD2)
			return new ReferenceFunction(new FunctionIndex(readUnsignedLEB128(in)));
		// Parametric Instructions
		if(identifier==0x1A)
			return new Drop();
		if(identifier==0x1B)
			return new Select(new Vector<ValueType>());
		if(identifier==0x1C)
			return new Select(readVector(ModuleDeserializer::readValueType, in));
		// Variable Instructions
		if(identifier==0x20)
			return new LocalGet(readIndex(in));
		if(identifier==0x21)
			return new LocalSet(readIndex(in));
		if(identifier==0x22)
			return new LocalTee(readIndex(in));
		if(identifier==0x23)
			return new GlobalGet(new GlobalIndex(readUnsignedLEB128(in)));
		if(identifier==0x24)
			return new GlobalSet(new GlobalIndex(readUnsignedLEB128(in)));
		// Table Instructions
		if(identifier==0x25)
			return new TableGet(new TableIndex(readUnsignedLEB128(in)));
		if(identifier==0x26)
			return new TableSet(new TableIndex(readUnsignedLEB128(in)));
		if(identifier==0xFC) {
			long subidentifier = readUnsignedLEB128(in);
			if(subidentifier==0)
				return new I32TruncSatF32S();
			if(subidentifier==1)
				return new I32TruncSatF32U();
			if(subidentifier==2)
				return new I32TruncSatF64S();
			if(subidentifier==3)
				return new I32TruncSatF64U();
			if(subidentifier==4)
				return new I64TruncSatF32S();
			if(subidentifier==5)
				return new I64TruncSatF32U();
			if(subidentifier==6)
				return new I64TruncSatF64S();
			if(subidentifier==7)
				return new I64TruncSatF64U();
			if(subidentifier==8) {
				MemoryInitialize retval = new MemoryInitialize(readIndex(in));
				if(in.read()!=0)
					throw new IOException("Unrecoginized memory initialize instruction subidentifier found.");
				return retval;
			}
			if(subidentifier==9)
				return new DataDrop(readIndex(in));
			if(subidentifier==10) {
				if((in.read()!=0)||(in.read()!=0))
					throw new IOException("Unrecoginized memory initialize instruction subidentifier found.");
				return new MemoryCopy();
			}
			if(subidentifier==11) {
				if(in.read()!=0)
					throw new IOException("Unrecoginized memory initialize instruction subidentifier found.");
				return new MemoryFill();
			}
			if(subidentifier==12)
				return new TableInitialize(readIndex(in),new TableIndex(readUnsignedLEB128(in)));
			if(subidentifier==13)
				return new ElementDrop(readIndex(in));
			if(subidentifier==14)
				return new TableCopy(new TableIndex(readUnsignedLEB128(in)),new TableIndex(readUnsignedLEB128(in)));
			if(subidentifier==15)
				return new TableGrow(new TableIndex(readUnsignedLEB128(in)));
			if(subidentifier==16)
				return new TableSize(new TableIndex(readUnsignedLEB128(in)));
			if(subidentifier==17)
				return new TableFill(new TableIndex(readUnsignedLEB128(in)));
			throw new IOException("Unrecoginized 0xFC instruction subidentifier found.");
		}
		// Memory Instructions
		if(identifier==0x28)
			return new I32Load(readUnsignedLEB128(in),readUnsignedLEB128(in));
		if(identifier==0x29)
			return new I64Load(readUnsignedLEB128(in),readUnsignedLEB128(in));
		if(identifier==0x2A)
			return new F32Load(readUnsignedLEB128(in),readUnsignedLEB128(in));
		if(identifier==0x2B)
			return new F64Load(readUnsignedLEB128(in),readUnsignedLEB128(in));
		if(identifier==0x2C)
			return new I32Load8S(readUnsignedLEB128(in),readUnsignedLEB128(in));
		if(identifier==0x2D)
			return new I32Load8U(readUnsignedLEB128(in),readUnsignedLEB128(in));
		if(identifier==0x2E)
			return new I32Load16S(readUnsignedLEB128(in),readUnsignedLEB128(in));
		if(identifier==0x2F)
			return new I32Load16U(readUnsignedLEB128(in),readUnsignedLEB128(in));
		if(identifier==0x30)
			return new I64Load8S(readUnsignedLEB128(in),readUnsignedLEB128(in));
		if(identifier==0x31)
			return new I64Load8U(readUnsignedLEB128(in),readUnsignedLEB128(in));
		if(identifier==0x32)
			return new I64Load16S(readUnsignedLEB128(in),readUnsignedLEB128(in));
		if(identifier==0x33)
			return new I64Load16U(readUnsignedLEB128(in),readUnsignedLEB128(in));
		if(identifier==0x34)
			return new I64Load32S(readUnsignedLEB128(in),readUnsignedLEB128(in));
		if(identifier==0x35)
			return new I64Load32U(readUnsignedLEB128(in),readUnsignedLEB128(in));
		if(identifier==0x36)
			return new I32Store(readUnsignedLEB128(in),readUnsignedLEB128(in));
		if(identifier==0x37)
			return new I64Store(readUnsignedLEB128(in),readUnsignedLEB128(in));
		if(identifier==0x38)
			return new F32Store(readUnsignedLEB128(in),readUnsignedLEB128(in));
		if(identifier==0x39)
			return new F64Store(readUnsignedLEB128(in),readUnsignedLEB128(in));
		if(identifier==0x3A)
			return new I32Store8(readUnsignedLEB128(in),readUnsignedLEB128(in));
		if(identifier==0x3B)
			return new I32Store16(readUnsignedLEB128(in),readUnsignedLEB128(in));
		if(identifier==0x3C)
			return new I64Store8(readUnsignedLEB128(in),readUnsignedLEB128(in));
		if(identifier==0x3D)
			return new I64Store16(readUnsignedLEB128(in),readUnsignedLEB128(in));
		if(identifier==0x3E)
			return new I64Store32(readUnsignedLEB128(in),readUnsignedLEB128(in));
		if(identifier==0x3F) {
			if(in.read()!=0)
				throw new IOException("Unrecoginized memory size instruction subidentifier found.");
			return new MemorySize();
		}
		if(identifier==0x40) {
			if(in.read()!=0)
				throw new IOException("Unrecoginized memory grow instruction subidentifier found.");
			return new MemoryGrow();
		}
		// Memory instructions encoded under the 0xFC identifier handled in the table section.
		// Numeric Instructions
		if(identifier==0x41)
			return new I32Const((int)readSignedLEB128(in));
		if(identifier==0x42)
			return new I64Const(readSignedLEB128(in));
		if(identifier==0x43)
			return new F32Const(readFloat(in));
		if(identifier==0x44)
			return new F64Const(readDouble(in));
		if(identifier==0x45)
			return new I32Eqz();
		if(identifier==0x46)
			return new I32Eq();
		if(identifier==0x47)
			return new I32Ne();
		if(identifier==0x48)
			return new I32LtS();
		if(identifier==0x49)
			return new I32LtU();
		if(identifier==0x4A)
			return new I32GtS();
		if(identifier==0x4B)
			return new I32GtU();
		if(identifier==0x4C)
			return new I32LeS();
		if(identifier==0x4D)
			return new I32LeU();
		if(identifier==0x4E)
			return new I32GeS();
		if(identifier==0x4F)
			return new I32GeU();
		if(identifier==0x50)
			return new I64Eqz();
		if(identifier==0x51)
			return new I64Eq();
		if(identifier==0x52)
			return new I64Ne();
		if(identifier==0x53)
			return new I64LtS();
		if(identifier==0x54)
			return new I64LtU();
		if(identifier==0x55)
			return new I64GtS();
		if(identifier==0x56)
			return new I64GtU();
		if(identifier==0x57)
			return new I64LeS();
		if(identifier==0x58)
			return new I64LeU();
		if(identifier==0x59)
			return new I64GeS();
		if(identifier==0x5A)
			return new I64GeU();
		if(identifier==0x5B)
			return new F32Eq();
		if(identifier==0x5C)
			return new F32Ne();
		if(identifier==0x5D)
			return new F32Lt();
		if(identifier==0x5E)
			return new F32Gt();
		if(identifier==0x5F)
			return new F32Le();
		if(identifier==0x60)
			return new F32Ge();
		if(identifier==0x61)
			return new F64Eq();
		if(identifier==0x62)
			return new F64Ne();
		if(identifier==0x63)
			return new F64Lt();
		if(identifier==0x64)
			return new F64Gt();
		if(identifier==0x65)
			return new F64Le();
		if(identifier==0x66)
			return new F64Ge();
		if(identifier==0x67)
			return new I32Clz();
		if(identifier==0x68)
			return new I32Ctz();
		if(identifier==0x69)
			return new I32Popcnt();
		if(identifier==0x6A)
			return new I32Add();
		if(identifier==0x6B)
			return new I32Sub();
		if(identifier==0x6C)
			return new I32Mul();
		if(identifier==0x6D)
			return new I32DivS();
		if(identifier==0x6E)
			return new I32DivU();
		if(identifier==0x6F)
			return new I32RemS();
		if(identifier==0x70)
			return new I32RemU();
		if(identifier==0x71)
			return new I32And();
		if(identifier==0x72)
			return new I32Or();
		if(identifier==0x73)
			return new I32Xor();
		if(identifier==0x74)
			return new I32Shl();
		if(identifier==0x75)
			return new I32ShrS();
		if(identifier==0x76)
			return new I32ShrU();
		if(identifier==0x77)
			return new I32Rotl();
		if(identifier==0x78)
			return new I32Rotr();
		if(identifier==0x79)
			return new I64Clz();
		if(identifier==0x7A)
			return new I64Ctz();
		if(identifier==0x7B)
			return new I64Popcnt();
		if(identifier==0x7C)
			return new I64Add();
		if(identifier==0x7D)
			return new I64Sub();
		if(identifier==0x7E)
			return new I64Mul();
		if(identifier==0x7F)
			return new I64DivS();
		if(identifier==0x80)
			return new I64DivU();
		if(identifier==0x81)
			return new I64RemS();
		if(identifier==0x82)
			return new I64RemU();
		if(identifier==0x83)
			return new I64And();
		if(identifier==0x84)
			return new I64Or();
		if(identifier==0x85)
			return new I64Xor();
		if(identifier==0x86)
			return new I64Shl();
		if(identifier==0x87)
			return new I64ShrS();
		if(identifier==0x88)
			return new I64ShrU();
		if(identifier==0x89)
			return new I64Rotl();
		if(identifier==0x8A)
			return new I64Rotr();
		if(identifier==0x8B)
			return new F32Abs();
		if(identifier==0x8C)
			return new F32Neg();
		if(identifier==0x8D)
			return new F32Ceil();
		if(identifier==0x8E)
			return new F32Floor();
		if(identifier==0x8F)
			return new F32Trunc();
		if(identifier==0x90)
			return new F32Nearest();
		if(identifier==0x91)
			return new F32Sqrt();
		if(identifier==0x92)
			return new F32Add();
		if(identifier==0x93)
			return new F32Sub();
		if(identifier==0x94)
			return new F32Mul();
		if(identifier==0x95)
			return new F32Div();
		if(identifier==0x96)
			return new F32Min();
		if(identifier==0x97)
			return new F32Max();
		if(identifier==0x98)
			return new F32CopySign();
		if(identifier==0x99)
			return new F64Abs();
		if(identifier==0x9A)
			return new F64Neg();
		if(identifier==0x9B)
			return new F64Ceil();
		if(identifier==0x9C)
			return new F64Floor();
		if(identifier==0x9D)
			return new F64Trunc();
		if(identifier==0x9E)
			return new F64Nearest();
		if(identifier==0x9F)
			return new F64Sqrt();
		if(identifier==0xA0)
			return new F64Add();
		if(identifier==0xA1)
			return new F64Sub();
		if(identifier==0xA2)
			return new F64Mul();
		if(identifier==0xA3)
			return new F64Div();
		if(identifier==0xA4)
			return new F64Min();
		if(identifier==0xA5)
			return new F64Max();
		if(identifier==0xA6)
			return new F64CopySign();
		if(identifier==0xA7)
			return new I32WrapI64();
		if(identifier==0xA8)
			return new I32TruncF32S();
		if(identifier==0xA9)
			return new I32TruncF32U();
		if(identifier==0xAA)
			return new I32TruncF64S();
		if(identifier==0xAB)
			return new I32TruncF64U();
		if(identifier==0xAC)
			return new I64ExtendI32S();
		if(identifier==0xAD)
			return new I64ExtendI32U();
		if(identifier==0xAE)
			return new I64TruncF32S();
		if(identifier==0xAF)
			return new I64TruncF32U();
		if(identifier==0xB0)
			return new I64TruncF64S();
		if(identifier==0xB1)
			return new I64TruncF64U();
		if(identifier==0xB2)
			return new F32ConvertI32S();
		if(identifier==0xB3)
			return new F32ConvertI32U();
		if(identifier==0xB4)
			return new F32ConvertI64S();
		if(identifier==0xB5)
			return new F32ConvertI64U();
		if(identifier==0xB6)
			return new F32DemoteF64();
		if(identifier==0xB7)
			return new F64ConvertI32S();
		if(identifier==0xB8)
			return new F64ConvertI32U();
		if(identifier==0xB9)
			return new F64ConvertI64S();
		if(identifier==0xBA)
			return new F64ConvertI64U();
		if(identifier==0xBB)
			return new F64PromoteF32();
		if(identifier==0xBC)
			return new I32ReinterpretF32();
		if(identifier==0xBD)
			return new I64ReinterpretF64();
		if(identifier==0xBE)
			return new F32ReinterpretI32();
		if(identifier==0xBF)
			return new F64ReinterpretI64();
		if(identifier==0xC0)
			return new I32Extend8S();
		if(identifier==0xC1)
			return new I32Extend16S();
		if(identifier==0xC2)
			return new I64Extend8S();
		if(identifier==0xC3)
			return new I64Extend16S();
		if(identifier==0xC4)
			return new I64Extend32S();
		// Numeric instructions encoded under the 0xFC identifier handled in the table section.
		// Vector Instructions
		if(identifier==0xFD) {
			long subidentifier = readUnsignedLEB128(in);
			if(subidentifier==0)
				return new V128Load(readUnsignedLEB128(in),readUnsignedLEB128(in));
			if(subidentifier==1)
				return new V128Load8x8S(readUnsignedLEB128(in),readUnsignedLEB128(in));
			if(subidentifier==2)
				return new V128Load8x8U(readUnsignedLEB128(in),readUnsignedLEB128(in));
			if(subidentifier==3)
				return new V128Load16x4S(readUnsignedLEB128(in),readUnsignedLEB128(in));
			if(subidentifier==4)
				return new V128Load16x4U(readUnsignedLEB128(in),readUnsignedLEB128(in));
			if(subidentifier==5)
				return new V128Load32x2S(readUnsignedLEB128(in),readUnsignedLEB128(in));
			if(subidentifier==6)
				return new V128Load32x2U(readUnsignedLEB128(in),readUnsignedLEB128(in));
			if(subidentifier==7)
				return new V128Load8Splat(readUnsignedLEB128(in),readUnsignedLEB128(in));
			if(subidentifier==8)
				return new V128Load16Splat(readUnsignedLEB128(in),readUnsignedLEB128(in));
			if(subidentifier==9)
				return new V128Load32Splat(readUnsignedLEB128(in),readUnsignedLEB128(in));
			if(subidentifier==10)
				return new V128Load64Splat(readUnsignedLEB128(in),readUnsignedLEB128(in));
			if(subidentifier==11)
				return new V128Store(readUnsignedLEB128(in),readUnsignedLEB128(in));
			if(subidentifier==12) {
				byte[] bytes = in.readNBytes(16);
				if(bytes.length<16)
					throw new IOException("16 bytes expected for V128 Constant, but only "+bytes.length+" found.");
				return new V128Const(bytes);
			}
			if(subidentifier==13) {
				byte[] bytes = in.readNBytes(16);
				if(bytes.length<16)
					throw new IOException("16 bytes expected for I8x16Shuffle, but only "+bytes.length+" found.");
				return new I8x16Shuffle(bytes);
			}
			if(subidentifier==14)
				return new I8x16Swizzle();
			if(subidentifier==15)
				return new I8x16Splat();
			if(subidentifier==16)
				return new I16x8Splat();
			if(subidentifier==17)
				return new I32x4Splat();
			if(subidentifier==18)
				return new I64x2Splat();
			if(subidentifier==19)
				return new F32x4Splat();
			if(subidentifier==20)
				return new F64x2Splat();
			if(subidentifier==21) {
				int laneIndex = in.read();
				if(laneIndex<0)
					throw new IOException("End of stream found when reading I8x16ExtractLaneS.");
				return new I8x16ExtractLaneS(laneIndex);
			}
			if(subidentifier==22) {
				int laneIndex = in.read();
				if(laneIndex<0)
					throw new IOException("End of stream found when reading I8x16ExtractLaneU.");
				return new I8x16ExtractLaneU(laneIndex);
			}
			if(subidentifier==23) {
				int laneIndex = in.read();
				if(laneIndex<0)
					throw new IOException("End of stream found when reading I8x16ReplaceLane.");
				return new I8x16ReplaceLane(laneIndex);
			}
			if(subidentifier==24) {
				int laneIndex = in.read();
				if(laneIndex<0)
					throw new IOException("End of stream found when reading I16x8ExtractLaneS.");
				return new I16x8ExtractLaneS(laneIndex);
			}
			if(subidentifier==25) {
				int laneIndex = in.read();
				if(laneIndex<0)
					throw new IOException("End of stream found when reading I16x8ExtractLaneU.");
				return new I16x8ExtractLaneU(laneIndex);
			}
			if(subidentifier==26) {
				int laneIndex = in.read();
				if(laneIndex<0)
					throw new IOException("End of stream found when reading I16x8ReplaceLane.");
				return new I16x8ReplaceLane(laneIndex);
			}
			if(subidentifier==27) {
				int laneIndex = in.read();
				if(laneIndex<0)
					throw new IOException("End of stream found when reading I32x4ExtractLane.");
				return new I32x4ExtractLane(laneIndex);
			}
			if(subidentifier==28) {
				int laneIndex = in.read();
				if(laneIndex<0)
					throw new IOException("End of stream found when reading I32x4ReplaceLane.");
				return new I32x4ReplaceLane(laneIndex);
			}
			if(subidentifier==29) {
				int laneIndex = in.read();
				if(laneIndex<0)
					throw new IOException("End of stream found when reading I64x2ExtractLane.");
				return new I64x2ExtractLane(laneIndex);
			}
			if(subidentifier==30) {
				int laneIndex = in.read();
				if(laneIndex<0)
					throw new IOException("End of stream found when reading I64x2ReplaceLane.");
				return new I64x2ReplaceLane(laneIndex);
			}
			if(subidentifier==31) {
				int laneIndex = in.read();
				if(laneIndex<0)
					throw new IOException("End of stream found when reading F32x4ExtractLane.");
				return new F32x4ExtractLane(laneIndex);
			}
			if(subidentifier==32) {
				int laneIndex = in.read();
				if(laneIndex<0)
					throw new IOException("End of stream found when reading F32x4ReplaceLane.");
				return new F32x4ReplaceLane(laneIndex);
			}
			if(subidentifier==33) {
				int laneIndex = in.read();
				if(laneIndex<0)
					throw new IOException("End of stream found when reading F64x2ExtractLane.");
				return new F64x2ExtractLane(laneIndex);
			}
			if(subidentifier==34) {
				int laneIndex = in.read();
				if(laneIndex<0)
					throw new IOException("End of stream found when reading F64x2ReplaceLane.");
				return new F64x2ReplaceLane(laneIndex);
			}
			if(subidentifier==35)
				return new I8x16Eq();
			if(subidentifier==36)
				return new I8x16Ne();
			if(subidentifier==37)
				return new I8x16LtS();
			if(subidentifier==38)
				return new I8x16LtU();
			if(subidentifier==39)
				return new I8x16GtS();
			if(subidentifier==40)
				return new I8x16GtU();
			if(subidentifier==41)
				return new I8x16LeS();
			if(subidentifier==42)
				return new I8x16LeU();
			if(subidentifier==43)
				return new I8x16GeS();
			if(subidentifier==44)
				return new I8x16GeU();
			if(subidentifier==45)
				return new I16x8Eq();
			if(subidentifier==46)
				return new I16x8Ne();
			if(subidentifier==47)
				return new I16x8LtS();
			if(subidentifier==48)
				return new I16x8LtU();
			if(subidentifier==49)
				return new I16x8GtS();
			if(subidentifier==50)
				return new I16x8GtU();
			if(subidentifier==51)
				return new I16x8LeS();
			if(subidentifier==52)
				return new I16x8LeU();
			if(subidentifier==53)
				return new I16x8GeS();
			if(subidentifier==54)
				return new I16x8GeU();
			if(subidentifier==55)
				return new I32x4Eq();
			if(subidentifier==56)
				return new I32x4Ne();
			if(subidentifier==57)
				return new I32x4LtS();
			if(subidentifier==58)
				return new I32x4LtU();
			if(subidentifier==59)
				return new I32x4GtS();
			if(subidentifier==60)
				return new I32x4GtU();
			if(subidentifier==61)
				return new I32x4LeS();
			if(subidentifier==62)
				return new I32x4LeU();
			if(subidentifier==63)
				return new I32x4GeS();
			if(subidentifier==64)
				return new I32x4GeU();
			if(subidentifier==65)
				return new F32x4Eq();
			if(subidentifier==66)
				return new F32x4Ne();
			if(subidentifier==67)
				return new F32x4Lt();
			if(subidentifier==68)
				return new F32x4Gt();
			if(subidentifier==69)
				return new F32x4Le();
			if(subidentifier==70)
				return new F32x4Ge();
			if(subidentifier==71)
				return new F64x2Eq();
			if(subidentifier==72)
				return new F64x2Ne();
			if(subidentifier==73)
				return new F64x2Lt();
			if(subidentifier==74)
				return new F64x2Gt();
			if(subidentifier==75)
				return new F64x2Le();
			if(subidentifier==76)
				return new F64x2Ge();
			if(subidentifier==77)
				return new V128Not();
			if(subidentifier==78)
				return new V128And();
			if(subidentifier==79)
				return new V128AndNot();
			if(subidentifier==80)
				return new V128Or();
			if(subidentifier==81)
				return new V128Xor();
			if(subidentifier==82)
				return new V128BitSelect();
			if(subidentifier==83)
				return new V128AnyTrue();
			if(subidentifier==84) {
				long align = readUnsignedLEB128(in);
				long offset = readUnsignedLEB128(in);
				int laneIndex = in.read();
				if(laneIndex<0)
					throw new IOException("End of stream found when reading V128Load8Lane.");
				return new V128Load8Lane(align, offset, laneIndex);
			}
			if(subidentifier==85) {
				long align = readUnsignedLEB128(in);
				long offset = readUnsignedLEB128(in);
				int laneIndex = in.read();
				if(laneIndex<0)
					throw new IOException("End of stream found when reading V128Load16Lane.");
				return new V128Load16Lane(align, offset, laneIndex);
			}
			if(subidentifier==86) {
				long align = readUnsignedLEB128(in);
				long offset = readUnsignedLEB128(in);
				int laneIndex = in.read();
				if(laneIndex<0)
					throw new IOException("End of stream found when reading V128Load32Lane.");
				return new V128Load32Lane(align, offset, laneIndex);
			}
			if(subidentifier==87) {
				long align = readUnsignedLEB128(in);
				long offset = readUnsignedLEB128(in);
				int laneIndex = in.read();
				if(laneIndex<0)
					throw new IOException("End of stream found when reading V128Load64Lane.");
				return new V128Load64Lane(align, offset, laneIndex);
			}
			if(subidentifier==88) {
				long align = readUnsignedLEB128(in);
				long offset = readUnsignedLEB128(in);
				int laneIndex = in.read();
				if(laneIndex<0)
					throw new IOException("End of stream found when reading V128Store8Lane.");
				return new V128Store8Lane(align, offset, laneIndex);
			}
			if(subidentifier==89) {
				long align = readUnsignedLEB128(in);
				long offset = readUnsignedLEB128(in);
				int laneIndex = in.read();
				if(laneIndex<0)
					throw new IOException("End of stream found when reading V128Store16Lane.");
				return new V128Store16Lane(align, offset, laneIndex);
			}
			if(subidentifier==90) {
				long align = readUnsignedLEB128(in);
				long offset = readUnsignedLEB128(in);
				int laneIndex = in.read();
				if(laneIndex<0)
					throw new IOException("End of stream found when reading V128Store32Lane.");
				return new V128Store32Lane(align, offset, laneIndex);
			}
			if(subidentifier==91) {
				long align = readUnsignedLEB128(in);
				long offset = readUnsignedLEB128(in);
				int laneIndex = in.read();
				if(laneIndex<0)
					throw new IOException("End of stream found when reading V128Store64Lane.");
				return new V128Store64Lane(align, offset, laneIndex);
			}
			if(subidentifier==92)
				return new V128Load32Zero(readUnsignedLEB128(in), readUnsignedLEB128(in));
			if(subidentifier==93)
				return new V128Load64Zero(readUnsignedLEB128(in), readUnsignedLEB128(in));
			if(subidentifier==94)
				return new F32x4DemoteF64x2Zero();
			if(subidentifier==95)
				return new F64x2PromoteLowF32x4();
			if(subidentifier==96)
				return new I8x16Abs();
			if(subidentifier==97)
				return new I8x16Neg();
			if(subidentifier==98)
				return new I8x16PopCnt();
			if(subidentifier==99)
				return new I8x16AllTrue();
			if(subidentifier==100)
				return new I8x16BitMask();
			if(subidentifier==101)
				return new I8x16NarrowI16x8S();
			if(subidentifier==102)
				return new I8x16NarrowI16x8U();
			if(subidentifier==103)
				return new F32x4Ceil();
			if(subidentifier==104)
				return new F32x4Floor();
			if(subidentifier==105)
				return new F32x4Trunc();
			if(subidentifier==106)
				return new F32x4Nearest();
			if(subidentifier==107)
				return new I8x16Shl();
			if(subidentifier==108)
				return new I8x16ShrS();
			if(subidentifier==109)
				return new I8x16ShrU();
			if(subidentifier==110)
				return new I8x16Add();
			if(subidentifier==111)
				return new I8x16AddSatS();
			if(subidentifier==112)
				return new I8x16AddSatU();
			if(subidentifier==113)
				return new I8x16Sub();
			if(subidentifier==114)
				return new I8x16SubSatS();
			if(subidentifier==115)
				return new I8x16SubSatU();
			if(subidentifier==116)
				return new F64x2Ceil();
			if(subidentifier==117)
				return new F64x2Floor();
			if(subidentifier==118)
				return new I8x16MinS();
			if(subidentifier==119)
				return new I8x16MinU();
			if(subidentifier==120)
				return new I8x16MaxS();
			if(subidentifier==121)
				return new I8x16MaxU();
			if(subidentifier==122)
				return new F64x2Trunc();
			if(subidentifier==123)
				return new I8x16AvgrU();
			if(subidentifier==124)
				return new I16x8ExtaddPairwiseI8x16S();
			if(subidentifier==125)
				return new I16x8ExtaddPairwiseI8x16U();
			if(subidentifier==126)
				return new I32x4ExtaddPairwiseI16x8S();
			if(subidentifier==127)
				return new I32x4ExtaddPairwiseI16x8U();
			if(subidentifier==128)
				return new I16x8Abs();
			if(subidentifier==129)
				return new I16x8Neg();
			if(subidentifier==130)
				return new I16x8Q15MulrSatS();
			if(subidentifier==131)
				return new I16x8AllTrue();
			if(subidentifier==132)
				return new I16x8BitMask();
			if(subidentifier==133)
				return new I16x8NarrowI32x4S();
			if(subidentifier==134)
				return new I16x8NarrowI32x4U();
			if(subidentifier==135)
				return new I16x8ExtendLowI8x16S();
			if(subidentifier==136)
				return new I16x8ExtendHighI8x16S();
			if(subidentifier==137)
				return new I16x8ExtendLowI8x16U();
			if(subidentifier==138)
				return new I16x8ExtendHighI8x16U();
			if(subidentifier==139)
				return new I16x8Shl();
			if(subidentifier==140)
				return new I16x8ShrS();
			if(subidentifier==141)
				return new I16x8ShrU();
			if(subidentifier==142)
				return new I16x8Add();
			if(subidentifier==143)
				return new I16x8AddSatS();
			if(subidentifier==144)
				return new I16x8AddSatU();
			if(subidentifier==145)
				return new I16x8Sub();
			if(subidentifier==146)
				return new I16x8SubSatS();
			if(subidentifier==147)
				return new I16x8SubSatU();
			if(subidentifier==148)
				return new F64x2Nearest();
			if(subidentifier==149)
				return new I16x8Mul();
			if(subidentifier==150)
				return new I16x8MinS();
			if(subidentifier==151)
				return new I16x8MinU();
			if(subidentifier==152)
				return new I16x8MaxS();
			if(subidentifier==153)
				return new I16x8MaxU();
			if(subidentifier==155)
				return new I16x8AvgrU();
			if(subidentifier==156)
				return new I16x8ExtmulLowI8x16S();
			if(subidentifier==157)
				return new I16x8ExtmulHighI8x16S();
			if(subidentifier==158)
				return new I16x8ExtmulLowI8x16U();
			if(subidentifier==159)
				return new I16x8ExtmulHighI8x16U();
			if(subidentifier==160)
				return new I32x4Abs();
			if(subidentifier==161)
				return new I32x4Neg();
			if(subidentifier==163)
				return new I32x4AllTrue();
			if(subidentifier==164)
				return new I32x4BitMask();
			if(subidentifier==167)
				return new I32x4ExtendLowI16x8S();
			if(subidentifier==168)
				return new I32x4ExtendHighI16x8S();
			if(subidentifier==169)
				return new I32x4ExtendLowI16x8U();
			if(subidentifier==170)
				return new I32x4ExtendHighI16x8U();
			if(subidentifier==171)
				return new I32x4Shl();
			if(subidentifier==172)
				return new I32x4ShrS();
			if(subidentifier==173)
				return new I32x4ShrU();
			if(subidentifier==174)
				return new I32x4Add();
			if(subidentifier==177)
				return new I32x4Sub();
			if(subidentifier==181)
				return new I32x4Mul();
			if(subidentifier==182)
				return new I32x4MinS();
			if(subidentifier==183)
				return new I32x4MinU();
			if(subidentifier==184)
				return new I32x4MaxS();
			if(subidentifier==185)
				return new I32x4MaxU();
			if(subidentifier==186)
				return new I32x4DotI16x8S();
			if(subidentifier==188)
				return new I32x4ExtmulLowI16x8S();
			if(subidentifier==189)
				return new I32x4ExtmulHighI16x8S();
			if(subidentifier==190)
				return new I32x4ExtmulLowI16x8U();
			if(subidentifier==191)
				return new I32x4ExtmulHighI16x8U();
			if(subidentifier==192)
				return new I64x2Abs();
			if(subidentifier==193)
				return new I64x2Neg();
			if(subidentifier==195)
				return new I64x2AllTrue();
			if(subidentifier==196)
				return new I64x2BitMask();
			if(subidentifier==199)
				return new I64x2ExtendLowI32x4S();
			if(subidentifier==200)
				return new I64x2ExtendHighI32x4S();
			if(subidentifier==201)
				return new I64x2ExtendLowI32x4U();
			if(subidentifier==202)
				return new I64x2ExtendHighI32x4U();
			if(subidentifier==203)
				return new I64x2Shl();
			if(subidentifier==204)
				return new I64x2ShrS();
			if(subidentifier==205)
				return new I64x2ShrU();
			if(subidentifier==206)
				return new I64x2Add();
			if(subidentifier==209)
				return new I64x2Sub();
			if(subidentifier==213)
				return new I64x2Mul();
			if(subidentifier==214)
				return new I64x2Eq();
			if(subidentifier==215)
				return new I64x2Ne();
			if(subidentifier==216)
				return new I64x2LtS();
			if(subidentifier==217)
				return new I64x2GtS();
			if(subidentifier==218)
				return new I64x2LeS();
			if(subidentifier==219)
				return new I64x2GeS();
			if(subidentifier==220)
				return new I64x2ExtmulLowI32x4S();
			if(subidentifier==221)
				return new I64x2ExtmulHighI32x4S();
			if(subidentifier==222)
				return new I64x2ExtmulLowI32x4U();
			if(subidentifier==223)
				return new I64x2ExtmulHighI32x4U();
			if(subidentifier==224)
				return new F32x4Abs();
			if(subidentifier==225)
				return new F32x4Neg();
			if(subidentifier==227)
				return new F32x4Sqrt();
			if(subidentifier==228)
				return new F32x4Add();
			if(subidentifier==229)
				return new F32x4Sub();
			if(subidentifier==230)
				return new F32x4Mul();
			if(subidentifier==231)
				return new F32x4Div();
			if(subidentifier==232)
				return new F32x4Min();
			if(subidentifier==233)
				return new F32x4Max();
			if(subidentifier==234)
				return new F32x4PMin();
			if(subidentifier==235)
				return new F32x4PMax();
			if(subidentifier==236)
				return new F64x2Abs();
			if(subidentifier==237)
				return new F64x2Neg();
			if(subidentifier==239)
				return new F64x2Sqrt();
			if(subidentifier==240)
				return new F64x2Add();
			if(subidentifier==241)
				return new F64x2Sub();
			if(subidentifier==242)
				return new F64x2Mul();
			if(subidentifier==243)
				return new F64x2Div();
			if(subidentifier==244)
				return new F64x2Min();
			if(subidentifier==245)
				return new F64x2Max();
			if(subidentifier==246)
				return new F64x2PMin();
			if(subidentifier==247)
				return new F64x2PMax();
			if(subidentifier==248)
				return new I32x4TruncSatF32x4S();
			if(subidentifier==249)
				return new I32x4TruncSatF32x4U();
			if(subidentifier==250)
				return new F32x4ConvertI32x4S();
			if(subidentifier==251)
				return new F32x4ConvertI32x4U();
			if(subidentifier==252)
				return new I32x4TruncSatF64x2SZero();
			if(subidentifier==253)
				return new I32x4TruncSatF64x2UZero();
			if(subidentifier==254)
				return new F64x2ConvertLowI32x4S();
			if(subidentifier==255)
				return new F64x2ConvertLowI32x4U();
		}
		
		throw new IOException("Unrecoginized instruction identifier found.");
	}
	
	/**
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static BlockType readBlockType(InputStream in) throws IOException {
		byte[] blockTypeBytes = readLEBBytes(in);
		if(blockTypeBytes.length == 1) {
			byte b = blockTypeBytes[0];
			if((b & 0x40)!=0) {
				if(b == 0x40)
					return new BlockTypeEmpty();
				for(ValueType valueType:ValueType.values())
					if(valueType.getType() == (0xFF & (int)b))
						return new BlockTypeValueType(valueType);
				throw new IOException("Unrecoginized block type found.");
			}
		}
		return new BlockTypeIndex(readSignedLEB128(new ByteArrayInputStream(blockTypeBytes)));
	}
	
	private static Index readIndex(InputStream in) throws IOException{
		return new Index(readUnsignedLEB128(in));
	}
	
	private static TypeIndex readTypeIndex(InputStream in) throws IOException{
		return new TypeIndex(readUnsignedLEB128(in));
	}
	
	private static FunctionIndex readFunctionIndex(InputStream in) throws IOException{
		return new FunctionIndex(readUnsignedLEB128(in));
	}
	
	private static Table readTable(InputStream in) throws IOException{
		return new Table(readTableType(in));
	}

	private static Memory readMemory(InputStream in) throws IOException{
		return new Memory(readMemoryType(in));
	}

	private static Global readGlobal(InputStream in) throws IOException{
		return new Global(readGlobalType(in), readExpression(in));
	}

	private static Export readExport(InputStream in) throws IOException{
		return new Export(readName(in), readExportDescriptor(in));
	}

	private static Start readStart(InputStream in) throws IOException{
		return new Start(new FunctionIndex(readUnsignedLEB128(in)));
	}

	private static Element readElement(InputStream in) throws IOException{
		int encodingFlag = in.read();
		if(encodingFlag<0)
			throw new IOException("End of stream found while reading Element.");
		boolean isActive = (encodingFlag&1)==0;
		boolean explicitTableIndex = (isActive && ((encodingFlag&2)>0));
		boolean isDeclarative = ((!isActive) && ((encodingFlag&2)>0));
		boolean isSimpleIndexes = ((encodingFlag&4)==0);
		TableIndex table = null;
		Expression offset = null;
		ReferenceType type = null;
		Vector<Expression> init = null;
		ElementMode mode = isActive?ElementMode.ACTIVE:(isDeclarative?ElementMode.DECLARATIVE:ElementMode.PASSIVE);
		if(explicitTableIndex)
			table = new TableIndex(readUnsignedLEB128(in));
		else if(isActive)
			table = new TableIndex(0);
		if(isActive)
			offset = readExpression(in);
		if(!(isActive&&!explicitTableIndex)) {
			if(isSimpleIndexes) {
				int b = in.read();
				if(b<0)
					throw new IOException("End of stream found while reading Element simple indexes marker.");
				if(b!=0)
					throw new IOException("Unrecognized marker found when simple indexes expected: "+b+" (encodingFlag="+encodingFlag+")");
				type = ReferenceType.REFTYPE_FUNCREF;
			}
			else {
				type = readReferenceType(in);
			}
		}
		else {
			type = ReferenceType.REFTYPE_FUNCREF;
		}
		if(isSimpleIndexes) {
			Vector<FunctionIndex> functionIndexes = readVector(ModuleDeserializer::readFunctionIndex, in);
			final Vector<Expression> initFinal = new Vector<>();
			functionIndexes.stream().forEach((e)->{
				LinkedList<Instruction> instructions = new LinkedList<>();
				instructions.add(new ReferenceFunction(e));
				initFinal.add(new Expression(instructions));
			});
			init = initFinal;
		}
		else {
			init = readVector(ModuleDeserializer::readExpression, in);
		}
		return new Element(type, init, mode, table, offset);
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

	
	/**
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static long readSignedLEB128(InputStream in) throws IOException {
		long retval = 0;
		int shift = 0;

		long b_in = 0;
		do {
			b_in = in.read();
			if(b_in<0)
				throw new IOException("End of stream encountered while reading unsigned LEB128.");
			retval |= ((b_in & 0x7FL) << shift);
			shift += 7;
		} while ((b_in & 0x080L) != 0);

		/* sign bit of byte is second high-order bit (0x40) */
		if ((b_in & 0x040L)!=0)
		  /* sign extend */
		  retval |= (~0L << shift);
		return retval;
	}
	
	private static byte[] readLEBBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		int b_in = 0;
		do {
			b_in = in.read();
			if(b_in<0)
				throw new IOException("End of stream encountered while reading LEB128 bytes.");
			out.write(b_in);
		} while ((b_in & 0x080L) != 0);

		return out.toByteArray();
	}
	
	private static float readFloat(InputStream in) throws IOException {
		byte[] bytes = in.readNBytes(4);
		if(bytes.length<4)
			throw new IOException("End of stream encountered while reading float.");
		int bits=0;
		for(int i=3;i>=0;i--) {
			bits<<=8;
			bits|=((int)bytes[i])&0x0FF;
		}
		return Float.intBitsToFloat(bits);
	}
	
	private static double readDouble(InputStream in) throws IOException {
		byte[] bytes = in.readNBytes(8);
		if(bytes.length<8)
			throw new IOException("End of stream encountered while reading float.");
		long bits=0;
		for(int i=7;i>=0;i--) {
			bits<<=8;
			bits|=((int)bytes[i])&0x0FF;
		}
		return Double.longBitsToDouble(bits);
	}
	
	interface ElementReader<ER>{
		ER read(InputStream out) throws IOException;
	}
	
	static class Else implements Instruction{}
}
