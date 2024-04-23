
-- Drop table
DROP TABLE IF EXISTS public.form;
DROP TABLE IF EXISTS public.lite_category;
DROP TABLE IF EXISTS public.security_referral;
DROP TABLE IF EXISTS public.risk_change;
DROP TABLE IF EXISTS public.next_review_change_history;

DROP TYPE IF EXISTS public.review_reason_enum;
DROP TYPE IF EXISTS public.cat_type_enum;
DROP TYPE IF EXISTS public.security_referral_status_enum;

CREATE TYPE public.review_reason_enum AS ENUM (
	'DUE',
	'AGE',
	'MANUAL',
	'RISK_CHANGE');

CREATE TYPE public.cat_type_enum AS ENUM (
	'INITIAL',
	'RECAT');

CREATE TYPE public.security_referral_status_enum AS ENUM (
	'NEW',
	'REFERRED',
	'COMPLETED',
	'CANCELLED');

CREATE TABLE public.form (
                             id serial4 NOT NULL,
                             form_response jsonb NULL,
                             booking_id int8 NOT NULL,
                             user_id varchar(32) NULL,
                             status varchar(20) NULL,
                             assigned_user_id varchar(32) NULL,
                             referred_date timestamptz NULL,
                             referred_by varchar(32) NULL,
                             sequence_no int4 NOT NULL DEFAULT 1,
                             risk_profile jsonb NULL,
                             prison_id varchar(6) NOT NULL DEFAULT 'XXX'::character varying,
                             offender_no varchar(10) NOT NULL DEFAULT 'unknown'::character varying,
                             start_date timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                             security_reviewed_by varchar(32) NULL,
                             security_reviewed_date timestamptz NULL,
                             approval_date date NULL,
                             cat_type public.cat_type_enum NOT NULL DEFAULT 'INITIAL'::cat_type_enum,
                             nomis_sequence_no int4 NULL,
                             assessment_date date NULL,
                             approved_by varchar(255) NULL,
                             assessed_by varchar(255) NULL,
                             review_reason public.review_reason_enum NULL,
                             due_by_date date NULL,
                             cancelled_date timestamptz NULL,
                             cancelled_by varchar(255) NULL
);

CREATE TABLE public.lite_category (
                                      booking_id int8 NOT NULL,
                                      "sequence" int4 NOT NULL,
                                      category varchar(6) NOT NULL,
                                      supervisor_category varchar(6) NULL,
                                      offender_no varchar(255) NOT NULL,
                                      prison_id varchar(6) NOT NULL,
                                      created_date timestamptz NOT NULL,
                                      approved_date timestamptz NULL,
                                      assessed_by varchar(255) NOT NULL,
                                      approved_by varchar(255) NULL,
                                      assessment_committee varchar(12) NOT NULL,
                                      assessment_comment varchar(4000) NULL,
                                      next_review_date date NOT NULL,
                                      placement_prison_id varchar(6) NULL,
                                      approved_committee varchar(12) NULL,
                                      approved_placement_prison_id varchar(6) NULL,
                                      approved_placement_comment varchar(240) NULL,
                                      approved_comment varchar(240) NULL
);

CREATE TABLE public.next_review_change_history (
                                                   id serial4 NOT NULL,
                                                   booking_id int8 NULL,
                                                   offender_no varchar(10) NOT NULL,
                                                   next_review_date date NOT NULL,
                                                   reason text NOT NULL,
                                                   change_date timestamptz NOT NULL,
                                                   changed_by varchar(255) NOT NULL
);

CREATE TABLE public.risk_change (
                                    id serial4 NOT NULL,
                                    old_profile jsonb NOT NULL,
                                    new_profile jsonb NOT NULL,
                                    offender_no varchar(255) NOT NULL,
                                    user_id varchar(255) NULL,
                                    prison_id varchar(6) NOT NULL,
                                    status varchar(20) NOT NULL DEFAULT 'NEW'::character varying,
                                    raised_date timestamptz NOT NULL
);

CREATE TABLE public.security_referral (
                                          id serial4 NOT NULL,
                                          offender_no varchar(255) NOT NULL,
                                          user_id varchar(255) NOT NULL,
                                          prison_id varchar(6) NOT NULL,
                                          status public.security_referral_status_enum NOT NULL DEFAULT 'NEW'::security_referral_status_enum,
                                          raised_date timestamptz NOT NULL,
                                          processed_date timestamptz NULL,
                                          CONSTRAINT security_referral_offender_no_unique UNIQUE (offender_no),
                                          CONSTRAINT security_referral_pkey PRIMARY KEY (id)
);
CREATE INDEX security_referral_prison_id_index ON public.security_referral USING btree (prison_id);

DROP TABLE IF EXISTS PREVIOUS_PROFILE

CREATE TABLE PREVIOUS_PROFILE
(
    OFFENDER_NO       VARCHAR(10) PRIMARY KEY,
    ESCAPE            TEXT,
    EXTREMISM         TEXT,
    SOC               TEXT,
    VIOLENCE          TEXT,
    EXECUTE_DATE_TIME TIMESTAMP   NOT NULL,
);

COMMENT ON TABLE PREVIOUS_PROFILE IS 'Records the previous risk profile results for an offender';

GRANT SELECT, INSERT, UPDATE ON PREVIOUS_PROFILE TO risk_profiler;

--DELETE FROM risk_change;
--DELETE FROM form;
--DELETE FROM lite_category;
--DELETE FROM security_referral;
--DELETE FROM lite_category;
--DELETE FROM next_review_change_history;
