package dev.ua.ikeepcalm.app.depositors.generics.impls;

import dev.ua.ikeepcalm.app.bank.Bank;
import dev.ua.ikeepcalm.app.depositors.generics.GenericDepositor;
import lombok.Getter;

@Getter
public class GenericBankOfficer<B extends Bank> extends GenericDepositor<B> {

    private final EmployeeRole role;

    public GenericBankOfficer() {
        this.role = new EmployeeRole("Bank Officer", "Banking", "Officer");
    }

    public GenericBankOfficer(int id, String name, B bank, EmployeeRole role) {
        super(id, name, bank);
        this.role = role;
    }

    public record EmployeeRole(String role, String department, String position) {
    }

    @Override
    public float initBonus() {
        return 0.05f;
    }

}