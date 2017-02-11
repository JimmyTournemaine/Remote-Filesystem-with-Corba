/**
 * Created on February 6th, 2017 for a project proposed by Mr Frank Singhoff as part of the teaching
 * unit system objects distributed at the University of Western Brittany.
 */
package files.ui.regular;

import org.omg.CORBA.StringHolder;
import files.access_denied;
import files.directory;
import files.end_of_file;
import files.invalid_type_file;
import files.io;
import files.mode;
import files.no_such_file;
import files.regular_file;
import files.regular_fileHolder;

/**
 * Manage a regular file
 *
 * @author Jimmy Tournemaine
 */
public class RegularFileManager {

    private directory current;
    private String filename;
    private final static int BUFFER_SIZE = 256;

    public RegularFileManager(directory dir, String name) {
        current = dir;
        filename = name;
    }

    /**
     * Read all the file contents.
     *
     * @throws io If any sort of I/O problem occurred.
     * @throws invalid_type_file If the name does not corresponding to a file.
     * @throws access_denied The file is above the root of the file system.
     * @throws no_such_file Any filename of this name exists.
     */
    public String readAll() throws no_such_file, access_denied, invalid_type_file, io {

        regular_fileHolder fileHolder = new regular_fileHolder();
        current.open_regular_file(fileHolder, filename, mode.read_only);
        regular_file file = fileHolder.value;

        StringHolder stringHolder = new StringHolder("");
        String content = new String();
        try {
            while (file.read(BUFFER_SIZE, stringHolder) > 0) {
                content += stringHolder.value;
            }
        } catch (end_of_file e) { // stop the reading
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            file.close();
        }

        return content;
    }

    /**
     * Write all the text in the file. Open the file in write_trunc to be sure that old data will be
     * overridden.
     *
     * @throws io If any sort of I/O problem occurred.
     * @throws invalid_type_file If the name does not corresponding to a file.
     * @throws access_denied The file is above the root of the file system.
     * @throws no_such_file Any filename of this name exists.
     */
    public void writeAll(String text) throws no_such_file, access_denied, invalid_type_file, io {

        regular_fileHolder fileHolder = new regular_fileHolder();
        current.open_regular_file(fileHolder, filename, mode.write_trunc);
        regular_file file = fileHolder.value;
        try {
            fileHolder.value.write(text.length(), text);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            file.close();
        }
    }
}
