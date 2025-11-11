create table if not exists prisoner_risk_profile
(
    offender_no       varchar(255) not null primary key,
    risk_profile      jsonb,
    execute_date_time timestamp,
);