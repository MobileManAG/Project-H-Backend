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

delete from user_granted_right;
delete from user_weight;
delete from patient_kpi_validation_data;
delete from patient_key_performance_indicator_validation;
delete from patient_key_performance_indicator;
update question_type set user_id = null;
delete from message;
delete from patient_question_answer;
delete from haq_chart_question;
delete from haq_chart;
delete from chart_type;
delete from question;
delete from patient_key_performance_indicator;
delete from patient_medication;
delete from patient_medicine_relation;
delete from haq;
delete from medicine_class;
delete from key_performance_indicator_type;
delete from user_disease;
delete from disease;
delete from disease_subgroup;
delete from disease_group;
delete from user_connection;
delete from "user";
delete from user_account;
delete from question_type_answer;
delete from question_type_tag;
delete from question_type;
delete from country;
delete from education;
delete from race;
delete from family_situation;
delete from "system";

ALTER TABLE "user" ALTER COLUMN unsuccessful_logins_count SET DEFAULT 0;
ALTER TABLE "user" ALTER COLUMN activation_state SET DEFAULT 1;
ALTER TABLE question_type_answer ALTER COLUMN kind SET DEFAULT 0;
ALTER TABLE patient_question_answer ALTER COLUMN kind SET DEFAULT 0;
ALTER TABLE haq ALTER COLUMN kind SET DEFAULT 0;
ALTER TABLE message ALTER COLUMN id SET DEFAULT nextval('hibernate_sequence');
ALTER TABLE message ALTER COLUMN created SET DEFAULT now();
ALTER TABLE message ALTER COLUMN readed SET DEFAULT false;
ALTER TABLE message ALTER COLUMN spam SET DEFAULT false;

ALTER SEQUENCE hibernate_sequence RESTART WITH 3000;

INSERT INTO "system"(id, model_version) VALUES (1, '0.1.2.0');

INSERT INTO country(id, code, "name", locale) VALUES (1, 'DE', 'Germany', 'de_DE');
INSERT INTO country(id, code, "name", locale) VALUES (2, 'CH', 'Switzerland', 'de_CH');
INSERT INTO country(id, code, "name", locale) VALUES (3, 'AT', 'Austria', 'de_AT');
INSERT INTO country(id, code, "name", locale) VALUES (4, 'LI', 'Fürstentum Liechtenstein', 'de_LI');

INSERT INTO education(id, code, "name") VALUES (1, 'Obligatorische Schule', 'Obligatorische Schule');
INSERT INTO education(id, code, "name") VALUES (2, 'Fachschule', 'Fachschule');
INSERT INTO education(id, code, "name") VALUES (3, 'Hochschule', 'Hochschule');
INSERT INTO education(id, code, "name") VALUES (4, 'Fachhochschule', 'Fachhochschule');
INSERT INTO education(id, code, "name") VALUES (5, 'Universität', 'Universität');
INSERT INTO education(id, code, "name") VALUES (6, 'Master', 'Master');
INSERT INTO education(id, code, "name") VALUES (7, 'Dr.', 'Dr.');
INSERT INTO education(id, code, "name") VALUES (8, 'Professor', 'Professor');

INSERT INTO race(id, code, "name") VALUES (1, 'Weiss', 'Weiss');
INSERT INTO race(id, code, "name") VALUES (2, 'Schwarz/dunkelhäutig', 'Schwarz/dunkelhäutig');
INSERT INTO race(id, code, "name") VALUES (3, 'Europäisch', 'Wuropäisch');
INSERT INTO race(id, code, "name") VALUES (4, 'Lateinamerikanisch', 'Lateinamerikanisch');
INSERT INTO race(id, code, "name") VALUES (5, 'Indisch', 'Indisch');
INSERT INTO race(id, code, "name") VALUES (6, 'Osteuropäisch', 'Osteuropäisch');
INSERT INTO race(id, code, "name") VALUES (7, 'Indianisch', 'Indianisch');
INSERT INTO race(id, code, "name") VALUES (8, 'Asiatisch', 'Asiatisch');
INSERT INTO race(id, code, "name") VALUES (9, 'Gemischt', 'Gemischt');
INSERT INTO race(id, code, "name") VALUES (10, 'Andere Ethnizität', 'Andere Ethnizität');

INSERT INTO family_situation(id, code, "name") VALUES (1, 'Allein lebend', 'Allein lebend');
INSERT INTO family_situation(id, code, "name") VALUES (2, '2-Personenpaarhaushalt', '2-Personenpaarhaushalt');
INSERT INTO family_situation(id, code, "name") VALUES (3, 'Paar mit Kind <25 Jahren', 'Paar mit Kind <25 Jahren');
INSERT INTO family_situation(id, code, "name") VALUES (4, 'Allein stehend mit Kind <25 Jahren', 'Allein stehend mit Kind <25 Jahren');
INSERT INTO family_situation(id, code, "name") VALUES (5, 'Anderer Familienhaushalt', 'Anderer Familienhaushalt');
INSERT INTO family_situation(id, code, "name") VALUES (6, 'Wohngemeinschaft', 'Wohngemeinschaft');
INSERT INTO family_situation(id, code, "name") VALUES (7, 'Nicht Familienhaushalt', 'Nicht Familienhaushalt');

INSERT INTO chart_type(id, description, "type") VALUES (1, 'A line chart shows trends in data at equal intervals', 0);
INSERT INTO chart_type(id, description, "type") VALUES (2, 'A pie chart shows the size of items that make up a data series, proportional to the sum of the items', 1);
INSERT INTO chart_type(id, description, "type") VALUES (3, 'A bar chart illustrates comparisons among individual items', 2);
INSERT INTO chart_type(id, description, "type") VALUES (4, 'A bubble chart is a type of xy (scatter) chart', 3);
INSERT INTO chart_type(id, description, "type") VALUES (5, 'An area chart emphasizes the magnitude of change over time', 4);
INSERT INTO chart_type(id, description, "type") VALUES (6, 'An xy (scatter) chart shows the relationships among the numeric values in several data series, or plots two groups of numbers as one series of xy coordinates', 5);

-- 'sysuser1' plainPassword: 12345 email: pat1@projecth.com
INSERT INTO user_account(id, email, "password", "login", created) VALUES (1, '64yP0ErKXYMMGzJSc++h5g==', '5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5', 'ps2Wp7x0wzfGgoMtd4KlAw==', now());
-- 'sysuser2' plainPassword: 67890 email: doc1@projecth.com
INSERT INTO user_account(id, email, "password", "login", created) VALUES (2, '/j0f7XakhJo6JJyQxo3Kiw==', 'e2217d3e4e120c6a3372a1890f03e232b35ad659d71f7a62501a4ee204a3e66d', 'YxH4lkvzq4VHQZykrRzyyw==', now());
-- 'sysuser3' plainPassword: 54321 email: pat2@projecth.com
INSERT INTO user_account(id, email, "password", "login", created) VALUES (3, 'PL5YZE1r/EyrFXLZsaIm1w==', '20f3765880a5c269b747e1e906054a4b4a3a991259f1e16b5dde4742cec2319a', 'Iy30wc9drJ170foucvRTGA==', now());

INSERT INTO "user"(id, resultid, activationcode, sex, "name", birthday, initialsymptomdate, initialdiagnosisdate, user_account_id, user_type, user_state, description, activation_uid, created, last_login, last_update)
    VALUES (4, 'pat1', 'pat1', 1, 'Pat1 Pat1', 1980, '2010-02-01', '2010-04-01', 1, 'P', 'A', 'pat1 description', 'pat1', '2010-04-01', '2010-04-01', now());
INSERT INTO "user"(id, resultid, activationcode, sex, "name", birthday, initialsymptomdate, initialdiagnosisdate, user_account_id, user_type, user_state, description, activation_uid, created, last_login, last_update)
    VALUES (5, 'pat2', 'pat2', 1, 'Pat2 Pat2', 1970, '2010-02-01', '2010-04-01', 2, 'P', 'A', 'pat2 description', 'pat2', '2010-04-01', '2010-04-01', now());
-- pat1 ID=4, pa2 ID=5
INSERT INTO "user"(id, "name", user_account_id, user_type, user_state, description, activation_uid, created, last_login, last_update) 
	VALUES(6, 'doctor1', 3, 'D', 'A', 'doc11 description', 'doc1', '2010-04-01', '2010-04-01', now());
-- admin
INSERT INTO user_account(id, email, "password", "login", created) VALUES (nextval('hibernate_sequence'), 'xX9SiJHAffHte2/n/txcUlNSc9S9T7Iic/MFtI2wg0U=', '65bb4b19d9e485fb57a3b02443619235b16bee7a52f0d256f51a0c226746b3cc', 'f+OFMquCxmqozeX60qHZkQ==', now());
INSERT INTO "user"(id, "name", user_account_id, user_type, user_state, description, created, last_login, last_update)
    VALUES (nextval('hibernate_sequence'), 'Administrator', (select ua.id from user_account ua where login='f+OFMquCxmqozeX60qHZkQ=='), 'A', 'A', 'Administrator', now(), now(), now());
-- projecth Testpatient
INSERT INTO user_account(id, email, "password", "login", created) VALUES (nextval('hibernate_sequence'), 'PYAWJKxC29vmUkTs2KqeBZt0i+5sIsZd/TXTaVhhmvg=', '65bb4b19d9e485fb57a3b02443619235b16bee7a52f0d256f51a0c226746b3cc', '5ZCNMNm5jc4QYzJZ59EJ1JcSPcP0Gv44YfGCMXoXGYY=', '2010-01-01');
INSERT INTO "user"(id, sex, "name", birthday, initialsymptomdate, initialdiagnosisdate, user_account_id, user_type, user_state, created, last_login, last_update) VALUES (nextval('hibernate_sequence'), 0, '5ZCNMNm5jc4QYzJZ59EJ1JcSPcP0Gv44YfGCMXoXGYY=', 1968, '2000-08-22', '2004-03-14', (select a.id from user_account a where a.login='5ZCNMNm5jc4QYzJZ59EJ1JcSPcP0Gv44YfGCMXoXGYY='), 'P', 'A', '2010-01-01', '2010-01-01', '2010-01-01');

INSERT INTO disease_group(id, code, "name") VALUES(426, 'M00-M99', 'Diseases of the musculoskeletal system and connective tissue');
INSERT INTO disease_group(id, code, "name") VALUES(425, 'L00-L99', 'Diseases of the skin and subcutaneous tissue');
INSERT INTO disease_subgroup(id, code, "name", disease_group_id) VALUES(436, 'M79', 'Other soft tissue disorders, not elsewhere classified', 426);
INSERT INTO disease_subgroup(id, code, "name", disease_group_id) VALUES(442, 'L40', 'Psoriasis', 425);
INSERT INTO disease_subgroup(id, code, "name", disease_group_id) VALUES(440, 'M45', 'Ankylosing spondylitis', 426);

INSERT INTO disease(id, code, "name", image_name, disease_subgroup_id) VALUES(437, 'M79.0', 'Polyarthritis (Rheuma)', 'imageName', 436);
INSERT INTO disease(id, code, "name", image_name, disease_subgroup_id) VALUES(443, 'L40.0', 'Psoriasis vulgaris', 'imageName', 442);
INSERT INTO disease(id, code, "name", image_name, disease_subgroup_id) VALUES(441, 'M45', 'Ankylosing spondylitis', '', 440);
--------- HAQ RHEUMA
INSERT INTO haq (id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (1, 0, 1, 'ACR Mannlein (Schwellung)', 'Erfassen Sie die geschwollenen Gelenke die mittels Punkt auf der Grafik markiert sind!', 437);
INSERT INTO haq (id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (2, 0, 2, 'ACR Mannlein (Druckempflindlichkeit)', 'Erfassen Sie die druckempfindlichen Gelenke die mittels Punkt auf der Grafik markiert sind!', 437);
INSERT INTO haq (id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (3, 0, 3, 'Morgensteifigkeit', 'Bitte geben Sie die Antwort an, die am Besten Ihren Zustand beschreibt!', 437);
INSERT INTO haq (id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (4, 0, 4, 'Allgemein Urteil des Patienten über seinen Gesundheitszustand', 'Bitte geben Sie in der Skala 1 bis 10 Ihren allgemeines Befinden ein!', 437);
INSERT INTO haq (id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (5, 0, 5, 'Psychologisches Befinden', 'Fragestellung: Bitte beantworten Sie jede Zeile mit "Ja" oder "Nein". Ich fühle mich heute...', 437);
INSERT INTO haq (id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (6, 0, 6, 'Eigenständigkeit', 'Bitte geben Sie die Antwort an, die am Besten Ihre Fähigkeit beschreibt!', 437);
INSERT INTO haq (id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (7, 0, 7, 'Arbeitsproduktivität', 'Bitte geben Sie die Antwort an, die am Besten Ihre Fähigkeit beschreibt!', 437);
--------- HAQ MORBUS BECHTEREW
INSERT INTO haq(id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (8, 0,   0, 'Müdigkeit / Erschöpfung', 'Müdigkeit / Erschöpfung', 441);
INSERT INTO haq(id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (9, 0,   1, 'Schmerzen', 'Schmerzen', 441);
INSERT INTO haq(id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (10, 0,  2, 'Schmerzen oder Schwellungen', 'Schmerzen oder Schwellungen', 441);
INSERT INTO haq(id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (11, 0,  3, 'Berührungs- oder Druckempfindliche Körperstellen', 'Berührungs- oder Druckempfindliche Körperstellen', 441);
INSERT INTO haq(id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (12, 0,  4, 'Morgensteifigkeit', 'Morgensteifigkeit', 441);
INSERT INTO haq(id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (13, 0,  5, 'Körperliche Tätigkeiten', 'Körperliche Tätigkeiten', 441);
INSERT INTO haq(id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (14, 0,  6, 'Aktiv', 'Aktiv', 441);
INSERT INTO haq(id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (15, 0,  7, 'Selbständigkeit', 'Selbständigkeit', 441);
INSERT INTO haq(id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (16, 0,  8, 'Allgemeine Tätigkeiten (Arbeitsproduktivität)', 'Allgemeine Tätigkeiten (Arbeitsproduktivität)', 441);
INSERT INTO haq(id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (17, 0,  9, 'Psychologisches Befinden', 'Psychologisches Befinden', 441);
INSERT INTO haq(id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (18, 0, 10, 'Augen', 'Augen', 441);
INSERT INTO haq(id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (19, 0, 11, 'Magen Darm', 'Magen Darm', 441);
INSERT INTO haq(id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (20, 0, 12, 'Haut', 'Haut', 441);
INSERT INTO haq(id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (21, 0, 13, 'Enthesien (Punkte)', 'Enthesien (Punkte)', 441);
-- HAQ ONE-TIME QUESTIONS
INSERT INTO haq(id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (22, 1, 0, 'Einmalige Abfrage (wird nur das erste Mal abgefragt)', 'Einmalige Abfrage (wird nur das erste Mal abgefragt)', 441);
INSERT INTO haq(id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (23, 1, 0, 'Einmalige Abfrage (wird nur das erste Mal abgefragt)', 'Einmalige Abfrage (wird nur das erste Mal abgefragt)', 437);
INSERT INTO haq(id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (24, 1, 0, 'Einmalige Abfrage (wird nur das erste Mal abgefragt)', 'Einmalige Abfrage (wird nur das erste Mal abgefragt)', 443);

INSERT INTO medicine_class(id, "name", expireddate, pzn, standard_unit_size, locale, createdatetime) VALUES (1, 'Kort 1', '2019-02-01', '2758089', '75mg', 'de_DE', now());
INSERT INTO medicine_class(id, "name", expireddate, pzn, standard_unit_size, locale, createdatetime) VALUES (2, 'Kort 2', '2019-01-01', '2758099', '100mg', 'de_DE', now());
INSERT INTO medicine_class(id, "name", expireddate, pzn, standard_unit_size, locale, createdatetime) VALUES (3, 'Kort 3 10mg', '2019-01-01', '2758100', '10mg', 'de_DE', now());
INSERT INTO medicine_class(id, "name", expireddate, pzn, standard_unit_size, locale, createdatetime) VALUES (4, 'Kort 3 100mg', '2019-01-01', '2758101', '100mg', 'de_DE', now());

INSERT INTO key_performance_indicator_type(id, "type", description, code, disease_id) VALUES (272,   0, 'CDAI', 'CDAI', 437);
INSERT INTO key_performance_indicator_type(id, "type", description, code, disease_id) VALUES (273,  1, 'DAS-28', 'DAS-28', 437);
INSERT INTO key_performance_indicator_type(id, "type", description, code, disease_id) VALUES (274, 2, 'Bath Ankylosing Spondylitis Disease Activity Index', 'BASDAI', 441);
INSERT INTO key_performance_indicator_type(id, "type", description, code, disease_id) VALUES (275, 3, 'Psoriasis Area Severity Index', 'PASI', 443);

INSERT INTO patient_key_performance_indicator(id, log_date, timestampcreated, kpi_value, validation_type_id, patient_id) VALUES (1, '2010-02-01', '2010-02-01', 2, 272, 4);
INSERT INTO patient_key_performance_indicator(id, log_date, timestampcreated, kpi_value, validation_type_id, patient_id) VALUES (2, '2010-02-02', '2010-02-02', 2, 272, 4);
INSERT INTO patient_key_performance_indicator(id, log_date, timestampcreated, kpi_value, validation_type_id, patient_id) VALUES (4, '2010-02-04', '2010-02-04', 1, 272, 4);
INSERT INTO patient_key_performance_indicator(id, log_date, timestampcreated, kpi_value, validation_type_id, patient_id) VALUES (3, '2010-02-05', '2010-02-03', 0, 272, 4);
INSERT INTO patient_key_performance_indicator(id, log_date, timestampcreated, kpi_value, validation_type_id, patient_id) VALUES (5, '2010-02-06', '2010-02-01', 1, 272, 4);
INSERT INTO patient_key_performance_indicator(id, log_date, timestampcreated, kpi_value, validation_type_id, patient_id) VALUES (6, '2010-02-07', '2010-02-01', 2, 272, 4);

INSERT INTO patient_medication(id, amount, "timestamp", cumsuption_date, medicine_id, patient_id, disease_id) VALUES (1, 1, '2010-11-01 15:53:00', '2010-11-01 15:53:00', 1, 4, 437);
INSERT INTO patient_medication(id, amount, "timestamp", cumsuption_date, medicine_id, patient_id, disease_id) VALUES (2, 1, '2010-11-02 15:53:00', '2010-11-02 15:53:00', 2, 4, 437);
INSERT INTO patient_medication(id, amount, "timestamp", cumsuption_date, medicine_id, patient_id, disease_id) VALUES (3, 2, '2010-11-02 15:53:00', '2010-11-02 15:53:00', 3, 4, 437);
INSERT INTO patient_medication(id, amount, "timestamp", cumsuption_date, medicine_id, patient_id, disease_id) VALUES (4, 1, '2010-11-02 15:53:00', '2010-11-02 15:53:00', 4, 4, 437);
INSERT INTO patient_medication(id, amount, "timestamp", cumsuption_date, medicine_id, patient_id, disease_id) VALUES (5, 1, '2010-11-02 18:53:00', '2010-11-02 18:53:00', 4, 4, 437);
INSERT INTO patient_medication(id, amount, "timestamp", cumsuption_date, medicine_id, patient_id, disease_id) VALUES (6, 1, '2010-11-03 18:53:00', '2010-11-03 18:53:00', 4, 4, 437);

INSERT INTO user_connection(id, state, owner_id, user_id, created) VALUES (10, 'P', 6, 5, '2010-11-02 18:53:00');
INSERT INTO user_connection(id, state, owner_id, user_id, created) VALUES (11, 'A', 6, 4, '2010-11-02 18:53:00');

-- QUESTION_TYPE
INSERT INTO question_type(id, description, "type", answer_data_type) VALUES (1, 'Yes/No questions', 2, 0);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (1, 2, 'Kaine Angabe', 0, 1, false);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (2, 0, 'Yes', 1, 1, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (3, 0, 'No', 2, 1, false);
INSERT INTO question_type_tag(questiontype_id, tag) VALUES (1, 'test1.jsp');

INSERT INTO question_type(id, description, "type", answer_data_type) VALUES (2, 'Zero/Ten questions', 2, 1);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (4, 2, 'Kaine Angabe', 0, 2, false);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (5, 0, '0', 1, 2, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (6, 0, '1', 2, 2, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (7, 0, '2', 3, 2, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (8, 0, '3', 4, 2, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (9, 0, '4', 5, 2, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (10, 0, '5', 6, 2, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (11, 0, '6', 7, 2, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (12, 0, '7', 8, 2, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (13, 0, '8', 9, 2, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (14, 0, '9', 10, 2, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (15, 0, '10', 11, 2, true);
INSERT INTO question_type_tag(questiontype_id, tag) VALUES (2, 'test1.jsp');

INSERT INTO question_type(id, description, "type", answer_data_type) VALUES (3, 'Time range single selection list', 2, 2);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (16, 2, 'Kaine Angabe', 0, 3, false);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (17, 0, 'weniger als 30 Minuten', 1, 3, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (18, 0, '30 Minuten bis 1 Stunde', 2, 3, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (19, 0, '1 bis 2 Stunden', 3, 3, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (20, 0, '2 bis 4 Stunden', 4, 3, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (21, 0, 'mehr als 4 Stunden', 5, 3, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (22, 0, 'den ganzen Tag', 6, 3, true);

INSERT INTO question_type(id, description, "type", answer_data_type) VALUES (4, 'Difficulties evaluation text single selection list', 2, 2);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (23, 2, 'Kaine Angabe', 0, 4, false);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (24, 0, 'Ohne jede Schwierigkeit', 1, 4, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (25, 0, 'Mit einigen Schwierigkeiten', 2, 4, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (26, 0, 'Mit grossen Schwierigkeiten', 3, 4, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (27, 0, 'Nicht dazu in der Lage', 4, 4, true);

INSERT INTO question_type(id, description, "type", answer_data_type) VALUES (5, 'Selbst sorgen', 2, 2);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (28, 2, 'Kaine Angabe', 0, 5, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (29, 0, 'Ich habe keine Probleme für mich selbst zu sorgen', 1, 5, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (30, 0, 'Ich habe einige Probleme, mich selbst zu waschen oder anzuziehen', 2, 5, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (31, 0, 'Ich bin nicht in der Lage, mich selbst zu waschen oder anzuziehen', 3, 5, true);

INSERT INTO question_type(id, description, "type", answer_data_type) VALUES (6, 'Note/Text', 0, 2);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (32, 2, 'Kaine Angabe', 0, 6, false);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (33, 0, 'Answer', 1, 6, true);

INSERT INTO question_type(id, description, "type", answer_data_type) VALUES (7, 'Alltäglichen Tätigkeiten', 2, 2);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (34, 2, 'Kaine Angabe', 0, 7, false);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (35, 0, 'Ich habe keine Probleme für mich selbst zu sorgen', 1, 7, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (36, 0, 'Ich habe einige Probleme, mich selbst zu waschen oder anzuziehen', 2, 7, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (37, 0, 'Ich bin nicht in der Lage, mich selbst zu waschen oder anzuziehen', 3, 7, true);

INSERT INTO question_type(id, description, "type", answer_data_type) VALUES (8, 'Augen', 2, 2);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (38, 2, 'Kaine Angabe', 0, 8, false);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (39, 0, 'Nein', 1, 8, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (40, 0, 'Leicht', 2, 8, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (41, 0, 'Mittel', 3, 8, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (42, 0, 'Schwer', 4, 8, true);

INSERT INTO question_type(id, description, "type", answer_data_type) VALUES (9, 'Täglich rauchen', 2, 2);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (43, 2, 'Kaine Angabe', 0, 9, false);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (44, 0, '1', 1, 9, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (45, 0, '2', 2, 9, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (46, 0, '3', 3, 9, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (47, 0, '4', 4, 9, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (48, 0, '5', 5, 9, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (49, 0, '6', 6, 9, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (50, 0, '7', 7, 9, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (51, 0, '8', 8, 9, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (52, 0, '9', 9, 9, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (53, 0, '10', 10, 9, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (54, 0, '11', 11, 9, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (55, 0, '12', 12, 9, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (56, 0, '13', 13, 9, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (57, 0, '14', 14, 9, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (58, 0, '15', 15, 9, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (59, 0, '16', 16, 9, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (60, 0, '17', 17, 9, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (61, 0, '18', 18, 9, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (62, 0, '19', 19, 9, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (63, 0, '20', 20, 9, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (64, 0, '2 PACKUNGEN', 21, 9, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (65, 0, '3 PACKUNGEN ODER MEHR', 22, 9, true);

INSERT INTO question_type(id, description, "type", answer_data_type) VALUES (10, 'Date', 0, 3);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (66, 2, 'Kaine Angabe', 0, 10, false);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (67, 0, 'Answer', 1, 10, true);

INSERT INTO question_type(id, description, "type", answer_data_type) VALUES (11, 'Enter Zero/Hundred scale - step 1', 3, 1);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (68, 2, 'Kaine Angabe', 0, 11, false);
INSERT INTO question_type_answer(id, kind, answer, max_value, max_value_title, min_value, min_value_title, selected_value_title, step, step_title, sort_order, question_type_id, active) VALUES (69, 1, 'Drag slider', 100, '100%', 1, '1%', '%', 1, '1', 1, 11, true);

INSERT INTO question_type(id, description, "type", answer_data_type) VALUES (12, 'Enter Image', 0, 4);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (70, 2, 'Kaine Angabe', 0, 12, false);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (71, 0, 'Enter Image', 1, 12, true);

INSERT INTO question_type(id, description, "type", answer_data_type) VALUES (13, '0 - 4 Textual level', 2, 2);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (72, 2, 'Kaine Angabe', 0, 13, false);
INSERT INTO question_type_answer(id, kind, answer, explanation, sort_order, question_type_id, active) VALUES (73, 0, '0', 'Gar nicht', 1, 13, true);
INSERT INTO question_type_answer(id, kind, answer, explanation, sort_order, question_type_id, active) VALUES (74, 0, '1', 'Mild', 2, 13, true);
INSERT INTO question_type_answer(id, kind, answer, explanation, sort_order, question_type_id, active) VALUES (75, 0, '2', 'Moderat', 3, 13, true);
INSERT INTO question_type_answer(id, kind, answer, explanation, sort_order, question_type_id, active) VALUES (76, 0, '3', 'Stark', 4, 13, true);
INSERT INTO question_type_answer(id, kind, answer, explanation, sort_order, question_type_id, active) VALUES (77, 0, '4', 'Sehr stark', 5, 13, true);

INSERT INTO question_type(id, description, "type", answer_data_type) VALUES (14, 'Ich fühle mich heute list', 2, 2);
INSERT INTO question_type_answer(id, answer, sort_order, question_type_id, active) VALUES (78, 'Kaine Angabe', 0, 14, true);
INSERT INTO question_type_answer(id, answer, sort_order, question_type_id, active) VALUES (79, 'Nie', 		1, 14, true);
INSERT INTO question_type_answer(id, answer, sort_order, question_type_id, active) VALUES (80, 'Selten', 2, 14, true);
INSERT INTO question_type_answer(id, answer, sort_order, question_type_id, active) VALUES (81, 'Manchmal', 3, 14, true);
INSERT INTO question_type_answer(id, answer, sort_order, question_type_id, active) VALUES (82, 'Oft', 4, 14, true);
INSERT INTO question_type_answer(id, answer, sort_order, question_type_id, active) VALUES (83, 'Die ganze Zeit', 5, 14, true);

INSERT INTO question_type(id, description, "type", answer_data_type) VALUES (15, 'Morbus Bechterew Morgensteifigkeit in Stunden', 2, 1);
INSERT INTO question_type_answer(id, kind, sort_order, question_type_id, active, answer, explanation) VALUES (84, 0, 0, 15, false, 'Kaine Angabe', 'Kaine Angabe');
INSERT INTO question_type_answer(id, kind, sort_order, question_type_id, active, answer, explanation) VALUES (85, 0, 1, 15, true,  '0', '0');
INSERT INTO question_type_answer(id, kind, sort_order, question_type_id, active, answer, explanation) VALUES (86, 0, 2, 15, true,  '1', '0.2h');
INSERT INTO question_type_answer(id, kind, sort_order, question_type_id, active, answer, explanation) VALUES (87, 0, 3, 15, true,  '2', '0.4h');
INSERT INTO question_type_answer(id, kind, sort_order, question_type_id, active, answer, explanation) VALUES (88, 0, 4, 15, true,  '3', '0.6h');
INSERT INTO question_type_answer(id, kind, sort_order, question_type_id, active, answer, explanation) VALUES (89, 0, 5, 15, true,  '4', '0.8h');
INSERT INTO question_type_answer(id, kind, sort_order, question_type_id, active, answer, explanation) VALUES (90, 0, 6, 15, true,  '5', '1h');
INSERT INTO question_type_answer(id, kind, sort_order, question_type_id, active, answer, explanation) VALUES (91, 0, 7, 15, true,  '6', '1.2h');
INSERT INTO question_type_answer(id, kind, sort_order, question_type_id, active, answer, explanation) VALUES (92, 0, 8, 15, true,  '7', '1.4h');
INSERT INTO question_type_answer(id, kind, sort_order, question_type_id, active, answer, explanation) VALUES (93, 0, 9, 15, true,  '8', '1.6h');
INSERT INTO question_type_answer(id, kind, sort_order, question_type_id, active, answer, explanation) VALUES (94, 0, 10, 15, true,  '9', '1.8h');
INSERT INTO question_type_answer(id, kind, sort_order, question_type_id, active, answer, explanation) VALUES (95, 0, 11, 15, true, '10', '>=2h');


-- user question type
INSERT INTO question_type(id, description, "type", answer_data_type, user_id) VALUES (100, 'Yes/No questions', 2, 0, 4);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (nextval('hibernate_sequence'), 2, 'Kaine Angabe', 0, 100, false);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (nextval('hibernate_sequence'), 0, 'Yes', 1, 100, true);
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (nextval('hibernate_sequence'), 0, 'No', 2, 100, true);

-- QUESTION default yes/no
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1001, 0, 0,  1, 1, 'Geschwollen rechte Hand Daumen vorne');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1002, 0, 1,  1, 1, 'Geschwollen rechte Hand Daumen hinten');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1003, 0, 2,  1, 1, 'Geschwollen rechte Hand Zeigefinger vorne');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1004, 0, 3,  1, 1, 'Geschwollen rechte Hand Zeigefinger hinten');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1005, 0, 4,  1, 1, 'Geschwollen rechte Hand Mittelfinger vorne');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1006, 0, 5,  1, 1, 'Geschwollen rechte Hand Mittelfinger hinten');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1007, 0, 5,  1, 1, 'Geschwollen rechte Hand Ringfinger vorne');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1008, 0, 7,  1, 1, 'Geschwollen rechte Hand Ringfinger hinten');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1009, 0, 8,  1, 1, 'Geschwollen rechte Hand Kleinfinger vorne');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1010, 0, 9,  1, 1, 'Geschwollen rechte Hand Kleinfinger hinten');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1011, 0, 10, 1, 1, 'Geschwollen rechtes Handgelenk');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1012, 0, 11, 1, 1, 'Geschwollen rechter Ellbogen');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1013, 0, 12, 1, 1, 'Geschwollen rechte Schulter');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1014, 0, 13, 1, 1, 'Geschwollen rechtes Knie');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1021, 0, 14, 1, 1, 'Geschwollen linke Hand Daumen vorne');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1022, 0, 15, 1, 1, 'Geschwollen linke Hand Daumen hinten');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1023, 0, 16, 1, 1, 'Geschwollen linke Hand Zeigefinger vorne');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1024, 0, 17, 1, 1, 'Geschwollen linke Hand Zeigefinger hinten');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1025, 0, 18, 1, 1, 'Geschwollen linke Hand Mittelfinger vorne');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1026, 0, 19, 1, 1, 'Geschwollen linke Hand Mittelfinger hinten');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1027, 0, 20, 1, 1, 'Geschwollen linke Hand Ringfinger vorne');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1028, 0, 21, 1, 1, 'Geschwollen linke Hand Ringfinger hinten');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1029, 0, 22, 1, 1, 'Geschwollen linke Hand Kleinfinger vorne');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1030, 0, 23, 1, 1, 'Geschwollen linke Hand Kleinfinger hinten');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1031, 0, 24, 1, 1, 'Geschwollen linkes Handgelenk');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1032, 0, 25, 1, 1, 'Geschwollen linker Ellbogen');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1033, 0, 26, 1, 1, 'Geschwollen linke Schulter');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1034, 0, 27, 1, 1, 'Geschwollen linkes Knie');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1051, 0, 0,  1, 2, 'Druckempfindlich rechte Hand Daumen vorne');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1052, 0, 1,  1, 2, 'Druckempfindlich rechte Hand Daumen hinten');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1053, 0, 2,  1, 2, 'Druckempfindlich rechte Hand Zeigefinger vorne');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1054, 0, 3,  1, 2, 'Druckempfindlich rechte Hand Zeigefinger hinten');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1055, 0, 4,  1, 2, 'Druckempfindlich rechte Hand Mittelfinger vorne');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1056, 0, 5,  1, 2, 'Druckempfindlich rechte Hand Mittelfinger hinten');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1057, 0, 6,  1, 2, 'Druckempfindlich rechte Hand Ringfinger vorne');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1058, 0, 7,  1, 2, 'Druckempfindlich rechte Hand Ringfinger hinten');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1059, 0, 8,  1, 2, 'Druckempfindlich rechte Hand Kleinfinger vorne');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1060, 0, 9,  1, 2, 'Druckempfindlich rechte Hand Kleinfinger hinten');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1061, 0, 10, 1, 2, 'Druckempfindlich rechtes Handgelenk');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1062, 0, 11, 1, 2, 'Druckempfindlich rechter Ellbogen');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1063, 0, 12, 1, 2, 'Druckempfindlich rechte Schulter');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1064, 0, 13, 1, 2, 'Druckempfindlich rechtes Knie');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1071, 0, 14, 1, 2, 'Druckempfindlich linke Hand Daumen vorne');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1072, 0, 15, 1, 2, 'Druckempfindlich linke Hand Daumen hinten');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1073, 0, 16, 1, 2, 'Druckempfindlich linke Hand Zeigefinger vorne');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1074, 0, 17, 1, 2, 'Druckempfindlich linke Hand Zeigefinger hinten');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1075, 0, 18, 1, 2, 'Druckempfindlich linke Hand Mittelfinger vorne');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1076, 0, 19, 1, 2, 'Druckempfindlich linke Hand Mittelfinger hinten');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1077, 0, 20, 1, 2, 'Druckempfindlich linke Hand Ringfinger vorne');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1078, 0, 21, 1, 2, 'Druckempfindlich linke Hand Ringfinger hinten');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1079, 0, 22, 1, 2, 'Druckempfindlich linke Hand Kleinfinger vorne');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1080, 0, 23, 1, 2, 'Druckempfindlich linke Hand Kleinfinger hinten');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1081, 0, 24, 1, 2, 'Druckempfindlich linkes Handgelenk');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1082, 0, 25, 1, 2, 'Druckempfindlich linker Ellbogen');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1083, 0, 26, 1, 2, 'Druckempfindlich linke Schulter');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1084, 0, 27, 1, 2, 'Druckempfindlich linkes Knie');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1100, 0, 0,  1, 3, 'Waren die Gelenke (Hände) steif, als Sie heute morgen aufgewacht sind?');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1102, 0, 1,  3, 3, 'Wenn ja, wie lange dauerte diese Steifigkeit heute morgen?');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1115, 0, 0,  4, 2, 'Gesundheitsskala');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1127, 0, 0,  5, 1, '...voller Schwung');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1128, 0, 1,  5, 1, '...sehr nervös');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1129, 0, 2,  5, 1, '...so niedergeschlagen, dass mich nichts aufheitern konnte');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1130, 0, 3,  5, 1, '...ruhig und gelassen');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1131, 0, 4,  5, 1, '...voller Energie');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1132, 0, 5,  5, 1, '...entmutigt und traurig');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1133, 0, 6,  5, 1, '...erschöpft');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1134, 0, 7,  5, 1, '...glücklich');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1135, 0, 8,  5, 1, '...müde');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1136, 0, 0, 6, 4, 'Können Sie sich ankleiden, inkl. Binden von Schnürsenkeln und Schliessen von Knöpfen?');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1137, 0, 1, 6, 4, 'Können Sie sich die Haare waschen?');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1138, 0, 0, 7, 4, 'Können Sie Hausarbeiten verrichten, z.B. Staubsaugen?');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1139, 0, 1, 7, 4, 'Können Sie alltägliche Tätigkeiten im Beruf ausführen?');
INSERT INTO question (id, kind, sort_order, haq_id, question_type_id, "text") VALUES (1140, 0, 2, 7, 1, 'Mussten Sie Ihrer Arbeitsstelle aufgrund Ihrer Erkrankung in den letzten 7 Tagen fern bleiben?');

INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2000,  0,  8, 2, 'Wie würden Sie Ihre allgemeine Müdigkeit und Erschöpfung beschreiben?');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2001,  0,  9, 2, 'Wie stark waren ihre Schmerzen in Nacken, Rücken oder Hüfte?');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2002,  0, 10, 2, 'Wie stark waren Ihre Schmerzen oder Schwellungen an anderen Gelenken?');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2003,  0, 11, 2, 'Wie unangenehm waren für Sie berührungs- oder druckempfindliche Körperstellen?');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2004,  0, 12, 2, 'Wie ausgeprägt war Ihre Morgensteifigkeit nach dem aufwachen?');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2005,  1, 12, 15, 'Wenn ja, wie lange dauerte diese Morgensteifigkeit im Allgemeinen?');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2006,  0, 13, 2, 'Konnten Sie körperlich anstrengende Tätigkeiten verrichten (z. B. krankengymnastische Uebungen, Gartenarbeit oder Sport)?');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2007,  0, 14, 2, 'Konnten Sie zuhause oder bei der Arbeit den ganzen Tag aktiv sein?');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2008,  0, 15, 5, 'Konnten Sie für sich selbst sorgen?');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2009,  0, 16, 7, 'Konnten Sie Ihren Tätigkeiten nachgehen (Arbeit, Studium, Hausarbeit, Familien- oder Freizeitaktivitäten)');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2010,  1, 16, 1, 'Mussten Sie Ihrer Arbeitsstelle aufgrund Ihrer Erkrankung in den letzten 7 Tagen fern bleiben?');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2011, 0, 17, 1, 'Aufgrund meiner Erkrankung bin ich oft niedergeschlagen.');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2012, 1, 17, 1, 'Ich habe Angst, die Erwartungen anderer an mich nicht erfüllen zu können.');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2013, 0, 18, 8, 'Hatten Sie in der letzten Woche entzündete Augen?');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2014, 1, 18, 1, 'Hatten Sie deswegen einen Arzt konsultiert?');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2015, 0, 19, 8, 'Hatten Sie in der letzten Woche Probleme im Magen Darm Bereich?');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2016, 1, 19, 1, 'Hatten Sie deswegen einen Arzt konsultiert?');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2017, 0, 20, 1, 'Hatten Sie in der letzten Woche Beschwerden mit der Haut?');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2018, 1, 20, 1, 'Vorne Kopf');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2019, 2, 20, 1, 'Vorne Arm links');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2020, 3, 20, 1, 'Vorne Arm rechts');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2021, 4, 20, 1, 'Vorne Brust links');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2022, 5, 20, 1, 'Vorne Brust rechts');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2023, 6, 20, 1, 'Bauch links');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2024, 7, 20, 1, 'Bauch rechts');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2025, 8, 20, 1, 'Oberschenkel links');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2026, 9, 20, 1, 'Oberschenkel rechts');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2027, 10, 20, 1, 'Schienbein und Fuss links');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2028, 11, 20, 1, 'Schienbein Fuss rechts');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2029, 12, 20, 1, 'Hinten Kopf');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2030, 13, 20, 1, 'Hinteram links');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2031, 14, 20, 1, 'Hinterarm rechts');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2032, 15, 20, 1, 'Schulterblatt links');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2033, 16, 20, 1, 'Schulterblatt rechts');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2034, 17, 20, 1, 'Unterer Rücken links');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2035, 18, 20, 1, 'Unterer Rücken rechts');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2036, 19, 20, 1, 'Schenkel hinten links');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2037, 20, 20, 1, 'Schenkel hinten rechts');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2038, 21, 20, 1, 'Waden und Ferse links');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2039, 22, 20, 1, 'Waden und Ferse rechts');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2040, 23, 20, 1, 'Hatten Sie deswegen einen Arzt konsultiert?');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2041, 0, 21, 1, 'Schlüsselbein links');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2042, 1, 21, 1, 'Schlüsselbein rechts');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2043, 2, 21, 1, 'Plex links');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2044, 3, 21, 1, 'Plex rechts');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2045, 4, 21, 1, 'Hüfte aussen');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2046, 5, 21, 1, 'Hüfte innen');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2047, 6, 21, 1, 'Becken links');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2048, 7, 21, 1, 'Becken rechts');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2049, 8, 21, 1, 'Sitzbein');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2050, 9, 21, 1, 'Ferse links');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2051, 10, 21, 1, 'Ferse rechts');
-- MORBUS BECHTEREW ONE-TIME QUESTIONS
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, group_text, "text") VALUES (0, 2055, 0, 22, 9, 'Risikoverhalten', 'Bitte geben Sie die Anzahl Zigaretten oder Pfeife an, die Sie täglich rauchen?');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, group_text, "text") VALUES (0, 2056, 1, 22, 1, 'Risikoverhalten', 'Konsumieren Sie regelmässig Alkohol?');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, group_text, explanation, "text") VALUES (0, 2057, 2, 22, 1, 'Weitere Erkrankungen', 'Welche weiteren Co-Morbiditäten (Erkrankungen) haben Sie?', 'Entzündungen des Darm (IBD, Morbus Crohn)');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, group_text, explanation, "text") VALUES (0, 2058, 3, 22, 1, 'Weitere Erkrankungen', 'Welche weiteren Co-Morbiditäten (Erkrankungen) haben Sie?', 'Entzündungen der Augen (Uveitis)');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, group_text, explanation, "text") VALUES (0, 2059, 4, 22, 6, 'Weitere Erkrankungen', 'Welche weiteren Co-Morbiditäten (Erkrankungen) haben Sie?', 'Weitere Erkrankungen');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, group_text, explanation, "text") VALUES (0, 2060, 5, 22, 1, 'Regelmässige Medikation', null, 'Schmerzmittel');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, group_text, explanation, "text") VALUES (0, 2061, 6, 22, 6, 'Regelmässige Medikation', null, 'Andere');
INSERT INTO question(kind, extended_type, id, sort_order, haq_id, question_type_id, group_text, explanation, "text") VALUES (0, 0, 2062, 7, 22, 10, null, null, 'Datum erste Symptome');
INSERT INTO question(kind, extended_type, id, sort_order, haq_id, question_type_id, group_text, explanation, "text") VALUES (0, 1, 2063, 8, 22, 10, null, null, 'Datum Diagnose');
--------- RHEUMA ONE-TIME QUESTIONS
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, group_text, explanation, "text") VALUES (0, 2070, 0, 23, 1, 'Medikamente, die Sie bisher verwenden und verwendet haben', null, 'Antirheumatika');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, group_text, explanation, "text") VALUES (0, 2071, 1, 23, 1, 'Medikamente, die Sie bisher verwenden und verwendet haben', null, 'Kortison Medikamente');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, group_text, explanation, "text") VALUES (0, 2072, 2, 23, 1, 'Medikamente, die Sie bisher verwenden und verwendet haben', null, 'Herkömmliche Basismedikamente');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, group_text, explanation, "text") VALUES (0, 2073, 3, 23, 1, 'Medikamente, die Sie bisher verwenden und verwendet haben', null, 'Biotechnologisch hergestellte Basismedikamente');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, group_text, explanation, "text") VALUES (0, 2074, 4, 23, 6, 'Regelmässige Medikation', null, 'Andere');
INSERT INTO question(kind, extended_type, id, sort_order, haq_id, question_type_id, group_text, explanation, "text") VALUES (0, 0, 2075, 5, 23, 10, null, null, 'Datum erste Symptome');
INSERT INTO question(kind, extended_type, id, sort_order, haq_id, question_type_id, group_text, explanation, "text") VALUES (0, 1, 2076, 6, 23, 10, null, null, 'Datum Diagnose');
--------- PSORIASIS ONE-TIME QUESTIONS
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, group_text, "text") VALUES (0, 2090, 0, 24, 9, 'Risikoverhalten', 'Bitte geben Sie die Anzahl Zigaretten oder Pfeife an, die Sie täglich rauchen?');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, group_text, "text") VALUES (0, 2091, 1, 24, 1, 'Risikoverhalten', 'Konsumieren Sie regelmässig Alkohol?');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, group_text, explanation, "text") VALUES (0, 2092, 2, 24, 1, 'Regelmässige Medikation', null, 'Schmerzmittel');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, group_text, explanation, "text") VALUES (0, 2093, 3, 24, 6, 'Regelmässige Medikation', null, 'Andere');
INSERT INTO question(kind, extended_type, id, sort_order, haq_id, question_type_id, group_text, explanation, "text") VALUES (0, 0, 2094, 4, 24, 10, null, null, 'Datum erste Symptome');
INSERT INTO question(kind, extended_type, id, sort_order, haq_id, question_type_id, group_text, explanation, "text") VALUES (0, 1, 2095, 5, 24, 10, null, null, 'Datum Diagnose');

----------------- CHARTS
-- HAQ1 
INSERT INTO haq_chart(id, sort_order, subtitle, tag, title, x_axis_name, y_axis_name, chart_type_id, haq_id) VALUES (nextval('hibernate_sequence'), 0, 'subtitle', 'tag1', 'Zeitlicher Verlauf geschwollene Gelenke', 'Zeit', 'Anzahl geschwollene gelenke', 1, 1);
-- HAQ2
INSERT INTO haq_chart(id, sort_order, subtitle, tag, title, x_axis_name, y_axis_name, chart_type_id, haq_id) VALUES (nextval('hibernate_sequence'), 0, 'subtitle', 'tag1', 'Zeitlicher Verlauf druckempfindliche Gelenke', 'Zeit', 'Anzahl druckempfindliche gelenke', 1, 2);
-- HAQ3	
INSERT INTO haq_chart(id, sort_order, subtitle, tag, title, x_axis_name, y_axis_name, chart_type_id, haq_id) VALUES (nextval('hibernate_sequence'), 0, 'subtitle', 'tag1', 'Waren Ihre Gelenke<br/>(Hände) steif, als<br/>Sie heute morgen<br/>aufwachten?', '', '', 2, 3);
INSERT INTO haq_chart(id, sort_order, subtitle, tag, title, x_axis_name, y_axis_name, chart_type_id, haq_id) VALUES (nextval('hibernate_sequence'), 1, 'subtitle', 'tag1', 'Wenn ja, wie lange dauerte<br/>diese Steifigkeit am morgen?', '', 'Häufigkeit', 3, 3);
-- HAQ4
INSERT INTO haq_chart(id, sort_order, subtitle, tag, title, x_axis_name, y_axis_name, chart_type_id, haq_id) VALUES (nextval('hibernate_sequence'), 0, 'Fragestellung: Wie würden Sie Ihren derzeitigen Gesundheitszustand beschreiben?', 'tag1', 'Gesundheitszustand', 'Bewertungsskala (0 = Sehr gut; 10 = Sehr schlecht)', 'Häufigkeit', 3, 4);
-- HAQ5
INSERT INTO haq_chart(id, sort_order, subtitle, tag, title, x_axis_name, y_axis_name, chart_type_id, haq_id) VALUES (nextval('hibernate_sequence'), 0, 'Fragestellung: Bitte beantworten Sie jede Zeile mit "Ja" oder "Nein". Ich fühle mich heute...', 'tag1', '', '', '', 6, 5);
-- HAQ6
INSERT INTO haq_chart(id, sort_order, subtitle, tag, title, x_axis_name, y_axis_name, chart_type_id, haq_id) VALUES (nextval('hibernate_sequence'), 0, 'Bitte geben Sie die Antwort an, die am Besten Ihre Fähigkeit beschreibt.', 'tag1', '', '', 'Häufigkeit', 3, 6);
-- HAQ7
INSERT INTO haq_chart(id, sort_order, subtitle, tag, title, x_axis_name, y_axis_name, chart_type_id, haq_id) VALUES (nextval('hibernate_sequence'), 0, 'Bitte geben Sie die Antwort an, die am Besten Ihre Fähigkeit beschreibt.', 'tag1', '', '', 'Häufigkeit', 3, 7);
INSERT INTO haq_chart(id, sort_order, subtitle, tag, title, x_axis_name, y_axis_name, chart_type_id, haq_id) VALUES (nextval('hibernate_sequence'), 1, '', 'tag1', 'Mussten Sie Ihrer Arbeitsstelle aufgrund Ihrer Erkrankung<br/>in den letzten 7 Tagen fern bleiben?', '', 'Häufigkeit', 2, 7);

---------------
INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id) VALUES (1, 4, '2010-11-01', 1001, 2);
INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id) VALUES (2, 4, '2010-11-01', 1002, 2);
INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id) VALUES (3, 4, '2010-11-01', 1051, 2);
INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id) VALUES (4, 4, '2010-11-01', 1052, 3);
INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id) VALUES (5, 4, '2010-11-01', 1115, 11);

INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id) VALUES (6, 4, '2010-11-02', 1001, 2);
INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id) VALUES (7, 4, '2010-11-02', 1002, 3);
INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id) VALUES (8, 4, '2010-11-02', 1051, 2);
INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id) VALUES (9, 4, '2010-11-02', 1052, 3);
INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id) VALUES (10, 4, '2010-11-02', 1115, 12);

INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id) VALUES (11, 4, '2010-11-03', 1001, 2);
INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id) VALUES (12, 4, '2010-11-03', 1002, 3);
INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id) VALUES (13, 4, '2010-11-03', 1051, 2);
INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id) VALUES (14, 4, '2010-11-03', 1052, 2);
INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id) VALUES (15, 4, '2010-11-03', 1115, 15);

--------- PSORIASIS HAQ
INSERT INTO haq(id, sortorder, "HAQ-Question", explanation, disease_id) VALUES (30, 0, 'Bild hochladen von Ihrer befallenen Stelle', 'Bild hochladen von Ihrer befallenen Stelle', 443);
INSERT INTO haq(id, sortorder, "HAQ-Question", explanation, disease_id) VALUES (31, 1, 'PASI - Werte', 'PASI - Werte', 443);
INSERT INTO haq(id, sortorder, "HAQ-Question", explanation, disease_id) VALUES (32, 2, 'Gesundheitsfrage', 'Gesundheitsfrage', 443);
INSERT INTO haq(id, sortorder, "HAQ-Question", explanation, disease_id) VALUES (33, 3, 'Psychologisches Befinden', 'Psychologisches Befinden', 443);

INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2100,  0,  30, 12, 'Format .jpg, .png, .bmp | mMax. Grösse 1MB Nicht mehr als 3 Bilder hochladen (Keine Gesichtserkennung um Daten anonym zu behalten)');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2101,  0,  31, 11, 'Kopf Umfang in %');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2102,  1,  31, 13, 'Kopf Erythema');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2103,  2,  31, 13, 'Kopf Induration');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2104,  3,  31, 13, 'Kopf Desquamation');

INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2105,  4,  31, 11, 'Körper Umfang in %');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2106,  5,  31, 13, 'Körper Erythema');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2107,  6,  31, 13, 'Körper Induration');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2108,  7,  31, 13, 'Körper Desquamation');

INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2109,  8,  31, 11, 'Arme Umfang in %');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2110,  9,  31, 13, 'Arme Erythema');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2111, 10,  31, 13, 'Arme Induration');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2112, 11,  31, 13, 'Arme Desquamation');

INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2113, 12,  31, 11, 'Beine Umfang in %');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2114, 13,  31, 13, 'Beine Erythema');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2115, 14,  31, 13, 'Beine Induration');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2116, 15,  31, 13, 'Beine Desquamation');

INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2117,  0,  32, 11, 'Wie fühlen Sie sich heute? Bitte beantworten Sie jede Zeile.');

INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2118,  0,  33, 14, 'Bitte beantworten Sie jede Zeile mit "Ja" oder "Nein. Ich fühle mich heute...');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2119,  1,  33, 14, 'Wie oft juckt Ihre Haut?');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2120,  2,  33, 14, 'Wie oft fühlt sich Ihre Haut schmerzhaft oder irritiert an?');

-------- ANSWER MORBUS BECHT sysuser1
INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id, custom_answer) VALUES (nextval('hibernate_sequence'), 4, '2010-11-01', 2004, 11, '7');--7
INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id) VALUES (nextval('hibernate_sequence'), 4, '2010-11-01', 2005, 87);--'3,75', '3/4'
INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id, custom_answer) VALUES (nextval('hibernate_sequence'), 4, '2010-11-01', 2000, 9, '5'); --5
INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id, custom_answer) VALUES (nextval('hibernate_sequence'), 4, '2010-11-01', 2001, 10, '6');--6
INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id, custom_answer) VALUES (nextval('hibernate_sequence'), 4, '2010-11-01', 2002, 11, '7');--7
INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id, custom_answer) VALUES (nextval('hibernate_sequence'), 4, '2010-11-01', 2003, 12, '8');--8

INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id, custom_answer) VALUES (nextval('hibernate_sequence'), 4, '2010-11-02', 2004, 11, '7');--7
INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id) VALUES (nextval('hibernate_sequence'), 4, '2010-11-02', 2005, 89);--'6,25', '1 1/4'
INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id, custom_answer) VALUES (nextval('hibernate_sequence'), 4, '2010-11-02', 2000, 8, '4'); --4
INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id, custom_answer) VALUES (nextval('hibernate_sequence'), 4, '2010-11-02', 2001, 9, '5');--5
INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id, custom_answer) VALUES (nextval('hibernate_sequence'), 4, '2010-11-02', 2002, 10, '6');--6
INSERT INTO patient_question_answer(id, patient_id, logdate, question_id, answer_id, custom_answer) VALUES (nextval('hibernate_sequence'), 4, '2010-11-02', 2003, 11, '7');--7

INSERT INTO user_disease(user_id, disease_id) VALUES ((select u.id from "user" u where u.name='5ZCNMNm5jc4QYzJZ59EJ1JcSPcP0Gv44YfGCMXoXGYY='), 437);

INSERT INTO message(id, sender_user_id, receiver_user_id, subject, body) VALUES (1, 4, 5, 'Hallo pat2', 'How are you?');
INSERT INTO message(id, sender_user_id, receiver_user_id, subject, body) VALUES (2, 4, 6, 'Hallo doc1', 'How are you?');
INSERT INTO message(id, sender_user_id, receiver_user_id, in_reply_to_message_id, subject, body) VALUES (3, 6, 4, 2, 'Hallo pat1', 'I am fine');

ALTER SEQUENCE hibernate_sequence RESTART WITH 5000;

