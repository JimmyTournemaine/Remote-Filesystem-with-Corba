package files;

import org.omg.CORBA.*;
import java.io.*;
import java.lang.*;
import java.util.*;

public class Client {

	public static void main(String[] args) throws IOException {

		ORB orb = ORB.init(args, null);
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

		directory root = directoryHelper.narrow(obj);

		if (root == null) {
			System.err.println("Erreur sur narrow() ");
			throw new RuntimeException();
		}

		directoryHolder f = new directoryHolder();
		regular_fileHolder r = new regular_fileHolder();
		directory toto, titi;
		regular_file a, b, c;
		
		////////////////////////////////////////////////////
		// Jeu de tests
		//
		//			(root)
		//		   /  |	 \
		//	   toto  titi  a
		//		/		
		//	   b
		////////////////////////////////////////////////////
		
		/* Création de la hiérarchie */
		try {
		    root.create_directory(f, "toto");
		    toto = f.value;
		
		    root.create_directory(f, "titi");
		    titi = f.value;
		
		    root.create_regular_file(r, "a");
		    a = r.value;
		
		    toto.create_regular_file(r, "b");
		    b = r.value;
		} catch(Exception e) {
		    System.out.println(e.getMessage());
		}
	}
}
