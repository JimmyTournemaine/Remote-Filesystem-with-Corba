/*
 * Created on 25 jan. 2017 under the authority of Franck Singhoff 
 * as part of practical work at the University of Western Brittany
 */
package files;

import org.omg.CORBA.*;
import org.omg.PortableServer.*;

import admin.server_handlerImpl;

import java.io.*;	

public class Serveur {
	public static void main(String[] args) throws IOException {

		try {
			final ORB orb = ORB.init(args, null);
			POA poa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			poa.the_POAManager().activate();

			directoryImpl directoryimpl = new directoryImpl(poa);
			org.omg.CORBA.Object alloc = poa.servant_to_reference(directoryimpl);
			
			server_handlerImpl handlerImpl = new server_handlerImpl(orb);
			org.omg.CORBA.Object obj = poa.servant_to_reference(handlerImpl);

			try {
				PrintWriter out = new PrintWriter(new FileOutputStream("files.ref"));
				out.println(orb.object_to_string(alloc));
				out.close();
				out = new PrintWriter(new FileOutputStream("admin.ref"));
				out.println(orb.object_to_string(obj));
				out.close();
			} catch (IOException ex) {
				System.err.println("Cannot write references");
				System.exit(1);
			}

			System.out.println("Server is ready.");
			
			orb.run();

			System.exit(0);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
