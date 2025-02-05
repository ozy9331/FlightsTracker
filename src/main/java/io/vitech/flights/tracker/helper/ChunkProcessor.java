package io.vitech.flights.tracker.helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChunkProcessor {

    public static <T> List<Set<T>> chunkify(Set<T> set, int chunkSize) {
        List<Set<T>> chunks = new ArrayList<>();
        Set<T> currentChunk = new HashSet<>();
        int count = 0;

        for (T element : set) {
            currentChunk.add(element);
            count++;
            if (count % chunkSize == 0) {
                chunks.add(currentChunk);
                currentChunk = new HashSet<>();
            }
        }

        if (!currentChunk.isEmpty()) {
            chunks.add(currentChunk);
        }

        return chunks;
    }
}
