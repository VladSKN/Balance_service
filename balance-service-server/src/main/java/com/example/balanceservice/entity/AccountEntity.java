package com.example.balanceservice.entity;


public class AccountEntity {
    private final Long id;
    private final Long amount;

    public AccountEntity(Long id, Long amount) {
        this.id = id;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public Long getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "AccountEntity{" +
                "id=" + id +
                ", amount=" + amount +
                '}';
    }
}
