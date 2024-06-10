package com.gyzer.legendaryrealms.Data.Quest.Progress;

import com.gyzer.legendaryrealms.Utils.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ProgressData {
    private HashMap<String,Progress[]> progress;

    public ProgressData(HashMap<String, Progress[]> progress) {
        this.progress = progress;
    }

    public HashMap<String, Progress[]> getProgress() {
        return progress;
    }

    public double getProgress(String quest,String goal){
        Progress[] progresses = progress.getOrDefault(quest,new Progress[]{});
        for (Progress progress : progresses) {
            if (progress.getName().equals(goal)) {
                return progress.getProgress();
            }
        }
        return 0;
    }
    public void setProgress(String quest,String goal,double progress){
        List<Progress> progresses = new ArrayList<>(Arrays.stream(this.progress.getOrDefault(quest, new Progress[]{})).collect(Collectors.toList()));
        boolean add = false;
        for (Progress pro : progresses){
            if (pro.getName().equals(goal)){
                pro.setProgress(progress);
                add = true;
                break;
            }
        }
        if (!add){
            progresses.add(new Progress(goal,progress));
            this.progress.put(quest,progresses.toArray(new Progress[]{}));
        }
    }

    public void setProgress(HashMap<String, Progress[]> progress) {
        this.progress = progress;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        List<String> list_quest = new ArrayList<>();
        if (!progress.isEmpty()){
            for (Map.Entry<String,Progress[]> entry:progress.entrySet()){
                String quest = entry.getKey();
                if (!list_quest.contains(quest)) {
                    List<String> list_goal = new ArrayList<>();
                    builder.append("{").append(quest).append("~");
                    for (Progress pro : entry.getValue()) {
                        String goal = pro.getName();
                        if (!list_goal.contains(goal)) {
                            builder.append(goal).append(":").append(pro.getProgress()).append(";");
                            list_goal.add(goal);
                        }
                    }
                    builder.append("}");
                    list_quest.add(quest);
                }
            }
        }
        return builder.toString();
    }
}
