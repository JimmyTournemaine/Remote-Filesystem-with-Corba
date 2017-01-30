package files.test;

import files.*;

import org.omg.CORBA.*;
import java.io.*;

public class Test {

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
		file_listHolder lh = new file_listHolder();
		directory_entryHolder de = new directory_entryHolder();
		directory toto, titi;
		regular_file a, b, c;
		String message;
		
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
		    
		    message = "I'm writing in a.";
		    a.write(message.length(), message); // I'm writing in a.
		    a.seek(message.length());
		    message = " I append some text now.";
		    a.write(message.length()-5, message); // I append some text
		    
		    /* Reading */
		    StringHolder sh = new StringHolder(new String());
		    try {
			    a.seek(0);
			    System.out.println("read : "+a.read(50, sh));
		    } catch(files.end_of_file eof) { // EOF should be thrown
		    	System.out.println(sh.value);
		    	if(!sh.value.equals("I'm writing in a. I append some text")) 
			    	throw new Exception ("String read does not corresponding with the written one.");
		    }
		    
		    /* Write append in b */
		    root.open_regular_file(r, "toto/b", mode.write_append);
		    b = r.value;
		    try {
		    	b.seek(0);
		    } catch(files.invalid_operation io) { // Cannot call seek in write_append mode
		    	
		    }
		    
		} catch(files.io io) {
			System.out.println("I/O Error :" + io.getMessage());
		} catch(files.invalid_operation io1) {
			System.out.println("Invalid operation :" + io1.getMessage());
		} catch(Exception e) {
		    e.printStackTrace();
		}
	}
}
