package application;

public interface List<T> {
	void insert(T data);

	 boolean delete(T data);

	Double search(T data);

	void display();

}
