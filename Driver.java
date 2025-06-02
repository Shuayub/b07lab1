import java.io.*;

public class Driver {
    public static void main(String[] args) throws IOException {
        // Test 1: Default constructor and evaluate()
        Polynomial p = new Polynomial();
        System.out.println("p(3) = " + p.evaluate(3)); // Should print 0.0

        // Test 2: Constructor with coefficients and exponents
        double[] c1 = {6, 5};     // 6 + 5x^3
        int[] e1 = {0, 3};
        Polynomial p1 = new Polynomial(c1, e1);
        System.out.println("p1(2) = " + p1.evaluate(2)); // Should be 6 + 5*8 = 46

        double[] c2 = {-2, -9};   // -2x - 9x^4
        int[] e2 = {1, 4};
        Polynomial p2 = new Polynomial(c2, e2);
        System.out.println("p2(1) = " + p2.evaluate(1)); // Should be -2 - 9 = -11

        // Test 3: add()
        Polynomial s = p1.add(p2); // Should be 6 - 2x + 5x^3 - 9x^4
        System.out.println("s(1) = " + s.evaluate(1)); // Should be 6 - 2 + 5 - 9 = 0
        System.out.println(s.hasRoot(1) ? "1 is a root of s" : "1 is not a root of s"); // Should be a root

        // Test 4: multiply()
        Polynomial product = p1.multiply(p2);
        // (6 + 5x^3)(-2x - 9x^4) = -12x - 54x^4 -10x^4 -45x^7
        // Simplified: -12x -64x^4 -45x^7
        System.out.println("product(1) = " + product.evaluate(1)); // Should be -12 -64 -45 = -121

        // Test 5: saveToFile()
        String filename = "poly_output.txt";
        s.saveToFile(filename);
        System.out.println("Polynomial saved to file: " + filename);

        // Test 6: File constructor
        File f = new File(filename);
        Polynomial fromFile = new Polynomial(f);
        System.out.println("fromFile(0.1) = " + fromFile.evaluate(0.1)); // Should match s.evaluate(0.1)
    }
}
