package org.apache.dolphinscheduler.api.controller.test;

import org.apache.arrow.vector.complex.writer.BaseWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TempDirTest {
    @TempDir
    static Path sharedTempDir;

    @Test
    void writeItemsToFile() throws IOException {
        Path file = sharedTempDir.resolve("test.txt");

//        new ListWriter(file).write("a", "b", "c");

        assertEquals(singletonList("a,b,c"), Files.readAllLines(file));
    }

    @Test
    void anotherTestThatUsesTheSameTempDir() {
        // use sharedTempDir
    }
    @Test
    void writeItemsToFile(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("test.txt");

//        new BaseWriter.ListWriter(file).write("a", "b", "c");

        assertEquals(singletonList("a,b,c"), Files.readAllLines(file));
    }

    @Test
    void copyFileFromSourceToTarget(@TempDir Path source, @TempDir Path target) throws IOException {
        Path sourceFile = source.resolve("test.txt");
//        new ListWriter(sourceFile).write("a", "b", "c");

        Path targetFile = Files.copy(sourceFile, target.resolve("test.txt"));

        assertNotEquals(sourceFile, targetFile);
        assertEquals(singletonList("a,b,c"), Files.readAllLines(targetFile));
    }
}
