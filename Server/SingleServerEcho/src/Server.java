

import java.io.IOException;
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
		fh.setDirectory("C:\\Users\\MehmetBerk\\Desktop\\dr\\");
		
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean getStatus(){
		return status;
	}
	
	public ServerWindow getServerWindow(){
		return sw;
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

						new Thread(new RequestHandler(getInstance(), s)).start();

					}catch(IOException e){
						e.printStackTrace();
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
			if (s!=null)
				s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	

}