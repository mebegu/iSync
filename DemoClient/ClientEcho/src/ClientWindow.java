

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
public class ClientWindow extends SWindow implements ActionListener {

	private JButton syncCheck;
	private JButton syncAll;
	private JButton syncFile;
	private JComboBox<String> files;
	private JTextArea text;
	private Client client;
	private UIButton btUI;
	
	public ClientWindow (){
		client = new Client(this, 4445);
		prepareWindow();
		setSize(600,400);
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
		
		files = new JComboBox<String>();
		files.setSize(50,50);
		files.setBackground(Color.WHITE);
		files.insertItemAt("example.png", 0);
		files.insertItemAt("hw.pdf", 1);
		getButtonPane().add(files);
		
		
		syncFile = new JButton("Sync File");
		syncFile.setSize(50,50);
		syncFile.setUI(btUI);
		syncFile.addActionListener(this);
		getButtonPane().add(syncFile);
		
		
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
			System.out.println("Sync check is pressed.");
		}else if(src == syncAll){
			System.out.println("Sync all is pressed.");
		}else if(src == syncFile){
			System.out.println("The file "+ files.getSelectedItem()+" is selected to sync.");
		}
		
	}
	
	public void write(String[] s){
		String areaText = "";
		for(int i=0; i<s.length; i++){
			areaText += (s[i]+"\n");
		}
		text.setText(areaText);
	}
	
	
}
