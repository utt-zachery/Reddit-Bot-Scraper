import java.util.ArrayList;

import settings.SettingsHolder;

public class DoComment implements Runnable {

	private ArrayList<Commentor> comments;
	private boolean isRunning = false;
	private SettingsHolder settings;
	
	public DoComment(SettingsHolder settings)
	{
		this.comments = new ArrayList<Commentor>();
		this.settings=settings;
	}
	
	public void addComment(Commentor toAdd)
	{
		this.comments.add(toAdd);
	}

	
	@Override
	public void run() {
		if (this.isRunning == false)
		{
			this.isRunning = true;
			System.out.println("**** Comments to Post: " + comments.size() + " ******");
			
			for (int iterator = 0; iterator < comments.size(); iterator++)
			{
				comments.get(iterator).run();
			
				if (iterator != comments.size()-1 && comments.size() != 0 )
				{
				try {
					Thread.sleep(((int)(Math.pow(10, 3))*(int)settings.getSetting("waitTime")));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				}
			}
			}
		
	
		System.out.println("Done");
	}
}
