package timo.jyu;

import java.util.ArrayList;

/**
	Helper class to keep track of pointers and record data
*/
public class EDFRecord{
	public int pointer; //Stores the pointer to the beginning of this record
	public ArrayList<ArrayList<Short>> data; //Stores the record data
	public EDFRecord(int pointer){
		this.pointer = pointer;
		data = new ArrayList<ArrayList<Short>>();
	}
	
	public short[] getSignal(int sNo){
		Short[] temp = data.get(sNo).toArray(new Short[]{(short) 0});
		short[] ret = new short[temp.length];
		for (int i = 0;i<temp.length;++i){
				ret[i] = (short) temp[i];
		}
		return ret;
	}
}