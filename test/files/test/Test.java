package files.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.omg.CORBA.ORB;
import org.omg.CORBA.StringHolder;

import files.*;
import junit.framework.TestCase;

public class Test extends TestCase {

	private directory root;

	/**
	 * @Before
	 */
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

	/**
	 * @throws already_exist
	 * @throws io
	 * @Test
	 */
	public void testCreation() throws already_exist, io {
		directoryHolder f = new directoryHolder();
		regular_fileHolder r = new regular_fileHolder();
		directory toto;

		////////////////////////////////////////////////////
		// Jeu de tests
		//
		// (root)
		// / | \
		// toto titi a
		// /
		// b
		////////////////////////////////////////////////////

		/* Création de la hiérarchie */
		root.create_directory(f, "toto");
		toto = f.value;

		root.create_directory(f, "titi");
		root.create_regular_file(r, "a");
		toto.create_regular_file(r, "b");
	}

	/**
	 * @throws invalid_type_file
	 * @throws no_such_file
	 * @Test
	 */
	public void testOpenDirectory() throws no_such_file, invalid_type_file {
		directoryHolder f = new directoryHolder();

		root.open_directory(f, "toto");
		root.open_directory(f, "titi");
	}

	/**
	 * @throws io
	 * @throws invalid_type_file
	 * @throws no_such_file
	 * @throws invalid_operation
	 * @throws invalid_offset
	 * @Test
	 */
	public void testRead() throws no_such_file, invalid_type_file, io, invalid_operation, invalid_offset {
		regular_fileHolder f = new regular_fileHolder();
		root.open_regular_file(f, "a", mode.read_write);
		root.open_regular_file(f, "toto/b", mode.read_write);
		regular_file a = f.value;
		String message = "I'm writing in a.";

		a.write(message.length(), message); // I'm writing in a.
		a.seek(message.length());
		message = " I append some text now.";
		a.write(message.length() - 5, message); // I append some text

		/* Reading */
		StringHolder sh = new StringHolder(new String());
		try {
			a.seek(0);
			assertEquals(36, a.read(50, sh));
			fail("An exception files.end_of_file should be thrown.");
		} catch (files.end_of_file eof) {
		}
		assertEquals("I'm writing in a. I append some text", sh.value);

		/* Write append in b */
		root.open_regular_file(f, "toto/b", mode.write_append);
		regular_file b = f.value;
		try {
			b.seek(0);
			fail("File open with write_append cannot call seek");
		} catch (files.invalid_operation io) {
		}
		fail();
	}
}
