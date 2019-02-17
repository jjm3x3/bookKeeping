package dataStore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.h2.tools.RunScript;
import org.h2.tools.Script;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Account;
import models.Transaction;

public class SimpleDbInteraction {

	private static final String DB_URL = "jdbc:h2:~/.bookKeeping/db";
	private static final String DB_PASSWORD = "secret";
	private static final String DB_USERNAME = "accountant";


	public static void AddAccount(String accountName, double accountStartAmount) {
		try {
			Connection dbConn = DriverManager.getConnection(DB_URL,DB_USERNAME, DB_PASSWORD);
			Statement stmt = dbConn.createStatement();
			String sql = "insert into accounts(id,name,initVal) values(NULL, ?, ?)";
			PreparedStatement prep = dbConn.prepareStatement(sql);
			prep.setString(1, accountName);
			prep.setDouble(2, accountStartAmount);
			boolean entryAdded = prep.execute();
			dbConn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void addTransaction(String transactionName, double amount, int account, LocalDate transactionDate){
		try {
			Connection dbConn = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
			Statement stmt = dbConn.createStatement();
			DateTime now = new DateTime();
			System.out.println("its this time: " + now.toString().length());
			String sql = "insert into transactions(id,name,amount,account_id,transaction_date,transaction_created) " + 
					"values(NULL, ?, ?, ?, ?, ?)";
			PreparedStatement prep = dbConn.prepareStatement(sql);
			prep.setString(1, transactionName);
			prep.setDouble(2, amount);
			prep.setInt(3, account);
			prep.setString(4, transactionDate.toString());
			prep.setString(5, now.toString());
			boolean reault = prep.execute();
			dbConn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ObservableList<Account> getAccountList() throws SQLException {
		Connection dbConn = DriverManager.getConnection(DB_URL,DB_USERNAME, DB_PASSWORD);
		Statement stmt = dbConn.createStatement();
		ResultSet result = stmt.executeQuery("select * from accounts");
		ObservableList<Account> accountsList = FXCollections.observableArrayList();
//		accountList.add(new Account(""))
		double totalAssets = 0;
		while(result.next()){
			String name = result.getString("name");
			double accountStart = result.getDouble("initVal");
			Statement transactionStatement = dbConn.createStatement();
			try {
				ResultSet transactions = transactionStatement.executeQuery("select * from transactions where account_id = " + result.getInt("id"));
				while (transactions.next()){
					accountStart += transactions.getDouble("amount");
				}
			} catch (SQLException e){
				System.err.println(e);
//				e.printStackTrace();
			}
			accountsList.add(new Account(name, accountStart));
			totalAssets += accountStart;
		}
		accountsList.add(new Account("   TOTAL" , totalAssets));
		dbConn.close();
		return accountsList;
	}
		
	public static ObservableList<Transaction> getTransactionList(int accountId) throws SQLException {
		Connection dbConn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
		Statement stmt = dbConn.createStatement();
		ResultSet result = stmt.executeQuery("Select * from transactions where account_id = " + accountId);
		ObservableList<Transaction> transactionList = FXCollections.observableArrayList();
		while (result.next()){
			String name = result.getString("name");
			double amount = result.getDouble("amount");
			transactionList.add(new Transaction(name, amount));
						
		}
		dbConn.close();
		return transactionList;
	}
	
	public static void runsql(){
		try{
			Connection dbConn = DriverManager.getConnection(DB_URL,DB_USERNAME, DB_PASSWORD);
			Statement stmt = dbConn.createStatement();
			boolean tableCreated = stmt.execute("create table if not exists accounts(id identity,name VARCHAR(50),initVal double)");
			dbConn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try{
			Connection dbConn = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
			Statement stmt = dbConn.createStatement();
			boolean createTable = stmt.execute("create table if not exists " + 
					"transactions(id identity," + 
					"name VARCHAR(100), " + 
						"amount double, " +  
						"account_id int," + 
						"transaction_date date, " + 
						"transaction_created varchar(30), " + 
						"foreign key (account_id) references public.accounts(id))");
			dbConn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void takeBackup(File file) {
		Connection dbConn;
		String fileName = file.getAbsolutePath();
		System.out.println(fileName.lastIndexOf('.'));
	
		if (fileName.lastIndexOf('.') != -1){
			fileName = fileName.substring(0, fileName.lastIndexOf('.'));
		}
		
		System.out.println("writing to file: "  + fileName);
		try {
			dbConn = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
			PreparedStatement prep = dbConn.prepareStatement("SCRIPT TO '" + fileName + ".bak';");
			
//			PreparedStatement prep = dbConn.prepareStatement("BACKUP TO 'bookKeeping.bak'");
			prep.execute();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Script runScript = new Script();
		// TODO Auto-generated method stub
		
	}
	
	public static void restoreFromFile(File file){
		Connection dbConn;
		try {
			Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", "rm ~/.bookKeeping/* "});
			while (p.isAlive()) {}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			dbConn = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
			try {
				PreparedStatement prep = dbConn.prepareStatement("RUNSCRIPT FROM '" + file.getAbsolutePath() + "'; ");
				prep.execute();
			} catch (SQLException e){
				e.printStackTrace();
			} finally {
				dbConn.close();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
		
	}

	public static void importCsv(File file, int accountId) {
		String tmpFileLoc = "tmpFile.del";
		try {
			Connection dbConn = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
			addCsvHeaderIntoFile(file, tmpFileLoc); 			

//			String localTestFile = "/home/jmeixner/Downloads/Checking1.csv";
			PreparedStatement prep = dbConn.prepareStatement("SELECT * FROM CSVREAD('" + tmpFileLoc + "');");
			ResultSet csvRows = prep.executeQuery();
			while(csvRows.next()){
				Map<String,Integer> csvMap = new HashMap<String,Integer>();
				csvMap.put("date", 1);
				csvMap.put("amount", 2);
				csvMap.put("name", 5);
				LocalDate transactionDate = new LocalDate();
				String transactionName = "";
				double transactionAmount = 0;
				transactionDate = LocalDate.parse(csvRows.getString(csvMap.get("date")), DateTimeFormat.forPattern("MM/dd/yyyy"));
				transactionAmount = csvRows.getDouble(csvMap.get("amount"));
				transactionName = csvRows.getString(csvMap.get("name"));

				if (transactionAmount != 0 && !transactionName.equals("")){
					addTransaction(transactionName, transactionAmount, accountId, transactionDate);
				}
			}

			try {
				Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", "rm " + tmpFileLoc});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		
	}

	private static void addCsvHeaderIntoFile(File file, String tmpFileLoc) throws SQLException {
		Connection dbConn = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
		PreparedStatement prep = dbConn.prepareStatement("SELECT * FROM CSVREAD('" + file.getAbsolutePath() + "');");
		ResultSet csvRows = prep.executeQuery();
		String csvHeader = "";
		try {
			for (int i = 0; i < csvRows.getMetaData().getColumnCount(); ++i){
				csvHeader += "\"\"";
				if (i != csvRows.getMetaData().getColumnCount() - 1)
					csvHeader += ",";
			}
			BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
			ArrayList<String> existingCsvContents = new ArrayList();
			existingCsvContents.add(csvHeader);
			String line;
			while ((line = reader.readLine()) != null){
				existingCsvContents.add(line);
			}
			reader.close();
			

			BufferedWriter writer = new BufferedWriter(new FileWriter(tmpFileLoc));
			for (String row : existingCsvContents){
				writer.write(row + "\n");
			}
			writer.close();

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
