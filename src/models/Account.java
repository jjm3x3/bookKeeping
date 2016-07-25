package models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Account {
	private final SimpleStringProperty _name;
	private final SimpleDoubleProperty _initAmount;
		
	public Account(String name, double initAmount){
		_name = new SimpleStringProperty(name);
		_initAmount = new SimpleDoubleProperty(initAmount);
	}
		
	public String getName(){
		return _name.get();
	}
	
	public double getInitAmount(){
		return _initAmount.get();
	}
}
