package application;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Patient {
	
	private SimpleStringProperty name;
	private SimpleIntegerProperty age;
	private ObjectProperty<String> gender;
	
	public Patient() {
		name = new SimpleStringProperty("");
		age = new SimpleIntegerProperty(0);
		gender = new SimpleObjectProperty<String>();
	}
	
	public Patient(String name, String gender) {
		this.name = new SimpleStringProperty(name);
		age = new SimpleIntegerProperty(0);
		this.gender = new SimpleObjectProperty<String>(gender);
	}
	
	public Patient(String name, int age, String gender) {
		this.name = new SimpleStringProperty(name);
		this.age = new SimpleIntegerProperty(age);
		this.gender = new SimpleObjectProperty<String>(gender);
	}

	public SimpleStringProperty nameProperty() { return name; }
	
	public String getName() { return name.get(); }

	public void setName(String name) { this.name = new SimpleStringProperty(name); }

	public int getAge() { return age.get(); }

	public void setAge(int age) { this.age = new SimpleIntegerProperty(age); }
	
	public ObjectProperty<String> genderProperty() { return gender; }

	public String getGender() {	return gender.get(); }

	public void setGender(String gender) { this.gender = new SimpleObjectProperty<String>(gender); }
	
}
