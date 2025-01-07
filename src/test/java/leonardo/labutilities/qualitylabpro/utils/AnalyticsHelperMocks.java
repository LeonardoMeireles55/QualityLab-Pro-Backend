package leonardo.labutilities.qualitylabpro.utils;

import leonardo.labutilities.qualitylabpro.dtos.analytics.GenericValuesRecord;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class AnalyticsHelperMocks {
        public static GenericValuesRecord createSampleRecord() {
                return new GenericValuesRecord(1L, LocalDateTime.of(2024, 12, 16, 7, 53), "0774693",
                                "608384", "ALB2", "PCCC1", 3.45, 3.35, 0.2, "g/dL", "Average",
                                "Approved");
        }

        public static List<GenericValuesRecord> createSampleRecordList() {
                List<GenericValuesRecord> records = new ArrayList<>();

                // Normal level records
                records.add(new GenericValuesRecord(1L, LocalDateTime.of(2024, 12, 16, 7, 53),
                                "0774693", "608384", "ALB2", "PCCC1", 3.45, 3.35, 0.2, "g/dL",
                                "Average", "Approved"));

                records.add(new GenericValuesRecord(2L, LocalDateTime.of(2024, 12, 16, 7, 53),
                                "0774707", "704991", "ALB2", "PCCC2", 4.85, 4.94, 0.3, "g/dL",
                                "Average", "Approved"));

                // Failed records
                records.add(new GenericValuesRecord(3L, LocalDateTime.of(2024, 12, 16, 8, 30),
                                "0774707", "704991", "ALTL", "PCCC2", 82.84, 110.0, 7.0, "U/L",
                                "-3s", "Failed"));

                // Different analyte records
                records.add(new GenericValuesRecord(4L, LocalDateTime.of(2024, 12, 16, 7, 54),
                                "0774707", "704991", "AMYL2", "PCCC2", 188.64, 187.0, 11.0, "U/L",
                                "Average", "Approved"));

                return records;
        }

        public static List<GenericValuesRecord> createMultiAnalyteRecordList() {
                List<GenericValuesRecord> records = new ArrayList<>();

                // ALB2 record
                records.add(new GenericValuesRecord(1L, LocalDateTime.of(2024, 12, 16, 7, 53),
                                "0774693", "608384", "ALB2", "PCCC1", 3.45, 3.35, 0.2, "g/dL",
                                "Average", "Approved"));

                // ALTL record
                records.add(new GenericValuesRecord(2L, LocalDateTime.of(2024, 12, 16, 9, 02),
                                "0774693", "608384", "ALTL", "PCCC1", 48.1, 46.8, 2.8, "U/L",
                                "Average", "Approved"));

                // CHOL2 record
                records.add(new GenericValuesRecord(3L, LocalDateTime.of(2024, 12, 16, 7, 56),
                                "0774693", "608384", "CHOL2", "PCCC1", 101.69, 104.0, 5.0, "mg/dL",
                                "Average", "Approved"));

                return records;
        }

        public static List<GenericValuesRecord> createDateRangeRecords() {
                List<GenericValuesRecord> records = new ArrayList<>();
                LocalDateTime baseDate = LocalDateTime.of(2024, 12, 16, 7, 53);

                // Add records across multiple timepoints
                for (int i = 0; i < 5; i++) {
                        records.add(new GenericValuesRecord((long) i + 1,
                                        baseDate.plusMinutes(i * 30), "0774693", "608384", "ALB2",
                                        "PCCC1", 3.45, 3.35, 0.2, "g/dL", "Average", "Approved"));
                }

                return records;
        }

        public static GenericValuesRecord createOutOfRangeRecord() {
                return new GenericValuesRecord(1L, LocalDateTime.of(2024, 12, 16, 8, 30), "0774707",
                                "704991", "ALTL", "PCCC2", 82.84, 110.0, 7.0, "U/L", "-3s",
                                "Failed");
        }
}
