package cs362;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class DataReader {

	private Scanner senti_scanner;
	private Scanner graph_scanner;
	// Classification or regression?
	private boolean _classification;

	public DataReader(String senti_filename,String graph_filename, boolean classification) throws FileNotFoundException {
		this.senti_scanner = new Scanner(new BufferedInputStream(new FileInputStream(senti_filename)));
		this.graph_scanner = new Scanner(new BufferedInputStream(new FileInputStream(graph_filename)));
		this._classification = classification;
	}
	
	public void close() {
		this.senti_scanner.close();
		this.graph_scanner.close();
	}
	
	//public List<Instance> readData() {
	public OpinionData readData() {
		//ArrayList<Instance> instances = new ArrayList<Instance>();
		OpinionData od = new OpinionData();
		//int a=0;
		while (this.senti_scanner.hasNextLine()) {
			String line = this.senti_scanner.nextLine();
			if (line.trim().length() == 0)
				   continue;

			/*if(a==0){
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
			}*/

			//else
			//{
			String user =line.split("	")[0];
			int uid = Integer.parseInt(user);
			int time = Integer.parseInt(line.split("	")[1]);
			double sentiment = Double.parseDouble(line.split("	")[2]);

			FeatureVector feature_vector = new FeatureVector(uid,sentiment,time);

			Instance instance = new Instance(feature_vector);
			//instances.add(instance);
			od.instances.add(instance);
			//}
		}

		while (this.graph_scanner.hasNextLine()) {
			String line = this.graph_scanner.nextLine();
			if (line.trim().length() == 0)
				continue;

			String follower =line.split("	")[0];
			int follower_uid = Integer.parseInt(follower);
			int followee = Integer.parseInt(line.split("	")[1]);

			if(od.OpGraph.containsKey(follower_uid)){
				ArrayList<Integer> amp = od.OpGraph.get(follower_uid);
				amp.add(followee);
				od.OpGraph.put(follower_uid,amp);
			}
			else {
				ArrayList<Integer> amp= new ArrayList<>();
				amp.add(followee);
				od.OpGraph.put(follower_uid,amp);
			}
		}

			//return instances;
		return od;
	}
}