import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The Test class creates Caches, searches the Caches using input specified by
 * the user, and outputs the data and its hit ratio.
 * 
 * @author chloejohnson
 *
 */
public class Test {

	// control output - modified by command-line args
	int option;
	int size1;
	int size2 = 0;
	File textInput;

	// global variables
	int hit1 = 0;
	int hitData1 = 0;
	int hitData2 = 0;
	int references1 = 0;
	int references2 = 0;

	/**
	 * Executes the main program
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Test test = new Test(args);
		test.runTest();
		test.printResults();
	}

	/**
	 * Creates a new Test and checks the user arguments to ensure that they don't
	 * violate the usage of the Test.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public Test(String[] args) throws Exception {
		if (args.length > 0) {
			if (Integer.parseInt(args[0]) == 1) {
				option = 1;
				try {
					size1 = Integer.parseInt(args[1]);
					textInput = new File(args[2]);
				} catch (NumberFormatException e) {
					System.err.println(args[1] + " must be an integer");
					System.exit(1);
				} catch (IndexOutOfBoundsException e) {
					System.err.println("Argument not found");
				}
			} else if (Integer.parseInt(args[0]) == 2) {
				option = 2;
				try {
					size1 = Integer.parseInt(args[1]);
					size2 = Integer.parseInt(args[2]);
					if (size1 > size2) {
						throw new Exception(
								"Cache level 2 must have equal or greater size in relation to cache level 1");
					}
					textInput = new File(args[3]);
				} catch (NumberFormatException e) {
					System.err.println(args[1] + " and  " + args[2] + " must be integers");
					System.exit(1);
				} catch (IndexOutOfBoundsException e) {
					System.err.println("Argument not found");
				}
			} else {
				throw new Exception("Incorrect command-line usage");
			}
		} else {
			throw new Exception("Incorrect command-line usage");
		}
	}

	/**
	 * Creates one cache for option 1 and two caches for option 2. For option 1, the
	 * cache is searched and the hits and references are updated. For option 2, the
	 * first cache is searched. If no hits are returned, the second cache is
	 * searched. Hits and references are updated accordingly.
	 * 
	 * 
	 * @throws Exception
	 */
	private void runTest() throws Exception {
		try {
			Cache<String> cache1;
			Cache<String> cache2 = null;
			Scanner scanner = new Scanner(textInput);
			String word;
			if (option == 1) {
				cache1 = new Cache<String>(size1, 1);
				System.out.println("First level cache with " + size1 + " entries has been created");
				while (scanner.hasNext()) {
					word = scanner.next();
					hit1 = cache1.search(word);
					hitData1 += hit1;
					references1++;
				}
			} else {
				cache1 = new Cache<String>(size1, 1);
				System.out.println("First level cache with " + size1 + " entries has been created");
				cache2 = new Cache<String>(size2, 2);
				System.out.println("Second level cache with " + size2 + " entries has been created");
				while (scanner.hasNext()) {
					word = scanner.next();
					hit1 = cache1.search(word, cache2);
					hitData1 += hit1;
					references1++;
					if (hit1 == 0) {
						hitData2 += cache2.search(word, cache1);
						references2++;
					}
				}
			}
			
			scanner.close();
		} catch (FileNotFoundException e) {
			System.err.println("The " + textInput + " file couldn't be found");
		} catch (NoSuchLevelFoundException e) {
			System.err.println("This program only accepts level 1 and 2");
		}
	}

	/**
	 * Prints the results from the hits and references for option 2 or option 1.
	 */
	private void printResults() {
		if (option == 2) {
			System.out.println("..............................");
			System.out.println("The number of global references: " + references1);
			System.out.println("The number of global cache hits: " + (hitData1 + hitData2));
			System.out
					.println("The global hit ratio                  : " + (hitData1 + hitData2) / (double) references1);
			System.out.println("");
			System.out.println("The number of 1st-level references: " + references1);
			System.out.println("The number of 1st-level cache hits: " + hitData1);
			System.out.println("The 1st-level hit ratio                  : " + (hitData1 / (double) references1));
			System.out.println("");
			System.out.println("The number of 2nd-level references: " + references2);
			System.out.println("The number of 2nd-level cache hits: " + hitData2);
			System.out.println("The 2nd-level hit ratio                  : " + hitData2 / (double) references2);
		} else {
			System.out.println("..............................");
			System.out.println("The number of references: " + references1);
			System.out.println("The number of cache hits: " + (hitData1 + hitData2));
			System.out.println("The hit ratio                  : " + (hitData1 + hitData2) / (double) references1);
		}
	}
}
