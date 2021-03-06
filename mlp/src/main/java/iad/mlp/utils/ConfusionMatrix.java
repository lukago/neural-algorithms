package iad.mlp.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 6/11/2017.
 */
public class ConfusionMatrix {

    public int truePositivesTotal;
    public int falseNegativesTotal;
    public int falsePositivesTotal;
    private double[][] out;
    private double[][] expectedOut;
    private int truePositives;
    private int falseNegatives;
    private int falsePositives;
    private int trueNegatives;
    private List<List<Integer>> classFN;
    private List<List<Integer>> classFP;

    public ConfusionMatrix(double[][] out, double[][] expectedOut) {
        this.out = out;
        this.expectedOut = expectedOut;

        classFN = new ArrayList<>();
        for (int i = 0; i < out[0].length; i++) {
            classFN.add(new ArrayList<>());
        }

        classFP = new ArrayList<>();
        for (int i = 0; i < out[0].length; i++) {
            classFP.add(new ArrayList<>());
        }

        calcClass();
    }

    private int calcAllClassLabels(int id) {
        int sum = 0;
        for (int i = 0; i < expectedOut.length; i++) {
            if (MLPUtils.maxId(expectedOut[i]) == id) {
                sum++;
            }
        }
        return sum;
    }

    public void calcClass() {
        for (int i = 0; i < out.length; i++) {
            if (MLPUtils.maxId(out[i]) != MLPUtils.maxId(expectedOut[i])) {
                classFN.get(MLPUtils.maxId(expectedOut[i])).add(i);
            }
        }

        for (int i = 0; i < classFN.size(); i++) {
            for (int j = 0; j < classFN.get(i).size(); j++) {
                int id = MLPUtils.maxId(out[classFN.get(i).get(j)]);
                classFP.get(id).add(classFN.get(i).get(j));
            }
        }

        for (int i = 0; i < out[0].length; i++) {
            sum(i);
            calcTotal();
        }
    }

    public void sum(int id) {
        falseNegatives = classFN.get(id).size();
        falsePositives = classFP.get(id).size();

        truePositives = calcAllClassLabels(id) - falseNegatives;
        trueNegatives =
                expectedOut.length - truePositives - falsePositives - falseNegatives;
    }

    public void calcTotal() {
        falseNegativesTotal += falseNegatives;
        falsePositivesTotal += falsePositives;
        truePositivesTotal += truePositives;
    }

    public void writeClassErrorMatrix(String filepath) {

        try (FileWriter ostream = new FileWriter(filepath)) {
            for (int i = 0; i < classFN.size(); i++) {
                sum(i);

                ostream.write("\nKlasa: " + i);
                ostream.write(" TP: " + truePositives);
                ostream.write(" FN: " + falseNegatives);
                //for (int j = 0; j < classFN.get(i).size(); j++) {
                //    ostream.write("\tid: " + classFN.get(i).get(j));
                //}
                ostream.write(" FP: " + falsePositives);
                //for (int j = 0; j < classFP.get(i).size(); j++) {
                //    ostream.write("\tid: " + classFP.get(i).get(j));
                //}
                ostream.write(" TN: " + trueNegatives);
            }

            ostream.write("\nSuma: ");
            ostream.write(" TP: " + truePositivesTotal);
            ostream.write(" FN: " + falseNegativesTotal);
            ostream.write(" FP: " + falsePositivesTotal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
