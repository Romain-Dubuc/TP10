package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.ServerException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Client is a class which allows to receive information from the server
 *
 * @author Romain DUBUC
 * @version 1.0
 */
public class Client extends Application {
    /**
     * Socket to receive information from the server
     */
    private Socket socket = null;

    /**
     * Channel to send information to the server
     */
    private static PrintWriter out = null;

    /**
     * Channel to receive information from the server
     */
    private static BufferedReader in = null;

    /**
     * The logger to have client information
     */
    private static final Logger LOGGER = Logger.getLogger("log");

    /**
     * Getter for the receive channel
     * @return (BufferedReader) The receive channel from the server
     */
    private static BufferedReader getIn() {
        return in;
    }

    /**
     * Getter for the sending channel
     * @return (PrintWriter) The sending channel to server
     */
    private static PrintWriter getOut() {
        return out;
    }

    /**
     * Setter for the receive channel
     * @param new_in New receive channel
     */
    private static void setIn(BufferedReader new_in) {
        in = new_in;
    }

    /**
     * Setter for the sending channel
     * @param new_out New sending channel
     */
    private static void setOut(PrintWriter new_out) {
        out = new_out;
    }

    /**
     * Get specific data from the server with a command send to the server
     * @param command The data you want (The name of a function of ParserData class)
     * @param args The arguments linked to the command
     * @return A JSON Object/Array with requested information
     * @throws ServerException If the received data is not an array or a map
     */
    public static Object getDataAPI(String command, Object... args) throws ServerException {
        // Make JSON with the command and its arguments
        JSONObject request = new JSONObject();
        request.put("command", command);
        request.put("args", args);

        try {
            // Send the request to the server
            getOut().println(request);
            // Receive data from the server
            String line = getIn().readLine();
            LOGGER.log(Level.INFO, "Message received from server: {0}", line);

            // Make a JSON Array if the received data is an array
            // else if it's a Map, we make a JSON Object
            if (line.startsWith("[")) {
                return new JSONArray(line);
            }else if (line.startsWith("{")) {
                return new JSONObject(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new ServerException("Wrong data received from the server");
    }

    /**
     * Make the connection with the server and show the client window
     * @param stage The window to display
     */
    @Override
    public void start(Stage stage) {
        String serverHostname = "127.0.0.1";
        LOGGER.log(Level.INFO, "Connecting to host {0} on port 1234", serverHostname);

        try {
            // Make the connection with the server
            socket = new Socket(serverHostname, 1234);
            setOut(new PrintWriter(socket.getOutputStream(), true));
            setIn(new BufferedReader(new InputStreamReader(socket.getInputStream())));

            // Make the client window with the fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
            Parent root = loader.load();
            stage.setTitle("TP 10 - Client");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (UnknownHostException e) {
            LOGGER.log(Level.WARNING, "Unknown host: {0}", serverHostname);
            System.exit(1);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Connexion closed");
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop the client by closing all channels and the socket with the server
     */
    @Override
    public void stop() {
        try {
            getIn().close();
            getOut().close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The main function to start the client
     * @param args the arguments of the main function
     */
    public static void main(String[] args) {
        launch();
    }

}
