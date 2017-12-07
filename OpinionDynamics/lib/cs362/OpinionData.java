package cs362;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OpinionData {
    public HashMap<Integer,ArrayList<Integer>> OpGraph= null;
    public List<Instance> instances = null;

    public OpinionData(){
        this.OpGraph = new HashMap<>();
        this.instances = new ArrayList<Instance>();
    }

    void print_OpinionData(){
        for(int i=0;i<this.instances.size();i++){
            System.out.print(this.instances.get(i).getFeatureVector().getUid());
            System.out.print(this.instances.get(i).getFeatureVector().getSentiment());
            System.out.print(this.instances.get(i).getFeatureVector().getTime()+"\n");
        }
    }

}
