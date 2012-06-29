package richList;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Hauptfenster implements ActionListener {

    JFrame fenster;
    JFrame slFenster;
    JLabel frage;
    DefaultListModel liste;
    DefaultListModel ergebnisliste;
    JList l2;
    JTabbedPane tabs;
    private static final boolean DUALMONITOR = false;
    boolean dualwindow;
    JPanel spiel, spielleiter;

    public static void main(String[] args) {
        new Hauptfenster();
    }

    public Hauptfenster() {
        this.erzeugeGUI();
    }

    private void erzeugeGUI() {
        fenster = new JFrame("Rich List");
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.out.println("Ohhh - kaputt");
        }


        this.erzeugeMenu();

        spiel = new JPanel(new BorderLayout());
        spielleiter = new JPanel(new BorderLayout());

        tabs = new JTabbedPane();
        fenster.add(tabs);
        tabs.add("Spiel", spiel);
        tabs.add("Spielleiter", spielleiter);
        dualwindow = false;
//        if (Hauptfenster.DUALMONITOR) {
//            slFenster = new JFrame("Rich List - Spielleiter");
//            slFenster.add(spielleiter);
//            dualwindow = true;
//        }

        //Spielansicht
        JPanel p = new JPanel(new FlowLayout());
        frage = new JLabel();
        p.add(frage);
        spiel.add(p, BorderLayout.NORTH);


        liste = new DefaultListModel();
        JList l = new JList(liste);
        l.setLayoutOrientation(JList.VERTICAL_WRAP);
        JScrollPane sp = new JScrollPane(l);
        spiel.add(sp, BorderLayout.CENTER);


        //Spielleiteransicht

        ergebnisliste = new DefaultListModel();
        l2 = new JList(ergebnisliste);
        l2.setLayoutOrientation(JList.VERTICAL_WRAP);
        sp = new JScrollPane(l2);
        spielleiter.add(sp);

        JPanel btnctn = new JPanel(new FlowLayout());
        JButton btn = new JButton("Zeigen");
        btnctn.add(btn);
        btn.addActionListener(this);
        btn.setActionCommand("zeige");
        btn.setToolTipText("STRG + Enter");

        JButton btn2 = new JButton("Auflösen");
        btnctn.add(btn2);
        btn2.addActionListener(this);
        btn2.setActionCommand("aufl");

        spielleiter.add(btnctn, BorderLayout.SOUTH);



        //Alles fertig, GUI anzeigen.
        fenster.pack();
        fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Bildschirmabmessungen holen
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int xpos = screenSize.width / 2 - fenster.getWidth() / 2;
        int ypos = screenSize.height / 2 - fenster.getHeight() / 2;
        // Rahmen auf Bildschirm zentrieren
        fenster.setLocation(xpos, ypos);

        fenster.setVisible(true);

//        //Zweites Fenster zeigen.
//        if (DUALMONITOR) {
//            slFenster.pack();
//            slFenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            slFenster.setLocation(xpos, ypos + fenster.getHeight());
//            slFenster.setVisible(true);
//        }
    }

    private void erzeugeMenu() {
        JMenuBar mb = new JMenuBar();
        fenster.setJMenuBar(mb);

        JMenu datei = new JMenu("Spiel");
        datei.setMnemonic('M');
        mb.add(datei);

        JMenuItem item = new JMenuItem("Öffne");
        item.addActionListener(this);
        item.setActionCommand("offne");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        item.setMnemonic('O');
        datei.add(item);
        datei.addSeparator();

        item = new JMenuItem("Zeige markiertes Element");
        item.addActionListener(this);
        item.setActionCommand("zeige");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK));
        item.setMnemonic('Z');
        datei.add(item);

        item = new JMenuItem("Spiel auflösen");
        item.addActionListener(this);
        item.setActionCommand("aufl");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
        item.setMnemonic('A');
        datei.add(item);


        item = new JMenuItem("Spielleiter-Fenster");
        item.addActionListener(this);
        item.setActionCommand("slf");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        item.setMnemonic('S');
        datei.add(item);
        datei.addSeparator();

        item = new JMenuItem("Beenden");
        item.addActionListener(this);
        item.setActionCommand("beenden");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
        item.setMnemonic('Q');
        datei.add(item);

        JMenu hilfe = new JMenu("?");
        item = new JMenuItem("Über dieses Programm");
        item.addActionListener(this);
        item.setActionCommand("about");
        hilfe.add(item);
        mb.add(hilfe);



    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        String cmd = arg0.getActionCommand();

        if (cmd == "offne") {
            this.ladeDatei();
        } else if (cmd == "zeige") {
            int index = l2.getSelectedIndex();
            if (!((String) ergebnisliste.get(index)).startsWith("<html>")) {
                liste.set(index, ergebnisliste.get(index));
                ergebnisliste.set(index, "<html><s>" + ergebnisliste.get(index) + "</s></html>");
                tabs.setSelectedIndex(0);
                fenster.pack();
            }
        } else if (cmd == "aufl") {
            for (int i = 0; i < ergebnisliste.getSize(); i++) {
                if (!((String) ergebnisliste.get(i)).startsWith("<html>")) {
                    liste.set(i, ergebnisliste.get(i));
                }
                tabs.setSelectedIndex(0);
            }
        } else if (cmd == "beenden") {
            System.exit(0);
        } else if (cmd == "about") {
            JOptionPane.showMessageDialog(fenster, "Veröffentlicht von Daniel Müllers 2012 unter \nCreative Commons 3.0 Deutschland Share-Alike-Nicht-Kommerziell (CC-BY-SA-NC)", "Über dieses Programm", JOptionPane.INFORMATION_MESSAGE);
        } else if (cmd == "slf") {
            if (this.dualwindow) {
                tabs.add("Spielleiter", spielleiter);
                fenster.pack();
                dualwindow = false;
                slFenster.setVisible(false);
            } else {
                slFenster = new JFrame("RichList - Spielleiter");
                slFenster.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                slFenster.add(spielleiter);
                slFenster.pack();
                Toolkit kit = Toolkit.getDefaultToolkit();
                Dimension screenSize = kit.getScreenSize();
                int xpos = screenSize.width / 2 - fenster.getWidth() / 2;
                int ypos = screenSize.height / 2 - fenster.getHeight() / 2;
                slFenster.setLocation(xpos, ypos + fenster.getHeight());
                slFenster.setVisible(true);
                dualwindow = true;
            }
        }
    }

    private void ladeDatei() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Textdateien", "txt"));

        int state = fc.showOpenDialog(fenster);

        if (state == JFileChooser.APPROVE_OPTION) {
            File datei = fc.getSelectedFile();
            try {
                BufferedReader br = new BufferedReader(new FileReader(datei));

                liste.removeAllElements();
                ergebnisliste.removeAllElements();
                String s = br.readLine();
                if (s != null) {
                    frage.setText("<html><b style='font-size: larger'>" + s + "</b></html>");
                }
                s = br.readLine();
                int i = 1;
                while (s != null) {
                    liste.addElement(i + ".");
                    ergebnisliste.addElement(i + ". " + s);
                    i++;
                    s = br.readLine();
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(fenster, e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            return;
        }
        fenster.pack();
        // Bildschirmabmessungen holen
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int xpos = screenSize.width / 2 - fenster.getWidth() / 2;
        int ypos = screenSize.height / 2 - fenster.getHeight() / 2;
        // Rahmen auf Bildschirm zentrieren
        fenster.setLocation(xpos, ypos);
        //Zweites Fenster zeigen.
        if (dualwindow) {
            slFenster.pack();
            slFenster.setLocation(xpos, ypos + fenster.getHeight());
            slFenster.setVisible(true);
        }
    }
}
