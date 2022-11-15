/**
 * 
 */
package com.shtick.utils.wasm.module;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * @author seanmcox
 *
 */
public class ModuleDeserializer {
	public Module readModule(InputStream in) throws IOException {
		byte[] magic = readNBytes(4,in);
		byte[] version = readNBytes(4,in);
		if(!Arrays.equals(magic, ModuleSerializer.MAGIC_NUMBER))
			throw new IOException("Magic number not found in data.");
		if(!Arrays.equals(version, ModuleSerializer.VERSION_NUMBER))
			throw new IOException("Supported version number not found in data.");
		// TODO
		return null;
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
		// TODO
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

}
