package de.twisted.examshare.util.interfaces;

public abstract class ExamModel {

    public abstract int getId();

    public enum ModelType {
        COMMENT,
        REPORT,
        TASK
    }

}
