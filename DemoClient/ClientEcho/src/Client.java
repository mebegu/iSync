// A simple Client Server Protocol .. Client for Echo Server

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Client implements Runnable
{

	public Client(){}
	@Override
	public void run() {
		InetAddress address = null;
		try {
			address = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Socket s=null;
		String line=null;
		BufferedReader br=null;
		PrintWriter os=null;
		ObjectInputStream input = null;
		try
		{
			s =new Socket(address, 4445); // You can use static final constant PORT_NUM
			br= new BufferedReader(new InputStreamReader(System.in));
			input = new ObjectInputStream(s.getInputStream()); //get java object from the server.

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
			while(line.compareTo("END")!=0)
			{
				os.println(line);
				os.flush();
				response = input.readObject(); //reads object

				HashMap<String, byte[]> yourMap = new HashMap<String, byte[]>();

				if(response instanceof HashMap){
					yourMap = (HashMap<String, byte[]>)response;
					System.out.println("Server Response : "+yourMap);
				}else if(response instanceof byte[]){
					printFile("ds.PNG", (byte[]) response);
					System.out.println("File data received. ");
					
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
			try {
				input.close();
				os.close();
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Connection Closed");
		}

	}
	private void printFile(String fileName, byte[] data) throws IOException {
		String path = "C:\\Users\\MehmetBerk\\Desktop\\out\\"+fileName;
		FileOutputStream fos = new FileOutputStream(path);
		fos.write(data);
		fos.close();
	}
}