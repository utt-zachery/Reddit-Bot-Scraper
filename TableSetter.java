import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class TableSetter {

	public static void main(String[] args) {
		
		String[][] data = {{"abortion", "thats serious", "TwoXChromosomes","thing"}};
		
		String[] headers = {"Phrase Keywords", "Reply Content", "Subreddit", "lastPost"};
		NoEditTableModel m = new NoEditTableModel(data, headers);
		
		
		ObjectOutputStream oos = null;
		FileOutputStream fout = null;
		try{
		     fout =  new FileOutputStream("Resources" + File.separator + "table.dat", true);
		     oos = new ObjectOutputStream(fout);
		    oos.writeObject(m);
		} catch (Exception ex) {
		    ex.printStackTrace();
		} finally {
		    if(oos  != null){
		        try {
					oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    } 
		}
	}

}
