package settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;



public class Setting {

	private Object value;
	private Class<?> type;
	private String name;
	
	public Setting(String name, Object value, Class<?> type) {
		
			this.name = name;
			this.value = value;
			this.type = type;
	
	}
	
	public boolean setSetting(Object value)
	{
		this.value = value;
		File datacenter = new File("Resources"+ File.separator + "settings.txt");
		try {
			Scanner reader = new Scanner(datacenter);
			String input = "";
			while (reader.hasNext())
			{
				input = input + reader.next();
			}
			reader.close();
			int startIndex = input.indexOf(this.name);
			String reput = input.substring(startIndex);
			String filtered = reput.substring(0, reput.indexOf(";"));
			String output = input.replace(filtered, filtered.split("=")[0] + "="+value);
			PrintWriter saveLine = new PrintWriter(datacenter, "UTF-8");
			saveLine.write(output);
			saveLine.close();
			return true;
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Class<?> getType()
	{
		return this.type;
	}
	public String getName()
	{
		return this.name;
	}
	@Override
	public String toString()
	{
		return this.type.getName() + " " +this.name + " = "+ this.value;
	}
	
	public Object getValue()
	{
		return this.value;
	}
}
