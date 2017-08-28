package cs362;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Venki on 8/28/2017.
 */

public class EvenOddAlgorithm extends Predictor {
    double even_sum,odd_sum;

    public EvenOddAlgorithm(){
        this.even_sum=0;
        this.odd_sum=0;
    }
    public void train(List<Instance> instances){
        for (Instance instance : instances) {
            FeatureVector fv = instance.getFeatureVector();
            HashMap<Integer, Double> hashMapfv = fv.FeatureVector;

            for(HashMap.Entry<Integer,Double> m:hashMapfv.entrySet()) {
                if(m.getKey()%2==0){
                    even_sum = even_sum + m.getValue();
                }
                if(m.getKey()%2==1){
                    odd_sum = odd_sum + m.getValue();
                }
            }
        }
    }

    public Label predict(Instance instance){
        return null;
    }
}
