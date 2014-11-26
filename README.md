maryspeak
=========

A small java program  to make the core features of Mary Text To Speech readily accessible as a shell command

For instructions to install MaryTTS on a Debian system see [MaryTTSDebianHowTo.md](MaryTTSDebianHowTo.md)

For instructions on installing maryspeak see [InstallingMaryspeak.md](InstallingMaryspeak.md)

For further information on exploring MaryTTS see [FurtherExplorationOfMaryTTS.md](FurtherExplorationOfMaryTTS.md)

#### What is Maryspeak ####

I've been inspired by Ken Fallon via Hacker Public Radio to write maryspeak; a small java program
to make the core features of Mary Text To Speech  readily accessible as a shell command. The aim of
maryspeak is to reduce the friction for Linux shell users to use MaryTTS. It accepts text input via
the command line, via a file or via stdin, processes it using MaryTTS, then outputs speech via 
sound, to a file, or to stdout. It also allows for the selection of a voice and/or a MaryTTS server.

MaryTTS is written in Java and the UI is not transparently accessible to the Linux command line. 
maryspeak is a wrapper around the MaryInterface classes used by the MaryTTS Java and http clients. 
Because its written in Java there is no reason this cannot also be on a Windows system, but the 
command switches are closer to the GNU conventions as they seek to approximate those used by espeak.