/**
 * Created on February 2nd, 2017 for a project proposed by Mr Frank Singhoff 
 * as part of the teaching unit system objects distributed 
 * at the University of Western Brittany.
 */
package admin;

import org.omg.CORBA.ORB;

/**
 * Shutdown the server properly
 * 
 * @see admin.idl
 */
public class server_handlerImpl extends server_handlerPOA {

	private ORB orb;

	/**
	 * Construct an handler able to shutdown a CORBA Object Request Broker
	 */
	public server_handlerImpl(ORB orb)
	{
		this.orb = orb;
	}
	
	/**
	 * Shutdown the ORB
	 */
	@Override
	public void shutdown() {
		orb.shutdown(false);
	}

}
