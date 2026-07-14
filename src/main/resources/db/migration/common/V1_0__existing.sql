create table if not exists form
(
    id                     serial
        primary key,
    form_response          jsonb,
    booking_id             bigint                                                        not null,
    user_id                varchar(32),
    status                 varchar(20),
    assigned_user_id       varchar(32),
    referred_date          timestamp with time zone,
    referred_by            varchar(32),
    sequence_no            integer                  default 1 not null,
    risk_profile           jsonb,
    prison_id              varchar(6)               default 'XXX'::character varying     not null,
    offender_no            varchar(10)              default 'unknown'::character varying not null,
    start_date             timestamp with time zone default CURRENT_TIMESTAMP(6)         not null,
    security_reviewed_by   varchar(32),
    security_reviewed_date timestamp with time zone,
    approval_date          date,
    cat_type               cat_type_enum            default 'INITIAL'::cat_type_enum     not null,
    nomis_sequence_no      integer,
    assessment_date        date,
    approved_by            varchar(255),
    assessed_by            varchar(255),
    review_reason          review_reason_enum,
    due_by_date            date,
    cancelled_date         timestamp with time zone,
    cancelled_by           varchar(255),
    constraint booking_nomis_sequence_index
        unique (booking_id, nomis_sequence_no),
    constraint booking_sequence_index
        unique (booking_id, sequence_no)
);

create index form_prison_id_index on form (prison_id);
create index idx_form_offender_no_status_sequence_no on form (offender_no, status, sequence_no);
create index idx_form_filters on form (prison_id, status, cat_type, review_reason);
create index idx_form_booking_id_sequence_no_desc on form (booking_id asc, sequence_no desc);

create table if not exists risk_change
(
    id          serial
        primary key,
    old_profile jsonb                                        not null,
    new_profile jsonb                                        not null,
    offender_no varchar(255)                                 not null,
    user_id     varchar(255),
    prison_id   varchar(6)                                   not null,
    status      varchar(20) default 'NEW'::character varying not null,
    raised_date timestamp with time zone                     not null
);

create index risk_change_prison_id_index on risk_change (prison_id);

create table if not exists security_referral
(
    id             serial
        primary key,
    offender_no    varchar(255)                                                               not null
        constraint security_referral_offender_no_unique
            unique,
    user_id        varchar(255)                                                               not null,
    prison_id      varchar(6)                                                                 not null,
    status         security_referral_status_enum default 'NEW'::security_referral_status_enum not null,
    raised_date    timestamp with time zone                                                   not null,
    processed_date timestamp with time zone
);

create index security_referral_prison_id_index on security_referral (prison_id);

create table if not exists lite_category
(
    booking_id                   bigint                   not null,
    sequence                     integer                  not null,
    category                     varchar(6)               not null,
    supervisor_category          varchar(6),
    offender_no                  varchar(255)             not null,
    prison_id                    varchar(6)               not null,
    created_date                 timestamp with time zone not null,
    approved_date                timestamp with time zone,
    assessed_by                  varchar(255)             not null,
    approved_by                  varchar(255),
    assessment_committee         varchar(12)              not null,
    assessment_comment           varchar(4000),
    next_review_date             date                     not null,
    placement_prison_id          varchar(6),
    approved_committee           varchar(12),
    approved_placement_prison_id varchar(6),
    approved_placement_comment   varchar(240),
    approved_comment             varchar(240),
    approved_category_comment    varchar(255),
    primary key (booking_id, sequence)
);

create index lite_category_offender_no_index on lite_category (offender_no);
create index lite_category_approved_date_index on lite_category (approved_date);
create index lite_category_prison_id_approved_date_index on lite_category (prison_id, approved_date);

create table if not exists next_review_change_history
(
    id               serial
        primary key,
    booking_id       bigint,
    offender_no      varchar(10)              not null,
    next_review_date date                     not null,
    reason           text                     not null,
    change_date      timestamp with time zone not null,
    changed_by       varchar(255)             not null
);

create index next_review_change_history_offender_no_index on next_review_change_history (offender_no);

create table if not exists previous_profile
(
    offender_no       varchar(255) not null
        primary key,
    escape            varchar(255),
    execute_date_time timestamp,
    soc               varchar(255),
    violence          varchar(255)
);

