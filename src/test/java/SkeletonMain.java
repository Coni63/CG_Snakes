import com.codingame.gameengine.runner.SoloGameRunner;

public class SkeletonMain {
    public static void main(String[] args)
    {
        SoloGameRunner gameRunner = new SoloGameRunner();
        gameRunner.setAgent(Agent1.class);
        // gameRunner.setTestCase("test1.json");

        // gameRunner.setTestCase("1_snakes_S.json");
        gameRunner.setTestCase("2_snakes_S.json"); // to use for the video
        // gameRunner.setTestCase("1_snakes_M.json"); 
        // gameRunner.setTestCase("2_snakes_M.json"); 
        // gameRunner.setTestCase("3_snakes_M.json"); 
        // gameRunner.setTestCase("1_snakes_L.json"); 
        // gameRunner.setTestCase("2_snakes_L.json"); 
        // gameRunner.setTestCase("3_snakes_L.json"); 
        // gameRunner.setTestCase("4_snakes_L.json"); 
        // gameRunner.setTestCase("1_snakes_XL.json");
        // gameRunner.setTestCase("2_snakes_XL.json");
        // gameRunner.setTestCase("3_snakes_XL.json");
        // gameRunner.setTestCase("4_snakes_XL.json");

        // gameRunner.setTestCase("1_snakes_S_T.json"); 
        // gameRunner.setTestCase("2_snakes_S_T.json"); 
        // gameRunner.setTestCase("1_snakes_M_T.json"); 
        // gameRunner.setTestCase("2_snakes_M_T.json");
        // gameRunner.setTestCase("3_snakes_M_T.json");
        // gameRunner.setTestCase("1_snakes_L_T.json");
        // gameRunner.setTestCase("2_snakes_L_T.json");
        // gameRunner.setTestCase("3_snakes_L_T.json");
        // gameRunner.setTestCase("4_snakes_L_T.json");
        // gameRunner.setTestCase("2_snakes_XL_T.json");
        // gameRunner.setTestCase("3_snakes_XL_T.json");
        // gameRunner.setTestCase("4_snakes_XL_T.json");
        // gameRunner.setTestCase("1_snakes_XL_T.json");

        // gameRunner.setTestCase("4_snakes_tricky.json");
        // gameRunner.setTestCase("4_snakes_tricky_T.json");

        // Another way to add a player
        // gameRunner.addAgent("python3 /home/user/player.py");
        
        gameRunner.start();
    }
}
