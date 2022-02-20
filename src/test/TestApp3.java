/* Name: Tirth Sharad Kelkar*/
/* UTA ID: 1001826874 */
package test ;
/*
Authored by Tirth Kelkar 
*/
public class TestApp3
{
    public static void main(String[] args)
    {
         System.out.println("Fetching data from Server A and Server B (through A )");
        try
        {
              TestClient clnt = new TestClient();
              String Resp = clnt.getServerResponse(ServerApp3.PortA);
              System.out.println(Resp);
        }
        catch(Exception e)
        {
             System.out.println("Exception Thrown: "+e.getMessage());
        }
     } // end main
} // End class TestApp3