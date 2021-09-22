package Server;

import java.io.Console;
import java.sql.*;
import java.util.Scanner;

public class DatabaseManager {
    public static final String ORGANIZATIONS_TABLE = "organizations";
    public static final String USER_TABLE = "users";
    public static final String COORDINATES_TABLE = "coordinates";
    public static final String ADDRESS_TABLE = "address";

    public static final String ORGANIZATIONS_TABLE_ID_COLUMN = "id";
    public static final String ORGANIZATIONS_TABLE_NAME_COLUMN = "name";
    public static final String ORGANIZATIONS_TABLE_ANNUAL_TURNOVER_COLUMN = "annual_turnover";
    public static final String ORGANIZATIONS_TABLE_COORDINATES_ID_COLUMN = "coordinates_id";
    public static final String ORGANIZATIONS_TABLE_CREATION_DATE_COLUMN = "creationDate";
    public static final String ORGANIZATIONS_TABLE_FULL_NAME_COLUMN = "full_name";
    public static final String ORGANIZATIONS_TABLE_USER_ID_COLUMN = "user_id";
    public static final String ORGANIZATIONS_TABLE_EMPLOYEES_COUNT_COLUMN = "employees_count";
    public static final String ORGANIZATIONS_TABLE_ADDRESS_ID_COLUMN = "address_id";
    public static final String ORGANIZATIONS_TABLE_ORGANIZATION_TYPE_COLUMN = "organization_type";
    public static final String ORGANIZATIONS_TABLE_KEY_COLUMN = "key";

    public static final String USER_TABLE_ID_COLUMN = "id";
    public static final String USER_TABLE_LOGIN_COLUMN = "login";
    public static final String USER_TABLE_PASSWORD_COLUMN = "password";
    public static final String USER_TABLE_ONLINE_COLUMN = "online";

    public static final String COORDINATES_TABLE_ID_COLUMN = "id";
    public static final String COORDINATES_TABLE_X_COLUMN = "x";
    public static final String COORDINATES_TABLE_Y_COLUMN = "y";

    public static final String ADDRESS_TABLE_ID_COLUMN = "id";
    public static final String ADDRESS_TABLE_STREET_COLUMN = "street";
    public static final String ADDRESS_TABLE_ZIP_CODE_COLUMN = "zipcode";

    //private final String url = "jdbc:postgresql://localhost:5678/studs"; //для идеи
    private final String url = "jdbc:postgresql://pg:5432/studs"; //для хелиоса
    //"jdbc:postgresql://pg:5432/studs"
    private String login;
    private String password;
    private Connection connection;

    public DatabaseManager() {
        doConnectionToDatabase();
    }

    private void doConnectionToDatabase() {
        Scanner scanner = new Scanner(System.in);
        Console console = System.console();
        System.out.println("Database connection");
        while (true) {
            System.out.println("enter login:");
            this.login = scanner.nextLine();
            System.out.println("enter password:");
            if (console == null) {
                this.password = scanner.nextLine();
            } else {
                this.password = String.valueOf(console.readPassword());
            }
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(url, login, password);
                System.out.println("Database connection!");
                break;
            } catch (SQLException e) {
                System.out.println("Check login and password or url");
            } catch (ClassNotFoundException e) {
                System.out.println("Database driver not found!");
                System.exit(0);
            }
        }
    }

    public PreparedStatement doPreparedStatement(String sqlStatement, boolean generateKeys) throws SQLException {
        PreparedStatement preparedStatement;
        try {
            if (connection == null) throw new SQLException();
            int autoGeneratedKeys = generateKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS;
            preparedStatement = connection.prepareStatement(sqlStatement,autoGeneratedKeys);
            return preparedStatement;
        } catch (SQLException e) {
            if (connection == null) {
                System.out.println("connection null!");
            }
            throw new SQLException();
        }
    }

    public void closePreparedStatement(PreparedStatement preparedStatement) {
        if (preparedStatement == null) return;
        try {
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("Failed to close SQL query");
        }
    }

    public void closeConnection() {
        if (connection == null) return;
        try {
            connection.close();
            System.out.println("The connection to the database has been dropped!");
        } catch (SQLException e) {
            System.out.println("An error occurred while disconnecting the database connection!");
        }
    }

    public void setCommit() {
        try {
            if (connection == null) throw new SQLException();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            System.out.println("An error occurred while setting 'commit'!");
        }
    }

    public void setAutoCommit() {
        try {
            if (connection == null) throw new SQLException();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("An error occured while setting 'auto_commit'!");
        }
    }

    public void commit() {
        try {
            if (connection == null) throw new SQLException();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("An error occurred while confirming the new state of the database!");
        }
    }

    public void rollback() {
        try {
            if (connection == null) throw new SQLException();
            connection.rollback();
        } catch (SQLException e) {
            System.out.println("An error occurred while resetting the database to its original state!");
        }
    }

    public void setSavepoint() {
        try {
            if (connection == null) throw new SQLException();
            connection.setSavepoint();
        } catch (SQLException e) {
            System.out.println("An error occurred while saving the database state!");
        }
    }
}
