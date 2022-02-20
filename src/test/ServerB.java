/* Name: Tirth Sharad Kelkar*/
/* UTA ID: 1001826874 */
package test;
import java.util.*;
import java.nio.file.FileSystems ;
import org.apache.commons.lang3.StringUtils;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server ;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener ;

/*
Authored by Tirth Kelkar 
*/
public class ServerB extends Thread
{
public boolean KeepOn = true; // boolean flag to control while loop
private String WorkDir = "" ;
  @Override
 public void run()
 {

  String Sep = FileSystems.getDefault().getSeparator();
  WorkDir = System.getProperty("user.dir")+Sep+"filedir"+Sep+"DirB" ;   
	System.out.println("Server B started in background thread.\nListening to port: "+ServerApp3.PortB);
	System.out.println("Ready to list files  from : "+WorkDir);
  
	
	 final Server server = new Server();
	server.addListener(new Listener() {
       public void received (Connection connection, Object object) {
           
						 String req = (String)object ;
             // Ours is very simple case, irrespective of type of request we just have to send directory list  
						    
						 String res =  getList();
             connection.sendTCP(res);
       } // end received
    });
	 
	 
	
try
{		 
	  server.bind(ServerApp3.PortB);
	  server.start();
    while(KeepOn)
    {
	   // System.out.println("Text from Thread A");
     try
		 {
	     Thread.sleep(2000);
		 }
		 catch(InterruptedException ex){}
	   }	// end while
	   server.stop();
		 server.close();
}
catch(Exception e)
{
  System.out.println("Exception Thrown: "+e.getMessage());
}		 
		 
  	 System.out.println("Server B has stopped.");

 } // end run

  // Private method to get directory listing from WorkDir
 private String getList()
 {
    	StringBuilder sb = new StringBuilder();
	    sb.append( StringUtils.repeat("-", 90));
	    sb.append(System.lineSeparator());
	    sb.append("Directory Listing: "+this.WorkDir);
	    sb.append(System.lineSeparator());
	    sb.append( StringUtils.repeat("-", 90));
	    sb.append(System.lineSeparator());
	    sb.append(DirectoryHelper.recordHeaderLine());
	    sb.append(System.lineSeparator());
	    sb.append( StringUtils.repeat("-", 90));
	    sb.append(System.lineSeparator());
	     ArrayList<DirectoryHelper.FileInfo> list =  DirectoryHelper.getDirectoryListing(this.WorkDir);
	     for (DirectoryHelper.FileInfo inf : list)
	     {
	        sb.append(inf.RecordLine);
	        sb.append(System.lineSeparator());		
	     }
	     sb.append( StringUtils.repeat("-", 90));
	     sb.append(System.lineSeparator());
       return sb.toString();
 } // end getList





} // End class definition