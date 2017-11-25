package cs362;

import java.io.Serializable;
import java.util.HashMap;

public class FeatureVector implements Serializable {

	//public HashMap<Integer, Double> FeatureVector = null;
	public int uid;
	public double sentiment;
	public int time;

	public FeatureVector(int uid, double sentiment, int time) {
		this.uid = uid;
		this.sentiment = sentiment;
		this.time = time;
	}
	
	public int getUid() {
		return this.uid;
	}

	public double getSentiment() {
		return this.sentiment;
	}

	public int getTime() {
		return this.time;
	}

}

