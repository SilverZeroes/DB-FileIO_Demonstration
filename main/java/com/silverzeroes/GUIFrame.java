package com.silverzeroes;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSetMetaData;

public class GUIFrame {
	
	private static JComboBox<Object> tableSelector;
	private static DefaultTableModel tableModel;
	private static JTable table;
	private static JFrame frame;
	
	public static void start() {
		// Connect to database.
		
		JPasswordField passwordField = new JPasswordField();
		int option = JOptionPane.showConfirmDialog(null, passwordField,
				"Enter the database's password:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		if (option == JOptionPane.CANCEL_OPTION) {
			System.exit(0);
		}
		String password = new String(passwordField.getPassword());
		try {
			DatabaseHandler.connect(password);
			
			// Initialize the frame;
			frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setTitle(Config.getSQLConnection() + "@" + Config.getSQLPort() + "/" + Config.getDatabaseName());
			frame.setSize(600, 400);
			frame.setResizable(false);
			
			frame.setLayout(new BorderLayout());
			
			ResultSet resultTables = DatabaseHandler.displayAllTables();
			ArrayList<String> tables = new ArrayList<>();
			while (resultTables.next()) {
				String tableName = resultTables.getString(1);
				tables.add(tableName);
			}
			
			tableSelector = new JComboBox<>(tables.toArray());
			tableSelector.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					loadTableData();
				}
			});
			
			// Table selection drop-down menu
			JPanel topPanel = new JPanel();
			topPanel.add(new JLabel("Table:"));
			topPanel.add(tableSelector);
			frame.add(topPanel, BorderLayout.NORTH);
			
			// Displaying the data in a tabular form.
			tableModel = new DefaultTableModel();
			table = new JTable(tableModel);
			loadTableData();
			JScrollPane scrollPane = new JScrollPane(table);
			frame.add(scrollPane, BorderLayout.CENTER);
			
			// Buttons
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			
			JButton insertButton = new JButton("Add");
			JButton deleteButton = new JButton("Remove");
			JButton closeButton = new JButton("Exit");
			
			buttonPanel.add(insertButton);
			buttonPanel.add(deleteButton);
			buttonPanel.add(closeButton);
			
			frame.add(buttonPanel, BorderLayout.SOUTH);
			
			insertButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showInsertDialog();
					loadTableData();
				}
			});
			
			deleteButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					deleteSelectedRow();
					loadTableData();
				}
			});
			
			closeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			
			
			
			frame.setVisible(true);
			
		} catch (SQLException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, e.getMessage() + "\nError Code: " + e.getErrorCode(),
						"Error",  JOptionPane.ERROR_MESSAGE);
				System.exit(1);
		}
	}
	
	private static void loadTableData() {
		String selectedTable = (String) tableSelector.getSelectedItem();
		tableModel.setRowCount(0);
		tableModel.setColumnCount(0);
		try {
			
			ResultSet rs = DatabaseHandler.executeSelectQuery("SELECT * FROM " + selectedTable);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			// Display Columns:
			for (int i = 1; i <= columnCount; i++) {
				tableModel.addColumn(metaData.getColumnName(i));
			}
			
			// Add Row Entries:
			while (rs.next()) {
				Vector<Object> row = new Vector<>();
				for (int i = 1; i <= columnCount; i++) {
					row.add(rs.getObject(i));
				}
				tableModel.addRow(row);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void showInsertDialog() {
		String selectedTable = (String) tableSelector.getSelectedItem();
		ArrayList<String> columnList = new ArrayList<>();
		
		try {
			ResultSet columnsRS = DatabaseHandler.getConnection().getMetaData().getColumns(null, null, selectedTable, null);
			while (columnsRS.next()) {
				String columnName = columnsRS.getString("COLUMN_NAME");
				if (columnsRS.getString("IS_AUTOINCREMENT").equals("YES")) continue;
				columnList.add(columnName);
				
				
			}
			
			String[] columns = columnList.toArray(new String[0]);
			JPanel panel = new JPanel();
			JTextField[] fields = new JTextField[columns.length];
			panel.setLayout(new FlowLayout(FlowLayout.LEFT));
			for (int i = 0; i < columns.length; i++) {
				fields[i] = new JTextField(20);
				panel.add(new JLabel(columns[i] + ":"));
				panel.add(fields[i]);
			}
				
			int result = JOptionPane.showConfirmDialog(null, panel, "Done", JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				String query = "INSERT INTO " + selectedTable + " (";

				for (int i = 0; i < columns.length; i++) {
					query += columns[i];
					if ((i + 1) != columns.length) query += ", "; 
				}
				query += ") VALUES (\"";
				for (int i = 0; i < fields.length; i++) {
					query += fields[i].getText().trim();
					if ((i + 1) != fields.length) query += "\", \""; 
				}
				query += "\");";
				System.out.println(query);
				DatabaseHandler.executeUpdateQuery(query);
				loadTableData();
				}
			} catch (SQLException e) {
		e.printStackTrace();
		}
	}
	
	private static void deleteSelectedRow() {
		String selectedTable = (String) tableSelector.getSelectedItem();
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(frame, "Please select an item to remove");
			return;
		}
		//String tableName = selectedTable;
		Object idRow = table.getValueAt(selectedRow, 0);
		
		String query = "DELETE FROM " + selectedTable + " WHERE ID=" + "'" + idRow + "'";
		try {
			DatabaseHandler.executeUpdateQuery(query);			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
	}
		
}
