package maryspeak;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Locale;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import maryspeak.util.Log;
import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.client.RemoteMaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.util.data.audio.AudioPlayer;
import marytts.util.data.audio.MaryAudioUtils;

/**
 * This private class is used to hold the configuration for execution
 * of Mary-TTS then execute that synthesis.
 * <p>  
 * @author Steve Bickle
 *
 */
public class Process {
	
	private final String DEFAULT_TEXT=
			"This voice synthesis example is produced using Mary Tee Tee Ess, via the maryspeak command wrapper.";
	private MaryInterface marytts;
	private String serverHost = "";
	private Integer serverPort = 59125;
	private boolean useStdin = false;         
	private boolean useStdout = false;
	private boolean showVoices = false;
	private boolean useDefaultText = false;
	private String inputFile = "";         
	private String outputFile = "";        
	private String voiceName = "";       
	private String text = null;
	
	/**
	 * Instantiate the Process class
	 */
	Process() {	}

	/**
	 * Creates the connection to a Mary-TTS server or starts a local instantiation
	 * if no host is set
	 */
	void openMaryTTS() 
	{
		if (serverHost.length()>0) // use a server or instantiate a local interface
		{
			try    // server
			{ 
				marytts= new RemoteMaryInterface(serverHost, serverPort);
			}
			catch(IOException ioe) { Log.print(Log.ERROR, ioe.getMessage()); }
		}else{
			try    // local
			{
				marytts = new LocalMaryInterface();
			}
			catch(MaryConfigurationException mce) { Log.print(mce); }
		}
	}
	
	/**
	 * Prints the currently available Synthesis voices to stdout
	 * @param marytts
	 */
	void printVoices()
	{
		System.out.println("Locale : Voice");
		Iterable<Locale> locales=marytts.getAvailableLocales();
		for (Locale locale : locales)
		{
			Iterable<String> voices=marytts.getAvailableVoices(locale);
			for (String voice: voices)
			{
				System.out.println(locale.toString() + " : " + voice);
			}
		}
	}

	/**
	 * Executes the processing of the speech synthesis using the provided information
	 */
	void execute() {
		// show variables for debug
		Log.print(Log.DEBUG, "text:" + text);
		Log.print(Log.DEBUG, "inputFile:" + inputFile);
		Log.print(Log.DEBUG, "useStdin:" + useStdin );
		Log.print(Log.DEBUG, "useDefaultText:" + useDefaultText);
		// check that there's something valid to do
		if (showVoices || useStdin || text.length() > 0 || inputFile.length() > 0 || useDefaultText)
		{
			// get the MaryTTS Interface to work against
			openMaryTTS();
			
			// set a voice if defined
			if (!voiceName.isEmpty()) marytts.setVoice(voiceName);			
			
			// set the default text
			if (useDefaultText) text=DEFAULT_TEXT;

			// define a buffered reader to accept the text
			BufferedReader brText = null;

			// define an audio stream to accept the synthesis
			AudioInputStream audio = null;			
			
			if (showVoices)
			{
				// print out the available voices
				printVoices();
			}
					
			else      // not being asked to show voices so lets get processing
			{
				// We have incoming text to process, we want it as a BufferedReader brText
				Log.print(Log.DEBUG, "text:" + text);
				if ( inputFile.length() >0 ) // use file input 
				{
					try {
						brText = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
						if (!useStdout) System.out.println("Processing file: " + inputFile);
					} catch (FileNotFoundException e) { Log.print(e);}
				}
				else if ( text.length() > 0 ) // use a text string
				{
					brText = new BufferedReader(new StringReader(text));
				}
				else if (useStdin)            // use system input
				{
					brText = new BufferedReader(new InputStreamReader(System.in));
				}

				//TODO set up output
				
				// capture the input text
				String strLine;
				StringBuffer sb = new StringBuffer();
				try 
				{
					while (((strLine = brText.readLine()) != null)) // && ( strLine.length() != 0 || !useStdin ))   
					{
						if (!strLine.isEmpty())  
						{
							//MaryTTS does not appear to like lines with no content
							Log.print(Log.DEBUG, strLine.length() + " :" + strLine);
							sb.append(strLine + "\n");
						}
					}
				} 
				catch (IOException e) { Log.print(e);}
				
				// process the audio
				try 
				{
					audio = marytts.generateAudio(sb.toString());
				} 
				catch (SynthesisException e) { Log.print(e); }
				
				// output the audio
				if (useStdout)
				{
					//TODO write a standard out mechanism
					System.out.println("--stdout Not Implemented");
				}
				
				else if (!outputFile.isEmpty())
				{
					try 
					{
						MaryAudioUtils.writeWavFile(MaryAudioUtils.getSamplesAsDoubleArray(audio), outputFile, audio.getFormat());
					} 
					catch (IOException e) { Log.print(e);}
				}
				
				else        //output to default audio device
				{
					playAudio(audio);
				}
			}
		}
	}
	
	private void playAudio(AudioInputStream audio)
	{
		LineListener lineListener = new LineListener() 
		{
            public void update(LineEvent event) 
            {
                if (event.getType() == LineEvent.Type.START) { Log.print(Log.DEBUG,"Audio started playing."); }
                else if (event.getType() == LineEvent.Type.STOP) { Log.print(Log.DEBUG,"Audio stopped playing."); } 
                else if (event.getType() == LineEvent.Type.OPEN) { Log.print(Log.DEBUG,"Audio line opened."); } 
                else if (event.getType() == LineEvent.Type.CLOSE) { Log.print(Log.DEBUG,"Audio line closed."); }
            }
        };

        AudioPlayer ap = new AudioPlayer(audio, lineListener);
        ap.start();
}

	/**
	 * Sets the host name of the MaryTTS server to be used.
	 * @param host
	 */
	public void setHost(String host) {
		this.serverHost = host;
	}

	/**
	 * Changes the port that's used to connect to the MaryTTS server where a
	 * port other than the default 59125 is to be used.
	 * @param port
	 */
	public void setPort(Integer port) {
		this.serverPort = port;
	}

	/**
	 * Sets whether to read text from stdin.
	 * @param useStdin
	 */
	public void setUseStdin(boolean useStdin) {
		this.useStdin = useStdin;
	}

	/**
	 * Sets whether to use stdout for speech output.
	 * @param useStdout
	 */
	public void setUseStdout(boolean useStdout) {
		this.useStdout = useStdout;
	}

	/**
	 * Sets whether to print the voices available
	 * @param showVoices
	 */
	public void setShowVoices(boolean showVoices) {
		this.showVoices = showVoices;
	}

	/**
	 * Sets the file-path for an input file containing text to be processed.
	 * @param inputFile
	 */
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
		this.useStdin = false;     //can't use stdin if were getting file input
	}

	/**
	 * Sets the file-path for an output file to contain processed speech.
	 * @param outputFile
	 */
	public void setOutputFile(String outputFile) {
			this.outputFile = outputFile;
	}

	/**
	 * Sets the name of a voice that's to be used.
	 * @param voiceName
	 */
	public void setVoiceName(String voiceName) {
		this.voiceName = voiceName;
	}

	/**
	 * Sets the text to be processed as speech.
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * If set to true
	 * @param useDefaultText
	 */
	public void setUseDefaultText(boolean useDefaultText) {
		this.useDefaultText = useDefaultText;
	}
}
