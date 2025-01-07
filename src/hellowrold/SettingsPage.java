package hellowrold;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsPage extends JPanel {
    private Game game;
    private SoundEffectPlayer soundEffectPlayer;
    private JButton toggleMusicButton;
    private JButton toggleSoundEffectsButton;
    private boolean isMusicPlaying = true;
    private boolean isSoundEffectsOn = true;

    public SettingsPage(Game game) {
        this.game = game;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(titleLabel, gbc);

        toggleMusicButton = new JButton("Music - ON");
        toggleMusicButton.setFont(new Font("Arial", Font.BOLD, 30));
        toggleMusicButton.setPreferredSize(new Dimension(300, 80));
        toggleMusicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleMusic();
            }
        });
        gbc.gridy = 1;
        add(toggleMusicButton, gbc);
        
        
        
   

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 30));
        backButton.setPreferredSize(new Dimension(300, 80));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.returnToMenu();
            }
        });
        gbc.gridy = 2;
        add(backButton, gbc);
    }

    private void toggleMusic() {
        if (isMusicPlaying) {
            game.getBackgroundMusic().stop();
            toggleMusicButton.setText("Music - OFF");
        } else {
            game.getBackgroundMusic().play();
            toggleMusicButton.setText("Music - ON");
        }
        isMusicPlaying = !isMusicPlaying;
    }
    
    
}