/* Name: Tirth Sharad Kelkar*/

package test ;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
/* This class handles the file locking, unlocking and Updating the file according to order they appear*/
/*
Authored by Tirth Kelkar 
*/
public class LockFile
{
public static void main(String[] args)
{
   
	 
	
	if(args.length==2) 
	{
      	int index = -1;
      	try
      	{
      	  index = Integer.parseInt(args[1]);
      	}
      	catch(Exception ex)
      	{
      	 System.out.println("Invalid Index: "+args[1]+"\n");
      	 System.exit(0);
      	}
      	
      	String cmd = args[0].toLowerCase();
      	String FileName = "";
      	switch(cmd)
      	{
      	  case "lock":
      		
      		FileName = FileStore.updateLock(index, true);
      		System.out.println("Locking file: "+FileName+" at  index: "+index+"\n");
      		break;
      
      	  case "unlock":
      		
      		FileName = FileStore.updateLock(index, false);
      		System.out.println("Un-Locking  file: "+FileName+" at index: "+index+"\n");
      		break;
      
      	  case "revert":
      		FileName = FileStore.popFile(index);
      		System.out.println("Updating file: "+FileName+" from FIFO queue at index: "+index+"\n");
      		
      		break;
      		
      		
      		
      		default:
      		System.out.println("Invalid command:"+cmd+"\n");
					System.out.println("Usage: Lock  [ lock /unlock /revert ] [index]\n");
      		System.exit(0);
      	
      	
      	} // end switch(cmd)
      	
	} // if(args.length==2) 
	else
	{
	 System.out.println("Invalid Number Of Arguments.");
	 System.out.println("Usage: Lock  [ lock /unlock /revert ] [index]\n");
	
	}
	
	FileStore.showLocks();
	
	 
} // end main




} // end class definiion
