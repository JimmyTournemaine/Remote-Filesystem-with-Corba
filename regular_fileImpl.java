package files;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringWriter;

public class regular_fileImpl extends files.regular_filePOA {
	
	private String name;
	private Mode rwx;
	private int offset;
	private File file;
	private FileReader rs;
	private FileWriter ws;

	public regular_fileImpl(File file, mode m) {
		this.file = file;
		rwx = m;
		rs = new FileReader(file);
		ws = new FileWriter(file);
		offset = 0;
	}

	public int read(int size, org.omg.CORBA.StringHolder data) throws files.invalid_operation, files.end_of_file {

		/* Deny access unless granted read authorizarion */
		if (rwx.equals(mode.write_append) || rwx.equals(mode.write_trunc)) {
			throw new files.invalid_operation();
		}

		/* Read the file */
		char[] cbuf = new char[size];
		int char_read = rs.read(cbuf, offset, length);
		data.value = new String(cbuf);

		/* End of file reached */
		if (-1 == char_read) {
			throw new files.end_of_file();
		}
	}

	public int write(int size, java.lang.String data) throws files.invalid_operation {
		/* Deny access for read only authorization */
		if (rwx.equals(mode.read_only))
			throw new files.invalid_operation();

		if (rwx.equals(mode.write_append)) {
			ws.append(data);
		} else { // write_trunc or read_write
			ws.write(data, offset, size);
		}
		sw.write(content);
		sw.write(data, offset, size);
		content = sw.toString();

		return size;
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
		rs.close();
		ws.close();
	}
}
