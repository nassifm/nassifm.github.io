package icse.demo.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * Code examples are adapted from:
 * https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html
 * and
 * https://docs.oracle.com/javase/tutorial/jdbc/basics/connecting.html
 */
public class SqlDatabaseConnectionDemo
{

	/*?
	 * Block:1
	 * Main
	 * This program showcases how to do 3 things with the JDBC API: 
	 * 1. Connect to an SQL database.
	 * 2. Read values from a table of the database.
	 * 3. Update some values in a table.
	 */
	public static void main(String[] args)
	{
		try (Connection connection = createConnection();)
		{
			viewTable(connection);
			float percentage = 80;
			modifyPrices(percentage, connection);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static Connection createConnection()
	{
		try
		{
			/*?
			* Keyword:connectionUrl
			* The general format of connection URLs is `protocol:subprotocol:subname`, where
			* - `protocol` is  `jdbc`,
			* - `subprotocol` is usually the database system name, 
			* - and `subname` depends on the database.
			* 
			* The exact format of the `subprotocol` and `subname` components is defined by each database provider.
			* Popular database systems include: MySQL (the database system in this example), SQLite, and PostgreSQL.
			* 
			* To see details about connection URLs for other common databases,
			* see [this codejava tutorial](https://www.codejava.net/java-se/jdbc/jdbc-database-connection-url-for-common-databases).
			* 
			* Internal:MySQL
			* connectionUrl
			* The basic format of connecting to a MySQL database is `jdbc:mysql://[host][:port]/[database]`,
			* whereby `host` represents an IP address or host name on which the server is running.
			* In our code example, the host is `localhost`, `8008` is the port number and `database1` is the database name. 
			* URL: https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-usagenotes-connect-drivermanager.html
			* 
			* Internal: SQLite
			* connectionUrl
			* The basic format of connecting to a SQLite database is `jdbc:sqlite:[database_file_path]`,
			* whereby `database_file_path` represents a relative or absolute path of the target SQLite database.
			* For example, `jdbc:sqlite:c:/User/Documents/database1.db` connects to database `database1.db` specified by an absolute path. 
			* URL: https://www.sqlitetutorial.net/sqlite-java/
			* 
			* Internal: PostgreSQL
			* connectionUrl
			* The basic format of connecting to a PostgreSQL database is `jdbc:postgresql://[host][:port]/[database]`,
			* whereby `host` represents an IP address or host name on which the server is running (optional, defaults to `localhost`),
			* `port` is the port number (optional, defaults to `5432`), and `database` is the database name.
			* For example, `jdbc:postgresql://localhost/ProductDB` connects to the `ProductDB` on PostgreSQL server on the host `localhost`.
			* URL:https://jdbc.postgresql.org/documentation/head/connect.html
			*/
			String connectionUrl = "jdbc:mysql://localhost:8008/database1";
			/*?
			 *Keyword:DriverManager
			 *When `getConnection` is called, `DriverManager` tries to select a suitable driver from the list of registered JDBC drivers.
			 *This is required to then establish a connection with the database specified by `connectionUrl`.
			 *
			 *An alternative is to use a [`DataSource`](https://docs.oracle.com/javase/tutorial/jdbc/basics/sqldatasources.html). 
			 * 
			 *Internal:JDBC drivers
			 *DriverManager
			 *JDBC Driver is a software component that enables java application to interact with the database.
			 *
			 *URL: https://docs.oracle.com/javase/tutorial/jdbc/basics/connecting.html
			 *
			 *Keyword: getConnection
			 *This method establishes a connection to the given database URL and returns it.
			 */
			Connection connection = DriverManager.getConnection(connectionUrl);
			return connection;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * This method prints the content of the "coffees" table in the console.
	 * 
	 * @param connection
	 *            the connection to the database
	 */
	/*?
	 * Block:1
	 * Retrieving Values From Table
	 * This method retrieves the contents of the `coffees` tables and prints them.
	 * 
	 * ###### Step-by-step:
	 * The overall approach in this method is:
	 * 1. Creating `Statement` object by calling `createStatement()` on `Connection` object 
	 * 2. Creating a `ResultSet` by calling `executeQuery()` on `Statement` object
	 * 3. Iterating through the `ResultSet` to obtain values in each row
	 * 
	 */
	public static void viewTable(Connection connection)
	{
		/*?
		 * Keyword:SELECT cof_name, sup_id, price, sales, total FROM coffees;
		 * The `SELECT` statement is used to get data from an SQL database. Its syntax is as follows:
		 * 
		 * `SELECT columnA, columnB, ...
		 * FROM table_name;`
		 * 
		 * To select all columns at once, `*` can be used instead of listing each column, like so:
		 * 
		 * `SELECT *
		 * FROM table_name;`
		 * 
		 * In our case, we only want to select columns `cof_name, sup_id, price, sales, total` from the `coffees` table.
		 * 
		 * URL: https://dev.mysql.com/doc/refman/8.0/en/select.html
		 */
		String query = "SELECT cof_name, sup_id, price, sales, total FROM coffees;";
		/*?
		 * Block:1
		 * try-with-resources
		 * The try-with-resources statement ensures that each resource is closed at the end of the statement execution. 
		 * URL: https://www.geeksforgeeks.org/try-with-resources-feature-in-java/
		 * 
		 * Internal:each resource
		 * 1
		 * A resource is an object that must be closed once your program is done using it. If we don't close the resources,
		 * it may constitute a resource leak and also the program could exhaust the resources available to it.
		 * URL: https://www.geeksforgeeks.org/try-with-resources-feature-in-java/
		 * 
		 * Internal:closed
		 * 1
		 * You can pass any object as a resource that implements _java.lang.AutoCloseable_, which includes all objects which implement _java.io.Closeable_.
		 * URL: https://www.geeksforgeeks.org/try-with-resources-feature-in-java/
		 */
		try (Statement stmt = connection.createStatement();
				/*?
				 * Keyword: ResultSet
				 * `ResultSet` maintains a cursor which points to the current row in the data. This is how we are able to traverse and obtain each row. 
				 * 
				 * We can also think of `ResultSet` objects as iterators.
				 * While `ResultSet` does not implement the `Iterable` interface, it follows an iterator design pattern.
				 * 
				 * Internal: cursor
				 * ResultSet
				 * The cursor in a `ResultSet` initially points before the first row. Calling `next()` moves the cursor to the next row in the set, 
				 * returning `false` when there are no longer any rows left. 
				 * 
				 * Using a `while` loop is therefore an efficient way of looping through all of the data in a `ResultSet`, as seen in the given code example.
				 * 
				 * Internal: iterator design pattern
				 * ResultSet
				 * The iterator design pattern is used to provide access to a collection of objects encapsulated within another object without violating 
				 * encapsulation and information hiding properties of this object.
				 * 
				 * In the case of `ResultSet`, it has a cursor pointing to a row of data. With each call to `next()`, the cursor moves down one row. 
				 * This is how data encapsulated within `ResultSet` is obtained using the iterator design pattern.  
				 * 
				 * Internal: encapsulation and information hiding
				 * iterator design pattern
				 * The idea of information hiding and encapsulation of structures is to only reveal
				 * the minimum amount of information that is necessary to use them, and hide the rest.
				 * 
				 */
				ResultSet rs = stmt.executeQuery(query);)
		{
			/*?
			 * Keyword:next
			 * This method both checks if there is another row, returning `true` or `false` accordingly, _and_ advances the cursor.
			 */
			while (rs.next())
			{
				/*?
				 * Block:5
				 * getXXX()
				 * `getXXX()` should be consistent with the data type of the SQL table column. If not, it will throw an exception. 
				 * 
				 * For instance, let's say column "sup_id" is actually of type VARCHAR. `getInt("sup_id")` would then be an inappropriate call.
				 * 
				 * Internal: VARCHAR
				 * 5
				 * VARCHAR, or Variable Character, is a variable length string.
				 * 
				 * Internal: variable
				 * VARCHAR
				 * The maximum length of String values in a VARCHAR column can be defined in parentheses
				 * when declaring the column type. For example, `VARCHAR(500)` can contain a maximum of 500 characters.
				 */
				String coffeeName = rs.getString("cof_name");
				int supplierID = rs.getInt("sup_id");
				float price = rs.getFloat("price");
				int sales = rs.getInt("sales");
				int total = rs.getInt("total");
				System.out.println(coffeeName + ", " + supplierID + ", " + price + ", " + sales + ", " + total);
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}

	/**
	 * This method modifies values in the "price" column of the "coffees" table using a percentage parameter.
	 * 
	 * @param percentage
	 *            the value by which to multiply the price. E.g., 80.0 multiplies the price by 80% (i.e., reduces the
	 *            price by 20%)
	 * @param connection
	 *            the database connection
	 */
	public static void modifyPrices(float percentage, Connection connection)
	{
		/*?
		 * Keyword: TYPE_SCROLL_SENSITIVE
		 * This constant indicates a scrollable `ResultSet`, meaning its cursor can move both forward and backward relative to the current position, and it can move to an absolute position.
		 * `TYPE_SCROLL_SENSITIVE` also makes `ResultSet` data sensitive to changes made to the database.
		 * 
		 * Internal: sensitive
		 * TYPE_SCROLL_SENSITIVE
		 * Taken from [tutorialspoint](https://www.tutorialspoint.com/what-is-type-scroll-sensitive-resultset-in-jdbc):
		 * "(Sensitivity) means if we have established a connection with a database using a JDBC program and retrieved a ResultSet holding all the records in a table named SampleTable.
		 * Meanwhile, if we have added some more records to the table (after retrieving the ResultSet), these recent changes will be reflected in the ResultSet object we previously obtained."
		 * 
		 * Keyword:CONCUR_UPDATABLE
		 * This constant indicates that the concurrency mode for this ResultSet object is updatable.
		 * This means that once you get a `ResultSet` object you can update its contents, which is particularly important for
		 * the task we are trying to complete since the goal is to update values in the `ResultSet`.
		 * 
		 * Block:1
		 * try-with-resources
		 * The try-with-resources statement ensures that each resource is closed at the end of the statement execution. 
		 * URL: https://www.geeksforgeeks.org/try-with-resources-feature-in-java/
		 * 
		 * Internal:each resource
		 * 1
		 * A resource is an object that must be closed once your program is done using it. If we don't close the resources,
		 * it may constitute a resource leak and also the program could exhaust the resources available to it.
		 * URL: https://www.geeksforgeeks.org/try-with-resources-feature-in-java/
		 * 
		 * Internal:closed
		 * 1
		 * You can pass any object as a resource that implements _java.lang.AutoCloseable_, which includes all objects which implement _java.io.Closeable_.
		 * URL: https://www.geeksforgeeks.org/try-with-resources-feature-in-java/
		 */
		try (Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				/*?
				* Keyword: ResultSet
				* `ResultSet` maintains a cursor which points to the current row in the data. This is how we are able to traverse and obtain each row. 
				* 
				* We can also think of `ResultSet` objects as iterators.
				* While `ResultSet` does not implement the `Iterable` interface, it follows an iterator design pattern.
				* 
				* Internal: cursor
				* ResultSet
				* The cursor in a `ResultSet` initially points before the first row. Calling `next()` moves the cursor to the next row in the set, 
				* returning `false` when there are no longer any rows left. 
				* 
				* Using a `while` loop is therefore an efficient way of looping through all of the data in a `ResultSet`, as seen in the given code example.
				* 
				* Internal: iterator design pattern
				* ResultSet
				* The iterator design pattern is used to provide access to a collection of objects encapsulated within another object without violating 
				* encapsulation and information hiding properties of this object.
				* 
				* In the case of `ResultSet`, it has a cursor pointing to a row of data. With each call to `next()`, the cursor moves down one row. 
				* This is how data encapsulated within `ResultSet` is obtained using the iterator design pattern.  
				* 
				* Internal: encapsulation and information hiding
				* iterator design pattern
				* The idea of information hiding and encapsulation of structures is to only reveal the minimum amount of information that is necessary to use them, and hide the rest.
				* 
				* Keyword:SELECT * FROM coffees;
				* The `SELECT` statement is used to get data from an SQL database.
				* To select all columns at once, `*` can be used instead of listing each column, like so:
				* 
				* `SELECT *
				* FROM table_name;`
				* 
				* In our case, we want to select _all_ columns from the `coffees` table.
				* 
				* URL: https://dev.mysql.com/doc/refman/8.0/en/select.html
				* 
				*/
				ResultSet uprs = stmt.executeQuery("SELECT * FROM coffees;");)
		{
			/*?
			 * Keyword:next
			 * This method both checks if there is another row, returning `true` or `false` accordingly, _and_ advances the cursor.
			 */
			while (uprs.next())
			{
				float f = uprs.getFloat("price");
				uprs.updateFloat("price", f * percentage);
				/*?
				 * Keyword:updateRow
				 * Per protocol, `updateRow()` must be called _after_ `updateFloat` in order to change the underlying database. 
				 */
				uprs.updateRow();
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
}
