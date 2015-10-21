

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Client
{
	private final static int serverPort = 4445;
	private final static int clientPort = 4446;
	private final static String dir = "C:\\Users\\MEHMETBERKGURCAY\\Desktop\\client\\";

	
	private Socket cmdSocket=null;
	private ServerSocket dataServer = null;
	private ClientWindow cw;
	private InetAddress serverAddress;
	private FileHandler fh;
	private BufferedReader cmdLnReader;
	private BufferedReader cmdIn;
	private PrintWriter cmdOut;
	private HashMap<String, ArrayList<String>> fileStatus;
	

	public Client(ClientWindow clientWindow, String sAddress) {
		cw = clientWindow;

		try {
			serverAddress = InetAddress.getByName(sAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		fh = FileHandler.getInstance();
		fh.setDirectory(dir);
		initialize();
	}
	
	public void syncCheck(){
		cw.resetText();
		HashMap<String, byte[]> clHash = fh.getFilesHash();
		HashMap<String, byte[]> srHash = getHashes();
		String result = "You have to update your storage with the following files:\n";
		
		
		for (final String key : clHash.keySet()) {
		    if (srHash.containsKey(key)) {
		        if(!Arrays.equals(clHash.get(key), srHash.get(key))){
		        	fileStatus.get("up").add(key);
		        	result += key+" update\n";
		        }
		    }else{
		    	fileStatus.get("del").add(key);
		    	result += key+" delete\n";
		    }
		}
		
		for (final String key : srHash.keySet()){
			if(!clHash.containsKey(key)){
				fileStatus.get("add").add(key);
				result += key+" add\n";
			}
		}
		cw.write(result);
		
	}
	
	public void syncAll(){
		cw.write("Start syncing.");
		
		for (final String fName : fileStatus.get("up")) {
			fh.deleteFile(fName);
			getAndSaveFile(fName);
			cw.write(fName+" is updated succesfully.");
		}
		for (final String fName : fileStatus.get("add")) {
			
			getAndSaveFile(fName);
			cw.write(fName+" is downloaded succesfully.");
			
		}
		for (final String fName : fileStatus.get("del")) {
			
			fh.deleteFile(fName);
			cw.write(fName+" is deleted succesfully.");
			
		}
		
		clearFileStatus();

	}
	
	public void syncFile(String fName){
		if(fileStatus.get("up").contains(fName)){
			
			fh.deleteFile(fName);
			getAndSaveFile(fName);
			cw.write(fName+" is updated succesfully.");
			fileStatus.get("up").remove(fName);
			
		}else if(fileStatus.get("add").contains(fName)){
			
			getAndSaveFile(fName);
			cw.write(fName+" is downloaded succesfully.");
			fileStatus.get("add").remove(fName);
			
		}else if(fileStatus.get("del").contains(fName)){
			
			fh.deleteFile(fName);
			cw.write(fName+" is deleted succesfully.");
			fileStatus.get("del").remove(fName);
			
		}else{
			cw.write(fName+" does not exist.");
		}
	}
	
	public void end(){
		cmdOut.println("END");
		cmdOut.flush();
		close();
		cw.write("Session is ended.");
	}
	
	
	
	@SuppressWarnings("unchecked")
	private HashMap<String, byte[]> getHashes() {
		try {
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

		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;


	}
	
	private void getAndSaveFile(String fName){
		try{
			cmdOut.println("GETFILE "+fName);
			cmdOut.flush();
			String response = cmdIn.readLine();

			if(verifyResponse(response)){
				Socket dataInSocket = dataServer.accept();
				ObjectInputStream dataInStream = new ObjectInputStream(dataInSocket.getInputStream());
				Object data = dataInStream.readObject();
				if(data instanceof byte[]){
					fh.printFile(fName, (byte[]) data);

				}else{
					if(data instanceof String)
						cw.write("Server Response : "+data);
					else
						cw.write("Server Response : "+response);
				}


			}else{
				cw.write("Server Response : "+response);
			}

		}catch(IOException | ClassNotFoundException e){
			e.printStackTrace();
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
			
			fileStatus = new HashMap<String, ArrayList<String>>();
			fileStatus.put("up", new ArrayList<String>());
			fileStatus.put("add", new ArrayList<String>());
			fileStatus.put("del", new ArrayList<String>());
			
			
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	private void clearFileStatus(){
		fileStatus.get("up").clear();
		fileStatus.get("add").clear();
		fileStatus.get("del").clear();
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