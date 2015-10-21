
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class FileHandler {
	private static FileHandler instance;
	private String dir;
	
	public static FileHandler getInstance(){
		if(instance == null) 
			instance = new FileHandler();
		
		return instance;
	}
	
	private FileHandler(){
	}
	
	// Sets the directory of the FileHandler's workspace
	public void setDirectory(String dir){
		this.dir = dir;
	}
	
	private String[] getAllFileNames(){
		File folder = new File(dir);
		File[] listOfFiles = folder.listFiles();

		int len = listOfFiles.length;
		
		String[] names = new String[len];

		for (int i=0; i<len ; i++) {
			names[i] = listOfFiles[i].getName();
		}
		
		return names;
	}
	
	public byte[] getFile(String fileName){
		Path path = Paths.get(dir+fileName);
		byte[] data = null;
		
		try {
			data = Files.readAllBytes(path);
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
		}
		
		return data;
	}
	
	public HashMap<String, byte[]> getFilesHash(){
		HashMap<String, byte[]> map = new HashMap<String, byte[]>();
		String fileName = "";
		String[] names = getAllFileNames();
		
		for (int i = 0; i < names.length; i++) {
			fileName = names[i];
			map.put(fileName, hashOfFile(getFile(fileName)));
		}
		
		return map;
	}
	
	private byte[] hashOfFile(byte[] file){
		String myHash = "MD5"; // or "SHA-1" or "SHA-256"
		MessageDigest complete= null;
		
		try {
			complete = MessageDigest.getInstance(myHash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return complete.digest(file);
	}
	

	
	
}
