import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import java.util.List;

public class Racing {
    static final Timer TIMER = new Timer((int) ((1.0/60)*1000), null);
    static JFrame screen = new JFrame("easportsitsinthegame");


    static final int WINDOW_HEADER_OFFSET = 28;
    static final int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 800;

    static final int PARTITION_WIDTH = 100, PARTITION_HEIGHT = 100;
    static final double EPSILON = 1E-8;

    static PartitionBlock[] blocks = new PartitionBlock[WINDOW_WIDTH/PARTITION_WIDTH * WINDOW_HEIGHT/PARTITION_HEIGHT];
    static final int PARTITION_ARRAY_HEIGHT = WINDOW_HEIGHT/PARTITION_HEIGHT;

    static final Player PLAYER = new Player(200,200);

    public static void main(String[] args) {
        for(int i = 0; i < blocks.length; i++){
            int y = i/PARTITION_ARRAY_HEIGHT;
            blocks[i] = new PartitionBlock(i % PARTITION_ARRAY_HEIGHT, y);
        }

        TIMER.addActionListener(new UpdateHandler());
        TIMER.start();

        screen.getContentPane().add(new Graphic());
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.setSize(WINDOW_WIDTH,WINDOW_HEIGHT + WINDOW_HEADER_OFFSET);
        screen.getContentPane().add(PLAYER);
        screen.addKeyListener(PLAYER);
        screen.setBackground(Color.black);
        screen.setLocationRelativeTo(null);
        screen.setVisible(true);
    }

    static class UpdateHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            PLAYER.update();
            PLAYER.transform.update();
            screen.repaint();
        }
    }

    static abstract class GameObject extends JPanel {
        PartitionBlock block;

        double posX, posY;
        Vector2 transform = new Vector2();
        public GameObject(int x, int y){
            posX = x;
            posY = y;
        }

        public void update(){

        }

        final public void updateBlock(){
            PartitionBlock curr = coordToBlock((int)(posX), (int)(posY));
            if(curr != block){
                if(block != null) block.remove(this);
                block = curr;
                if(block != null) block.append(this);
            }
        }
    }

    static class Player extends GameObject implements KeyListener {
        boolean W, A, S, D;

        public Player(int x, int y){
            super(x, y);
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()){
                case KeyEvent.VK_W -> W = true;
                case KeyEvent.VK_S -> S = true;
                case KeyEvent.VK_A -> A = true;
                case KeyEvent.VK_D -> D = true;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch(e.getKeyCode()){
                case KeyEvent.VK_W -> W = false;
                case KeyEvent.VK_S -> S = false;
                case KeyEvent.VK_A -> A = false;
                case KeyEvent.VK_D -> D = false;
            }
        }

        @Override
        public void update() {
            velUpdate();
            posUpdate();
        }

        private void velUpdate(){
            if(W){
                transform.changeMagnitudeBy(0.25);
            }
            if(S){
                transform.changeMagnitudeBy(-0.25);
            }
            if(A){
                transform.changeThetaBy(Math.toRadians(1)*-3);
            }
            if(D){
                transform.changeThetaBy(Math.toRadians(1)*3);
            }

            if(!(W||S)){
                transform.multiplyMagnitude(0.8);
            }else{
                transform.multiplyMagnitude(0.95);
            }
        }

        private void posUpdate(){
            posX += transform.x;
            posY += transform.y;

            if(transform.x > 0 || transform.y > 0){
                updateBlock();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(Color.RED);
            g.fillRect((int)(posX), (int)(posY), 4, 4);
            g.drawLine((int)(posX)+2, (int)(posY)+2, (int)(posX)+2+(int)((transform.x)*10), (int)(posY)+2+(int)((transform.y)*10));

            Graphics2D g2d = (Graphics2D) g;
            g2d.drawString("x: "+transform.x+" | y: "+transform.y+"\nmag: "+transform.magnitude+" | theta: "+transform.theta, 0, 200);
        }
    }

    static class PartitionBlock {
        public List<GameObject> objects = new ArrayList<>();
        final int x, y;

        public PartitionBlock(int x, int y){
            this.x = x;
            this.y = y;
        }

        public void append(GameObject o){
            objects.add(o);
        }

        public void remove(GameObject o){
            objects.remove(o);
        }

        @Override
        public int hashCode() {
            return x*1021 + y;
        }
    }

    static class Graphic extends JPanel {

        public Graphic(){

        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            for(PartitionBlock p : blocks){
                g2d.setColor(Color.ORANGE);
                g2d.draw(new Rectangle(p.x*PARTITION_WIDTH, p.y*PARTITION_HEIGHT, PARTITION_WIDTH, PARTITION_HEIGHT));
            }
        }
    }

    static PartitionBlock coordToBlock(int x, int y) {
        int blockIndex = (y - WINDOW_HEADER_OFFSET) / PARTITION_HEIGHT * PARTITION_ARRAY_HEIGHT + x / PARTITION_WIDTH;
        if(blockIndex < 0 || blockIndex > blocks.length){
            return null;
        }
        return blocks[blockIndex];
    }

    static PartitionBlock coordToBlock(Point p) {
        return coordToBlock(p.x, p.y);
    }
}