/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JPlaylistGUI.java
 *
 * Created on Apr 6, 2009, 9:28:23 AM
 */

// TODO:  Allow sampling rate changes if Client is enabled and active and Server
// allows sampling rate changes
package jplaylisteditor;

import javax.swing.*;
import java.io.*;
import java.util.Vector;
import java.awt.*;
import java.awt.event.*;
import jplaylisteditor.JPlaylistOptions.PLAYLIST_FORMAT.*;


/**
 *
 * @author Alan Jordan
 */
public class JPlaylistGUI extends javax.swing.JFrame {

    private File libraryRoot;
    private File playlistFolder;
    private JFileChooser fcLibraryRoot = new JFileChooser();
    private JFileChooser fcPlaylistFolder = new JFileChooser();
    private JFileChooser fcSavePlaylist;
    private JFileChooser fcOpenPlaylist;
    private JPlaylistOptions options;
    private MusicLibrary library;
    private Vector<Album> currentAlbumList;
    private Vector<String> currentGenreList;
    private Album currentAlbum;
    private Playlist playlist;
    private Process cplay = null;
    private Robot cplayRobot = null;
    private JPlaylistServerListener serverListener;
    private Thread serverThread;
    private JPlaylistClientListener clientListener;
    private Thread clientThread;
    private boolean serverAllowsClientSamplingRateChanges;

    /** Creates new form JPlaylistGUI */
    public JPlaylistGUI() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(JPlaylistGUI.class.getResource("music_green.png")));
        playlist = new Playlist();
        playlist.setArtist("Various Artists");
        initComponents();
        initCustomComponents();
        options = new JPlaylistOptions();
        library = null;
        initializeOptions();
    }

    public void setLibrary(MusicLibrary library) {
        this.library = library;
    }


    public void setNetworkStatus(JPlaylistEditorNetworkMessage.ConnectionStatus status,
            JPlaylistEditorNetworkMessage.NetworkApplicationType type, String strStatus) {
        lblNetworkStatus.setText("Network Status: " + strStatus);
        if (status == JPlaylistEditorNetworkMessage.ConnectionStatus.FAILURE) {
            if (type == JPlaylistEditorNetworkMessage.NetworkApplicationType.CLIENT) {
                mnuChkClient.setSelected(false);
            }
            else {
                mnuChkServer.setSelected(false);
            }
        }
    }

    public void setLibraryAvailable() {
        enablePanel(pnlLetters, true);
        lblLibraryRead.setText("Library available");
        fillGenreList();
        enablePanel(pnlGenres,true);
        //btnA.doClick();
        btnAll.doClick();
    }


    public void setStatus(String status) {
        lblStatus.setText("Status: " + status);
    }

    private void fillGenreList() {
        currentGenreList = library.getGenres();
        lstGenres = new JList(currentGenreList);
        lstGenres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        lstGenres.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstGenresValueChanged(evt);
            }
        });

        scrollPaneGenres.setViewportView(lstGenres);

    }

    private void initializeOptions() {

        mnuChkUseEmbeddedTags.setSelected(options.isUseEmbeddedTags());

        if (mnuChkUseEmbeddedTags.isSelected()) {
            mnuChkArtistInTrackTitle.setEnabled(true);
            mnuChkArtistInTrackTitle.setSelected(options.isArtistInTrackTitle());
            mnuChkShowSamplingRate.setEnabled(true);
            mnuChkShowSamplingRate.setSelected(options.isShowSamplingRate());
        } else {
            mnuChkArtistInTrackTitle.setSelected(false);
            mnuChkArtistInTrackTitle.setEnabled(false);
            mnuChkShowSamplingRate.setSelected(false);
            mnuChkShowSamplingRate.setEnabled(false);
        }

        mnuChkParseTitle.setSelected(options.isParseTitle());
        mnuChkParseTagsFromSingleFolder.setSelected(options.isParseTagsFromSingleFolder());

        mnuChkEnableFoobar.setSelected(options.isEnableFoobar());
        
        mnuChkShowFileType.setSelected(options.isShowFileType());
        
        if (options.getPlaylistFolder() != null) {
            playlistFolder = options.getPlaylistFolder();
            lblPlaylistFolder.setText("Playlist Directory: " + playlistFolder.getPath());
            playlist.setDirectoryOnDisc(playlistFolder);
            btnOpen.setEnabled(true);
        }
        if (options.getLibraryRoot() != null) {
            libraryRoot = options.getLibraryRoot();
            lblLibraryRoot.setText("Library Root Folder: " + libraryRoot.getPath());
            btnReadLibrary.setEnabled(true);
        }

        if (options.getLibraryRoots() != null) {
            lstLibraryRoots = new JList(options.getLibraryRoots());
            lstLibraryRoots.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            lstLibraryRoots.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
                public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                    lstLibraryRootsValueChanged(evt);
                }
            });
            scrollPaneLibraryRoots.setViewportView(lstLibraryRoots);

            //try to find the current selected library
//            for (int count = 0; count < options.getLibraryRoots().size(); count++ ) {
//                if (options.getLibraryRoot().getPath().equalsIgnoreCase(options.getLibraryRoots().elementAt(count).getLibraryRootPath())) {
//                    lstLibraryRoots.setSelectedIndex(count);
//                    break;
//                }
//            }

            try {
                lstLibraryRoots.setSelectedIndex(options.getSelectedLibrary());
                library = options.getLibraryRoots().elementAt(options.getSelectedLibrary()).getLibrary();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        
//        mnuChkServer.setSelected(options.isRunAsServer());
        if (options.isRunAsServer()) {
            mnuChkServer.doClick();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGrpSelectLetter = new javax.swing.ButtonGroup();
        btnGrpParentFolders = new javax.swing.ButtonGroup();
        btnGroupSamplingRate = new javax.swing.ButtonGroup();
        btnGrpUpsamplingRule = new javax.swing.ButtonGroup();
        btnGrpPlaylistFormat = new javax.swing.ButtonGroup();
        tabbedPaneMain = new javax.swing.JTabbedPane();
        jPanelSettings = new javax.swing.JPanel();
        btnSetPlaylistDirectory = new javax.swing.JButton();
        lblPlaylistFolder = new javax.swing.JLabel();
        btnReadLibrary = new javax.swing.JButton();
        lblLibraryRead = new javax.swing.JLabel();
        lblNetworkStatus = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblLibraryRoot = new javax.swing.JLabel();
        scrollPaneLibraryRoots = new javax.swing.JScrollPane();
        lstLibraryRoots = new javax.swing.JList();
        btnAddLibrary = new javax.swing.JButton();
        btnRemoveLibrary = new javax.swing.JButton();
        lblLibrary = new javax.swing.JLabel();
        jPanelCplayControl = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnPause = new javax.swing.JButton();
        btnRewind = new javax.swing.JButton();
        btnFastforward = new javax.swing.JButton();
        btnVolumeDown = new javax.swing.JButton();
        btnVolumeUp = new javax.swing.JButton();
        btnPhase = new javax.swing.JButton();
        lblCplayControls = new javax.swing.JLabel();
        jPanelPlaylistEditor = new javax.swing.JPanel();
        pnlHolder = new javax.swing.JPanel();
        pnlLetters = new javax.swing.JPanel();
        btnA = new javax.swing.JRadioButton();
        btnB = new javax.swing.JRadioButton();
        brnC = new javax.swing.JRadioButton();
        btnD = new javax.swing.JRadioButton();
        btnE = new javax.swing.JRadioButton();
        btnF = new javax.swing.JRadioButton();
        btnG = new javax.swing.JRadioButton();
        btnH = new javax.swing.JRadioButton();
        btnI = new javax.swing.JRadioButton();
        btnJ = new javax.swing.JRadioButton();
        btnK = new javax.swing.JRadioButton();
        btnL = new javax.swing.JRadioButton();
        btnM = new javax.swing.JRadioButton();
        btnN = new javax.swing.JRadioButton();
        btnO = new javax.swing.JRadioButton();
        btnP = new javax.swing.JRadioButton();
        btnQ = new javax.swing.JRadioButton();
        btnR = new javax.swing.JRadioButton();
        btnS = new javax.swing.JRadioButton();
        btnT = new javax.swing.JRadioButton();
        btnU = new javax.swing.JRadioButton();
        btnV = new javax.swing.JRadioButton();
        btnW = new javax.swing.JRadioButton();
        btnX = new javax.swing.JRadioButton();
        btnY = new javax.swing.JRadioButton();
        btnZ = new javax.swing.JRadioButton();
        btnAll = new javax.swing.JRadioButton();
        lblSelectArtists = new javax.swing.JLabel();
        lblAlbums = new javax.swing.JLabel();
        lblTracks = new javax.swing.JLabel();
        scrollPaneAlbums = new javax.swing.JScrollPane();
        lstAlbums = new javax.swing.JList();
        scrollPaneTracks = new javax.swing.JScrollPane();
        lstTracks = new javax.swing.JList();
        scrollPanePlaylist = new javax.swing.JScrollPane();
        lstPlaylist = new javax.swing.JList();
        lblPlaylist = new javax.swing.JLabel();
        btnAddTrack = new javax.swing.JButton();
        btnRemoveTrack = new javax.swing.JButton();
        btnUp = new javax.swing.JButton();
        btnDown = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnOpen = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnPlay = new javax.swing.JButton();
        pnlGenres = new javax.swing.JPanel();
        scrollPaneGenres = new javax.swing.JScrollPane();
        lstGenres = new javax.swing.JList();
        lblGenre = new javax.swing.JLabel();
        mnuMain = new javax.swing.JMenuBar();
        mnuSettings = new javax.swing.JMenu();
        mnuChkParseTitle = new javax.swing.JCheckBoxMenuItem();
        mnuChkParseTagsFromSingleFolder = new javax.swing.JCheckBoxMenuItem();
        mnuChkShowFileType = new javax.swing.JCheckBoxMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        mnuChkEnableFoobar = new javax.swing.JCheckBoxMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        mnuChkUseEmbeddedTags = new javax.swing.JCheckBoxMenuItem();
        mnuChkArtistInTrackTitle = new javax.swing.JCheckBoxMenuItem();
        mnuChkShowSamplingRate = new javax.swing.JCheckBoxMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        mnuChkServer = new javax.swing.JCheckBoxMenuItem();
        mnuChkClient = new javax.swing.JCheckBoxMenuItem();
        mnuHelp = new javax.swing.JMenu();
        mnuCPLEHelp = new javax.swing.JMenuItem();
        mnuHelpAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JPlay Mini Playlist Editor");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        btnSetPlaylistDirectory.setText("Set Playlist Directory");
        btnSetPlaylistDirectory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetPlaylistDirectoryActionPerformed(evt);
            }
        });

        lblPlaylistFolder.setText("Playlist Directory: Not set");

        btnReadLibrary.setText("Refresh Library");
        btnReadLibrary.setEnabled(false);
        btnReadLibrary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadLibraryActionPerformed(evt);
            }
        });

        lblLibraryRead.setText("Library not yet processed");

        lblNetworkStatus.setText("Network Status: ");

        lblStatus.setText("Status:");

        lblLibraryRoot.setText("Library Root Folder: Not set");

        scrollPaneLibraryRoots.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        lstLibraryRoots.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstLibraryRootsValueChanged(evt);
            }
        });
        scrollPaneLibraryRoots.setViewportView(lstLibraryRoots);

        btnAddLibrary.setText("Add");
        btnAddLibrary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddLibraryActionPerformed(evt);
            }
        });

        btnRemoveLibrary.setText("Remove");
        btnRemoveLibrary.setEnabled(false);
        btnRemoveLibrary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveLibraryActionPerformed(evt);
            }
        });

        lblLibrary.setText("Libraries");

        javax.swing.GroupLayout jPanelSettingsLayout = new javax.swing.GroupLayout(jPanelSettings);
        jPanelSettings.setLayout(jPanelSettingsLayout);
        jPanelSettingsLayout.setHorizontalGroup(
            jPanelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelSettingsLayout.createSequentialGroup()
                        .addGroup(jPanelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnReadLibrary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSetPlaylistDirectory))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPlaylistFolder)
                            .addComponent(lblLibraryRead)))
                    .addComponent(lblNetworkStatus)
                    .addComponent(lblStatus)
                    .addComponent(lblLibraryRoot)
                    .addGroup(jPanelSettingsLayout.createSequentialGroup()
                        .addComponent(scrollPaneLibraryRoots, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnAddLibrary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnRemoveLibrary)))
                    .addComponent(lblLibrary))
                .addContainerGap(524, Short.MAX_VALUE))
        );
        jPanelSettingsLayout.setVerticalGroup(
            jPanelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSetPlaylistDirectory)
                    .addComponent(lblPlaylistFolder))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReadLibrary)
                    .addComponent(lblLibraryRead))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblLibrary)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSettingsLayout.createSequentialGroup()
                        .addComponent(btnAddLibrary)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRemoveLibrary))
                    .addComponent(scrollPaneLibraryRoots, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblLibraryRoot)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNetworkStatus)
                .addContainerGap(248, Short.MAX_VALUE))
        );

        tabbedPaneMain.addTab("Manage Library", jPanelSettings);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnPrevious.setText("<|");
        btnPrevious.setToolTipText("previous song");
        btnPrevious.setEnabled(false);
        btnPrevious.setMaximumSize(new java.awt.Dimension(53, 23));
        btnPrevious.setMinimumSize(new java.awt.Dimension(53, 23));
        btnPrevious.setPreferredSize(new java.awt.Dimension(53, 23));
        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });

        btnNext.setText("|>");
        btnNext.setToolTipText("next song");
        btnNext.setEnabled(false);
        btnNext.setMaximumSize(new java.awt.Dimension(53, 23));
        btnNext.setMinimumSize(new java.awt.Dimension(53, 23));
        btnNext.setPreferredSize(new java.awt.Dimension(53, 23));
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnPause.setText("Pause");
        btnPause.setToolTipText("pause or play");
        btnPause.setEnabled(false);
        btnPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPauseActionPerformed(evt);
            }
        });

        btnRewind.setText("<<");
        btnRewind.setToolTipText("rewind in song");
        btnRewind.setEnabled(false);
        btnRewind.setMaximumSize(new java.awt.Dimension(53, 23));
        btnRewind.setMinimumSize(new java.awt.Dimension(53, 23));
        btnRewind.setPreferredSize(new java.awt.Dimension(53, 23));
        btnRewind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRewindActionPerformed(evt);
            }
        });

        btnFastforward.setText(">>");
        btnFastforward.setToolTipText("fast forward in song");
        btnFastforward.setEnabled(false);
        btnFastforward.setMaximumSize(new java.awt.Dimension(53, 23));
        btnFastforward.setMinimumSize(new java.awt.Dimension(53, 23));
        btnFastforward.setPreferredSize(new java.awt.Dimension(53, 23));
        btnFastforward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFastforwardActionPerformed(evt);
            }
        });

        btnVolumeDown.setText("Dn");
        btnVolumeDown.setToolTipText("turn volume down");
        btnVolumeDown.setEnabled(false);
        btnVolumeDown.setMaximumSize(new java.awt.Dimension(53, 23));
        btnVolumeDown.setMinimumSize(new java.awt.Dimension(53, 23));
        btnVolumeDown.setPreferredSize(new java.awt.Dimension(53, 23));
        btnVolumeDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolumeDownActionPerformed(evt);
            }
        });

        btnVolumeUp.setText("Up");
        btnVolumeUp.setToolTipText("turn volume up");
        btnVolumeUp.setEnabled(false);
        btnVolumeUp.setMaximumSize(new java.awt.Dimension(53, 23));
        btnVolumeUp.setMinimumSize(new java.awt.Dimension(53, 23));
        btnVolumeUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolumeUpActionPerformed(evt);
            }
        });

        btnPhase.setText("Phase");
        btnPhase.setToolTipText("change phase");
        btnPhase.setEnabled(false);
        btnPhase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPhaseActionPerformed(evt);
            }
        });

        lblCplayControls.setText("JPlay Control");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnPause, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnVolumeDown, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnVolumeUp, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnPhase, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnRewind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnPrevious, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnFastforward, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnNext, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(lblCplayControls)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCplayControls)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRewind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFastforward, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPause)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVolumeDown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnVolumeUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPhase)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanelCplayControlLayout = new javax.swing.GroupLayout(jPanelCplayControl);
        jPanelCplayControl.setLayout(jPanelCplayControlLayout);
        jPanelCplayControlLayout.setHorizontalGroup(
            jPanelCplayControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCplayControlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(648, Short.MAX_VALUE))
        );
        jPanelCplayControlLayout.setVerticalGroup(
            jPanelCplayControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCplayControlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(253, Short.MAX_VALUE))
        );

        tabbedPaneMain.addTab("JPlay Controls", jPanelCplayControl);

        pnlHolder.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        pnlLetters.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnGrpSelectLetter.add(btnA);
        btnA.setText("A");
        btnA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(btnB);
        btnB.setText("B");
        btnB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(brnC);
        brnC.setText("C");
        brnC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brnCActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(btnD);
        btnD.setText("D");
        btnD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(btnE);
        btnE.setText("E");
        btnE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(btnF);
        btnF.setText("F");
        btnF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(btnG);
        btnG.setText("G");
        btnG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(btnH);
        btnH.setText("H");
        btnH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(btnI);
        btnI.setText("I");
        btnI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(btnJ);
        btnJ.setText("J");
        btnJ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(btnK);
        btnK.setText("K");
        btnK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(btnL);
        btnL.setText("L");
        btnL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(btnM);
        btnM.setText("M");
        btnM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(btnN);
        btnN.setText("N");
        btnN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(btnO);
        btnO.setText("O");
        btnO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(btnP);
        btnP.setText("P");
        btnP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(btnQ);
        btnQ.setText("Q");
        btnQ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(btnR);
        btnR.setText("R");
        btnR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(btnS);
        btnS.setText("S");
        btnS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(btnT);
        btnT.setText("T");
        btnT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(btnU);
        btnU.setText("U");
        btnU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(btnV);
        btnV.setText("V");
        btnV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(btnW);
        btnW.setText("W");
        btnW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(btnX);
        btnX.setText("X");
        btnX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(btnY);
        btnY.setText("Y");
        btnY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnYActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(btnZ);
        btnZ.setText("Z");
        btnZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZActionPerformed(evt);
            }
        });

        btnGrpSelectLetter.add(btnAll);
        btnAll.setText("All");
        btnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAllActionPerformed(evt);
            }
        });

        lblSelectArtists.setText("Select Artist / Allbum");

        javax.swing.GroupLayout pnlLettersLayout = new javax.swing.GroupLayout(pnlLetters);
        pnlLetters.setLayout(pnlLettersLayout);
        pnlLettersLayout.setHorizontalGroup(
            pnlLettersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLettersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlLettersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSelectArtists)
                    .addComponent(btnAll))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlLettersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLettersLayout.createSequentialGroup()
                        .addComponent(btnA)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnB))
                    .addGroup(pnlLettersLayout.createSequentialGroup()
                        .addComponent(btnH)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnI))
                    .addGroup(pnlLettersLayout.createSequentialGroup()
                        .addGroup(pnlLettersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlLettersLayout.createSequentialGroup()
                                .addComponent(btnO)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnP))
                            .addGroup(pnlLettersLayout.createSequentialGroup()
                                .addComponent(btnV)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnW)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlLettersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlLettersLayout.createSequentialGroup()
                                .addComponent(btnJ)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnK)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnL)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnM)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnN)
                                .addGap(4, 4, 4))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlLettersLayout.createSequentialGroup()
                                .addComponent(brnC)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnD)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnF)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnG))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlLettersLayout.createSequentialGroup()
                                .addComponent(btnQ)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnR)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnS)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnT)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnU))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlLettersLayout.createSequentialGroup()
                                .addComponent(btnX)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnY)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnZ)))
                        .addGap(29, 29, 29)))
                .addContainerGap())
        );
        pnlLettersLayout.setVerticalGroup(
            pnlLettersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLettersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlLettersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlLettersLayout.createSequentialGroup()
                        .addComponent(brnC)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlLettersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnJ)
                            .addComponent(btnK, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlLettersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnQ, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnR)
                            .addComponent(btnS)
                            .addComponent(btnT)
                            .addComponent(btnU)))
                    .addGroup(pnlLettersLayout.createSequentialGroup()
                        .addGroup(pnlLettersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnA)
                            .addComponent(btnB)
                            .addComponent(lblSelectArtists)
                            .addComponent(btnD)
                            .addComponent(btnE)
                            .addComponent(btnF)
                            .addComponent(btnG))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlLettersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAll)
                            .addComponent(btnH)
                            .addComponent(btnI)
                            .addComponent(btnL)
                            .addComponent(btnM)
                            .addComponent(btnN))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlLettersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnO)
                            .addComponent(btnP))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlLettersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnV)
                    .addComponent(btnX)
                    .addComponent(btnW)
                    .addComponent(btnY)
                    .addComponent(btnZ))
                .addContainerGap())
        );

        lblAlbums.setText("Albums");

        lblTracks.setText("Tracks");

        scrollPaneAlbums.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPaneAlbums.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        lstAlbums.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstAlbums.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstAlbumsValueChanged(evt);
            }
        });
        scrollPaneAlbums.setViewportView(lstAlbums);

        scrollPaneTracks.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPaneTracks.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        lstTracks.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstTracksMouseClicked(evt);
            }
        });
        lstTracks.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstTracksValueChanged(evt);
            }
        });
        scrollPaneTracks.setViewportView(lstTracks);

        scrollPanePlaylist.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPanePlaylist.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        lstPlaylist.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstPlaylistValueChanged(evt);
            }
        });
        scrollPanePlaylist.setViewportView(lstPlaylist);

        lblPlaylist.setText("Playlist");

        btnAddTrack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jplaylisteditor/right-arrow17.gif"))); // NOI18N
        btnAddTrack.setEnabled(false);
        btnAddTrack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddTrackActionPerformed(evt);
            }
        });

        btnRemoveTrack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jplaylisteditor/larrow17.gif"))); // NOI18N
        btnRemoveTrack.setEnabled(false);
        btnRemoveTrack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveTrackActionPerformed(evt);
            }
        });

        btnUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jplaylisteditor/small-up17.gif"))); // NOI18N
        btnUp.setEnabled(false);
        btnUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpActionPerformed(evt);
            }
        });

        btnDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jplaylisteditor/small-down17.gif"))); // NOI18N
        btnDown.setEnabled(false);
        btnDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownActionPerformed(evt);
            }
        });

        btnSave.setText("Save");
        btnSave.setEnabled(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnOpen.setText("Open");
        btnOpen.setEnabled(false);
        btnOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenActionPerformed(evt);
            }
        });

        btnClear.setText("Clear");
        btnClear.setEnabled(false);
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnPlay.setText("Play");
        btnPlay.setToolTipText("send playlist to jplay");
        btnPlay.setEnabled(false);
        btnPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayActionPerformed(evt);
            }
        });

        pnlGenres.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lstGenres.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstGenres.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstGenresValueChanged(evt);
            }
        });
        scrollPaneGenres.setViewportView(lstGenres);

        lblGenre.setText("Genre");

        javax.swing.GroupLayout pnlGenresLayout = new javax.swing.GroupLayout(pnlGenres);
        pnlGenres.setLayout(pnlGenresLayout);
        pnlGenresLayout.setHorizontalGroup(
            pnlGenresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGenresLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblGenre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(scrollPaneGenres, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlGenresLayout.setVerticalGroup(
            pnlGenresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGenresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlGenresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblGenre)
                    .addComponent(scrollPaneGenres, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlHolderLayout = new javax.swing.GroupLayout(pnlHolder);
        pnlHolder.setLayout(pnlHolderLayout);
        pnlHolderLayout.setHorizontalGroup(
            pnlHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHolderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAlbums)
                    .addGroup(pnlHolderLayout.createSequentialGroup()
                        .addComponent(scrollPaneAlbums, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(pnlHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTracks)
                            .addComponent(scrollPaneTracks, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAddTrack)
                            .addComponent(btnRemoveTrack))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPlaylist)
                            .addGroup(pnlHolderLayout.createSequentialGroup()
                                .addComponent(scrollPanePlaylist, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                                .addGap(6, 6, 6)
                                .addGroup(pnlHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnOpen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnDown, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnUp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnPlay)))))
                    .addGroup(pnlHolderLayout.createSequentialGroup()
                        .addComponent(pnlLetters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(pnlGenres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlHolderLayout.setVerticalGroup(
            pnlHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHolderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlGenres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlLetters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlHolderLayout.createSequentialGroup()
                        .addComponent(lblTracks)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollPaneTracks))
                    .addGroup(pnlHolderLayout.createSequentialGroup()
                        .addComponent(lblAlbums)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollPaneAlbums, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE))
                    .addGroup(pnlHolderLayout.createSequentialGroup()
                        .addComponent(lblPlaylist)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnPlay)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHolderLayout.createSequentialGroup()
                                    .addGap(83, 83, 83)
                                    .addComponent(btnClear)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnUp)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnDown)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnOpen)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnSave)))
                            .addComponent(scrollPanePlaylist)))
                    .addGroup(pnlHolderLayout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addComponent(btnAddTrack)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemoveTrack)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanelPlaylistEditorLayout = new javax.swing.GroupLayout(jPanelPlaylistEditor);
        jPanelPlaylistEditor.setLayout(jPanelPlaylistEditorLayout);
        jPanelPlaylistEditorLayout.setHorizontalGroup(
            jPanelPlaylistEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 794, Short.MAX_VALUE)
            .addGroup(jPanelPlaylistEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelPlaylistEditorLayout.createSequentialGroup()
                    .addGap(14, 14, 14)
                    .addComponent(pnlHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(14, 14, 14)))
        );
        jPanelPlaylistEditorLayout.setVerticalGroup(
            jPanelPlaylistEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 457, Short.MAX_VALUE)
            .addGroup(jPanelPlaylistEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelPlaylistEditorLayout.createSequentialGroup()
                    .addGap(18, 18, 18)
                    .addComponent(pnlHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(18, 18, 18)))
        );

        tabbedPaneMain.addTab("Playlist Editor", jPanelPlaylistEditor);

        mnuSettings.setText("Settings");
        btnGrpParentFolders.add(mnuSettings);
        mnuSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSettingsActionPerformed(evt);
            }
        });

        mnuChkParseTitle.setText("Remove Track Number and File Extension from Track Title");
        mnuChkParseTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuChkParseTitleActionPerformed(evt);
            }
        });
        mnuSettings.add(mnuChkParseTitle);

        mnuChkParseTagsFromSingleFolder.setText("Parse Artist and Album from a Single Parent Folder");
        mnuChkParseTagsFromSingleFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuChkParseTagsFromSingleFolderActionPerformed(evt);
            }
        });
        mnuSettings.add(mnuChkParseTagsFromSingleFolder);

        mnuChkShowFileType.setText("Show File Type in Track List");
        mnuChkShowFileType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuChkShowFileTypeActionPerformed(evt);
            }
        });
        mnuSettings.add(mnuChkShowFileType);
        mnuSettings.add(jSeparator3);

        mnuChkEnableFoobar.setText("Enable Sending Playlists to JPlay Mini");
        mnuChkEnableFoobar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuChkEnableFoobarActionPerformed(evt);
            }
        });
        mnuSettings.add(mnuChkEnableFoobar);
        mnuSettings.add(jSeparator1);

        mnuChkUseEmbeddedTags.setText("Use Embedded Tags when Processing FLAC and M4A");
        mnuChkUseEmbeddedTags.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuChkUseEmbeddedTagsActionPerformed(evt);
            }
        });
        mnuSettings.add(mnuChkUseEmbeddedTags);

        mnuChkArtistInTrackTitle.setText("Title Tracks \"Artist - Title\"");
        mnuChkArtistInTrackTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuChkArtistInTrackTitleActionPerformed(evt);
            }
        });
        mnuSettings.add(mnuChkArtistInTrackTitle);

        mnuChkShowSamplingRate.setText("Show Sampling Rate when Reading Embedded Tags");
        mnuChkShowSamplingRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuChkShowSamplingRateActionPerformed(evt);
            }
        });
        mnuSettings.add(mnuChkShowSamplingRate);
        mnuSettings.add(jSeparator2);

        mnuChkServer.setText("Act as Server");
        mnuChkServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuChkServerActionPerformed(evt);
            }
        });
        mnuSettings.add(mnuChkServer);

        mnuChkClient.setText("Act as Client");
        mnuChkClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuChkClientActionPerformed(evt);
            }
        });
        mnuSettings.add(mnuChkClient);

        mnuMain.add(mnuSettings);

        mnuHelp.setText("Help");

        mnuCPLEHelp.setText("JPlaylist Editor Help");
        mnuCPLEHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCPLEHelpActionPerformed(evt);
            }
        });
        mnuHelp.add(mnuCPLEHelp);

        mnuHelpAbout.setText("About JPlaylist Editor");
        mnuHelpAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuHelpAboutActionPerformed(evt);
            }
        });
        mnuHelp.add(mnuHelpAbout);

        mnuMain.add(mnuHelp);

        setJMenuBar(mnuMain);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPaneMain)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPaneMain)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold defaultstate="collapsed" desc="Letter Button Event Handlers">
    private void btnAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_btnAActionPerformed

    private void btnBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_btnBActionPerformed

    private void brnCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brnCActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_brnCActionPerformed

    private void btnDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_btnDActionPerformed

    private void btnEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_btnEActionPerformed

    private void btnFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_btnFActionPerformed

    private void btnGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_btnGActionPerformed

    private void btnHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_btnHActionPerformed

    private void btnIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_btnIActionPerformed

    private void btnJActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_btnJActionPerformed

    private void btnKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_btnKActionPerformed

    private void btnLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_btnLActionPerformed

    private void btnMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_btnMActionPerformed

    private void btnNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_btnNActionPerformed

    private void btnOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_btnOActionPerformed

    private void btnPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_btnPActionPerformed

    private void btnQActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_btnQActionPerformed

    private void btnRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_btnRActionPerformed

    private void btnSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_btnSActionPerformed

    private void btnTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_btnTActionPerformed

    private void btnUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_btnUActionPerformed

    private void btnVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_btnVActionPerformed

    private void btnWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_btnWActionPerformed

    private void btnXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_btnXActionPerformed

    private void btnYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnYActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_btnYActionPerformed

    private void btnZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZActionPerformed
        onLetterSelect(evt.getActionCommand());
    }//GEN-LAST:event_btnZActionPerformed
    // </editor-fold>
private void btnSetPlaylistDirectoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetPlaylistDirectoryActionPerformed
            int retval = fcPlaylistFolder.showOpenDialog(this);
            if (retval == JFileChooser.APPROVE_OPTION) {
                playlistFolder = fcPlaylistFolder.getSelectedFile();
                options.setPlaylistFolder(playlistFolder);
                lblPlaylistFolder.setText("Playlist Directory: " + playlistFolder.getPath());
                btnOpen.setEnabled(true);
            } else {
                options.setPlaylistFolder(null);
                lblPlaylistFolder.setText("Playlist Directory: Not set");
            }

}//GEN-LAST:event_btnSetPlaylistDirectoryActionPerformed

private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    options.saveOptions();
}//GEN-LAST:event_formWindowClosing

private void mnuSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSettingsActionPerformed
    if (mnuChkParseTagsFromSingleFolder.isSelected()) {
        String seperator = JOptionPane.showInputDialog(this, "Enter the characters that separate Artist from Album Title", "Enter Seperator", 1);
        if (seperator != null) {
            options.setParseTagsFromSingleFolder(true);
            options.setParseSingleFolderSeperator(seperator);
        } else {
            options.setParseTagsFromSingleFolder(false);
            mnuChkParseTagsFromSingleFolder.setSelected(false);
        }
    } else {
        options.setParseTagsFromSingleFolder(false);
    }

}//GEN-LAST:event_mnuSettingsActionPerformed

private void mnuChkUseEmbeddedTagsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuChkUseEmbeddedTagsActionPerformed
    if (mnuChkUseEmbeddedTags.isSelected()) {
        mnuChkArtistInTrackTitle.setEnabled(true);
        mnuChkShowSamplingRate.setEnabled(true);
    } else {
        mnuChkArtistInTrackTitle.setSelected(false);
        mnuChkArtistInTrackTitle.setEnabled(false);
        mnuChkShowSamplingRate.setSelected(false);
        mnuChkShowSamplingRate.setEnabled(false);
    }
    options.setUseEmbeddedTags(mnuChkUseEmbeddedTags.isSelected());
    options.setArtistInTrackTitle(mnuChkArtistInTrackTitle.isSelected());

}//GEN-LAST:event_mnuChkUseEmbeddedTagsActionPerformed

private void mnuChkArtistInTrackTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuChkArtistInTrackTitleActionPerformed
    options.setArtistInTrackTitle(mnuChkArtistInTrackTitle.isSelected());

    for (int counter = 0; counter < options.getLibraryRoots().size(); counter++) {
        options.getLibraryRoots().elementAt(counter).getLibrary().changeTrackDisplay(options);
    }
}//GEN-LAST:event_mnuChkArtistInTrackTitleActionPerformed

private void mnuChkParseTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuChkParseTitleActionPerformed
    options.setParseTitle(mnuChkParseTitle.isSelected());
}//GEN-LAST:event_mnuChkParseTitleActionPerformed

private void mnuChkParseTagsFromSingleFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuChkParseTagsFromSingleFolderActionPerformed
    if (mnuChkParseTagsFromSingleFolder.isSelected()) {
        String seperator = JOptionPane.showInputDialog(this, "Enter the characters that separate Artist from Album Title", "Enter Seperator", 1);
        if (seperator != null) {
            options.setParseTagsFromSingleFolder(true);
            options.setParseSingleFolderSeperator(seperator);
        } else {
            options.setParseTagsFromSingleFolder(false);
            mnuChkParseTagsFromSingleFolder.setSelected(false);
        }
    } else {
        options.setParseTagsFromSingleFolder(false);
    }
}//GEN-LAST:event_mnuChkParseTagsFromSingleFolderActionPerformed

private void btnReadLibraryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadLibraryActionPerformed
    LibraryProcessor lp = new LibraryProcessor(options, this);
    lblStatus.setText("Status: Reading music library");
    lp.start();
}//GEN-LAST:event_btnReadLibraryActionPerformed

private void lstAlbumsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstAlbumsValueChanged
    if (evt.getValueIsAdjusting()) {
        return;
    }
    JList albumList = (JList) evt.getSource();
    int index = albumList.getSelectedIndex();
    currentAlbum = currentAlbumList.elementAt(index);


    lstTracks = new JList(currentAlbum.getTracks());
    lstTracks.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    lstTracks.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

        public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
            lstTracksValueChanged(evt);
        }
    });

    lstTracks.addMouseListener(new java.awt.event.MouseAdapter() {

        public void mouseClicked(java.awt.event.MouseEvent evt) {
            lstTracksMouseClicked(evt);
        }
    });


    scrollPaneTracks.setViewportView(lstTracks);
    btnAddTrack.setEnabled(false);
}//GEN-LAST:event_lstAlbumsValueChanged

private void lstTracksValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstTracksValueChanged
    if (evt.getValueIsAdjusting()) {
        return;
    }
    JList trackList = (JList) evt.getSource();
    if (trackList.isSelectionEmpty()) {
        btnAddTrack.setEnabled(false);
    } else {
        btnAddTrack.setEnabled(true);
    }

}//GEN-LAST:event_lstTracksValueChanged

private void btnAddTrackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddTrackActionPerformed
    Object[] selectedTracks = lstTracks.getSelectedValues();
    for (Object o : selectedTracks) {
        playlist.addTrack((Track) o);
    }

    lstPlaylist = new JList(playlist.getTracks());
    lstPlaylist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    lstPlaylist.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

        public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
            lstPlaylistValueChanged(evt);
        }
    });

    scrollPanePlaylist.setViewportView(lstPlaylist);
    btnUp.setEnabled(false);
    btnDown.setEnabled(false);
    btnSave.setEnabled(true);
    btnClear.setEnabled(true);
    enablePlayButtonIfAllowed();
}//GEN-LAST:event_btnAddTrackActionPerformed

private void lstPlaylistValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstPlaylistValueChanged
    if (evt.getValueIsAdjusting()) {
        return;
    }
    JList playlistList = (JList) evt.getSource();
    if (playlistList.isSelectionEmpty()) {
        btnRemoveTrack.setEnabled(false);
    } else {
        btnRemoveTrack.setEnabled(true);
    }

    setUpDownButtons();
    setClearButton();
    enablePlayButtonIfAllowed();
}//GEN-LAST:event_lstPlaylistValueChanged

    private void setClearButton() {
        int listCount = lstPlaylist.getModel().getSize();
        if (listCount > 0) {
            btnClear.setEnabled(true);
        } else {
            btnClear.setEnabled(false);
        }
    }

    private void setUpDownButtons() {
        int listCount = 0;
        if (lstPlaylist.isSelectionEmpty()) {
            btnUp.setEnabled(false);
            btnDown.setEnabled(false);
        } else {
            listCount = lstPlaylist.getModel().getSize();
            if (listCount > 1) {
                if (lstPlaylist.getSelectedIndex() > 0) {
                    btnUp.setEnabled(true);
                } else {
                    btnUp.setEnabled(false);
                }

                if (lstPlaylist.getSelectedIndex() < listCount - 1) {
                    btnDown.setEnabled(true);
                } else {
                    btnDown.setEnabled(false);
                }
            } else {
                btnUp.setEnabled(false);
                btnDown.setEnabled(false);
            }
        }

    }

private void btnRemoveTrackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveTrackActionPerformed

    // pull out in reverse playlist order so we don't get a null pointer
    // exception as increasing tracks are removed from the playlist
    int[] index = lstPlaylist.getSelectedIndices();
    int count = index.length;
    do {
        playlist.deleteTrack(index[count - 1]);
        count--;
    } while (count > 0);

    lstPlaylist = new JList(playlist.getTracks());
    lstPlaylist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    lstPlaylist.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

        public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
            lstPlaylistValueChanged(evt);
        }
    });

    scrollPanePlaylist.setViewportView(lstPlaylist);
    btnUp.setEnabled(false);
    btnDown.setEnabled(false);
    enablePlayButtonIfAllowed();
    if (lstPlaylist.getModel().getSize() > 0) {
        btnSave.setEnabled(true);
        btnClear.setEnabled(true);
    } else {
        btnSave.setEnabled(false);
        btnClear.setEnabled(false);
    }
}//GEN-LAST:event_btnRemoveTrackActionPerformed

private void btnUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpActionPerformed
    int index = lstPlaylist.getSelectedIndex();
    Track tmpCurrentTrack = playlist.getTrack(index);
    Track tmpPreviousTrack = playlist.getTrack(index - 1);
    playlist.setTrack(tmpCurrentTrack, index - 1);
    playlist.setTrack(tmpPreviousTrack, index);

    lstPlaylist = new JList(playlist.getTracks());
    lstPlaylist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    lstPlaylist.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

        public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
            lstPlaylistValueChanged(evt);
        }
    });
    lstPlaylist.setSelectedIndex(index - 1);

    scrollPanePlaylist.setViewportView(lstPlaylist);
    setUpDownButtons();
    btnSave.setEnabled(true);
}//GEN-LAST:event_btnUpActionPerformed

private void btnDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownActionPerformed
    int index = lstPlaylist.getSelectedIndex();
    Track tmpCurrentTrack = playlist.getTrack(index);
    Track tmpNextTrack = playlist.getTrack(index + 1);
    playlist.setTrack(tmpCurrentTrack, index + 1);
    playlist.setTrack(tmpNextTrack, index);

    lstPlaylist = new JList(playlist.getTracks());
    lstPlaylist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    lstPlaylist.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

        public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
            lstPlaylistValueChanged(evt);
        }
    });

    scrollPanePlaylist.setViewportView(lstPlaylist);
    lstPlaylist.setSelectedIndex(index + 1);
    setUpDownButtons();
    btnSave.setEnabled(true);
}//GEN-LAST:event_btnDownActionPerformed

private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
    // make sure there is a playlist directory
    if (options.getPlaylistFolder() == null) {
        JOptionPane.showMessageDialog(this, "You must set a Play List directory\nbefore saving a play list.");
        return;
    }

    int retval;
    fcSavePlaylist = new JFileChooser(playlistFolder);
    M3uFileFilter m3uFilter = new M3uFileFilter();
    fcSavePlaylist.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    fcSavePlaylist.setFileFilter(m3uFilter);

    retval = fcSavePlaylist.showSaveDialog(this);
    if (retval == JFileChooser.APPROVE_OPTION) {
        if (fcSavePlaylist.getSelectedFile().getName().toLowerCase().endsWith(".m3u")) {
            playlist.setDirectoryOnDisc(fcSavePlaylist.getSelectedFile());
            playlist.setAlbumTitle(fcSavePlaylist.getSelectedFile().getName().substring(0, fcSavePlaylist.getSelectedFile().getName().length() - 4));
        } else {
            File savedFile = new File(fcSavePlaylist.getSelectedFile().getAbsolutePath() + ".m3u");
            playlist.setDirectoryOnDisc(savedFile);
            playlist.setAlbumTitle(savedFile.getName().substring(0, savedFile.getName().length() - 4));
        }

        PlaylistWriter pw = new PlaylistWriter(playlist, options);
        pw.writeM3u();
        this.setStatus("Playlist " + playlist.getAlbumTitle() + " saved");
    }


}//GEN-LAST:event_btnSaveActionPerformed

private void btnOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenActionPerformed
//    fcOpenPlaylist = new JFileChooser(playlistFolder);
//    CueFileFilter cueFilter = new CueFileFilter();
//    fcOpenPlaylist.setFileSelectionMode(JFileChooser.FILES_ONLY);
//    fcOpenPlaylist.setFileFilter(cueFilter);
//
//    int retval = fcOpenPlaylist.showOpenDialog(this);
//    if (retval == JFileChooser.APPROVE_OPTION) {
//        if (fcOpenPlaylist.getSelectedFile().getName().toLowerCase().endsWith(".cue")) {
//            playlist = PlaylistProcessor.openPlaylist(fcOpenPlaylist.getSelectedFile());
//            lstPlaylist = new JList(playlist.getTracks());
//            lstPlaylist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//            lstPlaylist.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
//
//                public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
//                    lstPlaylistValueChanged(evt);
//                }
//            });
//
//            scrollPanePlaylist.setViewportView(lstPlaylist);
//            setUpDownButtons();
//            enablePlayButtonIfAllowed();
//            setClearButton();
//        }
//    }

////// Old method before adding Foobar support commented out above

    fcOpenPlaylist = new JFileChooser(playlistFolder);
    int retval;
    M3uFileFilter m3uFilter = new M3uFileFilter();
    fcOpenPlaylist.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fcOpenPlaylist.setFileFilter(m3uFilter);

    retval = fcOpenPlaylist.showOpenDialog(this);
    if (retval == JFileChooser.APPROVE_OPTION) {
        if (fcOpenPlaylist.getSelectedFile().getName().toLowerCase().endsWith(".m3u")) {
            playlist = PlaylistProcessor.openM3uPlaylist(fcOpenPlaylist.getSelectedFile());
            lstPlaylist = new JList(playlist.getTracks());
            lstPlaylist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            lstPlaylist.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
                public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                    lstPlaylistValueChanged(evt);
                }
            });

            scrollPanePlaylist.setViewportView(lstPlaylist);
            setUpDownButtons();
            setClearButton();
        }
    }
}//GEN-LAST:event_btnOpenActionPerformed

private void enablePlayButtonIfAllowed() {
    if ((lstPlaylist.getModel().getSize() > 0 && mnuChkEnableFoobar.isSelected()
            && options.getPlaylistFolder() != null) || mnuChkClient.isSelected()) {
        btnPlay.setEnabled(true);
    }
    else {
        btnPlay.setEnabled(false);
    }    
}

private void btnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAllActionPerformed
    currentAlbumList = library.getAllAlbums(MusicLibrary.LibrarySort.BY_ARTIST);
    lstAlbums = new JList(currentAlbumList);
    lstAlbums.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    lstAlbums.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

        public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
            lstAlbumsValueChanged(evt);
        }
    });

    scrollPaneAlbums.setViewportView(lstAlbums);

}//GEN-LAST:event_btnAllActionPerformed

private void lstTracksMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstTracksMouseClicked
    JList tList = (JList) evt.getSource();
    if (evt.getClickCount() == 2) {          // Double-click
        btnAddTrack.doClick();
        tList.clearSelection();
    }
}//GEN-LAST:event_lstTracksMouseClicked

private void mnuHelpAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuHelpAboutActionPerformed
    try {
        java.net.URL url = getClass().getResource("AboutJPlaylistEditor.html");
        HelpWindow hw = new HelpWindow("About JPlaylist Editor", url);
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
}//GEN-LAST:event_mnuHelpAboutActionPerformed

    public void playPlaylist(Playlist pl) {
        String exeCommand = "";
        PlaylistWriter pw = new PlaylistWriter(pl, options);
         if (mnuChkEnableFoobar.isSelected()){
            pl.setAlbumTitle("JPlay Temporary Playlist");
            pl.setDirectoryOnDisc(new File(playlistFolder.getAbsolutePath() + "\\" + pl.getAlbumTitle() + ".m3u"));
            exeCommand = options.getFoobarExecutablePath() + " \"" + pl.getDirectoryOnDisc().getAbsolutePath()+ "\"";
            pw.writeM3u();
        }

        pl = null;  //need to do this because the object is being hung on to when a new playlist is sent

        try {
            cplay = Runtime.getRuntime().exec(exeCommand);
            if (mnuChkServer.isSelected()) {
                cplayRobot = new Robot();
            }
//        cplay.waitFor();
        } catch (Exception e) {
            System.out.println("Error running JPLAYmini.exe");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    
private void btnPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayActionPerformed
    if (mnuChkClient.isSelected()) {
        JPlaylistEditorNetworkMessage message = new JPlaylistEditorNetworkMessage();
        message.setNetworkMessageType(JPlaylistEditorNetworkMessage.NetworkMessageType.PLAYLIST_MESSAGE);
        message.setPlaylist(playlist);
        clientListener.sendObject(message);
        enableCplayControlButtons();
        tabbedPaneMain.setSelectedIndex(1);
        return;
    }
    playPlaylist(playlist);
}//GEN-LAST:event_btnPlayActionPerformed

private void enableCplayControlButtons() {
        btnNext.setEnabled(true);
        //btnPrevious.setEnabled(true);
        btnPause.setEnabled(true);
        //btnFastforward.setEnabled(true);
        //btnRewind.setEnabled(true);
        //btnPhase.setEnabled(true);
        //btnVolumeUp.setEnabled(true);
        //btnVolumeDown.setEnabled(true);
}

private void mnuCPLEHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuCPLEHelpActionPerformed
    try {
        java.net.URL url = getClass().getResource("JPlaylistEditorHelp.html");
        HelpWindow hw = new HelpWindow("JPlaylist Editor Help", url);
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }

}//GEN-LAST:event_mnuCPLEHelpActionPerformed

private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
    int count = playlist.getTracks().length;

    do {
        playlist.deleteTrack(count - 1);
        count--;
    } while (count > 0);

//    playlist.resetHighestSamplingRate();
    
    lstPlaylist = new JList(playlist.getTracks());
    lstPlaylist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    lstPlaylist.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

        public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
            lstPlaylistValueChanged(evt);
        }
    });

    scrollPanePlaylist.setViewportView(lstPlaylist);
    btnUp.setEnabled(false);
    btnDown.setEnabled(false);
    if (lstPlaylist.getModel().getSize() > 0) {
        btnSave.setEnabled(true);
        btnClear.setEnabled(true);
    } else {
        btnSave.setEnabled(false);
        btnClear.setEnabled(false);
    }
    enablePlayButtonIfAllowed();
}//GEN-LAST:event_btnClearActionPerformed

    public void setLibrariesFromNetwork(Vector<LibraryRoot> libraryRoots) {
        options.setLibraryRoots(libraryRoots);
        if (options.getLibraryRoots() != null) {
            lstLibraryRoots = new JList(options.getLibraryRoots());
            lstLibraryRoots.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            lstLibraryRoots.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

                public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                    lstLibraryRootsValueChanged(evt);
                }
            });
            scrollPaneLibraryRoots.setViewportView(lstLibraryRoots);
        }

    }

    private void getLibrariesFromServer() {
        JPlaylistEditorNetworkMessage message = new JPlaylistEditorNetworkMessage();
        message.setNetworkMessageType(JPlaylistEditorNetworkMessage.NetworkMessageType.GET_LIBRARY_MESSAGE);
        clientListener.sendObject(message);
    }

private void btnAddLibraryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddLibraryActionPerformed
    if (mnuChkClient.isSelected()) {
        getLibrariesFromServer();
        return;
    }

//    boolean isCueOnly = false;
//    JPanel accesoryPanel = new JPanel(new GridLayout(2, 1));
//    accesoryPanel.setBorder(BorderFactory.createTitledBorder("Options"));

//    JCheckBox checkbox = new JCheckBox ("Cue only", isCueOnly);
//    accesoryPanel.add (checkbox);

//    fcLibraryRoot.setAccessory(accesoryPanel);
    int retval = fcLibraryRoot.showOpenDialog(this);

    if (retval == JFileChooser.APPROVE_OPTION) {

//        isCueOnly = checkbox.isSelected();

        libraryRoot = fcLibraryRoot.getSelectedFile();
        options.setLibraryRoot(libraryRoot);
        lblLibraryRoot.setText("Library Root Folder: " + libraryRoot.getPath());
        btnReadLibrary.setEnabled(true);
        String libraryName = (String) JOptionPane.showInputDialog(
                this, "please give this library a name: ", "Name Library",
                JOptionPane.PLAIN_MESSAGE);
        if ((libraryName != null) && (libraryName.length() > 0)) {
            LibraryRoot lr = new LibraryRoot(libraryRoot.getPath(), libraryName);
            options.addLibraryRoot(lr);
            // add stuff to list of library roots
            lstLibraryRoots = new JList(options.getLibraryRoots());
            lstLibraryRoots.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            lstLibraryRoots.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

                public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                    lstLibraryRootsValueChanged(evt);
                }
            });

            scrollPaneLibraryRoots.setViewportView(lstLibraryRoots);
            lstLibraryRoots.setSelectedIndex(options.getLibraryRoots().size() - 1);

            LibraryProcessor lp = new LibraryProcessor(options, this);
            lblStatus.setText("Status: Reading music library");
            lp.start();
        }
    }
}//GEN-LAST:event_btnAddLibraryActionPerformed

private void lstLibraryRootsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstLibraryRootsValueChanged
    if (evt.getValueIsAdjusting()) {
        return;
    }
    JList libraryRootList = (JList) evt.getSource();
    if (libraryRootList.isSelectionEmpty()) {
        btnRemoveLibrary.setEnabled(false);
        lblLibraryRoot.setText("Library Root Folder: ");
    } else {
        btnRemoveLibrary.setEnabled(true);
        int selectedIndex = libraryRootList.getSelectedIndex();
        lblLibraryRoot.setText("Library Root Folder: " + options.getLibraryRoots().elementAt(selectedIndex).getLibraryRootPath());
        libraryRoot = new File(options.getLibraryRoots().elementAt(selectedIndex).getLibraryRootPath());
        options.setLibraryRoot(libraryRoot);
        options.setSelectedLibrary(selectedIndex);

        if (options.getLibraryRoots().elementAt(selectedIndex).getLibrary() != null) {
            setLibrary(options.getLibraryRoots().elementAt(selectedIndex).getLibrary());
            // write library to options file also
            // options.getLibraryRoots().elementAt(options.getSelectedLibrary()).setLibrary(library);
            setLibraryAvailable();
        }

    }
}//GEN-LAST:event_lstLibraryRootsValueChanged

private void btnRemoveLibraryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveLibraryActionPerformed

    int selectedIndex = lstLibraryRoots.getSelectedIndex();
    if (selectedIndex > -1) {
        options.deleteLibraryRoot(selectedIndex);
        // add stuff to list of library roots
        lstLibraryRoots = new JList(options.getLibraryRoots());
        lstLibraryRoots.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstLibraryRoots.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstLibraryRootsValueChanged(evt);
            }
        });

        scrollPaneLibraryRoots.setViewportView(lstLibraryRoots);

        if (!options.getLibraryRoots().isEmpty()) {
            lstLibraryRoots.setSelectedIndex(0);
        }
    }
}//GEN-LAST:event_btnRemoveLibraryActionPerformed

private void mnuChkShowSamplingRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuChkShowSamplingRateActionPerformed
    options.setShowSamplingRate(mnuChkShowSamplingRate.isSelected());

    for (int counter = 0; counter < options.getLibraryRoots().size(); counter++) {
        options.getLibraryRoots().elementAt(counter).getLibrary().changeTrackDisplay(options);
    }

}//GEN-LAST:event_mnuChkShowSamplingRateActionPerformed

private void mnuChkShowFileTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuChkShowFileTypeActionPerformed
    options.setShowFileType(mnuChkShowFileType.isSelected());

    for (int counter = 0; counter < options.getLibraryRoots().size(); counter++) {
        options.getLibraryRoots().elementAt(counter).getLibrary().changeTrackDisplay(options);
    }
}//GEN-LAST:event_mnuChkShowFileTypeActionPerformed

private void mnuChkServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuChkServerActionPerformed
    if (mnuChkServer.isSelected()) {
        serverListener = new JPlaylistServerListener(this, options);
        serverThread = new Thread(serverListener);
        serverThread.start();
    } else {
        serverListener.stopServer();
    }
    options.setRunAsServer(mnuChkServer.isSelected());
}//GEN-LAST:event_mnuChkServerActionPerformed

private void mnuChkClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuChkClientActionPerformed
    if (mnuChkClient.isSelected()) {
        String ipAddress = JOptionPane.showInputDialog(this, "Enter Server IP Address", options.getServerIpAddress());
        options.setServerIpAddress(ipAddress);
        clientListener = new JPlaylistClientListener(this, options);
        clientThread = new Thread(clientListener);
        clientThread.start();
        btnAddLibrary.setText("Get");
        btnAddLibrary.doClick();
    } else {
        JPlaylistEditorNetworkMessage message = new JPlaylistEditorNetworkMessage();
        message.setDisconnectNotice(true);
        clientListener.sendObject(message);
        clientListener.stopClient();
        btnAddLibrary.setText("Add");
    }
}//GEN-LAST:event_mnuChkClientActionPerformed

private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
    JPlaylistEditorNetworkMessage message = new JPlaylistEditorNetworkMessage();
    message.setNetworkMessageType(JPlaylistEditorNetworkMessage.NetworkMessageType.PREVIOUS_SONG_MESSAGE);
    clientListener.sendObject(message);    
}//GEN-LAST:event_btnPreviousActionPerformed

private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
    JPlaylistEditorNetworkMessage message = new JPlaylistEditorNetworkMessage();
    message.setNetworkMessageType(JPlaylistEditorNetworkMessage.NetworkMessageType.NEXT_SONG_MESSAGE);
    clientListener.sendObject(message);
}//GEN-LAST:event_btnNextActionPerformed

private void btnPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPauseActionPerformed
    JPlaylistEditorNetworkMessage message = new JPlaylistEditorNetworkMessage();
    message.setNetworkMessageType(JPlaylistEditorNetworkMessage.NetworkMessageType.PAUSE_SONG_MESSAGE);
    clientListener.sendObject(message);
}//GEN-LAST:event_btnPauseActionPerformed

private void btnRewindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRewindActionPerformed
    JPlaylistEditorNetworkMessage message = new JPlaylistEditorNetworkMessage();
    message.setNetworkMessageType(JPlaylistEditorNetworkMessage.NetworkMessageType.REWIND_MESSAGE);
    clientListener.sendObject(message);    
}//GEN-LAST:event_btnRewindActionPerformed

private void btnFastforwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFastforwardActionPerformed
    JPlaylistEditorNetworkMessage message = new JPlaylistEditorNetworkMessage();
    message.setNetworkMessageType(JPlaylistEditorNetworkMessage.NetworkMessageType.FAST_FORWARD_MESSAGE);
    clientListener.sendObject(message);    
}//GEN-LAST:event_btnFastforwardActionPerformed

private void btnVolumeDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolumeDownActionPerformed
    JPlaylistEditorNetworkMessage message = new JPlaylistEditorNetworkMessage();
    message.setNetworkMessageType(JPlaylistEditorNetworkMessage.NetworkMessageType.VOLUME_DOWN_MESSAGE);
    clientListener.sendObject(message);    
}//GEN-LAST:event_btnVolumeDownActionPerformed

private void btnVolumeUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolumeUpActionPerformed
    JPlaylistEditorNetworkMessage message = new JPlaylistEditorNetworkMessage();
    message.setNetworkMessageType(JPlaylistEditorNetworkMessage.NetworkMessageType.VOLUME_UP_MESSAGE);
    clientListener.sendObject(message);    
}//GEN-LAST:event_btnVolumeUpActionPerformed

private void btnPhaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhaseActionPerformed
    JPlaylistEditorNetworkMessage message = new JPlaylistEditorNetworkMessage();
    message.setNetworkMessageType(JPlaylistEditorNetworkMessage.NetworkMessageType.PHASE_MESSAGE);
    clientListener.sendObject(message);    
}//GEN-LAST:event_btnPhaseActionPerformed

private void lstGenresValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstGenresValueChanged
        if (evt.getValueIsAdjusting()) {
        return;
    }
    JList genreList = (JList)evt.getSource();
    String genre = (String)genreList.getSelectedValue();

    currentAlbumList = library.getAlbumsByGenre(genre);
    lstAlbums = new JList(currentAlbumList);
    lstAlbums.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    lstAlbums.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
        public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
            lstAlbumsValueChanged(evt);
        }
    });

    scrollPaneAlbums.setViewportView(lstAlbums);
}//GEN-LAST:event_lstGenresValueChanged

private void mnuChkEnableFoobarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuChkEnableFoobarActionPerformed
    JFileChooser foobarChooser = new JFileChooser();
    foobarChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

    if (mnuChkEnableFoobar.isSelected()) {

        int retval = foobarChooser.showDialog(this, "Select JPLAYmini.exe");
        if (retval == JFileChooser.APPROVE_OPTION) {
            options.setEnableFoobar(true);
            options.setFoobarExecutablePath(foobarChooser.getSelectedFile().getAbsolutePath());
        } else {
            options.setEnableFoobar(false);
            mnuChkEnableFoobar.setSelected(false);
        }
    } else {
        options.setEnableFoobar(false);
        options.setFoobarExecutablePath(null);
    }
}//GEN-LAST:event_mnuChkEnableFoobarActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JPlaylistGUI().setVisible(true);
            }
        });
    }

    private void initCustomComponents() {
        enablePanel(pnlLetters, false);
        enablePanel(pnlGenres, false);
        fcLibraryRoot.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fcPlaylistFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    // Disabling the entire panel is not disabling the children
    // so this custom method will run through the children and enable or disable.
    // a slight change couls make this useful for any type of panel
    private void enablePanel(javax.swing.JPanel root, boolean enable) {
        java.awt.Component children[] = root.getComponents();
        for(int i = 0; i < children.length; i++) {
            if(children[i] instanceof javax.swing.JComponent) {
                children[i].setEnabled(enable);
            } 
        }
    }




    // Event handler for all letter button presses
    private void onLetterSelect(String letter) {
//        System.out.println(letter);
//        currentAlbumList = library.getAlbumsByArtistString(letter);
//        DefaultListModel listModel = new DefaultListModel();
//        for (Album a : currentAlbumList) {
//            listModel.addElement(a);
//        }
//        lstAlbums = new JList(listModel);
//        lstAlbums.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        scrollPaneAlbums.setViewportView(lstAlbums);
        
        currentAlbumList = library.getAlbumsByArtistString(letter);
        lstAlbums = new JList(currentAlbumList);
        lstAlbums.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        lstAlbums.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstAlbumsValueChanged(evt);
            }
        });

        scrollPaneAlbums.setViewportView(lstAlbums);
    }

    private Vector<Integer> getKeysForAction(JPlayControl.JPlayAction action) {
        Vector<Integer> ke = new Vector<Integer>(0,1);
        if (mnuChkEnableFoobar.isSelected()) {
                switch (action) {
                    case NEXT_SONG:
                        //ke.add(KeyEvent.VK_SHIFT);
                        //ke.add(KeyEvent.VK_CONTROL);
                        ke.add(KeyEvent.VK_SPACE);
                        break;
                    case PREVIOUS_SONG:
                        ke.add(KeyEvent.VK_SHIFT);
                        ke.add(KeyEvent.VK_CONTROL);
                        ke.add(KeyEvent.VK_B);
                        break;
                    case PAUSE_SONG:
                        //ke.add(KeyEvent.VK_SHIFT);
                        //ke.add(KeyEvent.VK_CONTROL);
                        ke.add(KeyEvent.VK_P);
                        break;
                    case FAST_FORWARD:
                        ke.add(KeyEvent.VK_SHIFT);
                        ke.add(KeyEvent.VK_CONTROL);
                        ke.add(KeyEvent.VK_F);
                        break;
                    case REWIND:
                        ke.add(KeyEvent.VK_SHIFT);
                        ke.add(KeyEvent.VK_CONTROL);
                        ke.add(KeyEvent.VK_R);
                        break;
                    case CHANGE_PHASE:
                        break;
                    case VOLUME_UP:
                        ke.add(KeyEvent.VK_SHIFT);
                        ke.add(KeyEvent.VK_CONTROL);
                        ke.add(KeyEvent.VK_U);
                        break;
                    case VOLUME_DOWN:
                        ke.add(KeyEvent.VK_SHIFT);
                        ke.add(KeyEvent.VK_CONTROL);
                        ke.add(KeyEvent.VK_D);
                        break;
                }
        }
        return ke;
    }

    public void controlCplay(JPlayControl.JPlayAction action) {
        if (cplay != null && cplayRobot != null) {
            Vector<Integer> keyEvents = getKeysForAction(action);
            try {

                for (Integer kp : keyEvents) {
                    cplayRobot.keyPress(kp.intValue());
                }

                for (Integer kr : keyEvents) {
                    cplayRobot.keyRelease(kr.intValue());
                }

//                switch (action) {
//                    case NEXT_SONG:
//                        cplayRobot.keyPress(KeyEvent.VK_CLOSE_BRACKET);
//                        cplayRobot.keyRelease(KeyEvent.VK_CLOSE_BRACKET);
//                        break;
//                    case PREVIOUS_SONG:
//                        cplayRobot.keyPress(KeyEvent.VK_OPEN_BRACKET);
//                        cplayRobot.keyRelease(KeyEvent.VK_OPEN_BRACKET);
//                        break;
//                    case PAUSE_SONG:
//                        cplayRobot.keyPress(KeyEvent.VK_P);
//                        cplayRobot.keyRelease(KeyEvent.VK_P);
//                        break;
//                    case FAST_FORWARD:
//                        cplayRobot.keyPress(KeyEvent.VK_EQUALS);
//                        cplayRobot.keyRelease(KeyEvent.VK_EQUALS);
//                        break;
//                    case REWIND:
//                        cplayRobot.keyPress(KeyEvent.VK_MINUS);
//                        cplayRobot.keyRelease(KeyEvent.VK_MINUS);
//                        break;
//                    case CHANGE_PHASE:
//                        cplayRobot.keyPress(KeyEvent.VK_SEMICOLON);
//                        cplayRobot.keyRelease(KeyEvent.VK_SEMICOLON);
//                        break;
//                    case VOLUME_UP:
//                        cplayRobot.keyPress(KeyEvent.VK_SHIFT);
//                        cplayRobot.keyPress(KeyEvent.VK_HOME);
//                        cplayRobot.keyRelease(KeyEvent.VK_SHIFT);
//                        cplayRobot.keyRelease(KeyEvent.VK_HOME);
//                        break;
//                    case VOLUME_DOWN:
//                        cplayRobot.keyPress(KeyEvent.VK_SHIFT);
//                        cplayRobot.keyPress(KeyEvent.VK_END);
//                        cplayRobot.keyRelease(KeyEvent.VK_SHIFT);
//                        cplayRobot.keyRelease(KeyEvent.VK_END);
//                        break;
//                }
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton brnC;
    private javax.swing.JRadioButton btnA;
    private javax.swing.JButton btnAddLibrary;
    private javax.swing.JButton btnAddTrack;
    private javax.swing.JRadioButton btnAll;
    private javax.swing.JRadioButton btnB;
    private javax.swing.JButton btnClear;
    private javax.swing.JRadioButton btnD;
    private javax.swing.JButton btnDown;
    private javax.swing.JRadioButton btnE;
    private javax.swing.JRadioButton btnF;
    private javax.swing.JButton btnFastforward;
    private javax.swing.JRadioButton btnG;
    private javax.swing.ButtonGroup btnGroupSamplingRate;
    private javax.swing.ButtonGroup btnGrpParentFolders;
    private javax.swing.ButtonGroup btnGrpPlaylistFormat;
    private javax.swing.ButtonGroup btnGrpSelectLetter;
    private javax.swing.ButtonGroup btnGrpUpsamplingRule;
    private javax.swing.JRadioButton btnH;
    private javax.swing.JRadioButton btnI;
    private javax.swing.JRadioButton btnJ;
    private javax.swing.JRadioButton btnK;
    private javax.swing.JRadioButton btnL;
    private javax.swing.JRadioButton btnM;
    private javax.swing.JRadioButton btnN;
    private javax.swing.JButton btnNext;
    private javax.swing.JRadioButton btnO;
    private javax.swing.JButton btnOpen;
    private javax.swing.JRadioButton btnP;
    private javax.swing.JButton btnPause;
    private javax.swing.JButton btnPhase;
    private javax.swing.JButton btnPlay;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JRadioButton btnQ;
    private javax.swing.JRadioButton btnR;
    private javax.swing.JButton btnReadLibrary;
    private javax.swing.JButton btnRemoveLibrary;
    private javax.swing.JButton btnRemoveTrack;
    private javax.swing.JButton btnRewind;
    private javax.swing.JRadioButton btnS;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSetPlaylistDirectory;
    private javax.swing.JRadioButton btnT;
    private javax.swing.JRadioButton btnU;
    private javax.swing.JButton btnUp;
    private javax.swing.JRadioButton btnV;
    private javax.swing.JButton btnVolumeDown;
    private javax.swing.JButton btnVolumeUp;
    private javax.swing.JRadioButton btnW;
    private javax.swing.JRadioButton btnX;
    private javax.swing.JRadioButton btnY;
    private javax.swing.JRadioButton btnZ;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelCplayControl;
    private javax.swing.JPanel jPanelPlaylistEditor;
    private javax.swing.JPanel jPanelSettings;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lblAlbums;
    private javax.swing.JLabel lblCplayControls;
    private javax.swing.JLabel lblGenre;
    private javax.swing.JLabel lblLibrary;
    private javax.swing.JLabel lblLibraryRead;
    private javax.swing.JLabel lblLibraryRoot;
    private javax.swing.JLabel lblNetworkStatus;
    private javax.swing.JLabel lblPlaylist;
    private javax.swing.JLabel lblPlaylistFolder;
    private javax.swing.JLabel lblSelectArtists;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTracks;
    private javax.swing.JList lstAlbums;
    private javax.swing.JList lstGenres;
    private javax.swing.JList lstLibraryRoots;
    private javax.swing.JList lstPlaylist;
    private javax.swing.JList lstTracks;
    private javax.swing.JMenuItem mnuCPLEHelp;
    private javax.swing.JCheckBoxMenuItem mnuChkArtistInTrackTitle;
    private javax.swing.JCheckBoxMenuItem mnuChkClient;
    private javax.swing.JCheckBoxMenuItem mnuChkEnableFoobar;
    private javax.swing.JCheckBoxMenuItem mnuChkParseTagsFromSingleFolder;
    private javax.swing.JCheckBoxMenuItem mnuChkParseTitle;
    private javax.swing.JCheckBoxMenuItem mnuChkServer;
    private javax.swing.JCheckBoxMenuItem mnuChkShowFileType;
    private javax.swing.JCheckBoxMenuItem mnuChkShowSamplingRate;
    private javax.swing.JCheckBoxMenuItem mnuChkUseEmbeddedTags;
    private javax.swing.JMenu mnuHelp;
    private javax.swing.JMenuItem mnuHelpAbout;
    private javax.swing.JMenuBar mnuMain;
    private javax.swing.JMenu mnuSettings;
    private javax.swing.JPanel pnlGenres;
    private javax.swing.JPanel pnlHolder;
    private javax.swing.JPanel pnlLetters;
    private javax.swing.JScrollPane scrollPaneAlbums;
    private javax.swing.JScrollPane scrollPaneGenres;
    private javax.swing.JScrollPane scrollPaneLibraryRoots;
    private javax.swing.JScrollPane scrollPanePlaylist;
    private javax.swing.JScrollPane scrollPaneTracks;
    private javax.swing.JTabbedPane tabbedPaneMain;
    // End of variables declaration//GEN-END:variables


}
