package com.github.xachman.Where;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xach on 5/10/17.
 */
public class Where {
    private List<List<Condition>> conditions = new ArrayList<List<Condition>>();

    public Where(List<Condition> conditions) {
        this.conditions.add(conditions);
    }

    @Override
    public String toString() {
        return conditionsToString();
    }

    private String conditionsToString() {
        StringBuilder sql = new StringBuilder();
        for(List<Condition> conditionList: conditions) {
            sql.append(conditionListToString(conditionList));
        }
        return sql.toString();
    }

    private String conditionListToString(List<Condition> conditionsList) {
        StringBuilder sql = new StringBuilder();
        int count = 0;
        for(Condition condition: conditionsList) {
            if(count > 0) {
                sql.append(" AND ");
            }
            sql.append(condition.toString());
            count++;
        }
        return sql.toString();
    }
}
