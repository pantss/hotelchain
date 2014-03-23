package hotelchain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileHandler 
{
//	private ArrayList accounts;
	private int IDcounter;
	
	private final String filename;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private boolean createNewFile;
	
	public FileHandler(String _filename)
	{
		//TODO underscoring convention
		filename= _filename;
		
		if(new File(filename).isFile())
			createNewFile = false;
		else
			createNewFile = true;
	
	}
	
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
		
			//catch e | e possible in 1.7
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

	protected boolean writeFile(ArrayList<?> accounts, int _IDcounter)
	{
		//TODO underscoring convention
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
		
	protected boolean createNewFile()
	{
		return createNewFile;
	}
	
	protected int getIDcounter()
	{
		return IDcounter;
	}
}
