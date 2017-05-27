package com.github.xachman.Where;

import com.github.xachman.Value;

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

    private String conditionsToString(boolean isPrepared) {
        StringBuilder sql = new StringBuilder();
        for(List<Condition> conditionList: conditions) {
            sql.append(conditionListToString(conditionList, isPrepared));
        }
        return sql.toString();
    }

    private String conditionsToString() {
        return conditionsToString(false);
    }

    private String conditionListToString(List<Condition> conditionsList, boolean isPrepared) {
        StringBuilder sql = new StringBuilder();
        int count = 0;
        for(Condition condition: conditionsList) {
            if(count > 0) {
                sql.append(" AND ");
            }
            if(!isPrepared) {
                sql.append(condition.toString());
            }else{
                sql.append(condition.toPreparedString());
            }
            count++;
        }
        return sql.toString();
    }

    public String toPreparedString() {
        return conditionsToString(true);
    }

    public List<Value> values() {
        List<Value> values = new ArrayList<Value>();

        for(List<Condition> listConditions : conditions) {
            for(Condition condition: listConditions) {
                values.add(condition.getValue());
            }
        }

        return values;
    }
}
