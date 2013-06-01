package wordguesser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.*;

/**
 * A word guessing game with a JFrame GUI allowing the user to see the shuffled
 * letters and to enter their guesses
 * 
 * @author Daniel Kapit
 * 
 */
public class WordGUI extends JFrame {

	static Game g = new Game();
	static String word = "                ";
	static JTextField word_length = new JTextField(5);
	JButton go = new JButton("Start Game");
	static ArrayList<Character> shuffled = Game.shuffle(word);
	static int goal = 0;
	static ArrayList<String> targets = new ArrayList<String>();
	static ArrayList<String> found;
	static JLabel prompt = new JLabel("Enter attempt:");
	static JTextField entry = new JTextField(10);
	static JTextArea foo = new JTextArea(5, 5);
	static JLabel score = new JLabel("Score: 0/0");

	public static void main(String[] args) {
		WordGUI game = new WordGUI();
		game.setVisible(true);
	}

	/**
	 * Constructs a game object, setting the size and adding all of the
	 * components to the frame.
	 */
	public WordGUI() {
		g.createWords();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Word Game");
		setSize(800, 800);
		// Adds basic controls to frame
		JPanel controls = new JPanel();
		go.addActionListener(new Start());
		controls.add(new JLabel("Word Length:"), BorderLayout.WEST);
		controls.add(word_length, BorderLayout.WEST);
		controls.add(go, BorderLayout.EAST);
		add(controls, BorderLayout.SOUTH);
		JPanel cont2 = new JPanel();
		cont2.add(prompt, BorderLayout.WEST);
		cont2.add(entry, BorderLayout.WEST);
		entry.addKeyListener(new Submit());
		cont2.add(entry, BorderLayout.WEST);
		add(cont2, BorderLayout.EAST);
		add(new Panel(), BorderLayout.CENTER);
		// Adds the frame that displays found words.
		foo.setEditable(false);
		foo.setPreferredSize(new Dimension(20, 800));
		foo.setWrapStyleWord(true);
		foo.setText("Found: \n");
		JScrollPane scroll = new JScrollPane(foo);
		add(scroll, BorderLayout.WEST);
		add(score, BorderLayout.NORTH);
	}

	/**
	 * Creates a panel that allows for the static placement of text in a frame
	 * 
	 * @author Daniel Kapit
	 * 
	 */
	class Panel extends JPanel {
		@Override
		protected void paintComponent(Graphics gra) {
			Graphics2D g2 = (Graphics2D) gra;
			g2.setFont(new Font("Arial", Font.BOLD, 24));
			int spacing = ((800 - shuffled.size()) / (shuffled.size()) / 2);
			for (int i = 1; i <= shuffled.size(); i++) {
				g2.drawString(shuffled.get(i - 1) + "", spacing * i, 100);
			}
		}
	}

	/**
	 * Creates an action listener that starts the game
	 * 
	 * @author Daniel Kapit
	 * 
	 */
	class Start implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			g.top = g.chooseWord(Integer.parseInt(word_length.getText()));
			word = g.top;
			ArrayList<String> perm = g.genPermutations(word);
			shuffled = Game.shuffle(word);
			for (String s : perm) {
				if (g.words.contains(s))
					targets.add(s);
			}
			System.out.print(targets);
			// obtains a list with the shuffled letters
			goal = targets.size();
			found = new ArrayList<String>();
			score.setText("0/" + targets.size());
			repaint();
		}

	}

	/**
	 * Creates an action listener that sends a string to be checked for validity
	 * 
	 * @author Daniel Kapit
	 * 
	 */
	class Submit implements KeyListener {

		public void actionPerformed(ActionEvent arg0) {
		}

		@Override
		public void keyPressed(KeyEvent arg0) {
			if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
				String attempt = entry.getText();
				if (targets.contains(attempt)) {
					foo.append(attempt + ", \n");
					found.add(attempt);
					targets.remove(attempt);
					entry.setText("");
					score.setText(found.size() + "/"
							+ (targets.size() + found.size()));
				}
				if (targets.isEmpty()) {
					JOptionPane.showMessageDialog(null, "You win!");
					System.exit(0);
				}
			}

			if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
				JOptionPane.showMessageDialog(null,
						"You found " + found.size() + " words: " + found
								+ "\nYou did not find " + targets.size()
								+ " words: " + targets);
				System.exit(0);
			}

		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

	}

}
