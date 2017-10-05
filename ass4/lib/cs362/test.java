package cs362;

import jdk.nashorn.internal.ir.BlockLexicalContext;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

public class test {



    public static void main(String args[]) {


        Random rand = new Random();
        BlockRealMatrix b = new BlockRealMatrix(10,16);
        for(int i=1;i<10;i++){
            for(int j=1;j<16;j++){
                b.setEntry(i,j, rand.nextInt(50));
            }
        }

        b.getRowVector(1).add()


        /*
        static HashMap<Integer, String> hmap = new HashMap<Integer, String>();

        hmap.put(12, "Chaitanya");
        hmap.put(2, "Rahul");
        hmap.put(7, "Singh");
        hmap.put(49, "Ajeet");
        hmap.put(3, "Anuj");
        hmap.put(3,"Venki");



        //int maxKey = Collections.max(hmap.keySet());
        System.out.print(hmap.get(3));*/

        /*BlockRealMatrix b = new BlockRealMatrix(10,16);
        for(int i=1;i<10;i++){
            for(int j=1;j<16;j++){
                b.setEntry(i,j,1);
            }
        }

        RealVector v = new ArrayRealVector(16);


        for(int i=1;i<16;i++){
            v.setEntry(i,i);
        }

        for(int i=0;i<16;i++){
            System.out.print(v.getEntry(i)+ "  ");
        }*/



    }
}
