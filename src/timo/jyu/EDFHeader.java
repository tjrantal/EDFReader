package timo.jyu;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
	Helper class to parse edf file header from a byte array 
	file definition http://www.edfplus.info/specs/edf.html
	Primarily to be used by EDFReader but can be used to explore an edf file
	
	Written by Timo Rantalainen 2017 tjrantal at gmail dot com. Released into the public domain.
*/
public class EDFHeader{
	//Constant length data
	String version;
	String localPat;
	String localRec;
	String startdate;
	String starttime;
	String numbytes;
	String reserved;
	String numDataRecords;
	String durationOfARecord;
	String numberOfSignals;
	
	//Variable length data
	ArrayList<String> labels;
	ArrayList<String> transducerTypes;
	ArrayList<String> physicalDimension;
	ArrayList<String> physicalMinimum;
	ArrayList<String> physicalMaximum;
	ArrayList<String> digitalMinimum;
	ArrayList<String> digitalMaximum;
	ArrayList<String> preFiltering;
	ArrayList<String> nOfSamplesInEachDataRecord;
	ArrayList<String> reserved2;
	
	//Fields to be read from other classes
	public int headerLength;	//Header lenght in bytes
	public int ndr;	//number of data records
	public int dor;	//duration of the recording
	public int ns;		//Number of signals
	public ArrayList<Integer> nSamples;	//Number of samples per data record
		
	ByteBuffer bb;	//Has to be global var so gets advanced when chars are read
	
	/**Constructor 
		@params data an array of data from the .edf file. Make sure this includes the whole header (I've fed in the whole file). Parses the header in this constructor
	*/
	public EDFHeader(byte[] data){
		bb = ByteBuffer.wrap(data);	//Wrap the array into a bytebuffer for easy reading
		ArrayList<String> temp = new ArrayList<String>();
		//Read header http://www.edfplus.info/specs/edf.html
		version 			= readString(8); 
		localPat 			= readString(80);
		localRec 			= readString(80);
		startdate 			= readString(8); 
		starttime 			= readString(8); 
		numbytes 			= readString(8); 
		reserved 			= readString(44);
		numDataRecords		= readString(8); 
		durationOfARecord	= readString(8); 
		numberOfSignals		= readString(4);
		
		headerLength = Integer.parseInt(numbytes);
		ndr = Integer.parseInt(numDataRecords);
		dor = Integer.parseInt(durationOfARecord);
		ns = Integer.parseInt(numberOfSignals);
		
		//Parse the variable length header fields
		labels = getVariableLengthField(16);
		transducerTypes = getVariableLengthField(80);
		physicalDimension = getVariableLengthField(8);
		physicalMinimum = getVariableLengthField(8);
		physicalMaximum = getVariableLengthField(8);
		digitalMinimum = getVariableLengthField(8);
		digitalMaximum = getVariableLengthField(8);
		preFiltering = getVariableLengthField(80);
		nOfSamplesInEachDataRecord  = getVariableLengthField(8);
		nSamples = new ArrayList<Integer>();
		for (int n = 0; n<nOfSamplesInEachDataRecord.size();++n){
			nSamples.add(Integer.parseInt(nOfSamplesInEachDataRecord.get(n)));
		}
		
	}
	
	/**
		Helper to extract ns Strings, used for variable length fields
		@params length length of each of the strings to extract
		@returns an array of strings created with @readString
	*/
	public ArrayList<String> getVariableLengthField(int length){
		ArrayList<String> temp = new ArrayList<String>();
		for (int i = 0; i<ns; ++i){
			temp.add(readString(length));
		}
		return temp;
	}

	/**Helper to extract a string from a byte buffer
		@params length the number of bytes in the string fields
		@returns trimmed string with up to length characters (trims away white space)
	*/
	public String readString(int length){
		byte[] temp = new byte[length];
		bb.get(temp);
		//System.out.println(temp.toString());
		return new String(temp).trim();
	}
	
	/**
		Helper methods to extract data from the header
	*/
	
	public String[] getLables(){
		return labels.toArray(new String[]{""});
	}
	
	public String[] getTransducerTypes(){
		return labels.toArray(new String[]{""});
	}
	
	public String[] getPhysicalDimension(){
		return labels.toArray(new String[]{""});
	}
	
	public String[] getPhysicalMinimum(){
		return labels.toArray(new String[]{""});
	}
	
	public String[] getPhysicalMaximum(){
		return labels.toArray(new String[]{""});
	}
	
	public String[] getDigitalMinimum(){
		return labels.toArray(new String[]{""});
	}
	
	public String[] getDigitalMaximum(){
		return labels.toArray(new String[]{""});
	}
	
	public String[] getPreFiltering(){
		return labels.toArray(new String[]{""});
	}
	
	/**
		For debugging, print the header data to command line
	*/
	public void printInfo(){
		ArrayList<String> temp = new ArrayList<String>();
		temp.add(version);
		temp.add(localPat);
		temp.add(localRec);
		temp.add(startdate);
		temp.add(starttime);
		temp.add(numbytes);		
		temp.add(numDataRecords);
		temp.add(durationOfARecord);
		temp.add(numberOfSignals);
		
		ArrayList<String> tempLabels = new ArrayList<String>();
		tempLabels.add("version");
		tempLabels.add("localPat");
		tempLabels.add("localRec");
		tempLabels.add("startdate");
		tempLabels.add("starttime");
		tempLabels.add("numbytes");
		tempLabels.add("numDataRecords");
		tempLabels.add("durationOfARecord");
		tempLabels.add("numberOfSignals");
		
		for (int i = 0;i<temp.size();++i){
			System.out.println(tempLabels.get(i)+": "+temp.get(i));
		}
		
		//Print variable length stuff
		ArrayList<String> tempLabels2 = new ArrayList<String>();
		tempLabels2.add("labels");
		tempLabels2.add("transducerTypes");
		tempLabels2.add("physicalDimension");
		tempLabels2.add("physicalMinimum");
		tempLabels2.add("physicalMaximum");
		tempLabels2.add("digitalMinimum");
		tempLabels2.add("digitalMaximum");
		tempLabels2.add("preFiltering");
		tempLabels2.add("nOfSamplesInEachDataRecord");
		
		ArrayList<ArrayList<String>> temp2 = new ArrayList<ArrayList<String>>();
		temp2.add(labels);
		temp2.add(transducerTypes);
		temp2.add(physicalDimension);
		temp2.add(physicalMinimum);
		temp2.add(physicalMaximum);
		temp2.add(digitalMinimum);
		temp2.add(digitalMaximum);
		temp2.add(preFiltering);
		temp2.add(nOfSamplesInEachDataRecord);
		
		for (int i = 0;i<tempLabels2.size();++i){
			System.out.println(tempLabels2.get(i));
			for (int j = 0;j<temp2.get(i).size();++j){
				System.out.println("\t"+temp2.get(i).get(j));
			}
		}
	}
}
