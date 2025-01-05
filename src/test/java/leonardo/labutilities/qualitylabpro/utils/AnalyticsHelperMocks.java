package leonardo.labutilities.qualitylabpro.utils;

import leonardo.labutilities.qualitylabpro.dtos.analytics.GenericValuesRecord;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class AnalyticsHelperMocks {
    public static GenericValuesRecord createSampleRecord() {
        return new GenericValuesRecord(
                1L,
                LocalDateTime.of(2024, 1, 1, 10, 0),
                "LOT123",
                "TEST456",
                "Glucose",
                "Normal",
                120.5,
                100.0,
                15.0,
                "mg/dL",
                "1s",
                "Approved"
        );
    }

    public static List<GenericValuesRecord> createSampleRecordList() {
        List<GenericValuesRecord> records = new ArrayList<>();

        // Normal level records
        records.add(new GenericValuesRecord(
                1L,
                LocalDateTime.of(2024, 1, 1, 10, 0),
                "LOT123",
                "TEST456",
                "Glucose",
                "Normal",
                120.0,
                100.0,
                15.0,
                "mg/dL",
                "+1s",
                "Approved"
        ));

        records.add(new GenericValuesRecord(
                2L,
                LocalDateTime.of(2024, 1, 1, 14, 0),
                "LOT123",
                "TEST456",
                "Glucose",
                "Normal",
                95.0,
                100.0,
                15.0,
                "mg/dL",
                "Average",
                "Approved"
        ));

        // High level records
        records.add(new GenericValuesRecord(
                3L,
                LocalDateTime.of(2024, 1, 1, 10, 30),
                "LOT124",
                "TEST457",
                "Glucose",
                "High",
                300.0,
                280.0,
                20.0,
                "mg/dL",
                "+1s",
                "Approved"
        ));

        // Low level records
        records.add(new GenericValuesRecord(
                4L,
                LocalDateTime.of(2024, 1, 1, 11, 0),
                "LOT125",
                "TEST458",
                "Glucose",
                "Low",
                50.0,
                40.0,
                5.0,
                "mg/dL",
                "+2s",
                "Approved"
        ));

        return records;
    }

    public static List<GenericValuesRecord> createMultiAnalyteRecordList() {
        List<GenericValuesRecord> records = new ArrayList<>();

        records.add(new GenericValuesRecord(
                1L,
                LocalDateTime.of(2024, 1, 1, 10, 0),
                "LOT123",
                "TEST456",
                "Glucose",
                "Normal",
                120.0,
                100.0,
                5.0,
                "mg/dL",
                "+3s",
                "Failed"
        ));

        // Creatinine records
        records.add(new GenericValuesRecord(
                2L,
                LocalDateTime.of(2024, 1, 1, 10, 0),
                "LOT789",
                "TEST101",
                "Creatinine",
                "Normal",
                1.2,
                1.0,
                0.2,
                "mg/dL",
                "+1s",
                "Approved"
        ));

        // Cholesterol records
        records.add(new GenericValuesRecord(
                3L,
                LocalDateTime.of(2024, 1, 1, 10, 0),
                "LOT456",
                "TEST789",
                "Cholesterol",
                "High",
                250.0,
                240.0,
                10.0,
                "mg/dL",
                "+1s",
                "Approved"
        ));

        return records;
    }

    public static List<GenericValuesRecord> createDateRangeRecords() {
        List<GenericValuesRecord> records = new ArrayList<>();
        LocalDateTime baseDate = LocalDateTime.of(2024, 1, 1, 10, 0);

        // Add records across multiple days
        for (int i = 0; i < 5; i++) {
            records.add(new GenericValuesRecord(
                    (long) i + 1,
                    baseDate.plusDays(i),
                    "LOT123",
                    "TEST456",
                    "Glucose",
                    "Normal",
                    100.0,
                    100.0,
                    15.0,
                    "mg/dL",
                    "Average",
                    "Approved"
            ));
        }

        return records;
    }

    public static GenericValuesRecord createOutOfRangeRecord() {
        return new GenericValuesRecord(
                1L,
                LocalDateTime.of(2024, 1, 1, 10, 0),
                "LOT123",
                "TEST456",
                "Glucose",
                "Normal",
                160.0,
                100.0,
                15.0,
                "mg/dL",
                "+3s",
                "Failed"
        );
    }
}