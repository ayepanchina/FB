package com.example.app;

/**
 * Created by Вадим on 20.05.14.
 */
public class ExpensePlanValue {
    float value;
    float prevValue;
    boolean edited;
    private long id;

    public ExpensePlanValue(long id, float value) {
        this.id = id;
        this.value = value;
        prevValue = value;
        this.edited = false;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public boolean isEdited() {
        return (value != prevValue);
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public float getPrevValue() {
        return prevValue;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
