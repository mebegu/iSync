

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server
{
	private int port;
	private ServerSocket cmdServer=null;
	private ServerWindow sw;
	private boolean status;
	private FileHandler fileHandler = null;
	private Socket cmdSocket = null;

	public Server(ServerWindow sw, int port){
		status=false;
		this.sw = sw;
		this.port = port;
		fileHandler = FileHandler.getInstance();
		fileHandler.setDirectory("C:\\Users\\MehmetBerk\\Desktop\\dr\\");
		

	}

	public boolean getStatus(){
		return status;
	}
	
	public ServerWindow getServerWindow(){
		return sw;
	}


	public void activate(){
		status = true;
		
		try {
			cmdServer = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		new Thread(new Runnable() {
			public void run() {

				while(status){
					try{
						sw.write("Listening...");
						cmdSocket= cmdServer.accept();
						sw.write("Connection Established: "+cmdSocket);
						
						new Thread(new RequestHandler(getInstance(), cmdSocket)).start();

					}catch(IOException e){
						System.err.println(e.getLocalizedMessage());
					}
				}
			}
		}).start();

	}
	
	public Server getInstance(){
		return this;
	}
	
	public void shutdown(){
		
		status = false;
		try {
			if(cmdServer!=null)
				cmdServer.close();
			if(cmdSocket!=null)
				cmdSocket.close();
			
			sw.write("Server is Closed.");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	

}