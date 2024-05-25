package com.su.FlightScheduler.Util;

import com.su.FlightScheduler.Entity.CabinCrewEntites.AttendantLanguageEntity;
import com.su.FlightScheduler.Entity.CabinCrewEntites.DishRecipeEntity;
import com.su.FlightScheduler.Entity.PilotLanguageEntity;

import java.util.ArrayList;
import java.util.List;

public class LanguageEntityListToStringConverter {

    public static String convert_pilot_language_entity_list_to_string(List<PilotLanguageEntity> pilotLanguageEntityList)
    {
        if (pilotLanguageEntityList == null || pilotLanguageEntityList.isEmpty())
        {
            return null;
        }
        List<String> languages = new ArrayList<>();
        for (PilotLanguageEntity pilotLanguageEntity: pilotLanguageEntityList)
        {
            languages.add(pilotLanguageEntity.getPilotLanguagePK().getLanguage());
        }
        return String.join(",", languages);
    }

    public static String convert_pilot_language_entity_to_string(PilotLanguageEntity pilotLanguageEntity)
    {
        if (pilotLanguageEntity == null)
        {
            return null;
        }
        return pilotLanguageEntity.getPilotLanguagePK().getLanguage();
    }

    public static String conver_string_list_to_string(List<String> languages)
    {
        if (languages == null || languages.isEmpty())
        {
            return null;
        }
        return String.join(",", languages);
    }

    public static String convert_cabin_crew_language_entity_list_to_string(List<AttendantLanguageEntity> attendantLanguageEntityList)
    {
        if (attendantLanguageEntityList == null || attendantLanguageEntityList.isEmpty())
        {
            return null;
        }
        List<String> languages = new ArrayList<>();
        for (AttendantLanguageEntity attendantLanguageEntity: attendantLanguageEntityList)
        {
            languages.add(attendantLanguageEntity.getAttendantLanguagePK().getLanguage());
        }
        return String.join(",", languages);
    }

    public static String convert_cabin_crew_dish_recipe_entity_list_to_string(List<DishRecipeEntity> dishRecipeEntityList)
    {
        if (dishRecipeEntityList == null || dishRecipeEntityList.isEmpty())
        {
            return null;
        }
        List<String> recipes = new ArrayList<>();
        for (DishRecipeEntity dishRecipeEntity: dishRecipeEntityList)
        {
            recipes.add(dishRecipeEntity.getDishRecipePK().getRecipe());
        }
        return String.join(",",recipes);
    }
}
