/*Programmer: Shannon Riegle
 *FINAL PROJECT FOR FIRST SEMESTER AP COMPUTER SCIENCE
 *Main Driver for Riddler Program
 */
import java.util.*; //Import Libraries
import java.io.*;

import java.awt.Robot; //Libraries used to time Input (https://stackoverflow.com/questions/12803151/how-to-interrupt-a-scanner-nextline-call)
import java.awt.event.KeyEvent;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class RiddlerMainProgram
{	
	public static void main(String[] args) throws IOException, InterruptedException, IllegalMonitorStateException
	{
		String randomriddle=randomriddle(); //Skips to randomriddle() method to choose the riddle Riddler asks
		
		/*RIDDLE AND ANSWER DISTINGUISHER
		 *This code parses the line from the .dat file to seperate the riddle from its answer.
		 */
		int comma=randomriddle.indexOf(",");
		String randomanswer=randomriddle.substring(comma+1);
		randomriddle=randomriddle.substring(0,comma);
		System.out.println("RIDDLER: "+randomriddle);
		
		/*USER INPUTS ANSWER
		 *Taken from Stack Overflow solution at https://stackoverflow.com/questions/12803151/how-to-interrupt-a-scanner-nextline-call. Provides time limit for user input
		*/
		new TimeoutThread().start();
		new ReaderThread().start();		
		try //Forces main Method to wait until both Timeout and Reader die
		{
			RiddlerMainProgram.class.wait();
		}
		catch(IllegalMonitorStateException e)
		{
		}
		String inputanswer=answer(); //Reads answer file to learn user input
		
		/*TEST FOR CORRECT ANSWER
		 *This section tests if the Player answered correctly or incorrectly.
		 */
		Thread.sleep(3000); //Three seconds of drumroll	
		if(inputanswer.equals(randomanswer)) //Correct Answer
		{
			System.out.println("RIDDLER: YOU GUESSED IT.");
		}
		else //Incorrect Answer
		{
			System.out.println("RIDDLER: INCORRECT.");
			System.out.println("     THE ANSWER WAS "+randomanswer+", OBVIOUSLY.");
		}
	} //Close main Method
	
	public static String randomriddle()
	{
		/*NUMBER OF RIDDLES TESTER
		 *This section of code determines how many riddles Riddler knows by using a while loop until the .dat file becomes null.
		 */
		int number=0;
		String placeholder="";
		try //Reads until null
		{
			File length=new File("riddles.dat");
			BufferedReader reader=new BufferedReader(new FileReader(length));
			placeholder=reader.readLine();
			while(placeholder!=null)
			{
				number=number+1; //Adds number for each riddle; end result is number of riddles
				placeholder=reader.readLine();
			}
			reader.close();
		}
		catch(IOException e)
		{
			System.err.println("ERROR 404: FILE IS NONEXISTANT");
		}
		
		/*DETERMINE RIDDLE SELECTED
		 *Using the number of riddles determined in the above code, a random number generator determines which riddle is used. The while loop is then repeated, this time until on this number.
		 */
		int random=0;
		int n=0;
		String riddle="";
		Random numgen = new Random(); //Random Number Block
		random=numgen.nextInt(number)+1; //Roll and Display Dice1
		try
		{
			File selectedriddle=new File("riddles.dat");
			BufferedReader reader2=new BufferedReader(new FileReader(selectedriddle));
			while(n<random) //Keeps changing riddle until at desired riddle
			{
				n=n+1;
				riddle=reader2.readLine();
			}
			reader2.close();
		}
		catch(IOException e)
		{
			System.err.println("ERROR 404: FILE IS NONEXISTANT");
		}
		return(riddle);
	}
	
	public static String answer()
	{
		String answer="";
		try //Takes answer from answer.dat into program
		{
			File selectedriddle=new File("answer.dat");
			BufferedReader reader3=new BufferedReader(new FileReader(selectedriddle));
			answer=reader3.readLine();
			reader3.close();
		}
		catch(IOException e)
		{
			System.err.println("ERROR 404: FILE IS NONEXISTANT");
		}
		return(answer);
	}
} //Close RiddlerMainProgram Class

/*ADDITIONAL THREADS TO ALLOW TIME LIMIT ON ANSWERING RIDDLES
 *Taken from Stack Overflow solution at https://stackoverflow.com/questions/12803151/how-to-interrupt-a-scanner-nextline-call. Provides time limit for user input. Very slightly modified.
 */
class ReaderThread extends Thread
{
    @Override
    public void run()
    {
        System.out.print("INPUT ANSWER: ");
        try(Scanner in = new Scanner(System.in))
        {
            String inputanswer = in.nextLine();
            if(inputanswer.trim().isEmpty())
            {
                inputanswer="I DON'T KNOW."; //Default Answer to Riddle
            }
            System.out.println("RIDDLER: WE HAVE AN ANSWER!");
            System.out.println("     DRUM ROLL PLEASE!");
            
            /*WRITE ANSWER TO FILE
             *Instead of messing with multithread synchronization, I decided to transfer the Strings by writing and then reading.
             */
            File outFile=new File("answer.dat");
            FileOutputStream fooStream = new FileOutputStream(outFile, false); //This fancy thing will prevent appending in the answer .dat. Found online at https://stackoverflow.com/questions/1016278/is-this-the-best-way-to-rewrite-the-content-of-a-file-in-java
            BufferedWriter writer=new BufferedWriter(new FileWriter(outFile,true));
            writer.write(inputanswer);
            writer.close();
            try
            {
            	notifyAll();
            }
            catch(IllegalMonitorStateException e)
            {
            }
        }
        catch(IOException e)
		{
			System.err.println("ERROR 404: FILE IS NONEXISTANT");
		}
    }
}

/*ADDITIONAL THREADS TO ALLOW TIME LIMIT ON ANSWERING RIDDLES
 *Taken from Stack Overflow solution at https://stackoverflow.com/questions/12803151/how-to-interrupt-a-scanner-nextline-call. Provides time limit for user input. Very slightly modified.
 */
class TimeoutThread extends Thread
{
    @Override
    public void run()
    {
        try
        {
            Thread.sleep(TimeUnit.SECONDS.toMillis(15));
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        }
        catch(Exception e) 
        {
            e.printStackTrace();
        }
    }
}