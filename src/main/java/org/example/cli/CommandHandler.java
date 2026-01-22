package org.example.cli;

import org.example.context.ApplicationContext;
import org.example.repository.Database;
import org.example.utils.TablePrinter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class CommandHandler {

    private final ApplicationContext ctx;

    private static final SimpleDateFormat DATE_FMT =
            new SimpleDateFormat("dd/MM/yyyy");

    public CommandHandler(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    public void handle(String line) {
        String[] parts = line.trim().split("\\s+");
        String cmd = parts[0].toUpperCase();

        switch (cmd) {
            case "CASH_IN" -> handleCashIn(parts);
            case "LIST_BILL" -> handleListBill();
            case "PAY" -> handlePay(parts);
            case "SCHEDULE" -> handleSchedule(parts);
            case "LIST_PAYMENT" -> handleListPayment();
            case "SEARCH_BILL_BY_PROVIDER" -> handleSearchByProvider(parts);
            case "DUE_DATE" -> handleDueDate();
            default -> System.out.println("Unknown command: " + cmd);
        }
    }

    // ==== handlers ====

    private void handleCashIn(String[] args) {
        Double amount = Double.parseDouble(args[1]);
        ctx.walletService().cashIn(Database.DEMO_CUSTOMER, amount);
        System.out.println("Your available balance: "
                + ctx.walletService().getBalance(Database.DEMO_CUSTOMER));
    }

    private void handleListBill() {
        var bills = ctx.billService().searchByProvider(Database.DEMO_CUSTOMER, null);
        TablePrinter.printBills(bills);
    }

    private void handlePay(String[] args) {
        if (args.length == 2) {
            int index = Integer.parseInt(args[1]);
            String billId = resolveBillIdFromIndex(index);

            boolean ok = ctx.paymentService()
                    .payBill(Database.DEMO_CUSTOMER, billId);

            if (ok) {
                System.out.println(
                        "Payment has been completed for Bill #" + index +
                                " (id=" + billId + ")");
            } else {
                System.out.println("Sorry! Not enough fund to proceed with payment.");
            }
        } else {
            var billIds = java.util.Arrays.stream(args)
                    .skip(1)
                    .map(Integer::parseInt)
                    .map(this::resolveBillIdFromIndex)
                    .toList();

            boolean ok = ctx.paymentService()
                    .payBills(Database.DEMO_CUSTOMER, billIds);

            if (ok) {
                System.out.println("Payments completed.");
            } else {
                System.out.println("Sorry! Not enough fund to proceed with payment.");
            }
        }

        System.out.println("Your current balance is: "
                + ctx.walletService().getBalance(Database.DEMO_CUSTOMER));
    }

    private void handleSchedule(String[] args) {
        int index = Integer.parseInt(args[1]);
        String billId = resolveBillIdFromIndex(index);

        Date date;
        try {
            date = DATE_FMT.parse(args[2]);
        } catch (ParseException e) {
            throw new IllegalArgumentException(
                    "Invalid date format, expected dd/MM/yyyy");
        }

        ctx.scheduleService()
                .schedule(Database.DEMO_CUSTOMER, billId, date);

        System.out.println(
                "Payment for bill id " + billId +
                        " is scheduled on " + DATE_FMT.format(date));
    }

    private void handleListPayment() {
        var txs = ctx.paymentService()
                .listPayments(Database.DEMO_CUSTOMER);

        TablePrinter.printPayments(txs);
    }

    private void handleSearchByProvider(String[] args) {
        var provider = args[1];
        var bills = ctx.billService()
                .searchByProvider(Database.DEMO_CUSTOMER, provider);

        TablePrinter.printBills(bills);
    }

    private void handleDueDate() {
        var bills = ctx.billService()
                .listDueBills(Database.DEMO_CUSTOMER);

        TablePrinter.printBills(bills);
    }

    private String resolveBillIdFromIndex(int index) {
        var bills = ctx.billService()
                .searchByProvider(Database.DEMO_CUSTOMER, null);

        if (index < 1 || index > bills.size()) {
            throw new IllegalArgumentException(
                    "Invalid bill number: " + index);
        }

        return bills.get(index - 1).getId();
    }

}
