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
            if(instance.getLabel().toString().equals("0")){
                this.count0++;
            }
            if(instance.getLabel().toString().equals("1")){
                this.count1++;
            }
        }
    }

    public Label predict(Instance instance){
        if(this.count0>this.count1){
            return new ClassificationLabel(0);
        }
        else{
            return new ClassificationLabel(1);
        }
    }
}