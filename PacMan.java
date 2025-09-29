import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Blob;
import  java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class PacMan extends JPanel implements ActionListener, KeyListener {
    class Block{
        int x; 
        int y;
        int width;
        int height;
        Image image;

        int stratx;
        int straty;
        char direction = 'U'; // U D L R
        int velocityx = 0;
        int velocityy = 0;

        Block(int x, int y, int width, int height, Image image){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.image = image;
            this.stratx = x;
            this.straty = y;
        }

        void updateDirection(char direction){
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelocity();
            this.x += this.velocityx;
            this.y += this.velocityy;
            for(Block wall: walls){
                if (collision(this, wall)){
                    this.x -= this.velocityx; 
                    this.y -= this.velocityy;  
                    this.direction = prevDirection;
                    updateVelocity();
                       
                }
            }
        }

        private void updateVelocity() {
           if (this.direction == 'U'){
                this.velocityx = 0;
                this.velocityy = -tileSize/4;
           }
           else if(this.direction == 'D'){
                this.velocityx = 0;
                this.velocityy = tileSize/4;
           }
           else if(this.direction == 'L'){
                this.velocityx = -tileSize/4;
                this.velocityy = 0;
           }
           else if(this.direction == 'R'){
                this.velocityx = 4;
                this.velocityy = 0;
           }
        }

        void reset(){
            this.x = this.stratx;
            this.y = this.straty;

        }
 
    }
        private int rowcount = 21;
        private int colcount = 19;
        private int tileSize = 32;
        private int boardwith = colcount * tileSize;
        private int boardheight = rowcount * tileSize;

        private Image wallImage;
        private Image blueGhostImage;
        private Image orangeGhostImage;
        private Image pinkGhostImage;
        private Image redGhostImage;

        private Image pacmanUpImage;
        private Image pacmanDownImage;
        private Image pacmanLeftImage;
        private Image pacmanRightImage;


    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
         private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "X       bpo       X",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };

        HashSet<Block> walls;
        HashSet<Block> foods;
        HashSet<Block> ghosts;
        Block pacman;

        Timer gameLoop;
        char [] directions = {'U', 'D', 'L', 'R'};//up down left right
        Random random = new Random();
        int score = 0;
        int lives = 3;
        Boolean gameOver = false;


        PacMan(){
            setPreferredSize(new Dimension(boardwith, boardheight));
            setBackground(Color.black);
            addKeyListener(this);
            setFocusable(true);

            //load images
            wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
            blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
            orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
            pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
            redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();

            pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
            pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
            pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
            pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();

            loadMap();
            for(Block ghost: ghosts){
                char newDirection = directions[random.nextInt(4)];
                ghost.updateDirection(newDirection);
            }
            //how lon it takes to start timer, milliseconds gone between frames
            gameLoop = new Timer(50, this);//20fps (1000/50)
            gameLoop.start();
           
        }
    public void loadMap(){
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();

        for(int r = 0; r < rowcount; r++){
            for(int c = 0; c < colcount; c++){
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c * tileSize;
                int y = r * tileSize;

                if (tileMapChar == 'X'){
                    Block wall = new Block(x, y, tileSize, tileSize, wallImage);
                    walls.add(wall);
                }
                 else if (tileMapChar == ' '){
                    Block food = new Block(x + 14, y + 14, 4, 4, null);
                    foods.add(food);
                }
                 else if (tileMapChar == 'P'){
                    pacman = new Block(x, y, tileSize, tileSize, pacmanRightImage);
                }
                 else if (tileMapChar == 'b'){
                    Block blueGhost = new Block(x, y, tileSize, tileSize, blueGhostImage);
                    ghosts.add(blueGhost);
                }
                 else if (tileMapChar == 'o'){
                    Block orangeGhost = new Block(x, y, tileSize, tileSize, orangeGhostImage);
                    ghosts.add(orangeGhost);
                }
                 else if (tileMapChar == 'p'){
                    Block pinkGhost = new Block(x, y, tileSize, tileSize, pinkGhostImage);
                    ghosts.add(pinkGhost);
                }
                 else if (tileMapChar == 'r'){
                    Block redGhost = new Block(x, y, tileSize, tileSize, redGhostImage);
                    ghosts.add(redGhost);
                }        
            }
        }
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(pacman.image,pacman.x, pacman.y, pacman.width, pacman.height, null);

        for(Block wall: walls){
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        for(Block ghost: ghosts){
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }
        g.setColor(Color.WHITE);    
        for(Block food: foods){
            g.fillRect(food.x, food.y, food.height, food.width);
        }
        //score
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf(score), tileSize/2 , tileSize/2);
        }
        else {
            g.drawString("x" + String.valueOf(lives) + " Score: "+ String.valueOf(score), tileSize/2 , tileSize/2);

        }


    } 

    public void move() {
        pacman.x += pacman.velocityx;
        pacman.y += pacman.velocityy;

        //check for wall collision
        for(Block wall: walls){
            if (collision(pacman, wall)){
                pacman.x -= pacman.velocityx;
                pacman.y -= pacman.velocityy;
                break;
            }

        }
         //check for ghost collisions
         for(Block ghost: ghosts){
            if(collision(ghost, pacman)){  
                lives -= 1;
                if (lives == 0){
                    gameOver = true;
                    return;
                }
                resetPositions();

            }
            if (ghost.y == tileSize *9 && ghost.direction != 'U' && ghost.direction != 'D'){
                ghost.updateDirection('U');     
            }
            ghost.x += ghost.velocityx;
            ghost.y += ghost.velocityy;
            for(Block wall: walls){
                if (collision(ghost, wall) || ghost.x <= 0  || ghost.x + ghost.width >= boardwith ){
                    ghost.x -= ghost.velocityx;
                    ghost.y -= ghost.velocityy;  
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);     
                }
            }

        }
        //check for food collision
        Block foodEaten = null;
        for(Block food: foods){
            if (collision(pacman, food)){
                foodEaten = food;
                score += 10;

            }
        }
        foods.remove(foodEaten);

        if(foods.isEmpty()) {
            loadMap();
            resetPositions();
        }
    }

    public boolean collision (Block a, Block b){
        return (a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y);
  
    }

    public void resetPositions(){
        pacman.reset();
        pacman.velocityx = 0 ;
        pacman.velocityy = 0 ; 
        for (Block ghost : ghosts) {
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();    
        if  (gameOver) {
            gameLoop.stop();
        }
    }
   @Override
public void keyTyped(KeyEvent e) {
    // لازم نیست چیزی بذاری
}

@Override
public void keyPressed(KeyEvent e) {
    // میتونی حرکت سریع رو اینجا هم اضافه کنی
}

@Override
public void keyReleased(KeyEvent e) {
    if (gameOver) {
        loadMap();
        resetPositions();
        score = 0;
        lives = 3;
        gameOver = false;
        gameLoop.start();
    }

    if (e.getKeyCode() == KeyEvent.VK_UP){
        pacman.updateDirection('U');
    }
    else if (e.getKeyCode() == KeyEvent.VK_DOWN){
        pacman.updateDirection('D');
    }
    else if (e.getKeyCode() == KeyEvent.VK_LEFT){
        pacman.updateDirection('L');
    }
    else if (e.getKeyCode() == KeyEvent.VK_RIGHT){
        pacman.updateDirection('R');
    }

    if (pacman.direction == 'U'){
        pacman.image = pacmanUpImage;
    }
    else if (pacman.direction == 'D'){
        pacman.image = pacmanDownImage;
    }
    else if (pacman.direction == 'L'){
        pacman.image = pacmanLeftImage;
    }
    else if (pacman.direction == 'R'){
        pacman.image = pacmanRightImage;
    } 
}
}



