package cs362;

import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OpinionPrediction extends Predictor {

    HashMap<Integer,ArrayList<MyPair>> history = new HashMap<>();

    public OpinionPrediction(List<Instance> instances){

         for(int i=0;i<instances.size();i++){
             int key = instances.get(i).getFeatureVector().getUid();
             MyPair mp = new MyPair(instances.get(i).getFeatureVector().getSentiment(),instances.get(i).getFeatureVector().getTime());
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
        printHistory();
    }

    void printHistory(){
        for (HashMap.Entry<Integer, ArrayList<MyPair>> m : history.entrySet()) {
            System.out.print(m.getKey()+":");
            ArrayList<MyPair> amp = m.getValue();
            for(int i = 0; i < amp.size(); i++) {
                System.out.print(" "+amp.get(i).getSentiment());
                System.out.print(" "+amp.get(i).getTime());
            }
            System.out.print("\n");
        }
    }

    @Override
    public void train(List<Instance> instances) {

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
