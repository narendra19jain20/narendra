/**
 * 
 */
package firstframe;

/**
 * @author 0000100363
 *
 */
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
	public class frame extends JFrame implements ActionListener {
	   JFrame Frame1 = new JFrame("Subtitle Fetch");
	   JPanel j2 = new JPanel();
	   JButton b1 = new JButton ("Download_Subs");
	   JTextField t1 = new JTextField(40);
	   JTextArea myText = new JTextArea("My text");
   JPanel j1 = new JPanel (new FlowLayout());
	   JLabel l1 = new JLabel("Search Subtitle: ");
	   JPanel j3= new JPanel();
	public frame()
	    {
		j1.setLayout(new FlowLayout());
		j1.add(l1);
	    j1.add(t1);
	    j2.setLayout(new FlowLayout());
	    j2.add(b1);
	    j3.setLayout(new BorderLayout());
	    j3.add(j1, BorderLayout.NORTH);
	    j3.add(myText, BorderLayout.CENTER);
	    j3.add(j2, BorderLayout.SOUTH);
	    Frame1.setSize(600,550);
	 //   Frame1.setLayout(new FlowLayout());
	    Frame1.add(j3);
	    
		b1.addActionListener(this);
		t1.addActionListener(this);
		//myCheckBox.addActionListener(this);
	    Frame1.setVisible(true);
	    Frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  }
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == b1){
			myText.setText("A button click");
			String query = t1.getText();
			TestGoogleSea subs= new TestGoogleSea();
			try {
				subs.Fetch(query);
			} catch (Throwable e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		//else if (e.getSource() == myCheckBox)
		//	myText.setText("The checkbox state changed to " + myCheckBox.isSelected());
		else
			myText.setText("E ...?");
	}
}