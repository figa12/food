package aau.sw8.recipe;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by jeria_000 on 0007 7. maj.
 */
public class Settings {

    private static final String FILE_PATH = "settings.txt";

    private Context context;

    private boolean navigationDrawerDisplayed = false; // default value

    public Settings(Context context) {
        this.context = context;
        this.loadSettings();
    }

    public boolean isNavigationDrawerDisplayed() {
        return this.navigationDrawerDisplayed;
    }

    public void setNavigationDrawerDisplayed(boolean navigationDrawerDisplayed) {
        this.navigationDrawerDisplayed = navigationDrawerDisplayed;
    }

    public void loadSettings() {
        String file = this.readFile();

        if (file == null) {
            this.saveSettings();
        } else if (file.equals("true")) {
            this.navigationDrawerDisplayed = true;
        }
    }

    public void saveSettings() {
        //TODO use some kind of serialization instead
        this.writeFile(Boolean.toString(this.navigationDrawerDisplayed));
    }

    private String readFile() {
        FileInputStream fileInputStream = null;
        StringWriter stringWriter = new StringWriter(1024);

        try {
            fileInputStream = this.context.openFileInput(Settings.FILE_PATH);

            int content;
            while ((content = fileInputStream.read()) != -1) {
                // convert to char and append to string
                stringWriter.append((char) content);
            }
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stringWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return stringWriter.toString();
    }

    private void writeFile(String fileContents) {
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = this.context.openFileOutput(Settings.FILE_PATH, Context.MODE_PRIVATE);
            fileOutputStream.write(fileContents.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
