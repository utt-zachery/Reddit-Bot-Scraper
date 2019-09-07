import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import settings.SettingsHolder;


public class Startup
{
	
	public static void main(String args[]) {
		try {
			
				SettingsHolder gears = new SettingsHolder();
				new Thread(new MailBot(gears)).start();
				FileInputStream streamIn = new FileInputStream("Resources" + File.separator+ "table.dat");
				ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);
			    NoEditTableModel readModel = (NoEditTableModel) objectinputstream.readObject();
			    streamIn.close();
			    objectinputstream.close();
			    
			    ArrayList<FirePhrase> triggers = new ArrayList<FirePhrase>();
			    ArrayList<String> subreddits = new ArrayList<String>();
			    ArrayList<String> lastPosts = new ArrayList<String>();
			    
			     for (int r = 0; r< readModel.getRowCount(); r++)
			     {
			    	 if (!subreddits.contains((String)readModel.getValueAt(r, 2)))
			    	 {
			    		 subreddits.add((String)readModel.getValueAt(r, 2));
			    		 lastPosts.add((String)readModel.getValueAt(r, 3));
			    	 }
			    	 triggers.add(new FirePhrase((String)readModel.getValueAt(r, 0), (String)readModel.getValueAt(r, 1),(String)readModel.getValueAt(r, 2)));
			     }
			     DoComment commentator = new DoComment(gears);
			     WebClient browser = new WebClient();
					browser.getOptions().setCssEnabled(false);
					browser.getOptions().setJavaScriptEnabled(false);
					
			for (int i = 0; i<subreddits.size(); i++)
			{
				
				HtmlPage rpage = browser.getPage("https://www.reddit.com/r/"+subreddits.get(i));
				readModel.setValueAt(((HtmlElement)rpage.querySelector(".thing")).getAttribute("id"), i, 3);
				
				String[][] goodData = new String[readModel.getRowCount()][4];
				for (int count = 0; count < readModel.getRowCount(); count++){
					goodData[count][0] = (String)readModel.getValueAt(count, 0);
					goodData[count][1] = (String)readModel.getValueAt(count, 1);
					goodData[count][2] = (String)readModel.getValueAt(count, 2);
					goodData[count][3] = (String)readModel.getValueAt(count, 3);
					}
				String[] headers = {"Phrase Keywords", "Reply Content", "Subreddit","lastPost"};
				NoEditTableModel m = new NoEditTableModel(goodData, headers);
				
				File old = new File("Resources" + File.separator + "table.dat");
				old.delete();
				ObjectOutputStream oos = null;
				FileOutputStream fout = null;
				try{
				     fout =  new FileOutputStream("Resources" + File.separator + "table.dat", true);
				     oos = new ObjectOutputStream(fout);
				    oos.writeObject(m);
				    oos.close();
				} catch (Exception ex) {
				    ex.printStackTrace();
				}
				
				outerloop:
				for (int page = 0; page < (int)gears.getSetting("viewpage");page++ )
				{
				System.out.println("Page: " + (1+page));
		
			DomNodeList<DomNode> posts= rpage.querySelectorAll(".thing");
			for (DomNode post : posts)
			{
				if (((DomElement) post).getAttribute("id").equals(lastPosts.get(i)))
				{
					System.out.println("**** DONE ****");
					break outerloop;
				}
				if (((DomElement) post).getAttribute("class").contains("self"))
				{
					for (DomElement e: ((DomElement)post).getChildElements())
					{
						if (e.getAttribute("class").contains("entry"))
						{
							boolean found = false;
							
							innertube:
							for (FirePhrase f : triggers)
							{
								
								if (f.contains(e.getFirstElementChild().getFirstElementChild().asText()))
								{
									found = true;
									commentator.addComment(new Commentor("https://reddit.com"+ ((DomElement)post).getAttribute("data-url"), f.getMessagePhrase(), gears));
									break innertube;
								}
							}
							
							if (!found)
							{
								System.out.println( e.getFirstElementChild().getFirstElementChild().asText());
								(new PostScraper("https://reddit.com"+ ((DomElement)post).getAttribute("data-url"), triggers, commentator, gears)).run();
							}
							
							
							break;
						}
					}
				
				
					}
			}
			rpage = browser.getPage("https://www.reddit.com/r/"+subreddits.get(i) + "/?count=25&after=" + ((HtmlElement)(rpage.querySelectorAll(".thing").get(rpage.querySelectorAll(".thing").size()-1))).getAttribute("id").substring(6));
			}
			
				
			commentator.run();
		
			browser.close();
		
			}
			
		} catch (FailingHttpStatusCodeException | IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
