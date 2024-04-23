INSERT INTO public.lite_category
(booking_id, "sequence", category, supervisor_category, offender_no, prison_id, created_date, approved_date, assessed_by, approved_by, assessment_committee, assessment_comment, next_review_date, placement_prison_id, approved_committee, approved_placement_prison_id, approved_placement_comment, approved_comment)
VALUES(0, 5, 'U', 'U', 'GXXXX', 'LPI', '2020-05-18 13:58:42.435 +0100', '2020-05-18 01:00:00.000 +0100', 'LBENNETT_GEN', 'CT_SUP', 'OCA', 'Testing the creation of an unsentenced', '2020-11-18', 'ASI', 'OCA', 'ASI', 'approved placement comment', 'approval comment');

INSERT INTO public.next_review_change_history
(id, booking_id, offender_no, next_review_date, reason, change_date, changed_by)
VALUES(1, '1146373', 'GXXXX', '2021-12-12', 'testing', '2021-09-22 10:25:44.395 +0100', 'CGLYNN_ADM');

INSERT INTO public.risk_change
(id, old_profile, new_profile, offender_no, user_id, prison_id, status, raised_date)
VALUES(1,
       '{"soc": {"nomsId": "GXXXX", "riskType": "SOC", "transferToSecurity": false, "provisionalCategorisation": "C"}, "escape": {"nomsId": "G0048VL", "riskType": "ESCAPE", "activeEscapeList": false, "activeEscapeRisk": false, "escapeListAlerts": [], "escapeRiskAlerts": [], "provisionalCategorisation": "C"}, "violence": {"nomsId": "G0048VL", "riskType": "VIOLENCE", "displayAssaults": true, "numberOfAssaults": 0, "notifySafetyCustodyLead": false, "numberOfSeriousAssaults": 0, "provisionalCategorisation": "C", "veryHighRiskViolentOffender": false}, "extremism": {"nomsId": "G0048VL", "riskType": "EXTREMISM", "notifyRegionalCTLead": false, "increasedRiskOfExtremism": false, "provisionalCategorisation": "C"}}', '{"soc": {"nomsId": "G0048VL", "riskType": "SOC", "transferToSecurity": true, "provisionalCategorisation": "C"}, "escape": {"nomsId": "G0048VL", "riskType": "ESCAPE", "activeEscapeList": false, "activeEscapeRisk": false, "escapeListAlerts": [], "escapeRiskAlerts": [], "provisionalCategorisation": "C"}, "violence": {"nomsId": "G0048VL", "riskType": "VIOLENCE", "displayAssaults": true, "numberOfAssaults": 1, "notifySafetyCustodyLead": false, "numberOfSeriousAssaults": 0, "provisionalCategorisation": "C", "veryHighRiskViolentOffender": false}, "extremism": {"nomsId": "G0048VL", "riskType": "EXTREMISM", "notifyRegionalCTLead": false, "increasedRiskOfExtremism": false, "provisionalCategorisation": "C"}}', 'GXXXX',
       'LBENNETT_GEN',
       '1000', 'NEW'::character varying,
       '2019-09-18 10:45:34.166 +0100');

INSERT INTO public.security_referral
(offender_no, user_id, prison_id, status, raised_date, processed_date)
VALUES('GXXXX', 'LBENNETT_GEN', 'LPI', 'REFERRED'::security_referral_status_enum, '2019-09-19 13:33:21.123 +0100', '2019-09-19 13:36:46.335 +0100');

INSERT INTO public.form
(form_response,
 booking_id,
 user_id,
 status,
 assigned_user_id,
 referred_date,
 referred_by,
 sequence_no,
 risk_profile,
 prison_id,
 offender_no,
 start_date,
 security_reviewed_by,
 security_reviewed_date,
 approval_date,
 cat_type,
 nomis_sequence_no,
 assessment_date,
 approved_by,
 assessed_by,
 review_reason,
 due_by_date,
 cancelled_date,
 cancelled_by)
VALUES('{"recat":{"decision":{"category":"B"},"securityBack":{},"securityInput":{"securityInputNeeded":"Yes","securityInputNeededText":"text from recat"},"nextReviewDate":{"date":"2/8/2019"},"riskAssessment":{"lowerCategory":"lower text","otherRelevant":"Yes","higherCategory":"higher text","otherRelevantText":"rel info"},"prisonerBackground":{"offenceDetails":"offence details text"},"higherSecurityReview":{"steps":"steps text","transfer":"Yes","behaviour":"higher security text","conditions":"security conditions","transferText":"manage text"}},"security":{"review":{"securityReview":"security review text"}},"supervisor":{"review":{"proposedCategory":"B","otherInformationText":"other rel info","supervisorCategoryAppropriate":"Yes"}},"openConditionsRequested":false}',
       0,
       'SRENDELL_GEN',
       'APPROVED',
       'SRENDELL_GEN',
       '2019-04-02 13:48:11.060',
       '',
       1,
       '{"history":{},"socProfile":{},"escapeProfile":{"nomsId":"G4143VX","riskType":"ESCAPE","provisionalCategorisation":"C"},"violenceProfile":{"nomsId":"G4143VX","riskType":"VIOLENCE","displayAssaults":false,"numberOfAssaults":0,"notifySafetyCustodyLead":false,"numberOfSeriousAssaults":0,"provisionalCategorisation":"C","veryHighRiskViolentOffender":false},"extremismProfile":{}}',
       'NMI'::character varying,
       'GXXXX',
       '2019-05-09 13:26:28.990',
       'LBENNETT_GEN',
       '2019-06-25 10:55:53.353',
       '2019-06-25',
       'INITIAL'::cat_type_enum,
       0,
       '2019-07-16',
       'SRENDELL_GEN',
       'SRENDELL_GEN',
       'DUE',
       '2017-04-01',
       '2019-11-25 11:24:12.768',
       'SRENDELL_GEN');

INSERT INTO public.PREVIOUS_PROFILE
(OFFENDER_NO, ESCAPE, EXTREMISM, SOC, VIOLENCE, EXECUTE_DATE_TIME)
VALUES('GXXXX', '{}', '{}', '{}', '{"nomsId": "G2194GK", "provisionalCategorisation": "C", "veryHighRiskViolentOffender": false, "notifySafetyCustodyLead": false, "displayAssaults": false, "numberOfAssaults": 0, "numberOfSeriousAssaults": 0, "numberOfNonSeriousAssaults": 0,"riskType": "VIOLENCE"}', '2021-09-22 10:25:44.395 +0100');