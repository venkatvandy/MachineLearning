package cs362;

import org.apache.commons.math3.linear.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class KernelLogisticRegression extends Predictor {
    public String kernel;
    double polynomial_kernel_exponent;
    double gaussian_kernel_sigma;
    double gradient_ascent_learning_rate;
    int gradient_ascent_training_iterations;
    int N;
    BlockRealMatrix gramMatrix;
    BlockRealMatrix newGramMatrix;
    BlockRealMatrix featureMatrix;
    BlockRealMatrix newfeatureMatrix;
    BlockRealMatrix alpha;
    int cols;
    static int count = 1;

    public KernelLogisticRegression(List<Instance> instances,String kernel,double polynomial_kernel_exponent,
                                    double gaussian_kernel_sigma,double gradient_ascent_learning_rate,int gradient_ascent_training_iterations){
        this.kernel = kernel;
        this.polynomial_kernel_exponent = polynomial_kernel_exponent;
        this.gaussian_kernel_sigma = gaussian_kernel_sigma;
        this.gradient_ascent_learning_rate = gradient_ascent_learning_rate;
        this.gradient_ascent_training_iterations = gradient_ascent_training_iterations;
        N = instances.size();
        //cols = instances.get(0).getFeatureVector().FeatureVector.size()+1;
        cols = getMaxKey(instances) +1 ;
        alpha = new BlockRealMatrix(1,N+1);

        int i;

        for(i=0;i<cols;i++){
            alpha.setEntry(0,i,0);
        }

        featureMatrix = new BlockRealMatrix(N+1,cols);

        makeFeatureMatrix(instances);

        //print_matrix(featureMatrix);

        gramMatrix = new BlockRealMatrix(N+1,N+1);

        calculateGramMatrix(instances);

    }

    int getMaxKey(List<Instance> instances){

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
        return max_key;
    }

    @Override
    public void train(List<Instance> instances) {
        double z;
        double derivative;
        BlockRealMatrix alpha_temp = new BlockRealMatrix(1,N+1);

        for(int p=0;p<gradient_ascent_training_iterations;p++) {
            for (int k = 1; k < N + 1; k++) {  // iterate over each alpha_k
                derivative = 0;
                for (int i = 1; i < N + 1; i++) { //iterate over each y_i
                    z = 0;
                    for (int j = 1; j < N + 1; j++) {
                        z = z + alpha.getEntry(0, j) * gramMatrix.getEntry(j, i);
                    }
                    derivative = derivative + Integer.parseInt(instances.get(i-1).getLabel().toString()) * g((-1*z)) * gramMatrix.getEntry(i, k) +
                            (1 - Integer.parseInt(instances.get(i-1).getLabel().toString())) * g(z) * (-1 * gramMatrix.getEntry(i, k));
                }
                double val = alpha.getEntry(0, k) + (gradient_ascent_learning_rate * derivative);
                alpha_temp.setEntry(0,k,val);
                //alpha.setEntry(0, k, val);
            }
            alpha.setRowMatrix(0,alpha_temp);
            //print_matrix(alpha);
        }
    }

    double g(double z){
        return 1/(1+Math.exp(-z));
    }

    public void makeFeatureMatrix(List<Instance> instances){
        int i=1;
        for (Instance instance : instances) {
            FeatureVector fv = instance.getFeatureVector();
            HashMap<Integer, Double> hashMapfv = fv.FeatureVector;

            for(HashMap.Entry<Integer,Double> m:hashMapfv.entrySet()) {
                featureMatrix.setEntry(i,m.getKey(),m.getValue());
            }
            i++;
        }
    }

    @Override
    public Label predict(Instance instance) {
        double val =0;
        int j;

        //System.out.print("\n"+count);

        makeNewGramMatrix(instance);

        for(j=1;j<N+1;j++){
            val = val + (alpha.getEntry(0,j)*newGramMatrix.getEntry(0,j));
        }

        count++;

        if(g(val)>=0.5){
            return new ClassificationLabel(1);
        }
        else{
            return new ClassificationLabel(0);
        }
    }

    public void makeNewGramMatrix(Instance  instance){
        if(kernel.equalsIgnoreCase("linear_kernel")) {
            int j;
            newGramMatrix = new BlockRealMatrix(1, N + 1);
            // Initialize with 0's
            //RealMatrix X = featureMatrix.getRowMatrix(count);
            BlockRealMatrix X = new BlockRealMatrix(1, cols);
            BlockRealMatrix X_dash;

            //RealMatrix X_dash;

            //Construct 1-d matrix for the test feature vector
            FeatureVector fv = instance.getFeatureVector();
            HashMap<Integer, Double> hashMapfv = fv.FeatureVector;
            for (HashMap.Entry<Integer, Double> m : hashMapfv.entrySet()) {
                X.setEntry(0, m.getKey(), m.getValue());
            }

            //BlockRealMatrix X_dash = new BlockRealMatrix(1,cols);
            for (j = 1; j < N + 1; j++) {
                //RealMatrix X_dash = featureMatrix.getRowMatrix(j).transpose();
                X_dash = featureMatrix.getRowMatrix(j).transpose();
                double determinant = new LUDecomposition(X.multiply(X_dash)).getDeterminant();
                newGramMatrix.setEntry(0, j, determinant);
            }
        }
        else if(kernel.equalsIgnoreCase("polynomial_kernel")){
            int j;
            newGramMatrix = new BlockRealMatrix(1, N + 1);
            // Initialize with 0's
            //RealMatrix X = featureMatrix.getRowMatrix(count);
            BlockRealMatrix X = new BlockRealMatrix(1, cols);
            BlockRealMatrix X_dash;

            //RealMatrix X_dash;

            //Construct 1-d matrix for the test feature vector
            FeatureVector fv = instance.getFeatureVector();
            HashMap<Integer, Double> hashMapfv = fv.FeatureVector;
            for (HashMap.Entry<Integer, Double> m : hashMapfv.entrySet()) {
                X.setEntry(0, m.getKey(), m.getValue());
            }

            //BlockRealMatrix X_dash = new BlockRealMatrix(1,cols);
            for (j = 1; j < N + 1; j++) {
                //RealMatrix X_dash = featureMatrix.getRowMatrix(j).transpose();
                X_dash = featureMatrix.getRowMatrix(j).transpose();
                double determinant = new LUDecomposition(X.multiply(X_dash)).getDeterminant();
                newGramMatrix.setEntry(0,j,Math.pow(1+ determinant,polynomial_kernel_exponent));
                //newGramMatrix.setEntry(0, j, determinant);
            }
        }
        else {
            int j,k;
            double sum;
            newGramMatrix = new BlockRealMatrix(1, N + 1);
            // Initialize with 0's
            //RealMatrix X = featureMatrix.getRowMatrix(count);
            BlockRealMatrix X = new BlockRealMatrix(1, cols);
            BlockRealMatrix X_dash;

            //RealMatrix X_dash;

            //Construct 1-d matrix for the test feature vector
            FeatureVector fv = instance.getFeatureVector();
            HashMap<Integer, Double> hashMapfv = fv.FeatureVector;
            for (HashMap.Entry<Integer, Double> m : hashMapfv.entrySet()) {
                X.setEntry(0, m.getKey(), m.getValue());
            }

            //BlockRealMatrix X_dash = new BlockRealMatrix(1,cols);
            for (j = 1; j < N + 1; j++) {
                //RealMatrix X_dash = featureMatrix.getRowMatrix(j).transpose();
                X_dash = featureMatrix.getRowMatrix(j);
                sum = 0 ;
                for(k=1;k<cols;k++){
                    sum = sum + Math.pow (X.getEntry(0,k) - X_dash.getEntry(0,k),2) ;
                }
                sum = sum * -1;
                sum = sum/(2*Math.pow(gaussian_kernel_sigma,2));
                newGramMatrix.setEntry(0,j,Math.exp(sum));
            }
        }
    }

    public void calculateGramMatrix(List<Instance> instances){
        if(kernel.equalsIgnoreCase("linear_kernel")){
            int i;
            int j;

            for(i=1;i<N+1;i++){
                //RealMatrix X = featureMatrix.getRowMatrix(i);
                BlockRealMatrix X = featureMatrix.getRowMatrix(i);
                for(j=1;j<N+1;j++){
                    //RealMatrix X_dash = featureMatrix.getRowMatrix(j).transpose();
                    BlockRealMatrix X_dash = featureMatrix.getRowMatrix(j).transpose();
                    double determinant = new LUDecomposition(X.multiply(X_dash)).getDeterminant();
                    gramMatrix.setEntry(i,j,determinant);
                }
            }
        }
        else if(kernel.equalsIgnoreCase("polynomial_kernel")){
            int i;
            int j;

            for(i=1;i<N+1;i++){
                //RealMatrix X = featureMatrix.getRowMatrix(i);
                BlockRealMatrix X = featureMatrix.getRowMatrix(i);
                for(j=1;j<N+1;j++){
                    //RealMatrix X_dash = featureMatrix.getRowMatrix(j).transpose();
                    BlockRealMatrix X_dash = featureMatrix.getRowMatrix(j).transpose();
                    double determinant = new LUDecomposition(X.multiply(X_dash)).getDeterminant();
                    gramMatrix.setEntry(i,j,Math.pow(1+ determinant,polynomial_kernel_exponent));
                    //gramMatrix.setEntry(i,j,determinant);
                }
            }
        }
        else{
            int i;
            int j;
            int k;
            double sum=0;

            for(i=1;i<N+1;i++){
                //RealMatrix X = featureMatrix.getRowMatrix(i);
                BlockRealMatrix X = featureMatrix.getRowMatrix(i);
                for(j=1;j<N+1;j++){
                    sum=0;
                    //RealMatrix X_dash = featureMatrix.getRowMatrix(j).transpose();
                    BlockRealMatrix X_dash = featureMatrix.getRowMatrix(j);
                    for(k=1;k<cols;k++){
                        sum = sum + Math.pow (X.getEntry(0,k) - X_dash.getEntry(0,k),2) ;
                    }
                    sum = sum * -1;
                    sum = sum/(2*Math.pow(gaussian_kernel_sigma,2));
                    gramMatrix.setEntry(i,j,Math.exp(sum));
                }
            }
        }
        //print_matrix(gramMatrix);
    }

    public void print_matrix(RealMatrix M){
        for(int i=0;i<M.getRowDimension();i++){
            System.out.print("\n\n\n\n");
            for(int j=1;j<M.getColumnDimension();j++){
                System.out.print(M.getEntry(i,j)+"   ");
            }
        }
    }
}
