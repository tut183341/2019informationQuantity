package s4.B183341;
import java.lang.*;
import s4.specification.*;

/*package s4.specification;

public interface FrequencerInterface {     // This interface provides the design for frequency counter.
    void setTarget(byte  target[]); // set the data to search.
    void setSpace(byte  space[]);  // set the data to be searched target from.
    int frequency(); //It return -1, when TARGET is not set or TARGET's length is zero
                    //Otherwise, it return 0, when SPACE is not set or SPACE's length is zero
                    //Otherwise, get the frequency of TAGET in SPACE
    int subByteFrequency(int start, int end);
    // get the frequency of subByte of taget, i.e target[start], taget[start+1], ... , target[end-1].
    // For the incorrect value of START or END, the behavior is undefined.

}
*/
public class Frequencer implements FrequencerInterface{
    // Code to start with: This code is not working, but good start point to work.
    byte [] myTarget;
    byte [] mySpace;
    boolean targetReady = false;
    boolean spaceReady = false;

    int []  suffixArray;

    // The variable, "suffixArray" is the sorted array of all suffixes of mySpace.
    // Each suffix is expressed by a integer, which is the starting position in mySpace.
    // The following is the code to print the variable
    
    private void printSuffixArray() {
		if(spaceReady) {
		    for(int i=0; i< mySpace.length; i++) {
				int s = suffixArray[i];
				for(int j=s;j<mySpace.length;j++) {
				    System.out.write(mySpace[j]);
				}
				System.out.write('\n');
		    }
		}
    }

    private int suffixCompare(int i, int j) {
		// comparing two suffixes by dictionary order.
		// i and j denotes suffix_i, and suffix_j
		// if suffix_i > suffix_j, it returns 1
		// if suffix_i < suffix_j, it returns -1
		// if suffix_i = suffix_j, it returns 0;
		// It is not implemented yet, 
		// It should be used to create suffix array.
		// Example of dictionary order
		// suffix_i    suffix_j
		// "i"      <  "o"        : compare by code
		// "Hi"     <  "Ho"       ; if head is same, compare the next element
		// "Ho"     <  "Ho "      ; if the prefix is identical, longer string is big
		//
		// ****  Please write code here... ***
 	   	//2/18 17:14 byte列を文字列にしてからの処理を，byte列のままでの処理に変更
    		/*byte列を比較する回数を決定*/
    		int loop = 0; //比較する回数を格納する変数
    		if(i > j) {
			loop = mySpace.length - i;
		}else if(j > i) {
			loop = mySpace.length - j;
		}else {
			return 0; //iとjが同一の値なら，比較対象の文字列も同一である為
		}
    	
    		/*比較*/
    		int k = 0;
    		for(; k < loop; k++) {
    			if(mySpace[i+k] > mySpace[j+k]) {
					return 1;
				}else if(mySpace[i+k] < mySpace[j+k]) {
					return -1;
				}
	    	}
    	
    		/*比較した部分で大小関係が現れなかった場合，byte長が長い方が大きい*/
    		if(loop == k) {
				if(i > j) {
					return -1;
				}else {
					return 1;
				}
			}
    	
    		return 0;
    }

    public void setSpace(byte []space) { 
		mySpace = space;
		if(mySpace.length > 0) spaceReady = true; 
		suffixArray = new int[space.length];
		
		// put all suffixes  in suffixArray. Each suffix is expressed by one integer.
		
		for(int i = 0; i< space.length; i++) {
		    suffixArray[i] = i;
		}
		// Sorting is not implmented yet.
		// ****  Please write code here... ***
		//バブルソート
		/*int compResult = 0;
		for(int i = 0; i < suffixArray.length - 1; i++) {
			for(int j = i; j < suffixArray.length; j++) {
				compResult = suffixCompare(suffixArray[i], suffixArray[j]);
				//System.out.println(compResult);
				if(compResult == 1) {
					int tmp = suffixArray[i];
					suffixArray[i] = suffixArray[j];
					suffixArray[j] = tmp;
				}
			}
		}*/
		
		//クイックソート
		if(suffixArray.length != 0) {
			quickSort(suffixArray, 0, suffixArray.length - 1);	
		}
		
    }
    
    void quickSort(int[] array, int left, int right){
    	//再起を用いてクイックソートを実現するメソッド
    	int index = partition(array, left, right);
    	if(left < index - 1){
    		quickSort(array, left, index - 1);
    	}
    	if(index < right){
    		quickSort(array, index, right);
    	}
    }

    int partition(int[] array, int left, int right){
    	//partitionを計算するメソッド
    	int pivot = array[(left + right)/2];
    	while (left < right) {
    		while (suffixCompare(suffixArray[left], pivot) == -1) left++;
    		while (suffixCompare(suffixArray[right], pivot) == 1) right --;
    		if(left <= right){
    			int tmp = array[left];
    			array[left] = array[right];
    			array[right] = tmp;
    			left ++;
    			right --;
    		}
    	}
    	return left;
    }

    private int targetCompare(int i, int start, int end) {
		// comparing suffix_i and target_start_end by dictonary order with limitation of length;
		// if the beginning of suffix_i matches target_start_end, and suffix is longer than target  it returns 0;
		//  suffix_i --> mySpace[i], mySpace[i+1], .... , mySpace[mySpace.length -1]
		//  target_start_end -> myTarget[start], myTarget[start+1], .... , myTarget[end-1]
		// if suffix_i > target_start_end it return 1;
		// if suffix_i < target_start_end it return -1
		// It is not implemented yet.
		// It should be used to search the apropriate index of some suffix.
		// Example of search
		// suffix_i        target_start_end
	        // "o"       >     "i"    
	        // "o"       <     "z"
		// "o"       =     "o"
	        // "o"       <     "oo"
		// "Ho"      >     "Hi"
		// "Ho"      <     "Hz"
		// "Ho"      =     "Ho"
	        // "Ho"      <     "Ho "   : "Ho " is not in the head of suffix "Ho"
		// "Ho"      =     "H"     : "H" is in the head of suffix "Ho"
		
		// ****  Please write code here... ***
    		//2/18 17:14 byte列を文字列にしてからの処理を，byte列のままでの処理に変更
    		int loop = end - start;
    		int flag = 0; //targetとなるbyte列の長さが，spaceのbyte列の長さよりも大きい時1
    	
    		if(loop > (mySpace.length - i)) { //byte比較する回数が，spaceのbyte長を超えないようにする為
    			loop = mySpace.length - i; flag = 1;
    		}
    		
    		for(int j = 0; j < loop; j++) {
    			if(mySpace[i + j] > myTarget[start + j]) {
    				return 1;
    			}else if(mySpace[i + j] < myTarget[start + j]) {
    				return -1;
    			}
    		}
    	
    		if(flag == 1) {
    			return -1;
    		}else {
    			return 0;
    		}
    	
    }

    private int subByteStartIndex(int start, int end) {
		// It returns the index of the first suffix which is equal or greater than subBytes;
		// not implemented yet;
		// If myTaget is "Hi Ho",  start=0, end=2 means "Hi".
		// For "Ho", it will return 5  for "Hi Ho Hi Ho".
		//   5 means suffix_5,
		//   Please note suffix_5 is "Ho" and "Ho" starts from here.
		// For "Ho ", it will return 6 for "Hi Ho Hi Ho".
		//   6 means suffix_6,
		//   Please note suffix_6 is "Ho Hi Ho", and "Ho " starts from here.
		//
		// ****  Please write code here... ***
	    int startIndex = -1;
	    
	    /*二分探索*/
	    int low = 0;
	    int high = suffixArray.length - 1;
	    int mid = 0;
	    
	    while(low <= high) {
	    	mid = (low + high) / 2;
	    	if(targetCompare(suffixArray[mid], start, end) == 0) {
	    		if(mid == 0) {
	    			startIndex = mid;
	    			break;
	    		}
	    		for(mid = mid - 1 ;mid >= 0; mid--) {
	    			if(targetCompare(suffixArray[mid], start, end) != 0) {
	    				startIndex = mid + 1;
	    				break;
	    			}
	    		}
	    		if(mid == -1) {
	    			startIndex = 0;
	    		}
	    		break;
	    	}else if(targetCompare(suffixArray[mid], start, end) == 1) {
	    		high = mid - 1;
	    	}else {
	    		low  = mid + 1;
	    	}
	    }
	    
	    /*単純探索*/
	    /*for(int i = 0; i < suffixArray.length; i++) {
	    	if(targetCompare(suffixArray[i], start, end) == 0) {
	    		startIndex = i;
	    		break;
	    	}
	    }*/
	    
	    //System.out.println("startIndex : " + startIndex);
	    
		return startIndex; // This line should be modified.
    }

    private int subByteEndIndex(int start, int end) {
		// It returns the next index of the first suffix which is greater than subBytes;
		// not implemented yet
		// If myTaget is "Hi Ho",  start=0, end=2 means "Hi".
		// For "Ho", it will return 7  for "Hi Ho Hi Ho".
		// For "Ho ", it will return 7 for "Hi Ho Hi Ho".
		//  7 means suffix_7,
		//  Please note suffix_7 is "i Ho Hi", which does not start with "Ho" nor "Ho ".
	    //  Whereas suffix_5 is "Ho Hi Ho", which starts "Ho" and "Ho ".
		//
		// ****  Please write code here... ***
	    int endIndex = -1;
	    
	    /*二分探索*/
	    int low = 0;
	    int high = suffixArray.length - 1;
	    int mid = 0;
	    
	    while(low <= high) {
	    	mid = (low + high) / 2;
	    	if(targetCompare(suffixArray[mid], start, end) == 0) {
	    		if(mid == 0) {
	    			endIndex = mid + 1;
	    			break;
	    		}
	    		for(mid = mid + 1 ;mid < suffixArray.length ; mid++) {
	    			if(targetCompare(suffixArray[mid], start, end) != 0) {
	    				endIndex = mid;
	    				break;
	    			}
	    		}
	    		if(mid == suffixArray.length) {
	    			endIndex = suffixArray.length;
	    		}
	    		break;
	    	}else if(targetCompare(suffixArray[mid], start, end) == 1) {
	    		high = mid - 1;
	    	}else {
	    		low  = mid + 1;
	    	}
	    }
	    
	    /*単純探索*/
	    /*for(int i = suffixArray.length - 1; i >= 0; i--) {
	    	if(targetCompare(suffixArray[i], start, end) == 0) {
	    		endIndex = i + 1;
	    		break;
	    	}
	    }*/
	    
    	//System.out.println("endIndex : " + endIndex);
	    
		return endIndex; // This line should be modified.
    }

    public int subByteFrequency(int start, int end) {
		/* This method be work as follows, but
		int spaceLength = mySpace.length;
		int count = 0;
		for(int offset = 0; offset< spaceLength - (end - start); offset++) {
		    boolean abort = false;
		    for(int i = 0; i< (end - start); i++) {
			if(myTarget[start+i] != mySpace[offset+i]) { abort = true; break; }
		    }
		    if(abort == false) { count++; }
		}
		*/
		int first = subByteStartIndex(start, end);
		int last1 = subByteEndIndex(start, end);
		return last1 - first;
    }

    public void setTarget(byte [] target) { 
    	myTarget = target; if(myTarget.length>0) targetReady = true; 
    }

    public int frequency() {
		if(targetReady == false) return -1;
		if(spaceReady == false) return 0;
		return subByteFrequency(0, myTarget.length);
    }

    public static void main(String[] args) {
		Frequencer frequencerObject;
		try {
		    frequencerObject = new Frequencer();
		    frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
		    frequencerObject.printSuffixArray(); // you may use this line for DEBUG
		    System.out.println();
		    /* Example from "Hi Ho Hi Ho"
		       0: Hi Ho
		       1: Ho
		       2: Ho Hi Ho
		       3:Hi Ho
		       4:Hi Ho Hi Ho
		       5:Ho
		       6:Ho Hi Ho
		       7:i Ho
		       8:i Ho Hi Ho
		       9:o
		       A:o Hi Ho
		    */
	
		    frequencerObject.setTarget(" ".getBytes());
		    
		    // ****  Please write code to check subByteStartIndex, and subByteEndIndex
		    //int start = frequencerObject.subByteStartIndex(0, 2);
		    //int end   = frequencerObject.subByteEndIndex(0, 2);
		    //System.out.println("start:" + start);
		    //System.out.println("end:" + end); //デバッグ用
		    //
	
		    int result = frequencerObject.frequency();
		    System.out.print("Freq = "+ result+" ");
		    if(3 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }
		}
		catch(Exception e) {
			e.printStackTrace();
		    System.out.println("STOP");
		}
    }
}

