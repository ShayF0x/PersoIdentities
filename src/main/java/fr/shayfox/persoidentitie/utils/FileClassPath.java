package fr.shayfox.persoidentitie.utils;

import fr.shayfox.persoidentitie.Main;

import java.net.URL;

public class FileClassPath {
    public static URL load(String file, Type type){
        URL url = Main.class.getClassLoader().getResource(type.getPath()+"/"+file);
        return url;
    }

    public enum Type{
        CSS("css"),
        FXML("fxml"),
        IMAGE("images"),
        THEME("themes");

        private String path;

        Type(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}
