package fr.shayfox.persoidentitie.utils;

import fr.shayfox.persoidentitie.controllers.OtherIdentitieTabEditController;
import fr.shayfox.persoidentitie.controllers.TabEditController;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;
import java.util.*;

public class Charactere {

    public String[] ListfileList;
    public String[] ListfileString;
    public List<String> FileStringlist;
    public String FileString;
    public String FileName;

    public HashMap<String, String> StringVar = new HashMap<>();

    public HashMap<String, List<String>> ListVar = new HashMap<>();

    public Float Level;

    public HashMap<String, List> Preferences = new HashMap<>();
    public HashMap<String, List> NewPreferences = new HashMap<>();

    private final String APPDATA = System.getenv("APPDATA");

    private String[] tab;




    public Charactere(String name){
        FileName = name;
        if(name.contains("(")){
            tab = name.split("\\)")[1].split(" ");
        }else {
            tab = name.split(" ");
        }

        String[] tab2 = {tab[0], " "};
        if(tab.length < 2)tab = tab2;
        if(tab.length >=3) {
            ArrayList<String> array = new ArrayList<String>();
            for (String string : tab) {
                array.add(string);
            }
            while (array.size() >=3) {
                array.add(1, array.get(1)+" "+array.get(2));
                array.remove(3);
                array.remove(2);
            }
            tab = array.toArray(new String[array.size()]);
        }

            StringVar.put("Name", tab[1]);
            StringVar.put("Prename", tab[0]);



    }

    public void loadPropertie(){
        ListfileList = new String[]{"OthersNames", "OtherIdentities", "Powers", "Default", "Quality", "Families", "Tags"};
        ListfileString = new String[]{"Level", "Gender", "MarriedName", "BornDate", "DeathDate", "BornCity", "DeathCity", "Race", "AffiliateElement", "Planet", "MainResidencePlace", "AstrologicalSign", "LunarSign", "ChineseSign", "AnimalTotem", "PhysicalAbility", "Caractere", "PhysicalCaracteristics", "Story", "StoryRole", "OtherThings", "TypeDetails", "Ascanding", "Decan" };

        for (String arg:ListfileString) {
            StringVar.put(arg, "Non Renseigné");
            if(!findPropertieString(arg).isEmpty() && findPropertieString(arg) != null)StringVar.put(arg, findPropertieString(arg));
        }

        for (String arg:ListfileList) {
            if(!findPropertieList(arg).isEmpty() && findPropertieList(arg) != null)ListVar.put(arg, findPropertieList(arg));
        }

        if(!findPropertieList("Preferences").contains("Aucun"))
            for (String str: findPropertieList("Preferences")) {
                Preferences.put(str, findPropertieList(str));
                NewPreferences.put(str, findPropertieList(str));
            }
    }

    private String findPropertieString(String string) {
        if(FileString != null) {
            FileString = null;
        }
        FileName = FileName.trim();
        YamlFile file = new YamlFile(APPDATA +"/PersoIdentitie/fiches/"+FileName+".yml");
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


    private List<String> findPropertieList(String string) {
        if(FileStringlist != null ) {
            for (int j = 0; j == FileStringlist.size(); j++) {
                FileStringlist.remove(j);
            }
        }
        FileName = FileName.trim();
        YamlFile file = new YamlFile(APPDATA +"/PersoIdentitie/fiches/"+FileName+".yml");
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
    private void savePropertieString(String arg, String PropertieName){
        if(arg == null)return;
        String argument = arg.trim();
        if(argument == null)return;
        if(argument.equalsIgnoreCase(StringVar.get(PropertieName)))return;
        YamlFile ymlFile = new YamlFile(APPDATA +"/PersoIdentitie/fiches/"+FileName+".yml");
        try {
            ymlFile.load();
            if(argument.equalsIgnoreCase("Non Renseigné")){
                ymlFile.set(PropertieName, "");
            }else{
                ymlFile.set(PropertieName, argument);
            }
            ymlFile.save();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void savePropertieList(List args, String PropertieName){
        if (args == null)return;
        if(args.equals(ListVar.get(PropertieName)))return;
        YamlFile ymlfFile = new YamlFile(APPDATA +"/PersoIdentitie/fiches/"+FileName+".yml");
        try {
            ymlfFile.load();
            if(args.contains("Aucun")){
                ymlfFile.set(PropertieName, "");
            }else{
                ymlfFile.set(PropertieName, args);
            }
            ymlfFile.save();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public Boolean CreateFile(){

        boolean exist;
        YamlFile ymlfile = new YamlFile(APPDATA +"/PersoIdentitie/fiches/"+FileName+".yml");
        if(ymlfile.exists()) {
            exist = true;
        }else {
            exist = false;

            try {
                ymlfile.createNewFile(true);
                ymlfile.load();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            ymlfile.set("Name", tab[1]);
            ymlfile.set("Prename", tab[0]);
            ListfileList = new String[]{"OthersNames", "OtherIdentities", "Powers", "Default", "Quality", "Families", "Tags", "Preferences"};
            ListfileString = new String[]{"Level", "Gender", "MarriedName", "BornDate", "DeathDate", "BornCity", "DeathCity","Decan", "Race", "AffiliateElement", "Planet", "MainResidencePlace", "AstrologicalSign", "LunarSign", "ChineseSign", "AnimalTotem", "PhysicalAbility", "Caractere", "PhysicalCaracteristics", "Story", "StoryRole", "OtherThings", "TypeDetails", "Ascanding" };

            for (String arg:ListfileString) {
                ymlfile.addDefault(arg, "Non Renseigné");
                if(arg.equalsIgnoreCase("BornDate") || arg.equalsIgnoreCase("DeathDate"))ymlfile.addDefault(arg, "dd/MM/yyyy");
            }
            for (String arg:ListfileList) {
                ymlfile.addDefault(arg, "Aucun");
            }

            try {
                ymlfile.save();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        return exist;
    }

    public void saveProperties(TabEditController Tab){
        List<String> tags = Tab.TagsView.getChips();
        tags.remove("Aucun");
        savePropertieString(Tab.MarriedNameLabel.getText(), "MarriedName");
        savePropertieString(Tab.GenreComboBox.getSelectionModel().getSelectedItem(), "Gender");
        savePropertieString(Tab.LevelComboBox.getSelectionModel().getSelectedItem(), "Level");
        savePropertieString(Tab.DecanComboBox.getSelectionModel().getSelectedItem(), "Decan");
        savePropertieString(Tab.BornDateLabel.getText(), "BornDate");
        savePropertieString(Tab.DeathDateLabel.getText(), "DeathDate");
        savePropertieString(Tab.BornCityLabel.getText(), "BornCity");
        savePropertieString(Tab.DeathCityLabel.getText(), "DeathCity");
        savePropertieString(Tab.RaceLabel.getText(), "Race");
        savePropertieString(Tab.AffiliateElementLabel.getText(), "AffiliateElement");
        savePropertieString(Tab.PlanetLabel.getText(), "Planet");
        savePropertieString(Tab.MainResidencePlaceLabel.getText(), "MainResidencePlace");
        savePropertieString(Tab.AstrologicalSignLabel.getText(), "AstrologicalSign");
        savePropertieString(Tab.LunarSignLabel.getText(), "LunarSign");
        savePropertieString(Tab.ChineseSignLabel.getText(), "ChineseSign");
        savePropertieString(Tab.AnimalTotemLabel.getText(), "AnimalTotem");
        savePropertieString(Tab.PhysicalAbilityArena.getText(), "PhysicalAbility");
        savePropertieString(Tab.CaractereArena.getText(), "Caractere");
        savePropertieString(Tab.PhysicalCaracteristicsArena.getText(), "PhysicalCaracteristics");
        savePropertieString(Tab.StoryArena.getText(), "Story");
        savePropertieString(Tab.StoryRoleArena.getText(), "StoryRole");
        savePropertieString(Tab.OtherThingsArena.getText(), "OtherThings");
        savePropertieString(Tab.TypeDetailsArena.getText(), "TypeDetails");
        savePropertieString(Tab.AscendingLabel.getText(), "Ascanding");
        savePropertieString(Tab.PrenameLabel.getText(), "Prename");
        savePropertieString(Tab.NameLabel.getText(), "Name");

        savePropertieList(Tab.OtherNameList.getItems(), "OthersNames");
        savePropertieList(Tab.OtherIdentitiesList.getItems(), "OtherIdentities");
        savePropertieList(Tab.PowersList.getItems(), "Powers");
        savePropertieList(Tab.DefaultList.getItems(), "Default");
        savePropertieList(Tab.QualityList.getItems(), "Quality");
        savePropertieList(Tab.FamiliesList.getItems(), "Families");
        savePropertieList(tags, "Tags");


        ArrayList<String> preferencesKeys = new ArrayList<>();
        for (Map.Entry me:Preferences.entrySet()){
            preferencesKeys.add(me.getKey().toString());
        }
        savePropertieList(preferencesKeys, "Preferences");
        for (Map.Entry me:Preferences.entrySet()) {
            savePropertieList((List) me.getValue(), me.getKey().toString());
        }

        YamlFile yamlFile = new YamlFile(APPDATA +"/PersoIdentitie/Stockage/etiquette.yml");
        try {
            yamlFile.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String str:yamlFile.getStringList("listettiquette")) {
            ArrayList array = new ArrayList(yamlFile.getStringList(str));
            if(array.contains(FileName))array.remove(FileName);
        }
        yamlFile.getStringList("\n-----------------\n");
        for (String str:tags) {
            List array = new ArrayList(yamlFile.getStringList(str));
            array.add(FileName);
            yamlFile.set(str, array);
        }
        try {
            yamlFile.save();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void saveProperties(OtherIdentitieTabEditController Tab){
        savePropertieString(Tab.MarriedNameLabel.getText(), "MarriedName");
        savePropertieString(Tab.GenreComboBox.getSelectionModel().getSelectedItem(), "Gender");
        savePropertieString(Tab.LevelComboBox.getSelectionModel().getSelectedItem(), "Level");
        savePropertieString(Tab.DecanComboBox.getSelectionModel().getSelectedItem(), "Decan");
        savePropertieString(Tab.BornDateLabel.getText(), "BornDate");
        savePropertieString(Tab.DeathDateLabel.getText(), "DeathDate");
        savePropertieString(Tab.BornCityLabel.getText(), "BornCity");
        savePropertieString(Tab.DeathCityLabel.getText(), "DeathCity");
        savePropertieString(Tab.RaceLabel.getText(), "Race");
        savePropertieString(Tab.AffiliateElementLabel.getText(), "AffiliateElement");
        savePropertieString(Tab.PlanetLabel.getText(), "Planet");
        savePropertieString(Tab.MainResidencePlaceLabel.getText(), "MainResidencePlace");
        savePropertieString(Tab.AstrologicalSignLabel.getText(), "AstrologicalSign");
        savePropertieString(Tab.LunarSignLabel.getText(), "LunarSign");
        savePropertieString(Tab.ChineseSignLabel.getText(), "ChineseSign");
        savePropertieString(Tab.AnimalTotemLabel.getText(), "AnimalTotem");
        savePropertieString(Tab.PhysicalAbilityArena.getText(), "PhysicalAbility");
        savePropertieString(Tab.CaractereArena.getText(), "Caractere");
        savePropertieString(Tab.PhysicalCaracteristicsArena.getText(), "PhysicalCaracteristics");
        savePropertieString(Tab.StoryArena.getText(), "Story");
        savePropertieString(Tab.StoryRoleArena.getText(), "StoryRole");
        savePropertieString(Tab.OtherThingsArena.getText(), "OtherThings");
        savePropertieString(Tab.TypeDetailsArena.getText(), "TypeDetails");
        savePropertieString(Tab.AscendingLabel.getText(), "Ascanding");
        if((Tab.PrenameLabel.getText() + " " + Tab.NameLabel.getText()).matches("^[a-zA-Z]+[ \\-']?[[a-z]+[ \\-']?]*[a-zA-Z \\-]+$")) {
            savePropertieString(Tab.PrenameLabel.getText(), "Prename");
            savePropertieString(Tab.NameLabel.getText(), "Name");
        }

        savePropertieList(Tab.OtherNameList.getItems(), "OthersNames");
        savePropertieList(Tab.PowersList.getItems(), "Powers");
        savePropertieList(Tab.DefaultList.getItems(), "Default");
        savePropertieList(Tab.QualityList.getItems(), "Quality");
        savePropertieList(Tab.FamiliesList.getItems(), "Families");


        ArrayList<String> Preferences_Keys = new ArrayList<>();
        for (Map.Entry me:Preferences.entrySet()){
            Preferences_Keys.add(me.getKey().toString());
        }
        savePropertieList(Preferences_Keys, "Preferences");
        for (Map.Entry me:Preferences.entrySet()) {
            savePropertieList((List) me.getValue(), me.getKey().toString());
        }
    }


}
