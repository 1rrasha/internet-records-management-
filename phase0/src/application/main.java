package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class main {
	public static void main(String[] args) {

		CountryList countryList = new CountryList(16);

		// reading the file
		try {
			File file = new File("C:\\Users\\user\\Downloads\\internet_2020.csv");
			Scanner scan = new Scanner(file);

			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				String[] data = line.split(",");
				if (data.length != 2) {
					System.out.println("wrong data : " + line);
					continue;
				}

				String countryName = data[0];
				try {
					double percentage = Double.parseDouble(data[1]);
					Country country = new Country(countryName, percentage);
//					countryList.insert(country);
				} catch (NumberFormatException e) {
					System.out.println("wrong percentage: " + data[1]);
				}
			}

			countryList.display();

		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
	}
}
