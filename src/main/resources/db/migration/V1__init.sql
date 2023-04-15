CREATE TABLE bill
(
    id                    long primary key unique,
--     repayment_plan_id     long         not null,
    customer_bank_account VARCHAR2(20) not null,
    amount                double       not null,
    credit_transaction_id VARCHAR2(20)
);

create sequence bill_id
    minvalue 1
    increment by 1;

CREATE TABLE repayment
(
    id                long primary key unique,
    repayment_plan_id long   not null,
    date              date   not null,
    amount            double not null,
    transaction_id    VARCHAR2(20)
);

create sequence repayment_id
    minvalue 1
    increment by 1;

CREATE TABLE repayment_plan
(
    id          long primary key unique,
    bill_id     long not null,
    credit_date date not null
);

create sequence repayment_plan_id
    minvalue 1
    increment by 1;

