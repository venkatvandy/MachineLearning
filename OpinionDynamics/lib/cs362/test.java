package cs362;

import jdk.nashorn.internal.ir.BlockLexicalContext;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class test {

    public static void main(String args[]) {
        Scanner senti_scanner=null;
        try {
            senti_scanner = new Scanner(new BufferedInputStream(new FileInputStream("Sentiment_MovieTwitter.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int total_count=0;
        while (senti_scanner.hasNextLine()) {
            total_count++;
            System.out.println(total_count);
        }
    }
}