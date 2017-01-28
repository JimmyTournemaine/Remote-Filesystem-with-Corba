package files;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class regular_fileImpl extends files.regular_filePOA {
	
	private String name;
	private mode rwx;
	private int offset;
	private File file;
	private FileReader rs;
	private FileWriter ws;

	public regular_fileImpl(File file, mode m) {
		this.file = file;
		rwx = m;
		offset = 0;
	}

	public int read(int size, org.omg.CORBA.StringHolder data) throws files.invalid_operation, files.end_of_file, files.io {

		/* Deny access unless granted read authorizarion */
		if (rwx.equals(mode.write_append) || rwx.equals(mode.write_trunc)) {
			throw new files.invalid_operation();
		}

		/* Read the file */
		int char_read;
		try {
			rs = new FileReader(file);
		    char[] cbuf = new char[size];
		    char_read = rs.read(cbuf, offset, size);
		    String str = new String(cbuf);
		    data.value = str;
		    
		    /* End of file reached */
		    if (-1 == char_read) {
			    throw new files.end_of_file();
		    }
		    rs.close();
		} catch (IOException e) {
		    throw new files.io(e.getMessage());
		}
		
		return char_read;
	}

	public int write(int size, java.lang.String data) throws files.invalid_operation, files.io {
		/* Deny access for read only authorization */
		if (rwx.equals(mode.read_only))
			throw new files.invalid_operation();

        try {
        	ws = new FileWriter(file);
		    if (rwx.equals(mode.write_append)) {
			    ws.append(data);
		    } else { // write_trunc or read_write
			    ws.write(data, offset, size);
		    }
		    ws.close();
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
		if (new_offset < 0 || new_offset >= file.length())
			throw new files.invalid_offset();
		
		offset = new_offset;
	}

	public void close() {
		try {
		    rs.close();
		    ws.close();
		} catch(Exception e) {
		    e.printStackTrace();
		}
	}
}
