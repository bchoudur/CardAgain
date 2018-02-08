package gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;


// This class does nothing within the acutal project, just used for experimental code
public class Experiement1 {
	
	public static void main(String[] args)
	{
		JFrame f= new JFrame("Example");
		
		final JTextField tf = new JTextField();
		tf.setBounds(50, 50, 150, 20);
		
		JButton b = new JButton("click");
		
		// This sets the position of the button to where it belongs on the Jframe
		b.setBounds(50,100,95,30); // x axis, y axis, width, height
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				tf.setText("Welcome to Javatpoint");
			}
		});
		
		f.add(b); // adding the button to the JFrame
		f.add(tf);
		f.setSize(400, 400); // Jframe f has 400 width and 500 height
		f.setLayout(null); // using no layout managers
		f.setVisible(true); // making the frame visible
	}

}