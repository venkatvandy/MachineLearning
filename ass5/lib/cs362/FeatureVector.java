package cs362;

import java.io.Serializable;
import java.util.HashMap;

public class FeatureVector implements Serializable {

	public HashMap<Integer, Double> FeatureVector = null;

	public FeatureVector(){
		this.FeatureVector = new HashMap<Integer,Double>();
	}


	public void add(int index, double value) {
		// TODO Auto-generated method stub
		synchronized (this) {
			this.FeatureVector.put(index,value);
		}
	}
	
	public double get(int index) {
		// TODO Auto-generated method stub
		synchronized (this) {
			return this.FeatureVector.get(index);
		}
	}

}
