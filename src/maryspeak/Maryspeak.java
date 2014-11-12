package maryspeak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.UnknownHostException;

import maryspeak.util.Log;
import marytts.client.MaryGUIClient;
import marytts.exceptions.MaryConfigurationException;

/**
 * This public class is designed to act as a command line interface to the 
 * Mary-TTS speech synthesis project. Instructions for use are detailed in
 * maryspeak.txt which is a man-page usage description delivered in this package.
 * <p>
 * This class mainly handles parsing the input commands and setting up the Process 
 * class which handles the actual interface to MaryTTS, then executes the synthesis 
 * process.
 * <p>
 * Pass in the argument -h or --help to show a man page style usage description.
 *
 * @author Steve Bickle
 *
 */
public class Maryspeak {
	
	public static final String VERSION = "0.1.007";
	public static final String HELPTEXT = "maryspeak.txt";
    static boolean executeProcess = true; 
	// get our process object ready to set up the Mary-TTS
	static private Process process = new Process();
	
	public static void main(String[] args)  {
		
		// loop through the arguments setting the parameters for Mary-TTS
		// collect any remaining input into text to be processed by Mary-TTS
		StringBuffer text = new StringBuffer("");
		for (String arg: args)
		{
			if (arg.startsWith("--"))
			{
				try {
					doubleDash(arg);
				} catch (MaryConfigurationException e) {
					Log.print(e); 
					return;
				}
			}
			else if (arg.startsWith("-"))
			{
				 try {
					singleDash(arg);
				} catch (MaryConfigurationException e) {
					Log.print(e);
					return;
				}
			}
			else 
			{
				// gather up text to be spoken
				text.append(arg + " ");
			}

  		}
		
		if (args.length == 0){
			Log.print(Log.DEBUG,"giving useage info");
			executeProcess = false;
			System.out.println("simplest usage: maryspeak <string of text to say>");
			System.out.println("use -h or --help for full instructions");
		};
		
		process.setText(text.toString());
		
		// OK lets get to it an say something if it was required
		if (executeProcess) process.execute();
	}
	
	/**
	 * Process the - switches from the argument
	 * @param arg
	 * @throws MaryConfigurationException 
	 */
	private static void singleDash(String arg) throws MaryConfigurationException 
	{
		// strip off the -
		String switches = arg.substring(1);
		// parse through all the switches in this string
		Log.print(Log.DEBUG, "switches:" + switches);
		while (switches.length() >0)
		{
			switch (switches.substring(0, 1))
			{
				case "d":
					Log.setLevel(Log.DEBUG);
					Log.print(Log.WARNING, "Logging was set to DEBUG");
					break;
				case "f":
					if (switches.length() <2)
						throw new MaryConfigurationException("-f must be followed by a filename or filepath");
					process.setInputFile(switches.substring(1));
					switches = " "; //we've now used the whole switches string
					break;
				case "g":
					executeProcess = false;  
					launchGui();
				case "h":
					executeProcess = false; 
					showHelp();
					break;
				case "i":
					process.setUseStdin(true);
					break;
				case "o":
					process.setUseStdout(true);
					break;
				case "v":
					if (switches.length() <2)
						throw new MaryConfigurationException("-v must be followed by a MaryTTS voice name");
					process.setVoiceName(switches.substring(1));
					switches = " ";
					break;
				case "w":
					if (switches.length() <2)
						throw new MaryConfigurationException("-w must be followed by a filename or filepath");
					process.setOutputFile(switches.substring(1));
					switches = " ";
					break;
				default:
					String msg = "'" + switches.substring(0, 1) + "' is not a valid switch for the maryspeak command";
					throw new MaryConfigurationException(msg);
			}
			switches = switches.substring(1);
		}
	}

	/**
	 * Process the -- switch from the argument passed in
	 * @param arg argument from the command line to be processed
	 * @throws MaryConfigurationException 
	 */
	private static void doubleDash(String arg) throws MaryConfigurationException 
	{
		switch (arg)
		{
			case "--debug":
				Log.setLevel(Log.DEBUG);
				Log.print(Log.WARNING, "Logging was set to DEBUG");
				break;
			case "--default":
				process.setUseDefaultText(true);
				break;
			case "--help":
				executeProcess = false; 
				showHelp();
				break;
			case "--host":
				process.setHost("localhost");
				break;
			case "--stdin":
				process.setUseStdin(true);
				break;
			case "--stdout":
				process.setUseStdout(true);
				break;
			case "--version":
				executeProcess = false;
				showVersion();
				break;
			case "--voices":
				process.setShowVoices(true);
				break;
			default:
				if (arg.startsWith("--host=") ) 
				{
					process.setHost(arg.substring(7));
				}
				else // it must be an unknown -- parameter
				{
					String msg = "'" + arg + "' is not a valid parameter for the maryspeak command";
					throw new MaryConfigurationException(msg);
				}
		}
	}

private static void showHelp()
	{
		try
		{
			InputStream is = Maryspeak.class.getClassLoader().getResourceAsStream(HELPTEXT);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String strLine;
	
			while ((strLine = br.readLine()) != null)
			{
			  System.out.println (strLine);
			}

			br.close();
		}
		catch(IOException ioe) { ioe.printStackTrace(); }
	}

	/**
	 * Prints to standard out the version of this Maryspeak wrapper and
	 * The implementation and specification versions of the local 
	 * MaryTTS library
	 */
	private static void showVersion()
	{
		System.out.println("Maryspeak Wrapper Version : " + VERSION);
		System.out.println("MaryTTS implementation : " 
							+ marytts.Version.implementationVersion());
		System.out.println("MaryTTS specification : "
							+ marytts.Version.specificationVersion());
	}
	
	private static void launchGui()
	{
		String args[] = { "localhost" , "59125" };
		try
		{
			MaryGUIClient.main(args);
		}
		catch (UnknownHostException uhe) { Log.print(uhe); }
		catch (IOException ioe) { Log.print(ioe); }
		catch (Exception e) { Log.print(e); }
	}

}