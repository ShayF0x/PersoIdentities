package fr.shayfox.persoidentitie.utils;

import fr.shayfox.persoidentitie.controllers.TabEditController;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class OtherCharactere {

    public String[] ListfileList;
    public String[] ListfileString;
    public List<String> FileStringlist;
    public String FileString;
    public String FileName;
    public String MemberShipName;
    public String MemberPath;

    public HashMap<String, String> StringVar = new HashMap<>();

    public HashMap<String, List<String>> ListVar = new HashMap<>();

    public Float Level;

    public HashMap<String, List> Preferences = new HashMap<>();

    private final String appdata = System.getenv("APPDATA");




    public OtherCharactere(String name, String membership){
        FileName = name;
        MemberShipName = membership;
        StringVar.put("Name", name.split(" ")[0]);
        StringVar.put("Prename", name.split(" ")[1]);

        MemberPath = appdata+"/PersoIdentitie/fiches/(" + MemberShipName + ")" + FileName+".yml";
    }

    public void loadPropertie(){
        ListfileList = new String[]{"OthersNames", "Powers", "Default", "Quality", "Decan"};
        ListfileString = new String[]{"Level", "Gender", "MarriedName", "BornDate", "DeathDate", "BornCity", "DeathCity", "Race", "AffiliateElement", "Planet", "MainResidencePlace", "AstrologicalSign", "LunarSign", "ChineseSign", "AnimalTotem", "PhysicalAbility", "Caractere", "PhysicalCaracteristics", "Story", "StoryRole", "OtherThings", "TypeDetails", "Ascanding" };

        for (String arg:ListfileString) {
            if(!FindPropertieString(arg).isEmpty() && FindPropertieString(arg) != null)StringVar.put(arg, FindPropertieString(arg));
        }

        for (String arg:ListfileList) {
            if(!FindPropertieList(arg).isEmpty() && FindPropertieList(arg) != null)ListVar.put(arg, FindPropertieList(arg));
        }
        List<String> decan = new ArrayList(ListVar.get("Decan"));
        if(decan.size() < 1 )decan.add(0, "Aucun");
        if(decan.size() < 2)decan.add(1, "Aucun");
        if(decan.size() < 3)decan.add(2, "Aucun");
        ListVar.put("Decan", decan);

        if(!FindPropertieList("Preferences").contains("Aucun"))
            for (String str: FindPropertieList("Preferences")) {
                Preferences.put(str, FindPropertieList(str));
            }
    }

    public String FindPropertieString(String string) {
        if(FileString != null) {
            FileString = null;
        }
        FileName = FileName.trim();
        YamlFile file = new YamlFile(MemberPath);
        try {
            file.load();
            FileString = file.getString(string);
        }catch(Exception e) {
            e.printStackTrace();
        }
        if(FileString == null) {
            FileString =  "Non Renseigné";
        }
        return FileString;

    }


    public List<String> FindPropertieList(String string) {
        if(FileStringlist != null ) {
            for (int j = 0; j == FileStringlist.size(); j++) {
                FileStringlist.remove(j);
            }
        }
        FileName = FileName.trim();
        YamlFile file = new YamlFile(MemberPath);
        try {
            file.load();
            if(!file.getStringList(string).isEmpty()) {
                FileStringlist = file.getStringList(string);
            }else {
                ArrayList<String> array = new ArrayList<>();
                array.add("Aucun");
                FileStringlist = Arrays.asList("Aucun".split("[\\s]+"));

            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return FileStringlist;

    }

    public Boolean CreateFile(){

        boolean exist;
        YamlFile ymlfile = new YamlFile(MemberPath);

        String[] tab = FileName.split(" ", 2);

        if(ymlfile.exists()) {
            exist = true;
        }else {
            exist = false;

            try {
                ymlfile.createNewFile(true);
                ymlfile.load();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            ymlfile.set("Name", tab[1]);
            ymlfile.set("Prename", tab[0]);
            String[] allpathname = {"Preferences", "OthersNames", "Powers", "Default", "Quality", "Decan", "Level", "Gender", "MarriedName", "BornDate", "DeathDate", "BornCity", "DeathCity", "Race", "AffiliateElement", "Planet", "MainResidencePlace", "AstrologicalSign", "LunarSign", "ChineseSign", "AnimalTotem", "PhysicalAbility", "Caractere", "PhysicalCaracteristics", "Story", "StoryRole", "OtherThings", "TypeDetails", "Ascanding"};
            for (String string : allpathname) {
                ymlfile.addDefault(string, "");
            }
            try {
                ymlfile.save();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return exist;
    }

    public void saveproperties(TabEditController Tab){

    }


}
