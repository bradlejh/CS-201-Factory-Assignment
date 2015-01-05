import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;


public class FactoryMain extends JFrame{

	public HashMap<String, Coordinates> locations;
	public HashMap<String, Integer> tools;//stores information about tools when reading in from the .factory file
	public factoryDisplay factoryPanel;
	List<WorkArea> workAreas;//holds all the work areas in the factory
	public HashMap<String, factoryImage> Materials;
	List<Item> items;
	lockFinish lf;
	static boolean started;
	static boolean gather;
	public ArrayList<worker> workers;
	public FactoryMain()
	{
		super("Factory");
		setSize(800,600);
		setLocation(100,10);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FactoryMain.started=false;
		lf=new lockFinish();
		FactoryMain.gather=false;
		workers=new ArrayList<worker>();//holds all workers
		 items= Collections.synchronizedList(new ArrayList<Item>());//holds the strings for each of the recipe items
		ArrayList<JLabel> labels=new ArrayList<JLabel>();//holds all the jlabels in the task board
		 locations= new HashMap<String, Coordinates>();//holds the coordinates of all images so worker can move to them
		 tools=new HashMap<String, Integer>();
		 workAreas=Collections.synchronizedList(new ArrayList<WorkArea>());
		 Materials=new HashMap<String, factoryImage>();
		JMenuBar jmb= new JMenuBar();
		final JPanel taskItems=new JPanel();
		JMenuItem openFile = new JMenuItem("Open Folder...");
			class startFactory implements ActionListener{
			public void actionPerformed(ActionEvent ae)//Action Event listener for OpenFile menu item
			{
			
				FactoryMain.started=true;
				final JFileChooser fc= new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				File path=new File("C:/Users/Bradley/Documents/Brad/USC/CS 201/eclipse_projects/Bradlejh_CSCI201_Assignment5a");
				fc.setCurrentDirectory(path);
				int status = fc.showOpenDialog(FactoryMain.this);
				if (status== JFileChooser.APPROVE_OPTION)
				{                                               //loading information from the selected file
					File selectedFile=fc.getSelectedFile();
					String folderName=selectedFile.getAbsolutePath();//getting path of the folder
					System.out.println("Name is: " + folderName);
					File folder = new File(folderName);
					File[] listOfFiles = folder.listFiles();//all files in folder to an array
					int endIndex=0;
					int numIndex=0;
					for (int i = 0; i < listOfFiles.length; i++) {//iterate and look for all the recipe files
					  File file = listOfFiles[i];
					  if (file.isFile() && file.getName().endsWith(".rcp"))//going through all the recipe files
					  {
						  System.out.println(file.getName());
						  try{
						  FileReader fr = new FileReader(folderName+ "\\" +file.getName());
							BufferedReader br= new BufferedReader(fr);
							String line = br.readLine();
							for(int j=0; j<line.length();j++)
							{
								if(line.charAt(j)==']')
								{
									 endIndex=j;
								}
								if(j>0&&line.substring(j-1,j+1).equals(" x"))
								{
									numIndex=j+1;
								}
							}
							String itemName= line.substring(1,endIndex);
							String numItems=line.substring(numIndex,numIndex+1);
							System.out.println(itemName +numItems);
							int number=Integer.parseInt(numItems);
							LinkedList<recipeStep> instructions=new LinkedList<recipeStep>();
							while((line=br.readLine()) !=null)
							{
								recipeStep step=new recipeStep(line);
								instructions.add(step);
								
							}
							//LinkedList<recipeStep> dummyInstruction;
							for(int k=0; k<number;k++)
							{
								LinkedList<recipeStep> dummyInstruction=new LinkedList<recipeStep>();
								//dummyInstruction.add(instructions.peek());
								for(int m=0;m<instructions.size();m++)
								{
									recipeStep temp=instructions.poll();
									dummyInstruction.add(temp);
									instructions.add(temp);
									
								}
								Item dummy=new Item(itemName,dummyInstruction);
								items.add(dummy);
								taskItems.add(dummy.itemLabel);
								System.out.println("Adding " + itemName+ "size is: " + dummyInstruction.size());
								taskItems.updateUI();
							}
						
							
						
						
						  }
						  catch(FileNotFoundException fnfe)
						  {
							  System.out.println("file not found");
						  }
						  catch (IOException ioe)
						  {
							  System.out.println("ioexception");
						  }
					  } 
					  
					  if(file.isFile() && file.getName().endsWith(".factory"))//goign through the factory file
					  {
						  System.out.println(file.getName());
						  try{
						  FileReader fr = new FileReader(folderName+ "\\" +file.getName());
							BufferedReader br= new BufferedReader(fr);
							String line = br.readLine();//this gets number of workiers (to be continued)
							int workerStart=line.indexOf(':');
							int workerEnd=line.indexOf(']');
							int numWorkers=Integer.parseInt(line.substring(workerStart+1,workerEnd));
							for(int x=0;x<numWorkers;x++)
							{
								worker test=new worker(5,5, factoryPanel,workAreas,items,locations,Materials,lf);
								workers.add(test);
								factoryPanel.add(test.workerImage,1);
							}
							while((line=br.readLine()) !=null)
							{
								for(int j=0; j<line.length();j++)
								{
									if(line.charAt(j)==']')
									{
										 endIndex=j-2;
										 numIndex=j-1;
									}

								}
								String toolName= line.substring(1,endIndex);
								String numTools=line.substring(numIndex,numIndex+1);
								System.out.println(toolName +" " + numTools);
								int toolNumber=Integer.parseInt(numTools);
								tools.put(toolName,toolNumber);
							}
					  }
						  catch(FileNotFoundException fnfe)
						  {
							  System.out.println("file not found");
						  }
						  catch (IOException ioe)
						  {
							  System.out.println("ioexception");
						  }
					  }
					}
					//createTool("Screwdriver", 5, 15,50);
					int yPosition=100;
					for(HashMap.Entry<String, Integer> toolEntry : tools.entrySet())//iterating through all the tools and creating icons for them
					{
			            System.out.println(toolEntry.getKey() +" :: "+ toolEntry.getValue());
			            createTool(toolEntry.getKey(),toolEntry.getValue(),15,yPosition);
			            yPosition=yPosition+ 85;
					}
					//worker test=new worker(5,5, factoryPanel,workAreas,items,locations,Materials,lf);
					//factoryPanel.add(test.workerImage,1);
					for(int y=0;y<workers.size();y++)
					{
					workers.get(y).start();
					}
					/*for(HashMap.Entry<String, Coordinates> locationEntry : locations.entrySet())//iterating through all the tools and creating icons for them
					{
			            System.out.println(locationEntry.getKey() +" :: "+ locationEntry.getValue().xValue + " " + locationEntry.getValue().yValue);
					}*/
				}
			
					
			else if (status == JFileChooser.CANCEL_OPTION){
					
				}
				
			/*	for(int m=0; m<items.size(); m++)
				{
					labels.add(new JLabel(items.get(m)+"...Not Built"));//array of JLabels for the Task Board
				}
				for (int n=0; n<labels.size();n++)
				{
					taskItems.add(labels.get(n));//adding all the labels to the task board JPanel
				}*/
			//	FactoryMain.started=true;
			}
		}
			final startFactory sf=new startFactory();
			openFile.addActionListener(sf);
		jmb.add(openFile);
		setJMenuBar(jmb);
		JPanel taskBoard=new JPanel();
		Dimension d1= new Dimension(200,600);
		taskBoard.setPreferredSize(d1);
		taskBoard.setLayout(new BoxLayout(taskBoard, BoxLayout.Y_AXIS));
		JLabel tbName= new JLabel("Task Board");
		taskBoard.add(tbName);
		Border blackline = BorderFactory.createLineBorder(Color.black);
		
		Dimension d= new Dimension(0, 600);
		taskItems.setPreferredSize(d);
		taskItems.setBorder(blackline);
		taskItems.setLayout(new BoxLayout(taskItems, BoxLayout.Y_AXIS));
		taskItems.add(new JLabel("                                                                "));//to make task board look nicer
		JScrollPane jsp=new JScrollPane(taskItems);
		add(jsp);
		taskBoard.add(taskItems);
		
		add(taskBoard,BorderLayout.EAST);
		
// adding the main panel that holds all of the icons
		factoryPanel = new factoryDisplay();
		factoryPanel.setLayout(null);
		//wood panel
		ImageIcon woodIcon = new ImageIcon("C:/Users/Bradley/Documents/Brad/USC/CS 201/eclipse_projects/Bradlejh_CSCI201_Assignment5a/Images/wood.png");
		factoryImage woodImage=new factoryImage(woodIcon);
		woodImage.amount=999;
		woodImage.material=true;
		JLabel woodText= new JLabel("Wood");
		JPanel wood=createComponent(woodText, woodImage);
		Dimension woodDimension=wood.getPreferredSize();
		wood.setBounds(150,5,woodDimension.width, woodDimension.height);
		locations.put("Wood",new Coordinates(150,5));//storing the coordinates of the wood icon
		factoryPanel.add(wood,0);
		Materials.put("Wood",woodImage);
		
		//metal panel
		ImageIcon metalIcon = new ImageIcon("C:/Users/Bradley/Documents/Brad/USC/CS 201/eclipse_projects/Bradlejh_CSCI201_Assignment5a/Images/metal.png");
		factoryImage metalImage=new factoryImage(metalIcon);
		metalImage.amount=999;
		metalImage.material=true;
		JLabel metalText= new JLabel("Metal");
		JPanel metal=createComponent(metalText, metalImage);
		Dimension metalDimension=metal.getPreferredSize();
		metal.setBounds(300,5,metalDimension.width, metalDimension.height);
		locations.put("Metal",new Coordinates(300,5));//storing the coordinates of the wood icon
		factoryPanel.add(metal,0);
		Materials.put("Metal",metalImage);
		
		//plastic panel
		ImageIcon plasticIcon = new ImageIcon("C:/Users/Bradley/Documents/Brad/USC/CS 201/eclipse_projects/Bradlejh_CSCI201_Assignment5a/Images/plastic.png");
		factoryImage plasticImage=new factoryImage(plasticIcon);
		plasticImage.amount=999;
		plasticImage.material=true;
		JLabel plasticText= new JLabel("Plastic");
		JPanel plastic=createComponent(plasticText, plasticImage);
		Dimension plasticDimension=plastic.getPreferredSize();
		plastic.setBounds(450,5,plasticDimension.width, plasticDimension.height);
		locations.put("Plastic",new Coordinates(450,5));//storing the coordinates of the wood icon
		factoryPanel.add(plastic,0);
		Materials.put("Plastic",plasticImage);
		
		//ADDING WORKAREAS
		//Anvils
		WorkArea Anvil1=new WorkArea("anvil",165,120);
		workAreas.add(Anvil1);
		factoryPanel.add(Anvil1.waLabel,0);
	
		WorkArea Anvil2=new WorkArea("anvil",240,120);
		workAreas.add(Anvil2);
		factoryPanel.add(Anvil2.waLabel);
		JLabel anvilName=new JLabel("Anvils");
		Dimension anvilDimension=anvilName.getPreferredSize();
		anvilName.setBounds(210,190,anvilDimension.width, anvilDimension.height);
		factoryPanel.add(anvilName,0);
		//WORKBENCHES
		WorkArea WorkBench1=new WorkArea("workbench",315,120);
		workAreas.add(WorkBench1);
		factoryPanel.add(WorkBench1.waLabel,0);
		WorkArea WorkBench2=new WorkArea("workbench",390,120);
		workAreas.add(WorkBench2);
		factoryPanel.add(WorkBench2.waLabel,0);
		WorkArea WorkBench3=new WorkArea("workbench",465,120);
		workAreas.add(WorkBench3);
		factoryPanel.add(WorkBench3.waLabel,0);
		JLabel wbName=new JLabel("Workbenches");
		Dimension wbDimension=wbName.getPreferredSize();
		wbName.setBounds(380,190,wbDimension.width, wbDimension.height);
		factoryPanel.add(wbName,0);
		//Furnaces
		WorkArea Furnace1=new WorkArea("furnace",165,260);
		workAreas.add(Furnace1);
		factoryPanel.add(Furnace1.waLabel,0);
		WorkArea Furnace2=new WorkArea("furnace",240,260);
		workAreas.add(Furnace2);
		factoryPanel.add(Furnace2.waLabel,0);
		JLabel furnaceName=new JLabel("Furnaces");
		Dimension furnaceDimension=furnaceName.getPreferredSize();
		furnaceName.setBounds(210,330,furnaceDimension.width, furnaceDimension.height);
		factoryPanel.add(furnaceName,0);
		//Table Saws
		WorkArea ts1=new WorkArea("tablesaw",315,260);
		workAreas.add(ts1);
		factoryPanel.add(ts1.waLabel,0);
		WorkArea ts2=new WorkArea("tablesaw",390,260);
		workAreas.add(ts2);
		factoryPanel.add(ts2.waLabel,0);
		WorkArea ts3=new WorkArea("tablesaw",465,260);
		workAreas.add(ts3);
		factoryPanel.add(ts3.waLabel,0);
		JLabel tsName=new JLabel("Table Saws");
		Dimension tsDimension=tsName.getPreferredSize();
		tsName.setBounds(380,330,tsDimension.width, tsDimension.height);
		factoryPanel.add(tsName,0);
		//Painting stations
		WorkArea ps1=new WorkArea("paintingstation",165,400);
		workAreas.add(ps1);
		factoryPanel.add(ps1.waLabel,0);
		WorkArea ps2=new WorkArea("paintingstation",240,400);
		workAreas.add(ps2);
		factoryPanel.add(ps2.waLabel,0);
		WorkArea ps3=new WorkArea("paintingstation",315,400);
		workAreas.add(ps3);
		factoryPanel.add(ps3.waLabel,0);
		WorkArea ps4=new WorkArea("paintingstation",390,400);
		workAreas.add(ps4);
		factoryPanel.add(ps4.waLabel,0);
		JLabel psName=new JLabel("Painting Stations");
		Dimension psDimension=psName.getPreferredSize();
		psName.setBounds(260,470,psDimension.width, psDimension.height);
		factoryPanel.add(psName,0);
		//Press
		WorkArea press=new WorkArea("press",465,400);
		workAreas.add(press);
		factoryPanel.add(press.waLabel,0);
		JLabel pressName=new JLabel("Press");
		Dimension pressDimension=pressName.getPreferredSize();
		pressName.setBounds(475,470,pressDimension.width, pressDimension.height);
		factoryPanel.add(pressName,0);
		
		/*JLabel testWorker=new JLabel(new ImageIcon("C:/Users/Bradley/Documents/Brad/USC/CS 201/eclipse_projects/Bradlejh_CSCI201_Assignment5a/Images/worker.png"));
		Dimension workerDimension=testWorker.getPreferredSize();
		testWorker.setBounds(465,420, workerDimension.width, workerDimension.height);
		factoryPanel.add(testWorker,1);*/
		
		add(factoryPanel,BorderLayout.CENTER);
		
		
		setVisible(true);
	}
	
	public JPanel createComponent(JLabel text, factoryImage image)
	{
		JPanel Component= new JPanel();
		Component.setLayout(new BoxLayout(Component, BoxLayout.Y_AXIS));
		Component.add(text);
		Component.add(image);
		return Component;
	}
	
	public void createTool(String tool,int toolAmount, int xPos, int yPos){
		//creating a tool
		String toolFile=tool.toLowerCase();
				if(toolFile.equals("paintbrushes"))
				{
					toolFile=toolFile.substring(0,toolFile.length()-2);
				}
				if(toolFile.equals("screwdrivers") || toolFile.equals("hammers"))
				{
					toolFile=toolFile.substring(0,toolFile.length()-1);
				}
				
				ImageIcon toolIcon = new ImageIcon("C:/Users/Bradley/Documents/Brad/USC/CS 201/eclipse_projects/Bradlejh_CSCI201_Assignment5a/Images/"+toolFile+".png");
				factoryImage toolImage=new factoryImage(toolIcon);
				toolImage.amount=toolAmount;
				toolImage.max=toolAmount;
				toolImage.material=false;
				JLabel toolText= new JLabel(tool);
				JPanel toolPanel=createComponent(toolText, toolImage);
				Dimension toolDimension=toolPanel.getPreferredSize();
				toolPanel.setBounds(xPos,yPos,toolDimension.width, toolDimension.height);
				String input=tool.toLowerCase();
				input=input.substring(0,input.length()-1);
				if(input.equals("paintbrushe"))
				{
					input=input.substring(0,input.length()-1);
				}
				System.out.println("putting in location the tool: " + input);
				locations.put(input,new Coordinates(xPos,yPos));//storing the coordinates of the wood icon
				factoryPanel.add(toolPanel,0);
				Materials.put(input,toolImage);
	}
	
	public static void main(String [] args)
	{
		FactoryMain fm =new FactoryMain();
		boolean once=false;
		 long startTime=0;
		while(true)
		{
			if(FactoryMain.started==true&&once==false)
			{
				System.out.println("started timing");
				 startTime = System.currentTimeMillis();
				once=true;
			}
			
			
			if(fm.lf.getFinished()==true)
			{
				final long endTime=System.currentTimeMillis();
				final long totalTime=(endTime-startTime)/1000;
				int selection = JOptionPane.showConfirmDialog(fm, "Factory Has Finished. It took " + totalTime + "seconds, press 'Yes' to load another file, press 'No' to quit.", "Confirmation", JOptionPane.YES_NO_OPTION);
				switch (selection) {
				 case JOptionPane.YES_OPTION: // case JOptionPane.OK_OPTION is the same
				 System.out.println("Yes");
				
				 break;
				 case JOptionPane.NO_OPTION:
				 System.out.println("No");
				 fm.setVisible(false);
				 fm.dispose();
				 fm.lf.setFalse();
				 System.exit(0);
				 break;
				 case JOptionPane.CANCEL_OPTION:
				 System.out.println("Cancel");
				 break;
				 case JOptionPane.CLOSED_OPTION:
				 System.out.println("Closed");
				 break;
			 }

			}
			
		}
		
	}
	
	
}



class factoryImage extends JLabel{//for creating the materials and tools gui
	public String text;
	public int amount;
	public boolean material;//different text schemes for materials vs tools
	public int max;//not used if material
	public factoryImage(ImageIcon ii)
	{
		super(ii);
	}
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		if(material==true)
		g.drawString(Integer.toString(amount), 15, 35);//if material
		else
			g.drawString(Integer.toString(amount)+"/"+max,15,35);//if tool
		}
	}
	

class factoryDisplay extends JPanel{
	worker w;
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		//insert code to paint all workers from a worker array
	}
	public void addWorker(worker w)
	{
		this.w=w;
	}
	//addWorkers(ArrayList<worker>)
	}

class worker extends Thread
{
	public LinkedList<recipeStep> orders;
	JLabel workerImage;
	int xPos;
	int yPos;
	factoryDisplay fd;
	boolean inProgress;
	List<WorkArea>WorkAreas;
	List<Item> items;
	HashMap<String, Coordinates> locations;
	HashMap<String, factoryImage> Materials;
	lockFinish lf;
	public worker(int xPos, int yPos, factoryDisplay fd ,List<WorkArea>WorkAreas, List<Item> items,HashMap<String, Coordinates> locations,HashMap<String, factoryImage> Materials,lockFinish lf)
	{
		this.xPos=xPos;
		this.yPos=yPos;
		this.fd=fd;
		workerImage=new JLabel(new ImageIcon("C:/Users/Bradley/Documents/Brad/USC/CS 201/eclipse_projects/Bradlejh_CSCI201_Assignment5a/Images/worker.png"));
		inProgress=false;
		this.WorkAreas=WorkAreas;
		this.items=items;
		this.locations=locations;
		this.Materials=Materials;
		this.lf=lf;
	}
	
	public void run()
	{
		int xDest=530;
		int yDest=70;
		boolean firstStep=true;
		boolean gatheredTools=false;
		boolean returnTools=false;
		boolean waiting=false;
		boolean goUp=false;
		boolean goDown=false;
		boolean finished=false;
		boolean done=false;
		boolean gathering=false;
		int projNum=0;
		int waNum=0;
		WorkArea holder=null;
		recipeStep rs=null;
		int toolIndex=0;
		while(true)
		{
		//	System.out.println("ydest is: " + yDest);
			done=true;
			for(int n=0;n<items.size();n++)//checking if done with all the projects
			{
				if(items.get(n).finished==false)
				{
					done=false;
				}
			}
			if(done==true)
			{
				workerImage.setVisible(false);
				lf.setTrue();
				System.out.println("set lf true");
				break;
			}
			try {
				 sleep((long)(5));
				 } catch (InterruptedException ie) {
				 System.out.println("interrupted");
				 return;
				 }
			if(firstStep==false)//if not navigating to the taskboard from initial starting point
			{
				if(orders.size()>0)
				{
					System.out.println("peeking" + " " + orders.size());
				rs=orders.peek();
				}
				else
				{
					finished=true;
				}
				if(finished==false)
				{	
				if(rs.useTools==false)//gathering materials
				{
					System.out.println("should not be in this twice?");
					yDest=70;
					xDest=locations.get(rs.material).xValue;
				}
				else if(rs.useTools==true)//using a tool/workarea
				{
					if(rs.recipeTools.size()==0&&gatheredTools==false&&returnTools==false)//no tools just go to work area
					{
						System.out.println("no tools needed in this step");
						
						int dummyCount=0;
						int counter=0;
						Iterator<WorkArea> iterator=WorkAreas.iterator();
						while(iterator.hasNext())
						{
							WorkArea dummy=iterator.next();
							if(dummy.type.equals(rs.location))
							{
								if(dummy.inUse==false&&dummyCount==0)
								{
									dummy.inUse=true;
									waNum=counter;
									holder=dummy;
									xDest=dummy.xPos;
									yDest=dummy.yPos-50;
									dummyCount++;
									gatheredTools=true;
								}
							}
							counter++;
						}
					}
					else if(rs.recipeTools.size() >0&&gatheredTools==false&&returnTools==false)//uses tools
					{
						if(FactoryMain.gather==false|| gathering==true)//allows only one worker to get tools at time, prevents deadlock
						{
						//System.out.println("using da toolds yo");
							FactoryMain.gather=true;
							gathering=true;
						xDest=75;
						yDest=locations.get(rs.recipeTools.get(toolIndex).name).yValue+20;
						if(xPos==xDest&&yPos==yDest)
						{
							System.out.println("at a tool");
							if((Materials.get(rs.recipeTools.get(toolIndex).name).amount-orders.peek().recipeTools.get(toolIndex).amount)>=0)
							{
							Materials.get(rs.recipeTools.get(toolIndex).name).amount=Materials.get(rs.recipeTools.get(toolIndex).name).amount-orders.peek().recipeTools.get(toolIndex).amount;//receiving the amount of materials
							Materials.get(rs.recipeTools.get(toolIndex).name).repaint();
							
							try {
								 sleep((long)(1000));
								 } catch (InterruptedException ie) {
								 System.out.println("interrupted");
								 return;
								 }
							if((rs.recipeTools.size()-(toolIndex+1))>0)//more than one tool
							{
								toolIndex++;
							}
							else//else gathered all tools, now head to location
							{
								gatheredTools=true;
								toolIndex=0;
								int dummyCount=0;
								int counter=0;
								Iterator<WorkArea> iterator=WorkAreas.iterator();
								while(iterator.hasNext())
								{
									WorkArea dummy=iterator.next();
									if(dummy.type.equals(rs.location))
									{
										if(dummy.inUse==false&&dummyCount==0)
										{
											dummy.inUse=true;
											waNum=counter;
											holder=dummy;
											xDest=dummy.xPos;
											yDest=dummy.yPos-50;
											dummyCount++;
										}
									}
									counter++;
								}
							/*	for(int i=0; i<WorkAreas.size();i++)
								{
									if(WorkAreas.get(i).type.equals(rs.location))
									{
										//System.out.println("i found a workarea!");
										if(WorkAreas.get(i).inUse==false&&dummyCount==0)
										{
											waNum=i;
											xDest=WorkAreas.get(i).xPos;
											yDest=WorkAreas.get(i).yPos-50;
											dummyCount++;
										}
									}
								}*/
								//toolIndex=0;
								//orders.poll();
								FactoryMain.gather=false;
								gathering=false;
							}
							}
						}
					}
					}
					else if(gatheredTools==true&&returnTools==false&&waiting==false )
					{
						//System.out.println("arrived at the workarea");
						if(xPos==xDest&&yPos==yDest)
						{
							yDest=yDest+70;
							goDown=true;
							waiting=true;
						}
					}
					else if(gatheredTools==true&&returnTools==true&&waiting==false&&rs.recipeTools.size()==0)
					{
						System.out.println("returning tools with size0");
						orders.poll();
						gatheredTools=false;
						returnTools=false;
					}
					else if(gatheredTools==true&&returnTools==true&&waiting==false&&rs.recipeTools.size() >0)//returning tools
					{
						xDest=75;
						yDest=locations.get(rs.recipeTools.get(toolIndex).name).yValue+20;
						if(xPos==xDest&&yPos==yDest)
						{
							System.out.println("returning a tool");
							Materials.get(rs.recipeTools.get(toolIndex).name).amount=Materials.get(rs.recipeTools.get(toolIndex).name).amount+orders.peek().recipeTools.get(toolIndex).amount;//receiving the amount of materials
							Materials.get(rs.recipeTools.get(toolIndex).name).repaint();
							try {
								 sleep((long)(1000));
								 } catch (InterruptedException ie) {
								 System.out.println("interrupted");
								 return;
								 }
							if((rs.recipeTools.size()-(toolIndex+1))>0)//more than one tool to return
							{
								toolIndex++;
							}
							else
							{
								toolIndex=0;
								System.out.println("finished this step");
								orders.poll();
								gatheredTools=false;
								returnTools=false;
							}
						}
					}
			}
			}
				if(finished==true)//heading back to taskboard
				{
					xDest=530;
					yDest=70;
					if(xPos==xDest&&yPos==yDest)
					{
						items.get(projNum).itemLabel.setText(items.get(projNum).name+ "...Finished");
						items.get(projNum).finished=true;
						items.get(projNum).itemLabel.updateUI();
						firstStep=true;
						finished=false;
					}
				}
			}
				if(waiting==true)//this block is just for the worker to move down and up
				{
					//System.out.println("in waiting");
					if(goDown==true)
					{
						if(yPos!=yDest)
						{
							yPos=yPos+1;
						}
						else
						{
							System.out.println("ireached the bottom "+ yDest);
							WorkAreas.get(waNum).inUse=true;
							for(int j=0;j<rs.time;j++)
							{
								WorkAreas.get(waNum).waLabel.setText(rs.time-j+"s");
								WorkAreas.get(waNum).waLabel.setForeground(Color.RED);
								WorkAreas.get(waNum).waLabel.updateUI();
								
								try {
									 sleep((long)(1000));
									 } catch (InterruptedException ie) {
									 System.out.println("interrupted");
									 return;
									 }
								
								
							}
							WorkAreas.get(waNum).waLabel.setText("Open");
							WorkAreas.get(waNum).waLabel.setForeground(Color.GREEN);
							WorkAreas.get(waNum).waLabel.updateUI();
							WorkAreas.get(waNum).inUse=false;
							yDest=yDest-70;
							System.out.println(yDest);
							goDown=false;
							goUp=true;
						}
					}
					else if(goUp==true)
					{
						System.out.println("how about here " + yPos + " " + yDest);
						if(yPos!=yDest)
						{
							System.out.println("do i ever get here");
							yPos=yPos-1;
						}
						else
						{
							goUp=false;
							returnTools=true;
							waiting=false;
						}
					}
					
				}/*if(yPos<yDest&&goUP==false)
					yPos=yPos+1;
					else if(yPos==yDest)
					{
						goUP=true;
						yDest=yDest-70;
						yPos=yPos-1;
					}
					if(goUP==true)
					{
						System.out.println("in goUP");
						if(yPos>yDest)
						yPos=yPos-1;
						else if(yPos==yDest)
						{
							goUP=false;
							waiting=false;
							returnTools=true;
						}
					}
					
				}*/
				else if(yPos==yDest&&xPos==xDest&&firstStep==true)//if arrived at taskboard
				{
					int counter=0;
					for(int i=0;i<items.size(); i++)
					{
						if(items.get(i).started==false && counter==0)
						{
							projNum=i;
							orders=items.get(i).instructions;
							System.out.println("instructions size is: " + items.get(i).instructions.size());
							System.out.println(items.get(i).instructions.size());
							System.out.println(orders.size());
							System.out.println(orders.peek().useTools);
							items.get(i).started=true;
							items.get(i).itemLabel.setText(items.get(i).name+ "...In Progress");
							items.get(i).itemLabel.updateUI();
							System.out.println("i am here");
							counter++;
							firstStep=false;
							try {
								 sleep((long)(1000));
								 } catch (InterruptedException ie) {
								 System.out.println("interrupted");
								 return;
								 }
						}
					}
				}
				else if(yPos==yDest&&xPos==xDest&&firstStep==false)//arrived at materials destination
				{
					if(rs.useTools==false)//gathering materials
					{
						System.out.println("Gathering material");
						Materials.get(rs.material).amount=Materials.get(rs.material).amount-orders.peek().amount;
						Materials.get(rs.material).repaint();
						orders.poll();
						try {
							 sleep((long)(1000));
							 } catch (InterruptedException ie) {
							 System.out.println("interrupted");
							 return;
							 }
					}
				
				}
				
			if(yPos!=yDest&&waiting==false)//navigates the worker to the "home" column first
			{
				if(xPos!=100)
				{
					if(xPos>100)
						xPos=xPos-1;
					else
						xPos=xPos+1;
				}
				else if(yPos<yDest)
				{
					yPos=yPos+1;
				}
				else if (yPos>yDest)
				{
					yPos=yPos-1;
				}
			}
			else if(yPos==yDest&&waiting==false)
			{
				if(xPos>xDest)
				xPos=xPos-1;
				else if (xPos<xDest)
				xPos=xPos+1;
			}
		
			Dimension workerDimension=workerImage.getPreferredSize();
			workerImage.setBounds(xPos,yPos, workerDimension.width, workerDimension.height);
			workerImage.updateUI();
			//fd.repaint();
		}
	}
}
