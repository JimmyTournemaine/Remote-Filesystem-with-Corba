package files;

public class file_listImpl extends file_listPOA
{
    private Iterator<directory_entry> iterator;
    
    public file_listImpl(ArrayList<directory_entry> list){
        list.iterator();
    }
    
	public boolean next_one(files.directory_entryHolder e) {
	    boolean boo = it.hasNext();
	    if(boo)
	      e.value = it.next();
	    return boo;
	}
}
