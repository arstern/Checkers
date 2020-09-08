import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.*;

public class Game implements Runnable {

	//Helper to write winner of game to file
	private void recordWinner(String winnerName, int score){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("files/HighScores.txt", true));
			writer.newLine();
			writer.write(winnerName);
			writer.newLine();
			writer.write(Integer.toString(score));
			writer.close(); 
		} catch (IOException e) {
		}
	}

	public void run() {

		final JFrame frame = new JFrame("CHECKERS");
		frame.setLocation(300, 300);

		//Status 
		final JPanel status_panel = new JPanel();
		frame.add(status_panel, BorderLayout.SOUTH);
		final JLabel status = new JLabel("Running...");
		status_panel.add(status);

		final GameCourt court = new GameCourt(status);
		frame.add(court, BorderLayout.CENTER);

		//Current scores and number of undos
		final JPanel current_score_panel = new JPanel();
		frame.add(current_score_panel, BorderLayout.WEST);
		current_score_panel.setLayout(new GridLayout(4,1));
		final JLabel currentRedScore = new JLabel("Red Score: " + court.getScore(Color.RED));
		final JLabel currentBlueScore = new JLabel("Blue Score: " + court.getScore(Color.BLUE));
		current_score_panel.add(currentRedScore);
		current_score_panel.add(currentBlueScore);
		final JLabel currentRedUndos = new JLabel("Red Undos Remaining: " + court.getUndos(Color.RED));
		final JLabel currentBlueUndos = new JLabel("Blue Undos Remaining: " + court.getUndos(Color.BLUE));
		current_score_panel.add(currentRedUndos);
		current_score_panel.add(currentBlueUndos);

		//Instructions and username input
		final String INSTRUCTIONS = (
				"Checkers: Select on the piece you want to move by clicking on it once. \n"
						+ " Then select the square you would like to move it to by clicking on it. \n" 
						+ " Each player is allowed to undo 3 of his/her moves. Undoing will cause \n" 
                        + " both players' most recent moves to be undone. A player can only undo after \n"
                        + " their opponent's  move and before their own next selection. Double jumping \n"
                        + " is not permitted. If a player cannot move, he/she must skip a turn. The \n" 
                        + " winner is determined when one player no longer has any pieces on the board. \n"
                        + " Scoring is as follows: 1 point for a regular piece and 3 for a king. High \n" 
                        + " scores are based on winning players with the most points at the end of the \n"
                        + "game. Red Player goes first. ");
		JOptionPane.showMessageDialog(frame, INSTRUCTIONS, "INSTRUCTIONS:", JOptionPane.PLAIN_MESSAGE);
		String redName = JOptionPane.showInputDialog(frame, "Red Player enter a username.", "USER INPUT", 
				JOptionPane.PLAIN_MESSAGE); 

		String blueName = JOptionPane.showInputDialog(frame, "Blue Player enter a username.", "USER INPUT", 
				JOptionPane.PLAIN_MESSAGE); 
		
		
		//Track scores look for winner 
		court.addMouseListener(new MouseAdapter() {
			boolean winnerAlreadyRecorded = false;

			public void mousePressed(MouseEvent e)  {

				GameCourt.Mode mode = court.getCurrentMode();
				int xClick = Math.floorDiv(e.getX(), 50);
				int yClick = Math.floorDiv(e.getY(), 50);
				mode.action(xClick, yClick);
				int redScore = court.getScore(Color.RED); 
				int blueScore = court.getScore(Color.BLUE);
				currentRedScore.setText("Red Score: " + redScore);
				currentBlueScore.setText("Blue Score: " + blueScore);
				if (redScore == 0) {
					String blueNameToUse;
					if (blueName.equals("")) {
						blueNameToUse = "Guest";
					} else {
						blueNameToUse = blueName;
					}
					JOptionPane.showMessageDialog(frame, blueNameToUse + " won!", "WINNER", JOptionPane.PLAIN_MESSAGE);
					if (!winnerAlreadyRecorded) {
						recordWinner(blueNameToUse, blueScore);
						winnerAlreadyRecorded = true;
					}
				} else if (blueScore == 0) {
					String redNameToUse;
					if (blueName.equals("")) {
						redNameToUse = "Guest";
					} else {
						redNameToUse = redName;
					}
					JOptionPane.showMessageDialog(frame, redNameToUse + " won!", "WINNER", JOptionPane.PLAIN_MESSAGE);
					if (!winnerAlreadyRecorded) {
						recordWinner(redNameToUse, redScore);
						winnerAlreadyRecorded = true;
					}
				}
			}
		});

		//Undo and skip
		final JPanel control_panel = new JPanel();
		frame.add(control_panel, BorderLayout.NORTH);
		final JButton undo = new JButton("Undo");
		undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				court.undo();
				currentRedScore.setText("Red Score: " + court.getScore(Color.RED));
				currentBlueScore.setText("Blue Score: " + court.getScore(Color.BLUE));
				currentRedUndos.setText("Red Undos Remaining: " + court.getUndos(Color.RED));
				currentBlueUndos.setText("Blue Undos Remaining: " + court.getUndos(Color.BLUE));
			}
		});
		control_panel.add(undo);
		final JButton skipTurn = new JButton("Skip Turn");
		skipTurn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				court.skipTurn();
			}
		});
		control_panel.add(skipTurn);

		// High score display
		final JPanel high_score_panel = new JPanel();
		high_score_panel.setLayout(new GridLayout(6,1));
		frame.add(high_score_panel, BorderLayout.EAST);
		final JLabel highScores = new JLabel(" High Scores ");
		high_score_panel.add(highScores);
		try {
			BufferedReader br = new BufferedReader(new FileReader("files/HighScores.txt"));
			Map<String, Integer> namesToScores= new TreeMap<String, Integer>();
			while (br.ready()) {

				String line = br.readLine();
				if (!line.isEmpty()) {
					String name = line;
					int score = Integer.parseInt(br.readLine());
					if (namesToScores.containsKey(name)) {
						if (score > namesToScores.get(name)) {
							namesToScores.put(name, score);
						}
					} else {
						namesToScores.put(name, score);
					}
				} 
			}


			//Sort scores descending order in map
			Comparator<String> valueComparator = 
					new Comparator<String>() {
				public int compare(String k1, String k2) {
					int compare = 
							namesToScores.get(k2).compareTo(namesToScores.get(k1));
					if (compare == 0) 
						return 1;
					else 
						return compare;
				}
			};
			Map<String, Integer> sortedByValues = 
					new TreeMap<String, Integer>(valueComparator);
			sortedByValues.putAll(namesToScores);

			//Show top 5 scores
			int limit = 0;
			for (Map.Entry<String,Integer> i : sortedByValues.entrySet()) {
				if (limit < 5) {
					String s = "";
					String key = i.getKey();
					String  value = Integer.toString(i.getValue());
					s = s + " " + key + ": " + value ;
					final JLabel scoresReadIn = new JLabel(s);
					high_score_panel.add(scoresReadIn);
					limit++; 
				}
			}
			br.close();
		} catch (FileNotFoundException e1) {
		} catch (IOException e1) {
		} 

		// Put the frame on the screen
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}

}