package com.handytrip.Structures;

public class FilterData {
    boolean all;
    boolean done;
    boolean history;
    boolean experience;
    boolean sight;

    public FilterData() {
        this.all = false;
        this.done = false;
        this.history = true;
        this.experience = true;
        this.sight = true;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
        if(all){
            setDone(true);
            setHistory(true);
            setExperience(true);
            setSight(true);
        } else{
            setDone(false);
            setHistory(false);
            setExperience(false);
            setSight(false);
        }
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
        if(!done && isAll()){
            setAll(false);
        }
    }

    public boolean isHistory() {
        return history;
    }

    public void setHistory(boolean history) {
        this.history = history;
        if(!history && isAll()){
            setAll(false);
        }
    }

    public boolean isExperience() {
        return experience;
    }

    public void setExperience(boolean experience) {
        this.experience = experience;
        if(!experience && isAll()){
            setAll(false);
        }
    }

    public boolean isSight() {
        return sight;
    }

    public void setSight(boolean sight) {
        this.sight = sight;
        if(!sight && isAll()){
            setAll(false);
        }
    }
}
