package pl.lmb.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.lmb.model.Player;

public class GameController implements Initializable
{

	@FXML
	private BorderPane gamePane;

	@FXML
	private GridPane grid;

	@FXML
	private AnchorPane topPane;

	@FXML
	private Button restartButton;

	@FXML
	private Label ScoreLabel;
	
    @FXML
    private MenuItem startMenuItem;

    @FXML
    private MenuItem exitMenuItem;

    @FXML
    private MenuItem scoreMenuItem;
    
	private final static Map<Integer, Image> tiles;
	private boolean isStarted;
	private int score;
	private Label endStatementLabel;
	private VBox endStmtLayout;
	
	private Player[] ranking = new Player[5];
	
	private int values[][];
	
	static
	{	
		tiles = new HashMap<>();
		File file = new File("res/0.jpg");
        Image image = new Image(file.toURI().toString());
        tiles.put(0, image);
        
		for (int i = 2; i <= 2048; i *= 2)
		{
			file = new File("res/" + i + ".jpg");
	        image = new Image(file.toURI().toString());
	        tiles.put(i, image);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		values = new int[4][4];
		initilizeRanking();
		
//		System.out.println("bla ->" + ScoreLabel.getText());
		
//		startButton.addEventFilter(ActionEvent.ACTION, e -> 
//			{
//				initializeGrid();
//				isStarted = true;
//				startButton.setDisable(true);
//			});
		restartButton.addEventFilter(ActionEvent.ACTION, e -> 
			{
				restart();
				isStarted = true;
			});
		
		
		
		gamePane.addEventFilter(KeyEvent.KEY_PRESSED, e ->
		{
			if (e.getCode() == KeyCode.LEFT)
			{
				System.out.println("left");
				if (isStarted)
					stepLeft();
			}
			else if (e.getCode() == KeyCode.RIGHT)
			{
				System.out.println("right");
				if (isStarted)
					stepRight();
			}
			else if (e.getCode() == KeyCode.UP)
			{
				System.out.println("up");
				if (isStarted)
					stepUp();
			}
			else if (e.getCode() == KeyCode.DOWN)
			{
				System.out.println("down");
				if (isStarted)
					stepDown();
			}
		});
		
		startMenuItem.setOnAction(e -> 
		{
			initializeGrid();
			isStarted = true;
			startMenuItem.setDisable(true);
		});
		exitMenuItem.setOnAction(e -> Platform.exit());
		
		scoreMenuItem.setOnAction(e ->
			{
//			       Parent root;
			        try 
			        {
			        	FXMLLoader loader = new FXMLLoader(
			        		    getClass().getResource(
			        		      "/pl/lmb/view/RankingPane.fxml"
			        		    )
			        		  );

			        		  Stage stage = new Stage();
			        		  stage.setScene(
			        		    new Scene(
			        		      (Pane) loader.load()
			        		    )
			        		  );
			        	
			            //root = FXMLLoader.load(getClass().getClassLoader().getResource("/pl/lmb/view/RankingPane.fxml"), resources);
			            //Stage stage = new Stage();
			            stage.setTitle("Ranking");
			            
			            RankingController controller = loader.<RankingController>getController();
			            controller.initData(ranking);
			            
			           // stage.setScene(new Scene(root));
			            stage.show();
			        }
			        catch (IOException exc) 
			        {
			            exc.printStackTrace();
			        }
			});
		
	}

	private void stepDown()
	{
		ObservableList<Node> nodes = grid.getChildren();
		

		for (int i = 0; i < 4; i++)
		{
				for (int j = 3; j > 0; j--) //tab[j][i]
				{
					if (values[j][i] == values[j - 1][i] && values[j][i] != 0) //dolna == dolna - 1
					{
						values[j][i] += values[j - 1][i];
						values[j - 1][i] = 0;
						
						updateScore(values[j][i]);
					}
					
					if (values[j - 1][i] != 0 && values[j][i] == 0)
					{
						for (int k = j; k < 4; k++)
						{
							if (values[k][i] == 0 && (k == 3 || values[k + 1][i] != 0))
							{
								values[k][i] += values[j - 1][i];
								values[j - 1][i] = 0;
								
								updateScore(values[k][i]);
							}
						}
					}
					
				}
				
		}
		
		updateValues(nodes, values);
		fillRandomFreeCell();
	}

	private void stepUp()
	{
		ObservableList<Node> nodes = grid.getChildren();

		for (int i = 0; i < 4; i++)
		{
				for (int j = 0; j < 4; j++) //tab[j][i]
				{
					if (j < 3 && values[j][i] == values[j + 1][i] && values[j][i] != 0) //górna == górna + 1
					{
						values[j][i] += values[j + 1][i];
						values[j + 1][i] = 0;
						
						updateScore(values[j][i]);
					}
					
					if (j > 0 && values[j - 1][i] == 0 && values[j][i] != 0)
					{
						for (int k = j; k >= 0; k--)
						{
							if (values[k][i] == 0 && (k == 0 || values[k - 1][i] != 0))
							{
								values[k][i] += values[j][i];
								values[j][i] = 0;
								
								updateScore(values[k][i]);
							}
						}
					}
				}
		}
		
		updateValues(nodes, values);
		fillRandomFreeCell();
	}

	private void stepRight()
	{
		ObservableList<Node> nodes = grid.getChildren();

		for (int i = 0; i < 4; i++)
		{
				for (int j = 3; j > 0; j--) //tab[j][i]
				{
					if (values[i][j] == values[i][j - 1] && values[i][j] != 0) //dolna == dolna - 1
					{
						values[i][j] += values[i][j - 1];
						values[i][j - 1] = 0;
						
						updateScore(values[i][j]);
					}
					
					if (values[i][j - 1] != 0 && values[i][j] == 0)
					{
						for (int k = j; k < 4; k++)
						{
							if (values[i][k] == 0 && (k == 3 || values[i][k + 1] != 0))
							{
								values[i][k] += values[i][j - 1];
								values[i][j - 1] = 0;
								
								updateScore(values[i][k]);
							}
						}
					}
					
				}
		}
		
		updateValues(nodes, values);
		fillRandomFreeCell();
	}

	private void stepLeft()
	{
		ObservableList<Node> nodes = grid.getChildren();

		for (int i = 0; i < 4; i++)
		{
				for (int j = 0; j < 4; j++) //tab[j][i]
				{
					if (j < 3 && values[i][j] == values[i][j + 1] && values[i][j] != 0) //górna == górna + 1
					{
						values[i][j] += values[i][j + 1];
						values[i][j + 1] = 0;
						
						updateScore(values[i][j]);
					}
					
					if (j > 0 && values[i][j - 1] == 0 && values[i][j] != 0)
					{
						for (int k = j; k >= 0; k--)
						{
							if (values[i][k] == 0 && (k == 0 || values[i][k - 1] != 0))
							{
								values[i][k] += values[i][j];
								values[i][j] = 0;
								
								updateScore(values[i][k]);
							}
						}
					}
				}
		}
		
		updateValues(nodes, values);
		fillRandomFreeCell();
	}
	
	private void updateScore(int points)
	{
		score += points;
		ScoreLabel.setText("Wynik: " + score);
	}
	
	private void fillRandomFreeCell()
	{
		ObservableList<Node> nodes = grid.getChildren();
		List<Integer> freeCells = new ArrayList<>();
		
		int value;
		String text; //end label
		for (int i = 0; i < nodes.size(); i++)
		{
			value = getValue(i);
			
			if (value == 0)
				freeCells.add(i);
		}
		
		if (freeCells.size() > 0)
		{
			Collections.shuffle(freeCells);
				//get first value from shuffled pool
			int drawn = freeCells.get(0);
			fillCell(drawn, 2);
		}
		else if (isLoser()) //TODO
		{
			text = "Przegrałeś! Chcesz Spróbować ponownie?";
			endStmtLayout = new VBox();
			endStatementLabel = new Label(text);
			endStatementLabel.setStyle("-fx-text-fill: #FF0000; -fx-font-size: 32;");
			gamePane.getChildren().remove(grid);
			endStmtLayout.getChildren().add(endStatementLabel);
			
				if (scoreInTop())
				{
					addBestScoreForm(endStmtLayout);
				}
				gamePane.setCenter(endStmtLayout);
				
				isStarted = false;
		}
		
		if (isWinner())
		{
			text = "Wygrałeś!!!";
			endStmtLayout = new VBox();
			endStatementLabel = new Label(text);
			endStatementLabel.setStyle("-fx-text-fill: #09B079; -fx-font-size: 32;");
			endStmtLayout.getChildren().add(endStatementLabel);
			
			if (scoreInTop())
			{
				addBestScoreForm(endStmtLayout); //layout
			}
			
			gamePane.setCenter(endStmtLayout);
			
			isStarted = false;
		}
	}
	
	private boolean isLoser()
	{
		boolean lost = true;
		ObservableList<Node> nodes = grid.getChildren();
		
		for (int i = 0; i < nodes.size(); i++)
		{
			int first = getValue(i); //values[i / 4][i % 4]
			if ((i + 1) % 4 != 0)
			{
				int rightNeighbour = getValue(i+1);
				
				if (first == rightNeighbour)
				{
					lost = false;
					break;
				}
			}
			
			if (i < 12)
			{
				int bottomNeighbour = getValue(i + 4);
				if (first == bottomNeighbour)
				{
					lost = false;
					break;
				}
			}
		}
		
		return lost;
	}

	private boolean isWinner()
	{
		boolean won = false;
		for (int i = 0; i < values.length; i++)
		{
			for (int j = 0; j < values.length; j++)
			{
				if (values[i][j] == 2048)
				{
					won = true;
					break;
				}
			}
		}
		
		return won;
	}
	
	/*
	 * 
	 * Updating values
	 */
	private void updateValues(ObservableList<Node> nodes, int[][] val)
	{
		
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				setCell(nodes.get(i * 4 + j), values[i][j]);
			}
		}
		
	}
	
	private void setCell(Node node, int value)
	{
		ImageView imageView = (ImageView) node;
        imageView.setImage(tiles.get(value));
	}
	
	private void resetGrid()
	{
		int i = 0;
		values = new int[4][4];
		
		for (Node node : grid.getChildren())
		{
			setCell(node, 0);
			fillCell(i++, 0);
		}
	}

	private void initializeGrid()
	{
		List<Integer> drawnList = randomize();
		resetGrid();
		
		int i = 0;
		for (Node node : grid.getChildren())
		{
			for (int value : drawnList)
			{
				if (value == i)
				{
					setCell(node, 2);
			        fillCell(i, 2);
			        
					break;
				}
			}
			i++;
		}
		
		//TODO temporary
//		values = new int[][]{
//				{2,4,8,16},
//				{16,8,4,2},
//				{256,512,1024,256},
//				{2,4,6,8}};
	}
	
	private void restart() //TODO
	{
		//if the game was ended and grid was replaced with label statement
		if (!gamePane.getChildren().contains(grid))
		{
			gamePane.getChildren().remove(endStmtLayout);
			gamePane.getChildren().add(grid);
		}
		
		initializeGrid();
		updateScore(-score);
	}

	private List<Integer> randomize()
	{
		List<Integer> range = IntStream.range(0, 16).boxed()
		        .collect(Collectors.toCollection(ArrayList::new));
		Collections.shuffle(range);
		List<Integer> drawnList = new ArrayList<>(range.subList(0, 5));

		
		return drawnList;
	}
	
	private void fillCell(int cell, int value)
	{
		values[cell / 4][cell % 4] = value;
	}
	
	//transform index from one dimentional array into two dimentional and return value from the cell
	private int getValue(int index)
	{
		return values[index / 4][index % 4];
	}
	
	private void initilizeRanking()
	{
		try (Scanner scanner = new Scanner(new File("res/ranking.txt"));)
		{
			String[] splittedLine;
			int i = 0;
			while (scanner.hasNextLine() && i < 5)
			{
				splittedLine = scanner.nextLine().split("\t");
				if (splittedLine.length == 2)
					ranking[i] = new Player(Integer.parseInt(splittedLine[0]), splittedLine[1]);
				i++;
			}
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
	}
	
	private boolean scoreInTop()
	{
		for (int i = 0; i < ranking.length; i++)
		{
			if (ranking[i] == null || score > ranking[i].getScore())
			{
				return true;
			}
		}

		return false;
	}
	
	private void addBestScoreForm(Pane layout) //Pane layout
	{
		TextField nickField = new TextField();
		Button addScoreToRankingButton = new Button("Dodaj do rankingu");
		addScoreToRankingButton.setOnAction(e -> 
		{
			updateRanking(nickField.getText());
			gamePane.getChildren().remove(layout);
			resetGrid();
			values = new int[4][4];
		});
		HBox box = new HBox();
		box.setAlignment(Pos.CENTER);
		box.getChildren().add(nickField);
		box.getChildren().add(addScoreToRankingButton);
		layout.getChildren().add(box);
//		gamePane.setBottom(nickField); //getChildren().add(nickField);
//		gamePane.setRight(addScoreToRankingButton);
	}
	
	private void updateRanking(String nickname)
	{
		if (nickname.equals("") || nickname == null)
		{
			nickname = "unknown_gamer";
		}
		
		Player tmpGamer;
		
		for (int i = 0; i < ranking.length; i++)
		{			
			if (ranking[i] == null || score > ranking[i].getScore())
			{
				tmpGamer = ranking[i];
				ranking[i] = new Player(score, nickname);
				
				if (i < ranking.length - 1)
				{
					for (int j = ranking.length - 1; j > i; j--)
					{
						ranking[j] = ranking[j - 1];
					}
					ranking[i + 1] = tmpGamer;
				}
				
				break;
			}
		}
		updateRankingFile();
	}
	
	private void swap(Player g1, Player g2)
	{
		Player tmpGamer = g1;
		g1 = g2;
		g2 = tmpGamer;
	}
	
	private void updateRankingFile()
	{
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File("res/ranking.txt"), false));)
		{
			Player gamer;
			for (int i = 0; i < ranking.length; i++)
			{
				if (ranking[i] != null)
				{
					gamer = ranking[i];
					writer.append(String.valueOf(gamer.getScore()));
					writer.append("\t");
					writer.append(gamer.getNickname());
				    if (i < 5)
				    	writer.append("\n");
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}

}
