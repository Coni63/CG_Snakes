import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Agent1 {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int height = in.nextInt();
        int width = in.nextInt();

        int loop = 0;
        String ans  = "UUURRRRDDDDLLLLLLL";
        String ans2 = "DDDRRUUUULLLLLDDDDD";

        // game loop
        while (true) {
            int seed = in.nextInt();
            int numSnake = in.nextInt();
            for (int s = 0; s < numSnake; s++)
            {
                int L = in.nextInt();
                for (int i = 0; i < L; i++) {
                    int bodyRow = in.nextInt();
                    int bodyCol = in.nextInt();
                }
                int appleRow = in.nextInt();
                int appleCol = in.nextInt();
            }

            // Write an answer using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.out.println(ans.charAt(loop));
            System.out.println(ans2.charAt(loop));
            System.out.println("-");
            System.out.println("-");
            loop++;
        }
    }
}