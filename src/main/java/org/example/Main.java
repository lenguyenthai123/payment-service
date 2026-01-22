package org.example;


import org.example.cli.CommandHandler;
import org.example.cli.CommandLoop;
import org.example.context.ApplicationContext;
import org.example.cronjob.ScheduledPaymentEngine;
import org.example.mock.MockDataInitializer;

public class Main {

    public static void main(String[] args) {
        MockDataInitializer.init();
        ApplicationContext ctx = new ApplicationContext();

        CommandHandler handler = new CommandHandler(ctx);
        CommandLoop loop = new CommandLoop(handler);

        loop.run();
        ctx.shutdown();
    }
}
