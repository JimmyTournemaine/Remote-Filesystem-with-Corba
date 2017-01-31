/*
 * Created on 25 jan. 2017 under the authority of Franck Singhoff 
 * as part of practical work at the University of Western Brittany
 */
package files.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.omg.CORBA.ORB;
import org.omg.CORBA.StringHolder;

import files.*;
import junit.framework.TestCase;

/**
 * This test case must be executed in a clean environment. Be sure that the root
 * folder exists and is empty before running this test.
 */
public class Test extends TestCase {

	private directory root;

	protected void setUp() throws Exception {
		ORB orb = ORB.init(new String[0], null);
		String ior = null;

		try {
			String ref = "files.ref";
			FileInputStream file = new FileInputStream(ref);
			BufferedReader in = new BufferedReader(new InputStreamReader(file));
			ior = in.readLine();
			file.close();
		} catch (IOException ex) {
			System.err.println("Impossible de lire fichier : `" + ex.getMessage() + "'");
			System.exit(1);
		}

		org.omg.CORBA.Object obj = orb.string_to_object(ior);

		if (obj == null) {
			System.err.println("Erreur sur string_to_object() ");
			throw new RuntimeException();
		}

		root = directoryHelper.narrow(obj);

		if (root == null) {
			System.err.println("Erreur sur narrow() ");
			throw new RuntimeException();
		}
	}

	public void testCreation() throws already_exist, io {
		directoryHolder f = new directoryHolder();
		regular_fileHolder r = new regular_fileHolder();
		directory toto;

		////////////////////////////////////////////////////
		// Hierarchy that will be made :
		//
		//        (root)
		//       /   |   \
		//    toto  titi  a
		//    /
		//   b
		////////////////////////////////////////////////////

		/* Legit creation */
		root.create_directory(f, "toto");
		toto = f.value;

		root.create_directory(f, "titi");
		root.create_regular_file(r, "a");
		toto.create_regular_file(r, "b");

		/* Folder already existing */
		try {
			root.create_directory(f, "toto");
			fail("An exception should be thrown.");
		} catch (files.already_exist e) {
		}

		/* File already existing + relative path */
		try {
			root.create_regular_file(r, "toto/b");
			fail("An exception should be thrown.");
		} catch (files.already_exist e) {
		}
	}

	public void testOpening() throws no_such_file, invalid_type_file, io {
		directoryHolder f = new directoryHolder();
		regular_fileHolder r = new regular_fileHolder();

		/* Must work */
		root.open_directory(f, "toto");
		root.open_directory(f, "titi");
		root.open_regular_file(r, "a", mode.read_only);
		root.open_regular_file(r, "toto/b", mode.read_only);

		/* No such file */
		try {
			root.open_directory(f, "tata");
			fail("An exception should be thrown.");
		} catch (no_such_file e) {
		}

		try {
			root.open_regular_file(r, "truc", mode.read_only);
			fail("An exception should be thrown.");
		} catch (no_such_file e) {
		}

		/* Invalid type */
		try {
			root.open_directory(f, "a");
			fail("An exception should be thrown.");
		} catch (invalid_type_file e) {
		}

		try {
			root.open_regular_file(r, "toto", mode.read_only);
			fail("An exception should be thrown.");
		} catch (invalid_type_file e) {
		}
	}

	public void testListing() {
		directory_entryHolder eh = new directory_entryHolder(new directory_entry("", file_type.regular_file_type));
		file_listHolder lh = new file_listHolder();
		root.list_files(lh);
		String str = "";
		while (lh.value.next_one(eh)) {
			str += String.format("%s%d", eh.value.name, eh.value.type.value());
		}
		assertEquals("a0titi1toto1", str);
	}

	public void testReadWriteMode() throws Exception {
		regular_fileHolder f = new regular_fileHolder();
		root.open_regular_file(f, "a", mode.read_write);
		regular_file a = f.value;

		/* Writing */
		String message = "I'm writing in a.";
		a.write(message.length(), message); // I'm writing in a.
		a.seek(message.length());
		message = " I append some text now.";
		a.write(message.length() - 5, message); // I append some text

		/* Reading */
		StringHolder sh = new StringHolder(new String());
		a.seek(0);
		assertEquals(36, a.read(36, sh));
		assertEquals("I'm writing in a. I append some text", sh.value);

		/* Bad offset */
		try {
			a.seek(50);
		} catch (files.invalid_offset e) {
		}

		/* Close */
		a.close();
	}

	public void testZeroTerminatedByte() throws Exception {
		StringHolder sh = new StringHolder("");
		regular_fileHolder f = new regular_fileHolder();
		root.open_regular_file(f, "a", mode.read_only);
		regular_file a = f.value;

		/* Zero-terminated byte */
		assertEquals(36, a.read(50, sh));
		assertEquals("I'm writing in a. I append some text", sh.value);
	}

	public void testReadOnlyMode() throws Exception {
		regular_fileHolder f = new regular_fileHolder();
		root.open_regular_file(f, "a", mode.read_only);
		regular_file a = f.value;

		/* Writing */
		try {
			String message = "I'm writing in a.";
			a.write(message.length(), message); // I'm writing in a.
			fail("Should not write in read only mode.");
		} catch (files.invalid_operation e) {
		}

		/* Reading */
		StringHolder sh = new StringHolder(new String());
		assertEquals(36, a.read(36, sh));
		assertEquals("I'm writing in a. I append some text", sh.value);

		/* Close */
		a.close();
	}
	
	public void testWriteAppendMode() throws Exception {
		regular_fileHolder f = new regular_fileHolder();
		root.open_regular_file(f, "a", mode.write_append);
		regular_file a = f.value;

		/* Writing */
		String message = "I'm writing in a.";
		a.write(message.length(), message); // I'm writing in a.
		message = " I append some text now.";
		a.write(message.length() - 5, message); // I append some text

		/* Reading */
		StringHolder sh = new StringHolder(new String()); 
		try {
			a.read(36, sh);
			fail("Should not read in write_append mode.");
		} catch(files.invalid_operation e){}
		
		/* Seek forbidden : only append */
		try {
			a.seek(0);
			fail("Should throw an invalid_operation expected.");
		} catch (files.invalid_operation e) {}
		
		/* Close */
		a.close();
		
		/* Check data */
		root.open_regular_file(f, "a", mode.read_only);
		a = f.value;
		a.read(72, sh);
		assertEquals("I'm writing in a. I append some textI'm writing in a. I append some text", sh.value);
		a.close();
	}
	
	public void testWriteTruncMode() throws Exception {
		regular_fileHolder f = new regular_fileHolder();
		root.open_regular_file(f, "a", mode.write_trunc);
		regular_file a = f.value;

		/* Writing */
		String message = "I'm truncating the file.";
		a.write(message.length(), message);

		/* Reading */
		StringHolder sh = new StringHolder(new String());
		try {
			a.read(message.length(), sh);
			fail("Cannot read in write_trunc mode");
		} catch(files.invalid_operation i){}

		/* Close */
		a.close();
		
		/* Check data */
		root.open_regular_file(f, "a", mode.read_only);
		a = f.value;
		a.read(message.length()+36, sh); // +36 to check that previous text has been erased.
		assertEquals("I'm truncating the file.", sh.value);
	}
}
