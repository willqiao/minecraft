package com.games;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CanvasPanel extends JPanel {
    BufferedImage apple = null;
    BufferedImage north = null;
    BufferedImage south = null;
    BufferedImage east = null;
    BufferedImage west = null;

    //1 left, 2 right, 3 up, 4 down
    public int direction = 2;
    int[][] data = new int[20][20];
    java.util.List<int[]> snake = new ArrayList<>();
    public int score = 0;
    InputStream bufferedIn;

    public CanvasPanel() {
        try {
            apple = ImageIO.read(CanvasPanel.class.getClassLoader().getResource("apple.png").openStream());
            north = ImageIO.read(CanvasPanel.class.getClassLoader().getResource("north.png").openStream());
            south = ImageIO.read(CanvasPanel.class.getClassLoader().getResource("south.png").openStream());
            east = ImageIO.read(CanvasPanel.class.getClassLoader().getResource("east.png").openStream());
            west = ImageIO.read(CanvasPanel.class.getClassLoader().getResource("west.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        snake.add(new int[]{10,10});
        snake.add(new int[]{10,11});
        snake.add(new int[]{10,12});
        snake.add(new int[]{10,13});
        for (int[] position : snake) {
            data[position[0]][position[1]] = 1;
        }

        randomApple(25);


    }

    public void playsound() {
        try {
            InputStream audioSrc = getClass().getClassLoader().getResourceAsStream("apple.wav");
            BufferedInputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

    }

    public void randomApple(int num) {
        while(num > 0) {
            int x = (int)(Math.random()*20);
            int y = (int)(Math.random()*20);
            if (data[x][y] == 0) {
                data[x][y] = 2;
                num --;
            }
        }
    }

    public void moveNext() {
        if (gameState == 0) return;

        int currX = snake.get(0)[0];
        int currY = snake.get(0)[1];

        switch (direction) {
            case 1://go east
                currX = (currX + 1) % data.length;
                break;
            case 2://go north
                currY = (currY - 1);
                if (currY == -1) currY = data.length - 1;
                break;
            case 3://go south
                currY = (currY + 1) % data.length;
                break;
            case 4://go west
                currX = (currX - 1);
                if (currX == -1) currX = data.length - 1;
                break;
        }
        //we are going to eat this apple.
        if (data[currX][currY]  == 2) {
            score += 100;
            this.playsound();
            this.randomApple(1);
        } else {
            int[] tail = snake.remove(snake.size() - 1);
            data[tail[0]][tail[1]] = 0;
        }
        data[currX][currY] = 1;
        snake.add(0, new int[]{currX, currY});
    }

    private void drawSquare(Graphics graphics, Color color) {
        graphics.setColor(color);


        int size = 30;

        for (int i = 0; i < 20; i ++) {
            for (int j = 0; j < 20; j++) {

                if (data[j][i] == 1) {
                    graphics.setColor(Color.GREEN);
                    int x = snake.get(0)[0];
                    int y = snake.get(0)[1];
                    if (x == j && y == i) {
                        if (direction == 1) {
                            graphics.drawImage(east, j * size, i * size, size, size, null);
                        } else if (direction == 2) {
                            graphics.drawImage(north, j * size, i * size, size, size, null);
                        }else if (direction == 3) {
                            graphics.drawImage(south, j * size, i * size, size, size, null);
                        }else if (direction == 4) {
                            graphics.drawImage(west, j * size, i * size, size, size, null);
                        }

                    } else {
                        graphics.fillRect(j * size, i * size, size , size);
                    }

                } else if (data[j][i] == 0){
                    graphics.setColor(Color.BLACK);
                    graphics.drawRect(j * size, i * size, size , size);
                } else if (data[j][i] == 2) {
                    graphics.setColor(Color.BLACK);
                    graphics.drawRect(j * size, i * size, size , size);
                    graphics.drawImage(apple, j * size, i * size,size, size, null);
                }

            }
        }

    }
    int gameState = 0;
    int count = 0;
    @Override
    protected void paintComponent(Graphics g) {
        count++;
        super.paintComponent(g);
        if (gameState == 0) {
            Font old = g.getFont();
            g.setFont(new Font("TimesRoman", Font.BOLD, 20));
            g.drawString("Ready?", 300,300);
            g.setFont(old);
        }
        g.drawString("Your score : " + score, 10, 615);
        drawSquare(g, Color.BLACK);
    }
}
