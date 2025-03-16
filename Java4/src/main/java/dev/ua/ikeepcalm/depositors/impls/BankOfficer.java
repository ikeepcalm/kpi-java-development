package dev.ua.ikeepcalm.depositors.impls;

import dev.ua.ikeepcalm.bank.Bank;
import dev.ua.ikeepcalm.depositors.Depositor;
import lombok.Getter;

@Getter
public class BankOfficer extends Depositor {

    private final EmployeeRole role;

    public BankOfficer() {
        this.role = new EmployeeRole("Bank Officer", "Banking", "Officer");
    }

    public BankOfficer(int id, String name, Bank bank, EmployeeRole role) {
        super(id, name, bank);
        this.role = role;
    }

    public record EmployeeRole(String role, String department, String position) {}

    @Override
    public float initBonus() {
        return 0.05f;
    }
}