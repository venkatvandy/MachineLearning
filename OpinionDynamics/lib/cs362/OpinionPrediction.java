package cs362;

import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OpinionPrediction extends Predictor {

    HashMap<Integer,ArrayList<MyPair>> history = new HashMap<>();

    //public OpinionPrediction(List<Instance> instances){
    public OpinionPrediction(OpinionData opinionData){

         for(int i=0;i<opinionData.instances.size();i++){
             int key = opinionData.instances.get(i).getFeatureVector().getUid();
             MyPair mp = new MyPair(opinionData.instances.get(i).getFeatureVector().getSentiment(),opinionData.instances.get(i).getFeatureVector().getTime());
             if(history.containsKey(key)){
                 ArrayList<MyPair> amp = history.get(key);
                 amp.add(mp);
                 history.put(key,amp);
             }
             else {
                 ArrayList<MyPair> amp= new ArrayList<>();
                 amp.add(mp);
                 history.put(key,amp);
             }
         }

        for (HashMap.Entry<Integer, ArrayList<MyPair>> m : history.entrySet()) {
           if(!opinionData.OpGraph.containsKey(m.getKey())){
               opinionData.OpGraph.put(m.getKey(),new ArrayList<Integer>());
           }
        }

        printHistory(opinionData.OpGraph);

    }

    void printHistory(HashMap<Integer,ArrayList<Integer>> OpGraph){
        for (HashMap.Entry<Integer, ArrayList<MyPair>> m : history.entrySet()) {
            System.out.print(m.getKey()+":");
            ArrayList<MyPair> amp = m.getValue();
            for(int i = 0; i < amp.size(); i++) {
                System.out.print(" "+amp.get(i).getSentiment());
                System.out.print(" "+amp.get(i).getTime());
            }
            System.out.print("\n");
        }

        System.out.print("Graph\n");
        for (HashMap.Entry<Integer, ArrayList<Integer>> m : OpGraph.entrySet()) {
            System.out.print(m.getKey()+":");
            ArrayList<Integer> amp = m.getValue();
            for(int i = 0; i < amp.size(); i++) {
                System.out.print(" "+amp.get(i));
            }
            System.out.print("\n");
        }


    }

    @Override
    public void train(OpinionData opinionData) {

    }

    @Override
    public Label predict(Instance instance) {
        return null;
    }

    public class MyPair
    {
        private final double sentiment;
        private final int time;

        public MyPair(double sentiment, int time)
        {
            this.sentiment = sentiment;
            this.time = time ;
        }

        public double getSentiment()   { return this.sentiment; }
        public int getTime() { return this.time; }
    }
}
