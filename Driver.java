import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Driver {

	private static Theater theater = new Theater(10,20);
	
	public static void main(String[] args) throws FileNotFoundException {
		
		File output = new File ("output.txt");
		Scanner inputStream = new Scanner(new File(args[0]));
		PrintWriter outputStream = new PrintWriter(output);
		
		while (inputStream.hasNextLine()) {
			outputStream.println(theater.bookSeats(inputStream.nextLine()));
		}
		inputStream.close();
		outputStream.close();
		
		System.out.println("Program output at - " + output.getAbsolutePath());
	}	
}
