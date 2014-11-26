### Installing Maryspeak ###

This installation assumes Debian but the same principles will apply to other distros.

Install MaryTTS - see [MaryTTSDebianHowTo.md](MaryTTSDebianHowTo.md)

Download the maryspeak.jar from here or download the source to build it for yourself

Copy the maryspeak.jar file to the MaryTTS library

	$ sudo cp maryspeak.jar /opt/marytts-5.1/lib

Create an alias to provide a nice clean usable command. In Debian add the following line
to the '~/.bash_aliases' file, create the file if it does not already exist.
 
	alias maryspeak='java -cp "/opt/marytts-5.1/lib/*" -Dmary.base=/opt/marytts-5.1 maryspeak.Maryspeak'
	
Log out of your session and log back in to pick up the new alias, alternately you can 
source the .bashrc to refresh the session

	$ source ~/.bashrc
	
### Using Maryspeak ###

You can use maryspeak standalone or with a MaryTTS server.

For the simplest demonstration of maryspeak working, it can speak an internal default phrase
by using the –default parameter

	$ maryspeak --default

Say what you want by just appending your text to the command. The full stop at the end is 
required, else for some reason the speech is too slow.

	$ maryspeak This is a short statement from your computer.

If you wish to process the speech on a MaryTTS server use the –host=servername parameter. 
If your server is on the local machine you can just use –host. It is even possible use 
the MaryTTS demo server with –host=mary.dfki.de

	$ maryspeak --host You will need to run the MaryTTS Server locally to hear this spoken.

Show the full Maryspeak usage instructions with -h or –help

	$ maryspeak --help