package iad.mlp.utils;

import iad.mlp.mlp.Layer;
import iad.mlp.mlp.MultiLayerPerceptron;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 6/10/2017.
 */
public final class IOUtils {

    private IOUtils() {
    }

    public static void writeVector(String filepath, double[] values) {
        try (FileWriter ostream = new FileWriter(filepath)) {
            for (int i = 0; i < values.length; i++) {
                ostream.write(i + "\t" + values[i] + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeMatrix(String filepath, double values[][]) {
        try (FileWriter ostream = new FileWriter(filepath)) {
            for (int i = 0; i < values.length; i++) {
                ostream.write(i + "\t");
                for (int j = 0; j < values[i].length; j++) {
                    String s = String.format("%.6f", values[i][j]);
                    ostream.write(s + "\t");
                }
                ostream.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double[][] readMatrix(String filepath, String regex) {
        List<double[]> tmpMatrix = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filepath));
            for (String line : lines) {
                String[] rowStr = line.split(regex);
                double[] row = new double[rowStr.length];
                for (int i = 0; i < rowStr.length; i++) {
                    row[i] = (Double.parseDouble(rowStr[i]));
                }
                tmpMatrix.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        double[][] ret = new double[tmpMatrix.size()][];
        for (int i = 0; i < tmpMatrix.size(); i++) {
            ret[i] = tmpMatrix.get(i);
        }

        return ret;
    }

    public static double[] readVector(String filepath) {
        List<Double> tmpMatrix = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(filepath)))) {
            String line;
            while ((line = br.readLine()) != null) {
                tmpMatrix.add(Double.parseDouble(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        double[] ret = new double[tmpMatrix.size()];
        for (int i = 0; i < tmpMatrix.size(); i++) {
            ret[i] = tmpMatrix.get(i);
        }

        return ret;
    }

    public static <T> void serialize(T mlp, String filepath) {
        FileOutputStream fileOut = null;
        ObjectOutputStream out = null;
        try {
            fileOut = new FileOutputStream(filepath);
            out = new ObjectOutputStream(fileOut);
            out.writeObject(mlp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOut.close();
                out.close();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(String filepath) {
        FileInputStream fileIn = null;
        ObjectInputStream in = null;
        T mlp = null;
        try {
            fileIn = new FileInputStream(filepath);
            in = new ObjectInputStream(fileIn);
            mlp = (T) in.readObject();
        } catch (IOException | ClassCastException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fileIn.close();
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return mlp;
    }

    public static void rumCmd(String cmd) {
        final Runtime rt = Runtime.getRuntime();
        try {
            rt.exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeStr(String filepath, String str) {
        try (FileWriter ostream = new FileWriter(filepath)) {
            ostream.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
