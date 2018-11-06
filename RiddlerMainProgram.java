/*Programmer: Shannon Riegle
 *FINAL PROJECT FOR FIRST SEMESTER AP COMPUTER SCIENCE
 *Main Driver for Riddler Program
 */
import java.util.*;
import java.io.*;

public class RiddlerMainProgram
{
	public static void main(String[] args) throws IOException, InterruptedException
	{
		String randomriddle=randomriddle(); //Skips to randomriddle() method to choose the riddle Riddler asks
		/*RIDDLE AND ANSWER DISTINGUISHER
		 *This code parses the line from the .dat file to seperate the riddle from its answer.
		 */
		 
		int comma=randomriddle.indexOf(",");
		String randomanswer=randomriddle.substring(comma+1);
		randomriddle=randomriddle.substring(0,comma);
		System.out.println("RIDDLER: "+randomriddle);
		
		Scanner keyboard=new Scanner(System.in);
		String guessedanswer=keyboard.nextLine();
		if(guessedanswer.equals(randomanswer))
		{
			System.out.println("RIDDLER: YOU GUESSED IT.");
		}
		else
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
		try
		{
			File length=new File("riddles.dat");
			BufferedReader reader=new BufferedReader(new FileReader(length));
			placeholder=reader.readLine();
			while(placeholder!=null)
			{
				number=number+1;
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
			while(n<random)
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
} //Close RiddlerMainProgram Class

//////////DEFINE VARIABLES, RANDOM NUMBER//////////
