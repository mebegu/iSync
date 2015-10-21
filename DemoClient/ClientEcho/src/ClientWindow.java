

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
public class ClientWindow extends SWindow implements ActionListener {

	private JButton syncCheck;
	private JButton syncAll;
	private JButton syncFile;
	private JTextField file;
	private JTextArea text;
	private Client client;
	private UIButton btUI;
	private JButton end;
	
	public ClientWindow (){
		
		prepareWindow();
		setSize(600,400);
		
		try {
			client = new Client(this, InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		resetText();
	}

	private void prepareWindow() {
		btUI = new UIButton();
		
		syncCheck = new JButton("Sync Check");
		syncCheck.setSize(50,50);
		syncCheck.setUI(btUI);
		syncCheck.addActionListener(this);
		getButtonPane().add(syncCheck);
		
		syncAll = new JButton("Sync All");
		syncAll.setSize(50,50);
		syncAll.setUI(btUI);
		syncAll.addActionListener(this);
		getButtonPane().add(syncAll);
		
		file = new JTextField();
		file.setColumns(10);
		file.setBackground(Color.WHITE);
		getButtonPane().add(file);
		
		
		syncFile = new JButton("Sync File");
		syncFile.setSize(50,50);
		syncFile.setUI(btUI);
		syncFile.addActionListener(this);
		getButtonPane().add(syncFile);
		
		end = new JButton("End session.");
		end.setSize(50,50);
		end.setUI(btUI);
		end.addActionListener(this);
		getButtonPane().add(end);
		
		
		text = new JTextArea(20, 50);
		text.setEditable ( false ); // set textArea non-editable
		JScrollPane scroll = new JScrollPane ( text );
		scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		getFieldPane().add(scroll);
		getFieldPane().updateUI();

		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object src = arg0.getSource();
		if(src == syncCheck){
			client.syncCheck();
		}else if(src == syncAll){
			client.syncAll();
		}else if(src == syncFile){
			client.syncFile(file.getText());
		}else if(src == end){
			client.end();
		}
		
	}
	
	public void write(String s){
		text.setText(text.getText()+s+"\n");
	}
	
	public void resetText(){
		text.setText(client.toString()+"\n");
	}
	
	
}
