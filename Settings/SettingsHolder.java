package settings;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class SettingsHolder {

	private ArrayList<Setting> gears;
	
	public SettingsHolder() 
	{
		this.gears = new ArrayList<Setting>();
		File datacenter = new File("Resources"+ File.separator + "settings.txt");
		try {
			Scanner reader = new Scanner(datacenter);
			String input = "";
			while (reader.hasNext())
			{
				input = input + reader.next();
			}
			for (String subSetting : input.split(";"))
			{
				if (subSetting.length()>0)
				{
				if (subSetting.charAt(0) == '0')
				{
					if (subSetting.charAt(subSetting.length()-1) == '1')
					{
						gears.add(new Setting(subSetting.substring(1, subSetting.lastIndexOf("=")),Boolean.TRUE, Boolean.class));
					}
					else
					{
						gears.add(new Setting(subSetting.substring(1, subSetting.lastIndexOf("=")),Boolean.FALSE, Boolean.class));
					}
				}
				if (subSetting.charAt(0) == '1')
				{
					gears.add(new Setting(subSetting.substring(1, subSetting.lastIndexOf("=")),Integer.parseInt(subSetting.substring(subSetting.lastIndexOf("=")+1)), Integer.class));
				}
				if (subSetting.charAt(0) == '2')
				{
					gears.add(new Setting(subSetting.substring(1, subSetting.lastIndexOf("=")),subSetting.substring(subSetting.lastIndexOf("=")+1), String.class));
				}
			}
			reader.close();
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,
				    "Due to a lack of contact, your license has been revoked.",
				    "Unlicensed",
				    JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
		
	}
	
	public Object getSetting(String name)
	{
		for (Setting s: gears)
		{
			if (name.equals(s.getName()))
			{
				if (s.getType() == Boolean.class)
				{
					
						
						return (Boolean)s.getValue();
					
					
				}
				return s.getValue();
			}
		}
		return null;
	}
	
	public boolean setSetting(String name, Object value)
	{
		boolean isBoolean = false;
		if (value.getClass() == Boolean.class)
		{
			isBoolean = true;
			if (value == Boolean.TRUE)
			{
				value = 1;
			}
			if (value == Boolean.FALSE)
			{
				value = 0;
			}
		}
		for (Setting s: gears)
		{
			if (name.equals(s.getName()))
			{
				return s.setSetting(value);
			}
		}
		gears.add(new Setting(name,value,value.getClass()));
		
		BufferedWriter out;
		try {
		out = new BufferedWriter(new FileWriter("Resources"+ File.separator + "settings.txt",true));
		int output = 0;
		if (value.getClass() == String.class)
		{
			output = 2;
		}
		if (value.getClass() == Integer.class)
		{
			output = 1;
		}
		if (isBoolean)
		{
			output = 0;
		}
        out.write(output + name + "=" + value+";");
        out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public void printAll()
	{
		for (Setting s: gears)
		{
			System.out.println(s.toString());
		}
	}
}
