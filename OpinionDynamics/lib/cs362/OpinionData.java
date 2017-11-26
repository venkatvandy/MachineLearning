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

}
