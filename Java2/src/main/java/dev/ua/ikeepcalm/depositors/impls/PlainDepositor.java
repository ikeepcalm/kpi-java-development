package dev.ua.ikeepcalm.depositors.impls;

import dev.ua.ikeepcalm.Main;
import dev.ua.ikeepcalm.bank.Bank;
import dev.ua.ikeepcalm.depositors.Depositor;

public class PlainDepositor extends Depositor {

    public PlainDepositor(int id, String name, Bank bank) {
        super(id, name, bank);
    }

    public void startBrouhaha() {
       new Runnable() {
           @Override
           public void run() {
               try {
                   Main.log("Brouhaha started!", Main.Color.RED);
                   Thread.sleep(1000);
                   Main.log("I will now scream!", Main.Color.RED);
                   Thread.sleep(1000);
                   for (int i = 0; i < 10; i++) {
                       Main.log("Aaaaaaaaaaaaaa!", Main.Color.RED);
                       Thread.sleep(500);
                   }
                   Main.log("Brouhaha ended!", Main.Color.RED);
               } catch (InterruptedException e) {
                   Main.log("Brouhaha interrupted!", Main.Color.RED);
               }
           }
       }.run();
    }

    public String getPriority() {
        return "Medium";
    }

    @Override
    public float initBonus() {
        return 0;
    }
}
