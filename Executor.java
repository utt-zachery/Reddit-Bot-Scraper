

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Executor {

	public static String Execute(URL toGo) throws IOException
	{
		String toReturn = "";
		URLConnection con;
			con = toGo.openConnection();
			InputStream is =con.getInputStream();
		   BufferedReader br = new BufferedReader(new InputStreamReader(is));
		   String inputLine;
		   while ((inputLine = br.readLine()) != null)
		   {
			   toReturn = toReturn + inputLine;
		   }
		   br.close();
		        return toReturn;
	}
	
	public static String Execute(String toGo) throws IOException
	{
		return Execute(new URL(toGo));
	}
}
