CREATE SCHEMA balance_service;

CREATE TABLE balance_service.account
(
    id        integer     not null,
    amount    integer     not null,
    constraint balance_service_account_id primary key (id)
);

comment on table balance_service.account is 'Справочник используется для хранения Счетов';
comment on column balance_service.account.id is 'Идентификатор счета';
comment on column balance_service.account.amount is 'Сумма';