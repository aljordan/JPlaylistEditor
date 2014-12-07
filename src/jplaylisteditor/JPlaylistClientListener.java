package jplaylisteditor;

import java.net.*;
import java.io.*;

/**
 *
 * @author Alan Jordan
 */
public class JPlaylistClientListener implements Runnable{
    private JPlaylistGUI gui;
    private int port = 6123; // default server port
    private Socket s1; // socket to speak to the server
    private ObjectInputStream in;
    private ObjectOutputStream out;     // for outgoing messages
    private boolean running = false;     // used to shutdown the thread
    JPlaylistOptions options;


    public JPlaylistClientListener(JPlaylistGUI gui, JPlaylistOptions options) {
        this.gui = gui;
        this.options = options;
    }


    private void setUpSocket(String serverName) {
        try {
            //create a new socket s1
            s1 = new Socket(serverName, port);
            out = new ObjectOutputStream(s1.getOutputStream());

            gui.setNetworkStatus(JPlaylistEditorNetworkMessage.ConnectionStatus.SUCCESS,
                    JPlaylistEditorNetworkMessage.NetworkApplicationType.CLIENT,
                    "Client connected to " + serverName);
        }//end try
        catch (IOException ioe) {
            gui.setNetworkStatus(JPlaylistEditorNetworkMessage.ConnectionStatus.FAILURE,
                    JPlaylistEditorNetworkMessage.NetworkApplicationType.CLIENT,
                    "Client could not connect to " + serverName);
            //print out error to consol
            System.out.println(ioe.getMessage());
        }//end catch

    }//end setUpSocket

  private void receiveObject(JPlaylistEditorNetworkMessage message) {
      System.out.println("Got message from server " + message.getNetworkMessageType().name());
      if (message.getNetworkMessageType() == JPlaylistEditorNetworkMessage.NetworkMessageType.GET_LIBRARY_MESSAGE) {
          gui.setLibrariesFromNetwork(message.getLibraryRoots());
      }
  }

  public void sendObject(JPlaylistEditorNetworkMessage message) {
      try {
          out.writeObject(message);
          out.flush();
          if (message.getNetworkMessageType() == JPlaylistEditorNetworkMessage.NetworkMessageType.PLAYLIST_MESSAGE) {
            out.reset();  //AARRGGHH - this keeps the object output stream from caching objects so they can be updated to new objects.
          }
      }
      catch (Exception e) {
          System.out.println(e.getMessage());
          e.printStackTrace();
      }
  }

    public void run() {
        setUpSocket(options.getServerIpAddress());

        //set running variable to true to control loop
        running = true;
        //while running listen for InputStream and append to txtOutput
        try {
            in = new ObjectInputStream(s1.getInputStream());
            while (running) {
                JPlaylistEditorNetworkMessage networkMessage = (JPlaylistEditorNetworkMessage) in.readObject();

                receiveObject(networkMessage);
            }//end try

        } //catch SocketException and break out of while loop
        catch (Exception se) {
            System.out.println(se.getMessage());
            se.printStackTrace();
        }
    }//end run()

 public void stopClient() {
    if (running == true) {
      running = false;
      try {
        s1.close();
      }
      catch (IOException e) {
        System.out.println("Error closing client socket");
      }
      finally {
        s1 = null;
      }
      gui.setNetworkStatus(JPlaylistEditorNetworkMessage.ConnectionStatus.FAILURE,
                    JPlaylistEditorNetworkMessage.NetworkApplicationType.CLIENT,
                    "Client Stopped");
    }
  }


}
