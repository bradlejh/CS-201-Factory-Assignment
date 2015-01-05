
public class lockFinish {

		 private static boolean finished=false;
		 public void setTrue()
		 {
			 finished=true;
		 }
		 public void setFalse()
		 {
			 finished=false;
		 }
		 public static synchronized boolean getFinished() {
		        return finished;
		    }
	 
}
