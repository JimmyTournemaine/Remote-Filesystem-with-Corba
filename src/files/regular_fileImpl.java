/*
 * Created on 25 jan. 2017 under the authority of Franck Singhoff 
 * as part of practical work at the University of Western Brittany
 */
package files;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;

public class regular_fileImpl extends files.regular_filePOA {
	
	private String name;
	private mode rwx;
	private RandomAccessFile raf;

	public regular_fileImpl(File file, mode m) throws IOException {
		name = file.getName();
		raf = new RandomAccessFile(file, "rw");
		rwx = m;
		switch(m.value()) {
			case mode._write_trunc:
				raf.setLength(0);
			case mode._read_only:
			case mode._read_write:
				raf.seek(0);
				break;
			case mode._write_append:
				raf.seek(raf.length());
		}
	}

	public int read(int size, org.omg.CORBA.StringHolder data) throws files.invalid_operation, files.end_of_file {	
		
		/* Deny access unless granted read authorization */
		if (rwx.value() != mode._read_only && rwx.value() != mode._read_write) {
			throw new files.invalid_operation("Access denied : cannot read the file named "+name);
		}

		/* Read the file */
		int char_read = 0;
		byte[] cbuf = new byte[size];
		try {
			char_read = raf.read(cbuf);
		} catch (Exception e1) {
		    throw new files.invalid_operation();
		}
		if(char_read == -1 ) throw new files.end_of_file();
		
		data.value = new String(cbuf, 0, char_read);
		
		return char_read;
	}

	public int write(int size, java.lang.String data) throws files.invalid_operation {
		/* Deny access for read only authorization */
		if (rwx.equals(mode.read_only))
			throw new files.invalid_operation();

        try {
		    if (rwx.equals(mode.write_append)) {
		    	raf.seek(raf.length()); // seek to the EOF
		    }
		    raf.writeBytes(data.substring(0, size));
		} catch (IOException e) {
		    throw new files.invalid_operation();
		}

		return data.length();
	}

	public void seek(int new_offset) throws files.invalid_offset, files.invalid_operation {
		/* Cannot update offset in write_append mode */
		if (rwx.equals(mode.write_append))
			throw new files.invalid_operation();

		if(new_offset < 0) throw new files.invalid_offset();
		
		/* Check new offset value */
		try {
			raf.seek(new_offset);
		} catch (IOException e) {
			throw new files.invalid_operation();
		}
	}

	public void close() {
		try {
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String name() {
		return name;
	}
}
