/*
 * Created on 25 jan. 2017 under the authority of Franck Singhoff 
 * as part of practical work at the University of Western Brittany
 */
package files;

import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import java.io.*;

public class Serveur {
	public static void main(String[] args) throws IOException {

		try {
			// init ORB
			ORB orb = ORB.init(args, null);

			// init POA
			POA poa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			poa.the_POAManager().activate();

			directoryImpl directoryimpl = new directoryImpl(poa);
			org.omg.CORBA.Object alloc = poa.servant_to_reference(directoryimpl);

			try {
				String calc_ref = orb.object_to_string(alloc);
				String refFile = "files.ref";
				PrintWriter out = new PrintWriter(new FileOutputStream(refFile));
				out.println(calc_ref);
				out.close();
			} catch (IOException ex) {
				System.err.println("Impossible d'ecrire la reference dans files.ref");
				System.exit(1);
			}

			System.out.println("Le serveur est pret ");

			orb.run();

			System.exit(0);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
