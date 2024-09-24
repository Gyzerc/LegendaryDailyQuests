package com.gyzer.legendaryrealms.Data.System;

import com.gyzer.legendaryrealms.LegendaryDailyQuests;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;

public class CompletedData {
    private final HashMap<String, LinkedList<UUID>> completed;

    public CompletedData(HashMap<String, LinkedList<UUID>> completed) {
        this.completed = completed;
    }
    public Optional<UUID> getUUID(String categorize,int number) {
        LinkedList<UUID> uuids = completed.getOrDefault(categorize,new LinkedList<>());
        return uuids.size() > number ? Optional.of(uuids.get(number)) : Optional.empty();
    }

    public void addUUID(String categorize,UUID uuid) {
        LinkedList<UUID> uuids = completed.getOrDefault(categorize,new LinkedList<>());
        if (uuids.contains(uuids)) {
            return;
        }
        uuids.add(uuid);
        completed.put(categorize,uuids);
    }

    public HashMap<String, LinkedList<UUID>> getCompleted() {
        return completed;
    }

    public LinkedList<UUID> getUUIDs(String categorize) {
        return completed.getOrDefault(categorize,new LinkedList<>());
    }

    public void clear(String categorize) {
        completed.remove(categorize);
    }
    public void update() {
        LegendaryDailyQuests.getLegendaryDailyQuests().getSystemDataManager().update(this);
    }

    public Optional<Integer> getTop(String categorize,UUID uuid) {
        return getUUIDs(categorize).contains(uuid) ? Optional.of(getUUIDs(categorize).indexOf(uuid)) : Optional.empty();
    }
}
