
//Runner
import java.net.URL;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;

//JSON
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import org.json.JSONArray;
import org.json.JSONException;

public class MuseBotRunner 
{
	private static final String token = "yourkeyhere";
	private static final String prefix = "!";
	private static IDiscordClient iDC;
	
	public static void main(String[] args)
	{
		iDC = makeIDClient(token, false);
		iDC.getDispatcher().registerListener(new MuseBotRunner());
		iDC.login();
	}
	
	public static IDiscordClient makeIDClient(String token, boolean login)
	{
		ClientBuilder cB = new ClientBuilder();
		cB.withToken(token);
		try
		{
			if(login) {return cB.login();}
			else {return cB.build();}
		}
		catch(DiscordException de)
		{
			de.printStackTrace();
			return null;
		}
	}
	
	@EventSubscriber
	public void courier(MessageReceivedEvent mre) throws JSONException, IOException
	{
		IChannel myChannel = mre.getChannel();
		String[] msgArr = mre.getMessage().getContent().split(" ");
		
		if (msgArr[0].startsWith(prefix))
		{
			String cmd = msgArr[0].substring(1);
			if (cmd.equalsIgnoreCase("info"))
			{
				MuseCommands.info(myChannel);
			}
			else if (cmd.equalsIgnoreCase("commands"))
			{
				MuseCommands.commands(myChannel);
			}
			else if (cmd.equalsIgnoreCase("talktome"))
			{
				myChannel.sendMessage("What do you want to talk about?");
				MuseCommands.talkwithme(myChannel);
			}
			else if (cmd.equalsIgnoreCase("rhyme"))
			{
				JSONArray jsonArray = JsonRead.readJsonFromUrl(("https://api.datamuse.com/words?rel_rhy="+msgArr[1]+"&max=5"));
				myChannel.sendMessage(jsonArray.toString(3));
			}
			else if (cmd.equalsIgnoreCase("ping"))
			{
				myChannel.sendMessage("pong!");
			}
		}
	}
}

class MuseCommands
{	
	public static void info(IChannel c)
	{
		c.sendMessage("Hi, I'm MuseBot! For a list of commands, try !commands");
	}
	
	public static void commands(IChannel c)
	{
		c.sendMessage("!info - Standard greeting.\n\n!commands - List of commands.\n\n!ping - pong!\n\n!rhyme - return JSON list of rhyming words\n\n");
	}
	
	public static void talkwithme(IChannel c)
	{
		//Thread.sleep(MessageReceivedEvent);
		//String response = mre.getMessage().getContent();
		//c.sendMessage(nextResponse(longestWord(response)));
	}
	
	public static String longestWord(String sentence)
	{
		String[] strArr = sentence.split(" ");
		String longest = "";
		
		for (int i = 0; i<strArr.length;i++)
		{
			if (strArr[i].compareTo(longest) > 0)
			{
				longest = strArr[i];
			}
		}
		return longest;
	}
	
	//simple eliza
	public static String nextResponse(String word)
	{
		switch(word.length())
		{
			case 3:
			{return "Maybe we should move on to a different topic.";}
			case 4:
			{return "Tell me more about "+word;}
			case 5:
			{return "Why do you think "+word+"is important?";}
			case 6:
			{return "Now we're getting somewhere! How does "+word+"affect you the most?";}
			default:
			{return "Well I'll be damned.";}
		}
	
		
	}

}




class JsonRead 
{
	private static String readTheJson(Reader readd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int i;
    while ((i = readd.read()) != -1) 
    {
    	sb.append((char) i);
    	System.out.println((char) i);
    	
    }
    return sb.toString();
  }

  public static JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
    InputStream is = new URL(url).openStream();
    try 
    {
    	BufferedReader buffRead = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      	String jsonText = readTheJson(buffRead);
      	JSONArray json = new JSONArray(jsonText);
      	return json;
    }
    finally 
    {
    	is.close();
    }
  	}
}
