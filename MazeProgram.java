import java.awt.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MazeProgram extends JPanel implements KeyListener, MouseListener {
    JFrame frame;
    String[][] maze = new String[31][46];
    int x = 0, y = 10;
    boolean gameOver = false;
    int moveCounter = 0;
    int playerDirection = 0;
    boolean shortcut = false;
    ArrayList<Wall> wallList;

    public MazeProgram() {
        setBoard();
        buildWalls();
        frame = new JFrame();
        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 850);
        frame.setVisible(true);
        frame.addKeyListener(this);
        // this.addMouseListener(this);

        // Add on: music
        try {
            final Clip audio = AudioSystem.getClip();
            final AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("Nate's Theme 3.0.wav"));
            audio.open(inputStream);
            audio.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK); // this will set the background color
        g.fillRect(0, 0, 1000, 850);

        // drawBoard here!
        g.setColor(Color.WHITE);
        int mazeX = 0;
        int mazeY = 0;
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j].equals("-") || maze[i][j].equals("|"))
                    g.drawRect(mazeX * 10, mazeY * 10, 10, 10);
                mazeX++;
            }
            mazeX = 0;
            mazeY++;
        }

        // 3D walls
        g.drawRect(150, 350, 150, 400);
        g.drawRect(300, 400, 100, 300);
        g.drawRect(400, 450, 50, 200);

        g.drawRect(650, 450, 50, 200);
        g.drawRect(700, 400, 100, 300);
        g.drawRect(800, 350, 150, 400);

        if (wallList != null)
            for (int i = 0; i < wallList.size(); i++) {
                g.setColor(Color.RED);
                g.fillPolygon(wallList.get(i).getPolygon());
                g.setColor(Color.WHITE);
                g.drawPolygon(wallList.get(i).getPolygon());
            }
        // playable character
        g.setColor(Color.RED);
        g.fillRect(x, y, 10, 10);

        g.setColor(Color.BLUE);
        if (playerDirection == 0) {
            g.fillOval(x + 5, y + 2, 5, 5);
        }

        if (playerDirection == 90) {
            g.fillOval(x + 2, y - 1, 5, 5);
        }

        if (playerDirection == 180) {
            g.fillOval(x - 1, y + 2, 5, 5);
        }

        if (playerDirection == 270) {
            g.fillOval(x + 2, y + 5, 5, 5);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Times New Roman", Font.PLAIN, 50));
        g.drawString("Moves: " + moveCounter, 710, 50);

        if (x == 450 && y == 290) {
            g.setColor(Color.BLACK); // this will set the background color
            g.fillRect(0, 0, 1000, 850);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Times New Roman", Font.PLAIN, 100));
            g.drawString("You Found the Exit!", 75, 400);
            g.setFont(new Font("Times New Roman", Font.PLAIN, 30));

            // Add on: restart
            g.drawString("Click any key to restart", 80, 600);
            moveCounter = 0;
            x = 0;
            y = 10;
            gameOver = false;
            shortcut = false;
        }

        // Add On: Teleportation to the finish at hidden location
        if (x == 140 & y == 70) {
            x = 440;
            y = 290;
            repaint();
            buildWalls();
            shortcut = true;
        }

        if (shortcut) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Times New Roman", Font.PLAIN, 30));
            g.drawString("You found the shortcut!", 580, 100);
        }
    }

    public void setBoard() {
        // choose your maze design
        ArrayList<String> list = new ArrayList<>();
        // pre-fill maze array here
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                maze[i][j] = " ";
            }
        }

        File name = new File("maze1.txt");
        int r = 0;
        try {
            BufferedReader input = new BufferedReader(new FileReader(name));
            String text;
            while ((text = input.readLine()) != null) {
                list.add(text);
            }
        } catch (IOException io) {
            System.err.println("File error");
        }

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                maze[i][j] = list.get(i).charAt(j) + "";
            }
        }

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                System.out.print(maze[i][j]);
            }
            System.out.println();
        }
    }

    public void buildWalls() {
        wallList = new ArrayList<>();
        int characterRow = y / 10;
        int characterCol = x / 10;

        if (playerDirection == 90) {
            int furthestRow = characterRow - 4;
            while (furthestRow <= characterRow) {
                if (furthestRow >= 0) {
                    if (characterCol + 1 < maze[0].length) {
                        String right = maze[furthestRow][characterCol + 1];
                        if (right.equals("-") || right.equals("|")) {
                            int distance = characterRow - furthestRow;
                            wallList.add(new Wall(distance, false));
                        }
                    }
                    if (characterCol - 1 >= 0) {
                        String left = maze[furthestRow][characterCol - 1];
                        if (left.equals("-") || left.equals("|")) {
                            int distance = characterRow - furthestRow;
                            wallList.add(new Wall(distance, true));
                        }
                    }
                    String front = maze[furthestRow][characterCol];
                    if (front.equals("-") || front.equals("|")) {
                        int distance = characterRow - furthestRow;
                        wallList.add(new Wall(distance));
                    }
                } else {
                    if (characterCol == 1) {
                        if (characterRow == 0) {
                            wallList.add(new Wall(1));
                        } else if (characterRow == 1) {
                            wallList.add(new Wall(2));
                        }
                    }
                }
                furthestRow++;
            }
        }
        if (playerDirection == 270) {
            int furthestRow = characterRow + 4;
            while (furthestRow >= characterRow) {
                if (furthestRow < maze.length) {
                    if (characterCol - 1 >= 0) {
                        String right = maze[furthestRow][characterCol - 1];
                        if (right.equals("-") || right.equals("|")) {
                            int distance = furthestRow - characterRow;
                            wallList.add(new Wall(distance, false));
                        }
                    }
                    if (characterCol + 1 < maze[0].length) {
                        String left = maze[furthestRow][characterCol + 1];
                        if (left.equals("-") || left.equals("|")) {
                            int distance = furthestRow - characterRow;
                            wallList.add(new Wall(distance, true));
                        }
                    }
                    String front = maze[furthestRow][characterCol];
                    if (front.equals("-") || front.equals("|")) {
                        int distance = furthestRow - characterRow;
                        wallList.add(new Wall(distance));
                    }
                }
                furthestRow--;
            }
        }
        if (playerDirection == 0) {
            int furthestCol = characterCol + 4;
            while (furthestCol >= characterCol) {
                if (furthestCol < maze.length) {
                    if (characterRow - 1 >= 0) {
                        String left = maze[characterRow - 1][furthestCol];
                        if (left.equals("-") || left.equals("|")) {
                            int distance = furthestCol - characterCol;
                            wallList.add(new Wall(distance, true));
                        }
                    }
                    if (characterRow + 1 < maze[0].length) {
                        String right = maze[characterRow + 1][furthestCol];
                        if (right.equals("-") || right.equals("|")) {
                            int distance = furthestCol - characterCol;
                            wallList.add(new Wall(distance, false));
                        }
                    }
                    String front = maze[characterRow][furthestCol];
                    if (front.equals("-") || front.equals("|")) {
                        int distance = furthestCol - characterCol;
                        wallList.add(new Wall(distance));
                    }
                }
                furthestCol--;
            }
        }
        if (playerDirection == 180) {
            int furthestCol = characterCol - 4;
            while (furthestCol <= characterCol) {
                if (furthestCol >= 0) {
                    if (characterRow + 1 < maze.length) {
                        String left = maze[characterRow + 1][furthestCol];
                        if (left.equals("-") || left.equals("|")) {
                            int distance = characterCol - furthestCol;
                            wallList.add(new Wall(distance, true));
                        }
                    }
                    if (characterRow - 1 >= 0) {
                        String right = maze[characterRow - 1][furthestCol];
                        if (right.equals("-") || right.equals("|")) {
                            int distance = characterCol - furthestCol;
                            wallList.add(new Wall(distance, false));
                        }
                    }
                    String front = maze[characterRow][furthestCol];
                    if (front.equals("-") || front.equals("|")) {
                        int distance = characterCol - furthestCol;
                        wallList.add(new Wall(distance));
                    }
                }
                furthestCol++;
            }
        }

    }

    public void move() {
        boolean canMove = true;

        if (!gameOver) {
            if (playerDirection == 0) {
                if (maze[y / 10][(x / 10) + 1].equals("-") || maze[y / 10][(x / 10) + 1].equals("|"))
                    canMove = false;
                if (canMove)
                    x += 10;
            }

            if (playerDirection == 90) {
                if (maze[y / 10 - 1][x / 10].equals("-") || maze[y / 10 - 1][x / 10].equals("|"))
                    canMove = false;
                if (canMove)
                    y -= 10;
            }

            if (playerDirection == 180) {
                if (maze[y / 10][x / 10 - 1].equals("-") || maze[y / 10][x / 10 - 1].equals("|"))
                    canMove = false;
                if (canMove)
                    x -= 10;
            }

            if (playerDirection == 270) {
                if (maze[y / 10 + 1][x / 10].equals("-") || maze[y / 10 + 1][x / 10].equals("|"))
                    canMove = false;
                if (canMove)
                    y += 10;
            }
            if (canMove)
                moveCounter++;
        }

        if (x == 900 && y == 580)
            gameOver = true;
    }

    public void turn(String dir) {
        if (dir.equals("left")) {
            if (playerDirection == 270)
                playerDirection = 0;
            else
                playerDirection += 90;
        }

        if (dir.equals("right")) {
            if (playerDirection == 0)
                playerDirection = 270;
            else
                playerDirection -= 90;
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 37) // left arrow
            turn("left");

        if (e.getKeyCode() == 38) // up arrow
            move();

        if (e.getKeyCode() == 39) // right arrow
            turn("right");

        System.out.println(x + ", " + y);
        buildWalls();
        repaint();
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public static void main(String args[]) {
        MazeProgram app = new MazeProgram();
    }
}