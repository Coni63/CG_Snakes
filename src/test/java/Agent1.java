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
            int seed = in.nextInt(); // current seed of the board (used to spawn apples)
            int numSnake = in.nextInt(); // the number of snakes you control
            int numParts = in.nextInt(); // sum of snake's length
            System.err.println(seed + "  " + numSnake +" " + numParts);
            
            for (int i = 0; i < numParts; i++) {
                int snakeId = in.nextInt(); // id of the snake -- positions are provided head to tail
                int bodyRow = in.nextInt(); // row of this part of the snake (from head to tail)
                int bodyCol = in.nextInt(); // col of this part of the snake (from head to tail)
                System.err.println(snakeId + "  " + bodyRow +" " + bodyCol);
            }
            for (int i = 0; i < numSnake; i++) {
                int appleId = in.nextInt(); // id of the snake
                int appleRow = in.nextInt(); // row of the apple for this snake
                int appleCol = in.nextInt(); // col of the apple for this snake
                System.err.println(appleRow + "  " + appleCol);
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