/* Name: Tirth Sharad Kelkar*/
/* UTA ID: 1001826874 */
package test ;

import java.nio.*;
import java.util.*;
import  org.fusesource.jansi.AnsiConsole ; /* https://github.com/fusesource/jansi */
import org.fusesource.jansi.Ansi;
import com.esotericsoftware.kryonet.Client ;  /*  https://github.com/EsotericSoftware/kryonet */

/*
Authored by Tirth Kelkar 
*/
public class ServerApp3
{
private static boolean  bKeepDoing = true;

public static final int PortA = 10001 ;
public static final int PortB = 10002 ;

public static boolean ARunning = false; // Flag to indicate that Server A has been started from menu selection 1
public static boolean BRunning = false; // Flag to indicate that Server B has been started from menu selection 2



public static void main(String[] args)
{
   AnsiConsole.systemInstall(); // Enable ANSI Console for console menus
	 FileStore.initDatabase();
	 System.out.println("Client Server Communication Through Scockets...\n\n");
	  FileSync SynThrd = new FileSync();
		SynThrd.start();
	 
	 printMenus();
	 
	 ServerA srvA = new ServerA();
	 ServerB srvB = new ServerB();
	 
	 
	 
	 
	 
   while(bKeepDoing)
	 {
	    Scanner input = new Scanner(System.in); 
     int option = input.nextInt();
	    switch(option)
     {
	 
        case 1 :
		      clearConsole();
					if(ServerApp3.BRunning == true ) System.out.println("Server A is already running.");
					else  srvA.start();
					ServerApp3.ARunning = true;
					printMenus();
					
		    break ;
		
        case 2 :
		      clearConsole();
					if(ServerApp3.BRunning == true ) System.out.println("Server B is already running.");
					else srvB.start();
					ServerApp3.BRunning = true;
					printMenus();
		    break ;

        case 3 :
		      clearConsole();
					if( ServerApp3.ARunning == true &&  ServerApp3.BRunning == true )
          {
					   fetchServerData(); 
          }	
          else
          {
					  System.out.println("Please start Server A and than B from Menu 1 and 2 ");
          }										
					printMenus();
		    break ;
				
				

		    case 4:
				  clearConsole();
					
					srvA.KeepOn = false;
					srvB.KeepOn = false;
					
				  System.out.println("Bye!");

		      bKeepDoing = false;
		     break;
		
      } // end switch			
	 
	 } // end while loop
	 
	SynThrd.bKeepDoing = false;
	 
	System.out.println("Program exited ...");
		
	 
  
}	// end main()
	
/* Fetch file informatin from Server A  */
public static void fetchServerData()
{
   System.out.println("Please wait, fetching file related data from server-A ...\n");
	 TestClient Cln = new TestClient();
	 String Resp = Cln.getServerResponse(ServerApp3.PortA);
	 System.out.println(Resp);
}



public static void clearConsole()
{
    Ansi ans = Ansi.ansi();
    System.out.println(ans.ansi().eraseScreen());
		System.out.println(ans.cursor(0, 0));
		
		// System.out.print("\033[H\033[2J");
     
}


public static void printMenus()
{

   
		System.out.println("Press 1 to Start Server A");
		System.out.println("Press 2 to Start Server B");
		System.out.println("Press 3 to Fetch Data From A  and B ( through A )");
		System.out.println("Press 4 to Quiz");
}

	
	
} // end class definition