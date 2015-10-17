// A simple Client Server Protocol .. Client for Echo Server

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

public class Client 
{

	@SuppressWarnings("unchecked")
	public static void main(String args[]) throws IOException
	{
		    InetAddress address=InetAddress.getLocalHost();
		    Socket s=null;
		    String line=null;
		    BufferedReader br=null;
		    PrintWriter os=null;
		    ObjectInputStream input = null;
		    try
		    {
		        s =new Socket(address, 4445); // You can use static final constant PORT_NUM
		        br= new BufferedReader(new InputStreamReader(System.in));
		        input = new ObjectInputStream(s.getInputStream());
		        
		        os= new PrintWriter(s.getOutputStream());
		    }
		    catch (IOException e)
		    {
		        e.printStackTrace();
		        System.err.print("IO Exception");
		    }
		
		    System.out.println("Client Address : "+address);
		    System.out.println("Enter Data to echo Server ( Enter QUIT to end):");
		
		    Object response=null;
		    try
		    {
		        line=br.readLine(); 
		        while(line.compareTo("QUIT")!=0)
		        {
	                os.println(line);
	                os.flush();
	                response = input.readObject();
	                
	                HashMap<String, byte[]> yourMap = new HashMap<String, byte[]>();
	                
	                if(response instanceof HashMap){
	                	yourMap = (HashMap<String, byte[]>)response;
	                	System.out.println("Server Response : "+yourMap);
	                }else{
	                	System.out.println("Server Response : "+response);
	                }

	                line=br.readLine();
		        }
		
		
		
		    }
		    catch(IOException | ClassNotFoundException e)
		    {
		        e.printStackTrace();
		        System.out.println("Socket read Error");
		    }
		    finally
		    {
		        input.close();os.close();br.close();
		        s.close();
		        System.out.println("Connection Closed");
		    }
		
		}
}