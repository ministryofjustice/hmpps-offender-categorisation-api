INSERT INTO public.lite_category
(booking_id, "sequence", category, supervisor_category, offender_no, prison_id, created_date, approved_date, assessed_by, approved_by, assessment_committee, assessment_comment, next_review_date, placement_prison_id, approved_committee, approved_placement_prison_id, approved_placement_comment, approved_comment)
VALUES(0, 5, 'U', 'U', '123ABC', 'LPI', '2020-05-18 13:58:42.435 +0100', '2021-05-18 01:00:00.000 +0100', 'TEST_GEN', 'TEST_SUP', 'OCA', 'Testing the creation of an unsentenced one', '2021-11-18', 'ASI', 'OCA', 'ASI', 'approved placement comment one', 'approval comment one');
INSERT INTO public.lite_category
(booking_id, "sequence", category, supervisor_category, offender_no, prison_id, created_date, approved_date, assessed_by, approved_by, assessment_committee, assessment_comment, next_review_date, placement_prison_id, approved_committee, approved_placement_prison_id, approved_placement_comment, approved_comment)
VALUES(1, 6, 'U', 'U', '123ABC', 'LEI', '2019-05-18 13:58:42.435 +0100', '2020-05-18 01:00:00.000 +0100', 'TEST2_GEN', 'TEST2_SUP', 'OCA', 'Testing the creation of an unsentenced two', '2022-11-18', 'ASI', 'OCA', 'ASI', 'approved placement comment two', 'approval comment two');
INSERT INTO public.lite_category
(booking_id, "sequence", category, supervisor_category, offender_no, prison_id, created_date, approved_date, assessed_by, approved_by, assessment_committee, assessment_comment, next_review_date, placement_prison_id, approved_committee, approved_placement_prison_id, approved_placement_comment, approved_comment)
VALUES(2, 7, 'U', 'U', '123ABC', 'DHI', '2018-05-18 13:58:42.435 +0100', '2019-05-18 01:00:00.000 +0100', 'TEST3_GEN', 'TEST3_SUP', 'OCA', 'Testing the creation of an unsentenced three', '2020-11-18', 'ASI', 'OCA', 'ASI', 'approved placement comment three', 'approval comment three');
INSERT INTO public.lite_category
(booking_id, "sequence", category, supervisor_category, offender_no, prison_id, created_date, approved_date, assessed_by, approved_by, assessment_committee, assessment_comment, next_review_date, placement_prison_id, approved_committee, approved_placement_prison_id, approved_placement_comment, approved_comment)
VALUES(3, 8, 'U', 'U', '123ABC', 'MLN', '2021-05-18 13:58:42.435 +0100', '2022-05-18 01:00:00.000 +0100', 'TEST4_GEN', 'TEST4_SUP', 'OCA', 'Testing the creation of an unsentenced four', '2023-11-18', 'ASI', 'OCA', 'ASI', 'approved placement comment four', 'approval comment four');

INSERT INTO public.next_review_change_history
(id, booking_id, offender_no, next_review_date, reason, change_date, changed_by)
VALUES(1, '1146373', '123ABC', '2021-05-18', 'testing one', '2020-05-18 10:25:44.395 +0100', 'TEST_GEN');
INSERT INTO public.next_review_change_history
(id, booking_id, offender_no, next_review_date, reason, change_date, changed_by)
VALUES(2, '1146374', '123ABC', '2020-05-18', 'testing two', '2019-05-18 10:25:44.395 +0100', 'TEST2_GEN');
INSERT INTO public.next_review_change_history
(id, booking_id, offender_no, next_review_date, reason, change_date, changed_by)
VALUES(3, '1146375', '123ABC', '2022-05-18', 'testing three', '2021-05-18 10:25:44.395 +0100', 'TEST3_GEN');

INSERT INTO public.risk_change
(old_profile, new_profile, offender_no, user_id, prison_id, status, raised_date)
VALUES('{"soc": {"nomsId": "123ABC", "riskType": "SOC", "transferToSecurity": false, "provisionalCategorisation": "C"}, "escape": {"nomsId": "123ABC", "riskType": "ESCAPE", "activeEscapeList": false, "activeEscapeRisk": true, "escapeListAlerts": [], "escapeRiskAlerts": [{"active": true, "alertId": 83, "comment": "** E LIST ESCORT - 11/03/2021 **\nPREVIOUSLY CLOSED ALERT - 25/03/2020 - 11/03/2021 - REMAIN E RISK\n09/03/2021 - on escort to A+E", "expired": false, "ranking": 0, "alertCode": "XER", "alertType": "X", "bookingId": 12345, "offenderNo": "123ABC", "dateCreated": "2021-03-11", "alertCodeDescription": "Escape Risk", "alertTypeDescription": "Security"}], "provisionalCategorisation": "C"}, "violence": {"nomsId": "123ABC", "riskType": "VIOLENCE", "displayAssaults": true, "numberOfAssaults": 8, "notifySafetyCustodyLead": false, "numberOfSeriousAssaults": 0, "provisionalCategorisation": "C", "numberOfNonSeriousAssaults": 0, "veryHighRiskViolentOffender": false}, "extremism": null}',
       '{"soc": {"nomsId": "123ABC", "riskType": "SOC", "transferToSecurity": false, "provisionalCategorisation": "C"}, "escape": {"nomsId": "123ABC", "riskType": "ESCAPE", "activeEscapeList": false, "activeEscapeRisk": true, "escapeListAlerts": [], "escapeRiskAlerts": [{"active": true, "alertId": 83, "comment": "** E LIST ESCORT - 11/03/2021 **\nPREVIOUSLY CLOSED ALERT - 25/03/2020 - 11/03/2021 - REMAIN E RISK\n09/03/2021 - on escort to A+E", "expired": false, "ranking": 0, "alertCode": "XER", "alertType": "X", "bookingId": 12345, "offenderNo": "123ABC", "dateCreated": "2021-03-11", "alertCodeDescription": "Escape Risk", "alertTypeDescription": "Security"}], "provisionalCategorisation": "C"}, "violence": {"nomsId": "123ABC", "riskType": "VIOLENCE", "displayAssaults": true, "numberOfAssaults": 8, "notifySafetyCustodyLead": false, "numberOfSeriousAssaults": 0, "provisionalCategorisation": "C", "numberOfNonSeriousAssaults": 0, "veryHighRiskViolentOffender": true}, "extremism": null}',
       '123ABC',
       'TEST_GEN',
       '1000',
       'NEW'::character varying,
       '2020-05-18 10:45:34.166 +0100'
      );
INSERT INTO public.risk_change
(old_profile, new_profile, offender_no, user_id, prison_id, status, raised_date)
VALUES('{"soc": {"nomsId": "123ABC", "riskType": "SOC", "transferToSecurity": false, "provisionalCategorisation": "C"}, "escape": {"nomsId": "G0048VL", "riskType": "ESCAPE", "activeEscapeList": false, "activeEscapeRisk": false, "escapeListAlerts": [], "escapeRiskAlerts": [], "provisionalCategorisation": "C"}, "violence": {"nomsId": "G0048VL", "riskType": "VIOLENCE", "displayAssaults": true, "numberOfAssaults": 0, "notifySafetyCustodyLead": false, "numberOfSeriousAssaults": 0, "provisionalCategorisation": "C", "veryHighRiskViolentOffender": false}, "extremism": {"nomsId": "G0048VL", "riskType": "EXTREMISM", "notifyRegionalCTLead": false, "increasedRiskOfExtremism": false, "provisionalCategorisation": "C"}}',
       '{"soc": {"nomsId": "123ABC", "riskType": "SOC", "transferToSecurity": true, "provisionalCategorisation": "C"}, "escape": {"nomsId": "G0048VL", "riskType": "ESCAPE", "activeEscapeList": false, "activeEscapeRisk": false, "escapeListAlerts": [], "escapeRiskAlerts": [], "provisionalCategorisation": "C"}, "violence": {"nomsId": "G0048VL", "riskType": "VIOLENCE", "displayAssaults": true, "numberOfAssaults": 1, "notifySafetyCustodyLead": false, "numberOfSeriousAssaults": 0, "provisionalCategorisation": "C", "veryHighRiskViolentOffender": false}, "extremism": {"nomsId": "G0048VL", "riskType": "EXTREMISM", "notifyRegionalCTLead": false, "increasedRiskOfExtremism": false, "provisionalCategorisation": "C"}}',
       '123ABC',
       'TEST2_GEN',
       '1000',
       'NEW'::character varying,
       '2019-05-18 10:45:34.166 +0100'
      );
INSERT INTO public.risk_change
(old_profile, new_profile, offender_no, user_id, prison_id, status, raised_date)
VALUES('{"soc": {"nomsId": "123ABC", "riskType": "SOC", "transferToSecurity": false, "provisionalCategorisation": "C"}, "escape": {"nomsId": "G0048VL", "riskType": "ESCAPE", "activeEscapeList": false, "activeEscapeRisk": false, "escapeListAlerts": [], "escapeRiskAlerts": [], "provisionalCategorisation": "C"}, "violence": {"nomsId": "G0048VL", "riskType": "VIOLENCE", "displayAssaults": true, "numberOfAssaults": 0, "notifySafetyCustodyLead": false, "numberOfSeriousAssaults": 0, "provisionalCategorisation": "C", "veryHighRiskViolentOffender": false}, "extremism": {"nomsId": "G0048VL", "riskType": "EXTREMISM", "notifyRegionalCTLead": false, "increasedRiskOfExtremism": false, "provisionalCategorisation": "C"}}',
       '{"soc": {"nomsId": "123ABC", "riskType": "SOC", "transferToSecurity": true, "provisionalCategorisation": "C"}, "escape": {"nomsId": "G0048VL", "riskType": "ESCAPE", "activeEscapeList": false, "activeEscapeRisk": false, "escapeListAlerts": [], "escapeRiskAlerts": [], "provisionalCategorisation": "C"}, "violence": {"nomsId": "G0048VL", "riskType": "VIOLENCE", "displayAssaults": true, "numberOfAssaults": 1, "notifySafetyCustodyLead": false, "numberOfSeriousAssaults": 0, "provisionalCategorisation": "C", "veryHighRiskViolentOffender": false}, "extremism": {"nomsId": "G0048VL", "riskType": "EXTREMISM", "notifyRegionalCTLead": false, "increasedRiskOfExtremism": false, "provisionalCategorisation": "C"}}',
       '123ABC',
       'TEST3_GEN',
       '1000',
       'NEW'::character varying,
       '2021-05-18 10:45:34.166 +0100'
      );


INSERT INTO public.security_referral
(offender_no, user_id, prison_id, status, raised_date, processed_date)
VALUES('123ABC', 'TEST3_GEN', 'LEI', 'NEW'::security_referral_status_enum, '2021-05-18 13:33:21.123 +0100', '2019-09-19 13:36:46.335 +0100');


INSERT INTO public.form
(form_response, booking_id, user_id, status, assigned_user_id, referred_date, referred_by, sequence_no, risk_profile, prison_id, offender_no, start_date, security_reviewed_by, security_reviewed_date, approval_date, cat_type, nomis_sequence_no, assessment_date, approved_by, assessed_by, review_reason, due_by_date)
VALUES('{"recat": {"decision": {"category": "C"}, "oasysInput": {"date": "16/04/2021", "oasysRelevantInfo": "No"}, "securityInput": {"securityNoteNeeded": "Yes", "securityInputNeeded": "Yes", "securityInputNeededText": "As per procedure."}, "nextReviewDate": {"date": "18/08/2022", "indeterminate": "false"}, "riskAssessment": {"lowerCategory": "Not at this time.", "otherRelevant": "No", "higherCategory": "To remain CAT C.", "otherRelevantText": ""}, "prisonerBackground": {"offenceDetails": "Main offence: something."}}, "security": {"review": {"securityReview": "Having considered all available information there is nothing further to add to this assessment."}}, "supervisor": {"review": {"proposedCategory": "C", "otherInformationText": "reviewed and approved ", "supervisorCategoryAppropriate": "Yes"}}, "openConditionsRequested": false}',
       0,
       'TEST_GEN',
       'APPROVED',
       'TEST2_GEN',
       '2019-11-25 11:24:12.768',
       '',
       1,
       '{"catHistory": [{"bookingId": 1234, "assessorId": 1234, "offenderNo": "123ABC", "approvalDate": "2020-12-05", "assessorUser": "TEST_GEN", "assessmentSeq": 1, "assessmentCode": "CATEGORY", "assessmentDate": "2020-12-23", "classification": "Cat C", "nextReviewDate": "2021-12-05", "assessmentStatus": "A", "agencyDescription": "LMI", "assessmentComment": "Cat-tool Recat", "assessmentAgencyId": "UKI", "classificationCode": "C", "approvalDateDisplay": "03/12/2020", "cellSharingAlertFlag": false, "assessmentDescription": "Categorisation"}, {"bookingId": 2345, "assessorId": 2345, "offenderNo": "123ABC", "approvalDate": "2022-04-12", "assessorUser": "TEST_GEN", "assessmentSeq": 3, "assessmentCode": "CATEGORY", "assessmentDate": "2022-03-10", "classification": "Cat C", "nextReviewDate": "2023-01-10", "assessmentStatus": "I", "agencyDescription": "LMI (HMP)", "assessmentComment": "Cat-tool Recat", "assessmentAgencyId": "LMI", "classificationCode": "C", "approvalDateDisplay": "11/07/2022", "cellSharingAlertFlag": false, "assessmentDescription": "Categorisation"}, {"bookingId": 4567, "assessorId": 6445, "offenderNo": "123ABC", "approvalDate": "2023-07-12", "assessorUser": "TEST_GEN", "assessmentSeq": 7, "assessmentCode": "CATEGORY", "assessmentDate": "2023-07-25", "classification": "Cat C", "nextReviewDate": "2023-12-14", "assessmentStatus": "I", "agencyDescription": "DHE (HMP)", "assessmentComment": "Cat-tool Recat", "assessmentAgencyId": "DHE", "classificationCode": "C", "approvalDateDisplay": "18/03/2023", "cellSharingAlertFlag": false, "assessmentDescription": "Categorisation"}, {"bookingId": 64543, "assessorId": 653635, "offenderNo": "123ABC", "approvalDate": "2024-07-24", "assessorUser": "TEST_GEN", "assessmentSeq": 9, "assessmentCode": "CATEGORY", "assessmentDate": "2024-07-21", "classification": "Cat C", "nextReviewDate": "2024-06-21", "assessmentStatus": "I", "agencyDescription": "Prison (HMP)", "assessmentComment": "Cat-tool Initial", "assessmentAgencyId": "PRI", "classificationCode": "C", "approvalDateDisplay": "27/07/2024", "cellSharingAlertFlag": false, "assessmentDescription": "Categorisation"}], "socProfile": {"nomsId": "123ABC", "riskType": "SOC", "transferToSecurity": false, "provisionalCategorisation": "C"}, "escapeProfile": {"nomsId": "123ABC", "riskType": "ESCAPE", "activeEscapeList": false, "activeEscapeRisk": false, "escapeListAlerts": [], "escapeRiskAlerts": [], "provisionalCategorisation": "C"}, "violenceProfile": {"nomsId": "123ABC", "riskType": "VIOLENCE", "displayAssaults": false, "numberOfAssaults": 0, "notifySafetyCustodyLead": false, "numberOfSeriousAssaults": 0, "provisionalCategorisation": "C", "numberOfNonSeriousAssaults": 0, "veryHighRiskViolentOffender": false}, "extremismProfile": {"nomsId": "123ABC", "riskType": "EXTREMISM", "notifyRegionalCTLead": false, "increasedRiskOfExtremism": false, "provisionalCategorisation": "C"}}',
       'NMI'::character varying,
       '123ABC',
       '2020-05-18 13:58:42.435 +0100',
       'TEST2_GEN',
       '2020-05-20 13:58:42.435 +0100',
       '2021-05-18',
       'RECAT'::cat_type_enum,
       0,
       '2019-07-16',
       'TEST_GEN',
       'TEST_GEN',
       'DUE',
       '2020-05-01'
  );
INSERT INTO public.form
(form_response, booking_id, user_id, status, assigned_user_id, referred_date, referred_by, sequence_no, risk_profile, prison_id, offender_no, start_date, security_reviewed_by, security_reviewed_date, approval_date, cat_type, nomis_sequence_no, assessment_date, approved_by, assessed_by, review_reason, due_by_date)
VALUES('{"recat": {"decision": {"category": "B"}, "oasysInput": {"date": "09/06/2022", "oasysRelevantInfo": "No"}, "securityBack": {}, "securityInput": {"securityNoteNeeded": "No", "securityInputNeeded": "Yes"}, "nextReviewDate": {"date": "06/03/2023", "indeterminate": "false"}, "riskAssessment": {"lowerCategory": "There is no evidence of something or other", "otherRelevant": "No", "higherCategory": "There is no evidence to suggest something.", "otherRelevantText": ""}, "prisonerBackground": {"offenceDetails": "Something about offences"}}, "security": {"review": {"securityReview": "There is nothing further to add to this assessment."}}, "supervisor": {"review": {"proposedCategory": "B", "supervisorCategoryAppropriate": "Yes"}}, "openConditionsRequested": false}',
       0,
       'TEST_GEN',
       'APPROVED',
       'TEST2_GEN',
       '2019-11-25 11:24:12.768',
       '',
       1,
       '{"history": {"catAType": null, "finalCat": null, "catAEndYear": null, "releaseYear": null, "catAStartYear": null}, "offences": [{"caseId": 1234, "bookingId": 4567, "courtDate": "2012-10-15", "mostSerious": false, "offenceCode": "SOMETHING", "offenceDate": "2010-02-12", "statuteCode": "TBD", "primaryResultCode": "1234", "offenceDescription": "Something", "primaryResultConviction": true, "primaryResultDescription": "Detention", "secondaryResultConviction": false}, {"caseId": 642646, "bookingId": 6757452, "courtDate": "2013-10-02", "mostSerious": false, "offenceCode": "SOMETHINGELSE", "offenceDate": "2013-01-06", "statuteCode": "GHD", "primaryResultCode": "64356", "offenceDescription": "Crime", "primaryResultConviction": true, "primaryResultDescription": "Detention", "secondaryResultConviction": false}, {"caseId": 63636, "bookingId": 78463, "courtDate": "2015-09-06", "mostSerious": false, "offenceCode": "SOME", "offenceDate": "2015-02-01", "statuteCode": "FGERG", "primaryResultCode": "6463", "offenceDescription": "More crime", "primaryResultConviction": true, "primaryResultDescription": "Imprisonment", "secondaryResultConviction": false}, {"caseId": 754747, "bookingId": 553463, "courtDate": "2018-10-11", "mostSerious": false, "offenceCode": "GUOGO", "offenceDate": "2017-01-05", "statuteCode": "GUOGO", "primaryResultCode": "546363", "offenceDescription": "Crime again", "primaryResultConviction": true, "primaryResultDescription": "Imprisonment", "secondaryResultConviction": false}, {"caseId": 7648643, "bookingId": 6425426, "courtDate": "2018-10-11", "mostSerious": false, "offenceCode": "UOGPUGP", "offenceDate": "2018-08-06", "statuteCode": "GUOGUP", "primaryResultCode": "643634", "offenceDescription": "Some crime", "primaryResultConviction": true, "primaryResultDescription": "Imprisonment", "secondaryResultConviction": false}, {"caseId": 6347474, "bookingId": 4325245, "courtDate": "2020-10-01", "mostSerious": false, "offenceCode": "UOGPGBP", "offenceDate": "2020-09-06", "statuteCode": "HUIPHGP", "primaryResultCode": "6356", "offenceDescription": "Drug crime", "primaryResultConviction": true, "primaryResultDescription": "Imprisonment", "secondaryResultConviction": false}], "socProfile": {"nomsId": "123ABC", "riskType": "SOC", "transferToSecurity": false, "provisionalCategorisation": "C"}, "lifeProfile": {"life": false, "nomsId": "123ABC", "riskType": "LIFE", "provisionalCategorisation": "C"}, "escapeProfile": {"nomsId": "123ABC", "riskType": "ESCAPE", "activeEscapeList": false, "activeEscapeRisk": false, "escapeListAlerts": [], "escapeRiskAlerts": [], "provisionalCategorisation": "C"}, "violenceProfile": {"nomsId": "123ABC", "riskType": "VIOLENCE", "displayAssaults": true, "numberOfAssaults": 2, "notifySafetyCustodyLead": false, "numberOfSeriousAssaults": 0, "provisionalCategorisation": "C", "numberOfNonSeriousAssaults": 0, "veryHighRiskViolentOffender": false}, "extremismProfile": {"nomsId": "123ABC", "riskType": "EXTREMISM", "notifyRegionalCTLead": false, "increasedRiskOfExtremism": false, "provisionalCategorisation": "C"}}',
       'NMI'::character varying,
       '123ABC',
       '2019-05-18 13:58:42.435 +0100',
       'TEST2_GEN',
       '2020-05-20 13:58:42.435 +0100',
       '2020-05-18',
       'RECAT'::cat_type_enum,
       0,
       '2020-05-01',
       'TEST_GEN',
       'TEST_GEN',
       'DUE',
       '2020-05-01'
  );
INSERT INTO public.form
(form_response, booking_id, user_id, status, assigned_user_id, referred_date, referred_by, sequence_no, risk_profile, prison_id, offender_no, start_date, security_reviewed_by, security_reviewed_date, approval_date, cat_type, nomis_sequence_no, assessment_date, approved_by, assessed_by, review_reason, due_by_date)
VALUES('{"ratings": {"escapeRating": {"escapeCatB": "No", "escapeOtherEvidence": "No"}, "securityInput": {"securityInputNeeded": "No"}, "furtherCharges": {"furtherCharges": "No"}, "nextReviewDate": {"date": "16/01/2019", "indeterminate": "false"}, "violenceRating": {"seriousThreat": "No", "highRiskOfViolence": "No"}, "extremismRating": {"previousTerrorismOffences": "No"}, "offendingHistory": {"previousConvictions": "No"}}, "supervisor": {"review": {"proposedCategory": "C", "supervisorCategoryAppropriate": "Yes"}}, "categoriser": {"review": {}, "provisionalCategory": {"suggestedCategory": "C", "categoryAppropriate": "Yes", "otherInformationText": "Not suitable for Cat D at this time, further assessment needed."}}}',
       0,
       'TEST_GEN',
       'APPROVED',
       'TEST2_GEN',
       '2019-11-25 11:24:12.768',
       '',
       1,
       '{"history":{},"socProfile":{},"escapeProfile":{"nomsId":"G4143VX","riskType":"ESCAPE","provisionalCategorisation":"C"},"violenceProfile":{"nomsId":"G4143VX","riskType":"VIOLENCE","displayAssaults":false,"numberOfAssaults":0,"notifySafetyCustodyLead":false,"numberOfSeriousAssaults":0,"provisionalCategorisation":"C","veryHighRiskViolentOffender":false},"extremismProfile":{}}',
       'NMI'::character varying,
       '123ABC',
       '2018-05-18 13:58:42.435 +0100',
       'TEST2_GEN',
       '2020-05-20 13:58:42.435 +0100',
       '2019-05-18',
       'INITIAL'::cat_type_enum,
       0,
       '2019-07-16',
       'TEST_GEN',
       'TEST_GEN',
       'DUE',
       '2020-05-01'
  );
INSERT INTO public.form
(form_response, booking_id, user_id, status, assigned_user_id, referred_date, referred_by, sequence_no, risk_profile, prison_id, offender_no, start_date, security_reviewed_by, security_reviewed_date, approval_date, cat_type, nomis_sequence_no, assessment_date, approved_by, assessed_by, review_reason, due_by_date)
VALUES('{"recat":{"decision":{"category":"B"},"securityBack":{},"securityInput":{"securityInputNeeded":"Yes","securityInputNeededText":"text from recat"},"nextReviewDate":{"date":"2/8/2019"},"riskAssessment":{"lowerCategory":"lower text","otherRelevant":"Yes","higherCategory":"higher text","otherRelevantText":"rel info"},"prisonerBackground":{"offenceDetails":"offence details text"},"higherSecurityReview":{"steps":"steps text","transfer":"Yes","behaviour":"higher security text","conditions":"security conditions","transferText":"manage text"}},"security":{"review":{"securityReview":"security review text"}},"supervisor":{"review":{"proposedCategory":"B","otherInformationText":"other rel info","supervisorCategoryAppropriate":"Yes"}},"openConditionsRequested":false}',
       0,
       'TEST_GEN',
       'APPROVED',
       'TEST2_GEN',
       '2019-11-25 11:24:12.768',
       '',
       1,
       '{"history":{},"socProfile":{},"escapeProfile":{"nomsId":"G4143VX","riskType":"ESCAPE","provisionalCategorisation":"C"},"violenceProfile":{"nomsId":"G4143VX","riskType":"VIOLENCE","displayAssaults":false,"numberOfAssaults":0,"notifySafetyCustodyLead":false,"numberOfSeriousAssaults":0,"provisionalCategorisation":"C","veryHighRiskViolentOffender":false},"extremismProfile":{}}',
       'NMI'::character varying,
       '123ABC',
       '2021-05-18 13:58:42.435 +0100',
       'TEST2_GEN',
       '2020-05-20 13:58:42.435 +0100',
       '2021-05-18',
       'RECAT'::cat_type_enum,
       0,
       '2022-05-18',
       'TEST_GEN',
       'TEST_GEN',
       'DUE',
       '2020-05-01'
  );


INSERT INTO risk_profiler.PREVIOUS_PROFILE
(OFFENDER_NO, ESCAPE, EXTREMISM, SOC, VIOLENCE, EXECUTE_DATE_TIME)
VALUES('123ABC', '{}', '{}', '{}', '{"nomsId": "123ABC", "provisionalCategorisation": "C", "veryHighRiskViolentOffender": false, "notifySafetyCustodyLead": false, "displayAssaults": false, "numberOfAssaults": 0, "numberOfSeriousAssaults": 0, "numberOfNonSeriousAssaults": 0,"riskType": "VIOLENCE"}', '2021-09-22 10:25:44.395 +0100');