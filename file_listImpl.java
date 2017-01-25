package files;

import java.util.Iterator;

public class file_listImpl extends file_listPOA
{
    private Iterator<directory_entry> iterator;
    
    public file_listImpl(ArrayList<directory_entry> list){
    	iterator = list.iterator();
    }
    
	public boolean next_one(files.directory_entryHolder e) {
	    boolean boo = iterator.hasNext();
	    if(boo)
	      e.value = iterator.next();
	    return boo;
	}
}
