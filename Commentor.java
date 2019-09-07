import java.io.IOException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

import settings.SettingsHolder;

public class Commentor implements Runnable {

	private String redditURL;
	private String toComment;
	private SettingsHolder settings;
	
	public Commentor(String redditURL,String toComment, SettingsHolder settings )
	{
	this.redditURL = redditURL;
	this.toComment=toComment;
	this.settings = settings;
	}
	
	
	public void run() {
		try {
			WebClient toComment = new Login((String)settings.getSetting("uname"),(String)settings.getSetting("pword"));
			toComment.getOptions().setThrowExceptionOnFailingStatusCode(false);
			toComment.getOptions().setThrowExceptionOnScriptError(false);
			toComment.getOptions().setCssEnabled(false);
			HtmlPage gotoPage = toComment.getPage(this.redditURL);
			System.out.println("good");
			((HtmlTextArea)gotoPage.querySelector("textarea")).setText(this.toComment);
			gotoPage = ((HtmlElement)gotoPage.querySelector(".save")).click();
			Thread.sleep(1000);
			System.out.println("Commented on " + this.redditURL);
			
			toComment.close();
		} catch (FailingHttpStatusCodeException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}

}
