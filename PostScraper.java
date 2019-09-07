import java.io.IOException;
import java.util.ArrayList;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import settings.SettingsHolder;

public class PostScraper implements Runnable {

	private String URL;
	private WebClient dog;
	private ArrayList<FirePhrase> toRun;
	private DoComment commentator;
	private SettingsHolder settings;
	
	public PostScraper(String URL, ArrayList<FirePhrase> toRun, DoComment commentator, SettingsHolder settings)
	{
		this.settings = settings;
		this.commentator=commentator;
		this.URL = URL;
		
		this.toRun = toRun;
	}
	
	public void run() {
		try {
			this.dog = new WebClient();
			this.dog.getOptions().setCssEnabled(false);
			this.dog.getOptions().setJavaScriptEnabled(false);
			HtmlPage p2 = this.dog.getPage(this.URL);
			
			if (p2.querySelectorAll(".expando").size() > 0)
			{
			String postData = p2.querySelector(".expando").asText();
		
			for (FirePhrase f : this.toRun)
			{
				if (f.contains(postData))
				{
				
					commentator.addComment((new Commentor(this.URL, f.getMessagePhrase(), settings)));
				}
			}
			
			}
		} catch (FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
		}
		
		
	}

}
