import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RequestHandler implements Runnable {
	private Server server;
	private Socket s;
	

	public RequestHandler(Server server, Socket socket){
		this.server = server;
		s = socket;
	}
	
	@Override
	public void run() {
		
		try {
			takeReq();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void takeReq() throws IOException{
		String line=null;
		BufferedReader  is = null;
		ObjectOutputStream output = null;

		is= new BufferedReader(new InputStreamReader(s.getInputStream()));
		output = new ObjectOutputStream(s.getOutputStream());


		line=is.readLine();
		while(server.getStatus())
		{
			server.getServerWindow().write("Client Request : "+line);
			Object response = interpret(line);

			if(response.equals("CLOSE")) break;

			output.writeObject(response);
			output.flush();
			server.getServerWindow().write("Response to Client: "+response);
			line=is.readLine();
		}   


		System.out.println("Connection Closing..");
		if (is!=null)
			is.close(); 

		if(output!=null)
			output.close();

	}
	
	public void close() throws IOException{
		if (s!=null)
			s.close();
	}
	
	//GETFILE filename request requires filename with no spaces
	public Object interpret(String cmd){
		if(cmd == null || cmd.equals("END"))
			return "CLOSE";
		else if(cmd.equals("GETHASH"))
			return FileHandler.getInstance().getFilesHash();
			
		else if (cmd.startsWith("GETFILE")){
			String[] req = cmd.split(" ");

			if(req.length == 2){
				byte[] file = FileHandler.getInstance().getFile(req[1]);
				return file != null ? file : "FILE DONT EXIST!: "+req[1];
			}else
				return "BAD REQUEST";

		}else
			return "BAD REQUEST";
		


	}

}
