/* Name: Tirth Sharad Kelkar*/

package test ;
import java.io.*;
import java.util.*;
import java.text.*;
import org.apache.commons.lang3.StringUtils;
/*
Authored by Tirth Kelkar 
*/

public class DirectoryHelper
{
	// Variables required to show file size in human readable format:
	 private static final long Peta = 1125899906842624L ;
   private static final long Tera = 1099511627776L ;
   private static final long Giga = 1073741824L ;
   private static final long Mega = 1048576L ;
   private static final long Kilo = 1024L  ; 
	 
	
	
	
	public static  ArrayList<DirectoryHelper.FileInfo> getDirectoryListing(String filepath  )
	{
	    ArrayList<DirectoryHelper.FileInfo> fList = new ArrayList<DirectoryHelper.FileInfo>();
	    
			SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
			File fldir = new File(filepath);
			File[] AryFiles = fldir.listFiles();
			
			   for(File fl : AryFiles )
         {
				      
				      DirectoryHelper.FileInfo inf = new DirectoryHelper.FileInfo(); 
							inf.FileName = fl.getName();
							inf.FileSize = fl.length() ;
							inf.ShowSize = DirectoryHelper.showByteSize(inf.FileSize);
							inf.LastChanged  = new Date(fl.lastModified());
							inf.ShowUpdated = fmt.format(inf.LastChanged);
							inf.RecordLine = DirectoryHelper.setStringWidth(inf.FileName, 50)+" | "+DirectoryHelper.setStringWidth(inf.ShowSize, 10)+" | "+DirectoryHelper.setStringWidth(inf.ShowUpdated,20);
							fList.add(inf);
							
							/*
							  public String FileName = "";
		 public long FileSize = 0;
		 public String ShowSize = "";
		 public Date  LastChanged = null;
	 
							*/
							
         }				 
			
			return fList;
	}
	

// Helper function to read file size in human readable format
public static String showByteSize(long len )
   {
        DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###.##");
        String ret = "" ; double size =0;
        if(len >= Peta )         ret = df.format((double)( (double)len/Peta ))+" Peta Bytes" ;
        else if (len >= Tera )   ret = df.format((double)( (double)len/Tera ))+" TB" ;
        else if ( len >= Giga )  ret = df.format((double)( (double)len/Giga ))+" GB" ;
        else if ( len >= Mega )  ret = df.format((double)( (double)len/Mega ))+" MB" ;
        else if ( len >= Kilo )  ret = df.format((double)( (double)len/Kilo ))+" KB" ;
        else  ret = len+" Bytes" ;
        
				return ret ;
  }
	
	 public static String recordHeaderLine()
	 {
	    return DirectoryHelper.setStringWidth("File Name", 50)+" | "+DirectoryHelper.setStringWidth("Size", 10)+" | "+DirectoryHelper.setStringWidth("Last Updated",20);
	 }

	 public static String setStringWidth(String str, int wdt )
		 {
		   if(StringUtils.isBlank(str)) return StringUtils.rightPad("", wdt);
			 String ret="";
			 if(str.length() > wdt ) ret = StringUtils.abbreviate(str, "...",wdt);
			 else 	ret = StringUtils.rightPad(str, wdt);
		   return ret;
		 }

   // Inner class to provide file related information
	 
	 public static class FileInfo
	 {
	   public String FileName = "";
		 public long FileSize = 0;
		 public String ShowSize = "";
		 public Date  LastChanged = null;
		 public String ShowUpdated = "";
		 public String RecordLine = "" ;
	 }

	
	



	
} // end class definition DirectoryHelper
