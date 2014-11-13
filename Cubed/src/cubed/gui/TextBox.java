package cubed.gui;

import Core.Color;
import Core.GameCore;
import Core.Graphics;
import Core.InputManager;
import Core.SREObject;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;

import javax.management.MXBean;

public abstract class TextBox extends SREObject {

    private boolean show = false;
    private int width, height;
    private Color background = new Color(0, 0, 0, 180);
    String inputText = "";
    static final int LINE = 20;
    public static int MAX_CHARS = 128;
    public static int MAX_LINES = 7;
    String text[] = new String[MAX_LINES];
    String linePrompt = "";
    boolean flush = false;
    private static final char CURSOR = '_';
    private int cursorBlink = 0;
    private int cursorBlinkTime = 7;
    private int counter = 0;
    int line = 0;
    Font f;

    public TextBox(int x, int y, int width, int height, Font f) {
        super(x, y);
        this.width = 300;
        this.height = height;
        this.f = f;
    }

    @Override
    public void draw(GameCore gc, Graphics g) {
        if (show) {
            String prompt = "Terminal# ";
            if (f != null) {
                g.setFont(f);
            }
            g.setColor(Color.YELLOW);
            g.drawChars((prompt + inputText + CURSOR).toCharArray(), 0, inputText.length() + prompt.length() + cursorBlink, (int) x + 10, (int) y);
//            g.drawString((prompt + inputText + CURSOR), (int) (x + 10),(int) y);
            for (int i = 0; i < MAX_LINES; i++) {
                if (text[i] != null) {
                    g.drawChars(text[i].toCharArray(), 0, text[i].length(), (int) x + 10, (int) y + LINE * i + LINE);
//                    g.drawString(text[i], (int) x + 10, (int) y + LINE * i + LINE);
                }
            }
            if (counter > cursorBlinkTime) {
                cursorBlink = (cursorBlink == 1 ? 0 : 1);
                counter = 0;
            }
            counter++;
        }
    }

    @Override
    public void update(GameCore gc, InputManager input, int delta) {
        if (show) {
            getHits(input);
        }
        if (flush) {
            flush = false;
            flush(input);
        }
    }

    public void showHide() {
        show = !show;
        flush = true;
    }

    public void setBackgroundColor(Color color) {
        background = color;
    }

    public void getHits(InputManager input) {
        if (show) {
            input.setListenKeys(true);
            if (input.isKeyTyped(InputManager.KEY_BACKSPACE)) {
                if (input.getTyped().length() > 1) {
                    input.setTyped(input.getTyped().substring(0, input.getTyped().length() - 2));
                } else {
                    input.setTyped("");
                }
                ;
            }
            if (inputText.length() < MAX_CHARS) {
                if (input.getTyped().length() > 0) {
                    if ((input.getTyped().charAt(input.getTyped().length() - 1) >= 32) && (input.getTyped().charAt(input.getTyped().length() - 1) <= 126)) {
                        inputText = linePrompt + input.getTyped();
                    }
                } else {
                    inputText = linePrompt;
                }
            }
//            System.out.println(inputText.length());
            if (input.isKeyTyped(InputManager.KEY_ENTER) && inputText.length() > 0) {
                addText(inputText);
                enterEvent(inputText);
                inputText = "";
                input.setTyped("");
            }

        }
    }

    public void flush(InputManager input) {
        input.flush();
    }

    private void addText(String newLine) {
        String buffer[] = new String[MAX_LINES + 1];
        for (int i = 0; i < line; i++) {
            buffer[i + 1] = text[i];
        }
        buffer[0] = new String(newLine);
        if (line < MAX_LINES) {
            line++;
        }
        for (int i = 0; i < line; i++) {
            text[i] = buffer[i];
        }

    }

    public boolean putLine(String in) {
        if (in.length() < MAX_CHARS) {
            addText(in);
            return true;
        } else {
            return false;
        }
    }

    public String getLine(int line) {
        if (this.line == 0) {
            return null;
        }
        if (line <= this.line) {
            return new String(text[line]);
        } else {
            return null;
        }
    }

    public boolean setPrompt(String prompt) {
        if (prompt.length() < MAX_CHARS) {
            linePrompt = prompt;
            return true;
        } else {
            return false;
        }
    }

    public boolean visible() {
        return show;
    }

    public abstract void enterEvent(String text);
}
