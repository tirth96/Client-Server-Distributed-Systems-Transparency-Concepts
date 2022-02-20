/* Name: Tirth Sharad Kelkar*/
/* UTA ID: 1001826874 */
package test;
import java.io.*;
import java.util.*;
import java.nio.file.FileSystems ;
import org.apache.commons.lang3.StringUtils;
import com.esotericsoftware.kryo.Kryo;
//import com.esotericsoftware.kryonet.Server ;
import com.esotericsoftware.kryonet.Client ;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener ;

/*
Authored by Tirth Kelkar 
*/
public class TestClient
{
	
	 boolean bKeepOn = true;
	 String Response = "" ;
	
   public String getServerResponse(int port)
   { 
       bKeepOn = true;
			 Response = "";
	 
	     Client client = new Client();
       client.addListener(new Listener() {
           public void received (Connection connection, Object object) {
          
              Response = (String)object;
              bKeepOn = false;
           }
      });	 
	  try
   {		
		client.start();
	  client.connect(3000, "localhost",port) ;
	  client.sendTCP("LIST");
	 
	     while(bKeepOn) 
	     {
	        Thread.sleep(100);
	     } // end while
	     client.stop();
	     client.close();
	  
		}
		catch(Exception e)
		{
       System.out.println("Exception From Client: "+e.getMessage());		
    }
		
		
		
		return Response;
  }  	
	
	
	
	
	
}	// end class definition
	