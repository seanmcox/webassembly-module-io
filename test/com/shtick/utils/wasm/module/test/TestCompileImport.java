/**
 * 
 */
package com.shtick.utils.wasm.module.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Vector;

import org.junit.jupiter.api.Test;

import com.shtick.utils.wasm.module.Code;
import com.shtick.utils.wasm.module.Context;
import com.shtick.utils.wasm.module.ElementSection;
import com.shtick.utils.wasm.module.Export;
import com.shtick.utils.wasm.module.ExportSection;
import com.shtick.utils.wasm.module.Expression;
import com.shtick.utils.wasm.module.FunctionDefinition;
import com.shtick.utils.wasm.module.FunctionType;
import com.shtick.utils.wasm.module.GlobalType;
import com.shtick.utils.wasm.module.Import;
import com.shtick.utils.wasm.module.ImportDescriptor;
import com.shtick.utils.wasm.module.Index;
import com.shtick.utils.wasm.module.Instruction;
import com.shtick.utils.wasm.module.Module;
import com.shtick.utils.wasm.module.ModuleSerializer;
import com.shtick.utils.wasm.module.Mutability;
import com.shtick.utils.wasm.module.ResultType;
import com.shtick.utils.wasm.module.StartSection;
import com.shtick.utils.wasm.module.ValueType;
import com.shtick.utils.wasm.module.ValueTypeInterface;
import com.shtick.utils.wasm.module.instructions.BlockTypeIndex;
import com.shtick.utils.wasm.module.instructions.CallByFunctionDefinition;
import com.shtick.utils.wasm.module.instructions.CallImportFunction;
import com.shtick.utils.wasm.module.instructions.GlobalGet;
import com.shtick.utils.wasm.module.instructions.I32Add;
import com.shtick.utils.wasm.module.instructions.I32Sub;
import com.shtick.utils.wasm.module.instructions.If;
import com.shtick.utils.wasm.module.instructions.LocalGet;

/**
 * @author seanmcox
 *
 */
class TestCompileImport {

	@Test
	void testFunctionImport() {
		Vector<ValueType> biParamTypes = new Vector<>();
		biParamTypes.add(ValueType.NUMTYPE_I32);
		biParamTypes.add(ValueType.NUMTYPE_I32);
		Vector<ValueType> triParamTypes = new Vector<>();
		triParamTypes.add(ValueType.NUMTYPE_I32);
		triParamTypes.add(ValueType.NUMTYPE_I32);
		triParamTypes.add(ValueType.NUMTYPE_I32);
		Vector<ValueType> resultTypes = new Vector<>();
		resultTypes.add(ValueType.NUMTYPE_I32);
		FunctionType biType = new FunctionType(new ResultType(biParamTypes), new ResultType(resultTypes));
		FunctionType triType = new FunctionType(new ResultType(triParamTypes), new ResultType(resultTypes));

		Context context = new Context();
		Import imp = context.addFunctionImport("module", "name", biType);

		Code sumCode;
		{
			LinkedList<Instruction> instructions = new LinkedList<>();
			instructions.add(new LocalGet(new Index(0)));
			instructions.add(new LocalGet(new Index(1)));
			instructions.add(new I32Add());
			sumCode = new Code(new Vector<>(), new Expression(instructions));
		}
		FunctionDefinition sum = new FunctionDefinition(biType, sumCode);
		context.addFunctionDefinition(sum);
		Code diffCode;
		{
			LinkedList<Instruction> instructions = new LinkedList<>();
			instructions.add(new LocalGet(new Index(0)));
			instructions.add(new LocalGet(new Index(1)));
			instructions.add(new I32Sub());
			diffCode = new Code(new Vector<>(), new Expression(instructions));
		}
		FunctionDefinition diff = new FunctionDefinition(biType, diffCode);
		context.addFunctionDefinition(diff);
		Code exportCode;
		{
			LinkedList<Instruction> instructions = new LinkedList<>();
			instructions.add(new LocalGet(new Index(0)));
			instructions.add(new LocalGet(new Index(1)));
			instructions.add(new LocalGet(new Index(2)));
			instructions.add(new If(new BlockTypeIndex(new FunctionType(new ResultType(biParamTypes), new ResultType(resultTypes)),context),
				new Instruction[] {new CallByFunctionDefinition(sum,context)},
				new Instruction[] {new CallImportFunction(imp,context)}));
			exportCode = new Code(new Vector<>(), new Expression(instructions));
		}
		FunctionDefinition export = new FunctionDefinition(triType, exportCode);
		context.addFunctionDefinition(export);
		
		// TODO Consider creating a function abstraction that can encapsulate the type, locals, and body of a function, which would then be translated to
		//      a FunctionSection and a CodeSection
		// See: https://webassembly.github.io/spec/core/binary/modules.html#binary-funcsec
		ExportSection exportSection;
		{
			Vector<Export> exports = new Vector<>();
			exports.add(new Export("binop",context.getFunctionIndex(export)));
			exportSection = new ExportSection(exports);
		}
		StartSection startSection = null;
		ElementSection elementSection = new ElementSection(new Vector<>());
		Module module = new Module(context,exportSection,startSection,elementSection,false);
		
		File outfile = new File("test_dist/import/function/test.wasm");
		try(FileOutputStream out = new FileOutputStream(outfile)) {
			ModuleSerializer.writeModule(module, out);
		}
		catch(IOException t) {
			t.printStackTrace();
			fail("IOException thrown");
		}
		catch(Throwable t){
			t.printStackTrace();
			fail("Throwable thrown");
		}
	}

	@Test
	void testGlobalImport() {
		Vector<ValueType> resultTypes = new Vector<>();
		resultTypes.add(ValueType.NUMTYPE_I32);
		FunctionType getIntType = new FunctionType(new ResultType(new Vector<>()), new ResultType(resultTypes));

		Context context = new Context();
		Import dexterImport = new Import("mod", "dexter", new GlobalType(ValueType.NUMTYPE_I32 ,Mutability.CONSTANT));
		Import sinisterImport = new Import("mod", "sinister", new GlobalType(ValueType.NUMTYPE_I32 ,Mutability.VARIABLE));
		context.addNonFunctionImport(dexterImport);
		context.addNonFunctionImport(sinisterImport);

		FunctionDefinition getDexter;
		{
			LinkedList<Instruction> instructions = new LinkedList<>();
			instructions.add(new GlobalGet(context.getGlobalIndex(dexterImport)));
			Code code = new Code(new Vector<>(), new Expression(instructions));
			getDexter = new FunctionDefinition(getIntType, code);
		}
		context.addFunctionDefinition(getDexter);
		FunctionDefinition getSinister;
		{
			LinkedList<Instruction> instructions = new LinkedList<>();
			instructions.add(new GlobalGet(context.getGlobalIndex(sinisterImport)));
			Code code = new Code(new Vector<>(), new Expression(instructions));
			getSinister = new FunctionDefinition(getIntType, code);
		}
		context.addFunctionDefinition(getSinister);
		
		// TODO Add an internal global to the test to verify that the index is being calculated correctly.
		
		// See: https://webassembly.github.io/spec/core/binary/modules.html#binary-funcsec
		ExportSection exportSection;
		{
			Vector<Export> exports = new Vector<>();
			exports.add(new Export("getDexter",context.getFunctionIndex(getDexter)));
			exports.add(new Export("getSinister",context.getFunctionIndex(getSinister)));
			exportSection = new ExportSection(exports);
		}
		StartSection startSection = null;
		ElementSection elementSection = new ElementSection(new Vector<>());
		Module module = new Module(context,exportSection,startSection,elementSection,false);
		
		File outfile = new File("test_dist/import/global/test.wasm");
		try(FileOutputStream out = new FileOutputStream(outfile)) {
			ModuleSerializer.writeModule(module, out);
		}
		catch(IOException t) {
			t.printStackTrace();
			fail("IOException thrown");
		}
		catch(Throwable t){
			t.printStackTrace();
			fail("Throwable thrown");
		}
	}

	@Test
	void testMemoryImport() {
		// TODO
	}

	@Test
	void testTableImport() {
		// TODO
	}
}
