package application;

//rasha mansour-1210773
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class InternetRecords extends Application {

	private Country[] countryArray = new Country[16];
	private int Count = 0;
	private TableView<Country> tableView = new TableView<>();
	private TextField countryNameField;
	private TextField percentageField;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Country List");

		BorderPane borderPane = new BorderPane();
		HBox hbox = new HBox();
		countryNameField = new TextField();
		percentageField = new TextField();
		Button insertButton = new Button("Insert");
		Button deleteButton = new Button("Delete");
		TextField searchField = new TextField();
		Button searchButton = new Button("Search");
		TextField filterField = new TextField();
		Button filterButton = new Button("Filter");
		Button loadButton = new Button("Load Data");

		hbox.getChildren().addAll(countryNameField, percentageField, insertButton, deleteButton, searchField,
				searchButton, filterField, filterButton);
		borderPane.setBottom(hbox);
		borderPane.setCenter(tableView);

		TableColumn<Country, String> nameColumn = new TableColumn<>("Country");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<Country, Double> percentageColumn = new TableColumn<>("Percentage");
		percentageColumn.setCellValueFactory(new PropertyValueFactory<>("percentage"));

		tableView.getColumns().addAll(nameColumn, percentageColumn);

		// handel events to the buttons

		insertButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String name = countryNameField.getText();
				String percentageText = percentageField.getText();

				if (name.isEmpty() || percentageText.isEmpty()) {
					showAlert("Error :(", "Please fill the both fields.");
					return;
				}

				try {
					double percentage = Double.parseDouble(percentageText);

					// add to the array
					if (Count == countryArray.length) {
						countryArray = resize(countryArray, Count * 2);
					}
					countryArray[Count++] = new Country(name, percentage);

					// clear text fields
					countryNameField.clear();
					percentageField.clear();

					// clear table view
					tableView.getItems().clear();
					for (int i = 0; i < Count; i++) {
						tableView.getItems().add(countryArray[i]);
					}

					showAlert("Success :)", "Inserted: " + name + " (" + percentage + "%)");
				} catch (NumberFormatException e) {
					showAlert("Error :(", "Invalid percentage.");
				}
			}
		});

		deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String name = countryNameField.getText();

				if (name.isEmpty()) {
					showAlert("Error :(", "Please enter a country name.");
					return;
				}

				int indexToDelete = -1;
				for (int i = 0; i < Count; i++) {
					if (countryArray[i].getName().equals(name)) {
						indexToDelete = i;
						break;
					}
				}

				if (indexToDelete != -1) {
					// Shift
					for (int i = indexToDelete; i < Count - 1; i++) {
						countryArray[i] = countryArray[i + 1];
					}
					countryArray[Count - 1] = null;
					Count--;

					countryNameField.clear();

					tableView.getItems().clear();
					for (int i = 0; i < Count; i++) {
						tableView.getItems().add(countryArray[i]);
					}

					showAlert("Success :)", "Deleted: " + name);
				} else {
					showAlert("Error :(", "Country not found.");
				}
			}
		});

		searchButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String name = searchField.getText();

				if (name.isEmpty()) {
					showAlert("Error :(", "Please enter a country name.");
					return;
				}

				Double percentage = null;
				for (int i = 0; i < Count; i++) {
					if (countryArray[i].getName().equals(name)) {
						percentage = countryArray[i].getPercentage();
						break;
					}
				}

				searchField.clear();

				if (percentage != null) {
					showAlert("Search Result", "Percentage of " + name + ": " + percentage + "%");
				} else {
					showAlert("Search Result", "Country not found!!.");
				}
			}
		});

		filterButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String filterText = filterField.getText();

				if (filterText.isEmpty()) {
					showAlert("Error :(", "Please enter a filter percentage.");
					return;
				}

				try {
					double filterPercentage = Double.parseDouble(filterText);

					filterField.clear();

					// StringBuilder to save the country name
					StringBuilder filteredCountries = new StringBuilder();

					for (int i = 0; i < Count; i++) {
						Country country = countryArray[i];
						if (country.getPercentage() > filterPercentage) {
							// Append the country name to the StringBuilder
							filteredCountries.append(country.getName()).append("\n");
						}
					}

					// print countries name that percentage is more than the entered one
					if (filteredCountries.length() > 0) {
						showAlert("Filter Result", "Countries with > " + filterPercentage + "% internet usage:\n"
								+ filteredCountries.toString());
					} else {
						showAlert("Filter Result",
								"No countries found with > " + filterPercentage + "% internet usage.");
					}
				} catch (NumberFormatException e) {
					showAlert("Error :(", "Invalid filter percentage!");
				}
			}
		});

		loadButton.setOnAction(event -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
			File file = fileChooser.showOpenDialog(primaryStage);
			if (file != null) {
				printFile(file);
				// Read data from the file
				readFile(file);
			}
		});

		hbox.getChildren().add(loadButton);

		Scene scene = new Scene(borderPane, 1000, 700);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	// methods

	// print alerts
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	// method print the file data
	private void printFile(File file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			System.out.println("File Content:");
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// method to read file data
	private void readFile(File file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			System.out.println("File content:");
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				String[] parts = line.split(",");
				if (parts.length == 2) {
					String name = parts[0].trim();
					double percentage = Double.parseDouble(parts[1].trim());
					System.out.println("Name: " + name + ", Percentage: " + percentage);
					Country country = new Country(name, percentage);
					// Check if the array is full and resize if count = the array length
					if (Count == countryArray.length) {
						countryArray = resize(countryArray, Count * 2);
					}
					countryArray[Count++] = country;
					tableView.getItems().add(country);

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// method to resize the array
	private Country[] resize(Country[] oldArray, int newSize) {
		Country[] newArray = new Country[newSize];
		System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
		return newArray;
	}

}
