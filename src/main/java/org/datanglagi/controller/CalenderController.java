package org.datanglagi.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class CalenderController {
 
@FXML 
    private Button btn;

@FXML
private GridPane calendarGrid;

@FXML
private Label monthLabel;

    private YearMonth currentMonth = YearMonth.now();

    @FXML
    public void initialize() {
        generateCalendar(currentMonth);
    }

    @FXML
    private void prevMonth() {
        currentMonth = currentMonth.minusMonths(1);
        generateCalendar(currentMonth);
    }

    @FXML
    private void nextMonth() {
        currentMonth = currentMonth.plusMonths(1);
        generateCalendar(currentMonth);
    }

private void generateCalendar(YearMonth yearMonth) {

    calendarGrid.getChildren().clear();

    monthLabel.setText(
        yearMonth.getMonth()
                 .getDisplayName(TextStyle.FULL, new Locale("id", "ID"))
        + " " + yearMonth.getYear()
    );

    LocalDate firstDay = yearMonth.atDay(1);
    int firstColumn = firstDay.getDayOfWeek().getValue() - 1;
    int daysInMonth = yearMonth.lengthOfMonth();
    int day = 1;

    LocalDate today = LocalDate.now(); // Pindahkan ke sini agar di-create sekali saja

    for (int row = 0; row < 6; row++) {
        for (int col = 0; col < 7; col++) {

            if (row == 0 && col < firstColumn) {
                continue;
            }

            if (day > daysInMonth) {
                return;
            }

            // 1. Deklarasi tombol
            Button btn = new Button(String.valueOf(day));
            btn.setPrefSize(42, 42);

            // Style default (warna putih)
            btn.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;" +
                "-fx-border-color: #E0E0E0;" +
                "-fx-font-size: 14;"
            );

            // 2. Pengecekan apakah "day" ini adalah hari ini (today)
            // Dipindahkan ke dalam sini agar masih dalam scope variabel 'btn'
            if (yearMonth.getYear() == today.getYear()
                && yearMonth.getMonthValue() == today.getMonthValue()
                && day == today.getDayOfMonth()) {

                // Override style jadi warna merah untuk hari ini
                btn.setStyle(
                    "-fx-background-color: #9B1C1C;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 18;" +
                    "-fx-font-weight: bold;"
                );
            }

            calendarGrid.add(btn, col, row);

            // day++ dilakukan paling akhir di dalam loop kolom
            day++;
        }
    }
}
}
