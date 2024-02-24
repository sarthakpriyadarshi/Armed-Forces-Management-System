package works.sarthakpriyadarshi.afms;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Server {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/afms";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "toor";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is running. Waiting for connections...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (
                    ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                    ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream())
            ) {
                int choice = (int) inputStream.readObject();
                handleClientRequest(choice, inputStream, outputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void handleClientRequest(int choice, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                switch (choice) {
                    case 1:
                        addServicePersonnelDetails(connection, inputStream, outputStream);
                        break;
                    case 2:
                        // Implement update operation
                        updateServicePersonnelDetails(connection, inputStream, outputStream);
                        break;
                    case 3:
                        displayServicePersonnelDetails(connection, outputStream);
                        break;
                    case 4:
                        // Implement delete operation
                        deleteServicePersonnelDetails(connection, inputStream, outputStream);
                        break;
                    default:
                        System.out.println("Invalid choice");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void addServicePersonnelDetails(Connection connection, ObjectInputStream inputStream, ObjectOutputStream outputStream) throws Exception {
            // Read service personnel details from the client
            ServicePersonnel personnel = (ServicePersonnel) inputStream.readObject();

            // Insert into the database
            String sql = "INSERT INTO service_personnel (name, service_number, service, trade) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, personnel.getName());
                statement.setString(2, personnel.getServiceNumber());
                statement.setString(3, personnel.getService());
                statement.setString(4, personnel.getTrade());
                statement.executeUpdate();
            }

            outputStream.writeObject("Service personnel added successfully.");
            outputStream.flush();
        }

        private void displayServicePersonnelDetails(Connection connection, ObjectOutputStream outputStream) throws Exception {
            String sql = "SELECT * FROM service_personnel";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();

                // Send the result set to the client
                while (resultSet.next()) {
                    ServicePersonnel personnel = new ServicePersonnel(
                            resultSet.getString("name"),
                            resultSet.getString("service_number"),
                            resultSet.getString("service"),
                            resultSet.getString("trade")
                    );
                    outputStream.writeObject(personnel);
                    outputStream.flush();
                }
            }

            // Send a signal to indicate the end of the data
            outputStream.writeObject(null);
            outputStream.flush();
        }
        private void updateServicePersonnelDetails(Connection connection, ObjectInputStream inputStream, ObjectOutputStream outputStream) throws Exception {
            String serviceNumberToUpdate = (String) inputStream.readObject();
            String updatedTrade = (String) inputStream.readObject();

            // Update the trade for the specified service number
            String sql = "UPDATE service_personnel SET trade = ? WHERE service_number = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, updatedTrade);
                statement.setString(2, serviceNumberToUpdate);
                statement.executeUpdate();
            }

            outputStream.writeObject("Service personnel details updated successfully.");
            outputStream.flush();
        }

        private void deleteServicePersonnelDetails(Connection connection, ObjectInputStream inputStream, ObjectOutputStream outputStream) throws Exception {
            String serviceNumberToDelete = (String) inputStream.readObject();

            // Delete the record for the specified service number
            String sql = "DELETE FROM service_personnel WHERE service_number = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, serviceNumberToDelete);
                statement.executeUpdate();
            }

            outputStream.writeObject("Service personnel details deleted successfully.");
            outputStream.flush();
        }
    }
}
