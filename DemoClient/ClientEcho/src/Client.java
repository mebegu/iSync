// A simple Client Server Protocol .. Client for Echo Server

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Client implements Runnable
{

	public Client(){}
	public Client(ClientWindow serverWindow, int i) {
		// TODO Auto-generated constructor stub
	}
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		InetAddress address = null;
		try {
			address = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Socket cmdSocket=null;
		ServerSocket dataServer = null;
		String line=null;
		BufferedReader cmdLnReader=null;
		PrintWriter cmdOut=null;
		BufferedReader cmdIn = null;
		try
		{
			dataServer = new ServerSocket(4446);
			cmdSocket =new Socket(address, 4445); // You can use static final constant PORT_NUM
			cmdLnReader= new BufferedReader(new InputStreamReader(System.in));
			cmdIn = new BufferedReader(new InputStreamReader(cmdSocket.getInputStream())); 
			cmdOut= new PrintWriter(cmdSocket.getOutputStream());
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.err.print("IO Exception");
		}

		System.out.println("Client Address : "+address);
		System.out.println("Enter Data to echo Server ( Enter QUIT to end):");

		String response=null;
		try
		{
			line=cmdLnReader.readLine(); 
			while(line.compareTo("END")!=0)
			{
				cmdOut.println(line);
				cmdOut.flush();
				response = cmdIn.readLine(); //reads object
				
				if(verifyResponse(response)){
					Socket dataInSocket = dataServer.accept();
					ObjectInputStream dataInStream = new ObjectInputStream(dataInSocket.getInputStream());
					Object data = dataInStream.readObject();
					
					if(data instanceof HashMap){
						HashMap<String, byte[]> yourMap = new HashMap<String, byte[]>();
						yourMap = (HashMap<String, byte[]>)data;
						System.out.println("Server Response : "+yourMap);
					}else if(data instanceof byte[]){
						printFile("ds.PNG", (byte[]) data);
						System.out.println("File data received. ");
						
					}else{
						if(data instanceof String)
							System.out.println("Server Response : "+data);
						else
							System.out.println("Server Response : "+response);
					}
				}else{
					System.out.println("Server Response : "+response);
				}

				line=cmdLnReader.readLine();
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
				cmdIn.close();
				cmdOut.close();
				cmdSocket.close();
				cmdLnReader.close();
				dataServer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Connection Closed");
		}

	}
	private boolean verifyResponse(String response) {
		if(response.startsWith("200"))
			return true;
		else			
			return false;
	}
	
	private void printFile(String fileName, byte[] data) throws IOException {
		String path = "C:\\Users\\MehmetBerk\\Desktop\\out\\"+fileName;
		FileOutputStream fos = new FileOutputStream(path);
		fos.write(data);
		fos.close();
	}
}