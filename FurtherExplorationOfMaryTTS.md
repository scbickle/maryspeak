### Further Exploration/Exploitation of MaryTTS ###

Maryspeak only offers a subset of the functionality of MaryTTS. Maryspeak is also not 
using the streaming capabilities of the MaryTTS library, so processes things in 
series: gather input, process the input, then output audio.

MaryTTS has a rich depth of functionality beyond that used by Maryspeak. For serious 
use I would recommend investigating this.

MaryTTS can be used directly from command line if required via:

	$ java -cp "classpath" [properties] marytts.client.http.MaryHttpClient [inputfile]

Assuming MaryTTS is installed as per [MaryTTSDebianHowTo.md](MaryTTSDebianHowTo.md), 
the instructions for usage of this class can be obtained by compiling and running 
the following java class:

	public class ShowUsage {
	    public static void main(String[] args) {
	    marytts.client.http.MaryHttpClient.usage();
	    }
	}

For your convenience I have done this, for MaryTTS version 5.1, this is the output:

	usage:
	 java [properties] marytts.client.http.MaryHttpClient [inputfile]
	Properties are: -Dinput.type=INPUTTYPE
	                -Doutput.type=OUTPUTTYPE
	                -Dlocale=LOCALE
	                -Daudio.type=AUDIOTYPE
	                -Dvoice.default=male|female|de1|de2|de3|...
	                -Dserver.host=HOSTNAME
	                -Dserver.port=PORTNUMBER
	 where INPUTTYPE is one of TEXT, RAWMARYXML, TOKENS, WORDS, POS,
	 PHONEMES, INTONATION, ALLOPHONES, ACOUSTPARAMS or MBROLA,
	 OUTPUTTYPE is one of TOKENS, WORDS, POS, PHONEMES,
	 INTONATION, ALLOPHONES, ACOUSTPARAMS, MBROLA, or AUDIO,
	 LOCALE is the language and/or the country (e.g., de, en_US);
	 and AUDIOTYPE is one of AIFF, AU, WAVE, MP3, and Vorbis.
	 The default values for input.type and output.type are TEXT and AUDIO,
	 respectively; default locale is en_US; the default audio.type is WAVE.
	inputfile must be of type input.type.
	 If no inputfile is given, the program will read from standard input.
	The output is written to standard output, so redirect or pipe as appropriate.

So for a quick demo try putting some text into a file test.txt (use a full stop at 
the end of your text or Mary speaks slowly for some reason) then run:

	$ java -cp "/opt/marytts-5.1/lib/*" marytts.client.http.MaryHttpClient test.txt | aplay

Note: that the output is piped into aplay which can play back a .wav stream. If you 
don't have aplay installed you can > output to a file.wav to play later.

Much more can be done using the full MaryTTS libraries within a Java program.