package com.superxc.chineseIdioms.model;

import com.superxc.chineseIdioms.util.Util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LocaleClearStage {
    private static final String fileName = "localeClearStage.cache";
    private static Map<Integer, Integer> map = new HashMap<>();

    public static final String DELIMITER = "delimiter";

    static {
        File file = new File(fileName);

        if (file.exists()) {
            String content = Util.fileReadToString(fileName);
            String[] rows = content.split("\n");

            for (String row : rows) {
                String[] split = row.split(DELIMITER);
                if (split.length != 2) {
                    continue;
                }
                map.put(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            }
        }
    }
    public static Map<Integer, Integer> get() {
        return map;
    }

    public static void add(int stageId, int starCount) {
        map.put(stageId, starCount);
        updateToFile();
    }

    private static void updateToFile() {
        StringBuilder sb = new StringBuilder();

        for (Integer key : map.keySet()) {
            sb.append(key + DELIMITER + map.get(key) + "\n");
        }
        Util.WriteStringToFile(fileName, sb.toString());
    }

    public static void update(int stageId, int starCount) {
        add(stageId, starCount);
    }
}
