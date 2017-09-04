package lees_childeren_yoonsoo_cho;

import java.sql.*;
import java.util.*;

public class GUI_M {

	/** Variables needed to build connection between java and database
	 *  DB_DRIVER : driver name used for connection
	 *  DB_CONNECTION : location where database stored
	 *  DB_USER : user name who has permission for database
	 *  DB_PASSWORD : user password
	 */
	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/db_lee";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "2279";

	/** In main method, connect to database and do SQL queries depending on user inputs
	 *  Menus separated by switch-case statements, based on user choice(input)
	 *  If case ended, return to main menu until user exit the menu
	 * @throws SQLException main method throw and catch SQLException to alert user that something goes wrong in SQL
	 */
	public static void main(String[] argv) throws SQLException {
		/** Define variables may be needed for SQL Query in advance
		 *  con : for building Database connection
		 *  pstm, st : for executing query and updates, inserts, and deletes
		 *  sql : for storing queries temporarily before execution
		 *  re : for pointing the result set of queries (which used for printing out the result set)
		 */
		Connection con = null;
		PreparedStatement pstm = null;
		Statement st = null;
		String sql = null;
		ResultSet rs = null;
		
		// open scanner for storing user inputs
		Scanner scan = new Scanner(System.in);

		// stop : variable for stopping the while loop, only become false when user typed "EXIT" code = 0
		boolean stop = true;

		// call method getDBConnection to open connection between database, and catch SQLException, which means you can not open connection
		try {
			con = (new GUI_M()).getDBConnection();
		} catch (SQLException ex) {
			System.out.println("Can't open connection, beacause "+ ex.getMessage());
		}

		// continue while loop until user choose "EXIT" menu
		while (stop) {
			// variables needed to separate each menus
			int mode = 0;
			int brand = 0;
			int detail = 0;
			int detail2 = 0;

			// variables needed to specify brands in SQL Queries
			int upper = 0, lower = 0;
			int upper_r = 0, lower_r = 0;
			// variables needed to specify age in SQL Queries
			int age_up = 0, age_down = 0;
			System.out
					.println("=================================================================================================");
			System.out.println("Please Choice Mode you want>>");
			System.out
					.println(" Employee Management : 1\n Brand Management    : 2\n Member Management   : 3\n EXIT          : 0");
			System.out
					.println("=================================================================================================\n");
			// store user input for choosing which menu to enter next
			mode = scan.nextInt();

			// separate cases depending on user input "mode"
			switch (mode) {
			case 1: // Employee management
				System.out.println("\t=====>>>"
						+ "Current Mode: Employee Management" + "<<<=====\t");
				System.out
						.println("\tWorkplace movement \t : 1\n \tSalary Update \t\t : 2\n \tNew Employee \t\t : 3");
				// store user input for choosing which menu to enter next
				detail = scan.nextInt();
				// separate sub menus depending on user input "detail"
				switch (detail) {
				case 1: // work place movement in employee
					// variables to store user input and later used in SQL Queries
					// employee : the person whose workplace will be changed
					// new_workplace : new modified workplace
					int employee = 0,
					new_workplace = 0;

					// store user inputs in variables employee, new_workplace
					System.out.print("-> Who do you want to move? : ");
					employee = scan.nextInt();

					System.out.print("-> to WHERE? : ");
					new_workplace = scan.nextInt();

					// store Update query and ? will be implemented later
					sql = "Update DBCOURSE_Employee set Workplace = ?"
							+ " where ID = ?";
					// If there are something wrong in executing queries, catch Exception and handle it
					try {
						// create prepared statement for executing queries and send sql as parameter
						pstm = con.prepareStatement(sql);
						// put new_workplace in the place of first ? in sql queries
						pstm.setInt(1, new_workplace);
						// put new_workplace in the place of second ? in sql queries
						pstm.setInt(2, employee);
						// execute Update queries, which update table DBCOURSE_Employee by setting Workplace to user input new_workplace where ID is the same with user input employee
						pstm.executeUpdate();

						System.out.println("\t\t********!!SUCCESS!!********");

						// create statement for executing another queries
						st = con.createStatement();
						// store select queries and concatenate to employee
						sql = "select * from DBCOURSE_EMPLOYEE where ID = ";
						sql = sql + employee;
						// execute Select queries and store result set in variable rs, which select all attributes from DBCOURSE_EMPLOYEE where ID is the same with user input employee
						rs = st.executeQuery(sql);

						System.out.println("ID\t" + "NAME\t\t" + "WORKPLACE");
						// print out Result table 'till the end of line
						while (rs.next()) {
							System.out.println(rs.getInt("ID") + "\t"
									+ rs.getString("NAME") + "\t"
									+ rs.getInt("WORKPLACE"));
						}
						System.out.println();
					} catch (SQLException e) {
						// Suppose All Exception occurs because user input was wrong
						System.out
								.println(" Wrong employee OR Wrong workplace INPUT : RETYPE YOUR ANSWER. ");
					}
					break;
				case 2: // salary update in employee
					// extra variables for executing various queries
					PreparedStatement pstm2 = null;
					String sql2 = null;

					int constraints = 0;
					double lb = 0;
					double Pure_Cost = 0;
					double r1 = 0,
					r2 = 0,
					r3 = 0,
					r4 = 0;

					con.setAutoCommit(false); // turn off auto commit : transaction start

					System.out.print("\t-> Workplace : "); // // user input : want to see Restaurant(workplace)
					constraints = scan.nextInt(); // insert user input into constraints

					System.out.print("\t-> Lower Bound: "); // user input : want to set up under bound of salary raise condition
					lb = scan.nextDouble(); // insert user input into lb
					try {

						sql = "select Pure_Cost from DBCOURSE_Restaurant where ID = ?";// sql query : receive the ID(WorkPlace) and print out Restaurant's Pure_Cost which have that ID
						pstm = con.prepareStatement(sql); // sql1 is connected to pstm
						pstm.setInt(1, constraints); // ? of pstm is filled with WorkPalce
						rs = pstm.executeQuery(); // then pstm is executed and result is inserted into re

						try {
							while (rs.next()) {
								Pure_Cost = rs.getDouble("Pure_Cost"); // insert value of rs into Pure_Cost
							}
						} catch (SQLException e) {
							System.out.println("Wrong Query!! : Check again");
						} // Pure_Cost

						if (Pure_Cost >= lb) { // if Pure_Cost is bigger than lower bound (or same)
							//then user can raise the salary of related employees of restaurant(WorkPlace)
							System.out
									.print("\t   -> Raise salary rate of Cook : ");
							r1 = scan.nextDouble();
							System.out
									.print("\t   -> Raise salary rate of Server : ");
							r2 = scan.nextDouble();
							System.out
									.print("\t   -> Raise salary rate of Manager : ");
							r3 = scan.nextDouble();
							System.out
									.print("\t   -> Raise salary rate of Casher : ");
							r4 = scan.nextDouble();

							pstm2 = con
									.prepareStatement("update DBCOURSE_Employee "
											+ "set salary = "
											+ "case role "
											+ "when 'Cook' then salary*? "
											+ "when 'Server' then salary*? "
											+ "when 'Manager' then salary*? "
											+ "else salary*? "
											+ "end "
											+ "where WorkPlace = ?");
							pstm2.setDouble(1, r1);
							pstm2.setDouble(2, r2);
							pstm2.setDouble(3, r3);
							pstm2.setDouble(4, r4);
							pstm2.setInt(5, constraints);
							// execute Update query, raising rate of Cook's salary, Server's salary, Manager's salary, Casher's salary where their workplace is same with user input constraints
							pstm2.executeUpdate();

							sql2 = "select DBCOURSE_Employee.ID, Role, Salary, Brand, Branch "
									+ "from DBCOURSE_Employee, DBCOURSE_Name "
									+ "where DBCOURSE_Employee.WorkPlace = DBCOURSE_Name.ID "
									+ "and DBCOURSE_Name.ID = ";
							sql2 = sql2 + constraints;
							// execute query for showing updated employee's 	information
							rs = pstm2.executeQuery(sql2);

							System.out
									.println("\n\t\t********!!SUCCESS!!********");
							System.out.println("ID\t" + "Role\t" + "Salary\t"
									+ "Brand\t\t\t" + "Branch");
							//print out result of query
							while (rs.next()) {
								System.out.println(rs.getInt(1) + "\t"
										+ rs.getString(2) + "\t" + rs.getInt(3)
										+ "\t" + rs.getString(4) + "\t\t"
										+ rs.getString(5));
							}
						}
						// when Pure_cost is smaller than lower bound, throw exception and message
						else {
							try {
								throw new Exception();
							} catch (Exception e) {
								System.out
										.println("You cannot raise the salary of this restaurant!");
							}
						}
						try {
							con.commit();
							// print if success transaction
							System.out.println("Done!\n");
						} catch (SQLException e) {
							// print if fails transaction and rollback
							System.out.println(e.getMessage());
							con.rollback();
						}
						//turn on auto commit
						con.setAutoCommit(true);

					} catch (SQLException e) {
						e.printStackTrace();
					}
					//close ResultSet, PreparedStatements
					try {
						rs.close();
						pstm.close();
						pstm2.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					break;

				case 3: // add new one into employee
					// variables for storing user inputs
					int id = 0,
					workplace = 0,
					salary = 0;
					String name = null,
					role = null;

					System.out.print("\t-> ID : ");
					id = scan.nextInt();

					System.out.print("\t-> Name : ");
					name = scan.next();

					System.out.print("\t-> Work place : ");
					workplace = scan.nextInt();

					System.out.print("\t-> Role : ");
					role = scan.next();

					System.out.print("\t-> Salary : ");
					salary = scan.nextInt();

					// INSERT query into DBCOURSE_Employee using uer inputs
					sql = "insert into DBCOURSE_Employee (ID, NAME, WORKPLACE, ROLE, SALARY) values (?, ?, ?, ?, ?)";
					// create prepared statement for INSERT query, and send parameter sql
					pstm = con.prepareStatement(sql);
					pstm.setInt(1, id);
					pstm.setString(2, name);
					pstm.setInt(3, workplace);
					pstm.setString(4, role);
					pstm.setInt(5, salary);
					// execute INSERT query, which insert new tuple(id, name, workplace, role, salary) into DBCOURSE_Employee depending on user inputs
					pstm.executeUpdate();

					try {
						// create statement for SELECT query
						st = con.createStatement();
						// select all attributes from DBCOURSE_Employee where ID is same with user input id
						sql = "select * from DBCOURSE_EMPLOYEE where ID = ";
						sql = sql + id;
						// execute SELECT query sql
						rs = st.executeQuery(sql);

						System.out.println("\n\t\t********!!SUCCESS!!********");
						System.out.println("ID\t" + "Name\t\t" + "WorkPlace");
						// print out Result table 'till the end of line
						while (rs.next()) {
							System.out.println(rs.getInt("ID") + "\t"
									+ rs.getString("NAME") + "\t\t"
									+ rs.getInt("WORKPLACE"));
						}
						System.out.println("\nDone!\n");
					}
					// Suppose All Exception occurs because user input was wrong
					catch (SQLException e) {
						System.out
								.println(" Wrong Employee INPUT : RETYPE YOUR ANSWER. ");
					}
					break;
				default:
					// when user input is not in menu
					System.out
							.println(" Wrong Menu INPUT : RETYPE YOUR ANSWER. ");
					break;
				}
				break;
			case 2: // brand management
				System.out.println("\t=====>>>"
						+ "Current Mode: Brand Management" + "<<<=====\t");
				System.out.println("\tChoose one BRAND BELOW");
				System.out
						.println("\t TOUSlesJOURS \t : 1 \n\t A TWOSOME PLACE : 2 \n\t bibigo \t : 3 \n\t bibigo dining \t : 4 \n\t VIPS \t\t : 5 \n\t China Factory \t : 6 ");
				System.out
						.println("\t===============================================\t");
				// store user choice of brand, and later used for calculating lower bound and upper bound
				brand = scan.nextInt();

				// depending on brand, print out different contents
				switch (brand) {
				case 1: // TOUSlesJOURS
					System.out.println("\t=====>>>"
							+ "Current Brand: TOUSlesJOURS" + "<<<=====\t");
					break;
				case 2: //
					System.out.println("\t=====>>>"
							+ "Current Brand: A TWOSOME PLACE " + "<<<=====\t");
					break;
				case 3: //
					System.out.println("\t=====>>>" + "Current Brand: bibigo"
							+ "<<<=====\t");
					break;
				case 4:
					System.out.println("\t=====>>>"
							+ "Current Brand: bibigo dining" + "<<<=====\t");
					break;
				case 5:
					System.out.println("\t=====>>>" + "Current Brand: VIPS"
							+ "<<<=====\t");
					break;
				case 6:
					System.out.println("\t=====>>>"
							+ "Current Brand: China Factory" + "<<<=====\t");
					break;
				}
				System.out.println("\tChoose one Mode BELOW");
				// calculate boundaries using brand
				// upper, lower : boundaries of Branch, 3 digit number
				// upper_r, lower_r : boundaries of Restaurant, 4 digit number
				upper = (brand + 1) * 100 - 1;
				lower = brand * 100;

				upper_r = (brand + 1) * 1000 - 1;
				lower_r = brand * 1000;

				System.out
						.println("\t Revenue Management \t : 1\n\t Evaluation Management \t : 2\n\t Population on Location  : 3");
				detail = scan.nextInt();

				// separate sub menus depending on user input "detail"
				switch (detail) {
				case 1:// Revenue Management
					System.out.println("\t     ->Revenue Manamgement : 1 \n");
					System.out
							.println("\t     ->Revenue on Menu : 1 \n\t     ->Revenue on Restaurant : 2 ");
					detail2 = scan.nextInt();
					switch (detail2) {
					case 1:// Revenue Management on menu
						try {
							// create statement for SELECT query
							st = con.createStatement();
							// select all attributes from view dbcourse_pure_cost where id is in boundary(lower, upper) and order the result by sum on descendant order
							sql = "select * from dbcourse_pure_cost where id>= "
									+ lower
									+ " and ID < "
									+ upper
									+ " order by sum desc";
							// Execute select query and store result table into rs
							rs = st.executeQuery(sql);
							System.out
									.println("\t\t********!!SUCCESS!!********");
							System.out.println("\t\tID\t" + " Name\t\t\t\t\t"
									+ "Times\t" + "Sum\t");
						}
						// Suppose All Exception occurs because user input was wrong
						catch (SQLException e) {
							System.out.println("Wrong SQL QUERY : Check again");
						}
						// print out Result table 'till the end of line
						while (rs.next()) {
							System.out.println("\t\t" + rs.getInt(1) + "\t "
									+ rs.getString(2) + "\t\t\t "
									+ rs.getInt(3) + " \t " + rs.getInt(4));
						}
						System.out.println();
						break;
					case 2:// Revenue Management on brand
						try {
							st = con.createStatement();
							// Update dbcourse_restaurant.pure_cost by adding dbcourse_pure_menu.sum when two table's ID is same
							sql = "update dbcourse_restaurant, dbcourse_pure_menu ";
							sql += "set dbcourse_restaurant.pure_cost=dbcourse_restaurant.pure_cost+dbcourse_pure_menu.sum ";
							sql += "where dbcourse_restaurant.id = dbcourse_pure_menu.id";

							// execute Update queries, sending sql as parameter
							st.executeUpdate(sql);
							/* Show the Update Restaurant_Pure_Cost */

							// Select all attributes from DBCOURSE_Restaurant natural join DBCOURSE_Name where DBCOURSE_Restaurant's ID is in boundary (lower_r, upper_r)
							sql = "select * ";
							sql += " from DBCOURSE_Restaurant natural join DBCOURSE_Name";
							sql += " where DBCOURSE_Restaurant.id > " + lower_r
									+ " and DBCOURSE_Restaurant.id < "
									+ upper_r;
							// execute Select query and store the result table into rs, sending sql as parameter
							rs = st.executeQuery(sql);
							System.out
									.println("ID \t Pure_Cost \t Brand \t\t Branch ");
							// print out Result table 'till the end of line
							while (rs.next()) {
								System.out.println(rs.getInt("ID") + "\t"
										+ rs.getInt("Pure_Cost") + "\t"
										+ rs.getString("Brand") + "\t\t"
										+ rs.getString("Branch"));
							}
						}
						// Suppose All Exception occurs because user input was wrong
						catch (SQLException e) {
							System.out.println("Wrong SQL QUERY : Check again");
						}

						System.out.println("Done!\n");
						System.out
								.println("Do you want to close the deficit restaurants? : Enter 0");
						int delete = scan.nextInt();

						// if user want to delete restaurant in deficit
						if (delete == 0) {
							System.out
									.print("Please enter the restaurant id you want to delete\n: ");
							// specify which restaurant to be deleted by user input
							int id = scan.nextInt();

							// turn off auto commit : transaction start
							con.setAutoCommit(false);

							/* Contacti_info Delete */
							sql = "delete from DBCOURSE_Contact_info ";
							sql += "where DBCOURSE_Contact_info.ID =" + id
									+ " ";
							st.executeUpdate(sql);

							/* Location Delete */
							sql = "delete from DBCOURSE_Location ";
							sql += "where DBCOURSE_Location.ID =" + id + " ";
							st.executeUpdate(sql);

							/* Evaluation Delete */
							sql = "delete from DBCOURSE_Evaluation ";
							sql += "where DBCOURSE_Evaluation.ID =" + id + " ";
							st.executeUpdate(sql);

							/* Name Delete */
							sql = "delete from DBCOURSE_Name ";
							sql += "where DBCOURSE_Name.ID =" + id + " ";
							st.executeUpdate(sql);

							/* Management Delete */
							sql = "delete from DBCOURSE_Management ";
							sql += "where DBCOURSE_Management.ID =" + id + " ";
							st.executeUpdate(sql);

							/* Renew Delete */
							sql = "delete from DBCOURSE_Renew ";
							sql += "where DBCOURSE_Renew.ID =" + id + " ";
							st.executeUpdate(sql);

							/* Employee Delete */
							sql = "delete from DBCOURSE_Employee ";
							sql += "where DBCOURSE_Employee.WorkPlace =" + id
									+ " ";
							st.executeUpdate(sql);

							/* Restaurant Delete */
							sql = "set foreign_key_checks = 0 ";
							st.executeUpdate(sql);

							sql = "delete from DBCOURSE_Restaurant ";
							sql += "where DBCOURSE_Restaurant.ID = " + id + " ";
							st.executeUpdate(sql);

							sql = "set foreign_key_checks = 1 ";
							st.executeUpdate(sql);

							try {
								con.commit();
								System.out.println("Done!\n");

								// Select query of showing all left restaurants
								sql = "select * from DBCOURSE_Restaurant ";
								rs = st.executeQuery(sql);

								System.out.println("ID \t Pure_Cost ");
								// print out Result table 'till the end of line
								while (rs.next()) {
									System.out.println(rs.getInt("ID") + "\t"
											+ rs.getInt("Pure_Cost"));
								}
								System.out.println();

							}
							// If any execution of query has failed, roll back all executions for preserving consistency
							catch (SQLException e) {
								System.out.println(e.getMessage());
								con.rollback();
							}
							// turn on auto commit : transaction end
							con.setAutoCommit(true);
						}
						// If user don't want to delete restaurant
						else {
							System.out.println("Good Bye. ");
						}
						break;

					default:
						System.out
								.println(" Wrong Menu INPUT : RETYPE YOUR ANSWER. ");
						break;
					}
					break;
				case 2:// Evaluation Management

					System.out.println("\t     ->Evaluation Manamgement   ");
					System.out
							.println("\t     ->Please enter into Evaluation Criteria:(Good,Poor)");

					// variables for criteria of which is good or poor restaurant
					double good = 0,
					poor = 0;
					// create statement for select query
					st = con.createStatement();

					// store user inputs in variable good, poor
					System.out.print("\t     ->Good Criteria : ");
					good = scan.nextDouble();

					System.out.print("\t     ->Poor Criteria : ");
					poor = scan.nextDouble();

					/* Show the results: Good Restaurants */
					// select all attributes from view DBCOURSE_Total_Eval where ID in boundary (lower_r, upper_r) and Grade higher than variable good, and order the result by Grade
					sql = "select * from DBCOURSE_Total_Eval ";
					sql += "where ID BETWEEN " + lower_r + " AND " + upper_r
							+ " ";
					sql += "AND Grade > " + good + " ";
					sql += "order by Grade ";

					// execute Select query
					rs = st.executeQuery(sql);
					System.out
							.println("\t     ************Good Restaurants****************");

					System.out
							.println("\t     ID \t Pure_Cost \t Brand \t Branch ");

					try {
						// print out Result table 'till the end of line
						while (rs.next()) {
							System.out.println("\t     " + rs.getInt("ID")
									+ "\t" + rs.getDouble("Grade") + "\t"
									+ rs.getString("Brand") + "\t"
									+ rs.getString("Branch"));
						}
						System.out.println("Done!\n");
					} catch (SQLException e) {
						e.printStackTrace();
					}

					/* Show the results: Fair Restaurants */
					// select all attributes from view DBCOURSE_Total_Eval where ID in boundary (lower_r, upper_r) and Grade between variable poor and good, and order the result by Grade
					sql = "select * from DBCOURSE_Total_Eval ";
					sql += "where ID BETWEEN " + lower_r + " AND " + upper_r
							+ " ";
					sql += "AND Grade BETWEEN " + poor + " AND " + good + " ";
					sql += "order by Grade ";

					rs = st.executeQuery(sql);
					System.out
							.println("\t     ************Fair Restaurants****************");

					System.out
							.println("\t     ID \t Pure_Cost \t Brand \t Branch ");

					try {
						// print out Result table 'till the end of line
						while (rs.next()) {
							System.out.println("\t     " + rs.getInt("ID")
									+ "\t" + rs.getDouble("Grade") + "\t"
									+ rs.getString("Brand") + "\t"
									+ rs.getString("Branch"));
						}
						System.out.println("Done!\n");
					} catch (SQLException e) {
						e.printStackTrace();
					}

					/* Show the results: Poor Restaurants */
					// select all attributes from view DBCOURSE_Total_Eval where ID in boundary (lower_r, upper_r) and Grade lower than variable poor, and order the result by Grade
					sql = "select * from DBCOURSE_Total_Eval ";
					sql += "where ID BETWEEN " + lower_r + " AND " + upper_r
							+ " ";
					sql += "AND Grade < " + poor + " ";
					sql += "order by Grade ";

					rs = st.executeQuery(sql);
					System.out
							.println("\t     ************Poor Restaurants****************");

					System.out
							.println("\t     ID \t Pure_Cost \t Brand \t Branch ");

					try {
						// print out Result table 'till the end of line
						while (rs.next()) {
							System.out.println("\t     " + rs.getInt("ID")
									+ "\t" + rs.getDouble("Grade") + "\t"
									+ rs.getString("Brand") + "\t"
									+ rs.getString("Branch"));
						}
						System.out.println("Done!\n");
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.println();
					break;
				
				case 3:// Population on Location
					// variable Gu storing user input for location specification
					String Gu = null;
					System.out.println("\t     ->Population on Location   ");

					System.out
							.print("\t     ->Please Specify area on search: ");
					Gu = scan.next();

					// Select all attributes from dbcourse_Location where ID in boundary(lower_R, upper_r) and Gu is same with user input Gu
					sql = "select *";
					sql += " from dbcourse_Location";
					sql += " where id>= " + lower_r + " and id< " + upper_r;
					sql += " and Gu = '" + Gu + "'";

					// create statement for select query
					st = con.createStatement();

					try {
						// execute select query and store the result table into rs, sending sql as parameter
						rs = st.executeQuery(sql);

					}
					// Suppose All Exception occurs because user input was wrong
					catch (SQLException e) {
						System.out
								.println("No branch on this area : change area");
					}

					try {
						// print out Result table 'till the end of line
						while (rs.next()) {
							System.out
									.println("\t     ok! The restaurant is searched.");
							System.out
									.println("\t     ID \t Gu \t\t Dong \t Rest ");
							System.out.println("\t     " + rs.getInt(1)
									+ " \t " + rs.getString(2) + " \t\t "
									+ rs.getString(3) + " \t "
									+ rs.getString(4));
						}
					}
					catch (Exception ne) {
						System.out
								.println("Can not Reach Result Table : Check Qeury again");
					}
					break;
				default:
					System.out
							.println(" Wrong Menu INPUT : RETYPE YOUR ANSWER. ");
					break;
				}
				break;
			case 3: // Member management
				System.out.println("\t=====>>>"
						+ "Current Mode: Member Management" + "<<<=====\t");
				System.out.println("\tChoose one BRAND BELOW");
				System.out
						.println("\t TOUSlesJOURS \t : 1 \n\t A TWOSOME PLACE : 2 \n\t bibigo \t : 3 \n\t bibigo dining \t : 4 \n\t VIPS \t\t : 5 \n\t China Factory \t : 6 ");
				System.out
						.println("\t===============================================\t");
				// store user choice of brand, and later used for calculating lower bound and upper bound
				brand = scan.nextInt();
				// calculate boundary depending on brand
				upper = (brand + 1) * 100 - 1;
				lower = brand * 100;

				// depending on brand, print out different contents
				switch (brand) {
				case 1: // TOUSlesJOURS
					System.out.println("\t=====>>>"
							+ "Current Brand: TOUSlesJOURS" + "<<<=====\t");
					break;
				case 2: //
					System.out.println("\t=====>>>"
							+ "Current Brand: A TWOSOME PLACE " + "<<<=====\t");
					break;
				case 3: //
					System.out.println("\t=====>>>" + "Current Brand: bibigo"
							+ "<<<=====\t");
					break;
				case 4:
					System.out.println("\t=====>>>"
							+ "Current Brand: bibigo dining" + "<<<=====\t");
					break;
				case 5:
					System.out.println("\t=====>>>" + "Current Brand: VIPS"
							+ "<<<=====\t");
					break;
				case 6:
					System.out.println("\t=====>>>"
							+ "Current Brand: China Factory" + "<<<=====\t");
					break;
				}
				System.out.println("\tChoose one Mode BELOW");

				System.out
						.println("\t Preference in age \t : 1\n\t Preference in gender \t : 2");
				// store choice of sub menu
				detail = scan.nextInt();
				switch (detail) {
				case 1:// favorite menu depending on age
					try {
						// create statement for select queries
						st = con.createStatement();
						// execute select queries when age 10~19, 20~29, 30~39, 40~49, 50~59, 60~69
						for (int i = 1; i < 7; i++) {
							age_up = (i + 1) * 10 - 1;
							age_down = i * 10;
							System.out.println("\t Age from " + age_down
									+ " to " + age_up);
							// Select Menu, Name from join 2 tables where member's age is in boundary(age_down, age_up) and Menu is in boundary(lower, upper) and group the result table by Menu and order it by sum(Times) descendant, limit 5
							sql = "select Menu, Name from DBCOURSE_Orders join DBCOURSE_Menu on (DBCOURSE_Menu.ID = Menu) ";
							sql += " where Member in (select ID from DBCOURSE_Member where Age between "
									+ age_down + " and " + age_up + ")";
							sql += " and Menu between " + lower + " and "
									+ upper;
							sql += " group by Menu ";
							sql += " order by sum(Times) desc limit 5 ";
							rs = st.executeQuery(sql);

							// print out there are no purchase on brand when the age is in this boundary
							if (!rs.next()) {
								System.out
										.println("\t No purchase on this brand's products ");
							}
							System.out.println("\t   Menu_ID" + "\t" + "Name");
							// print out Result table 'till the end of line
							while (rs.next()) {
								System.out.println("\t   " + rs.getInt(1)
										+ "\t" + rs.getString(2));
							}
						}
						st.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}

					break;
				case 2:// favorite menu depending on gender
					try {
						// create statement for select queries
						Statement stmt = con.createStatement();
						System.out.println("\t Woman's favorites : ");
						// select Menu, Name from join 2 tables where Member's gender is woman(0) and Menu in boundary(lower, upper) and group the result table by Menu and order it by sum(Times) descendant, limit 5 
						sql = "select Menu, Name ";
						sql += "From DBCOURSE_Orders join DBCOURSE_Menu on (DBCOURSE_Menu.ID = Menu) ";
						sql += "where Member in ";
						sql += "(select ID from DBCOURSE_Member where Gender = 0)";
						sql += " and Menu between " + lower + " and " + upper;
						sql += " group by Menu ";
						sql += " order by sum(Times) desc limit 5 ";

						// execute select query and store the result table into variable rset, sending sql as parameter
						ResultSet rset = stmt.executeQuery(sql);

						System.out.println("\t   Menu_ID" + "\t" + "Name");
						// print out Result table 'till the end of line
						while (rset.next()) {
							System.out.println("\t   " + rset.getInt(1) + "\t"
									+ rset.getString(2));
						}
						stmt.close();

						stmt = con.createStatement();
						System.out.println("\t Man's favorites : ");
						// select Menu, Name from join 2 tables where Member's gender is man(1) and Menu in boundary(lower, upper) and group the result table by Menu and order it by sum(Times) descendant, limit 5
						sql = "select Menu, Name ";
						sql += "From DBCOURSE_Orders join DBCOURSE_Menu on (DBCOURSE_Menu.ID = Menu) ";
						sql += "where Member in ";
						sql += "(select ID from DBCOURSE_Member where Gender = 1)";
						sql += " and Menu between " + lower + " and " + upper;
						sql += " group by Menu ";
						sql += " order by sum(Times) desc limit 5 ";
						
						// execute select query and store the result table into variable rset, sending sql as parameter
						rset = stmt.executeQuery(sql);

						System.out.println("\t   Menu_ID" + "\t" + "Name");
						// print out Result table 'till the end of line
						while (rset.next()) {
							System.out.println("\t   " + rset.getInt(1) + "\t"
									+ rset.getString(2));
						}
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					break;
				default:
					System.out
							.println(" Wrong Menu INPUT : RETYPE YOUR ANSWER. ");
					break;
				}
				break;

			// When user want to finish while loop
			case 0:
				System.out.println(" Bye Bye. ");
				// set stop to false so that next time while loop is not available
				stop = false;
				// close connection
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			default:
				System.out.println(" Wrong Menu INPUT : RETYPE YOUR ANSWER. ");
				break;
			}

		}

	}

/** method for building connection
 * @return conn connection got by driver, using all static final variables
 * @throws SQLException
 */
	public Connection getDBConnection() throws SQLException {
		try {
			Class.forName(DB_DRIVER).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection conn = DriverManager.getConnection(DB_CONNECTION, DB_USER,
				DB_PASSWORD);
		return conn;
	}
}
