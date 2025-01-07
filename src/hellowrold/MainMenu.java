package hellowrold;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenu extends JPanel {
    /**
	 * 
	 */	
	private static final long serialVersionUID = 1L;
	private Game game;
    private String skin;
    private BufferedImage defaultSkinImage;
    private BufferedImage skin1Image;
    private BufferedImage skin2Image;
    private BufferedImage playButton;
    private BufferedImage playButtonHover;
    private BufferedImage titleImage;
    private BufferedImage skinsImage;
    private BufferedImage skinsButtonHover;
    private BufferedImage quitImage;
    private BufferedImage quitButtonHover;
    private BufferedImage highScoreImage;
    private BufferedImage coinImage;
    private BufferedImage backgroundImage;
    private BufferedImage settingsImage;
    private BufferedImage settingsButtonHover;
    private JLabel highScoreLabel;

    public MainMenu(Game game) {
        this.game = game;
        this.skin = "Default";

        highScoreLabel = new JLabel();
        CoinSaver.loadCoinCount();

        new Random();

        try {
            defaultSkinImage = ImageIO.read(new File("src/hellowrold/Assets/Skins/SkinPlane1.png"));
            skin1Image = ImageIO.read(new File("src/hellowrold/Assets/Skins/Skin1.png"));
            skin2Image = ImageIO.read(new File("src/hellowrold/Assets/Skins/Skin2.png"));
            playButton = ImageIO.read(new File("src/hellowrold/Assets/Images/PlayButton.png"));
            playButtonHover = ImageIO.read(new File("src/hellowrold/Assets/Images/PlayButtonHover.png"));
            titleImage = ImageIO.read(new File("src/hellowrold/Assets/Images/orbitTitleImage.png"));
            skinsImage = ImageIO.read(new File("src/hellowrold/Assets/Images/skinsButton.png"));
            skinsButtonHover = ImageIO.read(new File("src/hellowrold/Assets/Images/skinsButtonHover.png"));
            quitImage = ImageIO.read(new File("src/hellowrold/Assets/Images/quitButton.png"));
            quitButtonHover = ImageIO.read(new File("src/hellowrold/Assets/Images/quitButtonHover.png"));
            coinImage = ImageIO.read(new File("src/hellowrold/Assets/Images/CoinImage.png"));
            highScoreImage = ImageIO.read(new File("src/hellowrold/Assets/Images/highScoreImage.png"));
            backgroundImage = ImageIO.read(new File("src/hellowrold/Assets/Backgrounds/mainMenuBackground.png"));
            settingsImage = ImageIO.read(new File("src/hellowrold/Assets/Images/settingsButton.png"));
            settingsButtonHover = ImageIO.read(new File("src/hellowrold/Assets/Images/settingsButtonHover.png"));
        } catch (IOException e) {
            e.printStackTrace();
            defaultSkinImage = null;
            skin1Image = null;
            skin2Image = null;
            playButton = null;
            playButtonHover = null;
            titleImage = null;
            skinsImage = null;
            skinsButtonHover = null;
            quitImage = null;
            quitButtonHover = null;
            coinImage = null;
            highScoreImage = null;
            backgroundImage = null;
            settingsImage = null;
            settingsButtonHover = null;
        }

        setBorder(BorderFactory.createEmptyBorder());
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel(new ImageIcon(titleImage));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder());
        titleLabel.setPreferredSize(new java.awt.Dimension(300, 90));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.8;
        add(titleLabel, gbc);

        JButton startButton = new JButton(new ImageIcon(playButton));
        startButton.setBorder(BorderFactory.createEmptyBorder());
        startButton.setPreferredSize(new java.awt.Dimension(235, 58));

        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startButton.setIcon(new ImageIcon(playButtonHover));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                startButton.setIcon(new ImageIcon(playButton));
            }
        });

        startButton.addActionListener(e -> game.startGame());
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weighty = 0.2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(startButton, gbc);

        JButton skinsButton = new JButton(new ImageIcon(skinsImage));
        skinsButton.setBorder(BorderFactory.createEmptyBorder());
        skinsButton.setPreferredSize(new java.awt.Dimension(98, 40));

        skinsButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                skinsButton.setIcon(new ImageIcon(skinsButtonHover));
            }

            public void mouseExited(MouseEvent e) {
                skinsButton.setIcon(new ImageIcon(skinsImage));
            }
        });

        skinsButton.addActionListener(e -> {
            game.showSkinsPage();
            updateCoinCount();
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weighty = 0.2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 10, 10, 5);
        add(skinsButton, gbc);

        JButton settingsButton = new JButton(new ImageIcon(settingsImage));
        settingsButton.setBorder(BorderFactory.createEmptyBorder());
        settingsButton.setPreferredSize(new java.awt.Dimension(158, 40));

        settingsButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                settingsButton.setIcon(new ImageIcon(settingsButtonHover));
            }

            public void mouseExited(MouseEvent e) {
                settingsButton.setIcon(new ImageIcon(settingsImage));
            }
        });

        settingsButton.addActionListener(e -> game.showSettingsPage());
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weighty = 0.2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 5, 10, 5);
        add(settingsButton, gbc);

        JButton quitButton = new JButton(new ImageIcon(quitImage));
        quitButton.setBorder(BorderFactory.createEmptyBorder());
        quitButton.setPreferredSize(new java.awt.Dimension(98, 40));

        quitButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                quitButton.setIcon(new ImageIcon(quitButtonHover));
            }

            public void mouseExited(MouseEvent e) {
                quitButton.setIcon(new ImageIcon(quitImage));
            }
        });
        quitButton.addActionListener(e -> System.exit(0));
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weighty = 0.2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 5, 10, 10);
        add(quitButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        add(highScoreLabel, gbc);
    }
    
    

    private void updateCoinCount() {
        CoinSaver.loadCoinCount();
    }

    public void setSkin(String skinName) {
        this.skin = skinName;
        repaint();

        if (game.getGamePanel() != null) {
            game.getGamePanel().setSkin(skinName);
        }
    }

    public void updateHighScore() {
        HighScoreManager.loadHighScore();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }

        drawRandomWhiteSpots(g2d);
        drawYellowSquareWithNumber(g2d, CoinSaver.loadCoinCount(), 15, 15, 40);
        drawHighScoreDisplay(g2d, CoinSaver.loadCoinCount(), 700, 19, 150, 30);
    }

    private void drawRandomWhiteSpots(Graphics2D g2d) {
        BufferedImage skinImage = null;
        if ("Default".equals(skin)) {
            skinImage = defaultSkinImage;
        } else if ("Skin1".equals(skin)) {
            skinImage = skin1Image;
        } else if ("Skin2".equals(skin)) {
            skinImage = skin2Image;
        } else {
            skinImage = defaultSkinImage;
        }

        if (skinImage != null) {
            g2d.drawImage(skinImage, (getWidth() - 50) / 2 - 15, (getHeight() - 20) / 2 - 10, 100, 45, this);
        }
    }

    private void drawHighScoreDisplay(Graphics2D g2d, int number, int x, int y, int width, int height) {
        if (highScoreImage != null) {
            g2d.drawImage(highScoreImage, x, y, width, height, this);
        }

        updateHighScore();

        g2d.setColor(new Color(254, 148, 55));
        g2d.setFont(new Font("Arial", Font.PLAIN, 32));
        int scoreCount2 = HighScoreManager.loadHighScore();
        String scoreText2 = String.valueOf(scoreCount2);

        g2d.drawString(scoreText2, 859, 45);

        g2d.setColor(new Color(212, 100, 2));
        g2d.setFont(new Font("Arial", Font.PLAIN, 32));
        int scoreCount = HighScoreManager.loadHighScore();
        String scoreText = String.valueOf(scoreCount);

        g2d.drawString(scoreText, 857, 45);
    }

    private void drawYellowSquareWithNumber(Graphics2D g2d, int number, int x, int y, int size) {
        if (coinImage != null) {
            g2d.drawImage(coinImage, x, y, size, size, this);
        }

        updateCoinCount();
        g2d.setColor(new Color(255, 255, 0));
        g2d.setFont(new Font("Arial", Font.PLAIN, 30));
        String justX = "X ";
        int coinCount = CoinSaver.loadCoinCount();
        String numberText = String.valueOf(coinCount);
        g2d.drawString(justX, 60, 45);
        g2d.drawString(numberText, 83, 45);
    }
}