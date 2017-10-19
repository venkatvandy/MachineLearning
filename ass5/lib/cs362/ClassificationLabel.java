package cs362;

import java.io.Serializable;

public class ClassificationLabel extends Label implements Serializable {
	
	int label;

	public ClassificationLabel(int label) {
		// TODO Auto-generated constructor stub
		this.label = label;
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.valueOf(label);
	}
}