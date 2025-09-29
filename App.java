import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        int rowcount = 21;
        int colcount = 19;
        int tileSize = 32;
        int boardwith = colcount * tileSize;
        int boardheight = rowcount * tileSize;


        JFrame frame = new JFrame("Pac Man");
       // frame.setVisible(true);
        frame.setSize(boardwith, boardheight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PacMan pacmanGame = new PacMan();
        frame.add(pacmanGame);
        frame.pack();
        pacmanGame.requestFocus();
        frame.setVisible(true);
          
    }
}
