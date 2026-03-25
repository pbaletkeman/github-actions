package com.quizengine.dao;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseManagerTest {

    @TempDir
    Path tempDir;

    @Test
    void create_andInitSchema_succeeds() throws Exception {
        Path dbFile = tempDir.resolve("test.db");
        try (DatabaseManager dm = DatabaseManager.create(dbFile.toString())) {
            assertDoesNotThrow(dm::initSchema);
        }
    }

    @Test
    void getConnection_returnsValidConnection() throws Exception {
        Path dbFile = tempDir.resolve("test2.db");
        try (DatabaseManager dm = DatabaseManager.create(dbFile.toString())) {
            dm.initSchema();
            try (Connection conn = dm.getConnection()) {
                assertNotNull(conn);
                assertFalse(conn.isClosed());
            }
        }
    }

    @Test
    void initSchema_createsQuestionsTable() throws Exception {
        Path dbFile = tempDir.resolve("test3.db");
        try (DatabaseManager dm = DatabaseManager.create(dbFile.toString())) {
            dm.initSchema();
            try (Connection conn = dm.getConnection();
                 var rs = conn.getMetaData().getTables(null, null, "questions", null)) {
                assertTrue(rs.next(), "questions table should exist");
            }
        }
    }
}
