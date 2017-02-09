/**
 * Created on January 28th, 2017 for a project proposed by Mr Frank Singhoff 
 * as part of the teaching unit system objects distributed 
 * at the University of Western Brittany.
 */
package files;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Iterator for a {@link files.directory_entry} list.
 * @see files.idl
 */
public class file_listImpl extends file_listPOA
{
    private Iterator<directory_entry> iterator;
    
    /**
     * Create the iterator on the given list.
     * @param list The list on which iterate.
     */
    public file_listImpl(ArrayList<directory_entry> list){
    	iterator = list.iterator();
    }
    
    /**
     * Get the next entry
     * 
     * @param e An holder to store the value of the next entry
     * @return If there is a entry
     */
	public boolean next_one(files.directory_entryHolder e) {
	    boolean boo = iterator.hasNext();
	    if(boo) {
	    	e.value = iterator.next();
	    }
	    return boo;
	}
}
