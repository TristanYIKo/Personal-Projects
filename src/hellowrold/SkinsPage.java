package hellowrold;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class SkinsPage extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Game game;
    private BufferedImage coinImage;
    private BufferedImage defaultSkinImage;
    private BufferedImage skin1Image;
    private BufferedImage skin2Image;
    private BufferedImage skin1GrayImage;
    private BufferedImage skin2GrayImage;
    private BufferedImage cycleForwardImage;
    private BufferedImage cycleForwardImageHover;
    private BufferedImage cycleBackwardImage;
    private BufferedImage cycleBackwardImageHover;
    private BufferedImage ownedImage;
    private BufferedImage cost100Image;
    private BufferedImage cost200Image;
    private BufferedImage backToMainMenuImage;
    private BufferedImage backToMainMenuImageHover;
    private BufferedImage buyImage;
    private BufferedImage buyImageHover;
    private BufferedImage selectImage;
    private BufferedImage selectImageHover;
    private JLabel skinImageLabel;
    private JLabel costImageLabel;
    private Set<String> purchasedSkins;
    private String[] skinNames = {"Default", "Skin1", "Skin2"};
    private int currentSkinIndex;
    public SkinsPage(Game game) {
        this.game = game;
        setLayout(new GridBagLayout());
        new Random();
        CoinSaver.loadCoinCount();
        GridBagConstraints gbc = new GridBagConstraints();
        
        setBackground(new Color(34, 34, 34)); // Dark grey background for the entire panel
        
        // Load images for skins    
        try {
        	coinImage = ImageIO.read(new File("src/hellowrold/Assets/Images/CoinImage.png"));
            defaultSkinImage = ImageIO.read(new File("src/hellowrold/Assets/Skins/SkinPlane1.png"));
            skin1Image = ImageIO.read(new File("src/hellowrold/Assets/Skins/Skin1.png"));
            skin2Image = ImageIO.read(new File("src/hellowrold/Assets/Skins/Skin2.png"));
            ownedImage = ImageIO.read(new File("src/hellowrold/Assets/Images/ownedImage.png"));
            cost100Image = ImageIO.read(new File("src/hellowrold/Assets/Images/cost100Image.png"));
            cost200Image = ImageIO.read(new File("src/hellowrold/Assets/Images/cost200Image.png"));
            cycleForwardImage = ImageIO.read(new File("src/hellowrold/Assets/Images/cycleForwardButton.png"));
            cycleForwardImageHover = ImageIO.read(new File("src/hellowrold/Assets/Images/cycleForwardButtonHover.png"));
            cycleBackwardImage = ImageIO.read(new File("src/hellowrold/Assets/Images/cycleBackwardButton.png"));
            cycleBackwardImageHover = ImageIO.read(new File("src/hellowrold/Assets/Images/cycleBackwardButtonHover.png"));
            backToMainMenuImage = ImageIO.read(new File("src/hellowrold/Assets/Images/backToMainMenuImage.png"));
            backToMainMenuImageHover = ImageIO.read(new File("src/hellowrold/Assets/Images/backToMainMenuImageHover.png"));
            buyImage = ImageIO.read(new File("src/hellowrold/Assets/Images/buyButton.png"));
            buyImageHover = ImageIO.read(new File("src/hellowrold/Assets/Images/buyButtonHover.png"));
            selectImage = ImageIO.read(new File("src/hellowrold/Assets/Images/selectButton.png"));
            selectImageHover = ImageIO.read(new File("src/hellowrold/Assets/Images/selectButtonHover.png"));
            
            
            // Grayscale versions
            skin1GrayImage = ImageIO.read(new File("src/hellowrold/Assets/Skins/GreyScaleSkin1.png"));
            skin2GrayImage = ImageIO.read(new File("src/hellowrold/Assets/Skins/GreyScaleSkin2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load purchased skins
        purchasedSkins = loadPurchasedSkins();
        currentSkinIndex = 0; // Start with the first skin

        // Panel for skin image and cycling buttons
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new GridBagLayout());
        imagePanel.setBackground(new Color(34, 34, 34)); // Dark grey background for the image panel
        GridBagConstraints imageGbc = new GridBagConstraints();
        
        // Button to cycle through skins backwards
        JButton cycleBackwardButton = new JButton(new ImageIcon(cycleBackwardImage));
        cycleBackwardButton.setBorder(BorderFactory.createEmptyBorder()); // Remove border
        cycleBackwardButton.setPreferredSize(new java.awt.Dimension(58, 78));

       cycleBackwardButton.addMouseListener(new MouseAdapter() {
            
            public void mouseEntered(MouseEvent e) {
            	cycleBackwardButton.setIcon(new ImageIcon(cycleBackwardImageHover));
            }
            
            public void mouseExited(MouseEvent e) {
            	cycleBackwardButton.setIcon(new ImageIcon(cycleBackwardImage));
            }
        });
       
       cycleBackwardButton.addActionListener(e -> cycleSkin(-1)); // Backward direction
       imageGbc.gridx = 0;
       imageGbc.gridy = 0;
       imageGbc.insets.set(10, 20, 10, 10); // Adjusted insets for positioning
       imagePanel.add(cycleBackwardButton, imageGbc);
        
       
    // Label for current skin image
       skinImageLabel = new JLabel();
       imageGbc.gridx = 1;
       imageGbc.gridy = 0; // Ensure skin image is in the first row
       imageGbc.weightx = 1.0; // Allow image label to expand and fill space horizontally
       imagePanel.add(skinImageLabel, imageGbc);

       // Initialize costImageLabel
       costImageLabel = new JLabel();
       imageGbc.gridx = 1;
       imageGbc.gridy = 1; // Place cost image right below the skin image
       imageGbc.insets.set(10, 0, 10, 0); // Adjust insets for positioning
       imagePanel.add(costImageLabel, imageGbc);

        
        // Button to cycle through skins forward
        JButton cycleForwardButton = new JButton(new ImageIcon(cycleForwardImage));
        cycleForwardButton.setBorder(BorderFactory.createEmptyBorder()); // Remove border
        cycleForwardButton.setPreferredSize(new java.awt.Dimension(58, 78));
        
        cycleForwardButton.addMouseListener(new MouseAdapter() {
            
            public void mouseEntered(MouseEvent e) {
            	cycleForwardButton.setIcon(new ImageIcon(cycleForwardImageHover));
            }
            
            public void mouseExited(MouseEvent e) {
            	cycleForwardButton.setIcon(new ImageIcon(cycleForwardImage));
            }
        });

        
        cycleForwardButton.addActionListener(e -> cycleSkin(1)); // Forward direction
        imageGbc.gridx = 2;
        imageGbc.gridy = 0;
        imageGbc.insets.set(10, 10, 10, 20); // Adjusted insets for positioning
        imagePanel.add(cycleForwardButton, imageGbc);

        // Add image panel to the main panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(imagePanel, gbc);	


        
        // Panel for centered buttons
        JPanel centeredButtonPanel = new JPanel();
        centeredButtonPanel.setLayout(new GridBagLayout());
        centeredButtonPanel.setBackground(new Color(34, 34, 34)); // Dark grey background for the button panel
        GridBagConstraints centeredButtonGbc = new GridBagConstraints();
        centeredButtonGbc.gridx = 0;
        centeredButtonGbc.gridy = 0;
        centeredButtonGbc.insets.set(10, 10, 10, 10);


        // Button to buy the current skin
        JButton buyButton = new JButton(new ImageIcon(buyImage));
        buyButton.setBorder(BorderFactory.createEmptyBorder()); // Remove border
        buyButton.setPreferredSize(new java.awt.Dimension(118, 40));
        buyButton.addMouseListener(new MouseAdapter() {
            
            public void mouseEntered(MouseEvent e) {
            	buyButton.setIcon(new ImageIcon(buyImageHover));
            }
            
            public void mouseExited(MouseEvent e) {
            	buyButton.setIcon(new ImageIcon(buyImage));
            }
        });

        
        buyButton.addActionListener(e -> {
            buySkin(skinNames[currentSkinIndex]);
            updateSkin(skinNames[currentSkinIndex]); // Update the skin after buying
        });
        centeredButtonGbc.gridy = 3;
        centeredButtonPanel.add(buyButton, centeredButtonGbc);

        
        // Button to select the current skin
        JButton selectButton = new JButton(new ImageIcon(selectImage));
        selectButton.setBorder(BorderFactory.createEmptyBorder()); // Remove border
        selectButton.setPreferredSize(new java.awt.Dimension(118, 40));
        selectButton.addMouseListener(new MouseAdapter() {
            
            public void mouseEntered(MouseEvent e) {
            	selectButton.setIcon(new ImageIcon(selectImageHover));
            }
            
            public void mouseExited(MouseEvent e) {
            	selectButton.setIcon(new ImageIcon(selectImage));
            }
        });

        selectButton.addActionListener(e -> selectSkinForUse(skinNames[currentSkinIndex]));
        centeredButtonGbc.gridy = 4;
        centeredButtonPanel.add(selectButton, centeredButtonGbc);
        
        
     // Button to return to the main menu
        JButton backButton = new JButton(new ImageIcon(backToMainMenuImage));
        backButton.setBorder(BorderFactory.createEmptyBorder()); // Remove border
        backButton.setPreferredSize(new java.awt.Dimension(98, 40));

        backButton.addMouseListener(new MouseAdapter() {
            
            public void mouseEntered(MouseEvent e) {
            	backButton.setIcon(new ImageIcon(backToMainMenuImageHover));
            }
            
            public void mouseExited(MouseEvent e) {
            	backButton.setIcon(new ImageIcon(backToMainMenuImage));
            }
        });
        

        backButton.addActionListener(e -> game.returnToMenu());
        centeredButtonGbc.insets.set(0, 10, 10, 770); // Adjusted insets for positioning
        centeredButtonGbc.gridy = 4;
        centeredButtonPanel.add(backButton, centeredButtonGbc);

        // Add centered button panel further down below the skin image
        gbc.gridy = 1;
        gbc.weighty = -10;
        add(centeredButtonPanel, gbc);

        // Display the initial skin image
        updateSkin(skinNames[currentSkinIndex]);
    }

    private void cycleSkin(int direction) {
        // Move to the next or previous skin based on direction
        currentSkinIndex = (currentSkinIndex + direction + skinNames.length) % skinNames.length;
        updateSkin(skinNames[currentSkinIndex]);
    }

    private void updateSkin(String skinName) {
        BufferedImage imageToDisplay = null;
        BufferedImage costImageToDisplay = null;
        
        if (purchasedSkins.contains(skinName) || skinName.equals("Default")) {
            switch (skinName) {
                case "Skin1":
                    imageToDisplay = skin1Image;
                    break;
                case "Skin2":
                    imageToDisplay = skin2Image;
                    break;
                case "Default":
                default:
                    imageToDisplay = defaultSkinImage;
                    break;
            }
            costImageToDisplay = ownedImage; // Display "owned" image
        } else {
            switch (skinName) {
                case "Skin1":
                    imageToDisplay = skin1GrayImage;
                    costImageToDisplay = cost100Image; // Display cost image for Skin1
                    break;
                case "Skin2":
                    imageToDisplay = skin2GrayImage;
                    costImageToDisplay = cost200Image; // Display cost image for Skin2
                    break;
                case "Default":
                default:
                    imageToDisplay = defaultSkinImage;
                    break;
            }
        }
        if (imageToDisplay != null) {
            skinImageLabel.setIcon(new ImageIcon(imageToDisplay.getScaledInstance(300, 150, BufferedImage.SCALE_SMOOTH)));
        } else {
            skinImageLabel.setIcon(null);
        }
        
        if (costImageToDisplay != null) {
            costImageLabel.setIcon(new ImageIcon(costImageToDisplay.getScaledInstance(200, 75, BufferedImage.SCALE_SMOOTH)));
        } else {
            costImageLabel.setIcon(null);
        }
        
        revalidate();
        repaint();
    }

    private void buySkin(String skinName) {
        int cost = getSkinCost(skinName);
        if (purchasedSkins.contains(skinName) || cost == 0) {
            showCustomDialog("You already own this skin.", "Purchase Unnecessary");
            return;
        }
        int totalCoins = CoinSaver.loadCoinCount();
        if (totalCoins >= cost) {
            totalCoins -= cost;
            CoinSaver.saveCoinCount(-cost); // Deduct coins
            purchasedSkins.add(skinName);
            savePurchasedSkins();
            game.updateSkin(skinName);
            showCustomDialog("You have successfully purchased the skin!", "Purchase Successful");
            updateSkin(skinName); // Update the skin and cost image after buying
        } else {
            showCustomDialog("Not enough coins to purchase this skin.", "Purchase Failed");
        }
    }

    private int getSkinCost(String skinName) {
        switch (skinName) {
            case "Skin1":
                return 100;
            case "Skin2":
                return 200;
            case "Default":
            default:
                return 0;
        }
    }

    private void selectSkinForUse(String skinName) {
        if (purchasedSkins.contains(skinName) || skinName.equals("Default")) {
            game.updateSkin(skinName);
            showCustomDialog("Skin selection successful!", "Selection Successful");
        } else {
            showCustomDialog("You do not own this skin. Please purchase it first.", "Selection Failed");
        }
    }

    private void showCustomDialog(String message, String title) {
        JDialog dialog = new JDialog();
        dialog.setTitle(title);
        dialog.setUndecorated(true); // Remove window decorations
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);

        JPanel contentPane = new JPanel();
        contentPane.setBackground(new Color(34, 34, 34));
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets.set(10, 10, 10, 10);

        JLabel messageLabel = new JLabel(message);
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        contentPane.add(messageLabel, gbc);

        JButton okButton = new JButton("OK");
        okButton.setBackground(new Color(34, 34, 34));
        okButton.setForeground(Color.WHITE);
        okButton.addActionListener(e -> dialog.dispose());
        gbc.gridy = 1;
        contentPane.add(okButton, gbc);

        dialog.setContentPane(contentPane);
        dialog.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        drawYellowSquareWithNumber(g2d, CoinSaver.loadCoinCount(), 15, 15, 40);
    }

    private Set<String> loadPurchasedSkins() {
        // Load purchased skins from file or return an empty set
        Set<String> purchasedSkins = new HashSet<>();
        try (Scanner scanner = new Scanner(new File("src/hellowrold/Assets/data/purchased_skins.txt"))) {
            while (scanner.hasNextLine()) {
                purchasedSkins.add(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return purchasedSkins;
    }
    
    private void updateCoinCount() {
        CoinSaver.loadCoinCount();
    }

    private void savePurchasedSkins() {
        // Save purchased skins to file
        try (PrintWriter writer = new PrintWriter(new FileWriter("src/hellowrold/Assets/data/purchased_skins.txt"))) {
            for (String skin : purchasedSkins) {
                writer.println(skin);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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