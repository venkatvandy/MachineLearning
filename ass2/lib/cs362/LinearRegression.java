package cs362;

import org.apache.commons.math3.linear.*;
//import org.apache.commons.math3.linear.RealMatrix;

import java.util.HashMap;
import java.util.List;

public class LinearRegression extends Predictor{

    public RealMatrix X = null;

    public RealMatrix Y = null;

    public RealMatrix W = null;

    public int rows,cols;

    public LinearRegression(List<Instance> instances){
        rows = instances.size();
        cols = instances.get(0).getFeatureVector().FeatureVector.size()+1;
        X = new BlockRealMatrix(rows,cols);
        Y = new BlockRealMatrix(rows,1);


        int i=0;
        int j=0;

        for (Instance instance : instances) {
            FeatureVector fv = instance.getFeatureVector();
            HashMap<Integer, Double> hashMapfv = fv.FeatureVector;
            X.setEntry(i,j,1);
            j++;
            for(HashMap.Entry<Integer,Double> m:hashMapfv.entrySet()) {
                double val = m.getValue();
                X.setEntry(i,j,val);
                j++;
            }
            i++;
            j=0;
        }

        i=0;
        for (Instance instance : instances) {
            Y.setEntry(i,0,Double.parseDouble(instance.getLabel().toString()));
            i++;
        }

        /*for(i=0;i<rows;i++){
            System.out.print("\n");
            for(j=0;j<cols;j++){
                System.out.print(X.getEntry(i,j)+"   ");
            }
        }
        System.out.print("\n\n\n****************\n\n");
        for(i=0;i<rows;i++){
            System.out.print(Y.getEntry(i,0));
            System.out.print("\n");
        }*/
        i=0;
        j=0;
    }

    @Override
    public void train(List<Instance> instances) {
        RealMatrix X_t = X.transpose();
        RealMatrix temp = X_t.multiply(X);
        RealMatrix X_txXInv = new LUDecomposition(temp).getSolver().getInverse();
        W = X_txXInv.multiply(X_t.multiply(Y));

        /*System.out.print("\n\n\n****************\n\n");

        for(int i=0;i<cols;i++){
            System.out.print(W.getEntry(i,0));
            System.out.print("\n");
        }*/
    }

    @Override
    public Label predict(Instance instance) {
        FeatureVector fv = instance.getFeatureVector();
        HashMap<Integer, Double> hashMapfv = fv.FeatureVector;
        RealMatrix Xi = new BlockRealMatrix(1,cols);
        int j=0;
        Xi.setEntry(0,j,1);
        j++;
        for(HashMap.Entry<Integer,Double> m:hashMapfv.entrySet()) {
            double val = m.getValue();
            Xi.setEntry(0,j,val);
            j++;
        }
        /*System.out.print("\n\n\n****************\n\n");
        for(j=0;j<cols;j++){
            System.out.print(Xi.getEntry(0,j)+"  ");
        }
        System.out.print("\n\n\n****************\n\n");
        for(j=0;j<cols;j++){
            System.out.print(W.getEntry(j,0)+" ");
            System.out.print("\n");
        }

        System.out.print("\n\n\n****Determinant******\n\n");*/

        double determinant = new LUDecomposition(Xi.multiply(W)).getDeterminant();
        return new RegressionLabel(determinant);

    }
}
