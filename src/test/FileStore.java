/* Name: Tirth Sharad Kelkar*/
package test;
import java.io.*;
import java.util.*;
import java.nio.file.Files ;
import java.nio.file.FileSystems ;
import java.sql.*;
import java.text.SimpleDateFormat ;
import org.h2.Driver ;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/* File Store class which will maintain locked flag of directory list from  Direcotry A */

/*
Authored by Tirth Kelkar 
*/

public class FileStore
{
 
 




public static void initDatabase()
{
    System.out.println("Initializing file store for FIFO update cache.");
    
     String filelocks_table =    " CREATE  TABLE IF NOT EXISTS  \"filelocks\"  ( "
                                +"  \"RecordID\"  INTEGER NOT NULL IDENTITY PRIMARY KEY , "
                                +"  \"FileName\"  VARCHAR_IGNORECASE(255)  DEFAULT '' , "
																+"  \"FileSize\"  BIGINT DEFAULT 0 , " 
                                +"  \"Lock\"  SMALLINT  DEFAULT 0 , "
                                +"  \"Updated\"  TIMESTAMP  DEFAULT NULL  " 
                                +" ) ; " ;
    
     String filelocks_index = " CREATE INDEX IF NOT EXISTS   \"FileName\"  ON  \"filelocks\"  (  \"FileName\"  ) ; ";
     
    											 
     String updatecahce_table = " CREATE  TABLE IF NOT EXISTS  \"updatecache\"  ( "
                               +"  \"RecordID\"  INTEGER NOT NULL IDENTITY PRIMARY KEY , "
                               +"  \"FileName\"  VARCHAR_IGNORECASE(255)  DEFAULT NULL , "
                               +"  \"Updated\"  TIMESTAMP  DEFAULT NULL , "
                               +"  \"FileBytes\"  LONGVARBINARY  DEFAULT NULL "  
                               +"  ) ; "	;										
    											
     String updatecahce_index1	 = " CREATE INDEX IF NOT EXISTS   \"FileName\"  ON  \"updatecache\"  ( \" FileName\"  ) ; " ;
		 String updatecahce_index2   = " CREATE INDEX IF NOT EXISTS  \"Updated\" ON \"updatecache\" ( \"Updated\" ) ;" ;
		 
		 
		 

		 

		 
		 // Load Initial files names in lock list 
		  String Sep = FileSystems.getDefault().getSeparator();
		  String PathA = System.getProperty("user.dir")+Sep+"filedir"+Sep+"DirA" ; 
		  File FlDirA = new File(PathA); //
			File[] AryFileA = FlDirA.listFiles(); 
		  
		
		 
		 
     try
     {
		    Connection con = FileStore.connect();
				
				Statement stmt = con.createStatement();
				stmt.executeUpdate(" DROP TABLE IF EXISTS  \"filelocks\" ");
				stmt.executeUpdate(" DROP TABLE IF EXISTS \"updatecache\" ");
				
				stmt.executeUpdate(filelocks_table);
				stmt.executeUpdate(filelocks_index);
				stmt.executeUpdate(updatecahce_table);
				stmt.executeUpdate(updatecahce_index1);
				stmt.executeUpdate(updatecahce_index2);
				
				 for (File fla :AryFileA ) // For each item in AryFileA
         {
				      
		           String Name =  fla.getName();
							 Timestamp ts = new Timestamp(fla.lastModified());
							 long size = fla.length();
		           stmt.executeUpdate("INSERT INTO \"filelocks\" (  \"FileName\" , \"FileSize\" ,  \"Lock\" ,  \"Updated\"  ) VALUES ( '"+Name+"', "+size+",  0,  '"+ts.toString()+ "' ) ") ; 
							// System.out.println(" Adding :"+Name); // ### DEBUG
							 
		     }
					
				FileStore.disconnect(con);
		 
     }
     catch(Exception ex)
     {
		    ExceptionHelper.printException(ex);
     }		 		 
		 
} // end initDatabase()


public static void addEntry(String filename)
{
      String Sep = FileSystems.getDefault().getSeparator();
		  String PathA = System.getProperty("user.dir")+Sep+"filedir"+Sep+"DirA" ; 
			
      Connection con = FileStore.connect();
			boolean bFound = false;
		  try
		 {
		   
	     Statement stmt = con.createStatement(); 
	     ResultSet rscnt = stmt.executeQuery(" SELECT COUNT(*) FROM \"filelocks\"  WHERE  \"FileName\" = '"+filename+"' ");
			 if(rscnt.next())
       {
			     bFound = ( rscnt.getInt(1) > 0 ) ? true:false;
			 }
			 if(bFound == false)
			 {
			   File newfile = new File(PathA+Sep+filename);
				 Timestamp ts = new Timestamp(newfile.lastModified());
				 long size = newfile.length();
        stmt.executeUpdate("INSERT INTO \"filelocks\" (  \"FileName\" , \"FileSize\" ,  \"Lock\" ,  \"Updated\"  ) VALUES ( '"+filename+"', "+size+", 0,  '"+ts.toString()+ "' ) ") ; 
			 }
			 
     }
		 catch(Exception ex)
		 {
		    ExceptionHelper.printException(ex);
		 }
		 finally
		 {
		   FileStore.disconnect(con);
		 }
		 
}


   public static void pushFile(String fullpath , java.sql.Timestamp ts ) 
	 {
	  if(StringUtils.isBlank(fullpath)) return ;
		if(ts == null) return ;
		 Connection con = FileStore.connect();
		  try
		 {
		  File flobj = new File(fullpath) ;
		  if(!flobj.exists()) return;
		
		  String FileName = FilenameUtils.getName(fullpath);
	    byte[] FileBytes =  FileUtils.readFileToByteArray(flobj);
		  Timestamp now = new Timestamp(System.currentTimeMillis());

	     PreparedStatement stmt=con.prepareStatement("   INSERT INTO \"updatecache\" ( \"FileName\", \"Updated\", \"FileBytes\" ) VALUES ( ?, ?, ?  ) "  );
	     stmt.setString( 1 , FileName ) ; 
       stmt.setTimestamp( 2 , ts ) ; 
       stmt.setBytes( 3 , FileBytes ) ; 
			 stmt.executeUpdate();
     }
		 catch(Exception ex)
		 {
		    ExceptionHelper.printException(ex);
		 }
		 finally
		 {
		   FileStore.disconnect(con);
		 }
		 
		 
		 
		 
		 
	 }   

	 public static String popFile(int index) 
	 {
 
	     String FlName = "" ;
       Connection con = FileStore.connect();
		   try
		   {
		      boolean bFound = false;
					int nRecordID = 0;
					byte[] FileBytes = null;
					FlName = getFileForIndex(index);
					
          if(StringUtils.isBlank(FlName)) return "Error: File Not Found.";
	        Statement stmt = con.createStatement(); 
	        ResultSet rslt = stmt.executeQuery(" SELECT * FROM \"updatecache\" WHERE \"FileName\" = '"+FlName+"' ORDER BY \"Updated\" ASC ");
					if(rslt.next())
          {
					   bFound = true;
						 nRecordID = rslt.getInt("RecordID");
						 FileBytes = rslt.getBytes("FileBytes");
					
					}					
			 
			    if(bFound)
          {
					  String Sep = FileSystems.getDefault().getSeparator();
					  String DestFilePath = System.getProperty("user.dir")+Sep+"filedir"+Sep+"DirA"+Sep+FlName; ; 
						File DstFl = new  File(DestFilePath);
						if(DstFl.exists())  FileUtils.deleteQuietly(DstFl);
						FileUtils.writeByteArrayToFile(DstFl, FileBytes);
						
						File UpdFile = new  File(DestFilePath);
						java.sql.Timestamp ts = new java.sql.Timestamp( UpdFile.lastModified()) ;
						long length = UpdFile.length();
						
					  stmt.executeUpdate(" DELETE * FROM \"updatecache\" WHERE \"RecordID\" = "+nRecordID+" " );
						stmt.executeUpdate(" UPDATE \"filelocks\"  SET \"FileSize\" = "+length+" , \"Updated\"  = '"+ts.toString()+"' WHERE \"FileName\" = '"+FlName+"' ");
						
					}					
			 
        }
		    catch(Exception ex)
		    {
		      ExceptionHelper.printException(ex);
		    }
		    finally
		    {
		      FileStore.disconnect(con);
		    }
		 
		return FlName ; 
	 }   

	 
	 public static java.sql.Timestamp lastLock(String name)
   {
      Connection con = FileStore.connect();
			java.sql.Timestamp ts = null;
		  try
		 {
		   
	     Statement stmt = con.createStatement(); 
	      
	     
			 ResultSet rslt = stmt.executeQuery(" SELECT * FROM \"updatecache\" WHERE \"FileName\" = '"+name+"' ORDER BY \"Updated\" ASC ");
			 if(rslt.last())
       {
			   ts = rslt.getTimestamp("Updated");
			 }			 
			 
			 
     }
		 catch(Exception ex)
		 {
		    ExceptionHelper.printException(ex);
		 }
		 finally
		 {
		   FileStore.disconnect(con);
		 }
		 return ts;
	 }	 
	 
	 
	 
	 public static boolean isLocked(String name )
	 {
	   short lock = 0;
		Connection con = FileStore.connect(); 
	  try
		 {
		   
	     Statement stmt = con.createStatement(); 
	     ResultSet rslt = stmt.executeQuery(" SELECT * FROM \"filelocks\" WHERE  \"FileName\" = '"+name+"' ");
			
			 if( rslt!=null && rslt.next()) lock = rslt.getShort("Lock");
     }
		 catch(Exception ex)
		 {
		    ExceptionHelper.printException(ex);
		 }
		 finally
		 {
		   FileStore.disconnect(con);
		 }
		 return lock > 0 ? true : false ;
	 }
	 


	  public static String updateLock(int index, boolean lock_status)
	  {
		
		 Timestamp ts1 = new Timestamp(System.currentTimeMillis());
		   
	   String FlName = getFileForIndex(index);
     if(StringUtils.isBlank(FlName)) return "Error: File Not Found.";
		 
		 Connection con = FileStore.connect();
		 try
		 {
		   short lck = ( lock_status == true)? (short)1 : (short)0;
	     Statement stmt = con.createStatement(); 
			 
	     stmt.executeUpdate (" UPDATE \"filelocks\" SET \"Lock\" = "+lck+" , \"Updated\" = '"+ts1.toString()+"' WHERE  \"FileName\" = '"+FlName+"' ");
     }
		 catch(Exception ex)
		 {
		    ExceptionHelper.printException(ex);
		 }
		 finally
		 {
		   FileStore.disconnect(con);
		 }
		 
		 return FlName;
	  }
	 
	  private static String getFileForIndex( int index)
	 {
	    Connection con = FileStore.connect();
      int n=0;
			String ret = "";
     try
		 {
		  
	     Statement stmt = con.createStatement(); 
	     ResultSet rslt = stmt.executeQuery("SELECT * FROM \"filelocks\" ORDER BY \"RecordID\" ");
			 while(rslt.next())
			   {
			   String Name = rslt.getString("FileName");
				 if(n==index) ret = Name;
				 n++; 
        }
     }
		 catch(Exception ex)
		 {
		    ExceptionHelper.printException(ex);
		 }
		 finally
		 {
		   FileStore.disconnect(con);
		 }				 
		 return ret ; 
				 
	 }
	 
	 
	 
	  public static void showLocks()
	  {
	   // Name , Size, Updated, Lock
		 System.out.print("Index");
		 System.out.print(setStringWidth(" | File Name",40));
		 System.out.print(setStringWidth(" | File Size",20));
		 System.out.print(setStringWidth(" | Updated On",25));
		 System.out.println(setStringWidth(" | Lock Status",15));
		 
		 
		 int index = 0;
		  Connection con = FileStore.connect();
     try
		 {
		  
	     Statement stmt = con.createStatement(); 
	     ResultSet rslt = stmt.executeQuery("SELECT * FROM \"filelocks\" ORDER BY \"RecordID\" ");
			 while(rslt.next())
			 {
			   String Name = rslt.getString("FileName");
				 long Size = rslt.getLong("FileSize");

				 String LockStatus = ( rslt.getShort("Lock") > (short)0  )?  "LOCKED" : "NOT LOCKED" ;
				 Timestamp Updated = rslt.getTimestamp("Updated");
				 String ShowSize = DirectoryHelper.showByteSize(Size);
          String ShowTm = FileStore.showTime(Updated);
					 
					StringBuilder rec = new StringBuilder();
					rec.append(StringUtils.leftPad(""+index, 5));
					rec.append(setStringWidth(" | "+Name,40));
					rec.append(setStringWidth(" | "+ShowSize ,20));
					rec.append(setStringWidth(" | "+ShowTm ,  25));
					rec.append(setStringWidth(" | "+LockStatus,15));
					
					System.out.println(rec);
					 
					
				 
			   index++ ;
			 } // end - while(rslt.next())

     }
		 catch(Exception ex)
		 {
		    ExceptionHelper.printException(ex);
		 }
		 finally
		 {
		   FileStore.disconnect(con);
		 }
		 	   
	 
		 
	  } // showLocks()
	 

    public static Connection connect()
    {
		
       String Sep = FileSystems.getDefault().getSeparator();
       String dbfile = System.getProperty("user.dir")+Sep+"filedir"+Sep+"database"+Sep+"filestore" ;
		
		  Connection conn = null;
      try
			{
			 Class.forName ("org.h2.Driver");
			conn =  DriverManager.getConnection ("jdbc:h2:"+dbfile+";AUTO_SERVER=TRUE", "sa", ""); 
		  }catch(Exception ex)
			{
			  ExceptionHelper.printException(ex);
			}
		  return conn;
		  
    }
  
	
	

    public static void disconnect(Connection conx)
    {
      try
      {
    	  if(conx!=null) conx.close();
    
      }catch(Exception ex)
      {
    	    ExceptionHelper.printException(ex);
      }	
    
    } // end Disconnect

		
		public static String setStringWidth(String str, int wdt )
		 {
		   if(StringUtils.isBlank(str)) return StringUtils.rightPad("", wdt);
			 String ret="";
			 if(str.length() > wdt ) ret = StringUtils.abbreviate(str, "...",wdt);
			 else 	ret = StringUtils.rightPad(str, wdt);
		   return ret;
		 }
		
		
		public static String showTime(java.sql.Timestamp ts)
		{
		 if(ts==null) return " Time Not Set" ;
		 SimpleDateFormat fmt = new java.text.SimpleDateFormat("dd MMM yyyy hh:mm:ss");
		 return fmt.format(ts);
		}
		
		
    /* TEMP
		Connection con = FileStore.connect();
		  try
		 {
		   
	     Statement stmt = con.createStatement(); 
	     
			 
			 
			 
     }
		 catch(Exception ex)
		 {
		    ExceptionHelper.printException(ex);
		 }
		 finally
		 {
		   FileStore.disconnect(con);
		 }
		 
		 
		 */		 
		 
		 

} // end  - class FileStore


