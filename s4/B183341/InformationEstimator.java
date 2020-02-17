package s4.B183341; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

/* What is imported from s4.specification
package s4.specification;
public interface InformationEstimatorInterface{
    void setTarget(byte target[]); // set the data for computing the information quantities
    void setSpace(byte space[]); // set data for sample space to computer probability
    double estimation(); // It returns 0.0 when the target is not set or Target's length is zero;
// It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
// The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
// Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
// Otherwise, estimation of information quantity, 
}                        
*/
public class InformationEstimator implements InformationEstimatorInterface{
    // Code to tet, *warning: This code condtains intentional problem*
    byte [] myTarget; // data to compute its information quantity
    byte [] mySpace;  // Sample space to compute the probability
    FrequencerInterface myFrequencer;  // Object for counting frequency

    byte [] subBytes(byte [] x, int start, int end) {
		// corresponding to substring of String for  byte[] ,
		// It is not implement in class library because internal structure of byte[] requires copy.
		byte [] result = new byte[end - start];
		for(int i = 0; i<end - start; i++) { result[i] = x[start + i]; };
		return result;
    }

    // IQ: information quantity for a count,  -log2(count/sizeof(space))
    double iq(int freq) {
    	return  - Math.log10((double) freq / (double) mySpace.length)/ Math.log10((double) 2.0);
    }

    public void setTarget(byte [] target) {
    	myTarget = target;
    }
    public void setSpace(byte []space) { 
		myFrequencer = new Frequencer();
		mySpace = space; myFrequencer.setSpace(space); 
    }

    public double estimation(){
    	//DPできる情報量の定義(例) calcResultはiqの計算結果を格納している
    	/*ex) abcd
    	 * iq("a")    = min(iq("a")) : calcResult[0]
    	 * iq("ab")   = min(iq("ab"), iq("a") + iq("b")) : calcResult[1]
    	 * iq("abc")  = min(iq("abc"), iq("a") + iq("bc"), iq("ab") + f("c")) : calcResult[2]
    	 * iq("abcd") = min(iq("abcd"), iq("a") + iq("bcd"), iq("ab") + iq("cd"), iq("abc") + iq("d")) : calcResult[3]
    	 * 過去の計算結果を用いるように書き換えると，
    	 * iq("a")    = min(iq("a")) : calcResult[0]
    	 * iq("ab")   = min(iq("ab"), calcResult[0] + iq("b")) : calcResult[1]
    	 * iq("abc")  = min(iq("abc"), calcResult[0] + iq("bc"), calcResult[1] + f("c")) : calcResult[2]
    	 * iq("abcd") = min(iq("abcd"), calcResult[0] + iq("bcd"), calcResult[1] + iq("cd"), calcResult[2] + iq("d")) : calcResult[3]
    	 * このように，過去の計算結果を用いることができ，計算量が多項式オーダとなる．
    	 */
    	
    	double[] calcResult = new double[myTarget.length]; //iqの計算結果を格納
    	double min = 0;  //最終的にcalcResultに入る値
    	double comp = 0; //一時保存用変数．(minと比べてcompの方が小さければminに置き換え)
    	myFrequencer.setTarget(myTarget); //FrequencerクラスのsubByteFrequencyメソッドを用いる為，FrequencerクラスにmyTargetを設定
    	
    	//上記の例のように動作させる為，FrequencerクラスのsubByteFrequencyメソッドを用いて，myTargetを先頭から切り取り，
    	//その切り取った文字列のiqの計算結果をcalcResultに保存する．
    	for(int i = 1; i <= calcResult.length; i++) {
    		min = iq(myFrequencer.subByteFrequency(0, i));
    		for(int j = 1; j < i; j++) {
    			comp =iq(myFrequencer.subByteFrequency(j, i)) + calcResult[j-1]; //calcResultに保存されている計算結果を再利用
    			if(comp < min) {
    				min = comp;
    			}
    		}
    		calcResult[i-1] = min;
    	}
    	
	return calcResult[myTarget.length - 1]; //配列の最後の番地に格納されているものが，文字列全体(myTarger全体)の情報量となる
    }


    public static void main(String[] args) {
		InformationEstimator myObject;
		double value;
		myObject = new InformationEstimator();
		myObject.setSpace("3210321001230123".getBytes());
		myObject.setTarget("0".getBytes());
		value = myObject.estimation();
		System.out.println(">0 "+value);
		myObject.setTarget("01".getBytes());
		value = myObject.estimation();
		System.out.println(">01 "+value);
		myObject.setTarget("0123".getBytes());
		value = myObject.estimation();
		System.out.println(">0123 "+value);
		myObject.setTarget("00".getBytes());
		value = myObject.estimation();
		System.out.println(">00 "+value);
    }
}

