import java.io.File;

public class ProjectSpecifications {

	public final static String TITLE = "Reddit Bot";
	public final static String LICENSE = "p4e0Q4DB6Rj9uCtknoF0fIcbJ";
	
	public static String convertPath(String path)
	{
		return path.replace("\\", File.separator);
	}
}
