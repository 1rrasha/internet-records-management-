package application;

public class Country implements Comparable<Country> {

	// attributes
	private String name;
	private double percentage;

	// constructors

	public Country() {
		super();
	}

	public Country(String name, double percentege) {
		super();
		this.name = name;
		this.percentage = percentege;
	}

	// setters&getters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentege) {
		this.percentage = percentege;
	}

	@Override
	public String toString() {
		return "Country [name=" + name + ", percentege=" + percentage + "]";
	}

	@Override
	public int compareTo(Country o) {
		return this.name.compareTo(o.name);
	}

	@Override
	public boolean equals(Object obj) {// to check if the name is on the list to
										// add it or just add the content of it

		Country o = (Country) obj;
		return name.equals(o.name);// we can use the method compareto here
									// because it do the same
	}

}
