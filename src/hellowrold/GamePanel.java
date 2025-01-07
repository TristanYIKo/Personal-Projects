package hellowrold;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class GamePanel extends JPanel implements Runnable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int panelWidth;
    private int panelHeight;
    private boolean running;
    private boolean gameOver;
    private Thread gameThread;
    private final int rectWidth = 100;
    private final int rectHeight = 45;
    private final int moveSpeed = 15;
    private boolean movingUp;
    private ArrayList<Circle> circles;
    private ArrayList<Trail> trails;
    private Coin currentCoin;
    private SoundEffectPlayer soundEffectPlayer;
    private final int circleSpeed = 12;
    private final int spawnInterval = 90;
    private int frameCount;
    private int rectX, rectY;
    private int score;
    private int coinCount;
    private Random rand;
    private JPanel gameOverPanel;
    private JLabel scoreLabel;
    private JLabel coinLabel;
    private String skin = "Default"; // Default skin
    private BufferedImage defaultSkinImage; // BufferedImage for default skin
    private BufferedImage skin1Image; // BufferedImage for Skin1
    private BufferedImage skin2Image; // BufferedImage for Skin2
    private BufferedImage coinImage;
    private BufferedImage[] circleImages; // Array to hold the circle images
    private BufferedImage gameOverBackground;
    private BufferedImage retryImage;
    private BufferedImage retryImageHover;
    private BufferedImage menuImage;
    private BufferedImage menuImageHover;
    private final int TRAIL_WIDTH = 22; // Width of the trail segment
    private final int TRAIL_HEIGHT = 22; // Height of the trail segment
    private long lastCoinSpawnTime; // Time stamp for the last coin spawn
    private static final int SPAWN_INTERVAL = 5; // The lower the value, the faster the spawn rate
    private int spawnCounter = 0;
    
    private BufferedImage[] backgroundImages;
    private int[] backgroundX1;
    private int[] backgroundX2;
    private int[] backgroundSpeeds;	

    public GamePanel(int width, int height) {
        this.panelWidth = width;
        this.panelHeight = height;
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        rand = new Random();
        trails = new ArrayList<>();
        circles = new ArrayList<>();
        soundEffectPlayer = new SoundEffectPlayer();
        setBackground(new Color(34, 34, 34));

        
     // Load the circle images from the specified paths
        circleImages = new BufferedImage[5];
        try {
            circleImages[0] = ImageIO.read(new File("src/hellowrold/Assets/Images/planetImage1.png"));
            circleImages[1] = ImageIO.read(new File("src/hellowrold/Assets/Images/planetImage2.png"));
            circleImages[2] = ImageIO.read(new File("src/hellowrold/Assets/Images/planetImage3.png"));
            circleImages[3] = ImageIO.read(new File("src/hellowrold/Assets/Images/planetImage4.png"));
            circleImages[4] = ImageIO.read(new File("src/hellowrold/Assets/Images/planetImage5.png"));
            gameOverBackground = ImageIO.read(new File("src/hellowrold/Assets/Backgrounds/gameOverBackground.png"));
            retryImage = ImageIO.read(new File("src/hellowrold/Assets/Images/retryButton.png"));
            retryImageHover = ImageIO.read(new File("src/hellowrold/Assets/Images/retryButtonHover.png"));
            menuImage = ImageIO.read(new File("src/hellowrold/Assets/Images/gameOverMenuReturnButton.png"));
            menuImageHover = ImageIO.read(new File("src/hellowrold/Assets/Images/gameOverMenuReturnButtonHover.png"));
            
            
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the case where images fail to load
            circleImages[0] = null;
            circleImages[1] = null;
            circleImages[2] = null;
            circleImages[3] = null;
            circleImages[4] = null;
            gameOverBackground = null;
            retryImage = null;
            retryImageHover = null;
            menuImage = null;
            menuImageHover = null;
        }
        
        
        // Load the default skin image from the specified path
        try {
            defaultSkinImage = ImageIO.read(new File("src/hellowrold/Assets/Skins/SkinPlane1.png"));
        } catch (IOException e) {
            e.printStackTrace();
            defaultSkinImage = null;
        }

        // Load the Skin1 image from the specified path
        try {
            skin1Image = ImageIO.read(new File("src/hellowrold/Assets/Skins/Skin1.png"));
        } catch (IOException e) {
            e.printStackTrace();
            skin1Image = null;
        }

        // Load the Skin2 image from the specified path
        try {
            skin2Image = ImageIO.read(new File("src/hellowrold/Assets/Skins/Skin2.png"));
        } catch (IOException e) {
            e.printStackTrace();
            skin2Image = null;
        }
     // Load the coin image from the specified path
        try {
            coinImage = ImageIO.read(new File("src/hellowrold/Assets/Images/coinImage.png"));
        } catch (IOException e) {
            e.printStackTrace();
            coinImage = null;
        }
     // Load multiple background images
        String[] backgroundPaths = {
            "src/hellowrold/Assets/Backgrounds/gamePanelBackground1.png",
            "src/hellowrold/Assets/Backgrounds/gamePanelBackground2.png",
            "src/hellowrold/Assets/Backgrounds/gamePanelBackground3.png"
        };
        backgroundImages = new BufferedImage[backgroundPaths.length];
        for (int i = 0; i < backgroundPaths.length; i++) {
            try {
                backgroundImages[i] = ImageIO.read(new File(backgroundPaths[i]));
            } catch (IOException e) {
                e.printStackTrace();
                backgroundImages[i] = null;
            }
        }

        initializeGame();
        setupKeyListener();
        setupGameOverPanel();
        
     // Initialize background positions and speeds
        int numLayers = backgroundImages.length;
        backgroundX1 = new int[numLayers];
        backgroundX2 = new int[numLayers];
        backgroundSpeeds = new int[numLayers];
        for (int i = 0; i < numLayers; i++) {
            backgroundX1[i] = 0;
            backgroundX2[i] = backgroundImages[i] != null ? backgroundImages[i].getWidth() : panelWidth;
            backgroundSpeeds[i] = (i + 1); // Each layer moves at different speed
        }
    }

    // Method to set the skin
    public void setSkin(String skinName) {
        this.skin = skinName;

        // Update skin image based on the selected skin
        try {
            if ("Skin1".equals(skin)) {
                skin1Image = ImageIO.read(new File("src/hellowrold/Assets/Skins/Skin1.png"));
            } else if ("Skin2".equals(skin)) {
                skin2Image = ImageIO.read(new File("src/hellowrold/Assets/Skins/Skin2.png"));
            } else {
                // Default skin
                defaultSkinImage = ImageIO.read(new File("src/hellowrold/Assets/Skins/SkinPlane1.png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            skin2Image = null;
            skin1Image = null;
            defaultSkinImage = null;
        }

        repaint(); // Repaint to apply new skin
    }

    private void setupKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver) {
                	System.out.println("Turn");  //                                                                CHANGE HEREREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
                    movingUp = !movingUp;
                }
            }
        });
        setFocusable(true);
    }
    

    public class BackgroundPanel extends JPanel {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Image backgroundImage;

        public BackgroundPanel(BufferedImage gameOverBackground) {
            this.backgroundImage = new ImageIcon(gameOverBackground).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
    
    
    private void setupGameOverPanel() {
        gameOverPanel = new BackgroundPanel(gameOverBackground);
        gameOverPanel.setLayout(new GridBagLayout());

        // Set the preferred, minimum, and maximum sizes
        Dimension panelSize = new Dimension(960, 540); // Adjust the width and height as needed
        gameOverPanel.setPreferredSize(panelSize);
        gameOverPanel.setMinimumSize(panelSize);
        gameOverPanel.setMaximumSize(panelSize);

        // Create and configure displayPanel
        JPanel displayPanel = new JPanel(new GridBagLayout());
        displayPanel.setOpaque(false); // Make sure it doesn't cover the background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHEAST; // Align to the top right
        gbc.insets = new Insets(6, 680, 5, 60); // Add padding (top, left, bottom, right)
        gbc.fill = GridBagConstraints.HORIZONTAL; // Allow horizontal expansion

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 50));
        scoreLabel.setForeground(new Color(88, 26, 0));
        gbc.gridx = 0; // Column 0
        gbc.gridy = 0; // Row 0
        gbc.gridwidth = 1; // Span one column
        displayPanel.add(scoreLabel, gbc);

        coinLabel = new JLabel("Coins: 0");
        coinLabel.setFont(new Font("Arial", Font.BOLD, 50));
        coinLabel.setForeground(new Color(88, 26, 0));
        gbc.gridy = 1; // Row 1 (below scoreLabel)
        displayPanel.add(coinLabel, gbc);

        // Create and configure buttonPanel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false); // Make sure it doesn't cover the background

        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.insets = new Insets(0, 20, 50, 570); // Adjust padding if needed
        gbc1.fill = GridBagConstraints.HORIZONTAL; // Allow horizontal expansion
        gbc1.gridx = 0; // Column 0

        JButton menuButton = new JButton(new ImageIcon(menuImage));
        menuButton.setBorder(BorderFactory.createEmptyBorder());
        menuButton.setPreferredSize(new Dimension(298, 78)); // Set button size to 300x80
        
        
        menuButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
            	menuButton.setIcon(new ImageIcon(menuImageHover));
            }

            public void mouseExited(MouseEvent e) {
            	menuButton.setIcon(new ImageIcon(menuImage));
            }
        });

        menuButton.addActionListener(e -> {
            initializeGame();
            gameOver = false;
            gameOverPanel.setVisible(false);
            ((Game) getTopLevelAncestor()).returnToMenu();
        });

        JButton retryButton = new JButton(new ImageIcon(retryImage));
        retryButton.setBorder(BorderFactory.createEmptyBorder());
        retryButton.setPreferredSize(new Dimension(298, 78)); // Set button size to 300x80
        
        retryButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
            	retryButton.setIcon(new ImageIcon(retryImageHover));
            }

            public void mouseExited(MouseEvent e) {
            	retryButton.setIcon(new ImageIcon(retryImage));
            }
        });
        
        retryButton.addActionListener(e -> {
            initializeGame();
            gameOver = false;
            gameOverPanel.setVisible(false);
            start();
        });

        // Add buttons to buttonPanel with GridBagConstraints
        gbc1.gridy = 0; // Row 0
        buttonPanel.add(retryButton, gbc1);
        gbc1.gridy = 1; // Row 1
        buttonPanel.add(menuButton, gbc1);

        // Add the button panel to the main panel
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0; // Column 0
        gbc2.gridy = 2; // Row 2
        gbc2.fill = GridBagConstraints.HORIZONTAL; // Stretch horizontally
        gbc2.weighty = 1.0; // Allow button panel to take up vertical space
        gameOverPanel.add(buttonPanel, gbc2);

        // Add the displayPanel to the main panel
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.gridx = 0; // Column 0
        gbc3.gridy = 0; // Row 0
        gbc3.weighty = 0.0; // Do not expand vertically
        gbc3.fill = GridBagConstraints.NONE; // Do not stretch
        gameOverPanel.add(displayPanel, gbc3);

        gameOverPanel.setVisible(false);
        add(gameOverPanel);
    }
    
    

    private void initializeGame() {
        rectX = (panelWidth - rectWidth) / 2;
        rectY = (panelHeight - rectHeight) / 2;
        movingUp = true;
        circles.clear();
        trails.clear(); // Clear trails when initializing game
        frameCount = 0;
        score = 0;
        coinCount = 0;
        gameOver = false;
        currentCoin = null; // No coin at the start
        lastCoinSpawnTime = System.currentTimeMillis(); // Reset coin spawn timer
    }

    public void start() {
        running = true;
        gameOver = false;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void stop() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running) {
            if (gameOver) {
                break;
            }
            update();
            repaint();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        showGameOver();
    }

    public void update() {
        final int BOTTOM_BUFFER = 25; // Buffer space above the bottom edge

        if (movingUp && rectY > moveSpeed) {
            rectY -= moveSpeed;
        } else if (!movingUp && rectY < panelHeight - rectHeight - moveSpeed - BOTTOM_BUFFER) {
            rectY += moveSpeed;
        }

     // Update background positions for each layer
        for (int i = 0; i < backgroundImages.length; i++) {
            backgroundX1[i] -= backgroundSpeeds[i];
            backgroundX2[i] -= backgroundSpeeds[i];

            // Reset positions if the background has moved off screen
            if (backgroundX1[i] + backgroundImages[i].getWidth() < 0) {
                backgroundX1[i] = backgroundX2[i] + backgroundImages[i].getWidth();
            }
            if (backgroundX2[i] + backgroundImages[i].getWidth() < 0) {
                backgroundX2[i] = backgroundX1[i] + backgroundImages[i].getWidth();
            }
        }
        
        
        int trailStartX = rectX;
        int trailStartY = rectY + rectHeight / 2;
        // Always create a trail at the middle of the rectangle's left end
        spawnCounter++;
        if (spawnCounter >= SPAWN_INTERVAL) {
        	trails.add(new Trail(trailStartX, trailStartY, TRAIL_WIDTH, TRAIL_HEIGHT));
        }
        
        
        

        frameCount++;
        if (frameCount >= spawnInterval) {
            spawnCircles();
            frameCount = 0;
        }

        checkScore(); // Check and update score

        // Update trails
        ArrayList<Trail> trailsToRemove = new ArrayList<>();
        for (Trail trail : trails) {
            trail.update();
            trail.move(); // Move the trail with the player
            if (!trail.isAlive()) {
                trailsToRemove.add(trail);
            }
        }
        trails.removeAll(trailsToRemove);

        // Update circles and coins, and check if they are off screen
        Iterator<Circle> circleIt = circles.iterator();
        while (circleIt.hasNext()) {
            Circle circle = circleIt.next();
            circle.move();
            if (circle.isOffScreen()) {
                circleIt.remove();
            }
        }

        if (currentCoin != null) {
            currentCoin.move();
            if (currentCoin.isCollected(rectX, rectY, rectWidth, rectHeight)) {
            	soundEffectPlayer.playSound("src/hellowrold/Assets/music/collectCoin.wav");
                coinCount++;
                CoinSaver.saveCoinCount(coinCount); // Save the updated coin count
                currentCoin = null; // Remove the collected coin
            } else if (currentCoin.isOffScreen()) {
                currentCoin = null; // Remove the coin if it goes off screen
            }
        } else {
            // Check if it's time to spawn a new coin
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - lastCoinSpawnTime;
            if (elapsedTime >= 10000 && elapsedTime <= 15000) {
                spawnCoin();
                lastCoinSpawnTime = currentTime;
            }
        }

        checkCollisions(); // Check for collisions after updates
    }

    
    
    
    
    
    private void checkScore() {
        for (Circle circle : circles) {
            if (circle.getX() < rectX && !circle.hasPassed()) {
                circle.setPassed(true);
                score++; // Increment score for each circle passed
            }
        }
    }

    private void spawnCircles() {
        int diameter = rand.nextInt(201) + 150;
        int x = panelWidth + diameter;
        int y = rand.nextInt(panelHeight - diameter);
        circles.add(new Circle(x, y, diameter, circleSpeed));
    }

    private void spawnCoin() {
        // Ensure the coin does not spawn inside a green circle
        boolean validSpawn = false;
        int x = 0, y = 0, size = 32; // Fixed coin size of 25x25

        while (!validSpawn) {
            x = panelWidth + size;
            y = rand.nextInt(panelHeight - size);

            validSpawn = true;
            for (Circle circle : circles) {
                if (circle.contains(x, y, size, size)) {
                    validSpawn = false;
                    break;
                }
            }
        }

        currentCoin = new Coin(x, y, size, size, circleSpeed);
    }

    private void checkCollisions() {
        for (Circle circle : circles) {
            if (circle.intersects(rectX, rectY, rectWidth, rectHeight)) {
                gameOver = true;
                break;
            }
        }
    }
    

    private void showGameOver() {
        running = false;
        HighScoreManager.saveHighScore(score); // Save the high score
        scoreLabel.setText("" + score);
        coinLabel.setText("" +coinCount);
        gameOverPanel.setVisible(true);
    }

    
    
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
     

     // Draw the scrolling background layers
        for (int i = 0; i < backgroundImages.length; i++) {
            if (backgroundImages[i] != null) {
                g2d.drawImage(backgroundImages[i], backgroundX1[i], 0, panelWidth, panelHeight, null);
                g2d.drawImage(backgroundImages[i], backgroundX2[i], 0, panelWidth, panelHeight, null);
            }
        }

        // Draw the game elements
        for (Trail trail : trails) {
            trail.draw(g2d);
        }

        for (Circle circle : circles) {
            circle.draw(g2d);
        }

        if (currentCoin != null) {
            currentCoin.draw(g2d);
        }

        // Draw the rectangle with the selected skin
        BufferedImage skinImage = defaultSkinImage; // Default to the default skin

        if ("Skin1".equals(skin) && skin1Image != null) {
            skinImage = skin1Image;
        } else if ("Skin2".equals(skin) && skin2Image != null) {
            skinImage = skin2Image;
        }

        if (skinImage != null) {
            g2d.drawImage(skinImage, rectX, rectY, rectWidth, rectHeight, null);
        } else {
            g2d.setColor(Color.RED);
            g2d.fillRect(rectX, rectY, rectWidth, rectHeight);
        }

        // Display the score and coin count
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("Score: " + score, 10, 30);
        g2d.drawString("Coins: " + coinCount, 10, 60);

        if (gameOver) {
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, panelWidth, panelHeight);
        }
    }

    private class Circle {
        private int x, y, diameter, speed;
        private boolean passed;
        private BufferedImage image; // Image to cover the circle

        public Circle(int x, int y, int diameter, int speed) {
            this.x = x;
            this.y = y;
            this.diameter = diameter;
            this.speed = speed;
            this.passed = false;
            
         // Randomly select an image from the loaded images
            int imageIndex = rand.nextInt(circleImages.length);
            this.image = circleImages[imageIndex];
        }

        public void move() {
            x -= speed;
        }

        public void draw(Graphics2D g2d) {
        	if (image != null) {
                // Create a circular clip
                Ellipse2D.Double circleClip = new Ellipse2D.Double(x, y, diameter, diameter);
                Area clipArea = new Area(circleClip);

                // Save the original clip
                Shape originalClip = g2d.getClip();

                // Set the new clip
                g2d.setClip(clipArea);

                // Draw the image
                g2d.drawImage(image, x, y, diameter, diameter, null);

                // Restore the original clip
                g2d.setClip(originalClip);
            } else {
                // Fallback to drawing a green circle if image is not loaded
                g2d.setColor(Color.GREEN);
                g2d.fillOval(x, y, diameter, diameter);
            }
        }

        
        
        
        
        public boolean intersects(int rectX, int rectY, int rectWidth, int rectHeight) {
            int circleRadius = diameter / 2;
            int circleCenterX = x + circleRadius;
            int circleCenterY = y + circleRadius;

            int rectCenterX = rectX + rectWidth / 2;
            int rectCenterY = rectY + rectHeight / 2;

            int distX = Math.abs(circleCenterX - rectCenterX);
            int distY = Math.abs(circleCenterY - rectCenterY);

            if (distX > (rectWidth / 2 + circleRadius) || distY > (rectHeight / 2 + circleRadius)) {
                return false;
            }

            if (distX <= (rectWidth / 2) || distY <= (rectHeight / 2)) {
                return true;
            }

            int cornerDistSq = (distX - rectWidth / 2) * (distX - rectWidth / 2) +
                               (distY - rectHeight / 2) * (distY - rectHeight / 2);

            return cornerDistSq <= (circleRadius * circleRadius);
        }

        public boolean isOffScreen() {
            return x + diameter < 0;
        }

        public int getX() {
            return x;
        }

        public boolean hasPassed() {
            return passed;
        }

        public void setPassed(boolean passed) {
            this.passed = passed;
        }

        public boolean contains(int coinX, int coinY, int coinWidth, int coinHeight) {
            int circleRadius = diameter / 2;
            int circleCenterX = x + circleRadius;
            int circleCenterY = y + circleRadius;

            int coinCenterX = coinX + coinWidth / 2;
            int coinCenterY = coinY + coinHeight / 2;

            int distX = Math.abs(circleCenterX - coinCenterX);
            int distY = Math.abs(circleCenterY - coinCenterY);

            if (distX > (coinWidth / 2 + circleRadius) || distY > (coinHeight / 2 + circleRadius)) {
                return false;
            }

            if (distX <= (coinWidth / 2) || distY <= (coinHeight / 2)) {
                return true;
            }

            int cornerDistSq = (distX - coinWidth / 2) * (distX - coinWidth / 2) +
                               (distY - coinHeight / 2) * (distY - coinHeight / 2);

            return cornerDistSq <= (circleRadius * circleRadius);
        }
    }

    private class Coin {
        private int x, y, width, height, speed;

        public Coin(int x, int y, int width, int height, int speed) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.speed = speed;
        }

        public void move() {
            x -= speed;
        }

        public void draw(Graphics2D g2d) {
        	g2d.drawImage(coinImage, x, y, width, height, null);
        }
        


        public boolean isOffScreen() {
            return x + width < 0;
        }

        public boolean isCollected(int rectX, int rectY, int rectWidth, int rectHeight) {
            return x < rectX + rectWidth &&
                   x + width > rectX &&
                   y < rectY + rectHeight &&
                   y + height > rectY;
        }
    }

    private class Trail {
        private int x, y, width, height;
        private final int LIFETIME = 25; // Lifetime in frames (adjust as needed)
        private int age;
        private final double ANGLE = Math.toRadians(45); // Rotation angle in radians

        public Trail(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.age = 0;
        }

        public void update() {
            age++;
        }

        public void move() {
            x -= moveSpeed; // Move the trail with the player
        }

        public void draw(Graphics2D g2d) {
        	// Save the current transform
            AffineTransform originalTransform = g2d.getTransform();
            
         // Create a new transform for rotation
            AffineTransform transform = new AffineTransform();
            transform.rotate(ANGLE, x + width / 2, y + height / 2); // Rotate around the center of the rectangle
            transform.translate(x, y); // Translate to the position of the rectangle
            
         // Apply the rotation transform
            g2d.setTransform(transform);
            
            
         // Draw the trail rectangle with the rotated transform
            float alpha = 1.0f - Math.min(1.0f, (float) age / LIFETIME); // Ensure alpha is between 0 and 1
            g2d.setColor(new Color(128, 128, 128, (int) (alpha * 255))); // Gradient color
            g2d.fillRect(0, 0, width, height);

            // Restore the original transform
            g2d.setTransform(originalTransform);
        }
        
            

        public boolean isAlive() {
            return age < LIFETIME;
        }
    }
}