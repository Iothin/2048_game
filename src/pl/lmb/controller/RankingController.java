package pl.lmb.controller;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pl.lmb.model.Player;

public class RankingController implements Initializable
{

	@FXML
	private TableView<Player> table;

	@FXML
	private TableColumn<Player, Integer> positionCol;

	@FXML
	private TableColumn<Player, String> nickCol;

	@FXML
	private TableColumn<Player, Integer> scoreCol;

	//private Player[] ranking;

	void initData(Player[] ranking)
	{
		//this.ranking = ranking;
		final ObservableList<Player> data = FXCollections.observableArrayList(Arrays.asList(ranking));
		table.setItems(data);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		
		nickCol.setCellValueFactory(new PropertyValueFactory<Player, String>("nickname"));
		scoreCol.setCellValueFactory(new PropertyValueFactory<Player, Integer>("score"));

		positionCol.setCellValueFactory(column -> new ReadOnlyObjectWrapper<Integer>(table.getItems().indexOf(column.getValue()) + 1));
	}

}