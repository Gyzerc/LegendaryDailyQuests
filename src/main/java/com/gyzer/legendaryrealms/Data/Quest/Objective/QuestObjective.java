package com.gyzer.legendaryrealms.Data.Quest.Objective;

import com.gyzer.legendaryrealms.Data.Quest.Condition.Condition;

import java.util.List;

public class QuestObjective {
        private String id;
        private ObjectiveType type;
        private String value;
        private double amount;
        private Condition[] conditions;
        private List<String> run;

        public QuestObjective(String id,ObjectiveType type, String value, double amount,List<String> run,Condition[] conditions) {
            this.id = id;
            this.type = type;
            this.value = value;
            this.amount = amount;
            this.run = run;
            this.conditions = conditions;
        }

    public String getId() {
        return id;
    }

    public List<String> getRun() {
            return run;
        }

        public Condition[] getConditions() {
            return conditions;
        }

        public ObjectiveType getType() {
            return type;
        }

        public String getValue() {
            return value;
        }

        public double getAmount() {
            return amount;
        }

        public boolean isIntAmount(){
            return type.isUseInt();
        }

}
