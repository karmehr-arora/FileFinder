package finder;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;
import java.util.Scanner;

/**
 * @author Karmehr Arora
 */
public class FileFinder{
	
	private static int numFiles = 0, numFolders = 0;
	/**
	 * @param f represents the file attributes are currently being calculated from
	 * @return file modified date
	 */
	public static String toDate(File f) {
		Date mod = new Date (f.lastModified());
		String date = mod.getMonth()+1 + "/" + mod.getDate() + "/20" + (mod.getYear()-100);
		return date;
	}
	
	/**
	 * @param f represents the file attributes are currently being calculated from
	 * @return time of day file was modified
	 */
	public static String time(File f) {
		Date mod = new Date (f.lastModified());
		String morningOrNight, hours, minutes;
		morningOrNight = (mod.getHours()<=11)? "am" : "pm";
		hours = (mod.getHours()%12 < 10)? "0" + mod.getHours()%12 : "" + mod.getHours()%12;
		if(hours.equals("00")) {hours = "12";}
		minutes = (mod.getMinutes() > 9)? "" + mod.getMinutes() : "0" + mod.getMinutes();		
		return hours + ":" + minutes + " " + morningOrNight;
	}
	
	/**
	 * @param dir 			determines directory to be searched
	 * @param fileContains  keyword the file must contain
	 * @param extension		file extension i.e. .pdf or .docx
	 * @param searchDir		determines whether or not to search subdirectories
	 * 
	 * finds and prints out files, directories in correspondence 
	 * to the defining factors passed into the parameters
	 * if the parameters don't describe anything then all files are found & printed
	 */
	public static void findFiles(File dir, String fileContains, String extension, boolean searchDir) {
		File[] files = dir.listFiles(new FilenameFilter() {// filters files to only accept those which satisfy the user requirements
			@Override
			public boolean accept(File dir, String name) {
				boolean result = (name.toLowerCase().contains(fileContains))? true : false;
				if(dir.isDirectory())
					result = true;
				return result;
			}
		});	
		// file search issues
		if(files == null)
			return;
		boolean fCEmpty = fileContains.isEmpty();
		// prints the number of files the folder contains
		if(fCEmpty) {
			System.out.println("\tNumber of files: " + files.length);
			if(files.length == 0) {
				System.out.println("\tFolder contains no files");
				System.out.println("***************************************************");
			}
		}
		// searches each folder/file to ensure it matches the criteria and then prints out file details
		for(File f: files) {
			if(f.isFile()) {
				String absolutePath = f.getAbsolutePath(), name = f.getName().toLowerCase();
				if(absolutePath.endsWith(extension) && name.contains(fileContains)) {
					System.out.println(absolutePath);
					System.out.println("\tLast Modified: " + toDate(f));
					System.out.println("\tTime Modified: " + time(f));
					numFiles++;
				}
			}
			else {
				String name = f.getName().toLowerCase();
				if(name.contains(fileContains)) {
					numFolders++;
					System.out.println("***************************************************");
					System.out.println("\n\n" + f);
					System.out.println("\tFolder Last Modified: " + toDate(f));
					System.out.println("\tFolder Time Modified: " + time(f));
				}
				if(searchDir)
					findFiles(f, fileContains, extension, searchDir);
			}
		}
	}
	
	/**
	 * @param user Scanner input for user
	 * Finds and prints files found according to the user input it takes in
	 */
	public static void FileFind(Scanner user) {		
		//Gets user to input a legitimate directory
		File dir = new File("");
		while(!dir.exists() || dir.isFile()){
			System.out.println("What directory are you searching for files in? ");
			String path = user.nextLine();
			dir = new File(path); // put whatever directory you want
			if(!dir.exists() || dir.isFile())
				System.out.println("Please provide a valid path to a directory");
		}
		
		// user determines their preferences for keyword/phrase, fileType, date
		System.out.println("Is there a keyword or phrase the file you're looking for contains? If not, press enter");
		String fileContains = user.nextLine().toLowerCase().trim();
		
		System.out.println("File Type (ex. \".pdf\", \".docx\")?");
		System.out.println("Press enter if you are not looking for a specific filetype");
		String extension = "" + user.nextLine();
		
		System.out.println("Would you like to search any subdirectories? (Y/N)");
		boolean searchDir = (user.next().equals("Y"))? true : false;
				
		//finding & printing files
		System.out.println("Root Directory: " + dir);
		findFiles(dir, fileContains, extension, searchDir);
		System.out.println("Total Folders Found " + numFolders);
		System.out.println("Total Files Found: " + numFiles + "\ndone");
	}
	
	/**
	 * resets instance variables if the user would like to continue searching for files
	 */
	public static void reset() {
		numFolders = 0 ;
		numFiles = 0;
	}
	
	public static void main(String[] args) {
		Scanner user = new Scanner(System.in);
		String search = "C";
		while(search.equals("C")) {
			// begin fileSearch: 
			FileFind(user);
			
			// restart or quit search
			search = "";
			System.out.println("Enter Q to quit or C to continue: ");
			while(search.isEmpty()) {
				search = user.nextLine().toUpperCase();
				if (search == "")
					search = user.nextLine().toUpperCase();
				if(!(search.equals("C") || search.equals("Q"))){
					System.out.println("Please enter a valid input:");
					search = "";
				}	
			}
			reset();
		}
		// exiting application
		System.out.println("Search Ended");
		user.close();
		System.exit(0);
	}
}
