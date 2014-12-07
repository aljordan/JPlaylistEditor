package jplaylisteditor;

/**
 *
 * @author Alan Jordan
 */
import java.util.Vector;
import java.io.*;

public class JPlaylistOptions implements java.io.Serializable {
    private String playlistFolderPath;
    private File playlistFolder;
    private File libraryRoot; //Root folder of music library
    private String libraryRootPath; //String representation of above var because can't always serialize File object
    private Vector<LibraryRoot> libraryRoots;
    private boolean parseTagsFromSingleFolder; //Parse artist and album from single parent folder instead of two parent folders.
    private String parseSingleFolderSeperator; //String to use when parsing tags from single parent folder
    private boolean useEmbeddedTags; //Read embedded tags from FLAC and MP3 files
    private boolean artistInTrackTitle; //Various artist - performer - title overrides
    private boolean parseTitle; //Remove track number and file extension from track names   
    private String foobarExecutablePath;
    private boolean enableFoobar;
    private int selectedLibrary;
    private boolean showSamplingRate;
    private boolean showFileType;
    private String serverIpAddress;
    private boolean runAsServer;
    private boolean enableDynamicUpsampling;


    public JPlaylistOptions () {
        initOptions();
    }


    public enum PLAYLIST_FORMAT {
        CUE_SHEET, M3U_EXTENDED
    }

    public void initOptions() {
        try {
            FileInputStream f_in = new FileInputStream("JPlaylistOptions.data");
            ObjectInputStream obj_in = new ObjectInputStream (f_in);
            Object obj = obj_in.readObject();

            if (obj instanceof JPlaylistOptions) {
                JPlaylistOptions tempOptions = (JPlaylistOptions)obj;
                this.parseTagsFromSingleFolder = tempOptions.isParseTagsFromSingleFolder();
                this.parseSingleFolderSeperator = tempOptions.getParseSingleFolderSeperator();
                this.useEmbeddedTags = tempOptions.isUseEmbeddedTags();
                this.artistInTrackTitle = tempOptions.isArtistInTrackTitle();
                this.parseTitle = tempOptions.isParseTitle();
                this.enableFoobar = tempOptions.isEnableFoobar();
                this.foobarExecutablePath = tempOptions.getFoobarExecutablePath();
                this.selectedLibrary = tempOptions.getSelectedLibrary();
                this.showFileType = tempOptions.isShowFileType();
                this.showSamplingRate = tempOptions.isShowSamplingRate();
                this.runAsServer = tempOptions.isRunAsServer();
                this.enableDynamicUpsampling = tempOptions.isEnableDynamicUpsampling();

                if (tempOptions.getPlaylistFolderPath() != null) {
                    this.playlistFolder = new File(tempOptions.getPlaylistFolderPath());
                    this.playlistFolderPath = playlistFolder.getPath();
                }
                else {
                    this.playlistFolder = null;
                    this.playlistFolderPath = null;
                }
                
                if (tempOptions.getLibraryRootPath() != null) {
                    this.libraryRoot = new File(tempOptions.getLibraryRootPath()); 
                    this.libraryRootPath = libraryRoot.getPath();
                }
                else {
                    this.libraryRoot = null;
                    this.libraryRootPath = null;
                }

                if (tempOptions.getLibraryRoots() != null) {
                    this.setLibraryRoots(tempOptions.getLibraryRoots());
                }
                else {
                    this.setLibraryRoots(new Vector<LibraryRoot>());
                }

                this.serverIpAddress = tempOptions.getServerIpAddress();

            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            playlistFolder = null;
            playlistFolderPath = null;
            libraryRoot = null;
            libraryRootPath = null;
            useEmbeddedTags = false;
            parseTagsFromSingleFolder = false;
            artistInTrackTitle = false;
            parseTitle = false;
            foobarExecutablePath = null;
            enableFoobar = false;
            setLibraryRoots(new Vector<LibraryRoot>());
            enableDynamicUpsampling = false;
        }
        
    }

    
    public void saveOptions() {
        //set objects in a manner that will be serializable.  File object at root of drive
        // can't be serialized and unserialized.

        // rely on string instead of File object
        try {
            playlistFolderPath = playlistFolder.getPath();
            playlistFolder = null;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            playlistFolder = null;
            playlistFolderPath = null;
        }

        try {
            libraryRootPath = libraryRoot.getPath();
            libraryRoot = null;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            libraryRootPath = null;
            libraryRoot = null;
        }
        
        try {
            FileOutputStream f_out = new FileOutputStream("JPlaylistOptions.data");
            ObjectOutputStream obj_out = new ObjectOutputStream (f_out);
            obj_out.writeObject(this);
        }
        catch (FileNotFoundException fe) {
            System.out.println(fe.getMessage());
            System.out.println(fe.getStackTrace());
        }
        catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            System.out.println(ioe.getStackTrace());
        }
    }
    
    /**
     * @return the playlistDirectoryFolderPath
     */
    public String getPlaylistFolderPath() {
        return playlistFolderPath;
    }

    /**
     * @param playlistDirectoryFolderPath the playlistDirectoryFolderPath to set
     */
    public void setPlaylistFolderPath(String playlistDirectoryFolderPath) {
        this.playlistFolderPath = playlistDirectoryFolderPath;
    }

    /**
     * @return the playlistFolder
     */
    public File getPlaylistFolder() {
        return playlistFolder;
    }

    /**
     * @param playlistFolder the playlistFolder to set
     */
    public void setPlaylistFolder(File playlistFolder) {
        this.playlistFolder = playlistFolder;
    }

    /**
     * @return the libraryRoot
     */
    public File getLibraryRoot() {
        return libraryRoot;
    }

    public Vector<LibraryRoot> getLibraryRoots() {
        return libraryRoots;
    }

    /**
     * @param libraryRoot the libraryRoot to set
     */
    public void setLibraryRoot(File libraryRoot) {
        this.libraryRoot = libraryRoot;
        this.libraryRootPath = libraryRoot.getPath();
    }

        /**
     * @param libraryRoot the libraryRoot to set
     */
    public void addLibraryRoot(LibraryRoot libraryRoot) {
        this.libraryRoots.add(libraryRoot);
    }

    public void deleteLibraryRoot(int index) {
        libraryRoots.removeElementAt(index);
        libraryRoots.trimToSize();
    }

    public String getLibraryRootPath() {
        return libraryRootPath;
    }

    /**
     * @return the parseTagsFromSingleFolder
     */
    public boolean isParseTagsFromSingleFolder() {
        return parseTagsFromSingleFolder;
    }

    /**
     * @param parseTagsFromSingleFolder the parseTagsFromSingleFolder to set
     */
    public void setParseTagsFromSingleFolder(boolean parseTagsFromSingleFolder) {
        this.parseTagsFromSingleFolder = parseTagsFromSingleFolder;
    }

    /**
     * @return the parseSingleFolderSeperator
     */
    public String getParseSingleFolderSeperator() {
        return parseSingleFolderSeperator;
    }

    /**
     * @param parseSingleFolderSeperator the parseSingleFolderSeperator to set
     */
    public void setParseSingleFolderSeperator(String parseSingleFolderSeperator) {
        this.parseSingleFolderSeperator = parseSingleFolderSeperator;
    }
    
    /**
     * @return the useEmbeddedTags
     */
    public boolean isUseEmbeddedTags() {
        return useEmbeddedTags;
    }

    /**
     * @param useEmbeddedTags the useEmbeddedTags to set
     */
    public void setUseEmbeddedTags(boolean useEmbeddedTags) {
        this.useEmbeddedTags = useEmbeddedTags;
    }

     /**
     * @return the artistInTrackTitle
     */
    public boolean isArtistInTrackTitle() {
        return artistInTrackTitle;
    }

    /**
     * @param artistInTrackTitle the artistInTrackTitle to set
     */
    public void setArtistInTrackTitle(boolean compilationOptimization) {
        this.artistInTrackTitle = compilationOptimization;
    }

    /**
     * @return the parseTitle
     */
    public boolean isParseTitle() {
        return parseTitle;
    }

    /**
     * @param parseTitle the parseTitle to set
     */
    public void setParseTitle(boolean parseTitle) {
        this.parseTitle = parseTitle;
    }


    public String getFoobarExecutablePath() {
        return foobarExecutablePath;
    }

    public void setFoobarExecutablePath(String foobarExecutablePath) {
        this.foobarExecutablePath = foobarExecutablePath;
    }

    public boolean isEnableFoobar() {
        return enableFoobar;
    }

    public void setEnableFoobar(boolean enableFoobar) {
        this.enableFoobar = enableFoobar;
    }

    /**
     * @return the selectedLibrary
     */
    public int getSelectedLibrary() {
        return selectedLibrary;
    }

    /**
     * @param selectedLibrary the selectedLibrary to set
     */
    public void setSelectedLibrary(int selectedLibrary) {
        this.selectedLibrary = selectedLibrary;
    }

    /**
     * @return the showSamplingRate
     */
    public boolean isShowSamplingRate() {
        return showSamplingRate;
    }

    /**
     * @param showSamplingRate the showSamplingRate to set
     */
    public void setShowSamplingRate(boolean showSamplingRate) {
        this.showSamplingRate = showSamplingRate;
    }

    /**
     * @return the showFileType
     */
    public boolean isShowFileType() {
        return showFileType;
    }

    /**
     * @param showFileType the showFileType to set
     */
    public void setShowFileType(boolean showFileType) {
        this.showFileType = showFileType;
    }

    /**
     * @return the serverIpAddress
     */
    public String getServerIpAddress() {
        return serverIpAddress;
    }

    /**
     * @param serverIpAddress the serverIpAddress to set
     */
    public void setServerIpAddress(String serverIpAddress) {
        this.serverIpAddress = serverIpAddress;
    }

    public void setLibraryRoots(Vector<LibraryRoot> libraryRoots) {
        this.libraryRoots = libraryRoots;
    }

    public boolean isRunAsServer() {
        return runAsServer;
    }

    public void setRunAsServer(boolean runAsServer) {
        this.runAsServer = runAsServer;
    }


    /**
     * @return the enableDynamicUpsampling
     */
    public boolean isEnableDynamicUpsampling() {
        return enableDynamicUpsampling;
    }

    /**
     * @param enableDynamicUpsampling the enableDynamicUpsampling to set
     */
    public void setEnableDynamicUpsampling(boolean enableDynamicUpsampling) {
        this.enableDynamicUpsampling = enableDynamicUpsampling;
    }

}
