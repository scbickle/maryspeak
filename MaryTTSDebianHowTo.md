### MaryTTS voice synthesizer How to for Debian ###

Mary Text To Speech logoMaryTTS is an open-source, multilingual Text-to-Speech 
Synthesis platform written in Java (homepage <http://mary.dfki.de/>). I’ve taken 
an interest in it after it was featured on [Hacker Public Radio Episode 1599](http://hackerpublicradio.org/eps.php?id=1599). 
As a Java program it should run anywhere, however here is how to get it to 
work on a Debian Linux machine.

Download the MaryTTS runtime package from the link on the download page:

<http://mary.dfki.de/download/index.html>

	$ cd Downloads
	$ wget https://github.com/marytts/marytts/releases/download/v5.1/marytts-5.1.zip

Unzip the application to the /usr/bin directory

	$ sudo unzip marytts-5.1.zip -d /opt

At this point it will not run unless the you have already installed Java 1.7
you can determine the current version of Java by executing:

	$ java -version

Install the required version of Java (also add openjdk-7-jdk if you intend to 
do any java development):

	$ sudo apt-get install openjdk-7-jre

After installing the new java runtime (jre) it will still not be the default. 
To set the new jre to your default use:

	$ sudo update-alternatives --config java
	
	Selection Path Priority Status
	------------------------------------------------------------
	* 0 /usr/lib/jvm/java-6-openjdk-amd64/jre/bin/java 1061 auto mode
	1 /usr/lib/jvm/java-6-openjdk-amd64/jre/bin/java 1061 manual mode
	2 /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java 1051 manual mode
	
	Press enter to keep the current choice[*], or type selection number: 2
	Having selected option 2 the java version should return something similar to:

	$ java -version
	java version "1.7.0_65"
	OpenJDK Runtime Environment (IcedTea 2.5.1) (7u65-2.5.1-5~deb7u1)
	OpenJDK 64-Bit Server VM (build 24.65-b04, mixed mode)

The runtime package delivers the scripts necessary to run the MaryTTS Server, 
which can be used via a browser of the client to synthesize speech. The server 
can be launched with:

	$ /opt/marytts-5.1/bin/marytts-server.sh

This can then be used either through a browser or via the MaryTTS Client. The 
browser address will be:

	http://localhost:59125

The MaryTTS Client, which is a Java GUI can be launched with:

	$ /opt/marytts-5.1/bin/marytts-client.sh

In addition to the server and client components there is the MaryTTS Component 
Installer, which can be used to install additional voices and apply any 
available updates to the voices (the server comes with a single us female voice
as a default). To launch the installer:

	$ /opt/marytts-5.1/bin/marytts-component-installer.sh

Once the installer is running click [Update] to fetch the latest selection of 
voices. Buttons are then available to install or remove voices.
