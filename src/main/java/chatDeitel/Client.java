package chatDeitel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;

public class Client extends JFrame {
	private JTextField enterField;
	private JTextArea displayArea;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message;
	private String chatServer;
	private Socket client;

	public Client(String host) {
		super("Client");

		message = "";
		chatServer = host;

		enterField = new JTextField();
		enterField.setEditable(false);
		enterField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				sendData(event.getActionCommand());
				enterField.setText("");
			}
		});

		add(enterField, BorderLayout.NORTH);

		displayArea = new JTextArea();
		add(new JScrollPane(displayArea), BorderLayout.CENTER);

		setSize(300, 150);
		setVisible(true);
	}

	public void runClient() {
		try {
			connectToServer();
			getStreams();
			processConnection();
		} catch (EOFException eofexception) {
			displayMessage("\nClient terminated connection");

		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		} finally {
			closeConnection();
		}
	}

	private void connectToServer() throws IOException {
		displayMessage("Attempting connection\n");

		client = new Socket(InetAddress.getByName(chatServer), 12345);

		displayMessage("Connected to " + client.getInetAddress().getHostAddress());
	}

	private void getStreams() throws IOException {
		output = new ObjectOutputStream(client.getOutputStream());
		output.flush();

		input = new ObjectInputStream(client.getInputStream());
		displayMessage("\nGot IO Streams\n");
	}

	private void processConnection() throws IOException {
		setTextFieldEditable(true);
		do {
			try {
				message = (String) input.readObject();
				displayMessage("\n" + message);
			} catch (ClassNotFoundException classnotfoundexception) {
				displayMessage("\nUnknown Object type received");
			}
		} while (!message.equals("SERVER>>TERMINATE"));
	}

	private void closeConnection() {
		displayMessage("\nClosing connection");

		setTextFieldEditable(false);

		try {
			output.close();
			input.close();
			client.close();
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		}
	}

	private void sendData(String message) {
		try {
			output.writeObject("CLIENT>> " + message);
			output.flush();
			displayMessage("\nCLIENT>> " + message);
		} catch (IOException ioexception) {
			displayArea.append("\nError writting object");
		}
	}

	private void displayMessage(final String messageToDisplay) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				displayArea.append(messageToDisplay);
			}
		}

		);
	}
	private void setTextFieldEditable(final boolean editable) {
		SwingUtilities.invokeLater(
				new Runnable()
				{
					public void run() {
						enterField.setEditable(editable);
					}
				}
				
				);
	}

	public static void main(String[] args) {
		Client application;
		
		if (args.length==0) {
			application = new Client("127.0.0.1");			
		} else {
			application = new Client(args[0]);
		}
		
		application.setDefaultCloseOperation(EXIT_ON_CLOSE);
		application.runClient();
	}

}
