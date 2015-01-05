import java.util.ArrayList;
import java.util.HashMap;


public class recipeStep {
	public String material;
	public int amount;
	public ArrayList<holder> recipeTools;
	public String location;
	public int time;
	public boolean useTools;

	public recipeStep(String instruction)
	{
		recipeTools=new ArrayList<holder>();
		useTools=false;
		int endString=instruction.indexOf("]");
		if(instruction.contains("Use"))//step uses tools, not materials/ or only using location
		{
			material="none";
			amount=0;
			System.out.println("checkpoint1");
			if (instruction.substring(6,7).equals("x"))//if need to use tools
			{
				useTools=true;
				System.out.println("checkpoint2");
				String toolName=instruction.substring(8,instruction.indexOf(" ",8));//first tool
				toolName=toolName.toLowerCase();
				if(toolName.equals("Screwdrivers"))
				{
					toolName="screwdriver";
				}
				if(toolName.equals("Hammer"))
				{
					toolName="hammer";
				}
				if(toolName.charAt(toolName.length()-1)=='s')
				{
					toolName=toolName.substring(0,toolName.length()-1);
				}
				int toolAmount=Integer.parseInt(instruction.substring(5,6));
				System.out.println("first toolname and amount are:" + toolName + toolAmount);
				recipeTools.add(new holder(toolName,toolAmount));
				
				while(instruction.contains("and"))//multiple tools
				{
					
					System.out.println("in and loop");
					endString=instruction.indexOf("]");
					int endIndex=instruction.indexOf(" and");
					String toolName2=instruction.substring(endIndex+8,instruction.indexOf(" ",endIndex+8));
					int toolAmount2=Integer.parseInt(instruction.substring(endIndex+5,endIndex+6));
					toolName2=toolName2.toLowerCase();
					if(toolName2.equals("Screwdrivers"))
					{
						toolName2="screwdriver";
					}
					if(toolName2.equals("Hammer"))
					{
						toolName2="hammer";
					}
					
					if(toolName2.charAt(toolName2.length()-1)=='s')
					{
						toolName2=toolName2.substring(0,toolName.length()-1);
					}
					if(toolName2.equals("screw"))
					{
						toolName2="screwdriver";
					}
					recipeTools.add(new holder(toolName2,toolAmount2));
					System.out.println("first toolname2 and amount are:" + toolName2 + toolAmount2);
					instruction=instruction.substring(0,endIndex)+instruction.substring(endIndex+4,endString+1);//omitting the "and"
				}
				endString=instruction.indexOf("]");
				if(instruction.charAt(endString-3)==' ')
				{
				time=Integer.parseInt(instruction.substring(endString-2,endString-1));
				}
				else
				{
					time=Integer.parseInt(instruction.substring(endString-3,endString-1));
				}
				int toolBIndex=instruction.indexOf("at")+3;
				int toolEIndex=instruction.indexOf(" for");
				location=instruction.substring(toolBIndex,toolEIndex).toLowerCase();
				location=location.replaceAll("\\s+","");
				location=location.toLowerCase();
				System.out.println("recipe step location is: " + location);
			}
			else//only using a location, dont' need tools (example: "Use Saw for 2s")
			{
				useTools=true;
				System.out.println("no tools needed");
				int endIndex=instruction.indexOf(" for");
				location=instruction.substring(5,endIndex);
				location=location.replaceAll("\\s+","");
				location=location.toLowerCase();
				time=Integer.parseInt(instruction.substring(endString-2,endString-1));
				System.out.println("locatino with no tools is: " + location);
			}
			
		}
		else//step uses materials
		{
			System.out.println("in materials");
			location="none";
			time=0;
			amount=Integer.parseInt(instruction.substring(endString-1,endString));
			material=instruction.substring(1,endString-2);
			System.out.println("instruction for materials: " + material + " " + amount);
		}
			
			
	}
}

class holder{
	public String name;
	public int amount;
	public holder(String name, int amount)
	{
		this.name=name;
		this.amount=amount;
	}
}


