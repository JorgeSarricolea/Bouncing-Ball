import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BouncingBall extends JFrame {

    // Constants for the frame dimensions and ball properties
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int BALL_SIZE = 50;
    private static final int SHADOW_OFFSET = 5;
    private static final int TRANSFORMATION_DURATION = 200;

    // Variables for ball position, speed, and transformation
    private int ballX = 100;
    private int ballY = 100;
    private int ballSpeedX = 5;
    private int ballSpeedY = 5;
    private float ovalScale = 1.2f;
    private boolean flagX = false;
    private boolean flagY = false;

    // Flags to control oval transformation and timers
    private boolean isOval = false;
    private Timer ovalTimer;

    // Constructor for the BouncingBall class
    public BouncingBall() {
        // Set frame properties
        setTitle("Bouncing Ball");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Timer for regular ball movement
        Timer timer = new Timer(25, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveBall();
                repaint();
            }
        });
        timer.start();

        // Create a thread for oval transformation
        Thread ovalThread = new Thread(new OvalTransformation());
        ovalThread.start();
    }

    // Inner class to handle oval transformation using a separate thread
    private class OvalTransformation implements Runnable {
        @Override
        public void run() {
            while (true) {
                if (isOval) {
                    transformToOval();
                }
                try {
                    // Sleep to control the rate of oval transformation
                    Thread.sleep(TRANSFORMATION_DURATION);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Method to handle ball movement based on collisions with frame edges
    private void moveBall() {
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        // Check for collisions with frame edges
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

    // Method to initiate oval transformation
    private void transformToOval() {
        isOval = true; // Start transformation when it collides with edges
        ovalTimer = new Timer(TRANSFORMATION_DURATION, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isOval = false; // Return to the original shape after transformation
            }
        });
        ovalTimer.start();
    }

    // Override paint method to draw the ball and its shadow
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawBall(g);
    }

    // Method to draw the ball and its shadow
    private void drawBall(Graphics g) {
        // Clear the previous frame
        g.clearRect(0, 0, WIDTH, HEIGHT);

        // Draw the shadow
        g.setColor(Color.BLACK);
        drawOval(g, ballX + SHADOW_OFFSET, ballY + SHADOW_OFFSET, BALL_SIZE, BALL_SIZE);

        // Draw the ball
        g.setColor(Color.RED);
        drawOval(g, ballX, ballY, BALL_SIZE, BALL_SIZE);
    }

    // Method to draw the oval with possible scaling based on flags
    private void drawOval(Graphics g, int x, int y, int width, int height) {
        if (isOval) {
            // X axis animation
            if (flagX == true && flagY == false) {
                g.fillOval(x, y, (int) (width * ovalScale), height);
            // Y axis animation
            } else if (flagY == true &&  flagX == false) {
                g.fillOval(x, y, width, (int) (height * ovalScale));
            }
        } else {
            g.fillOval(x, y, width, height);
        }
    }

    // Main method to launch the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BouncingBall().setVisible(true);
            }
        });
    }
}
