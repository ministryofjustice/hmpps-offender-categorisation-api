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
       '2019-11-25 11:24:12.768+00',
       '',
       1,
       '{"catHistory":{},"socProfile":{},"escapeProfile":{"nomsId":"G4143VX","riskType":"ESCAPE","provisionalCategorisation":"C"},"violenceProfile":{"nomsId":"G4143VX","riskType":"VIOLENCE","displayAssaults":false,"numberOfAssaults":0,"notifySafetyCustodyLead":false,"numberOfSeriousAssaults":0,"provisionalCategorisation":"C","veryHighRiskViolentOffender":false},"extremismProfile":{}}',
       'NMI'::character varying,
       'G8105VR',
       '2019-11-25 11:24:12.768+00',
       'LBENNETT_GEN',
       '2019-11-25 11:24:12.768+00',
       '2019-06-25',
       'INITIAL'::cat_type_enum,
       0,
       '2019-07-16',
       'SRENDELL_GEN',
       'SRENDELL_GEN',
       'DUE',
       '2017-04-01',
       '2019-11-25 11:24:12.768+00',
       'SRENDELL_GEN');