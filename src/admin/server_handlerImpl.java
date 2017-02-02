package admin;

import org.omg.CORBA.ORB;

public class server_handlerImpl extends server_handlerPOA {

	private ORB orb;
	
	public server_handlerImpl(ORB orb)
	{
		this.orb = orb;
	}
	
	@Override
	public void shutdown() {
		orb.shutdown(false);
	}

}
