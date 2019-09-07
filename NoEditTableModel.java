import javax.swing.table.DefaultTableModel;

public class NoEditTableModel extends DefaultTableModel {


	private static final long serialVersionUID = 1L;

	public NoEditTableModel(String[][] data, String[] headers) {
		super(data, headers);
	}

	
	public boolean isCellEditable(int row, int column){  
          return false;  
      }
	
	

}