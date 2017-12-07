package cs362;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class DataReader {

	private Scanner senti_train_scanner;
	private Scanner senti_test_scanner;
	private Scanner get_number_of_lines;

	private Scanner graph_scanner;
	// Classification or regression?
	private boolean _classification;
	private int total_count;
	private int breakpoint;

	public DataReader(String senti_filename,String graph_filename, boolean classification) throws FileNotFoundException {
		this.senti_train_scanner = new Scanner(new BufferedInputStream(new FileInputStream(senti_filename)));
		this.senti_test_scanner = new Scanner(new BufferedInputStream(new FileInputStream(senti_filename)));
		this.get_number_of_lines = new Scanner(new BufferedInputStream(new FileInputStream(senti_filename)));
		this.graph_scanner = new Scanner(new BufferedInputStream(new FileInputStream(graph_filename)));
		this._classification = classification;
		this.total_count = 0;
		while (this.get_number_of_lines.hasNextLine()) {
			this.total_count++;
			this.get_number_of_lines.nextLine();
		}
		//System.out.println(this.total_count);
		breakpoint = (int)(0.9*total_count);
	}
	
	public void close() {
		this.senti_test_scanner.close();
		this.senti_train_scanner.close();
		this.get_number_of_lines.close();
		this.graph_scanner.close();
	}
	
	//public List<Instance> readData() {
	public OpinionData readData(int choice) {  //1-train  2-test
		//ArrayList<Instance> instances = new ArrayList<Instance>();
		OpinionData od = new OpinionData();
		//while (this.senti_scanner.hasNextLine()) {

		if(choice==1){
			int new_count = 0;
			while (new_count<breakpoint) {
				new_count++;
				String line = this.senti_train_scanner.nextLine();
				String line2 = this.senti_test_scanner.nextLine();
				if (line.trim().length() == 0)
					   continue;

				String user =line.split("	")[0];
				int uid = Integer.parseInt(user);
				int time = Integer.parseInt(line.split("	")[1]);
				double sentiment = Double.parseDouble(line.split("	")[2]);

				FeatureVector feature_vector = new FeatureVector(uid,sentiment,time);

				Instance instance = new Instance(feature_vector);
				//instances.add(instance);
				od.instances.add(instance);
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
		}
		else {
			int new_count = breakpoint;
			while (new_count < total_count) {
				new_count++;
				String line = this.senti_test_scanner.nextLine();
				if (line.trim().length() == 0)
					continue;

				String user = line.split("	")[0];
				int uid = Integer.parseInt(user);
				int time = Integer.parseInt(line.split("	")[1]);
				double sentiment = Double.parseDouble(line.split("	")[2]);

				FeatureVector feature_vector = new FeatureVector(uid, sentiment, time);

				Instance instance = new Instance(feature_vector);
				//instances.add(instance);
				od.instances.add(instance);
			}
		}
		return od;
	}
}