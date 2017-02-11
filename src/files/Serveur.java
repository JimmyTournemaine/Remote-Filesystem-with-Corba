/**
 * Created on January 28th, 2017 for a project proposed by Mr Frank Singhoff as part of the teaching
 * unit system objects distributed at the University of Western Brittany.
 */
package files;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import admin.server_handlerImpl;

/**
 * The server which handle the remote file system.
 *
 * @author Pierre Siguret
 * @author Jimmy Tournemaine
 */
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
