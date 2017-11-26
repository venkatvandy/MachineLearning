package cs362;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class DataReader {

	private Scanner _scanner;
	// Classification or regression?
	private boolean _classification;

	public DataReader(String filename, boolean classification) throws FileNotFoundException {
		this._scanner = new Scanner(new BufferedInputStream(new FileInputStream(filename)));
		this._classification = classification;
	}
	
	public void close() {
		this._scanner.close();
	}
	
	//public List<Instance> readData() {
	public OpinionData readData() {
		//ArrayList<Instance> instances = new ArrayList<Instance>();
		OpinionData od = new OpinionData();
		int a=0;
		while (this._scanner.hasNextLine()) {
			String line = this._scanner.nextLine();
			if (line.trim().length() == 0)
				   continue;

			if(a==0){
				a=1;
				String[] nodes =line.split(" ");
				for(int i=0;i<nodes.length;i++){
					ArrayList<Integer> flist = new ArrayList<>();
					int follower = Integer.parseInt(nodes[i].split(":")[0]);
					String followees  = nodes[i].split(":")[1];
					String[] followees_list = followees.split(",");
					for(int j =0;j<followees_list.length;j++){
						flist.add(Integer.parseInt(followees_list[j]));
					}

					od.OpGraph.put(follower,flist);
				}
			}

			else
			{
				String user =line.split(":")[0];
				int uid = Integer.parseInt(user);
				double sentiment = Double.parseDouble(line.split(":")[1]);
				int time = Integer.parseInt(line.split(":")[2]);

				FeatureVector feature_vector = new FeatureVector(uid,sentiment,time);

				Instance instance = new Instance(feature_vector);
				//instances.add(instance);
				od.instances.add(instance);
			}
		}		
		
		//return instances;
		return od;
	}
}