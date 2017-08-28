package cs362;

import java.util.List;

/**
 * Created by Venki on 8/28/2017.
 */

public class MajorityAlgorithm extends Predictor{

    int count0,count1;

    public MajorityAlgorithm() {
        this.count0=0;
        this.count1=0;
    }

    public void train(List<Instance> instances){
        for (Instance instance : instances) {
            if(instance.getLabel().toString()=="0"){
                this.count0++;
            }
            if(instance.getLabel().toString()=="1"){
                this.count1++;
            }
        }
    }

    public Label predict(Instance instance){
        return null;
    }
}