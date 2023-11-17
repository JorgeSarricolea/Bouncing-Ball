import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BouncingBall extends JFrame {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int BALL_SIZE = 50;
    private static final int SHADOW_OFFSET = 5;
    private static final int TRANSFORMATION_DURATION = 200;

    private int ballX = 100;
    private int ballY = 100;
    private int ballSpeedX = 5;
    private int ballSpeedY = 5;
    private float ovalScale = 1.1f;
    private boolean flagX = false;
    private boolean flagY = false;


    private boolean isOval = false;
    private Timer ovalTimer;

    public BouncingBall() {
        setTitle("Bouncing Ball");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Timer timer = new Timer(25, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveBall();
                repaint();
            }
        });
        timer.start();

        ovalTimer = new Timer(TRANSFORMATION_DURATION, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isOval = false; // Returns to original shape after transformation
                ovalTimer.stop();
            }
        });
    }

    private void moveBall() {
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        if (ballX <= 0 || ballX >= WIDTH - BALL_SIZE) {
            if (ballSpeedX != 0) {
                System.out.println("Y axis collision");
                // Change flags position in Y axis
                flagY = true;
                flagX = false;
            }
            ballSpeedX = -ballSpeedX;
            transformToOval();
        }

        if (ballY <= 0 || ballY >= HEIGHT - BALL_SIZE) {
            if (ballSpeedY != 0) {
                System.out.println("X axis collision");
                // Change flags position in X axis
                flagX = true;
                flagY = false;
            }
            ballSpeedY = -ballSpeedY;
            transformToOval();
        }
    }

    private void transformToOval() {
        isOval = true; // Start transformation when it collides with edges
        ovalTimer.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawBall(g);
    }

    private void drawBall(Graphics g) {
        // Clear the previous frame
        g.clearRect(0, 0, WIDTH, HEIGHT);

        // Draw shadow
        g.setColor(Color.BLACK);
        drawOval(g, ballX + SHADOW_OFFSET, ballY + SHADOW_OFFSET, BALL_SIZE, BALL_SIZE);

        // Draw the ball
        g.setColor(Color.RED);
        drawOval(g, ballX, ballY, BALL_SIZE, BALL_SIZE);
    }

    private void drawOval(Graphics g, int x, int y, int width, int height) {
        if (isOval) {
            //X axis animation
            if (flagX == true && flagY == false) {
                g.fillOval(x, y, (int) (width * ovalScale), height);
            //Y axis animation
            } else if (flagY == true &&  flagX == false) {
                g.fillOval(x, y, width, (int) (height * ovalScale));
            }
        } else {
            g.fillOval(x, y, width, height);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BouncingBall().setVisible(true);
            }
        });
    }
}
