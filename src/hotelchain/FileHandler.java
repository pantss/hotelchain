package hotelchain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Handles reading and writing of a file storing an ArrayList of objects and a counter integer.  
 * @author Joost Janssen
 */
public class FileHandler 
{
	private int IDcounter;	
	private final String filename;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private boolean createNewFile;
	
	/**
	 * Constructs a new FileHandler handling file with filename. 
	 * Indicates whether the file indicated exists or whether a new file should be created. 
	 * @param _filename Name of file.
	 */
	public FileHandler(String _filename)
	{
		filename= _filename;
		
		if(new File(filename).isFile())
			createNewFile = false;
		else
			createNewFile = true;	
	}
	
	/**
	 * Reads the contents of the file, stores the counter and returns the ArrayList it contains.
	 * @return Returns the ArrayList contained in the file.
	 */
	protected ArrayList<?> readFile()
	{
		ArrayList<?> accounts= null;
		boolean failed = false;		
		try {
			inputStream = new ObjectInputStream(new FileInputStream(filename));
		}catch (IOException e) {
			System.out.println("There was an error reading the file: Could not open file " + filename);
			failed = true;				
			System.out.println("Attention: new, empty file created: " + filename);
			IDcounter =0;
			writeFile(accounts, IDcounter);
		}
		if(!failed)
			try {
				accounts =  (ArrayList<?>) inputStream.readObject();
				IDcounter =  inputStream.readInt();
			} catch (IOException e1) {
				System.out.println("There was an error loading the file " + filename);
				failed = true;
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				System.out.println("There was an error loading the file " + filename);
				failed = true;
				e1.printStackTrace();
			}										
		
		if(!failed)
			try {
				inputStream.close();
			} catch (IOException e) {
					System.out.println("There was an error reading the file: Could not close file " + filename);
					e.printStackTrace();
			}			
		return accounts;			
	}

	/**
	 * Writes a given ArrayList and integer to the file.
	 * @param accounts ArrayList to be written to file.
	 * @param _IDcounter Integer to be written to file.
	 * @return Returns whether filewrite was successful. 
	 */
	protected boolean writeFile(ArrayList<?> accounts, int _IDcounter)
	{
		IDcounter = _IDcounter;
		boolean failed = false;
		try {
			 outputStream = new ObjectOutputStream(new FileOutputStream(filename));
			} catch (IOException e) {
		
			System.out.println("There was an error writing the file: Could not open file " + filename);
			failed = true;
			e.printStackTrace();
		}
		if(!failed)
			try {
				outputStream.writeObject(accounts);
				outputStream.writeInt(IDcounter);
			} catch (IOException e) {		
				System.out.println("There was an error writing the file " + filename);
				failed = true;
				e.printStackTrace();
			}			
		
		if(!failed)
			try {
				outputStream.close();
			} catch (IOException e) {		
				System.out.println("There was an error writing the file: Could not close file " + filename);
				failed = true;
				e.printStackTrace();
			}
		
		return !failed;
	}
		
	/**
	 * @return Returns whether creating a new file is required, returns false if file exists.
	 */
	protected boolean createNewFile()
	{
		return createNewFile;
	}
	
	/**
	 * @return Returns integer read from file.
	 */
	protected int getIDcounter()
	{
		return IDcounter;
	}
}