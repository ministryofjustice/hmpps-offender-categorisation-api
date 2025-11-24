create table if not exists prisoner_risk_profile
(
    offender_no       varchar(255) not null primary key,
    risk_profile      jsonb,
    calculated_at timestamp with time zone
);