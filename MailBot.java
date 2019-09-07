import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.DefaultListModel;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import settings.SettingsHolder;

public class MailBot implements Runnable{

	private SettingsHolder settings;
	
	public MailBot(SettingsHolder settings) {
		super();
		this.settings = settings;
	}

	
	@Override
	public void run() {
		WebClient mailman = new Login((String)settings.getSetting("uname"), (String)settings.getSetting("pword"));
		mailman.getOptions().setCssEnabled(false);
		mailman.getOptions().setJavaScriptEnabled(false);
		try {
			HtmlPage inbox = mailman.getPage("https://www.reddit.com/message/inbox/");
			for (DomNode d : inbox.querySelectorAll(".thing"))
			{
				if (((HtmlElement)d).getAttribute("id").equals((String)settings.getSetting("lastPost")))
				{
					settings.setSetting("lastPost", ((HtmlElement)inbox.querySelector(".thing")).getAttribute("id"));
					mailman.close();
					return;
				}
				String from = ((HtmlElement)d).getAttribute("data-author");
				String url = "https://reddit.com" + ((HtmlElement)d.querySelector("a")).getAttribute("href");
				String postTitle = d.querySelector("a").asText();
				String subReddit = "";
				String content = d.querySelector(".md").asText();
				if (d.querySelectorAll(".subreddit").size() > 0 && (Boolean)settings.getSetting("sendEmailReply"))
				{
					subReddit = d.querySelector(".subreddit").asText();
				
					String body = "The Reddit Bot received a comment. <br/></br><b>From</b>: " + from + "<br/><b>Post Title</b>: " +postTitle + "<br/><b>Subreddit</b>: " + subReddit + "<br/><b>Post content: </b>" + content + "<br/><br/><b>Access this post at the following link</b>: " +  url;
					
					ObjectInputStream objectinputstream = null;
					try {
						FileInputStream streamIn = new FileInputStream("Resources" + File.separator+ "emails.dat");
					     objectinputstream = new ObjectInputStream(streamIn);
				
						@SuppressWarnings("unchecked")
						DefaultListModel<String> emails= (DefaultListModel<String>) objectinputstream.readObject();
						for (int i=0; i<emails.size(); i++)
						{
							EmailService.generateAndSendEmail(emails.getElementAt(i), body);
						}
					     streamIn.close();
					} catch (Exception e) {
					    e.printStackTrace();
					}
					
					
				}
				else
				{
					if ((Boolean)settings.getSetting("sendEmailPM"))
					{
						url = ((HtmlElement)d.querySelector(".bylink")).getAttribute("href");
					postTitle = d.querySelector(".subject-text").asText();
					String body = "The Reddit Bot received a Private Message. <br/></br><b>From</b>: " + from + "<br/><b>Message Title</b>: " +postTitle + "<br/><b>Message content: </b>" + content + "<br/><br/><b>Access this message at the following link</b>: " +  url;
					ObjectInputStream objectinputstream = null;
					try {
						FileInputStream streamIn = new FileInputStream("Resources" + File.separator+ "emails.dat");
					     objectinputstream = new ObjectInputStream(streamIn);
				
						@SuppressWarnings("unchecked")
						DefaultListModel<String> emails= (DefaultListModel<String>) objectinputstream.readObject();
						for (int i=0; i<emails.size(); i++)
						{
							EmailService.generateAndSendEmail(emails.getElementAt(i), body);
						}
					     streamIn.close();
					} catch (Exception e) {
					    e.printStackTrace();
					}
					
					}
				}
				
			}
			settings.setSetting("lastPost", ((HtmlElement)inbox.querySelector(".thing")).getAttribute("id"));
			
		} catch (FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
		}
		mailman.close();
	}

	
}
