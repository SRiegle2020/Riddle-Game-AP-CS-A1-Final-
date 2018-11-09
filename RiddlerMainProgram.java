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
	public static Scanner keyboard=new Scanner(System.in);
	public static void main(String[] args) throws IOException, InterruptedException, IllegalMonitorStateException
	{
		introduction();
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
		Thread.sleep(7500);
		String inputanswer=answer(); //Reads answer file to learn user input
		
		/*TEST FOR CORRECT ANSWER
		 *This section tests if the Player answered correctly or incorrectly.
		 */
		System.out.println("\nRIDDLER: ALL RIGHT, TIME IS UP!");   
        if(randomanswer.equals(inputanswer)) //Correct Answer
		{
			System.out.println("     CONGRATULATIONS, YOU ANSWERED THE RIDDLE CORRECTLY.");
			System.out.println("     HMM, PERHAPS YOU AREN'T AS INTELLECTUALLY INFERIOR TO ME AS I FIRST ASSUMED.");
			System.out.println("     NO MATTER, YOU STILL HAVE TO STUMP ME WITH A RIDDLE! HEH, GOOD LUCK WITH THAT.");
		}
		else //Incorrect Answer
		{
			System.out.println("RIDDLER: INCORRECT.");
			System.out.println("     THE ANSWER WAS "+randomanswer+", OBVIOUSLY.");
			Thread.sleep(1500);
			wheelofdeath();
		}
		ask();
	} //Close main Method
	
	public static void ask() throws InterruptedException, IOException
	{
		/*ASK A RIDDLE
		 *Code to input riddle to ask Riddler
		 */
		System.out.println("     WHAT RIDDLE CAN STUMP ME?");
		System.out.println("[INPUT RIDDLE]");
		String askedriddle=keyboard.nextLine(); //Have to double up because of error.
		askedriddle=keyboard.nextLine();
		int repeat=2;
		while(repeat==2)
		{
			new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor(); // Clear Screen Command
			System.out.println("RIDDLER: YOUR RIDDLE IS \""+askedriddle+"\"");
			System.out.println("1) THAT IS MY RIDDLE.");
			System.out.println("2) THAT IS NOT WHAT I SAID.");
			repeat=keyboard.nextInt();
			switch(repeat)
			{
				case 1:
					break;
				case 2:
					System.out.println("RIDDLER: THEN SPEAK UP! WHAT IS YOUR RIDDLE?");
					System.out.println("[INPUT RIDDLE]");
					askedriddle=keyboard.nextLine(); //Have to double up
					askedriddle=keyboard.nextLine();
					break;
				default:
					repeat=2;
					System.out.println("RIDDLER: THEN SPEAK UP! WHAT IS YOUR RIDDLE?");
					System.out.println("[INPUT RIDDLE]");
					askedriddle=keyboard.nextLine(); //Have to double up
					askedriddle=keyboard.nextLine();
					break;
			}
		}
		
		System.out.println("NOW I SHALL ANSWER");
		Thread.sleep(100000);
	}
	
	public static void wheelofdeath() throws InterruptedException, IOException
	{
		new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor(); // Clear Screen Command
		
		/*CLOSING DIALOGUE
		 *Just to add flesh and because Riddler can't shut up.
		 */		
		System.out.println("RIDDLER: YOU LOSE! DO YOU KNOW WHAT THAT MEANS?");
		Thread.sleep(1500);
		System.out.println("CROWD: SPIN THE WHEEL! SPIN THE WHEEL!");
		Thread.sleep(1500);
		System.out.println("RIDDLER: YOU HEARD THEM, SPIN THE WHEEL!");
		System.out.println("     IN THE MEAN TIME, WE WOULD LIKE TO THANK OUR SPONSERS, GOTHAM CENTRAL BANK.");
		System.out.println("     THANK YOU FOR MAKING IT SUPER EASY FOR US TO ROB YOU! WE DIDN'T EVEN HAVE TO BRING THE CROWBARS!");
		
		/*RANDOM PUNISHMENT
		 *Chooses one of six punishments to deliver to the loser.
		 */
		Random numgen=new Random(); //Random Number Block
		int random=numgen.nextInt(6)+1; //Randome number
		switch(random)
		{
			case 1:
				System.out.println("     ALL RIGHT, LET'S LOOK AT THE WHEEL. OH, MY.");
				System.out.println("     IT LOOKS THAT YOU ROLLED THE DE-MOLECULARIZER.");
				System.out.println("     TAKE IT AWAY, BOYS!");
				break;
			case 2:
				System.out.println("     WELL, WE SHOULD LOOK AT THE WHEEL.");
				System.out.println("     YOU ROLLED HA-HA-HA! YOU KNOW WHAT THAT MEANS.");
				System.out.println("     TAKE THIS IMBECILE TO JOKER. HE NEEDS SOME TEST SUBJECTS FOR A NEW STRAIN OF JOKER GAS.");
				break;
			case 3:
				System.out.println("3");
				break;
			case 4:
				System.out.println("4");
				break;
			case 5:
				System.out.println("5");
				break;
			case 6:
				System.out.println("6");
				break;
			default:
				System.out.println("RIDDLER: HUH, I DON'T THINK THAT'S SUPPOSED TO HAPPEN.");
				break;
		}
		Thread.sleep(2000);
		System.exit(0);
	}

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
		random=numgen.nextInt(number)+1; //Randome number
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
	
	public static String introduction() throws InterruptedException, IOException
	{
		/*INTRODUCTORY DIALOGUE
		 *Just some dialogue to add some flesh and instructions to the game.
		 */	
		
		System.out.println("RIDDLER: WHAT TIME IS IT?");
		//Thread.sleep(1000);
		System.out.println("CROWD: RIDDLE TIME!");
		//Thread.sleep(1000);
		System.out.println("RIDDLER: I'M SORRY, I DIDN'T HEAR YOU. WHAT TIME IS IT?");
		//Thread.sleep(1500);
		System.out.println("CROWD: RIDDLE TIME!");
		//Thread.sleep(1000);
		System.out.println("RIDDLER: THAT'S RIGHT, IT'S RIDDLE TIME!");
		System.out.println("     A GAUNTLET HAS BEEN LAID AT MY FEET.");
		System.out.println("     WHAT'S YOUR NAME, CONTESTANT?");
		
		/*USER NAME INPUT
		 *Just so user has a name
		 */
		System.out.println("[INPUT NAME]");
		String user=keyboard.nextLine();
		new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor(); // Clear Screen Command
		
		/*MORE DIALOGUE
		 *He just won't shut up.
		 */	
		System.out.println("RIDDLER: "+user+" HAS CHALLENGED ME TO A GAME.");
		System.out.println("     "+user+" HAS FIFTEEN SECONDS TO LISTEN TO MY RIDDLE AND WRITE DOWN AN ANSWER.");
		System.out.println("     IF "+user+" ANSWERS CORRECTLY, THEY MUST ASK A RIDDLE THAT STUMPS ME, THE KING OF RIDDLES!");
		System.out.println("     IF THEY SUCCEED, THEY WIN A FULL BRITAINICA ENCYCLOPEDIA SET, MINUS THE R'S.");
		System.out.println("CROWD: BOOOOO!");
		System.out.println("RIDDLER:... AND TEN THOUSAND DOLLARS.");
		System.out.println("CROWD: YEAH!!!");
		System.out.println("RIDDLER: HOWEVER, IF THEY DON'T ANSWER MY RIDDLE CORRECTLY OR IF I ANSWER THEIRS CORRECTLY,");
		System.out.println("     WE SHALL SPIN THE WHEEL OF DOOM TO DETERMINE THEIR FATE.");
		System.out.println("     LET'S BEGIN.");
		//Thread.sleep(10000);
		return(user);
	}
} 

/*ADDITIONAL THREADS TO ALLOW TIME LIMIT ON ANSWERING RIDDLES
 *Taken from Stack Overflow solution at https://stackoverflow.com/questions/12803151/how-to-interrupt-a-scanner-nextline-call. Provides time limit for user input. Very slightly modified.
 */
class ReaderThread extends Thread
{
    @Override
    public void run()
    {
        System.out.print("[INPUT ANSWER] ");
        try(Scanner in = new Scanner(System.in))
        {
            String inputanswer = in.nextLine();
            if(inputanswer.trim().isEmpty())
            {
                inputanswer="UNICORNS"; //Default Answer to Riddle
            }
            
            /*WRITE ANSWER TO FILE
             *Instead of messing with multithread synchronization, I decided to transfer the Strings by writing and then reading.
             */
            File outFile=new File("answer.dat");
            FileOutputStream fooStream = new FileOutputStream(outFile, false); //This fancy thing will prevent appending in the answer .dat. Found online at https://stackoverflow.com/questions/1016278/is-this-the-best-way-to-rewrite-the-content-of-a-file-in-java
            BufferedWriter writer=new BufferedWriter(new FileWriter(outFile,true));
            writer.write(inputanswer);
			writer.close();
			Thread.sleep(TimeUnit.SECONDS.toMillis(1000));
        }
        catch(Exception e)
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
            Thread.sleep(TimeUnit.SECONDS.toMillis(7));
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            Thread.sleep(TimeUnit.SECONDS.toMillis(1000));
        }
        catch(Exception e) 
        {
            e.printStackTrace();
        }
    }
}