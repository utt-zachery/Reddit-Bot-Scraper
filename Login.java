import java.io.IOException;
import java.io.Serializable;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Login extends WebClient implements Serializable{

	private static final long serialVersionUID = 1L;
	public Login(String username, String password) {
		super(BrowserVersion.FIREFOX_45);
		super.getOptions().setCssEnabled(false);
		super.getOptions().setThrowExceptionOnFailingStatusCode(false);
		super.getOptions().setThrowExceptionOnScriptError(false);
		try {
			HtmlPage login1 = super.getPage("https://www.reddit.com/login");
			super.waitForBackgroundJavaScript(1000);
			if (login1.getUrl().toString().contains("login"))
			{
			while (login1.querySelectorAll(".c-form-control").getLength() == 0)
			{
				Thread.sleep(100);
			}
			login1.getElementById("user_login").setAttribute("value", username);
			login1.getElementById("passwd_login").setAttribute("value", password);
			while (login1.getUrl().toString().contains("login"))
			{
				login1 = ((HtmlButton)login1.querySelectorAll(".c-btn").get(1)).click();
				super.waitForBackgroundJavaScript(1000);
			}
			}
			
		} catch (FailingHttpStatusCodeException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
