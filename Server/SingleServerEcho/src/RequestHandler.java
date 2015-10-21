import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class RequestHandler implements Runnable {
	private Server server;
	private Socket s;
	private final static int clientPort = 4446;

	public RequestHandler(Server server, Socket socket){
		this.server = server;
		s = socket;
	}
	
	@Override
	public void run() {
		
		try {
			takeReq();
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
		}

	}
	
	public void takeReq() throws IOException{
		String request=null;
		BufferedReader  cmdIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
		PrintWriter cmdOut = new PrintWriter(s.getOutputStream());

		
		while(server.getStatus())
		{
			request=cmdIn.readLine();
			server.getServerWindow().write(" "+s.getInetAddress()+" |Client Request : "+request);
			if(request == null || request.equals("END")) break;
			
			if(verifyReq(request)){
				cmdOut.println("200 OK");
				server.getServerWindow().write(" "+s.getInetAddress()+" |Response to Client: 200 OK");
				cmdOut.flush();
				
				Object response = interpret(request);
				Socket dataSocket = new Socket(s.getInetAddress(), clientPort);
				ObjectOutputStream dataOut = new ObjectOutputStream(dataSocket.getOutputStream());
				dataOut.writeObject(response);
				dataOut.flush();
				server.getServerWindow().write(" "+s.getInetAddress()+" |Data is sent: "+response);
				
				dataSocket.close();
				dataOut.close();
				
			}else{
				cmdOut.println("400 BAD REQUEST");
				cmdOut.flush();
				server.getServerWindow().write(" "+s.getInetAddress()+" |400 BAD REQUEST");
			}
			
		}   


		server.getServerWindow().write("Connection Closed With: "+s.getInetAddress());
		cmdIn.close(); 
		cmdOut.close();
		s.close();

	}

	
	public Object interpret(String cmd){
		if(cmd.equals("GETHASH"))
			return FileHandler.getInstance().getFilesHash();
			
		else if (cmd.startsWith("GETFILE")){
			String[] req = cmd.split(" ",2);
			if(req.length == 2){
				byte[] file = FileHandler.getInstance().getFile(req[1]);
				return file != null ? file : "FILE DONT EXIST!: "+req[1];
			}else{
				return "400 BAD REQUEST";
			}
		}else
			return "BAD REQUEST";

	}
	
	public boolean verifyReq(String cmd){
		if(cmd == null || cmd.equals("END"))
			return true;
		else if(cmd.equals("GETHASH"))
			return true;
		else if (cmd.startsWith("GETFILE"))
			return true;
		else
			return false;
	}

}
