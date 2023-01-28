/**
 * 
 */
package com.shtick.utils.wasm.module.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.shtick.utils.wasm.module.Module;
import com.shtick.utils.wasm.module.ModuleDeserializer;
import com.shtick.utils.wasm.module.ModuleSerializer;

/**
 * @author seanmcox
 *
 */
class TestDecompile {

	@Test
	void testSerialization() {
		for(String filename:new String[] {"test_dist/decompile/dnd_wasm.wasm","test_dist/decompile/mandelbrot_wasm.wasm"}) {
			File file = new File(filename);
			try(FileInputStream in = new FileInputStream(file)){
				byte[] inData;
				inData = in.readAllBytes();
				Module module = ModuleDeserializer.readModule(new ByteArrayInputStream(inData));
				ByteArrayOutputStream out = new ByteArrayOutputStream(inData.length);
				ModuleSerializer.writeModule(module, out);
				byte[] outData = out.toByteArray();
				assertTrue(Arrays.equals(inData, outData),"Input data matched output data for "+filename);
			}
			catch(IOException t) {
				t.printStackTrace();
				fail("IOException thrown.");
			}
		}
	}
}
