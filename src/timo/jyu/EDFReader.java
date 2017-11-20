package timo.jyu;

import java.util.ArrayList;

//File reading
import java.io.FileInputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;
import java.text.ParsePosition;

//Handling the data array in memory 
import java.nio.ByteBuffer;
import java.nio.ByteOrder;	//Tell java to use little-endian

//Writing the data to a file
import java.io.FileOutputStream;
import java.nio.LongBuffer;
import java.nio.DoubleBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
	A class to read .edf files recorded by Faros180 device
	EDF file definition http://www.edfplus.info/specs/edf.html
	
	Does not implement the full specification of an EDF file and no attempt is made to handle EDF+. 
	
	Written by Timo Rantalainen 2017 tjrantal at gmail dot com. Released into the public domain.
*/

public class EDFReader{
	public EDFHeader header;	//Stores the header information
	byte[] bytes;	//The whole file is held in memory to keep things moving along faster
	ByteBuffer bb;	//Maintained in global var so pointers are maintained from a read to the next
	public int recordsRead = 0;
		
	//Debugging, enable calling the class from command line
	public static void main(String[] a){
		//System.out.println("Calling QuickTRXAnalyser "+a[0]+" "+a[1]);
		new EDFReader(a[0],a[1]);
	}
	
	public EDFReader(String path, String fName){
		File toRead = new File(path+fName);
		//System.out.println("QuickTRXAnalyser constructor "+toRead.toString());
		
		if (toRead.exists() & toRead.canRead()){
			//DEBUGGING read only the first few bits
			//int bytesToRead = 3000000;
			int bytesToRead = (int) toRead.length();
			//System.out.println("File to read "+fName+" "+bytesToRead+" bytes");
			
			bytes = new byte[bytesToRead];
			//byte[] bytes = new byte[(int) 10e6];
			try{
				FileInputStream fis = new FileInputStream(toRead);
				int readBytes = fis.read(bytes);
				if (bytesToRead == readBytes){
					fis.close();
					System.out.println("Read "+readBytes+" of "+bytesToRead);
					header = new EDFHeader(bytes);	//SEEMS TO WORK -> read data next
					//header.printInfo();	
					bb  = ByteBuffer.wrap(bytes);//Wrap bytes data in ByteBuffer
					bb.order(ByteOrder.LITTLE_ENDIAN);	//Make sure that data is read in little endian byte order
					bb.get(new byte[header.headerLength]); 	//Skip the header
				}
				//data = analyseData(bytes,100d);
			}catch (Exception e){
				System.out.println(e.toString());
			}
			
		}else{
			System.out.println("Could not find or read "+fName);
		}
		
	}
	
	/**
	Read data from the file with this function
	Depends on global var bb to maintain pointers
	@params recordsToRead, how many records of data to try to read
	@returns ArrayList of ArrayList<Short> with data for each of the channels, either recordsToRead records or as much as was available in the file
	*/
	public ArrayList<EDFRecord> readData(int recordsToRead){
		ArrayList<EDFRecord> temp = new ArrayList<EDFRecord>();

		//Make sure we read a maximum of header.ndr records from the file
		recordsToRead = header.ndr-recordsRead > recordsToRead ? recordsToRead : header.ndr-recordsRead;
		
		for (int s = 0; s< recordsToRead; ++s){
			temp.add(new EDFRecord(bb.position()));
			for (int i = 0;i<header.ns;++i){
				temp.get(s).data.add(new ArrayList<Short>());
			}
			for (int i = 0;i<header.ns;++i){
				for (int p = 0; p<header.nSamples.get(i); ++p){
					temp.get(s).data.get(i).add(bb.getShort());
				}
			}
			++recordsRead;	//Keep track of how many records have been read.
		}
		return temp;
	}
}