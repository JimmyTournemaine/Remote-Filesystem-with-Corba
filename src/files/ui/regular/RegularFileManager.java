package files.ui.regular;

import org.omg.CORBA.StringHolder;

import files.end_of_file;
import files.regular_file;

public class RegularFileManager {

	private regular_file file;
	private final static int BUFFER_SIZE = 256;
	
	public RegularFileManager(regular_file file) {
		this.file = file;
	}
	
	public String readAll() {
		StringHolder stringHolder = new StringHolder("");
		String content = new String();
		try {
			file.seek(0);
			while(file.read(BUFFER_SIZE, stringHolder) > 0) {
				content += stringHolder.value;
			}
		} catch(end_of_file e) { // stop reading
		} catch(Exception e) {
			e.printStackTrace();
		} 
		
		return content;
	}
	
	public void writeAll(String text) {
		try {
			file.seek(0);
			file.write(text.length(), text);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
