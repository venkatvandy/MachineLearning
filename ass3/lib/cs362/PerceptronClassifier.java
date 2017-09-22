package cs362;

import org.apache.commons.math3.linear.*;

import java.util.HashMap;
import java.util.List;

public class PerceptronClassifier extends Predictor {

    public RealMatrix X = null;
    public RealMatrix Y = null;
    public RealMatrix Y_cap = null;
    public RealMatrix W = null;
    int rows;
    int cols;
    double learning_rate;
    int I;

    public PerceptronClassifier(List<Instance> instances,double lr,int training_iterations) {
        int i=0;
        rows = instances.size();

        Y = new BlockRealMatrix(rows,1);
        Y_cap = new BlockRealMatrix(rows,1);

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
        cols = max_key;

        X = new BlockRealMatrix(1, cols);
        W = new BlockRealMatrix(cols, 1);

        //Initialize weight vector
        for(i=0;i<cols;i++){
            W.setEntry(i,0,0);
        }

        i=0;
        //Copy Y labels
        for (Instance instance : instances) {
            double label = Double.parseDouble(instance.getLabel().toString());
            if(label==0)
                label = -1;
            Y.setEntry(i,0,label);
            i++;
        }
        //System.out.print("\n\n*****Given Labels*****\n\n");
        //print_matrix(Y);

        //Zero out Y_cap
        for(i=0;i<cols;i++){
            Y_cap.setEntry(i,0,0);
        }

        //Zero out X initially
        for(i =0;i<cols;i++){
            X.setEntry(0,i,0);
        }

        learning_rate=lr;
        I = training_iterations;
    }

    public void print_matrix(RealMatrix M){
        for(int i=0;i<M.getRowDimension();i++){
            //System.out.print("\n");
            for(int j=0;j<M.getColumnDimension();j++){
                System.out.print(M.getEntry(i,j)+"   ");
            }
        }
    }

    void makeFeatureVectorMatrixRowfromInstance(Instance instance){
        FeatureVector fv = instance.getFeatureVector();
        HashMap<Integer, Double> hashMapfv = fv.FeatureVector;
        for(int i =0;i<cols;i++){
            X.setEntry(0,i,0);
        }
        for(HashMap.Entry<Integer,Double> m:hashMapfv.entrySet()) {
            if(m.getValue()==1){
                X.setEntry(0,m.getKey()-1,1);
            }
        }
    }

    @Override
    public void train(List<Instance> instances) {
        int count=0;
        for(int k=1;k<=I;k++){
            count=0;
            for (Instance instance : instances) {
                makeFeatureVectorMatrixRowfromInstance(instance);
                double determinant = new LUDecomposition(X.multiply(W)).getDeterminant();
                //System.out.print("\n\n*****W*Xi*****\n\n");
                //print_matrix(W.multiply(X));
                //System.out.print("\n\n*****Determinant is*****: "+determinant);
                if(determinant>=0){
                    Y_cap.setEntry(count,0,1);
                }
                else{
                    Y_cap.setEntry(count,0,-1);
                }
                //if((Y.getEntry(count,0)==1 && Y_cap.getEntry(count,0)!=1) || (Y.getEntry(count,0)==0 && Y_cap.getEntry(count,0)!=-1)){
                if(Y.getEntry(count,0)!=Y_cap.getEntry(count,0)){
                    //Update weights
                    RealMatrix correction = X.transpose().scalarMultiply(learning_rate*Y.getEntry(count,0));
                    W = W.add(correction);
                    //System.out.print("\n\n*****Weight Matrix*****\n\n");
                    //print_matrix(W);
                }
                count++;
            }
        }

    }

    @Override
    public Label predict(Instance instance) {
        makeFeatureVectorMatrixRowfromInstance(instance);
        double determinant = new LUDecomposition(X.multiply(W)).getDeterminant();
        if(determinant>=0)
            return new ClassificationLabel(1);
        else
            return new ClassificationLabel(0);
    }
}