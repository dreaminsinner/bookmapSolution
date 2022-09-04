import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class Main {

    static final File INPUT_FILE = new File("input.txt");
    static final File OUTPUT_FILE = new File("output.txt");

    static Map<Integer, Integer> VALUES_BID = new HashMap<>();

    static Map<Integer, Integer> VALUES_ASK = new HashMap<>();

    private static final Consumer<List<String>> u = values -> {
        int mbKey = Integer.parseInt(values.get(1));
        if (mbKey < 1) return;
        int mbValue = Integer.parseInt(values.get(2));
        if (mbValue < 0) return;
        if (values.get(3).equals("ask")) {
            VALUES_ASK.put(mbKey, mbValue);
        } else if (values.get(3).equals("bid")) {
            VALUES_BID.put(mbKey, mbValue);
        }
    };

    private static final Consumer<List<String>> q = values -> {
        switch (values.get(1)) {
            case "best_bid" -> {
                if (!VALUES_BID.isEmpty()) {
                    int maxBid = Collections.max(VALUES_BID.keySet());
                    write(maxBid + "," + VALUES_BID.get(maxBid) + System.lineSeparator());
                }
            }
            case "best_ask" -> {
                if(!VALUES_ASK.isEmpty()){
                    int minAsk = Collections.min(VALUES_ASK.keySet());
                    write(minAsk + "," + VALUES_ASK.get(minAsk) + System.lineSeparator());
                }
            }
            case "size" -> {
                int value = Integer.parseInt(values.get(2));
                if (VALUES_ASK.containsKey(value)) write(VALUES_ASK.get(value).toString() + System.lineSeparator());
                else if (VALUES_BID.containsKey(value))
                    write(VALUES_BID.get(value).toString() + System.lineSeparator());
            }
        }
    };

    private static final Consumer<List<String>> o = values -> {
        if (values.get(1).equals("buy")) {
            int minAsk = Collections.min(VALUES_ASK.keySet());
            VALUES_ASK.put(minAsk, VALUES_ASK.get(minAsk) - Integer.parseInt(values.get(2)));
        } else if (values.get(1).equals("sell")) {
            int maxBid = Collections.max(VALUES_BID.keySet());
            VALUES_BID.put(maxBid, VALUES_BID.get(maxBid) - Integer.parseInt(values.get(2)));
        }
    };
    static Map<String, Consumer<List<String>>> ACTIONS = Map.of("u", u, "q", q, "o", o);

    public static void main(String[] args) {
        calculateBookMap();
    }

    private static List<List<String>> readInputInstructions() {
        List<List<String>> values = new ArrayList<>();
        try (Scanner scanner = new Scanner(INPUT_FILE)) {
            while (scanner.hasNextLine()) {
                values.add(List.of(scanner.nextLine().split(",")));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return values;
    }

    private static void write(String str) {
        try (FileWriter writer = new FileWriter(OUTPUT_FILE, true)) {
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void calculateBookMap() {
        readInputInstructions().forEach(value -> ACTIONS.get(value.get(0)).accept(value));
    }
}

