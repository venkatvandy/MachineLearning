package cs362.image_denoise;

import java.util.Collections;
import java.util.HashMap;

public class MRFImageProcessor {

    double eta;
    double beta;
    int num_iterations;

    public MRFImageProcessor(double eta, double beta, int num_iterations){
        this.eta = eta;
        this.beta = beta;
        this.num_iterations = num_iterations;
    }

    int[][] denoisifyImage(int[][] image1,int[][] image2){

        int[][] original_image = image1;
        int[][] hidden_nodes = image1;
        int[][] temp_output = new int[original_image.length][original_image[0].length];

        HashMap<Integer, Double> unique_colors = new HashMap<Integer, Double>();
        for(int i=0;i<original_image.length;i++){
            for(int j=0;j<original_image[i].length;j++){
                unique_colors.put(original_image[i][j],0.0);
            }
        }

        if(unique_colors.size()==2){
        for (int k = 0; k < num_iterations; k++) {
                for (int i = 0; i < hidden_nodes.length; i++) {
                    for (int j = 0; j < hidden_nodes[i].length; j++) {

                        for(HashMap.Entry<Integer,Double> m:unique_colors.entrySet()) {
                            if (i == 0 || i == hidden_nodes.length - 1 || j == 0 || j == hidden_nodes[i].length - 1) { //boundary conditions

                                //corners
                                if(i==0 && j==0) {
                                    if (m.getKey() == hidden_nodes[i][j + 1]) {
                                        unique_colors.put(m.getKey(), m.getValue() + (-1 * beta));
                                    } else {
                                        unique_colors.put(m.getKey(), m.getValue() + beta);
                                    }

                                    if (m.getKey() == hidden_nodes[i + 1][j]) {
                                        unique_colors.put(m.getKey(), m.getValue() + (-1 * beta));
                                    } else {
                                        unique_colors.put(m.getKey(), m.getValue() + beta);
                                    }
                                }

                                if(i==0 && j==hidden_nodes[i].length-1){
                                    if (m.getKey() == hidden_nodes[i][j - 1]) {
                                        unique_colors.put(m.getKey(), m.getValue() + (-1 * beta));
                                    } else {
                                        unique_colors.put(m.getKey(), m.getValue() + beta);
                                    }

                                    if (m.getKey() == hidden_nodes[i + 1][j]) {
                                        unique_colors.put(m.getKey(), m.getValue() + (-1 * beta));
                                    } else {
                                        unique_colors.put(m.getKey(), m.getValue() + beta);
                                    }
                                }
                                if(i==hidden_nodes.length-1 && j==0){
                                    if (m.getKey() == hidden_nodes[i-1][j]) {
                                        unique_colors.put(m.getKey(), m.getValue() + (-1 * beta));
                                    } else {
                                        unique_colors.put(m.getKey(), m.getValue() + beta);
                                    }

                                    if (m.getKey() == hidden_nodes[i][j+1]) {
                                        unique_colors.put(m.getKey(), m.getValue() + (-1 * beta));
                                    } else {
                                        unique_colors.put(m.getKey(), m.getValue() + beta);
                                    }
                                }
                                if(i==hidden_nodes.length-1 && j==hidden_nodes[i].length-1){
                                    if (m.getKey() == hidden_nodes[i][j - 1]) {
                                        unique_colors.put(m.getKey(), m.getValue() + (-1 * beta));
                                    } else {
                                        unique_colors.put(m.getKey(), m.getValue() + beta);
                                    }

                                    if (m.getKey() == hidden_nodes[i - 1][j]) {
                                        unique_colors.put(m.getKey(), m.getValue() + (-1 * beta));
                                    } else {
                                        unique_colors.put(m.getKey(), m.getValue() + beta);
                                    }

                                }
                                //corners end

                                //1st row
                                if(i==0 && j>0 && j<hidden_nodes[i].length-1) {
                                    if (m.getKey() == hidden_nodes[i][j - 1]) {
                                        unique_colors.put(m.getKey(), m.getValue() + (-1 * beta));
                                    } else {
                                        unique_colors.put(m.getKey(), m.getValue() + beta);
                                    }

                                    if (m.getKey() == hidden_nodes[i][j+1]) {
                                        unique_colors.put(m.getKey(), m.getValue() + (-1 * beta));
                                    } else {
                                        unique_colors.put(m.getKey(), m.getValue() + beta);
                                    }

                                    if (m.getKey() == hidden_nodes[i + 1][j]) {
                                        unique_colors.put(m.getKey(), m.getValue() + (-1 * beta));
                                    } else {
                                        unique_colors.put(m.getKey(), m.getValue() + beta);
                                    }
                                }


                                //last row
                                if(i==hidden_nodes.length-1 && j>0 && j<hidden_nodes[i].length-1) {
                                    if (m.getKey() == hidden_nodes[i][j - 1]) {
                                        unique_colors.put(m.getKey(), m.getValue() + (-1 * beta));
                                    } else {
                                        unique_colors.put(m.getKey(), m.getValue() + beta);
                                    }

                                    if (m.getKey() == hidden_nodes[i][j+1]) {
                                        unique_colors.put(m.getKey(), m.getValue() + (-1 * beta));
                                    } else {
                                        unique_colors.put(m.getKey(), m.getValue() + beta);
                                    }

                                    if (m.getKey() == hidden_nodes[i - 1][j]) {
                                        unique_colors.put(m.getKey(), m.getValue() + (-1 * beta));
                                    } else {
                                        unique_colors.put(m.getKey(), m.getValue() + beta);
                                    }
                                }

                                //1st column
                                if(j==0 && i>0 && i<hidden_nodes.length-1) {
                                    if (m.getKey() == hidden_nodes[i-1][j]) {
                                        unique_colors.put(m.getKey(), m.getValue() + (-1 * beta));
                                    } else {
                                        unique_colors.put(m.getKey(), m.getValue() + beta);
                                    }

                                    if (m.getKey() == hidden_nodes[i + 1][j]) {
                                        unique_colors.put(m.getKey(), m.getValue() + (-1 * beta));
                                    } else {
                                        unique_colors.put(m.getKey(), m.getValue() + beta);
                                    }

                                    if (m.getKey() == hidden_nodes[i][j+1]) {
                                        unique_colors.put(m.getKey(), m.getValue() + (-1 * beta));
                                    } else {
                                        unique_colors.put(m.getKey(), m.getValue() + beta);
                                    }
                                }


                                //last column
                                if(j==hidden_nodes[i].length-1 && i>0 && i<hidden_nodes.length-1) {
                                    if (m.getKey() == hidden_nodes[i-1][j]) {
                                        unique_colors.put(m.getKey(), m.getValue() + (-1 * beta));
                                    } else {
                                        unique_colors.put(m.getKey(), m.getValue() + beta);
                                    }

                                    if (m.getKey() == hidden_nodes[i + 1][j]) {
                                        unique_colors.put(m.getKey(), m.getValue() + (-1 * beta));
                                    } else {
                                        unique_colors.put(m.getKey(), m.getValue() + beta);
                                    }

                                    if (m.getKey() == hidden_nodes[i][j-1]) {
                                        unique_colors.put(m.getKey(), m.getValue() + (-1 * beta));
                                    } else {
                                        unique_colors.put(m.getKey(), m.getValue() + beta);
                                    }
                                }

                                if (m.getKey() == original_image[i][j]) {
                                    unique_colors.put(m.getKey(), m.getValue() + (-1 * eta));
                                }
                                else {
                                    unique_colors.put(m.getKey(), m.getValue() + eta);
                                }

                            } else { //big else
                                if (m.getKey() == hidden_nodes[i][j - 1]) {
                                    unique_colors.put(m.getKey(), m.getValue() + (-1 * beta));
                                } else {
                                    unique_colors.put(m.getKey(), m.getValue() + beta);
                                }
                                if (m.getKey() == hidden_nodes[i][j + 1]) {
                                    unique_colors.put(m.getKey(), m.getValue() + (-1 * beta));
                                } else {
                                    unique_colors.put(m.getKey(), m.getValue() + beta);
                                }
                                if (m.getKey() == hidden_nodes[i - 1][j]) {
                                    unique_colors.put(m.getKey(), m.getValue() + (-1 * beta));
                                } else {
                                    unique_colors.put(m.getKey(), m.getValue() + beta);
                                }
                                if (m.getKey() == hidden_nodes[i + 1][j]) {
                                    unique_colors.put(m.getKey(), m.getValue() + (-1 * beta));
                                } else {
                                    unique_colors.put(m.getKey(), m.getValue() + beta);
                                }

                                if (m.getKey() == original_image[i][j]) {
                                    unique_colors.put(m.getKey(), m.getValue() + (-1 * eta));
                                } else {
                                    unique_colors.put(m.getKey(), m.getValue() + eta);
                                }
                            } //else finished
                        } //for finished

                        double min_val = 20000;
                        int required_color=0;

                        for(HashMap.Entry<Integer,Double> m:unique_colors.entrySet()) {
                            if(m.getValue()<min_val){
                                min_val = m.getValue();
                                required_color = m.getKey();
                            }
                        }

                        temp_output[i][j] = required_color;

                        //zero out hashmap
                        for(HashMap.Entry<Integer,Double> m:unique_colors.entrySet()) {
                            unique_colors.put(m.getKey(),0.0);
                        }
                    }
                }
                hidden_nodes = temp_output;
        }}
        else { //biggest else
            for (int k = 0; k < num_iterations; k++) {
                for (int i = 0; i < hidden_nodes.length; i++) {
                    for (int j = 0; j < hidden_nodes[i].length; j++) {

                        for(HashMap.Entry<Integer,Double> m:unique_colors.entrySet()) {
                            if (i == 0 || i == hidden_nodes.length - 1 || j == 0 || j == hidden_nodes[i].length - 1) { //boundary conditions
                                //corners
                                if(i==0 && j==0) {
                                    unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-hidden_nodes[i][j + 1])+1) -1 )*beta);

                                    unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-hidden_nodes[i + 1][j])+1) -1 )*beta);

                                }

                                if(i==0 && j==hidden_nodes[i].length-1){

                                    unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-hidden_nodes[i][j - 1])+1) -1 )*beta);

                                    unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-hidden_nodes[i + 1][j])+1) -1 )*beta);

                                }

                                if(i==hidden_nodes.length-1 && j==0){

                                    unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-hidden_nodes[i - 1][j])+1) -1 )*beta);

                                    unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-hidden_nodes[i][j + 1])+1) -1 )*beta);

                                }
                                if(i==hidden_nodes.length-1 && j==hidden_nodes[i].length-1){

                                    unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-hidden_nodes[i][j - 1])+1) -1 )*beta);

                                    unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-hidden_nodes[i - 1][j])+1) -1 )*beta);

                                }
                                //corners end

                                //1st row
                                if(i==0 && j>0 && j<hidden_nodes[i].length-1) {

                                    unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-hidden_nodes[i][j - 1])+1) -1 )*beta);

                                    unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-hidden_nodes[i][j + 1])+1) -1 )*beta);

                                    unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-hidden_nodes[i + 1][j])+1) -1 )*beta);

                                }


                                //last row
                                if(i==hidden_nodes.length-1 && j>0 && j<hidden_nodes[i].length-1) {

                                    unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-hidden_nodes[i][j - 1])+1) -1 )*beta);

                                    unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-hidden_nodes[i][j + 1])+1) -1 )*beta);

                                    unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-hidden_nodes[i -1][j])+1) -1 )*beta);
                                }

                                //1st column
                                if(j==0 && i>0 && i<hidden_nodes.length-1) {

                                    unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-hidden_nodes[i - 1][j])+1) -1 )*beta);

                                    unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-hidden_nodes[i + 1][j])+1) -1 )*beta);

                                    unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-hidden_nodes[i][j + 1])+1) -1 )*beta);

                                }


                                //last column
                                if(j==hidden_nodes[i].length-1 && i>0 && i<hidden_nodes.length-1) {

                                    unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-hidden_nodes[i - 1][j])+1) -1 )*beta);

                                    unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-hidden_nodes[i + 1][j])+1) -1 )*beta);

                                    unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-hidden_nodes[i][j - 1])+1) -1 )*beta);

                                }

                                unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-original_image[i][j])+1) -1 )*eta);

                            } else { //big else

                                unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-hidden_nodes[i][j - 1])+1) -1 )*beta);

                                unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-hidden_nodes[i][j + 1])+1) -1 )*beta);

                                unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-hidden_nodes[i - 1][j])+1) -1 )*beta);

                                unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-hidden_nodes[i + 1][j])+1) -1 )*beta);

                                unique_colors.put(m.getKey(),m.getValue()+(Math.log(Math.abs(m.getKey()-original_image[i][j])+1) -1 )*eta);

                            } //else finished
                        } //for finished

                        double min_val = 20000;
                        int required_color=0;

                        for(HashMap.Entry<Integer,Double> m:unique_colors.entrySet()) {
                            if(m.getValue()<min_val){
                                min_val = m.getValue();
                                required_color = m.getKey();
                            }
                        }

                        temp_output[i][j] = required_color;

                        //zero out hashmap
                        for(HashMap.Entry<Integer,Double> m:unique_colors.entrySet()) {
                            unique_colors.put(m.getKey(),0.0);
                        }

                    }
                }
                hidden_nodes = temp_output;
            }
        }

        return hidden_nodes;
    }
}
