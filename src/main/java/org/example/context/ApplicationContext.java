package org.example.context;

import org.example.cronjob.ScheduledPaymentEngine;
import org.example.service.BillService;
import org.example.service.PaymentService;
import org.example.service.ScheduleService;
import org.example.service.WalletService;
import org.example.service.impl.BillServiceImpl;
import org.example.service.impl.PaymentServiceImpl;
import org.example.service.impl.ScheduleServiceImpl;
import org.example.service.impl.WalletServiceImpl;

public final class ApplicationContext {

    private final BillService billService;
    private final WalletService walletService;
    private final PaymentService paymentService;
    private final ScheduleService scheduleService;
    private ScheduledPaymentEngine engine;
    private Thread engineThread;

    public ApplicationContext() {
        this.billService = new BillServiceImpl();
        this.walletService = new WalletServiceImpl();
        this.scheduleService = new ScheduleServiceImpl(this.billService);
        this.paymentService =
                new PaymentServiceImpl(billService, walletService);
        startScheduler();

    }

    public BillService billService() {
        return billService;
    }

    public WalletService walletService() {
        return walletService;
    }

    public PaymentService paymentService() {
        return paymentService;
    }

    public ScheduleService scheduleService() {
        return scheduleService;
    }

    private void startScheduler() {
        engine = new ScheduledPaymentEngine(paymentService);
        engineThread = new Thread(engine, "scheduled-payment-engine");
        engineThread.setDaemon(true); // JVM exit thì thread cũng chết
        engineThread.start();
    }

    public void shutdown() {
        if (engine != null) {
            engine.shutdown();
        }
    }
}
