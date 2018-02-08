package server;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUIHelper {
	
	JButton button;
	JLabel jLabel;
	JTextField jTextField;
	
	public GUIHelper(){
		
	}
	
	public void createButton(JPanel jpanel, String label, int x, int y, int width, int height){
		button = new JButton(label);
		button.setBounds(x, y, width, height);
		jpanel.add(button);
	}
	
	public void createInput(JPanel jpanel, String label, int x, int y){
		jLabel = new JLabel(label);
		jLabel.setBounds(x, y, 150, 15);
		jpanel.add(jLabel);
		
		jTextField = new JTextField(15);
		jTextField.setBounds(x + 155, y, 150, 15);
		jTextField.add(jTextField);
	}

}
