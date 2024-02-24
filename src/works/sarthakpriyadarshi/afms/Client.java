package works.sarthakpriyadarshi.afms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ArmedForcesClientGUI().createAndShowGUI());
    }
}

class ArmedForcesClientGUI {
    private final JTextArea outputArea;
    private final JTextField inputField;
    private ObjectOutputStream outputStream;

    public ArmedForcesClientGUI() {
        outputArea = new JTextArea(20, 40);
        inputField = new JTextField(20);
        outputArea.setEditable(false);
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Armed Forces Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon originalIcon = new ImageIcon("img/afms.png"); // Replace with the path to your icon file
        frame.setIconImage(originalIcon.getImage());
        // Resize the icon
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        JButton addButton = new JButton("Add Service Personnel");
        JButton updateButton = new JButton("Update Service Personnel");
        JButton displayButton = new JButton("Display Service Personnel");
        JButton deleteButton = new JButton("Delete Service Personnel");

        JPanel panel = new JPanel();
        panel.add(new JLabel(resizedIcon));
        panel.add(addButton);
        panel.add(updateButton);
        panel.add(displayButton);
        panel.add(deleteButton);

        addButton.addActionListener(e -> performOperation(1));
        updateButton.addActionListener(e -> performOperation(2));
        displayButton.addActionListener(e -> performOperation(3));
        deleteButton.addActionListener(e -> performOperation(4));

        frame.getContentPane().add(panel, "North");
        frame.getContentPane().add(new JScrollPane(outputArea), "Center");

        frame.pack();
        frame.setVisible(true);
    }

    private void performOperation(int choice) {
        try {
            Socket socket = new Socket("localhost", 12345);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            // Send the choice to the server
            outputStream.writeObject(choice);
            outputStream.flush();

            switch (choice) {
                case 1:
                    addServicePersonnelDetails(inputStream);
                    break;
                case 2:
                    updateServicePersonnelDetails(inputStream);
                    break;
                case 3:
                    displayServicePersonnelDetails(inputStream);
                    break;
                case 4:
                    deleteServicePersonnelDetails(inputStream);
                    break;
                // Implement other cases if needed

            }

            // Close the connection
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addServicePersonnelDetails(ObjectInputStream inputStream) {
        // Collect service personnel details from the user and send to the server
        String name = JOptionPane.showInputDialog("Enter Name:");
        String serviceNumber = JOptionPane.showInputDialog("Enter Service Number:");
        String service = JOptionPane.showInputDialog("Enter Service (Army, Air Force, Navy):");
        String trade = JOptionPane.showInputDialog("Enter Trade:");

        try {
            ServicePersonnel personnel = new ServicePersonnel(name, serviceNumber, service, trade);
            outputStream.writeObject(personnel);
            outputStream.flush();

            // Receive and display the server's response
            String response = (String) inputStream.readObject();
            JOptionPane.showMessageDialog(null, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateServicePersonnelDetails(ObjectInputStream inputStream) {
        // Collect service number and updated trade from the user and send to the server
        String serviceNumberToUpdate = JOptionPane.showInputDialog("Enter Service Number to Update:");
        String updatedTrade = JOptionPane.showInputDialog("Enter Updated Trade:");

        try {
            // Send service number and updated trade to the server
            outputStream.writeObject(serviceNumberToUpdate);
            outputStream.writeObject(updatedTrade);
            outputStream.flush();

            // Receive and display the server's response
            String response = (String) inputStream.readObject();
            JOptionPane.showMessageDialog(null, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void displayServicePersonnelDetails(ObjectInputStream inputStream) {
        try {
            outputArea.setText(""); // Clear existing content in JTextArea

            // Read and display personnel details until the server signals the end
            while (true) {
                ServicePersonnel personnel = (ServicePersonnel) inputStream.readObject();

                if (personnel == null) {
                    // End of data signal received from the server
                    break;
                }

                // Display the personnel details in the JTextArea
                outputArea.append(personnel.toString() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void deleteServicePersonnelDetails(ObjectInputStream inputStream) {
        // Collect service number to delete from the user and send to the server
        String serviceNumberToDelete = JOptionPane.showInputDialog("Enter Service Number to Delete:");

        try {
            // Send service number to delete to the server
            outputStream.writeObject(serviceNumberToDelete);
            outputStream.flush();

            // Receive and display the server's response
            String response = (String) inputStream.readObject();
            JOptionPane.showMessageDialog(null, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}