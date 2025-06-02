import java.io.*;
import java.util.*;

public class Polynomial {
    private double[] coefficients;
    private int[] exponents;

    public Polynomial() {
        this.coefficients = new double[]{0};
        this.exponents = new int[]{0};
    }

    public Polynomial(double[] coefficients, int[] exponents) {
        if (coefficients.length != exponents.length)
            throw new IllegalArgumentException("Coefficients and exponents must match in length.");

        this.coefficients = coefficients;
        this.exponents = exponents;
    }

    public Polynomial add(Polynomial other) {
        Map<Integer, Double> termMap = new HashMap<>();

        for (int i = 0; i < this.coefficients.length; i++) {
            termMap.put(this.exponents[i], this.coefficients[i]);
        }

        for (int i = 0; i < other.coefficients.length; i++) {
            termMap.put(other.exponents[i], termMap.getOrDefault(other.exponents[i], 0.0) + other.coefficients[i]);
        }

        return fromMap(termMap);
    }

    public Polynomial multiply(Polynomial other) {
        Map<Integer, Double> termMap = new HashMap<>();

        for (int i = 0; i < this.coefficients.length; i++) {
            for (int j = 0; j < other.coefficients.length; j++) {
                int newExp = this.exponents[i] + other.exponents[j];
                double newCoef = this.coefficients[i] * other.coefficients[j];
                termMap.put(newExp, termMap.getOrDefault(newExp, 0.0) + newCoef);
            }
        }

        return fromMap(termMap);
    }

    public double evaluate(double x) {
        double result = 0;
        for (int i = 0; i < coefficients.length; i++) {
            result += coefficients[i] * Math.pow(x, exponents[i]);
        }
        return result;
    }

    public boolean hasRoot(double x) {
        return Math.abs(evaluate(x)) < 1e-9;
    }

    public Polynomial(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        reader.close();

        String[] terms = line.split("(?=[+-])");
        double[] tempCoefs = new double[terms.length];
        int[] tempExps = new int[terms.length];
        int count = 0;

        for (String term : terms) {
            term = term.trim();
            if (term.isEmpty()) continue;
            if (term.contains("x")) {
                int xIndex = term.indexOf("x");
                double coef = (xIndex == 0 || term.charAt(0) == '+') ? 1.0 :
                              (term.substring(0, xIndex).equals("-") ? -1.0 : Double.parseDouble(term.substring(0, xIndex)));
                int exp = 1;
                if (xIndex + 1 < term.length()) {
                    exp = Integer.parseInt(term.substring(xIndex + 1));
                }
                tempCoefs[count] = coef;
                tempExps[count] = exp;
                count++;
            } else {
                tempCoefs[count] = Double.parseDouble(term);
                tempExps[count] = 0;
                count++;
            }
        }

        this.coefficients = Arrays.copyOf(tempCoefs, count);
        this.exponents = Arrays.copyOf(tempExps, count);
    }

    public void saveToFile(String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < coefficients.length; i++) {
            if (coefficients[i] == 0) continue;
            if (i > 0 && coefficients[i] > 0) sb.append("+");
            sb.append(coefficients[i]);
            if (exponents[i] != 0) {
                sb.append("x").append(exponents[i]);
            }
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(sb.toString());
        writer.close();
    }

    private static Polynomial fromMap(Map<Integer, Double> map) {
        int count = 0;
        for (double value : map.values()) {
            if (Math.abs(value) > 1e-9) count++;
        }

        double[] coefs = new double[count];
        int[] exps = new int[count];
        int i = 0;
        for (Map.Entry<Integer, Double> entry : map.entrySet()) {
            if (Math.abs(entry.getValue()) > 1e-9) {
                exps[i] = entry.getKey();
                coefs[i] = entry.getValue();
                i++;
            }
        }

        return new Polynomial(coefs, exps);
    }
}
