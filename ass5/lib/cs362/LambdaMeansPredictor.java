package cs362;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.*;

public class LambdaMeansPredictor extends Predictor {

    double cluster_lambda;
    int clustering_training_iterations;
    public BlockRealMatrix featureMatrix;
    //Vector<Double> mean;
    RealVector mean;
    //Vector<Integer> clusterIndicator;
    RealVector clusterIndicator;
    int rows;
    int cols;
    int cluster_size;
    //HashMap<Integer,Vector<Double>> cluster;
    HashMap<Integer,RealVector> cluster;

    public LambdaMeansPredictor(List<Instance> instances,double cluster_lambda,int clustering_training_iterations){

        rows = instances.size() +1 ;
        cols = getMaxKey(instances) +1 ;
        this.cluster_size =1;

        featureMatrix = new BlockRealMatrix(rows,cols);
        makeFeatureMatrix(instances);

        calculate_mean_of_all_instances(instances);
        if(cluster_lambda==0){  //since default value passed is 0 we set our own default value
            this.cluster_lambda = calculate_lambda();
        }
        else{
            this.cluster_lambda = cluster_lambda;
        }
        this.clustering_training_iterations = clustering_training_iterations;

        initialize_cluster();
        
    }

    private void initialize_cluster() {
        cluster = new HashMap<>();
        cluster.put(1,mean);
        //clusterIndicator.add(0,-1);
        clusterIndicator = new ArrayRealVector(rows);
    }

    private double calculate_lambda() {
        double sum=0;
        for(int i=1;i<rows;i++){
            for(int j=1;j<cols;j++){
                //sum+=Math.pow(featureMatrix.getEntry(i,j) - mean.get(j),2);
                sum+=Math.pow(featureMatrix.getEntry(i,j) - mean.getEntry(j),2);
            }
        }
        return(sum/(rows-1));
    }

    int getMaxKey(List<Instance> instances){

        int max_key = 0;
        for (Instance instance : instances) {
            FeatureVector fv = instance.getFeatureVector();
            HashMap<Integer, Double> hashMapfv = fv.FeatureVector;

            int new_key = Collections.max(hashMapfv.keySet());
            if(new_key>max_key)
                max_key=new_key;
        }
        return max_key;
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
    public void train(List<Instance> instances) {
        for(int k=0;k<clustering_training_iterations;k++){

            //E step
            for(int i=1;i<rows;i++){

                RealVector temp = featureMatrix.getRowVector(i);
                double min_distance = getSquaredEuclideanDistance(temp,cluster.get(1));
                int cluster_index=1;

                //for(HashMap.Entry<Integer,Vector<Double>> m:cluster.entrySet()) {
                for(HashMap.Entry<Integer,RealVector> m:cluster.entrySet()) {
                    double distance = getSquaredEuclideanDistance(temp,m.getValue());
                    if(distance<min_distance){
                        min_distance=distance;
                        cluster_index = m.getKey();
                    }
                }
                if(min_distance < cluster_lambda){
                    clusterIndicator.setEntry(i,cluster_index);
                }
                else {
                    int new_cluster_index = addNewCluster(temp);
                    clusterIndicator.setEntry(i,new_cluster_index);
                }
            }

            // M-Step Update the prototype vector for each cluster

            for(HashMap.Entry<Integer,RealVector> m:cluster.entrySet()) {
                RealVector prototype_vec = new ArrayRealVector(cols);
                int count = 0;
                for(int i=1;i<rows;i++){
                    if(clusterIndicator.getEntry(i)==m.getKey()){ //the instance belongs to this cluster
                            count++;
                            prototype_vec = prototype_vec.add(featureMatrix.getRowVector(i));
                    }
                }
                if(count!=0) // only if cluster is not empty
                    prototype_vec.mapDivideToSelf(count);
                //Update the prototype vector
                cluster.put(m.getKey(),prototype_vec); //if cluster empty prototype_vec will be 0
            }
        }

        //print_cluster();
    }

    private void print_cluster() {
        List<Double> list1 = new ArrayList<>();
        for(int i=0;i<clusterIndicator.toArray().length;i++){
            list1.add(clusterIndicator.toArray()[i]);
        }
        for (HashMap.Entry<Integer, RealVector> m : cluster.entrySet()) {
            int freq = Collections.frequency(list1,m.getKey().doubleValue());
            System.out.print("Frequency of cluster "+m.getKey()+ " is :"+ freq+"\n");
        }
    }

    private int addNewCluster(RealVector v) {
        int new_key = Collections.max(cluster.keySet())+1;
        cluster.put(new_key,v);
        return new_key;
    }

    //private double getSquaredEuclidianDistance(RealVector temp, Vector<Double> value) {
    private double getSquaredEuclideanDistance(RealVector temp, RealVector value) {
        double sum=0;
        for(int i =1;i<cols;i++){
            sum+= Math.pow(temp.getEntry(i)-value.getEntry(i),2);
        }
        return sum;
    }

    @Override
    public Label predict(Instance instance) {

        //Construct a RealVector from the instance to be predicted.
        RealVector currentVector = new ArrayRealVector(cols);
        FeatureVector fv = instance.getFeatureVector();
        HashMap<Integer, Double> hashMapfv = fv.FeatureVector;
        for(HashMap.Entry<Integer,Double> m:hashMapfv.entrySet()) {
            //featureMatrix.setEntry(i,m.getKey(),m.getValue());
            currentVector.setEntry(m.getKey(),m.getValue());
        }

        int required_cluster_index = 1;
        double min_distance = getSquaredEuclideanDistance(currentVector,cluster.get(1));
        for (HashMap.Entry<Integer, RealVector> m : cluster.entrySet()) {
            double new_distance = getSquaredEuclideanDistance(currentVector,m.getValue());
            if(new_distance<min_distance){
                min_distance = new_distance;
                required_cluster_index = m.getKey();
            }
        }

        return new ClassificationLabel(required_cluster_index);
    }

    public void calculate_mean_of_all_instances(List<Instance> instances){
        int i,j;
        double sum=0;
        //mean = new Vector<>(16);
        mean = new ArrayRealVector(cols);
        for(i=1;i<cols;i++){
            sum=0;
            for(j=1;j<rows;j++){
                sum+=featureMatrix.getEntry(j,i);
            }
            sum = sum/(rows-1);
            //mean.add(i,sum);
            mean.setEntry(i,sum);
        }
    }
}