/* Name: Tirth Sharad Kelkar*/
/* UTA ID: 1001826874 */
package test;
import java.io.*;
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

public class ServerA extends Thread
{


public boolean KeepOn = true; // boolean flag to control while loop
private String WorkDir = "" ;

  @Override
 public void run()
 {
  String Sep = FileSystems.getDefault().getSeparator();
  WorkDir = System.getProperty("user.dir")+Sep+"filedir"+Sep+"DirA" ;
  System.out.println("Server A started in background thread.\nListening to port: "+ServerApp3.PortA);
	System.out.println("Ready to list files  from  : "+WorkDir);
	
  final Server server = new Server();
  Kryo kryo = server.getKryo();
	//kryo.register(String.class);
	
	/* Add a listner to the server 
	   Create a in-line Listner object that handles events of client connections
	   Ovveride Method received in our code to process the request.
		 received method will be called whenever client sends a request to server
	*/
	


	server.addListener(new Listener() {
       public void received (Connection connection, Object object) {
           
						 String req = (String)object ;
             // Ours is very simple case, irrespective of type of request we just have to send directory list  
						    
						 StringBuilder sbResp = new StringBuilder();
						 String ListA =  getList();
						 sbResp.append(ListA);
						 
						 
						 TestClient clnt = new TestClient();
						 String ListB = clnt.getServerResponse(ServerApp3.PortB);
						 sbResp.append(ListB);
             connection.sendTCP(sbResp.toString());
       } // end received
    });
	
try
{	
	
  server.bind(ServerApp3.PortA);
	server.start();
 
  while(KeepOn)
  {
	   // System.out.println("Text from Thread A");
     try
		 {
	     Thread.sleep(100); // Keep server running and constantly looking for client requests
		 }
		 catch(InterruptedException ex){}
	} // End while	
	server.stop();
	server.close();
	
}
catch(Exception e)
{
  System.out.println("Exception Thrown: "+e.getMessage());

}	
	System.out.println("Server A has stopped.");
 
 

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