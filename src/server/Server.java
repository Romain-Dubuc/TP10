package server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import static server.Property.getSubjectInfo;
import static server.Property.getSubjects;

/**
 * Server is a class that allows sending information to the client
 *
 * @author Romain DUBUC
 * @version 1.0
 */
public class Server extends Thread{
    /**
     * The port to set up the server
     */
    public static final int PORT_NUMBER = 1234;

    /**
     * Socket to send information to the client
     */
    protected Socket socket;

    /**
     * The logger to have server information
     */
    private static final Logger LOGGER = Logger.getLogger("log");

    /**
     * Set up the server with the given socket
     * @param socket The socket to communicate with the client
     */
    private Server(Socket socket) {
        this.socket = socket;
        LOGGER.log(Level.INFO, "New client connected from {0}", socket.getInetAddress().getHostAddress());
        start();
    }

    /**
     * Send information about the client's request
     * @param request The client's request
     * @param data_json Parser to get data of the data file
     * @return information requested for the client
     */
    public String responseRequest(String request, DataJSON data_json) {
        // Make the client request readable
        JSONObject request_object = new JSONObject(request);
        // Get the arguments of the request
        JSONArray args = new JSONArray(request_object.get("args").toString());
        try {
            switch (request_object.get("command").toString()) {
                case "getSubjects":
                    return getSubjects(Boolean.parseBoolean(args.get(0).toString())).toString() + "\n";
                case "getInfoSubjectsForLevel":
                    return data_json.getInfoSubjectsForLevel(Integer.parseInt(args.get(0).toString())).toString() + "\n";
                case "unionMarksBySubjectForClassSpecialExam":
                    return data_json.unionMarksBySubjectForClassSpecialExam(
                            args.get(0).toString(),
                            Integer.parseInt(args.get(1).toString()),
                            args.get(2).toString()
                    ).toString() + "\n";
                case "unionMarksBySubjectForClass":
                    return data_json.unionMarksBySubjectForClass(args.get(0).toString()).toString() + "\n";
                case "getSubjectInfo":
                    JSONArray response = new JSONArray();
                    response.put(getSubjectInfo(
                            args.get(0).toString(),
                            args.get(1).toString()
                    ));
                    return response.toString() + "\n";
                case "getClassesNameByLevel":
                    return data_json.getClassesNameByLevel(Integer.parseInt(args.get(0).toString())).toString() + "\n";
                case "getInfoSubjectsForClass":
                    return data_json.getInfoSubjectsForClass(args.get(0).toString()).toString() + "\n";
                case "getStudentsForClass":
                    return data_json.getStudentsForClass(args.get(0).toString()).toString() + "\n";
                case "importData":
                    data_json.importData(args.get(0).toString());
                    return "[done]" + "\n";
                case "getMarksForClass":
                    return data_json.getMarksForClass(args.get(0).toString()).toString() + "\n";
                case "getMarksForStudent":
                    return data_json.getMarksForStudent(args.get(0).toString(), args.get(1).toString()).toString() + "\n";
                case "getMarksForLevel":
                    return data_json.getMarksForLevel(Integer.parseInt(args.get(0).toString())).toString() + "\n";
                default:
                    return "unknown command\n";
            }
        } catch (Exception e) {
            return "missing parameters\n";
        }
    }

    /**
     * Run the server for a client
     */
    @Override
    public void run() {
        // We initiate the input and output stream to communicate with the client
        try (InputStream in = socket.getInputStream(); OutputStream out = socket.getOutputStream()) {
            // Get all data from the data file
            DataJSON data_json = new DataJSON();

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String request;

            // Read all client requests
            while ((request = br.readLine()) != null) {
                LOGGER.log(Level.INFO, "Message received from server: {0}", request);
                // Get and response to the client request
                out.write(responseRequest(request, data_json).getBytes());
            }

        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, "Unable to get streams from client");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The main function to start the server
     * @param args the arguments of the main function
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        LOGGER.log(Level.INFO, "SocketServer waiting client");
        try (ServerSocket server = new ServerSocket(PORT_NUMBER)) {
            while (true) {
                // Create a new connection when there is a new client to allow multiple clients
                new Server(server.accept());
            }
        } catch (IOException ex) {
            LOGGER.log(Level.INFO, "Unable to start server");
        }
    }
}
