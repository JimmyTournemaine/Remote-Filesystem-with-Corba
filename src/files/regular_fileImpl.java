/**
 * Created on January 28th, 2017 for a project proposed by Mr Frank Singhoff as part of the teaching
 * unit system objects distributed at the University of Western Brittany.
 */
package files;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * An implementation of {@link files.regular_file } interface defined by
 * <a href="../../files.idl">files.idl</a>
 */
public class regular_fileImpl extends files.regular_filePOA {

    private String name;
    private mode rwx;
    private RandomAccessFile raf;

    /**
     * Construct a regular file
     *
     * @param file The file store on the server
     * @param m The mode
     * @throws IOException
     */
    public regular_fileImpl(File file, mode m) throws IOException {
        name = file.getName();
        raf = new RandomAccessFile(file, "rw");
        rwx = m;
        switch (m.value()) {
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

    /**
     * Read {@code size} characters in the file from {@code offset}. The file must has been opened
     * in read_only or read_write mode.
     *
     * To move the offset see {@link #seek(int)}
     *
     * @param size The number of characters to read.
     * @param data An holder on the data read.
     * @return The number of characters read.
     * @throws invalid_operation The opening mode does not allow reading.
     * @throws end_of_file The end of file is reached before starting to read.
     * @throws io If an I/O error occurred.
     */
    @Override
    public int read(int size, org.omg.CORBA.StringHolder data)
            throws files.invalid_operation, files.end_of_file, files.io {

        /* Deny access unless granted read authorization */
        if (rwx.value() != mode._read_only && rwx.value() != mode._read_write)
            throw new files.invalid_operation();

        /* Read the file */
        int char_read = 0;
        byte[] cbuf = new byte[size];
        try {
            char_read = raf.read(cbuf);
        } catch (IOException e1) {
            throw new io();
        }
        if (char_read == -1) throw new files.end_of_file();

        data.value = new String(cbuf, 0, char_read);

        return char_read;
    }

    /**
     * Write {@code size} characters of the given data in the file from the offset. The file must
     * has been opened with a write access. In write_append mode, the offset is always put at the
     * end of the file.
     *
     * @param size The number of characters to write.
     * @param data Data to write.
     * @return The number of characters wrote.
     * @throws invalid_operation Signal that the mode does not allow write access.
     * @throws io If an I/O error occurred or that {@code size} is greater that the data length.
     */
    @Override
    public int write(int size, java.lang.String data) throws files.invalid_operation, files.io {
        /* Deny access for read only authorization */
        if (rwx.equals(mode.read_only)) throw new files.invalid_operation();

        try {
            raf.writeBytes(data.substring(0, size));
        } catch (IOException e) {
            throw new files.io();
        } catch (IndexOutOfBoundsException e) {
            throw new files.io();
        }

        return size;
    }

    /**
     * Sets the file-pointer offset.
     *
     * @param new_offset The new value of the offset.
     * @throws invalid_offset The offset must be strictly positive.
     * @throws invalid_operation The offset can not be set in write_append mode.
     * @throws io If an I/O error occurred.
     */
    @Override
    public void seek(int new_offset)
            throws files.invalid_offset, files.invalid_operation, files.io {
        /* Cannot update offset in write_append mode */
        if (rwx.equals(mode.write_append)) throw new files.invalid_operation();

        if (new_offset < 0) throw new files.invalid_offset();

        /* Check new offset value */
        try {
            raf.seek(new_offset);
        } catch (IOException e) {
            throw new files.io();
        }
    }

    /**
     * Close the stream
     */
    @Override
    public void close() {
        try {
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String name() {
        return name;
    }
}
