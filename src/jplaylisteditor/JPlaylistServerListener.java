package jplaylisteditor;

import java.net.*;       //For Socket behavior
import java.io.*;        //For Stream behavior
import jplaylisteditor.JPlaylistEditorNetworkMessage.*;

/**
 *
 * @author Alan jordan
 */
public class JPlaylistServerListener implements Runnable {
  private int port = 6123; // default server port
  private ServerSocket ss;          // main connection listening socket
  private boolean running=false;     // used to shut down server when needed
  private JPlaylistGUI gui;
  private ServerConnectionThread sct;
  private JPlaylistOptions options;

  public JPlaylistServerListener(JPlaylistGUI gui, JPlaylistOptions options) {
      this.gui = gui;
      this.options = options;
  }

  /*
    run method initializes server listening socket
    and passes connections off to separate threads when they occur
  */
  public void run() {

    try {

      InetAddress inetAddress = InetAddress.getLocalHost();
      String ipAddress = inetAddress.getHostAddress();

      ss=new ServerSocket(port);
//      System.out.println("JplayListEditor Server: listening on port "
//              + ss.getLocalPort() + " on " + ss.getInetAddress());
      gui.setNetworkStatus(JPlaylistEditorNetworkMessage.ConnectionStatus.SUCCESS,
                    JPlaylistEditorNetworkMessage.NetworkApplicationType.SERVER,
                    "Server running on port " + ss.getLocalPort() + " on " + ipAddress);
      running = true;
    }
    catch(Exception e) {
      System.out.println("Error starting server");
      gui.setNetworkStatus(JPlaylistEditorNetworkMessage.ConnectionStatus.FAILURE,
                    JPlaylistEditorNetworkMessage.NetworkApplicationType.SERVER,
                    "Error starting server");
    }
    //constantly listen for incoming connections
    do {
      try {
        // when a connection is recieved, start a new ServerConnectionThread
        // and add it to the vector holding the connection threads
        sct = new ServerConnectionThread(ss.accept());
      }
      catch(Exception e) {
        System.out.println("Error accepting connection");
        running=false;
      }
    } while(running);
   System.out.println("Server stopped successfully");
  }

  public void messageReceived(JPlaylistEditorNetworkMessage networkMsg) {
    // if disconnected string comes through then kill the thread
    //CPlayListEditorNetworkMessage networkMsg = (JPlaylistEditorNetworkMessage)msg;
    System.out.println("Server message received: " + networkMsg.getNetworkMessageType().name());

    // case of disconnect message
    if (networkMsg.isDisconnectNotice() == true) {
      sct.stopRunning();
      return;
    }
    
    switch (networkMsg.getNetworkMessageType()) {
        case PLAYLIST_MESSAGE:
            gui.playPlaylist(networkMsg.getPlaylist());
            break;
            
        case GET_LIBRARY_MESSAGE:
            networkMsg.setLibraryRoots(options.getLibraryRoots());
            sct.sendData(networkMsg);
            break;
        
        case NEXT_SONG_MESSAGE:
            gui.controlCplay(JPlayControl.JPlayAction.NEXT_SONG);
            break;
        
        case PREVIOUS_SONG_MESSAGE:
            gui.controlCplay(JPlayControl.JPlayAction.PREVIOUS_SONG);
            break;

        case PAUSE_SONG_MESSAGE:
            gui.controlCplay(JPlayControl.JPlayAction.PAUSE_SONG);
            break;
            
        case FAST_FORWARD_MESSAGE:
            gui.controlCplay(JPlayControl.JPlayAction.FAST_FORWARD);
            break;

        case REWIND_MESSAGE:
            gui.controlCplay(JPlayControl.JPlayAction.REWIND);
            break;

        case PHASE_MESSAGE:
            gui.controlCplay(JPlayControl.JPlayAction.CHANGE_PHASE);
            break;
            
        case VOLUME_UP_MESSAGE:
            gui.controlCplay(JPlayControl.JPlayAction.VOLUME_UP);
            break;
            
        case VOLUME_DOWN_MESSAGE:
            gui.controlCplay(JPlayControl.JPlayAction.VOLUME_DOWN);
            break;
    }   
}


  public void startServer() {
    this.run();
  }

  public void stopServer() {
    if (running == true) {
      running = false;
      try {
        ss.close();
      }
      catch (IOException e) {
        System.out.println("Error closing server socket");
      }
      finally {
        ss = null;
      }
      gui.setNetworkStatus(JPlaylistEditorNetworkMessage.ConnectionStatus.FAILURE,
                    JPlaylistEditorNetworkMessage.NetworkApplicationType.SERVER,
                    "Server Stopped");
    }
  }


  /* Private inner class to encapsulate an individual Client Connection*/
  class ServerConnectionThread extends Thread {
    private Socket socket;            // socket for the client connection
    private ObjectInputStream in;
    private ObjectOutputStream out;     // for outgoing messages
    private boolean running=true;     // used to shutdown the client thread

    public ServerConnectionThread(Socket socket) {
      try {
        this.socket=socket;
      }
      catch(Exception e) {
        e.printStackTrace();
      }

      // set up input and output streams and start
      // running the client connection thread
      try {
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
      }
      catch(Exception e) {
        e.printStackTrace();
      }
      this.start();
    }

    @Override public void run() {
      // Listen for messages that come in from the
      // client via the GUIChatServer object
      while(running) {
        try {
            JPlaylistEditorNetworkMessage doSomething = (JPlaylistEditorNetworkMessage)in.readObject();
            messageReceived(doSomething);
        }
        catch(Exception e)
        {
          e.printStackTrace();
          running=false;
        }
      }
      // if running is false then shut down io streams and the socket
      try {
          out.close();
          in.close();
      } catch (IOException e) {
      }
      finally {
        out=null;
        in=null;
        socket=null;
      }
    }

    // send data to individual client connection
    public void sendData(JPlaylistEditorNetworkMessage msg) {
      try {
        out.writeObject(msg);
        out.flush();
      } catch(Exception e) {
        e.printStackTrace();
      }
    }

    //used to shut down the thread
    public void stopRunning() {
      running=false;
    }
  }  // end of ServerConnectionThread inner class


} 