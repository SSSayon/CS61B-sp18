package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; ++i) {
            sb.append(CHARACTERS[rand.nextInt(CHARACTERS.length)]);
        }
        return sb.toString();
    }

    public void drawFrame(String s) {
        StdDraw.clear();
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(width / 2, height / 2, s);

        if (!gameOver) {
            StdDraw.setFont(new Font("Monaco", Font.BOLD, 20));
            StdDraw.textLeft(1, height - 1, "Round" + round);
            StdDraw.text(width / 2, height - 1, playerTurn ? "Type!" : "Watch!");
            StdDraw.textRight(width - 1, height - 1, ENCOURAGEMENT[round % ENCOURAGEMENT.length]);
            StdDraw.line(0, height - 2, width, height - 2);
        }

        StdDraw.show();
    }

    public void flashSequence(String letters) {
        for (int i = 0; i < letters.length(); ++i) {
            drawFrame(letters.substring(i, i+1));
            StdDraw.pause(500);
            drawFrame(" ");
            StdDraw.pause(250);
        }
    }

    public String solicitNCharsInput(int n) {
        String input = "";
        drawFrame(input);

        while (input.length() < n) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            input += String.valueOf(key);
            drawFrame(input);
        }
        StdDraw.pause(1000);
        return input;
    }

    public void startGame() {
        gameOver = false;
        playerTurn = false;
        round = 1;

        while(!gameOver) {
            playerTurn = false;
            drawFrame("Round " + round);
            StdDraw.pause(1000);
            drawFrame("");
            StdDraw.pause(500);

            String s = generateRandomString(round);
            flashSequence(s);

            playerTurn = true;
            String input = solicitNCharsInput(round);

            if (!input.equals(s)) {
                gameOver = true;
                drawFrame("Game over! You made it to round: " + round);
            } else {
                round += 1;
            }

        }
    }

}
