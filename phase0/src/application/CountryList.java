package application;

import java.util.Arrays;

public class CountryList<T extends Country> implements List<T> {

	// attributes
	private T[] data;
	private int count = 0;

	// constructor
	public CountryList(int capacity) {
		data = (T[]) new Country[capacity];
	}

	// getters
	public T[] getData() {
		return data;
	}

	public int getCount() {
		return count;
	}

	// methods

	@Override
	public void insert(T country) {
		if (count < data.length) {
			data[count] = country;
			count++;
		} else {
			System.out.println("List is full. Cannot insert more countries.");
		}
	}

	@Override
	public boolean delete(T country) {
		for (int i = 0; i < count; i++) {
			if (data[i].equals(country)) {
				// Shift elements to the left to remove the element
				System.arraycopy(data, i + 1, data, i, count - i - 1);
				data[count - 1] = null;
				count--;
				return true;
			}
		}
		return false; // Country not found in the list
	}

	@Override
	public Double search(T country) {
		for (int i = 0; i < count; i++) {
			if (data[i].equals(country)) {
				return data[i].getPercentage();
			}
		}
		return null; // Country not found in the list
	}

	@Override
	public void display() {
		System.out.println("Countries with greater %:");
		System.out.println("-----------------");
		System.out.printf("%s %n", "Country", "Percentage:%");
		System.out.println("-----------------");

		for (int i = 0; i < count; i++) {
			if (data[i].getPercentage() > 0) { // to chick if the percentage is not negative
				System.out.printf("%s %n", data[i].getName(), data[i].getPercentage());
			}
		}
	}
}
