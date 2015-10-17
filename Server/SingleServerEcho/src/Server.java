// echo server
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server
{
	private Socket s = null;
	private ServerSocket ss=null;
	private ServerWindow sw;
	private boolean status;
	private FileHandler fh = null;

	public Server(ServerWindow sw, int port){
		status=false;
		this.sw = sw;
		
		fh = FileHandler.getInstance();
		fh.setDirectory("C:\\Users\\MEHMETBERKGURCAY\\Desktop\\dir\\");
		
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean getStatus(){
		return status;
	}


	public void activate(){
		status = true;
		
		new Thread(new Runnable() {
			public void run() {

				while(status){
					try{
						sw.write("Listening");
						s= ss.accept();
						sw.write("Connection Established: "+s);
						takeReq(s);

					}catch(IOException e){
						e.printStackTrace();
					}
				}
			}
		}).start();

	}
	
	public void shutdown(){
		status = false;
		try {
			close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public void takeReq(Socket s) throws IOException{
		String line=null;
		BufferedReader  is = null;
		ObjectOutputStream output = null;

		is= new BufferedReader(new InputStreamReader(s.getInputStream()));
		output = new ObjectOutputStream(s.getOutputStream());


		line=is.readLine();
		sw.write("Client Request : "+line);
		while(status)
		{
			Object response = interpret(line);

			if(response.equals("CLOSE")) break;

			output.writeObject(response);
			output.flush();
			sw.write("Response to Client: "+response);
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
		if(cmd.equals("GETHASH"))
			return fh.getFilesHash();
			
		else if (cmd.startsWith("GETFILE")){
			String[] req = cmd.split(" ");

			if(req.length == 2){
				byte[] file = fh.getFile(req[1]);
				return file != null ? file : "FILE DONT EXIST!: "+req[1];
			}else
				return "BAD REQUEST";

		}else if(cmd.equals("END"))
			return "CLOSE";
		
		else
			return "BAD REQUEST";
		


	}
	

}