package com.example.balanceservice.dto;

public class AccountTO {
    private Long id;
    private Long amount;

    public AccountTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "AccountTO{" +
                "id=" + id +
                ", amount=" + amount +
                '}';
    }
}
