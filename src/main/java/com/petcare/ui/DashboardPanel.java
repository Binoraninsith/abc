package com.petcare.ui;

import com.petcare.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.function.Consumer;


 
public class DashboardPanel extends JPanel {

    private static final Object[][] MODULES = {
            
            { MainFrame.CUSTOMERS,    "Customers",    "Manage pet owners",               false },
            { MainFrame.PETS,         "Pets",         "Manage registered pets",          false },
            { MainFrame.SERVICES,     "Services",     "Manage the service catalogue",    true  },
            { MainFrame.APPOINTMENTS, "Appointments", "Schedule and manage visits",      false },
            { MainFrame.BILLING,      "Billing",      "Record payments for visits",      false },
            { MainFrame.REPORTS,      "Reports",      "Generate the PDF report",         true  },
    };

    public DashboardPanel(User user, Consumer<String> opener) {
        setLayout(new BorderLayout(0, 18));
        setBackground(Theme.BG);
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        add(banner(user), BorderLayout.NORTH);

        boolean staff = "STAFF".equalsIgnoreCase(user.getRole());
        JPanel grid = new JPanel(new GridLayout(0, 3, 16, 16));
        grid.setOpaque(false);
        int count = 0;
        for (Object[] m : MODULES) {
            if ((Boolean) m[3] && staff) {
                continue; 
            }
            count++;
            String key = (String) m[0];
            grid.add(new Tile(String.format("%02d", count), (String) m[1], (String) m[2],
                    () -> opener.accept(key)));
        }
        while (count % 3 != 0) { 
            JPanel filler = new JPanel();
            filler.setOpaque(false);
            grid.add(filler);
            count++;
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(grid, BorderLayout.NORTH);
        add(wrapper, BorderLayout.CENTER);
    }

    private JPanel banner(User user) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.SURFACE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 18, 18));
                g2.setColor(Theme.ACCENT);
                g2.fillRoundRect(0, 18, 4, getHeight() - 36, 4, 4);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(22, 28, 22, 28));

        JLabel title = new JLabel("Welcome to PetCare center");
        title.setFont(Theme.font(Font.BOLD, 24f));
        title.setForeground(Theme.TEXT);
        JLabel sub = new JLabel("Manage your clinic, pets and appointments.   Signed in as "
                + user.getFullName() + " (" + user.getRole() + ")");
        sub.setFont(Theme.font(Font.PLAIN, 13f));
        sub.setForeground(Theme.MUTED);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);   
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(6));
        card.add(sub);
        return card;
    }

    
    private static class Tile extends JPanel {
        private boolean hover;

        Tile(String number, String title, String subtitle, Runnable action) {
            setOpaque(false);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(BorderFactory.createEmptyBorder(20, 24, 18, 24));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            add(label(number, Font.BOLD, 12f, Theme.ACCENT));
            add(Box.createVerticalStrut(10));
            add(label(title, Font.BOLD, 20f, Theme.TEXT));
            add(Box.createVerticalStrut(4));
            add(label(subtitle, Font.PLAIN, 12f, Theme.MUTED));
            add(Box.createVerticalGlue());
        

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
                @Override public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
                @Override public void mouseClicked(MouseEvent e) { action.run(); }
            });
        }

        private static JLabel label(String text, int style, float size, Color color) {
            JLabel l = new JLabel(text);
            l.setFont(Theme.font(style, size));
            l.setForeground(color);
            l.setAlignmentX(Component.LEFT_ALIGNMENT);
            return l;
        }

        @Override public Dimension getPreferredSize() {
            return new Dimension(260, 150);
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(hover ? Theme.SURFACE_ALT : Theme.SURFACE);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 18, 18));
            g2.setColor(hover ? Theme.ACCENT : Theme.BORDER);
            g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 18, 18));
            g2.dispose();
        }
    }
}