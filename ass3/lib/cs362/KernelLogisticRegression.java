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
    BlockRealMatrix featureMatrix;
    BlockRealMatrix alpha;
    int cols;
    List<Instance> total_instances;

    public KernelLogisticRegression(List<Instance> instances,String kernel,double polynomial_kernel_exponent,
                                    double gaussian_kernel_sigma,double gradient_ascent_learning_rate,int gradient_ascent_training_iterations){
        this.kernel = kernel;
        this.polynomial_kernel_exponent = polynomial_kernel_exponent;
        this.gaussian_kernel_sigma = gaussian_kernel_sigma;
        this.gradient_ascent_learning_rate = gradient_ascent_learning_rate;
        this.gradient_ascent_training_iterations = gradient_ascent_training_iterations;
        N = instances.size();
        cols = instances.get(0).getFeatureVector().FeatureVector.size()+1;
        alpha = new BlockRealMatrix(1,N+1);
        total_instances = instances;

        int i=1;
        int j=1;

        for(i=0;i<cols;i++){
            alpha.setEntry(0,i,0);
        }

        i=1;

        featureMatrix = new BlockRealMatrix(N+1,cols);
        for (Instance instance : instances) {
            FeatureVector fv = instance.getFeatureVector();
            HashMap<Integer, Double> hashMapfv = fv.FeatureVector;

            for(HashMap.Entry<Integer,Double> m:hashMapfv.entrySet()) {
                featureMatrix.setEntry(i,j,m.getValue());
                j++;
            }
            i++;
            j=1;
        }

        //print_matrix(featureMatrix);


        gramMatrix = new BlockRealMatrix(N+1,N+1);

        calculate_gram_matrix(instances);
        /*if(kernel.equalsIgnoreCase("linear_kernel"))
            LinearKernelLogisticRegression lklr = new LinearKernelLogisticRegression(instances, kernel, polynomial_kernel_exponent, gaussian_kernel_sigma, gradient_ascent_learning_rate, gradient_ascent_training_iterations);
        else if(kernel.equalsIgnoreCase("polynomial_kernel"))
            asdsa;
        else*/


    }
    @Override
    public void train(List<Instance> instances) {
        double z;
        double derivative;

        for(int p=0;p<gradient_ascent_training_iterations;p++) {
            for (int k = 1; k < N + 1; k++) {  // iterate over each alpha_k
                derivative = 0;
                for (int i = 1; i < N + 1; i++) { //iterate over each y_i
                    z = 0;
                    for (int j = 1; j < N + 1; j++) {
                        z = z + alpha.getEntry(0, j) * gramMatrix.getEntry(j, i);
                    }
                    derivative = derivative + Integer.parseInt(instances.get(i-1).getLabel().toString()) * g(-z) * gramMatrix.getEntry(i, k) +
                            (1 - Integer.parseInt(instances.get(i-1).getLabel().toString())) * g(z) * (-1 * gramMatrix.getEntry(i, k));
                }
                double val = alpha.getEntry(0, k) + (gradient_ascent_learning_rate * derivative);
                alpha.setEntry(0, k, val);
            }
            //print_matrix(alpha);
        }
    }

    double g(double z){
        return 1/(1+Math.exp(-z));
    }

    @Override
    public Label predict(Instance instance) {
        double val =0;
        int j=1;
        BlockRealMatrix currentFeatureVector = new BlockRealMatrix(1,N+1);
        int required_index=-1;

        for(j=0;j<total_instances.size();j++){
            if(instance.equals(total_instances.get(j))){
                required_index = j+1;
            }
        }

        /*FeatureVector fv = instance.getFeatureVector();
        HashMap<Integer, Double> hashMapfv = fv.FeatureVector;

        for(HashMap.Entry<Integer,Double> m:hashMapfv.entrySet()) {
            currentFeatureVector.setEntry(0,j,m.getValue());
            j++;
        }

        print_matrix(currentFeatureVector);

        for(j=1;j<N+1;j++){
            if(Arrays.equals(featureMatrix.getRow(j),currentFeatureVector.getRow(0))){
                required_index = j;
                break;
            }
        }*/

        if(required_index==-1)
            System.out.print("\n\n\n\n\n\nMa chudva lo");

        for(j=1;j<N+1;j++){
            val = val + (alpha.getEntry(0,j)*gramMatrix.getEntry(j,required_index));
        }

        if(g(val)>=0.5){
            return new ClassificationLabel(1);
        }
        else{
            return new ClassificationLabel(0);
        }
    }

    public void calculate_gram_matrix(List<Instance> instances){
        if(kernel.equalsIgnoreCase("linear_kernel")){
            int i=1;
            int j=1;

            for(i=1;i<N+1;i++){
                for(j=1;j<N+1;j++){
                    RealMatrix X = featureMatrix.getSubMatrix(i,i,1,cols-1);

                    RealMatrix X_dash = featureMatrix.getSubMatrix(j,j,1,cols-1).transpose();

                    double determinant = new LUDecomposition(X.multiply(X_dash)).getDeterminant();
                    gramMatrix.setEntry(i,j,determinant);
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
