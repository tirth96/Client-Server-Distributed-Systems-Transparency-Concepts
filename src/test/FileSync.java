/* Name: Tirth Sharad Kelkar*/
package test;
import java.io.*;
import java.util.*;
import java.nio.file.FileSystems ; // java's built in class for File system operations.
import org.apache.commons.io.FileUtils ; // Web site: https://commons.apache.org/proper/commons-io/

/*
Authored by Tirth Kelkar 
*/

/* 
Apache Commons IO Library: Easy to use general file manipulation utilities.
FileUtils helper class provides  methods to copy contents of file between directories preserving timestamp.
It also provides other userful directory related functions like listing files in directory etc.
*/

/* Purpose of this Class
  Synchronize contents for two different directories DirA and DirB relative to ./filedir
  Task:
	- Get directory listing from DirA
	- Get driectory listing from DirB
	- Compare A and B
	- Syncronize contents of A and B 
	- If there is file with the same name in both dierctories than replace older file with newer file. 
	
	This class will continuesly run as backgroun thread started from main class ServerApp2.
	At every 300 miliseconds it will keep on  comparing DirA and DirB until the main application terminates.
	
	
*/

public class FileSync  extends Thread
{
 public boolean bKeepDoing  = true; // A loop control variable that keeps on running the while loop until its value is set to false.
 private String PathA = "" ; // Canonical path name for Directory A.
 private String PathB = "" ; // Canonical path  name for Directory B.
 
 
 private String Sep = "" ; // Check at run time about file path seperator character '/' for Linux and  '\' for Windows.
 private HashSet<String> SyncEntries = null ; // Set of Strings that holds the name of files present in  synchronized directories A and B.
 
  
 
 
 public FileSync()
 {
   // Get the full path names for directories, from application startup directory.
	 // Data directories are in relative path ./filedir/DirA and ./filedir/DirB
	 
   this.Sep = FileSystems.getDefault().getSeparator();
   this.PathA  = System.getProperty("user.dir")+Sep+"filedir"+Sep+"DirA" ;  // Directory A  is relative path from execution directory ./filedir/DirA
	 this.PathB  = System.getProperty("user.dir")+Sep+"filedir"+Sep+"DirB" ;  // Directory B  is relative path from execution directory ./filedir/DirB
 
 
   this.SyncEntries = new  HashSet<String>(); // Initialized set object
	 
 }
 
 
  @Override
   public void run()
   {
     
    	   System.out.println(" Background thread to synchronize directory A and B started.\n");
         // First call to syncFolders() to synchronize contents of directory A and B.
      	 // At the beginning of thread we do not check for deletions, it will be checked later in while loop
       	 syncDirectories(); 
    		
         while(bKeepDoing)
         { 
    			  // Keep the CPU load moderate by sleeping the thread( pausing ) for 300 millisecond in while loop.
    			  // Otherwise most of the CPU clock cycles with just go wasted.
    				// End user can not do anything faster than 300 miliseconds .
              try
        		  {	
        			  Thread.sleep(300);
              }
        			catch(Exception ex)
        			{
        			  System.out.println("Error: "+ex.getMessage());
								ExceptionHelper.printException(ex);
        			}
    
    			    // Regularly syhchronize contents between directory A and B checking for add, update, delete in either folder.
    			    checkDeletedFiles() ; //  Just look for file deletions by end user in either directory A or directory B
    				  syncDirectories();  // Synchronize contents of directory A and B
    	  } // end - while(bKeepDoing)
				
         System.out.println(" Synchronizing thread ended  on program exit.\n");
 
   } // end -  run()
 
 /*---------------------------------------------------------------------------  
    Method: syncDirectories()
	  This method will be called firstly on thread startup and then regularaly every 300 miliseconds in 
		continues while loop until program gets terminated.
		Look for the changes in  the contents for directory A and directory B
	  LOGIC for synchronizing files between directory A and directory B
	
		Step 1 Sync. Check A -> B
		Iterate through  directory A Listing. . 
		Check for every file name present in  directory A for the same file name in directory B.
		if List B does not contain entry for filename than simply copy file from A -> B 
		if List B contains file name entry  than copy and overwrite if the filename entry in Directory A is newer than in  directory B.
	 
	  Step 2 Sync. Check B -> A
		Iterate through  directory B Listing.
		Check for every file name present in  directory B for the same file name in directory A.
		if List A does not contain entry for filename than simply copy file from B -> A.
		if List A contains file name entry  than copy and overwrite if the filename entry in Directory B  is newer than in  directory A.
	 ----------------------------------------------------------------------------*/
	
private void  syncDirectories()	 
{

    try
    {
    	     File FlDirA = new File(PathA); //  java.io.File Object representing directory path A.
    			 File FlDirB = new File(PathB);  // java.io.File Object representing directory path B.
    			
    		   File[] AryFileA = FlDirA.listFiles(); // Array of java.io.File objects for all files present in  directory A.
    			 File[] AryFileB = FlDirB.listFiles(); // Array of java.io.File objects for all files present in  directory B.
     		
    		 // Step 1: (Sync A -> B ). Iterate through   AryFileA[]  to look  updated and newly added files in  in directory A. 
    			for (File fla :AryFileA ) // For each item in AryFileA
          {
    			  String filename = fla.getName();
    				File Src = new File(PathA+Sep+filename); // File object for source file:filename in directory A
    				File Dest = new File(PathB+Sep+filename); // File object for corrosponding destination file  in directory B 
    				File Target = new File(PathB); //  Target directory for file copy operation - directory B
            this.SyncEntries.add(filename) ; // Add file to Entry list if its newly added.
    				                                 // As SyncEntries is a SET, so duplicate entries will have no effect, it will be ignored.
    				if(Dest.exists()) 
    				{
       				// File: (filename)  exists in target directory B. 
    					// Overwrite file in B with updated file from A if file in A is newer than file in  B.
    				  if( FileUtils.isFileNewer(Src,Dest) ) FileUtils.copyFile(Src,Dest,true); // Copy file from A -> B preserving time stamp and overwrite dest. file
    				} 
    				else  // else of  if(Dest.exists()) 
    				{
    				    // File does not exist in target diirectory B so it is new file added to directory A by the user.
    				    FileUtils.copyFileToDirectory(Src,Target, true); // preserve time stamp
								FileStore.addEntry(filename);
    				} // end - if(Dest.exists() 
    			}	// end - for (File fla :AryFileA )		
    			
    		 	// Step 2:  (Sync B -> A ). Iterate through  AryFileB[]  to look for updated and newly added files in  in directory B. 
    			for (File flb :AryFileB ) // For each item in AryFileB
          {
    			  String filename = flb.getName();
    				File Src = new File(PathB+Sep+filename); // File object for source file:filename in directory B
    				File Dest = new File(PathA+Sep+filename); // File object for corrosponding destination file  in directory A 
    				File Target = new File(PathA); //  Target directory for file copy operation - directory A
            this.SyncEntries.add(filename) ; // Add file to Entry list if its newly added.
                                             // As SyncEntries is a SET , so duplicate entries will have no effect, it will be ignored.
    							
    				if(Dest.exists()) 
    				{
       				// File: (filename)  exists in target directory A. 
    					// Overwrite file in A with updated file from B if file in B is newer than file in A.
    				  if( FileUtils.isFileNewer(Src,Dest) )
							{
							   //Overrite only if there is not lock onf file
								 boolean bLocked = FileStore.isLocked(filename);
								 if(bLocked )  // Do not overrite push the file in FIFO Queue for future update.
								 {   
								    java.sql.Timestamp tsSrc = new java.sql.Timestamp(Src.lastModified());
										java.sql.Timestamp tsStr = FileStore.lastLock(filename);
										// If updated file in B is newer that same file is FIFO cache.
										 
										if (  tsStr != null && tsSrc.after(tsStr ) ) FileStore.pushFile(PathB+Sep+filename, tsSrc);
								 }
								 else
								 {
								   FileUtils.copyFile(Src,Dest,true); // Copy file from A -> B preserving time stamp and overwrite dest. file
								 }
						
								 
							   
    				
						  }
						
						} 
    				else  // else of  if(Dest.exists()) 
    				{
    				    // File does not exist in  target diirectory  A, so it is new file added to directory B by the user.
    				    FileUtils.copyFileToDirectory(Src,Target, true); // preserve time stamp
								FileStore.addEntry(filename);
    				} // end - if(Dest.exists() 
    			}	// end - for (File flB :AryFileB )		
    
		 } // end try
		 catch(Exception ex)
		 {
		 System.out.println("Error in file synchronization: "+ex.getMessage());
		 ExceptionHelper.printException(ex);
		 }	// end catch
}	 // End method - syncDirectories()

 /* ---------------------------------------------------------------------------------------
  Method: checkDeletedFiles
	This method will check for file deletions by the user in either directory A or dirctory B.
	It will walk through synchronized set -> this.SyncEntries and check for existance of corresponding files in directory A and B.
	It the file is missing in either A or B  means that the user has on purpose deleted that file.
	The presence of file name entry in this.SyncEntries indicates that it was there earlier when program started.
	To properly handle delted file by the user,  we have to remove entry for the deleted file from - this.SyncEntries and also delete the 
	same file in mirrored directory  ( A->B) of (B->A) to keep it synchronized.
 -----------------------------------------------------------------------------------------*/	 
	 
  private void checkDeletedFiles()
	{
   
	try
  { 
    	 // Check deletions: Every file name entry in this.SyncEntreis must have corresponding  physical files in both directory A and directory B.
    	 ArrayList<String> Removed = new ArrayList<String>(); // List of file names to be removed from SyncEntries for deleted files by user.
    	 for( String filename: SyncEntries)
       {
    	     File CheckA = new File( PathA+Sep+filename); // File object for cheking existance of file :- filename in directory A
    			 File CheckB = new File( PathB+Sep+filename); // File object for cheking existance of file :- filename in directory B
    			 
    			 if ( !CheckA.exists() || !CheckB.exists() ) // File :- filename file, does not exist either in direcotry A or directory B
    			 {
    			    // Silently delete the file in both locations. 
    					// Unlike java's java.io.File class FileUtils of Apache Commons does not throw exceptions.
							boolean bLocked = FileStore.isLocked(filename);
							if (bLocked == false )FileUtils.deleteQuietly(CheckA);  
    					FileUtils.deleteQuietly(CheckB);
    					 Removed.add(filename); // Flag the name of deleted file for removing from SyncEntries
    			 }
    	 }	 // End for( String filename: SyncEntries)
    	  // Remove deleted file name entry from this.SyncEntries .
    	  for(String del_item : Removed )  this.SyncEntries.remove(del_item); 
	}catch(Exception ex)
  {
	   System.out.println(" Error in file deletion: "+ex.getMessage());
		 ExceptionHelper.printException(ex);
		 
	}
	 
} // end checkDeletedFolders()

} // End class definition FileSync
