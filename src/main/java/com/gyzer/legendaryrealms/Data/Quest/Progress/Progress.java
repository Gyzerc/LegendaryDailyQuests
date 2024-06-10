package com.gyzer.legendaryrealms.Data.Quest.Progress;

public class Progress {
    private String name;
    private double progress;

    public Progress(String name, double progress) {
        this.name = name;
        this.progress = progress;
    }

    public String getName() {
        return name;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }
}
