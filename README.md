# Client-Server-Distributed-Systems-Transparency-Concepts
A Java Command Line application which implements Transparency concepts of distributed system. Also, implementation of file read/write lock and storing metadata into built in H2 database.
# External Libraries used:
# Apache Commons Language 3
Web Site: https://commons.apache.org/proper/commons-lang/
Apache Commons Lang provides a host of helper utilities for the java.lang API, notably String manipulation methods, basic numerical methods, object reflection, concurrency, creation and serialization and System properties.
# Apache Commons IO
Web site: https://commons.apache.org/proper/commons-io/
Library for file related IO operations. Easy to user java classes for file stream operations and file copy and directory related operations.\
# JANSI
Web Site: https://github.com/fusesource/jansi and http://fusesource.github.io/jansi/
Jansi is a small java library that allows developer to use ANSI escape sequences to format the console output which works even on windows.
# Kryonet
Web Site: https://github.com/EsotericSoftware/kryonet
KryoNet is a Java library that provides a clean and simple API for efficient TCP and UDP client/server network communication using NIO. KryoNet is ideal for any client/server application. It is very efficient. KryoNet can also be useful for inter-process communication.
# To run the app,
1) Firstly, Set ANT PATH. If not installed ANT; download ANT from: https://ant.apache.org/bindownload.cgi
2) SET ANT HOME in System environment variable and copy that path and open build.bat file with any text editor and simply paste it in SET variable.
3) I have used several libraries which I have stored in lib folder. Build.xml file has path to ../lib folder which will be used to compile all the java source files.
4) I have used several libraries which I have stored in lib folder. Build.xml file has path to ../lib folder which will be used to compile all the java source files.
5) Run build.bat file to compile all source java files. Build.bat file will au-tomatically compile all the .java files and compiled .class files will be stored in ../out/test/ folder.
6) After all the above steps, double click on run-server.bat file and fol-low-on screen instructions for running the server a. In my application, we do NOT need to run client separately because I have designed my server in such a way that using single command line window and that window will itself work as client side.
7) After successfully running above instructions, open a new command line window in the path and test the locking/unlocking and update according to order they appear.
8) To lock a file type “file-lock lock [index of the file].
9) To unlock that file type “file-lock unlock [index of the file].
10) To revert (Applying updates), type “file-lock revert [index of the file].
