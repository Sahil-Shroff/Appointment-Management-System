package application;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Patient {
	
	private SimpleIntegerProperty id = new SimpleIntegerProperty();
	private SimpleIntegerProperty order = new SimpleIntegerProperty(0);
	private SimpleStringProperty name;
	private SimpleIntegerProperty age;
	private ObjectProperty<String> gender;
	private SimpleIntegerProperty fees = new SimpleIntegerProperty(0);
	private SimpleIntegerProperty feesPaid = new SimpleIntegerProperty(0);
	private SimpleIntegerProperty priority = new SimpleIntegerProperty(0);
	private SimpleIntegerProperty receipt = new SimpleIntegerProperty(1);
	
	public Patient() {
		name = new SimpleStringProperty("");
		age = new SimpleIntegerProperty(0);
		gender = new SimpleObjectProperty<String>();
	}
	
	public Patient(int id, int order, String name, int age, String gender, int priority) {
		this.id = new SimpleIntegerProperty(id);
		this.order = new SimpleIntegerProperty(order);
		this.name = new SimpleStringProperty(name);
		this.age = new SimpleIntegerProperty(age);
		this.gender = new SimpleObjectProperty<String>(gender);
		this.priority = new SimpleIntegerProperty(priority);
	}
	
	public Patient(int id, String name, String gender) {
		this.id = new SimpleIntegerProperty(id);
		this.name = new SimpleStringProperty(name);
		this.age = new SimpleIntegerProperty(0);
		this.gender = new SimpleObjectProperty<String>(gender);
	}

	public Patient(int id, String name, int age, String gender) {
		this.id = new SimpleIntegerProperty(id);
		this.name = new SimpleStringProperty(name);
		this.age = new SimpleIntegerProperty(age);
		this.gender = new SimpleObjectProperty<String>(gender);
	}
	
	public Patient(int id, String name, int age, String gender, int fees, int fees_paid, int receipt) {
		this.id = new SimpleIntegerProperty(id);
		this.name = new SimpleStringProperty(name);
		this.age = new SimpleIntegerProperty(age);
		this.gender = new SimpleObjectProperty<String>(gender);
		this.fees = new SimpleIntegerProperty(fees);
		this.feesPaid = new SimpleIntegerProperty(fees_paid);
		this.receipt = new SimpleIntegerProperty(receipt);
	}
	
	public Patient(int id, int order, String name, String gender, int priority) {
		this.id = new SimpleIntegerProperty(id);
		this.order = new SimpleIntegerProperty(order);
		this.name = new SimpleStringProperty(name);
		this.gender = new SimpleObjectProperty<String>(gender);
		this.priority = new SimpleIntegerProperty(priority);
	}
	
	public int getFees() { return fees.get();	}
	
	public SimpleIntegerProperty getFeesProperty() {	return fees;	}
	
	public SimpleIntegerProperty getFeesPaidProperty() {	return feesPaid;	}
	
	public void setFeesPaidProperty(boolean pay) {
		if (pay)
			feesPaid = new SimpleIntegerProperty(1);
		else
			feesPaid = new SimpleIntegerProperty(0);
	}
	
	public String getFeesPaid() {
		if (feesPaid.get() > 0)
			return "Paid";
		else
			return "Not Paid";
	}
	
	public boolean isFeesPaid() {
		if (feesPaid.get() > 0)
			return true;
		else
			return false;
	}

	public int getId() {	return id.get();	}
	
	public SimpleStringProperty nameProperty() { return name; }
	
	public String getName() { return name.get(); }

	public void setName(String name) { this.name = new SimpleStringProperty(name); }

	public int getAge() { return age.get(); }

	public void setAge(int age) { this.age = new SimpleIntegerProperty(age); }
	
	public ObjectProperty<String> genderProperty() { return gender; }

	public String getGender() {	return gender.get(); }

	public void setGender(String gender) { this.gender = new SimpleObjectProperty<String>(gender); }

	public int getOrder() {	return order.get();	}
}
