package cs362;

import jdk.nashorn.internal.ir.Block;
import org.apache.commons.math3.linear.*;

import java.lang.reflect.Array;
import java.util.*;

public class OpinionPrediction extends Predictor {

    HashMap<Integer,ArrayList<MyPair>> history = new HashMap<>();
    double omega;
    HashMap<Integer,BlockRealMatrix> ghmap = new HashMap<>();
    HashMap<Integer,BlockRealMatrix> Yhmap = new HashMap<>();
    BlockRealMatrix alpha;
    BlockRealMatrix A;
    int total_users;



    //public OpinionPrediction(List<Instance> instances){
    public OpinionPrediction(OpinionData opinionData){

        this.omega = 1.0;
         for(int i=0;i<opinionData.instances.size();i++){
             int key = opinionData.instances.get(i).getFeatureVector().getUid();
             MyPair mp = new MyPair(key,opinionData.instances.get(i).getFeatureVector().getSentiment(),opinionData.instances.get(i).getFeatureVector().getTime());
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

        for (HashMap.Entry<Integer, ArrayList<MyPair>> m : history.entrySet()) {
           if(!opinionData.OpGraph.containsKey(m.getKey())){
               opinionData.OpGraph.put(m.getKey(),new ArrayList<Integer>());
           }
           Collections.sort(m.getValue());
        }

        //printHistory(opinionData.OpGraph);
    }

    void printHistory(HashMap<Integer,ArrayList<Integer>> OpGraph){
        for (HashMap.Entry<Integer, ArrayList<MyPair>> m : history.entrySet()) {
            System.out.print(m.getKey()+":");
            ArrayList<MyPair> amp = m.getValue();
            for(int i = 0; i < amp.size(); i++) {
                System.out.print(" "+amp.get(i).getuid());
                System.out.print(" "+amp.get(i).getSentiment());
                System.out.print(" "+amp.get(i).getTime());
                System.out.print(" || ");
            }
            System.out.print("\n");
        }

        System.out.print("Graph\n");
        for (HashMap.Entry<Integer, ArrayList<Integer>> m : OpGraph.entrySet()) {
            System.out.print(m.getKey()+":");
            ArrayList<Integer> amp = m.getValue();
            for(int i = 0; i < amp.size(); i++) {
                System.out.print(" "+amp.get(i));
            }
            System.out.print("\n");
        }


    }

    void printArraylist(ArrayList<MyPair> merged){
        for(int j = 0; j < merged.size(); j++) {
            System.out.print(" "+merged.get(j).getuid());
            System.out.print(" "+merged.get(j).getSentiment());
            System.out.print(" "+merged.get(j).getTime());
            System.out.print(" || ");
        }
    }

    public RealMatrix inferOpinionParams(BlockRealMatrix Y, BlockRealMatrix g,double lambda){

        int s = g.getRowDimension();

        BlockRealMatrix L = (g.transpose()).multiply(g);

        //BlockRealMatrix I = (BlockRealMatrix) MatrixUtils.createRealIdentityMatrix(s);
        RealMatrix I = MatrixUtils.createRealIdentityMatrix(total_users+1);

        RealMatrix lambdaXI = I.scalarMultiply(lambda);
        //print_Rmatrix(lambdaXI);

        RealMatrix temp = lambdaXI.add(L);


        RealMatrix temp_inverse = new LUDecomposition(temp).getSolver().getInverse();

        //print_Rmatrix(temp_inverse);

        RealMatrix x = temp_inverse.multiply(g.transpose());
        x = x.multiply(Y);

        return x;
    }

    public void estimateAlphaA(OpinionData opinionData){
        for (HashMap.Entry<Integer, ArrayList<Integer>> m : opinionData.OpGraph.entrySet()) {
            int each_user = m.getKey();
            ArrayList<Integer> neigh_list_of_each_user = new ArrayList<>();
            neigh_list_of_each_user = m.getValue();

            BlockRealMatrix g = new BlockRealMatrix(history.get(each_user).size(),history.size()+1);
            BlockRealMatrix Y = new BlockRealMatrix(history.get(each_user).size(),1);


            for(int i=0;i<neigh_list_of_each_user.size();i++){
                ArrayList<MyPair> merged = new ArrayList<>();
                merged.addAll(history.get(each_user));

                int neighbour_id = neigh_list_of_each_user.get(i);
                ArrayList<MyPair> blah = history.get(neighbour_id);
                merged.addAll(blah);

                Collections.sort(merged);

                //printArraylist(merged);

                double opinion_last=0;
                int t_last = 0;
                int j=0;

                for(int k=0;k<merged.size();k++){
                    int l_uid = merged.get(k).getuid();
                    double l_senti = merged.get(k).getSentiment();
                    int l_time = merged.get(k).getTime();

                    int t_now= l_time;
                    double opinion_now;
                    if(each_user==l_uid){
                        opinion_now = opinion_last*Math.exp(-1*this.omega*(t_now-t_last));
                        //System.out.print("\nIf opinion_now: "+opinion_now);
                        //g.setEntry(j,l_uid,opinion_now);
                        g.setEntry(j,neighbour_id,opinion_now);
                        g.setEntry(j,0,1);
                        Y.setEntry(j,0,l_senti);
                        j++;
                    }
                    else{
                        opinion_now = opinion_last*Math.exp(-1*this.omega*(t_now-t_last))+ l_senti;
                        //System.out.print("\nElse opinion_now: "+opinion_now);
                    }

                    t_last = t_now;
                    opinion_last = opinion_now;
                }

                //System.out.print(" \n ");
            }

            ghmap.put(each_user,g);
            //System.out.print("\n\n*&^%$$%^&%$^&");
            //print_matrix(g);
            Yhmap.put(each_user,Y);
            //System.out.print("\n\n*&^%$$%^&%$^&");
            //print_matrix(Y);
        }

        total_users = history.size();
        alpha = new BlockRealMatrix(1,total_users);
        A  = new BlockRealMatrix(total_users,total_users);

        for (HashMap.Entry<Integer, ArrayList<Integer>> m : opinionData.OpGraph.entrySet()) {
            int each_user = m.getKey();
            RealMatrix a = inferOpinionParams(Yhmap.get(each_user),ghmap.get(each_user),0.5);

            alpha.setEntry(0,each_user-1,a.getEntry(0,0));
            A.setColumnMatrix(each_user-1,a.getSubMatrix(1,total_users,0,0));



        }

        print_matrix(alpha);
        //print_matrix(A);


    }


    public void print_matrix(BlockRealMatrix M){
        for(int i=0;i<M.getRowDimension();i++){
            System.out.print("\n");
            for(int j=0;j<M.getColumnDimension();j++){
                System.out.print(M.getEntry(i,j)+"   ");
            }
        }
    }

    public void print_Rmatrix(RealMatrix M){
        for(int i=0;i<M.getRowDimension();i++){
            System.out.print("\n");
            for(int j=0;j<M.getColumnDimension();j++){
                System.out.print(M.getEntry(i,j)+"   ");
            }
        }
    }


    @Override
    public void train(OpinionData opinionData) {
        estimateAlphaA(opinionData);

        //Calculate max time
        int max_time = 0;
        for (HashMap.Entry<Integer, ArrayList<MyPair>> m : history.entrySet()) {
            int size_of_arraylist = m.getValue().size();
            int current_time = m.getValue().get(size_of_arraylist-1).getTime();
            if(current_time>max_time){
                max_time = current_time;
            }
        }

        //Initialize mu(initial intensities) for each user
        BlockRealMatrix mu = new BlockRealMatrix(1,total_users);
        Random rand = new Random();
        for(int i=0;i<total_users;i++){
            mu.setEntry(0,i,rand.nextDouble());
        }

        OpinionModelSimulation(max_time+60000,mu/*beta,*/);
    }

    public void OpinionModelSimulation(int T,BlockRealMatrix mu){
        BlockRealMatrix LastOpinionUpdateValue = alpha;
        BlockRealMatrix  LastOpinionUpdateTime = new BlockRealMatrix(1,total_users);

        BlockRealMatrix LastIntensityUpdateValue = mu;
        BlockRealMatrix LastIntensityUpdateTime = new BlockRealMatrix(1,total_users);

        ArrayList<MyPair> H = new ArrayList<>();

        for (HashMap.Entry<Integer, ArrayList<MyPair>> m : history.entrySet()) {
            int cur_user = m.getKey();
            //int post_time = SampleEvent(mu.getEntry(0,cur_user),0,cur_user);
            double post_time = SampleEvent(mu.getEntry(0,cur_user),0,cur_user,T);
            //H.add(new MyPair(cur_user,0.0,post_time));
        }

        Collections.sort(H);

    }

    //public int SampleEvent(double lambda, int t,int v){
    public double SampleEvent(double lambda, int t,int v,int T){

        double lambda_ = lambda;
        double s = (double)t;
        Random rand = new Random();
        while(s<T){
            double u = rand.nextDouble();
            s = s - (Math.log(u)/lambda_);

        }

        return s;
    }

    @Override
    public Label predict(Instance instance) {
        return null;
    }

    public class MyPair implements Comparable
    {
        private  int uid;
        private double sentiment;
        private int time;


        public MyPair(int uid, double sentiment, int time)
        {
            this.uid = uid;
            this.sentiment = sentiment;
            this.time = time ;
        }

        public double getSentiment()   { return this.sentiment; }
        public int getTime() { return this.time; }
        public int getuid() {return this.uid;}

        @Override
        public int compareTo(Object mp2) {
            int comp = ((MyPair)mp2).getTime();
            return this.getTime() - comp;
        }
    }
}
