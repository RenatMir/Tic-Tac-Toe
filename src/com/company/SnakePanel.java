package com.company;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class SnakePanel extends JPanel implements ActionListener {
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private final int UNIT = 25;
    private final int GAME_UNITS = (WIDTH * HEIGHT) / UNIT;
    private final int DELAY = 75;
    private int appleX, appleY;
    private int body = 0;
    private int[] xDir = new int[GAME_UNITS];
    private int[] yDir = new int[GAME_UNITS];
    private int direction = 39;
    private boolean running;
    private int applesEaten = 0;
    Timer timer;
    Random random;

    SnakePanel(){
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);
        this.setBackground(Color.lightGray);
        this.addKeyListener(new MyKey());
        random = new Random();
        startGame();
    }
    private void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    private void draw(Graphics g){
        if(running) {
            for (int i = 0; i < HEIGHT / UNIT; i++) {
                g.drawLine(0, i * UNIT, WIDTH, i * UNIT);
                g.drawLine(i * UNIT, 0, i * UNIT, HEIGHT);
            }

            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT, UNIT);


            for (int i = 0; i <= body; i++) {
                if (i == 0) {
                    g.setColor(Color.blue);

                } else {
                    g.setColor(new Color(70, 83, 217, 255));
                }
                g.fillRect(xDir[i], yDir[i], UNIT, UNIT);
            }
            g.setColor(Color.red);
            g.setFont(new Font("Calibri", Font.PLAIN, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score = " + applesEaten, (WIDTH - metrics.stringWidth("Score = " + applesEaten)) / 2, g.getFont().getSize());
        }
        else
            stopGame(g);
    }

    private void move(){
        for(int i = body; i > 0; i--){
            xDir[i] = xDir[i - 1];
            yDir[i] = yDir[i - 1];
        }

        switch (direction){
            case 37:
                xDir[0] -= UNIT;
                break;
            case 38:
                yDir[0] -= UNIT;
                break;
            case 39:
                xDir[0] += UNIT;
                break;
            case 40:
                yDir[0] += UNIT;
                break;
        }
    }

    private void checkCollisions(){
        if(xDir[0] > WIDTH - UNIT
                || xDir[0] < 0
                || yDir[0] > HEIGHT - UNIT
                || yDir[0] < 0)
            running = false;

        for(int i = body; i > 0; i--){
            if(xDir[0] == xDir[i] && yDir[0] == yDir[i])
                running = false;
        }

        if(!running) {
            timer.stop();
        }

    }
    private void newApple(){
        appleX = random.nextInt(WIDTH / UNIT) * UNIT;
        appleY = random.nextInt(HEIGHT / UNIT) * UNIT;
    }

    private void checkApple() {
        if(xDir[0] == appleX && yDir[0] == appleY){
            body++;
            applesEaten++;
            newApple();
        }
    }

    private void stopGame(Graphics g){
        g.setColor(Color.red);
        g.setFont(new Font("Calibri", Font.PLAIN, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkCollisions();
            checkApple();
        }
        repaint();
    }



    private class MyKey extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 39)
                        direction = 37;
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 37)
                        direction = 39;
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 40)
                        direction = 38;
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 38)
                        direction = 40;
                    break;
            }
        }
    }
}
