import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class WorkArea {
public String type;
public int xPos;
public int yPos;
public boolean inUse;
public JLabel waLabel;
public WorkArea(String type, int xPos, int yPos)//work area creates the GUI as well as adding it to a container of workareas for easy access for the worker threads
{
	this.type=type;
	this.xPos=xPos;
	this.yPos=yPos;
	inUse=false;
	ImageIcon ii=new ImageIcon("C:/Users/Bradley/Documents/Brad/USC/CS 201/eclipse_projects/Bradlejh_CSCI201_Assignment5a/Images/"+ type + ".png");
	if(this.type.equals("tablesaw"))
	{
		this.type="saw";
		
	}
	waLabel=new JLabel("Open",ii,SwingConstants.CENTER);
	waLabel.setForeground(Color.GREEN);
	waLabel.setHorizontalTextPosition(SwingConstants.CENTER);
	waLabel.setVerticalTextPosition(SwingConstants.TOP);
	Dimension waDimension=waLabel.getPreferredSize();
	waLabel.setBounds(xPos,yPos,waDimension.width, waDimension.height);
	//factoryPanel.add(waLabel);
}

}
