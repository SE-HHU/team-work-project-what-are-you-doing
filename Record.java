package storages;

import java.util.ArrayList;
import java.util.LinkedList;

public class Record {
    public static ArrayList<String> wrongArithmetics = new ArrayList<>();
    public static LinkedList<Double> accuracyRate = new LinkedList<>(){{
        add(0.0);
        add(0.0);
        add(0.0);
        add(0.0);
        add(0.0);
    }};
}
