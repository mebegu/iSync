
import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class SWindow extends JFrame{


	private static final long serialVersionUID = 1L;
	private JMenuItem menuExit;
	private Point initialClick;
	private JMenuBar menubar;
	private JMenu file;
	private JPanel buttonPane;
	private JPanel fieldPane;
	private JSplitPane mSP;


	public SWindow(int x, int y) {
		prepareFrame(x, y);
	}
	public SWindow() {
		prepareFrame(250, 300);
	}

	private void prepareFrame(int x, int y) {
		createMenuBar();
		setUndecorated(true);
		setSize(x, y);
		setLocationRelativeTo(null);
		getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		
		fieldPane = new JPanel();
		fieldPane.setBackground(Color.WHITE);
		
		buttonPane = new JPanel();
		buttonPane.setBackground(Color.WHITE);
		
		mSP = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		mSP.setDividerSize(1);
		mSP.setResizeWeight(1);
		mSP.setDividerLocation(getHeight()-62);
		mSP.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));
		add(mSP);
		
		mSP.add(fieldPane);
		mSP.add(buttonPane);
		mSP.updateUI();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public JPanel getFieldPane(){
		return fieldPane;
	}
	
	public JPanel getButtonPane(){
		return buttonPane;
	}
	
	public JSplitPane getSplitter(){
		return mSP;
	}


	private void createMenuBar() {

		menubar = new JMenuBar();
		

		menubar.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				initialClick = e.getPoint();
				getComponentAt(initialClick);
			}
		});

		menubar.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {

				// get location of Window
				int thisX = getLocation().x;
				int thisY = getLocation().y;

				// Determine how much the mouse moved since the initial click
				int xMoved = (thisX + e.getX()) - (thisX + initialClick.x);
				int yMoved = (thisY + e.getY()) - (thisY + initialClick.y);

				// Move window to this position
				int X = thisX + xMoved;
				int Y = thisY + yMoved;
				setLocation(X, Y);
			}
		});

		file = new JMenu("Menu");

		menuExit = new JMenuItem("Exit");
		menuExit.setToolTipText("Exit application");
		menuExit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
		});

		file.add(menuExit);
		menubar.add(file);
		setJMenuBar(menubar);
	}
	
	public void addMenuBarItem(JMenuItem item, ActionListener listener){
		item.addActionListener(listener);
		file.add(item,0);
	}
	
	
	



}
