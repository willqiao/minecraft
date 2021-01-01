package com.games;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.Executor;

public class MyMain {
    boolean gameState = false;
    public static void main(String[] args) {

        JFrame window = new JFrame("My First Game");
        CanvasPanel canvas = new CanvasPanel();

        JMenuBar jMenuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem item = fileMenu.add("Restart");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.playsound();
            }
        });
        fileMenu.add("Pause");
        jMenuBar.add(fileMenu);
        JMenu aboutMenu = new JMenu("About");
        aboutMenu.add("About us");
        aboutMenu.add("Version");
        jMenuBar.add(aboutMenu);
        window.setJMenuBar(jMenuBar);
//        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        window.setUndecorated(true);
        window.setPreferredSize(new Dimension(610, 700));


        window.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyChar() == 's') {
                    canvas.direction = 3;
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT ||e.getKeyChar() == 'a') {
                    canvas.direction = 4;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT ||e.getKeyChar() == 'd') {
                    canvas.direction = 1;
                } else if (e.getKeyCode() == KeyEvent.VK_UP ||e.getKeyChar() == 'w') {
                    canvas.direction = 2;
                } else if (e.getKeyChar() == ' ') {
                    canvas.gameState = 1;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        // ()->{}

        Thread thread = new Thread(()->{
            while(true) {
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                canvas.moveNext();
                canvas.repaint();
            }

        });
        thread.start();

//        JLabel label = new JLabel("just a label");
//        window.getContentPane().add(label);
        window.getContentPane().add(canvas, BorderLayout.CENTER);

        canvas.setPreferredSize(new Dimension(610,700));


//        window.setResizable(false);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.pack();
        window.setVisible(true);
    }
}
