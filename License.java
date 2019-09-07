

import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.swing.JOptionPane;

import settings.SettingsHolder;

public class License {

	public static void Validate(SettingsHolder gear)
	{
		if (((int)gear.getSetting("lcode")) != 5924)
		{
			try {
				String response = Executor.Execute("https://script.google.com/macros/s/AKfycbxImNh5dm61VWQdZLPvCzuZaALhCqVtJfgEWF194PfRwpxVaeI/exec?id=" + ProjectSpecifications.LICENSE);
		
				if (response.equals("1"))
				{
					gear.setSetting("lcode", 5924);
				}
				else if (response.equals("0"))
				{
					int n = 5924;
					while (n == 5924)
					{
						Random rand = new Random();
						n = rand.nextInt(9999 - 1000 + 1) + 1000;
					}
					gear.setSetting("lcode", n);
				}
				else
				{
					printFailure();
				}
			} catch (IOException e) {
				printFailure();
				e.printStackTrace();
			}
		}
		
	}
	
	public static void printFailure()
	{
	
		File settings = new File(ProjectSpecifications.convertPath("Resources//settings.txt"));
		settings.delete();
		JOptionPane.showMessageDialog(null,
			    "Due to a lack of contact, your license has been revoked.",
			    "Unlicensed",
			    JOptionPane.ERROR_MESSAGE);
		System.exit(-1);
	}
}
