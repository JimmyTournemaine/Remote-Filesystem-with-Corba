package files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.IOException;

public class regular_fileImpl extends files.regular_filePOA {
	
	private mode rwx;
	private RandomAccessFile raf;

	public regular_fileImpl(File file, mode m) throws FileNotFoundException {
		raf = new RandomAccessFile(file, "rw");
		rwx = m;
	}

	public int read(int size, org.omg.CORBA.StringHolder data) throws files.invalid_operation, files.end_of_file, files.io {

		/* Deny access unless granted read authorizarion */
		if (rwx.equals(mode.write_append) || rwx.equals(mode.write_trunc)) {
			throw new files.invalid_operation();
		}

		/* Read the file */
		int char_read = 0;
		try {
			char[] cbuf = new char[size];
			for(int i=0; i<size; i++){
				cbuf[i] = raf.readChar();
				char_read++;
			}
		    data.value = new String(cbuf);
		    
		} catch(java.io.EOFException e) {
			throw new files.end_of_file(e.getMessage());
		}
		catch (IOException e1) {
		    throw new files.io(e1.getMessage());
		}
		
		return char_read;
	}

	// TODO write only size characters
	public int write(int size, java.lang.String data) throws files.invalid_operation, files.io {
		/* Deny access for read only authorization */
		if (rwx.equals(mode.read_only))
			throw new files.invalid_operation();

        try {
		    if (rwx.equals(mode.write_append)) {
		    	raf.seek(raf.length());
			    raf.writeUTF(data);
		    } else { // write_trunc or read_write
		    	raf.writeUTF(data);
		    }
		} catch (IOException e) {
		    throw new files.io(e.getMessage());
		}

		return data.length();
	}

	public void seek(int new_offset) throws files.invalid_offset, files.invalid_operation {
		/* Cannot update offset in write_append mode */
		if (rwx.equals(mode.write_append))
			throw new files.invalid_operation();

		/* Check new offset value */
		try {
			raf.seek(new_offset);
		} catch (IOException e) {
			throw new files.invalid_offset();
		}
	}

	public void close() {}
}
