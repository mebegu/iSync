

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
public class ServerWindow extends SWindow implements ActionListener {

	private JButton serverButton;
	private JTextArea text;
	private Server server;
	private UIButton btUI;
	
	public ServerWindow (){
		server = new Server(this);
		prepareWindow();
		setSize(600,400);
	}

	private void prepareWindow() {
		btUI = new UIButton();
		btUI.rc = Color.GREEN;
		btUI.fc = Color.WHITE;
		
		serverButton = new JButton("Activate");
		serverButton.setSize(50,50);
		serverButton.setUI(btUI);
		serverButton.addActionListener(this);
		getButtonPane().add(serverButton);
		
		text = new JTextArea(20, 50);
		text.setEditable ( false ); // set textArea non-editable
		JScrollPane scroll = new JScrollPane ( text );
		scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		getFieldPane().add(scroll);
		getFieldPane().updateUI();

		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(server.getStatus()){
			server.shutdown();
			btUI.rc = Color.GREEN;
			serverButton.setText("Activate");
		}else{
			server.activate();
			btUI.rc = Color.RED;
			serverButton.setText("Shutdown");
		}
		
	}
	
	public void write(String s){
		text.setText(text.getText()+s+"\n");
	}
	
	
}
