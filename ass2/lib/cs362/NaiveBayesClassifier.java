package cs362;

import org.apache.commons.math3.linear.*;

import java.util.HashMap;
import java.util.List;

public class NaiveBayesClassifier extends Predictor {

    double p_0,p_1;
    int cols;
    RealMatrix FeatureCount_0=null;
    RealMatrix FeatureCount_1=null;
    RealMatrix ProbCount_0=null;
    RealMatrix ProbCount_1=null;
    double total_0 = 0;
    double total_1 = 0;
    double lambda;

    public NaiveBayesClassifier(List<Instance> instances,double l){
        for (Instance instance : instances) {
            double label = Double.parseDouble(instance.getLabel().toString());
            if(label==1)
                total_1++;
            else
                total_0++;
        }

        lambda = l;

        p_0 = total_0/instances.size();
        p_1 = total_1/instances.size();

        System.out.print("\n\n*****total_0****: "+ total_0);
        System.out.print("\n\n*****total_1****: "+ total_1);

        System.out.print("\n\n*****p_0****: "+p_0);
        System.out.print("\n\n*****p_1****: "+p_1);

        int max_key = 0;
        for (Instance instance : instances) {
            FeatureVector fv = instance.getFeatureVector();
            HashMap<Integer, Double> hashMapfv = fv.FeatureVector;
            for (HashMap.Entry<Integer, Double> m : hashMapfv.entrySet()) {
                if (m.getKey() > max_key) {
                    max_key = m.getKey();
                }
            }
        }
        cols = max_key+1;
        FeatureCount_0 = new BlockRealMatrix(1,cols);
        FeatureCount_1 = new BlockRealMatrix(1,cols);
        ProbCount_0 = new BlockRealMatrix(1,cols);
        ProbCount_1 = new BlockRealMatrix(1,cols);
    }

    public void print_matrix(RealMatrix M){
        for(int i=0;i<M.getRowDimension();i++){
            //System.out.print("\n");
            for(int j=0;j<M.getColumnDimension();j++){
                System.out.print(M.getEntry(i,j)+"   ");
            }
        }
    }

    @Override
    public void train(List<Instance> instances) {

        for (Instance instance : instances) {
            double label = Double.parseDouble(instance.getLabel().toString());
            FeatureVector fv = instance.getFeatureVector();
            HashMap<Integer, Double> hashMapfv = fv.FeatureVector;
            for (HashMap.Entry<Integer, Double> m : hashMapfv.entrySet()) {
                double count;
                if(label==0) {
                    count = FeatureCount_0.getEntry(0, m.getKey()) + 1;
                    FeatureCount_0.setEntry(0,m.getKey(),count);
                }
                else if(label==1) {
                    count = FeatureCount_1.getEntry(0, m.getKey()) + 1;
                    FeatureCount_1.setEntry(0, m.getKey(),count);
                }
            }
        }
        /*System.out.print("\n\nFeatureCount_0****\n\n");
        print_matrix(FeatureCount_0);
        System.out.print("\n\nFeatureCount_1****\n\n");
        print_matrix(FeatureCount_1);*/

        for(int i=0;i<cols;i++){
            double prob = FeatureCount_0.getEntry(0,i)/total_0;
            ProbCount_0.setEntry(0,i,prob);

            prob = FeatureCount_1.getEntry(0,i)/total_1;
            ProbCount_1.setEntry(0,i,prob);
        }


        /*System.out.print("\n\nProbCount_0****\n\n");
        print_matrix(ProbCount_0);
        System.out.print("\n\nProbCount_1****\n\n");
        print_matrix(ProbCount_1);*/

    }

    @Override
    public Label predict(Instance instance) {
        double prob0=0;
        double prob1=0;
        FeatureVector fv = instance.getFeatureVector();
        HashMap<Integer, Double> hashMapfv = fv.FeatureVector;
        for (HashMap.Entry<Integer, Double> m : hashMapfv.entrySet()) {
            prob0 = prob0 + Math.log10(ProbCount_0.getEntry(0,m.getKey()));
            prob1 = prob1 + Math.log10(ProbCount_1.getEntry(0,m.getKey()));
        }
        double predict_0= Math.log10(p_0) + prob0;
        double predict_1= Math.log10(p_1) + prob1;

        if(predict_0>predict_1){
            return new ClassificationLabel(0);
        }
        else{
            return  new ClassificationLabel(1);
        }
    }
}