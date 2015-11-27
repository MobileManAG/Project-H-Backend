/*******************************************************************************
 * Copyright 2015 MobileMan GmbH
 * www.mobileman.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

ALTER TABLE medicine_class ADD COLUMN pzn character varying(7);
ALTER TABLE medicine_class ADD COLUMN standard_unit_size character varying(255);
ALTER TABLE medicine_class ADD COLUMN locale character varying(255);
update medicine_class set locale = 'de_DE';
ALTER TABLE medicine_class ALTER COLUMN locale SET NOT NULL;
ALTER TABLE ONLY medicine_class ADD CONSTRAINT uk_medication_pzn UNIQUE (pzn);

CREATE TABLE disease (
    ID BIGINT DEFAULT NEXTVAL('HIBERNATE_SEQUENCE'::REGCLASS) NOT NULL
);

ALTER TABLE public.disease OWNER TO projecth;
ALTER TABLE ONLY disease ADD CONSTRAINT disease_pkey PRIMARY KEY (ID);
CREATE TABLE patient_medication (
    id bigint NOT NULL,
    "timestamp" timestamp without time zone NOT NULL,
    patient_id bigint NOT NULL,
    disease_id bigint NOT NULL,
    medicine_id bigint NOT NULL,
    amount numeric(19,2) NOT NULL
);

ALTER TABLE public.patient_medication OWNER TO projecth;
ALTER TABLE ONLY patient_medication ADD CONSTRAINT patient_medication_pkey PRIMARY KEY (id);
ALTER TABLE ONLY patient_medication ADD CONSTRAINT fk_patient_medication_disease_id FOREIGN KEY (disease_id) REFERENCES disease(id);
ALTER TABLE ONLY patient_medication ADD CONSTRAINT fk_patient_medication_medicine_id FOREIGN KEY (medicine_id) REFERENCES medicine_class(id);
ALTER TABLE ONLY patient_medication ADD CONSTRAINT fk_patient_medication_patient_id FOREIGN KEY (patient_id) REFERENCES patient(id); 

CREATE TABLE "user" (
    ID BIGINT DEFAULT NEXTVAL('HIBERNATE_SEQUENCE'::REGCLASS) NOT NULL,
    USER_TYPE CHARACTER(1) NOT NULL,
    --USER_STATE CHARACTER(1) NOT NULL,
    NAME CHARACTER VARYING(255) NOT NULL,
    DESCRIPTION CHARACTER VARYING(4000),
    --EMAIL  CHARACTER VARYING(50) NOT NULL UNIQUE,
    ACTIVATION_UID  CHARACTER VARYING(255),
    SYSUSER_ID BIGINT NOT NULL,
    RESULTID CHARACTER VARYING(50),
    ACTIVATIONCODE CHARACTER VARYING(50),
    SEX INTEGER,
    GENDER CHARACTER VARYING(20),
    BIRTHDAY INTEGER,
    INITIALSYMPTOMDATE DATE,
    INITIALDIAGNOSISDATE DATE,
    TIMESYMPTOMDIAGNOSTIC INTEGER,
    CREATED TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    LAST_LOGIN TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL
);
ALTER TABLE "user" OWNER TO projecth;
ALTER TABLE ONLY "user" ADD CONSTRAINT USER_PKEY PRIMARY KEY (ID);
ALTER TABLE ONLY "user" ADD CONSTRAINT FK_USER_SYSUSER_ID FOREIGN KEY (SYSUSER_ID) REFERENCES SYSUSER(ID);

CREATE TABLE USER_CONNECTION (
    ID BIGINT NOT NULL,
    STATE CHARACTER(1) NOT NULL,
    OWNER_ID BIGINT NOT NULL,
    USER_ID BIGINT NOT NULL,
    CREATED TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL
);
ALTER TABLE USER_CONNECTION OWNER TO projecth;
ALTER TABLE ONLY USER_CONNECTION ADD CONSTRAINT USER_CONNECTION_PKEY PRIMARY KEY (ID);
ALTER TABLE ONLY USER_CONNECTION ADD CONSTRAINT FK_USER_CONNECTION_OWNER_ID FOREIGN KEY (OWNER_ID) REFERENCES "user"(ID);
ALTER TABLE ONLY USER_CONNECTION ADD CONSTRAINT FK_USER_CONNECTION_USER_ID FOREIGN KEY (USER_ID) REFERENCES "user"(ID);
ALTER TABLE USER_CONNECTION ADD CONSTRAINT UK_OWNER_ID_USER_ID UNIQUE (OWNER_ID,USER_ID);

INSERT INTO "user"(ID, USER_TYPE, EMAIL, RESULTID, ACTIVATIONCODE, SEX, GENDER, NAME, BIRTHDAY, INITIALSYMPTOMDATE, INITIALDIAGNOSISDATE, TIMESYMPTOMDIAGNOSTIC, CREATED, SYSUSER_ID)
SELECT ID,'P', 'patient@mail.com', RESULTID, ACTIVATIONCODE, SEX, GENDER, NAME, BIRTHDAY, INITIALSYMPTOMDATE, INITIALDIAGNOSISDATE, TIMESYMPTOMDIAGNOSTIC, CREATED, SYSUSER_ID FROM PATIENT;
INSERT INTO "user"(ID, USER_TYPE, EMAIL, NAME, DESCRIPTION, SYSUSER_ID)
SELECT ID,'C', 'customer@mail.com', NAME, DESCRIPTION, SYSUSER_ID FROM CUSTOMER;
INSERT INTO "user"(ID, USER_TYPE, EMAIL, NAME, SYSUSER_ID)
SELECT ID,'D', 'doctor@mail.com', NAME, SYSUSER_ID FROM DOCTOR;

INSERT INTO USER_CONNECTION(ID, STATE, OWNER_ID, USER_ID, CREATED)
SELECT ID, 'P', CUSTOMER_ID, DOCTOR_ID, 'now' FROM CUSTOMER_DOCTOR;
INSERT INTO USER_CONNECTION(ID, STATE, OWNER_ID, USER_ID, CREATED)
SELECT ID, 'P', "FK DoctorID", "FK PatientID", 'now' FROM DOCTOR_PATIENT;

--FK on customer_doctor
ALTER TABLE customer_doctor DROP CONSTRAINT fk_cust_doc_customer_id;
ALTER TABLE customer_doctor DROP CONSTRAINT fk_cust_doc_doctor_id;
ALTER TABLE customer_doctor ADD CONSTRAINT fk_cust_doc_customer_id FOREIGN KEY (customer_id) REFERENCES "user" (id);
ALTER TABLE customer_doctor ADD CONSTRAINT fk_cust_doc_doctor_id FOREIGN KEY (doctor_id) REFERENCES "user" (id);

--FK on doctor_patient
ALTER TABLE doctor_patient DROP CONSTRAINT fk_doc_pat_rel_doctor_id;
ALTER TABLE doctor_patient DROP CONSTRAINT fk_doc_pat_rel_patient_id;
ALTER TABLE doctor_patient ADD CONSTRAINT fk_doc_pat_rel_doctor_id FOREIGN KEY ("FK DoctorID") REFERENCES "user" (id);
ALTER TABLE doctor_patient ADD CONSTRAINT fk_doc_pat_rel_patient_id FOREIGN KEY ("FK PatientID") REFERENCES "user" (id);      

--FK on doctor_patient_questionary_validation
ALTER TABLE doctor_patient_questionary_validation DROP CONSTRAINT fk_doc_pat_overr_patient_id;
ALTER TABLE doctor_patient_questionary_validation ADD CONSTRAINT fk_doc_pat_overr_patient_id FOREIGN KEY (patient_id) REFERENCES "user" (id);

--FK on patient_answer
ALTER TABLE patient_answer DROP CONSTRAINT fk_patiententry_patient_id;
ALTER TABLE patient_answer ADD CONSTRAINT fk_patiententry_patient_id FOREIGN KEY (fk_patient) REFERENCES "user" (id);

--FK on patient_group_relation
ALTER TABLE patient_group_relation DROP CONSTRAINT fk_pat_grp_rel_patient_id;
ALTER TABLE patient_group_relation ADD CONSTRAINT fk_pat_grp_rel_patient_id FOREIGN KEY (fk_patientid) REFERENCES "user" (id);

--FK on patient_medication
ALTER TABLE patient_medication DROP CONSTRAINT fk_patient_medication_patient_id;
ALTER TABLE patient_medication ADD CONSTRAINT fk_patient_medication_patient_id FOREIGN KEY (patient_id) REFERENCES "user" (id);

--FK on patient_medicine_relation
ALTER TABLE patient_medicine_relation DROP CONSTRAINT fk_pat_med_rel_patient_id;
ALTER TABLE patient_medicine_relation ADD CONSTRAINT fk_pat_med_rel_patient_id FOREIGN KEY (fk_patientid) REFERENCES "user" (id);

--FK on patient_scanner_data
ALTER TABLE patient_scanner_data DROP CONSTRAINT fk_pat_scann_dat_patient_id;
ALTER TABLE patient_scanner_data ADD CONSTRAINT fk_pat_scann_dat_patient_id FOREIGN KEY (patient_id) REFERENCES "user" (id);

ALTER TABLE sysuser RENAME TO user_account;
ALTER TABLE "user" RENAME sysuser_id  TO user_account_id;
ALTER TABLE "user" ALTER COLUMN user_account_id DROP NOT NULL;

DROP VIEW patient_answer_result_set_timeline;
DROP VIEW patient_answer_result_set;
DROP VIEW patient_medicine;

CREATE OR REPLACE VIEW patient_medicine AS 
 SELECT u.id, u.resultid, u.gender, u.birthday, u.initialsymptomdate, u.initialdiagnosisdate, patient_medicine_relation.fk_patientid, patient_medicine_relation.fk_medicineid, medicine_class.name, patient_medicine_relation."Value", 
        CASE
            WHEN patient_medicine_relation."Value"::text = '0'::text THEN 'Nein'::character varying
            WHEN patient_medicine_relation."Value"::text = '1'::text THEN 'Ja'::character varying
            WHEN patient_medicine_relation."Value"::text = '-1'::text THEN 'Keine Angabe'::character varying
            ELSE patient_medicine_relation."Value"
        END AS result
   FROM "user" u
   LEFT JOIN patient_medicine_relation ON u.id = patient_medicine_relation.fk_patientid
   LEFT JOIN medicine_class ON patient_medicine_relation.fk_medicineid = medicine_class.id
   WHERE u.user_type = 'P';
ALTER TABLE patient_medicine OWNER TO projecth;

CREATE OR REPLACE VIEW patient_answer_result_set AS 
 SELECT patient_answer.timestampcreated, patient_answer.logdate, patient_answer.fk_patient AS patientid, u.resultid AS patientresultid, u.gender AS patientgender, u.name AS patientname, u.birthday AS patientbirthday, date_part('year'::text, now()) - u.birthday::double precision AS patientage, u.initialsymptomdate AS patientinitialsymptomdate, u.initialdiagnosisdate AS patientinitialdiagnosisdate, haq.id AS haqid, haq."HAQ-Question" AS haq, haq.explanation AS haqexplanation, patient_answer.fk_questionaryid AS questionid, questionary.questiontext, patient_answer.answer_id, question_answer.answertext
   FROM "user" u
   RIGHT JOIN (patient_answer
   LEFT JOIN question_answer ON patient_answer.answer_id = question_answer.answerid AND patient_answer.fk_questionaryid = question_answer.fk_questionaryid) ON u.id = patient_answer.fk_patient AND u.id = patient_answer.fk_patient
   LEFT JOIN questionary ON patient_answer.fk_questionaryid = questionary.id
   LEFT JOIN haq ON questionary.fk_haqid = haq.id AND questionary.fk_haqid = haq.id
   WHERE u.user_type = 'P';
ALTER TABLE patient_answer_result_set OWNER TO projecth;

CREATE OR REPLACE VIEW patient_answer_result_set_timeline AS 
 SELECT time_dimension.thedate AS logdate, time_dimension.theday, time_dimension.themonth, time_dimension.theyear, time_dimension.dayofmonth, time_dimension.weekofyear, time_dimension.monthofyear, time_dimension.quarterofyear, time_dimension.fiscalperiod, patient_answer_result_set.timestampcreated, patient_answer_result_set.patientid, patient_answer_result_set.patientresultid, patient_answer_result_set.patientgender, patient_answer_result_set.patientname, patient_answer_result_set.patientbirthday, patient_answer_result_set.patientage, patient_answer_result_set.patientinitialsymptomdate, patient_answer_result_set.patientinitialdiagnosisdate, patient_answer_result_set.haqid, patient_answer_result_set.haq, patient_answer_result_set.haqexplanation, patient_answer_result_set.questionid, patient_answer_result_set.questiontext, patient_answer_result_set.answer_id, patient_answer_result_set.answertext
   FROM patient_answer_result_set
   RIGHT JOIN time_dimension ON patient_answer_result_set.logdate = time_dimension.thedate;
ALTER TABLE patient_answer_result_set_timeline OWNER TO projecth;

DELETE FROM DOCTOR_PATIENT;
DELETE FROM CUSTOMER_DOCTOR;
DELETE FROM CUSTOMER;
DELETE FROM DOCTOR;
DELETE FROM PATIENT;
--DROP TABLE DOCTOR_PATIENT;
--DROP TABLE CUSTOMER_DOCTOR;
DROP TABLE CUSTOMER;
DROP TABLE DOCTOR;
DROP TABLE PATIENT;

-- DISEASE GROUP
CREATE TABLE DISEASE_GROUP (
    ID BIGINT DEFAULT NEXTVAL('HIBERNATE_SEQUENCE'::REGCLASS) NOT NULL,
    CODE character varying(50) NOT NULL,
    NAME character varying(255) NOT NULL
);
ALTER TABLE DISEASE_GROUP OWNER TO projecth;
ALTER TABLE DISEASE_GROUP ADD CONSTRAINT DISEASE_GROUP_CODE_KEY UNIQUE(CODE);
ALTER TABLE ONLY DISEASE_GROUP ADD CONSTRAINT DISEASE_GROUP_PKEY PRIMARY KEY (ID);
-- DISEASE SUBGROUP
CREATE TABLE DISEASE_SUBGROUP (
    ID BIGINT DEFAULT NEXTVAL('HIBERNATE_SEQUENCE'::REGCLASS) NOT NULL,
    CODE character varying(50) NOT NULL,
    NAME character varying(255) NOT NULL,
    DISEASE_GROUP_ID BIGINT NOT NULL
);
ALTER TABLE DISEASE_SUBGROUP OWNER TO projecth;
ALTER TABLE DISEASE_SUBGROUP ADD CONSTRAINT DISEASE_SUBGROUP_CODE_KEY UNIQUE(CODE);
ALTER TABLE ONLY DISEASE_SUBGROUP ADD CONSTRAINT DISEASE_SUBGROUP_PKEY PRIMARY KEY (ID);
ALTER TABLE ONLY DISEASE_SUBGROUP ADD CONSTRAINT FK_DSG_DISEASE_GROUP_ID FOREIGN KEY (DISEASE_GROUP_ID) REFERENCES DISEASE_GROUP(ID);
-- DISEASE
ALTER TABLE DISEASE ADD COLUMN     CODE character varying(50) NOT NULL;
ALTER TABLE DISEASE ADD COLUMN     NAME character varying(255) NOT NULL;
ALTER TABLE DISEASE ADD COLUMN     IMAGE_NAME character varying(255);
ALTER TABLE DISEASE ADD COLUMN     DISEASE_SUBGROUP_ID BIGINT NOT NULL;
ALTER TABLE ONLY DISEASE ADD CONSTRAINT FK_D_DISEASE_SUBGROUP_ID FOREIGN KEY (DISEASE_SUBGROUP_ID) REFERENCES DISEASE_SUBGROUP(ID);
ALTER TABLE DISEASE ADD CONSTRAINT DISEASE_CODE_KEY UNIQUE(CODE);
-- HAQ
ALTER TABLE HAQ ADD COLUMN DISEASE_ID BIGINT; 
ALTER TABLE ONLY HAQ ADD CONSTRAINT FK_HAQ_DISEASE_ID FOREIGN KEY (DISEASE_ID) REFERENCES DISEASE(ID);
--USER_DISEASE
CREATE TABLE USER_DISEASE (
    ID BIGINT DEFAULT NEXTVAL('HIBERNATE_SEQUENCE'::REGCLASS) NOT NULL,
    USER_ID BIGINT NOT NULL,
    DISEASE_ID BIGINT NOT NULL
);
ALTER TABLE USER_DISEASE OWNER TO projecth;
ALTER TABLE ONLY USER_DISEASE ADD CONSTRAINT USER_DISEASE_PKEY PRIMARY KEY (ID);
ALTER TABLE ONLY USER_DISEASE ADD CONSTRAINT FK_USER_DISEASE_USER_ID FOREIGN KEY (USER_ID) REFERENCES "user"(ID);
ALTER TABLE ONLY USER_DISEASE ADD CONSTRAINT FK_USER_DISEASE_DISEASE_ID FOREIGN KEY (DISEASE_ID) REFERENCES DISEASE(ID);

-- DISEASE_GROUP
INSERT INTO disease_group(code, "name") VALUES('A00-B99', 'Certain infectious and parasitic diseases');
INSERT INTO disease_group(code, "name") VALUES('C00-D48', 'Neoplasms');
INSERT INTO disease_group(code, "name") VALUES('D50-D89', 'Diseases of the blood and blood-forming organs and certain disorders involving the immune mechanism');
INSERT INTO disease_group(code, "name") VALUES('E00-E90', 'Endocrine, nutritional and metabolic diseases');
INSERT INTO disease_group(code, "name") VALUES('F00-F99', 'Mental and behavioural disorders');
INSERT INTO disease_group(code, "name") VALUES('G00-G99', 'Diseases of the nervous system');
INSERT INTO disease_group(code, "name") VALUES('H00-H59', 'Diseases of the eye and adnexa');
INSERT INTO disease_group(code, "name") VALUES('H60-H95', 'Diseases of the ear and mastoid process');
INSERT INTO disease_group(code, "name") VALUES('I00-I99', 'Diseases of the circulatory system');
INSERT INTO disease_group(code, "name") VALUES('J00-J99', 'Diseases of the respiratory system');
INSERT INTO disease_group(code, "name") VALUES('K00-K93', 'Diseases of the digestive system');
INSERT INTO disease_group(code, "name") VALUES('L00-L99', 'Diseases of the skin and subcutaneous tissue');
INSERT INTO disease_group(code, "name") VALUES('M00-M99', 'Diseases of the musculoskeletal system and connective tissue');
INSERT INTO disease_group(code, "name") VALUES('N00-N99', 'Diseases of the genitourinary system');
INSERT INTO disease_group(code, "name") VALUES('O00-O99', 'Pregnancy, childbirth and the puerperium');
INSERT INTO disease_group(code, "name") VALUES('P00-P96', 'Certain conditions originating in the perinatal period');
INSERT INTO disease_group(code, "name") VALUES('Q00-Q99', 'Congenital malformations, deformations and chromosomal abnormalities');
INSERT INTO disease_group(code, "name") VALUES('R00-R99', 'Symptoms, signs and abnormal clinical and laboratory findings, not elsewhere classified');
INSERT INTO disease_group(code, "name") VALUES('S00-T98', 'Injury, poisoning and certain other consequences of external causes');
INSERT INTO disease_group(code, "name") VALUES('V01-Y98', 'External causes of morbidity and mortality');
INSERT INTO disease_group(code, "name") VALUES('Z00-Z99', 'Factors influencing health status and contact with health services');
INSERT INTO disease_group(code, "name") VALUES('U00-U99', 'Codes for special purposes');

-- M79.0 - Rheumatism, unspecified 

INSERT INTO disease_subgroup(code, "name", disease_group_id) SELECT 'M79', 'Other soft tissue disorders, not elsewhere classified', ID FROM disease_group WHERE code = 'M00-M99';
INSERT INTO disease(id, code, "name", disease_subgroup_id) SELECT NEXTVAL('HIBERNATE_SEQUENCE'::REGCLASS), 'M79.0', 'Rheumatism, unspecified', ID FROM disease_subgroup WHERE code = 'M79';

-- M08.1, M45 
INSERT INTO disease_subgroup(code, "name", disease_group_id) SELECT 'M08', 'Juvenile arthritis', ID FROM disease_group WHERE code = 'M00-M99';
INSERT INTO disease(id, code, "name", disease_subgroup_id) SELECT NEXTVAL('HIBERNATE_SEQUENCE'::REGCLASS), 'M08.1', 'Juvenile ankylosing spondylitis', ID FROM disease_subgroup WHERE code = 'M08';
INSERT INTO disease_subgroup(code, "name", disease_group_id) SELECT 'M45', 'Ankylosing spondylitis', ID FROM disease_group WHERE code = 'M00-M99';
INSERT INTO disease(id, code, "name", disease_subgroup_id) SELECT NEXTVAL('HIBERNATE_SEQUENCE'::REGCLASS), 'M45', 'Ankylosing spondylitis', ID FROM disease_subgroup WHERE code = 'M45';

-- L40
INSERT INTO disease_subgroup(code, "name", disease_group_id) SELECT 'L40', 'Psoriasis', ID FROM disease_group WHERE code = 'L00-L99';
INSERT INTO disease(id, code, "name", disease_subgroup_id) SELECT NEXTVAL('HIBERNATE_SEQUENCE'::REGCLASS), 'L40.0', 'Psoriasis vulgaris', ID FROM disease_subgroup WHERE code = 'L40';
INSERT INTO disease(id, code, "name", disease_subgroup_id) SELECT NEXTVAL('HIBERNATE_SEQUENCE'::REGCLASS), 'L40.1', 'AGeneralized pustular psoriasis', ID FROM disease_subgroup WHERE code = 'L40';
INSERT INTO disease(id, code, "name", disease_subgroup_id) SELECT NEXTVAL('HIBERNATE_SEQUENCE'::REGCLASS), 'L40.2', 'Acrodermatitis continua', ID FROM disease_subgroup WHERE code = 'L40';
INSERT INTO disease(id, code, "name", disease_subgroup_id) SELECT NEXTVAL('HIBERNATE_SEQUENCE'::REGCLASS), 'L40.3', 'Pustulosis palmaris et plantaris', ID FROM disease_subgroup WHERE code = 'L40';
INSERT INTO disease(id, code, "name", disease_subgroup_id) SELECT NEXTVAL('HIBERNATE_SEQUENCE'::REGCLASS), 'L40.4', 'Guttate psoriasis', ID FROM disease_subgroup WHERE code = 'L40';
INSERT INTO disease(id, code, "name", disease_subgroup_id) SELECT NEXTVAL('HIBERNATE_SEQUENCE'::REGCLASS), 'L40.5', 'Arthropathic psoriasis', ID FROM disease_subgroup WHERE code = 'L40';
INSERT INTO disease(id, code, "name", disease_subgroup_id) SELECT NEXTVAL('HIBERNATE_SEQUENCE'::REGCLASS), 'L40.8', 'Other psoriasis', ID FROM disease_subgroup WHERE code = 'L40';
INSERT INTO disease(id, code, "name", disease_subgroup_id) SELECT NEXTVAL('HIBERNATE_SEQUENCE'::REGCLASS), 'L40.9', 'Psoriasis, unspecified', ID FROM disease_subgroup WHERE code = 'L40';

-- HAQ
UPDATE HAQ SET DISEASE_ID = (SELECT ID FROM DISEASE WHERE CODE = 'M79.0');
ALTER TABLE HAQ ALTER COLUMN DISEASE_ID SET NOT NULL;

ALTER TABLE "user" ADD COLUMN USER_STATE CHARACTER(1);
UPDATE "user" SET USER_STATE = 'A';
ALTER TABLE "user" ALTER COLUMN USER_STATE SET NOT NULL;
ALTER TABLE "user" ADD CONSTRAINT USER_EMAIL_KEY UNIQUE(EMAIL);

ALTER TABLE "user" ADD COLUMN connection_requests_blocked boolean NOT NULL DEFAULT false;

ALTER TABLE user_account ADD COLUMN email character varying(50);
update user_account uc set email = (select u.email from "user" u where u.user_account_id = uc.id);
ALTER TABLE user_account ALTER COLUMN email SET NOT NULL;
ALTER TABLE user_account ADD CONSTRAINT uk_user_email_key UNIQUE(email);
ALTER TABLE "user" DROP COLUMN email;
