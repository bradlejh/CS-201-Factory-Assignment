import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JLabel;


public class Item {
	public JLabel itemLabel;
	public String name;
	public LinkedList<recipeStep> instructions;
	boolean started;
	boolean finished;
	
	public Item(String name,LinkedList<recipeStep> instructions)
	{
		this.name=name;
		itemLabel=new JLabel(name + "...Not Built");
		this.instructions=instructions;
		started=false;
		finished=false;
	}
}
