/* Name: Tirth Sharad Kelkar*/
/* UTA ID: 1001826874 */
package test ;
import java.util.* ;
import java.io.* ;
/*
  ExceptionHelper - helper class that only prints the exception on stdout.
  

*/
/*
Authored by Tirth Kelkar 
*/

public class ExceptionHelper
{

    public static void printException(Exception ex)
    {
    
              ByteArrayOutputStream bout = new ByteArrayOutputStream();
              ex.printStackTrace(new PrintStream(bout));
              String Trace =  bout.toString();
              System.out.println("----------------------------------------------------");
              System.out.println("EXCEPTION: "+ex.getMessage());
              System.out.println("----------------------------------------------------");
              System.out.println(Trace);
              System.out.println("----------------------------------------------------");
    }


}