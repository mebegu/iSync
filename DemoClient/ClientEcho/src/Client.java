// A simple Client Server Protocol .. Client for Echo Server

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Client
{
	private Socket cmdSocket=null;
	private ServerSocket dataServer = null;
	private ClientWindow cw;
	private final int clientPort = 4446;
	private int serverPort;
	private InetAddress serverAddress;
	private FileHandler fh;
	private BufferedReader cmdLnReader;
	private BufferedReader cmdIn;
	private PrintWriter cmdOut;
	

	public Client(ClientWindow clientWindow, String sAddress, int port) {
		cw = clientWindow;
		serverPort = port;
		try {
			serverAddress = InetAddress.getByName(sAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		fh = FileHandler.getInstance();
		fh.setDirectory("C:\\Users\\MehmetBerk\\Desktop\\out\\");
		initialize();
	}
	
	public void syncCheck(){
		cw.write("Sync check is pressed.");
	}
	
	public void syncAll(){
		cw.write("Sync all is pressed.");
	}
	
	public void syncFile(String fileName){
		cw.write("The file "+ fileName+" is selected to sync.");
	}
	
	public void end(){
		cw.write("Session is ended.");
	}
	
	
	
	@SuppressWarnings("unchecked")
	private HashMap<String, byte[]> getHashes(String op, String filename) throws IOException, ClassNotFoundException{
		cmdOut.println("GETHASH");
		cmdOut.flush();
		String response = cmdIn.readLine();
		
		if(verifyResponse(response)){
			Socket dataInSocket = dataServer.accept();
			ObjectInputStream dataInStream = new ObjectInputStream(dataInSocket.getInputStream());
			Object data = dataInStream.readObject();
			
			if(data instanceof HashMap){
				HashMap<String, byte[]> serverHashes = new HashMap<String, byte[]>();
				serverHashes = (HashMap<String, byte[]>)data;
				cw.write("Server Response : "+serverHashes);
				return serverHashes;
			}else{
				cw.write("Server Response : "+response);
				return null;
			}
			
		}else{
			cw.write("Server Response : "+response);
			return null;
		}

		
	}
	
	private void getAndSaveFile(String fName) throws IOException, ClassNotFoundException{
		cmdOut.println("GETFILE "+fName);
		cmdOut.flush();
		String response = cmdIn.readLine();
		
		if(verifyResponse(response)){
			Socket dataInSocket = dataServer.accept();
			ObjectInputStream dataInStream = new ObjectInputStream(dataInSocket.getInputStream());
			Object data = dataInStream.readObject();
			if(data instanceof byte[]){
				fh.printFile(fName, (byte[]) data);
				cw.write("File data received. ");
				
			}else{
				if(data instanceof String)
					cw.write("Server Response : "+data);
				else
					cw.write("Server Response : "+response);
			}
			
			
		}else{
			cw.write("Server Response : "+response);
		}
		
		
			
		
	}
	
	private boolean verifyResponse(String response) {
		if(response.startsWith("200"))
			return true;
		else			
			return false;
	}
	
	private void initialize(){
		try
		{
			dataServer = new ServerSocket(clientPort);
			cmdSocket =new Socket(serverAddress, serverPort); // You can use static final constant PORT_NUM
			cmdLnReader= new BufferedReader(new InputStreamReader(System.in));
			cmdIn = new BufferedReader(new InputStreamReader(cmdSocket.getInputStream())); 
			cmdOut= new PrintWriter(cmdSocket.getOutputStream());
			cw.write("Connection established with sever: "+serverAddress);
			cw.write("Enter Data to echo Server :");
			
			
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	private void close() {
		try {
			cmdIn.close();
			cmdOut.close();
			cmdSocket.close();
			cmdLnReader.close();
			dataServer.close();
			cw.write("Connection Closed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public String toString() {
		try {
			return "Local [address / port]: " + InetAddress.getLocalHost().getHostAddress() +" / "+clientPort 
					+ "\nServer  [address / port]: "+ serverAddress.getHostAddress()+" / "+serverPort; 

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	

}