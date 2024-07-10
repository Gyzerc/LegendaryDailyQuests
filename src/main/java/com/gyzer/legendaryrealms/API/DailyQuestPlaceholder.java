package com.gyzer.legendaryrealms.API;

import com.gyzer.legendaryrealms.Configurations.Language;
import com.gyzer.legendaryrealms.Data.Quest.Categorize;
import com.gyzer.legendaryrealms.Data.Quest.Quest;
import com.gyzer.legendaryrealms.Data.Quest.QuestRarity;
import com.gyzer.legendaryrealms.Data.User.PlayerData;
import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DailyQuestPlaceholder extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "LegendaryDailyQuests";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Gyzer";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.8";
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        return Arrays.asList("refresh_[Categorize]");
    }

    private static LegendaryDailyQuests legendaryDailyQuests = LegendaryDailyQuests.getLegendaryDailyQuests();
    private static Language language = legendaryDailyQuests.getConfigurationsManager().getLanguage();
    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        PlayerData data = LegendaryDailyQuests.getLegendaryDailyQuests().getPlayerDataManager().getPlayerData(player.getUniqueId());
        if (params.startsWith("refresh_")) {
            String categorize = params.replace("refresh_","");
            if (LegendaryDailyQuests.getLegendaryDailyQuests().getCategorizesManager().getCategorize(categorize) != null) {
                return String.valueOf(data.getRefresh().getOrDefault(categorize,0));
            }
            return "0";
        } else if (params.startsWith("amount_")) {
            String[] args = params.split("_");
            int amount = 0;
            if (args.length == 3) {
                Categorize categorize = legendaryDailyQuests.getCategorizesManager().getCategorize(args[1]);
                if (categorize != null) {
                    QuestRarity rarity = legendaryDailyQuests.getQuestRaritiesManager().getRarity(args[2]);
                    if (rarity != null) {
                        List<String> quests = data.getQuests().getOrDefault(args[1],new LinkedList<>());
                        if (!quests.isEmpty()) {
                            amount = quests.stream().filter(id -> {
                                Quest quest = legendaryDailyQuests.getQuestsManager().getQuest(id);
                                if (quest != null) {
                                    return quest.getRarity().getId().equals(args[2]);
                                }
                                return false;
                            }).collect(Collectors.toList()).size() ;
                        }
                    }
                }
            }
            return String.valueOf(amount);
        }
        else if (params.startsWith("top_me_")) {
            String categorize = params.replace("top_me_","");
            if (legendaryDailyQuests.getCategorizesManager().getCategorize(categorize) != null) {
                return ""+ (1 + legendaryDailyQuests.getSystemDataManager().getCompletedData().getTop(categorize,player.getUniqueId()).orElse(-1));
            }
        } else if (params.startsWith("top_")) {
            String arg = params.replace("top_","");
            String numberArg = arg.substring(arg.lastIndexOf("_") + 1);
            if (isInteger(numberArg)) {
                String categorize = arg.replace("_"+numberArg,"");
                if (!categorize.isEmpty()) {
                    if (LegendaryDailyQuests.getLegendaryDailyQuests().getCategorizesManager().getCategorize(categorize) != null) {
                        UUID uuid = LegendaryDailyQuests.getLegendaryDailyQuests().getSystemDataManager().getCompletedData().getUUID(categorize,Integer.parseInt(numberArg) - 1).orElse(null);
                        if (uuid != null) {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                            return offlinePlayer.getName();
                        }
                    }
                }
            }
            return language.default_no_player;
        }
        return "";
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (IllegalArgumentException e){
            return false;
        }
    }
}
