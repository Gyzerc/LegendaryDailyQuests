package com.gyzer.legendaryrealms.Data.Quest.Objective;

import com.gyzer.legendaryrealms.Data.Quest.Condition.Condition;

import java.util.List;

public class QuestObjective {
        private final String id;
        private final ObjectiveType type;
        private final String value;
        private final double amount;
        private final Condition[] conditions;
        private final List<String> run;

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
