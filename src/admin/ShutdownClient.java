/**
 * Created on February 2nd, 2017 for a project proposed by Mr Frank Singhoff as part of the teaching
 * unit system objects distributed at the University of Western Brittany.
 */
package admin;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.omg.CORBA.ORB;

/**
 * An admin client able to shutdown {@link files.Serveur}.
 *
 * @author Jimmy Tournemaine
 */
public class ShutdownClient {

    public ShutdownClient() {
        super();
    }

    public static void main(String[] argv) {

        ORB orb = ORB.init(argv, null);
        String ior = null;

        try {
            String ref = "admin.ref";
            FileInputStream file = new FileInputStream(ref);
            BufferedReader in = new BufferedReader(new InputStreamReader(file));
            ior = in.readLine();
            file.close();
        } catch (IOException ex) {
            System.err.println("Cannot read file : `" + ex.getMessage() + "'");
            System.exit(1);
        }

        org.omg.CORBA.Object obj = orb.string_to_object(ior);

        if (obj == null) {
            System.err.println("Erreur when doing string_to_object() ");
            throw new RuntimeException();
        }

        server_handler handler = server_handlerHelper.narrow(obj);

        if (handler == null) {
            System.err.println("Erreur when doing narrow() ");
            throw new RuntimeException();
        }

        handler.shutdown();
    }

}
