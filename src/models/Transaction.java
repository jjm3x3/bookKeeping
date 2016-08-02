package models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Transaction {
	private final SimpleStringProperty _name;
	private final SimpleDoubleProperty _amount;

	public Transaction(String name, double amount){
		this._name = new SimpleStringProperty(name);
		this._amount = new SimpleDoubleProperty(amount);
	}
	
	public String getName() {
		return _name.get();
	}
	
	public double getAmount() {
		return _amount.get();
	}
}
