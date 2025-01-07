package hellowrold;    // save 8


import javax.swing.JFrame;
import java.awt.CardLayout;
import java.awt.GraphicsEnvironment;
import java.util.Set;

public class Game extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int WIDTH = 960; // 16:9 ratio 960, 1280, 1440
    int HEIGHT = 540; // 16:9 ratio 540, 720, 810
    private CardLayout cardLayout;
    private MainMenu mainMenu;
    private GamePanel gamePanel;
    private SkinsPage skinsPage;
    private SettingsPage settingsPage;
    private MusicPlayer backgroundMusic;
    private SoundEffectPlayer soundEffectPlayer;
    private Set<String> purchasedSkins;
    public Game() {
    	
        setTitle("Orbit made by Tristan Ko");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
     // Set up the graphics device for full screen
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.getDefaultScreenDevice();


        cardLayout = new CardLayout();
        setLayout(cardLayout);

        mainMenu = new MainMenu(this);
        gamePanel = new GamePanel(WIDTH, HEIGHT);
        skinsPage = new SkinsPage(this);
        settingsPage = new SettingsPage(this);

        add(mainMenu, "MainMenu");
        add(gamePanel, "GamePanel");
        add(skinsPage, "SkinsPage");
        add(settingsPage, "SettingsPage");
        
        
        cardLayout.show(getContentPane(), "MainMenu");

        setVisible(true);
        
     // Start playing background music
        backgroundMusic = new MusicPlayer("src/hellowrold/Assets/music/themeMusic.wav");

        backgroundMusic.play();
        backgroundMusic.setVolume(0.7f); // Set the volume to 50%
    }
    
    // Method to check if a skin is purchased
    public boolean isSkinPurchased(String skinName) {
        return purchasedSkins.contains(skinName) || "Default".equals(skinName);
    }
    
 
    public void startGame() {
        cardLayout.show(getContentPane(), "GamePanel");
        gamePanel.requestFocusInWindow();
        gamePanel.start();
    }

    public void returnToMenu() {
        cardLayout.show(getContentPane(), "MainMenu");
        gamePanel.stop();
    }

    public void showSkinsPage() {
        cardLayout.show(getContentPane(), "SkinsPage");
    }
    
    public void showSettingsPage() {
    	cardLayout.show(getContentPane(), "SettingsPage");
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
    
    public void updateSkin(String skinName) {
        mainMenu.setSkin(skinName);
        gamePanel.setSkin(skinName);
    }
    
    public void playSoundEffect(String soundFilePath) {
        soundEffectPlayer.playSound(soundFilePath);
    }
    
    public SoundEffectPlayer getSoundEffectPlayer() { 
        return soundEffectPlayer;
    }
    
    public void setMusicVolume(float volume) {
        backgroundMusic.setVolume(volume);
    }
    
    public MusicPlayer getBackgroundMusic() {
        return backgroundMusic;
    }

	public GamePanel getMainMenu() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) {
        new Game();
    }
}