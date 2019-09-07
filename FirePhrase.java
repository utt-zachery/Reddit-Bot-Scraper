
public class FirePhrase {

	private String firePhrase;
	private String messagePhrase;
	private String subReddit;
	
	public FirePhrase(String firePhrase,String messagePhrase, String subReddit )
	{
		this.firePhrase= firePhrase;
		this.messagePhrase = messagePhrase;
		this.subReddit = subReddit;
	}
	
	public String getSubReddit()
	{
		return this.subReddit;
	}
	public boolean contains(String testText)
	{
		for (String s : firePhrase.split(","))
		{
			if (testText.contains(s))
			{
				return true;
			}
		}
		return testText.contains(firePhrase);
	}
	
	public String firePhrase()
	{
		return this.firePhrase;
	}
	
	public String getMessagePhrase()
	{
		return this.messagePhrase;
	}
}
