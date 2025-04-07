INSERT INTO public.form (form_response,
                  booking_id, user_id, status, assigned_user_id, referred_date, referred_by, sequence_no, risk_profile,
                  prison_id, offender_no, start_date, security_reviewed_by, cat_type, nomis_sequence_no,
                  assessment_date, approved_by, assessed_by, review_reason, due_by_date, cancelled_by)
VALUES ('{"something": "else"}', 0, 'TEST_GEN', 'STARTED', 'TEST_GEN', '2025-01-25 11:24:12.768', '', 1,
        '{"risk": "profile"}',
        'BMI'::character varying, 'ABC123', '2025-01-25 11:24:12.768', '', 'RECAT'::cat_type_enum, 1,
        '2025-01-25 11:24:12.768', '', '', 'MANUAL',
        '2025-01-20', '');
