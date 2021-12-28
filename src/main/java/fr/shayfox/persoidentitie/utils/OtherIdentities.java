package fr.shayfox.persoidentitie.utils;

import java.util.HashMap;
import java.util.List;

public class OtherIdentities {

    public String[] ListfileList;
    public String[] ListfileString;
    public List<String> FileStringlist;
    public String FileString;
    public String FileName;

    public HashMap<String, String> StringVar = new HashMap<>();

    public HashMap<String, List<String>> ListVar = new HashMap<>();

    private final String appdata = System.getenv("APPDATA");

    public OtherIdentities(String name){
        FileName = name;
        StringVar.put("Name", name.split(" ")[0]);
        StringVar.put("Prename", name.split(" ")[1]);
    }

    public void initdata(){
        ListfileList = new String[]{"OthersNames", "Powers", "Default", "Quality", "Families", "Decan", "Tags"};
        ListfileString = new String[]{"Level", "Gender", "MarriedName", "BornDate", "DeathDate", "BornCity", "DeathCity", "Race", "AffiliateElement", "Planet", "MainResidencePlace", "AstrologicalSign", "LunarSign", "ChineseSign", "AnimalTotem", "PhysicalAbility", "Caractere", "PhysicalCaracteristics", "Story", "StoryRole", "OtherThings", "TypeDetails", "Ascanding" };

    }
}
