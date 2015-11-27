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

alter table "user" add column logins_count integer not null default 0;
alter table "user" add column last_update timestamp without time zone default now() not null;
alter table questionary_validation_type rename to key_performance_indicator_type;
alter table key_performance_indicator_type add column disease_id bigint;
update key_performance_indicator_type set disease_id = 437;
alter table key_performance_indicator_type alter column disease_id set not null;
alter table key_performance_indicator_type add constraint fk_kpi_type_disease_id foreign key (disease_id) references disease (id) on update no action on delete no action;
create index fki_kpi_type_disease_id on key_performance_indicator_type(disease_id);
alter index public.fki_doc_pat_quest_validation_type_id rename to fki_doc_pat_quest_kpi_type_id;
ALTER TABLE key_performance_indicator_type RENAME "name" TO code;
ALTER TABLE key_performance_indicator_type ALTER COLUMN code SET NOT NULL;
ALTER TABLE key_performance_indicator_type ADD COLUMN "type" integer;
update key_performance_indicator_type set "type" = 0;
ALTER TABLE key_performance_indicator_type ALTER COLUMN "type" SET NOT NULL;
ALTER TABLE "user" ADD COLUMN unsuccessful_logins_count integer NOT NULL DEFAULT 0;

CREATE TABLE patient_key_performance_indicator
(
  id bigint NOT NULL,
  log_date timestamp without time zone NOT NULL,
  timestampcreated timestamp without time zone NOT NULL,
  kpi_value numeric(19,2) NOT NULL,
  validation_type_id bigint NOT NULL,
  patient_id bigint NOT NULL,
  CONSTRAINT patient_key_performance_indicator_pkey PRIMARY KEY (id),
  CONSTRAINT fk_patient_kpi_kpit_id FOREIGN KEY (validation_type_id)
      REFERENCES key_performance_indicator_type (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_patient_kpi_patient_id FOREIGN KEY (patient_id)
      REFERENCES "user" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE patient_key_performance_indicator OWNER TO projecth;

CREATE TABLE chart_type
(
  id bigint NOT NULL,
  description character varying(255),
  "type" integer NOT NULL,
  CONSTRAINT chart_type_pkey PRIMARY KEY (id)
);
ALTER TABLE chart_type OWNER TO projecth;

CREATE TABLE question_type
(
  id bigint NOT NULL,
  description character varying(255),
  "type" integer NOT NULL,
  answer_data_type integer NOT NULL,
  user_id bigint,
  CONSTRAINT question_type_pkey PRIMARY KEY (id),
  CONSTRAINT fk_question_type_user_id FOREIGN KEY (user_id)
      REFERENCES "user" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE question_type OWNER TO projecth;

CREATE TABLE question_type_answer
(
  kind integer default 0 NOT NULL,
  id bigint NOT NULL,
  active boolean NOT NULL,
  answer character varying(255) NOT NULL,
  explanation character varying(255),
  image_data character varying(4000),
  image_name character varying(4000),
  sort_order integer NOT NULL,
  max_value numeric(19,2),
  max_value_title character varying(255),
  min_value numeric(19,2),
  min_value_title character varying(255),
  selected_value_title character varying(255),
  step numeric(19,2),
  step_title character varying(255),
  question_type_id bigint NOT NULL,
  CONSTRAINT question_type_answer_pkey PRIMARY KEY (id),
  CONSTRAINT fk_answer_question_type_id FOREIGN KEY (question_type_id)
      REFERENCES question_type (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE question_type_answer OWNER TO projecth;

CREATE TABLE question_type_tag
(
  questiontype_id bigint NOT NULL,
  tag character varying(255) NOT NULL,
  CONSTRAINT fk_qtt_question_type_id FOREIGN KEY (questiontype_id)
      REFERENCES question_type (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE question_type_tag OWNER TO projecth;

CREATE TABLE haq_chart
(
  id bigint NOT NULL,
  sort_order integer NOT NULL,
  subtitle character varying(255),
  tag character varying(255) NOT NULL,
  title character varying(255) NOT NULL,
  x_axis_name character varying(255),
  y_axis_name character varying(255),
  chart_type_id bigint NOT NULL,
  haq_id bigint NOT NULL,
  CONSTRAINT haq_chart_pkey PRIMARY KEY (id),
  CONSTRAINT fk_haqchart_chart_type_id FOREIGN KEY (chart_type_id)
      REFERENCES chart_type (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_haqchart_haq_id FOREIGN KEY (haq_id)
      REFERENCES haq (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE haq_chart OWNER TO projecth;

CREATE TABLE question
(
  kind integer NOT NULL,
  id bigint NOT NULL,
  image_data character varying(4000),
  image_name character varying(4000),
  sort_order integer NOT NULL,
  "text" character varying(2500) NOT NULL,
  group_text character varying(255),
  explanation character varying(2500),
  custom_question_type integer,
  haq_id bigint,
  question_type_id bigint NOT NULL,
  creator_id bigint,
  disease_id bigint,
  target_question_id bigint,
  user_id bigint,
  CONSTRAINT question_pkey PRIMARY KEY (id),
  CONSTRAINT fk_question_creator_id FOREIGN KEY (creator_id)
      REFERENCES "user" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_question_disease_id FOREIGN KEY (disease_id)
      REFERENCES disease (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_question_haq_id FOREIGN KEY (haq_id)
      REFERENCES haq (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_question_question_type_id FOREIGN KEY (question_type_id)
      REFERENCES question_type (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_question_target_question_id FOREIGN KEY (target_question_id)
      REFERENCES question (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_question_user_id FOREIGN KEY (user_id)
      REFERENCES "user" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE question OWNER TO projecth;

CREATE TABLE haq_chart_question
(
  haq_chart_id bigint NOT NULL,
  question_id bigint NOT NULL,
  CONSTRAINT fk_haqchq_haq_chart_id FOREIGN KEY (haq_chart_id)
      REFERENCES haq_chart (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_haqchq_haq_question_id FOREIGN KEY (question_id)
      REFERENCES question (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE haq_chart_question OWNER TO projecth;

CREATE TABLE patient_question_answer
(
  id bigint NOT NULL,
  kind integer not null default 0,
  custom_answer character varying(2500),
  date_taken timestamp without time zone,
  logdate timestamp without time zone NOT NULL,
  answer_id bigint NOT NULL,
  patient_id bigint NOT NULL,
  question_id bigint NOT NULL,
  CONSTRAINT patient_question_answer_pkey PRIMARY KEY (id),
  CONSTRAINT fk_pat_q_answer_answer_id FOREIGN KEY (answer_id)
      REFERENCES question_type_answer (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_pat_q_answer_patient_id FOREIGN KEY (patient_id)
      REFERENCES "user" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_pat_q_answer_question_id FOREIGN KEY (question_id)
      REFERENCES question (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE patient_question_answer OWNER TO projecth;

alter table haq add column kind integer DEFAULT 0 not null;

---------------
INSERT INTO key_performance_indicator_type(id, "type", description, code, disease_id) VALUES (273, 1, 'DAS-28', 'DAS-28', 437);
INSERT INTO key_performance_indicator_type(id, "type", description, code, disease_id) VALUES (274, 2, 'Bath Ankylosing Spondylitis Disease Activity Index', 'BASDAI', 441);

---------------
INSERT INTO chart_type(id, description, "type") VALUES (1, 'A line chart shows trends in data at equal intervals', 0);
INSERT INTO chart_type(id, description, "type") VALUES (2, 'A pie chart shows the size of items that make up a data series, proportional to the sum of the items', 1);
INSERT INTO chart_type(id, description, "type") VALUES (3, 'A bar chart illustrates comparisons among individual items', 2);
INSERT INTO chart_type(id, description, "type") VALUES (4, 'A bubble chart is a type of xy (scatter) chart', 3);
INSERT INTO chart_type(id, description, "type") VALUES (5, 'An area chart emphasizes the magnitude of change over time', 4);
INSERT INTO chart_type(id, description, "type") VALUES (6, 'An xy (scatter) chart shows the relationships among the numeric values in several data series, or plots two groups of numbers as one series of xy coordinates', 5);
---------------
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
INSERT INTO question_type_answer(id, kind, answer, sort_order, question_type_id, active) VALUES (98, 0, '0', 1, 9, true);

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
INSERT INTO question_type_answer(id, kind, sort_order, question_type_id, active, answer, explanation) VALUES (84, 0, 0, 15, false,    '0', 'Kaine Angabe');
INSERT INTO question_type_answer(id, kind, sort_order, question_type_id, active, answer, explanation) VALUES (85, 0, 1, 15,  true, '1.25', '1/4');
INSERT INTO question_type_answer(id, kind, sort_order, question_type_id, active, answer, explanation) VALUES (86, 0, 2, 15,  true,  '2.5', '1/5');
INSERT INTO question_type_answer(id, kind, sort_order, question_type_id, active, answer, explanation) VALUES (87, 0, 3, 15,  true, '3.75', '3/4');
INSERT INTO question_type_answer(id, kind, sort_order, question_type_id, active, answer, explanation) VALUES (88, 0, 4, 15,  true,    '5', '1');
INSERT INTO question_type_answer(id, kind, sort_order, question_type_id, active, answer, explanation) VALUES (89, 0, 5, 15,  true, '6.25', '1 1/4');
INSERT INTO question_type_answer(id, kind, sort_order, question_type_id, active, answer, explanation) VALUES (90, 0, 6, 15,  true,  '7.5', '1 1/2');
INSERT INTO question_type_answer(id, kind, sort_order, question_type_id, active, answer, explanation) VALUES (91, 0, 7, 15,  true, '8.75', '1 3/4');
INSERT INTO question_type_answer(id, kind, sort_order, question_type_id, active, answer, explanation) VALUES (92, 0, 8, 15,  true,   '10', '>=2');

-------------
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
INSERT INTO haq_chart(id, sort_order, subtitle, tag, title, x_axis_name, y_axis_name, chart_type_id, haq_id) VALUES (38090, 1, 'Bitte geben Sie die Antwort an, die am Besten Ihre Fähigkeit beschreibt.', 'tag1', 'Konnten Sie selbständig duschen?', '', 'Häufigkeit', 3, 6);
-- HAQ7
INSERT INTO haq_chart(id, sort_order, subtitle, tag, title, x_axis_name, y_axis_name, chart_type_id, haq_id) VALUES (nextval('hibernate_sequence'), 0, 'Bitte geben Sie die Antwort an, die am Besten Ihre Fähigkeit beschreibt.', 'tag1', '', '', 'Häufigkeit', 6, 7);
INSERT INTO haq_chart(id, sort_order, subtitle, tag, title, x_axis_name, y_axis_name, chart_type_id, haq_id) VALUES (nextval('hibernate_sequence'), 1, '', 'tag1', 'Mussten Sie Ihrer Arbeitsstelle aufgrund Ihrer Erkrankung<br/>in den letzten 7 Tagen fern bleiben?', '', 'Häufigkeit', 2, 7);

-------------
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

INSERT INTO patient_question_answer(id, logdate, answer_id, patient_id, question_id)
select  nextval('hibernate_sequence'), pa.logdate, 
(
select qta.id from question_type_answer qta 
join question_type qt on qt.id = qta.question_type_id
join question q on q.question_type_id = qt.id
where qta.sort_order = pa.answer_id + 1
and q.id = pa.fk_questionaryid 
), pa.fk_patient, pa.fk_questionaryid, pa.answer_id
from patient_answer pa
where exists (
select qta.id from question_type_answer qta 
join question_type qt on qt.id = qta.question_type_id
join question q on q.question_type_id = qt.id
where qta.sort_order = pa.answer_id + 1
and q.id = pa.fk_questionaryid 
)

UPDATE user_account SET "login"= 'fqLIO68MZosT0CH7PID74g==', email='lA4yUgsDwpf7d6BZ2GZy7A==' WHERE "login"='pat0';
UPDATE user_account SET "login"= 'B3uKG3BDtVESncW7vkEXbw==', email='sVtNYm+rPyfdC/eBVnpaxh52jh1c/hEznDa7zuLqMdk=' WHERE "login"='pat2';
UPDATE user_account SET "login"= 'miMznYy0vcs6YxgJBepsaQ==', email='2e6PMUZQ0/txkFiGTR0nIB52jh1c/hEznDa7zuLqMdk=' WHERE "login"='pat3';
UPDATE user_account SET "login"= 'AVNXUS49UjZhuAY18q4lgg==', email='QO7M0JsDp5RgyXGcfFn9+x52jh1c/hEznDa7zuLqMdk=' WHERE "login"='pat4';
UPDATE user_account SET "login"= 'Y6jFoK15zPyCW49QXGPNwQ==', email='OneBKK5rxqKJf5YVU36tCR52jh1c/hEznDa7zuLqMdk=' WHERE "login"='pat5';
UPDATE user_account SET "login"= 'Cq1UhEiDF36eW6OoBB87uA==', email='odZ0qxVAJOkDfhNhSrWFs8mLbKT8MufNWuXS5uX9woE=' WHERE "login"='doc1';
UPDATE user_account SET "login"= 'r872HJYKFoRRQMCg30k8VQ==', email='tPBJoVGCEXbqExp1IhpFhhcNFe4ufu0AaEG3m2eIaro=' WHERE "login"='cust1';
UPDATE user_account SET "login"= '3dpr7qjnLUZLo/H+fWG3mA==', email='Jqp7vtiBwpgflz/LsQn1SxcNFe4ufu0AaEG3m2eIaro=' WHERE "login"='cust2';
UPDATE user_account SET "login"= 'qC6xH/e6J68kV5+EeyLHlg==', email='OxrLBaQUenQhqJVjnlFSYh52jh1c/hEznDa7zuLqMdk=' WHERE "login"='pat6';
UPDATE user_account SET "login"= '1U1jnoHCSqRTHOSylcBgmA==', email='GJ47GDfc0lQXDBwynMzZER52jh1c/hEznDa7zuLqMdk=' WHERE "login"='pat7';
UPDATE user_account SET "login"= 'xXTQ0Cp6SW91lTUEnkR+/A==', email='Fp0IFRY4sEJTRdphKnSOlB52jh1c/hEznDa7zuLqMdk=' WHERE "login"='pat8';
UPDATE user_account SET "login"= 'qhq16L1B8aUvxBH+DM1YUA==', email='+EyOJHEkj00kA2ya+M30Yx52jh1c/hEznDa7zuLqMdk=' WHERE "login"='pat9';
UPDATE user_account SET "login"= '2TKNkrsz0aV+V9yrDeqP6w==', email='R0MtouvXRuUycTuI0ayBXxcNFe4ufu0AaEG3m2eIaro=' WHERE "login"='pat10';
UPDATE user_account SET "login"= 'eKZEK03cW4ZgxdtCy77QQA==', email='0jGU2YXafuPM5hfEUs6/ihcNFe4ufu0AaEG3m2eIaro=' WHERE "login"='pat11';
UPDATE user_account SET "login"= 'm2AxdVNoA09XNPXWgK2hGQ==', email='EcC+VGVTd4dr8yF+3PDS/x52jh1c/hEznDa7zuLqMdk=' WHERE "login"='pat1';
UPDATE user_account SET "login"= 'NXlVtn8DUxlgSSHHybw5MA==', email='1OpGjLe6bK2/xXcTi7Ll7Q==' WHERE "login"='testuser1';
UPDATE user_account SET "login"= 'ZXtMEj6MPmYElLeyGx8lmA==', email='jClrYq7ukYp6+LX/Jruvlg==' WHERE "login"='testdoc1';
UPDATE user_account SET "login"= 'PQU4VQKTaVehCA9bdW/9fg==', email='F/Lx0EcWWOKlEs7pURu5snk9bjWD8W13Fsch0dz2mosedo4dXP4RM5w2u87i6jHZ' WHERE "login"='pat12';
UPDATE user_account SET "login"= 'nvexy5tR8w+Jlzql7jHO5g==', email='3zMyWoaQ06s9iPbRCD6OmZt0i+5sIsZd/TXTaVhhmvg=' WHERE "login"='mitglied';

--------- MORBUS BECHTEREW
INSERT INTO haq(id, sortorder, "HAQ-Question", explanation, disease_id) VALUES (8,   0, 'Müdigkeit / Erschöpfung', 'Müdigkeit / Erschöpfung', 441);
INSERT INTO haq(id, sortorder, "HAQ-Question", explanation, disease_id) VALUES (9,   1, 'Schmerzen', 'Schmerzen', 441);
INSERT INTO haq(id, sortorder, "HAQ-Question", explanation, disease_id) VALUES (10,  2, 'Schmerzen oder Schwellungen', 'Schmerzen oder Schwellungen', 441);
INSERT INTO haq(id, sortorder, "HAQ-Question", explanation, disease_id) VALUES (11,  3, 'Berührungs- oder Druckempfindliche Körperstellen', 'Berührungs- oder Druckempfindliche Körperstellen', 441);
INSERT INTO haq(id, sortorder, "HAQ-Question", explanation, disease_id) VALUES (12,  4, 'Morgensteifigkeit', 'Morgensteifigkeit', 441);
INSERT INTO haq(id, sortorder, "HAQ-Question", explanation, disease_id) VALUES (13,  5, 'Körperliche Tätigkeiten', 'Körperliche Tätigkeiten', 441);
INSERT INTO haq(id, sortorder, "HAQ-Question", explanation, disease_id) VALUES (14,  6, 'Aktiv', 'Aktiv', 441);
INSERT INTO haq(id, sortorder, "HAQ-Question", explanation, disease_id) VALUES (15,  7, 'Selbständigkeit', 'Selbständigkeit', 441);
INSERT INTO haq(id, sortorder, "HAQ-Question", explanation, disease_id) VALUES (16,  8, 'Allgemeine Tätigkeiten (Arbeitsproduktivität)', 'Allgemeine Tätigkeiten (Arbeitsproduktivität)', 441);
INSERT INTO haq(id, sortorder, "HAQ-Question", explanation, disease_id) VALUES (17,  9, 'Psychologisches Befinden', 'Psychologisches Befinden', 441);
INSERT INTO haq(id, sortorder, "HAQ-Question", explanation, disease_id) VALUES (18, 10, 'Augen', 'Augen', 441);
INSERT INTO haq(id, sortorder, "HAQ-Question", explanation, disease_id) VALUES (19, 11, 'Magen Darm', 'Magen Darm', 441);
INSERT INTO haq(id, sortorder, "HAQ-Question", explanation, disease_id) VALUES (20, 12, 'Haut', 'Haut', 441);
INSERT INTO haq(id, sortorder, "HAQ-Question", explanation, disease_id) VALUES (21, 13, 'Enthesien (Punkte)', 'Enthesien (Punkte)', 441);

INSERT INTO haq(id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (22, 1, 0, 'Einmalige Abfrage (wird nur das erste Mal abgefragt)', 'Einmalige Abfrage (wird nur das erste Mal abgefragt)', 441);
INSERT INTO haq(id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (23, 1, 0, 'Einmalige Abfrage (wird nur das erste Mal abgefragt)', 'Einmalige Abfrage (wird nur das erste Mal abgefragt)', 437);
INSERT INTO haq(id, kind, sortorder, "HAQ-Question", explanation, disease_id) VALUES (24, 1, 0, 'Einmalige Abfrage (wird nur das erste Mal abgefragt)', 'Einmalige Abfrage (wird nur das erste Mal abgefragt)', 443);

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
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2045, 4, 21, 1, 'Hüfte aussen links');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2046, 5, 21, 1, 'Hüfte innen links');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2047, 6, 21, 1, 'Becken links');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2048, 7, 21, 1, 'Becken rechts');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2049, 8, 21, 1, 'Sitzbein');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2050, 9, 21, 1, 'Ferse links');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2051, 10, 21, 1, 'Ferse rechts');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2052, 11, 21, 1, 'Hüfte aussen rechts');
INSERT INTO question(kind, id, sort_order, haq_id, question_type_id, "text") VALUES (0, 2053, 12, 21, 1, 'Hüfte innen rechts');

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

--##########################
CREATE TABLE country
(
  id bigint NOT NULL,
  code character varying(255) NOT NULL,
  "name" character varying(255) NOT NULL,
  CONSTRAINT country_pkey PRIMARY KEY (id),
  CONSTRAINT country_code_key UNIQUE (code),
  CONSTRAINT country_name_key UNIQUE (name)
);
ALTER TABLE country OWNER TO projecth;

INSERT INTO country(id, code, "name") VALUES (nextval('hibernate_sequence'), 'DE', 'Germany');
INSERT INTO country(id, code, "name") VALUES (nextval('hibernate_sequence'), 'CH', 'Switzerland');
INSERT INTO country(id, code, "name") VALUES (nextval('hibernate_sequence'), 'AT', 'Austria');
INSERT INTO country(id, code, "name") VALUES (nextval('hibernate_sequence'), 'LI', 'Fürstentum Liechtenstein');

ALTER TABLE "user" ALTER COLUMN "name" DROP NOT NULL;
ALTER TABLE "user" ADD COLUMN surname character varying(255);
ALTER TABLE "user" ADD COLUMN title character varying(100);

ALTER TABLE "user" ADD COLUMN medic_instit_country_id bigint;
ALTER TABLE "user" ADD CONSTRAINT fk_medic_instit_country_id FOREIGN KEY (medic_instit_country_id) REFERENCES country (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_medic_instit_country_id ON "user"(medic_instit_country_id);
ALTER TABLE "user" ADD COLUMN medic_instit_name character varying(255);
ALTER TABLE "user" ADD COLUMN medic_instit_homepage_url character varying(255);
ALTER TABLE "user" ADD COLUMN medic_instit_fax_country_code character varying(5);
ALTER TABLE "user" ADD COLUMN medic_instit_fax_number character varying(10);
ALTER TABLE "user" ADD COLUMN medic_instit_phone_country_code character varying(5);
ALTER TABLE "user" ADD COLUMN medic_instit_phone_number character varying(10);
ALTER TABLE "user" ADD COLUMN medic_instit_address_postal_code character varying(10);
ALTER TABLE "user" ADD COLUMN medic_instit_address_number character varying(20);
ALTER TABLE "user" ADD COLUMN medic_instit_address character varying(255);
ALTER TABLE "user" ADD COLUMN medic_instit_address_place character varying(255);

------------------------------------------
INSERT INTO user_account(id, email, "password", "login", created) VALUES (nextval('hibernate_sequence'), 'PYAWJKxC29vmUkTs2KqeBZt0i+5sIsZd/TXTaVhhmvg=', '65bb4b19d9e485fb57a3b02443619235b16bee7a52f0d256f51a0c226746b3cc', '5ZCNMNm5jc4QYzJZ59EJ1JcSPcP0Gv44YfGCMXoXGYY=', '2010-01-01');
INSERT INTO "user"(id, sex, "name", birthday, initialsymptomdate, initialdiagnosisdate, user_account_id, user_type, user_state, created, last_login, last_update) VALUES (nextval('hibernate_sequence'), 0, 'projecth Testpatient', 1968, '2000-08-22', '2004-03-14', (select a.id from user_account a where a.login='5ZCNMNm5jc4QYzJZ59EJ1JcSPcP0Gv44YfGCMXoXGYY='), 'P', 'A', '2010-01-01', '2010-01-01', '2010-01-01');
INSERT INTO user_disease(id, user_id, disease_id) VALUES (nextval('hibernate_sequence'), (select u.id from "user" u where u.name='projecth Testpatient'), 437);

INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1001);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 2, (select u.id from "user" u where u.name='projecth Testpatient'), 1002);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1003);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1004);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1005);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1006);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1007);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1008);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1009);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1010);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1011);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1012);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1013);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1014);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1021);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1022);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1023);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1024);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1025);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1026);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1027);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1028);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1029);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1030);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1031);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1032);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1033);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 2, (select u.id from "user" u where u.name='projecth Testpatient'), 1034);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 2, (select u.id from "user" u where u.name='projecth Testpatient'), 1051);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1052);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1053);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1054);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1055);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1056);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1057);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1058);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1059);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1060);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1061);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1062);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1063);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 2, (select u.id from "user" u where u.name='projecth Testpatient'), 1064);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1071);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 2, (select u.id from "user" u where u.name='projecth Testpatient'), 1072);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1073);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1074);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1075);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1076);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1077);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1078);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1079);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1080);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1081);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1082);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1083);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 3, (select u.id from "user" u where u.name='projecth Testpatient'), 1084);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 1, (select u.id from "user" u where u.name='projecth Testpatient'), 1100);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 16, (select u.id from "user" u where u.name='projecth Testpatient'), 1102);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 13, (select u.id from "user" u where u.name='projecth Testpatient'), 1115);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 1, (select u.id from "user" u where u.name='projecth Testpatient'), 1127);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 1, (select u.id from "user" u where u.name='projecth Testpatient'), 1128);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 2, (select u.id from "user" u where u.name='projecth Testpatient'), 1129);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 1, (select u.id from "user" u where u.name='projecth Testpatient'), 1130);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 1, (select u.id from "user" u where u.name='projecth Testpatient'), 1131);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 2, (select u.id from "user" u where u.name='projecth Testpatient'), 1132);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 1, (select u.id from "user" u where u.name='projecth Testpatient'), 1133);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 1, (select u.id from "user" u where u.name='projecth Testpatient'), 1134);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 1, (select u.id from "user" u where u.name='projecth Testpatient'), 1135);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 26, (select u.id from "user" u where u.name='projecth Testpatient'), 1136);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 24, (select u.id from "user" u where u.name='projecth Testpatient'), 1137);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 25, (select u.id from "user" u where u.name='projecth Testpatient'), 1138);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 23, (select u.id from "user" u where u.name='projecth Testpatient'), 1139);
INSERT INTO patient_question_answer (id, kind, custom_answer, date_taken, logdate, answer_id, patient_id, question_id) VALUES (nextval('hibernate_sequence'), 0, NULL, NULL, '2011-01-16 16:39:59.147', 2, (select u.id from "user" u where u.name='projecth Testpatient'), 1140);

-------------
ALTER TABLE "user" ADD COLUMN date_of_birth date;
ALTER TABLE "user" ADD COLUMN photo character varying(255);
ALTER TABLE "user" ADD COLUMN country_of_living_id bigint;
ALTER TABLE "user" ADD CONSTRAINT fk_user_country_of_living_id FOREIGN KEY (country_of_living_id) REFERENCES country (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_user_country_of_living_id ON "user"(country_of_living_id);

ALTER TABLE "user" ADD COLUMN nationality_id bigint;
ALTER TABLE "user" ADD CONSTRAINT fk_user_nationality_id FOREIGN KEY (nationality_id) REFERENCES country (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_user_nationality_id ON "user"(nationality_id);

CREATE TABLE race
(
  id bigint NOT NULL,
  code character varying(255) NOT NULL,
  "name" character varying(255) NOT NULL,
  CONSTRAINT race_pkey PRIMARY KEY (id),
  CONSTRAINT race_code_key UNIQUE (code),
  CONSTRAINT race_name_key UNIQUE (name)
);
ALTER TABLE race OWNER TO projecth;

ALTER TABLE "user" ADD COLUMN race_id bigint;
ALTER TABLE "user" ADD CONSTRAINT fk_user_race_id FOREIGN KEY (race_id) REFERENCES race (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_user_race_id ON "user"(race_id);

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


CREATE TABLE education
(
  id bigint NOT NULL,
  code character varying(255) NOT NULL,
  "name" character varying(255) NOT NULL,
  CONSTRAINT education_pkey PRIMARY KEY (id),
  CONSTRAINT education_code_key UNIQUE (code),
  CONSTRAINT education_name_key UNIQUE (name)
);
ALTER TABLE education OWNER TO projecth;

ALTER TABLE "user" ADD COLUMN education_id bigint;
ALTER TABLE "user" ADD CONSTRAINT fk_user_education_id FOREIGN KEY (education_id) REFERENCES education (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_user_education_id ON "user"(education_id);

INSERT INTO education(id, code, "name") VALUES (1, 'Obligatorische Schule', 'Obligatorische Schule');
INSERT INTO education(id, code, "name") VALUES (2, 'Fachschule', 'Fachschule');
INSERT INTO education(id, code, "name") VALUES (3, 'Hochschule', 'Hochschule');
INSERT INTO education(id, code, "name") VALUES (4, 'Fachhochschule', 'Fachhochschule');
INSERT INTO education(id, code, "name") VALUES (5, 'Universität', 'Universität');
INSERT INTO education(id, code, "name") VALUES (6, 'Master', 'Master');
INSERT INTO education(id, code, "name") VALUES (7, 'Dr.', 'Dr.');
INSERT INTO education(id, code, "name") VALUES (8, 'Professor', 'Professor');

CREATE TABLE family_situation
(
  id bigint NOT NULL,
  code character varying(255) NOT NULL,
  "name" character varying(255) NOT NULL,
  CONSTRAINT family_situation_pkey PRIMARY KEY (id),
  CONSTRAINT family_situation_code_key UNIQUE (code),
  CONSTRAINT family_situation_name_key UNIQUE (name)
);
ALTER TABLE family_situation OWNER TO projecth;

ALTER TABLE "user" ADD COLUMN family_situation_id bigint;
ALTER TABLE "user" ADD CONSTRAINT fk_user_family_situation_id FOREIGN KEY (family_situation_id) REFERENCES family_situation (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_user_family_situation_id ON "user"(family_situation_id);

INSERT INTO family_situation(id, code, "name") VALUES (1, 'Allein lebend', 'Allein lebend');
INSERT INTO family_situation(id, code, "name") VALUES (2, '2-Personenpaarhaushalt', '2-Personenpaarhaushalt');
INSERT INTO family_situation(id, code, "name") VALUES (3, 'Paar mit Kind <25 Jahren', 'Paar mit Kind <25 Jahren');
INSERT INTO family_situation(id, code, "name") VALUES (4, 'Allein stehend mit Kind <25 Jahren', 'Allein stehend mit Kind <25 Jahren');
INSERT INTO family_situation(id, code, "name") VALUES (5, 'Anderer Familienhaushalt', 'Anderer Familienhaushalt');
INSERT INTO family_situation(id, code, "name") VALUES (6, 'Wohngemeinschaft', 'Wohngemeinschaft');
INSERT INTO family_situation(id, code, "name") VALUES (7, 'Nicht Familienhaushalt', 'Nicht Familienhaushalt');

ALTER TABLE "user" ADD COLUMN height integer;
CREATE TABLE user_weight
(
  user_id bigint NOT NULL,
  date date NOT NULL,
  weight integer NOT NULL,
  CONSTRAINT fk_user_weight_user_id FOREIGN KEY (user_id)
      REFERENCES "user" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE user_weight OWNER TO projecth;

CREATE TABLE patient_key_performance_indicator_validation
(
  id bigint NOT NULL,
  log_date timestamp without time zone NOT NULL,
  kpi_value numeric(19,2) NOT NULL,
  doctor_id bigint NOT NULL,
  validation_type_id bigint NOT NULL,
  patient_id bigint NOT NULL,
  data character varying(1000) NOT NULL,
  CONSTRAINT patient_key_performance_indicator_validation_pkey PRIMARY KEY (id),
  CONSTRAINT fk_patient_kpi_doctor_id FOREIGN KEY (doctor_id)
      REFERENCES "user" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_patient_kpi_kpit_id FOREIGN KEY (validation_type_id)
      REFERENCES key_performance_indicator_type (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_patient_kpi_patient_id FOREIGN KEY (patient_id)
      REFERENCES "user" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE patient_key_performance_indicator_validation OWNER TO projecth;
drop table doctor_patient_questionary_validation;

CREATE TABLE patient_kpi_validation_data
(
  patient_kpi_id bigint NOT NULL,
  data numeric(19,2),
  idx integer NOT NULL,
  CONSTRAINT patient_kpi_validation_data_pkey PRIMARY KEY (patient_kpi_id, idx)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE patient_kpi_validation_data OWNER TO projecth;

CREATE TABLE user_granted_right
(
  user_id bigint NOT NULL,
  "right" integer,
  CONSTRAINT fk29f0a224c44c83ad FOREIGN KEY (user_id)
      REFERENCES "user" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT user_granted_right_user_id_key UNIQUE (user_id, "right")
)
WITH (
  OIDS=FALSE
);
ALTER TABLE user_granted_right OWNER TO projecth;

INSERT INTO key_performance_indicator_type(id, "type", description, code, disease_id) VALUES (275, 2, 'Psoriasis Area Severity Index', 'PASI', 443);

ALTER TABLE "user" ADD COLUMN about_me character varying(4000);
ALTER TABLE "user" ADD COLUMN activation_state integer not null default 1;

INSERT INTO user_account(id, email, "password", "login", created) VALUES (nextval('hibernate_sequence'), 'xX9SiJHAffHte2/n/txcUlNSc9S9T7Iic/MFtI2wg0U=', '65bb4b19d9e485fb57a3b02443619235b16bee7a52f0d256f51a0c226746b3cc', 'f+OFMquCxmqozeX60qHZkQ==', now());
INSERT INTO "user"(id, "name", user_account_id, user_type, user_state, description, created, last_login, last_update)
    VALUES (nextval('hibernate_sequence'), 'Administrator', (select ua.id from user_account ua where login='f+OFMquCxmqozeX60qHZkQ=='), 'A', 'A', 'Administrator', now(), now(), now());

CREATE TABLE system
(
  id bigint NOT NULL,
  model_version character varying(255) NOT NULL,
  CONSTRAINT system_pkey PRIMARY KEY (id)
);
ALTER TABLE system OWNER TO projecth;

INSERT INTO "system"( id, model_version) VALUES (1, '0.1.2.0');
ALTER TABLE "user" ALTER COLUMN "name" DROP NOT NULL;
CREATE INDEX idx_patient_kpi_kpit_id ON patient_key_performance_indicator_validation USING btree (validation_type_id);
CREATE INDEX idx_patient_kpi_disease_id ON patient_key_performance_indicator_validation USING btree (disease_id);

-------------------------------- 0.1.2.0

delete from question_type_answer where id >= 84;
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

CREATE TABLE message
(
  id bigint NOT NULL DEFAULT nextval('hibernate_sequence'::regclass),
  created timestamp without time zone NOT NULL DEFAULT now(),
  readed boolean NOT NULL DEFAULT false,
  spam boolean NOT NULL DEFAULT false,
  send_copy_to_sender boolean NOT NULL DEFAULT false,
  subject character varying(255) NOT NULL,
  body character varying(4000) NOT NULL,
  receiver_user_id bigint NOT NULL,
  sender_user_id bigint NOT NULL,
  in_reply_to_message_id bigint,
  CONSTRAINT message_pkey PRIMARY KEY (id),
  CONSTRAINT fk_message_receiver_user_id FOREIGN KEY (receiver_user_id)
      REFERENCES "user" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_message_sender_user_id FOREIGN KEY (sender_user_id)
      REFERENCES "user" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_message_in_reply_to_message_id FOREIGN KEY (in_reply_to_message_id)
      REFERENCES message (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE message OWNER TO projecth;
CREATE INDEX idx_message_receiver_user_id ON message (receiver_user_id ASC NULLS LAST);
CREATE INDEX idx_message_sender_user_id ON message (sender_user_id ASC NULLS LAST);
CREATE INDEX idx_message_in_reply_to_message_id ON message (in_reply_to_message_id ASC NULLS LAST);
ALTER TABLE question ADD COLUMN extended_type integer;
update question set extended_type = 0 where "text" = 'Datum erste Symptome';
update question set extended_type = 1 where "text" = 'Datum Diagnose';

ALTER TABLE "user" ALTER medic_instit_address_postal_code TYPE character varying(100);
ALTER TABLE "user" ALTER medic_instit_address_number TYPE character varying(100);
ALTER TABLE "user" ALTER medic_instit_phone_number TYPE character varying(100);
ALTER TABLE "user" ALTER medic_instit_phone_country_code TYPE character varying(100);
ALTER TABLE "user" ALTER medic_instit_fax_number TYPE character varying(100);
ALTER TABLE "user" ALTER medic_instit_fax_country_code TYPE character varying(100);
ALTER TABLE haq ADD COLUMN tag character varying(255);


------------- BECHTEREW
INSERT INTO haq_chart(id, sort_order, title, subtitle, tag, x_axis_name, y_axis_name, chart_type_id, haq_id) 
	VALUES (nextval('hibernate_sequence'), 0, 'Müdigkeit / Erschöpfung', 'Wie würden Sie Ihre allgemeine Müdigkeit und Erschöpfung beschreiben?', 'tag1', 'Bewertungsskala (0 = Sehr gut; 10 = Sehr schlecht)', 'Häufigkeit', 3, 8);
INSERT INTO haq_chart(id, sort_order, title, subtitle, tag, x_axis_name, y_axis_name, chart_type_id, haq_id) 
	VALUES (nextval('hibernate_sequence'), 0, 'Schmerzen', 'Wie stark waren ihre Schmerzen in Nacken, Rücken oder Hüfte?', 'tag1', 'Bewertungsskala (0 = Sehr gut; 10 = Sehr schlecht)', 'Häufigkeit', 3, 9);
INSERT INTO haq_chart(id, sort_order, title, subtitle, tag, x_axis_name, y_axis_name, chart_type_id, haq_id) 
	VALUES (nextval('hibernate_sequence'), 0, 'Schmerzen oder Schwellungen', 'Wie stark waren Ihre Schmerzen oder Schwellungen an anderen Gelenken?', 'tag1', 'Bewertungsskala (0 = Sehr gut; 10 = Sehr schlecht)', 'Häufigkeit', 3, 10);
INSERT INTO haq_chart(id, sort_order, title, subtitle, tag, x_axis_name, y_axis_name, chart_type_id, haq_id) 
	VALUES (nextval('hibernate_sequence'), 0, 'Berührungs- oder Druckempfindliche Körperstellen', 'Wie unangenehm waren für Sie berührungs- oder druckempfindliche Körperstellen?', 'tag1', 'Bewertungsskala (0 = Sehr gut; 10 = Sehr schlecht)', 'Häufigkeit', 3, 11);
INSERT INTO haq_chart(id, sort_order, title, subtitle, tag, x_axis_name, y_axis_name, chart_type_id, haq_id) 
	VALUES (nextval('hibernate_sequence'), 0, 'Morgensteifigkeit', 'Wie ausgeprägt war Ihre Morgensteifigkeit nach dem aufwachen?', 'tag1', 'Bewertungsskala (0 = Sehr gut; 10 = Sehr schlecht)', 'Häufigkeit', 3, 12);
INSERT INTO haq_chart(id, sort_order, title, subtitle, tag, x_axis_name, y_axis_name, chart_type_id, haq_id) 
	VALUES (nextval('hibernate_sequence'), 1, 'Morgensteifigkeit', 'Wenn ja, wie lange dauerte diese Morgensteifigkeit im Allgemeinen?', 'tag1', 'Bewertungsskala (0 = Sehr gut; 10 = Sehr schlecht)', 'Häufigkeit', 3, 12);
INSERT INTO haq_chart(id, sort_order, title, subtitle, tag, x_axis_name, y_axis_name, chart_type_id, haq_id) 
	VALUES (nextval('hibernate_sequence'), 0, 'Körperliche Tätigkeiten', 'Konnten Sie körperlich anstrengende Tätigkeiten verrichten (z. B. krankengymnastische Uebungen, Gartenarbeit oder Sport)?', 'tag1', 'Bewertungsskala (0 = Sehr gut; 10 = Sehr schlecht)', 'Häufigkeit', 3, 13);
INSERT INTO haq_chart(id, sort_order, title, subtitle, tag, x_axis_name, y_axis_name, chart_type_id, haq_id) 
	VALUES (nextval('hibernate_sequence'), 0, 'Aktiv', 'Konnten Sie zuhause oder bei der Arbeit den ganzen Tag aktiv sein?', 'tag1', 'Bewertungsskala (0 = Sehr gut; 10 = Sehr schlecht)', 'Häufigkeit', 3, 14);
INSERT INTO haq_chart(id, sort_order, title, subtitle, tag, x_axis_name, y_axis_name, chart_type_id, haq_id) 
	VALUES (nextval('hibernate_sequence'), 0, 'Selbständigkeit', 'Konnten Sie für sich selbst sorgen?', 'tag1', 'Bewertungsskala (0 = Sehr gut; 10 = Sehr schlecht)', 'Häufigkeit', 3, 15);
INSERT INTO haq_chart(id, sort_order, title, subtitle, tag, x_axis_name, y_axis_name, chart_type_id, haq_id) 
	VALUES (nextval('hibernate_sequence'), 0, 'Allgemeine Tätigkeiten (Arbeitsproduktivität)', 'Konnten Sie Ihren Tätigkeiten nachgehen (Arbeit, Studium, Hausarbeit, Familien- oder Freizeitaktivitäten)', 'tag1', 'Bewertungsskala (0 = Sehr gut; 10 = Sehr schlecht)', 'Häufigkeit', 3, 16);
INSERT INTO haq_chart(id, sort_order, title, subtitle, tag, x_axis_name, y_axis_name, chart_type_id, haq_id) 
	VALUES (nextval('hibernate_sequence'), 1, 'Allgemeine Tätigkeiten (Arbeitsproduktivität)', 'Mussten Sie Ihrer Arbeitsstelle aufgrund Ihrer Erkrankung in den letzten 7 Tagen fern bleiben?', 'tag1', '', 'Häufigkeit', 3, 16);
INSERT INTO haq_chart(id, sort_order, title, subtitle, tag, x_axis_name, y_axis_name, chart_type_id, haq_id) 
	VALUES (nextval('hibernate_sequence'), 0, 'Psychologisches Befinden', 'Aufgrund meiner Erkrankung bin ich oft niedergeschlagen.', 'tag1', '', 'Häufigkeit', 3, 17);
INSERT INTO haq_chart(id, sort_order, title, subtitle, tag, x_axis_name, y_axis_name, chart_type_id, haq_id) 
	VALUES (nextval('hibernate_sequence'), 1, 'Psychologisches Befinden', 'Ich habe Angst, die Erwartungen anderer an mich nicht erfüllen zu können.', 'tag1', '', 'Häufigkeit', 3, 17);
INSERT INTO haq_chart(id, sort_order, title, subtitle, tag, x_axis_name, y_axis_name, chart_type_id, haq_id) 
	VALUES (nextval('hibernate_sequence'), 0, 'Augen', 'Hatten Sie in der letzten Woche entzündete Augen?', 'tag1', '', 'Häufigkeit', 3, 18);
INSERT INTO haq_chart(id, sort_order, title, subtitle, tag, x_axis_name, y_axis_name, chart_type_id, haq_id) 
	VALUES (nextval('hibernate_sequence'), 1, 'Augen', 'Hatten Sie deswegen einen Arzt konsultiert?', 'tag1', '', 'Häufigkeit', 3, 18);
INSERT INTO haq_chart(id, sort_order, title, subtitle, tag, x_axis_name, y_axis_name, chart_type_id, haq_id) 
	VALUES (nextval('hibernate_sequence'), 0, 'Magen Darm', 'Hatten Sie in der letzten Woche Probleme im Magen Darm Bereich?', 'tag1', '', 'Häufigkeit', 3, 19);
INSERT INTO haq_chart(id, sort_order, title, subtitle, tag, x_axis_name, y_axis_name, chart_type_id, haq_id) 
	VALUES (nextval('hibernate_sequence'), 1, 'Magen Darm', 'Hatten Sie deswegen einen Arzt konsultiert?', 'tag1', '', 'Häufigkeit', 3, 19);
INSERT INTO haq_chart(id, sort_order, title, subtitle, tag, x_axis_name, y_axis_name, chart_type_id, haq_id) 
	VALUES (nextval('hibernate_sequence'), 0, 'Haut', 'Zeitlicher Verlauf', 'tag1', 'Zeit', 'Anzahl', 1, 20);
INSERT INTO haq_chart(id, sort_order, title, subtitle, tag, x_axis_name, y_axis_name, chart_type_id, haq_id) 
	VALUES (nextval('hibernate_sequence'), 0, 'Enthesien (Punkte)', 'Zeitlicher Verlauf', 'tag1', 'Zeit', 'Anzahl', 1, 21);





------------- PSORIASIS
INSERT INTO haq_chart(id, sort_order, title, subtitle, tag, x_axis_name, y_axis_name, chart_type_id, haq_id) 
	VALUES (nextval('hibernate_sequence'), 0, 'PASI - Werte', 'Wie würden Sie Ihre allgemeine Müdigkeit und Erschöpfung beschreiben?', 'tag1', 'Bewertungsskala (0 = Sehr gut; 10 = Sehr schlecht)', 'Häufigkeit', 3, 31);

INSERT INTO haq_chart(id, sort_order, title, subtitle, tag, x_axis_name, y_axis_name, chart_type_id, haq_id) 
	VALUES (nextval('hibernate_sequence'), 0, 'Gesundheitsfrage', 'Wie fühlen Sie sich heute? Bitte beantworten Sie jede Zeile.', 'tag1', 'Bewertungsskala (0 = Sehr gut; 10 = Sehr schlecht)', 'Häufigkeit', 3, 32);
INSERT INTO haq_chart(id, sort_order, title, subtitle, tag, x_axis_name, y_axis_name, chart_type_id, haq_id) 
	VALUES (nextval('hibernate_sequence'), 0, 'Psychologisches Befinden', 'Bitte beantworten Sie jede Zeile mit "Ja" oder "Nein. Ich fühle mich heute...', 'tag1', 'Bewertungsskala (0 = Sehr gut; 10 = Sehr schlecht)', 'Häufigkeit', 3, 33);
INSERT INTO haq_chart(id, sort_order, title, subtitle, tag, x_axis_name, y_axis_name, chart_type_id, haq_id) 
	VALUES (nextval('hibernate_sequence'), 1, 'Psychologisches Befinden', 'Wie oft juckt Ihre Haut?', 'tag1', 'Bewertungsskala (0 = Sehr gut; 10 = Sehr schlecht)', 'Häufigkeit', 3, 33);
INSERT INTO haq_chart(id, sort_order, title, subtitle, tag, x_axis_name, y_axis_name, chart_type_id, haq_id) 
	VALUES (nextval('hibernate_sequence'), 2, 'Psychologisches Befinden', 'Wie oft fühlt sich Ihre Haut schmerzhaft oder irritiert an?', 'tag1', 'Bewertungsskala (0 = Sehr gut; 10 = Sehr schlecht)', 'Häufigkeit', 3, 33);


drop view patient_answer_result_set_timeline;
drop view patient_answer_result_set;
drop view question_catalog;
drop table patient_answer;
drop table question_answer;
drop table questionary;

ALTER TABLE country ADD COLUMN locale character varying(255);
update country set locale = 'de_DE' where code = 'DE';
update country set locale = 'fr_CH' where code = 'CH';
update country set locale = 'de_AT' where code = 'AT';
update country set locale = 'de_LI' where code = 'LI';
ALTER TABLE country ALTER COLUMN locale SET NOT NULL;
ALTER TABLE country ADD CONSTRAINT country_locale_key UNIQUE (locale);

ALTER TABLE message ADD COLUMN deleted_by_sender boolean not null default false;
ALTER TABLE message ADD COLUMN deleted_by_receiver boolean not null default false;
ALTER TABLE patient_key_performance_indicator_validation DROP COLUMN data;
 
ALTER TABLE patient_medication ADD COLUMN comment character varying(1000);
ALTER TABLE patient_medication ADD COLUMN cumsuption_date timestamp without time zone;
update patient_medication set cumsuption_date = "timestamp";
ALTER TABLE patient_medication ALTER COLUMN cumsuption_date SET NOT NULL;

ALTER TABLE question add column tag character varying(255);

-------------------------------
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'AF', 'Afghanistan', 'en_AF');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'AL', 'Albanien', 'sq_AL');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'DZ', 'Algerien', 'ar_DZ');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'AS', 'American Samoa', 'en_AS');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'AD', 'Andorra', 'en_AD');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'AO', 'Angola', 'en_AO');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'AI', 'Anguilla', 'en_AI');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'AQ', 'Antarctica', 'en_AQ');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'AG', 'Antigua And Barbuda', 'en_AG');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'AR', 'Argentinien', 'es_AR');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'AM', 'Armenia', 'en_AM');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'AW', 'Aruba', 'en_AW');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'AU', 'Australien', 'en_AU');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'AZ', 'Azerbaijan', 'en_AZ');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'BS', 'Bahamas', 'en_BS');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'BH', 'Bahrain', 'ar_BH');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'BD', 'Bangladesh', 'en_BD');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'BB', 'Barbados', 'en_BB');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'BY', 'Belarus', 'be_BY');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'BE', 'Belgien', 'nl_BE');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'BZ', 'Belize', 'en_BZ');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'BJ', 'Benin', 'en_BJ');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'BM', 'Bermuda', 'en_BM');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'BT', 'Bhutan', 'en_BT');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'BO', 'Bolivien', 'es_BO');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'BQ', 'Bonaire, Saint Eustatius And Saba', 'en_BQ');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'BA', 'Bosnien Und Herzegowina', 'sr_BA');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'BW', 'Botswana', 'en_BW');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'BV', 'Bouvet Island', 'en_BV');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'BR', 'Brasilien', 'pt_BR');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'IO', 'British Indian Ocean Territory', 'en_IO');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'BN', 'Brunei Darussalam', 'en_BN');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'BG', 'Bulgarien', 'bg_BG');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'BF', 'Burkina Faso', 'en_BF');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'BI', 'Burundi', 'en_BI');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'KH', 'Cambodia', 'en_KH');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'CM', 'Cameroon', 'en_CM');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'CA', 'Kanada', 'en_CA');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'CV', 'Cape Verde', 'en_CV');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'KY', 'Cayman Islands', 'en_KY');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'CF', 'Central African Republic', 'en_CF');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'TD', 'Chad', 'en_TD');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'CL', 'Chile', 'es_CL');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'CN', 'China', 'zh_CN');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'CX', 'Christmas Island', 'en_CX');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'CC', 'Cocos (keeling) Islands', 'en_CC');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'CO', 'Kolumbien', 'es_CO');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'KM', 'Comoros', 'en_KM');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'CG', 'Congo', 'en_CG');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'CD', 'Congo, The Democratic Republic Of The', 'en_CD');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'CK', 'Cook Islands', 'en_CK');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'CR', 'Costa Rica', 'es_CR');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'HR', 'Kroatien', 'hr_HR');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'CU', 'Cuba', 'en_CU');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'CW', 'Curacao', 'en_CW');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'CY', 'Zypern', 'el_CY');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'CZ', 'Tschechische Republik', 'cs_CZ');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'DK', 'Dänemark', 'da_DK');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'DJ', 'Djibouti', 'en_DJ');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'DM', 'Dominica', 'en_DM');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'DO', 'Dominikanische Republik', 'es_DO');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'EC', 'Ecuador', 'es_EC');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'EG', 'Ägypten', 'ar_EG');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'SV', 'El Salvador', 'es_SV');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'GQ', 'Equatorial Guinea', 'en_GQ');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'ER', 'Eritrea', 'en_ER');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'EE', 'Estland', 'et_EE');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'ET', 'Ethiopia', 'en_ET');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'FK', 'Falkland Islands (malvinas)', 'en_FK');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'FO', 'Faroe Islands', 'en_FO');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'FJ', 'Fiji', 'en_FJ');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'FI', 'Finnland', 'fi_FI');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'FR', 'Frankreich', 'fr_FR');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'GF', 'French Guiana', 'en_GF');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'PF', 'French Polynesia', 'en_PF');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'TF', 'French Southern Territories', 'en_TF');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'GA', 'Gabon', 'en_GA');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'GM', 'Gambia', 'en_GM');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'GE', 'Georgia', 'en_GE');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'GH', 'Ghana', 'en_GH');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'GI', 'Gibraltar', 'en_GI');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'GR', 'Griechenland', 'el_GR');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'GL', 'Greenland', 'en_GL');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'GD', 'Grenada', 'en_GD');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'GP', 'Guadeloupe', 'en_GP');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'GU', 'Guam', 'en_GU');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'GT', 'Guatemala', 'es_GT');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'GG', 'Guernsey', 'en_GG');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'GN', 'Guinea', 'en_GN');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'GW', 'Guinea-bissau', 'en_GW');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'GY', 'Guyana', 'en_GY');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'HT', 'Haiti', 'en_HT');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'HM', 'Heard Island And Mcdonald Islands', 'en_HM');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'VA', 'Holy See (vatican City State)', 'en_VA');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'HN', 'Honduras', 'es_HN');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'HK', 'Hongkong', 'zh_HK');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'HU', 'Ungarn', 'hu_HU');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'IS', 'Island', 'is_IS');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'IN', 'Indien', 'en_IN');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'ID', 'Indonesien', 'in_ID');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'IR', 'Iran, Islamic Republic Of', 'en_IR');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'IQ', 'Irak', 'ar_IQ');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'IE', 'Irland', 'en_IE');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'IM', 'Isle Of Man', 'en_IM');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'IL', 'Israel', 'iw_IL');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'IT', 'Italien', 'it_IT');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'JM', 'Jamaica', 'en_JM');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'JP', 'Japan', 'ja_JP_JP');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'JE', 'Jersey', 'en_JE');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'JO', 'Jordanien', 'ar_JO');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'KZ', 'Kazakhstan', 'en_KZ');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'KE', 'Kenya', 'en_KE');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'KI', 'Kiribati', 'en_KI');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'KP', 'Korea, Democratic People"s Republic Of', 'en_KP');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'KR', 'Südkorea', 'ko_KR');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'KW', 'Kuwait', 'ar_KW');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'KG', 'Kyrgyzstan', 'en_KG');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'LA', 'Lao People"s Democratic Republic', 'en_LA');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'LV', 'Lettland', 'lv_LV');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'LB', 'Libanon', 'ar_LB');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'LS', 'Lesotho', 'en_LS');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'LR', 'Liberia', 'en_LR');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'LY', 'Libyen', 'ar_LY');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'LT', 'Litauen', 'lt_LT');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'LU', 'Luxemburg', 'de_LU');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'MO', 'Macao', 'en_MO');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'MK', 'Mazedonien', 'mk_MK');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'MG', 'Madagascar', 'en_MG');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'MW', 'Malawi', 'en_MW');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'MY', 'Malaysia', 'ms_MY');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'MV', 'Maldives', 'en_MV');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'ML', 'Mali', 'en_ML');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'MT', 'Malta', 'mt_MT');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'MH', 'Marshall Islands', 'en_MH');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'MQ', 'Martinique', 'en_MQ');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'MR', 'Mauritania', 'en_MR');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'MU', 'Mauritius', 'en_MU');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'YT', 'Mayotte', 'en_YT');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'MX', 'Mexiko', 'es_MX');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'FM', 'Micronesia, Federated States Of', 'en_FM');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'MD', 'Moldova, Republic Of', 'en_MD');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'MC', 'Monaco', 'en_MC');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'MN', 'Mongolia', 'en_MN');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'ME', 'Montenegro', 'sr_ME');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'MS', 'Montserrat', 'en_MS');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'MA', 'Marokko', 'ar_MA');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'MZ', 'Mozambique', 'en_MZ');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'MM', 'Myanmar', 'en_MM');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'NA', 'Namibia', 'en_NA');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'NR', 'Nauru', 'en_NR');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'NP', 'Nepal', 'en_NP');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'NL', 'Niederlande', 'nl_NL');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'NC', 'New Caledonia', 'en_NC');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'NZ', 'Neuseeland', 'en_NZ');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'NI', 'Nicaragua', 'es_NI');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'NE', 'Niger', 'en_NE');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'NG', 'Nigeria', 'en_NG');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'NU', 'Niue', 'en_NU');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'NF', 'Norfolk Island', 'en_NF');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'MP', 'Northern Mariana Islands', 'en_MP');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'NO', 'Norwegen', 'no_NO_NY');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'OM', 'Oman', 'ar_OM');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'PK', 'Pakistan', 'en_PK');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'PW', 'Palau', 'en_PW');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'PS', 'Palestinian Territory, Occupied', 'en_PS');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'PA', 'Panama', 'es_PA');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'PG', 'Papua New Guinea', 'en_PG');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'PY', 'Paraguay', 'es_PY');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'PE', 'Peru', 'es_PE');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'PH', 'Philippinen', 'en_PH');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'PN', 'Pitcairn', 'en_PN');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'PL', 'Polen', 'pl_PL');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'PT', 'Portugal', 'pt_PT');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'PR', 'Puerto Rico', 'es_PR');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'QA', 'Katar', 'ar_QA');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'RO', 'Rumänien', 'ro_RO');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'RU', 'Russland', 'ru_RU');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'RW', 'Rwanda', 'en_RW');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'BL', 'Saint Barth�lemy', 'en_BL');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'SH', 'Saint Helena, Ascension And Tristan Da Cunha', 'en_SH');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'KN', 'Saint Kitts And Nevis', 'en_KN');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'LC', 'Saint Lucia', 'en_LC');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'MF', 'Saint Martin (french Part)', 'en_MF');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'PM', 'Saint Pierre And Miquelon', 'en_PM');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'VC', 'Saint Vincent And The Grenadines', 'en_VC');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'WS', 'Samoa', 'en_WS');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'SM', 'San Marino', 'en_SM');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'ST', 'Sao Tome And Principe', 'en_ST');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'SA', 'Saudi-arabien', 'ar_SA');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'SN', 'Senegal', 'en_SN');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'RS', 'Serbien', 'sr_RS');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'SC', 'Seychelles', 'en_SC');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'SL', 'Sierra Leone', 'en_SL');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'SG', 'Singapur', 'en_SG');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'SX', 'Sint Maarten (dutch Part)', 'en_SX');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'SK', 'Slowakei', 'sk_SK');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'SI', 'Slowenien', 'sl_SI');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'SB', 'Solomon Islands', 'en_SB');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'SO', 'Somalia', 'en_SO');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'ZA', 'Südafrika', 'en_ZA');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'GS', 'South Georgia And The South Sandwich Islands', 'en_GS');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'ES', 'Spanien', 'ca_ES');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'LK', 'Sri Lanka', 'en_LK');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'SD', 'Sudan', 'ar_SD');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'SR', 'Suriname', 'en_SR');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'SJ', 'Svalbard And Jan Mayen', 'en_SJ');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'SZ', 'Swaziland', 'en_SZ');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'SE', 'Schweden', 'sv_SE');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'SY', 'Syrien', 'ar_SY');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'TW', 'Taiwan', 'zh_TW');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'TJ', 'Tajikistan', 'en_TJ');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'TZ', 'Tanzania, United Republic Of', 'en_TZ');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'TH', 'Thailand', 'th_TH_TH');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'TL', 'Timor-leste', 'en_TL');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'TG', 'Togo', 'en_TG');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'TK', 'Tokelau', 'en_TK');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'TO', 'Tonga', 'en_TO');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'TT', 'Trinidad And Tobago', 'en_TT');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'TN', 'Tunesien', 'ar_TN');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'TR', 'Türkei', 'tr_TR');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'TM', 'Turkmenistan', 'en_TM');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'TC', 'Turks And Caicos Islands', 'en_TC');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'TV', 'Tuvalu', 'en_TV');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'UG', 'Uganda', 'en_UG');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'UA', 'Ukraine', 'uk_UA');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'AE', 'Vereinigte Arabische Emirate', 'ar_AE');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'GB', 'Vereinigtes Königreich', 'en_GB');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'US', 'Vereinigte Staaten Von Amerika', 'es_US');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'UM', 'United States Minor Outlying Islands', 'en_UM');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'UY', 'Uruguay', 'es_UY');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'UZ', 'Uzbekistan', 'en_UZ');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'VU', 'Vanuatu', 'en_VU');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'VE', 'Venezuela', 'es_VE');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'VN', 'Vietnam', 'vi_VN');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'VG', 'Virgin Islands, British', 'en_VG');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'VI', 'Virgin Islands, U.s.', 'en_VI');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'WF', 'Wallis And Futuna', 'en_WF');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'EH', 'Western Sahara', 'en_EH');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'YE', 'Jemen', 'ar_YE');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'ZM', 'Zambia', 'en_ZM');
INSERT INTO country(id, code, "name", locale) VALUES (nextval('hibernate_sequence'), 'ZW', 'Zimbabwe', 'en_ZW');

update country set "name" = 'Deutschland' where "name" = 'Germany';

INSERT INTO haq_chart(id, sort_order, subtitle, tag, title, x_axis_name, y_axis_name, chart_type_id, haq_id) VALUES (38089, 2, 'Bitte geben Sie die Antwort an, die am Besten Ihre Fähigkeit beschreibt.', 'tag1', '', '', 'Häufigkeit', 6, 7);

INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (38082, 1100);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (38083, 1102);

INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (38087, 1138);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (38088, 1140);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (38089, 1139);

INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (480134, 2004);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (480135, 2005);

INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (480139, 2009);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (480140, 2010);

INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (480141, 2011);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (480142, 2012);

INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (480143, 2013);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (480144, 2014);

INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (480145, 2015);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (480146, 2016);

INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (480151, 2118);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (480152, 2119);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (480153, 2120);

INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (38086, 1136);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (38090, 1137);

delete from patient_question_answer where question_id >= 2017 and question_id <= 2040;
delete from question where id >= 2019 and id <= 2039;

INSERT INTO question(kind, id, sort_order, "text", explanation, haq_id, question_type_id) VALUES (0, 2019, 0, 'Vorne Arme', 'Vorne Arme',  20, 1);
INSERT INTO question(kind, id, sort_order, "text", explanation, haq_id, question_type_id) VALUES (0, 2020, 0, 'Vorme Rumpf', 'Vorne Rumpf',  20, 1);
INSERT INTO question(kind, id, sort_order, "text", explanation, haq_id, question_type_id) VALUES (0, 2021, 0, 'Vorne Beine', 'Vorne Beine',  20, 1);
INSERT INTO question(kind, id, sort_order, "text", explanation, haq_id, question_type_id) VALUES (0, 2022, 0, 'Hinten Kopf', 'Vorne Kopf',  20, 1);
INSERT INTO question(kind, id, sort_order, "text", explanation, haq_id, question_type_id) VALUES (0, 2023, 0, 'Hinten Arme', 'Vorne Arme',  20, 1);
INSERT INTO question(kind, id, sort_order, "text", explanation, haq_id, question_type_id) VALUES (0, 2024, 0, 'Hinten Rumpf', 'Vorne Rumpf',  20, 1);
INSERT INTO question(kind, id, sort_order, "text", explanation, haq_id, question_type_id) VALUES (0, 2025, 0, 'Hinten Beine', 'Vorne Beine',  20, 1);

INSERT INTO haq_chart(id, sort_order, subtitle, tag, title, x_axis_name, y_axis_name, chart_type_id, haq_id) VALUES (480154, 1, 'Angabe über Beschwerden mit der Haut', 'tag1', 'Haut', '', 'Häufigkeit', 8, 20);
INSERT INTO haq_chart(id, sort_order, subtitle, tag, title, x_axis_name, y_axis_name, chart_type_id, haq_id) VALUES (480155, 2, 'Hatten Sie deswegen einen Arzt konsultiert', 'tag1', 'Haut', '', 'Häufigkeit', 2, 20);

INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (480147, 2017);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (480155, 2040);

INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (480154, 2018);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (480154, 2019);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (480154, 2020);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (480154, 2021);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (480154, 2022);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (480154, 2023);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (480154, 2024);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (480154, 2025);

-- 480149;0;"Angabe über allgemeine Müdigkeit und Erschöpfung";"tag1";"PASI - Werte";"Bewertungsskala (0 = Sehr gut; 10 = Sehr schlecht)";"Häufigkeit";8;31;
update haq_chart_question set haq_chart_id = 480135 where  question_id = 2005;

CREATE INDEX fk_haq_disease_id ON haq (disease_id);
CREATE INDEX fk_question_haq_id ON question (haq_id);
CREATE INDEX fk_pat_quest_answ_logdate ON patient_question_answer (logdate);
CREATE INDEX fk_pat_quest_answ_question_id ON patient_question_answer (question_id);
CREATE INDEX fk_pat_quest_answ_answer_id ON patient_question_answer (answer_id);
CREATE INDEX fk_question_question_type_id ON question (question_type_id);
CREATE INDEX fk_question_type_answer_question_type_id ON question_type_answer (question_type_id);
  
CREATE INDEX fk_patient_medication_patient_id ON patient_medication (patient_id);
CREATE INDEX fk_patient_medication_disease_id ON patient_medication (disease_id);
CREATE INDEX fk_patient_medication_medicine_id ON patient_medication (medicine_id);
CREATE INDEX fk_patient_medication_cumsuption_date ON patient_medication (cumsuption_date);
  
CREATE INDEX fk_message_receiver_user_id ON message (receiver_user_id);
CREATE INDEX fk_message_sender_user_id ON message (sender_user_id);
CREATE INDEX fk_message_deleted_by_sender ON message (deleted_by_sender);
CREATE INDEX fk_message_deleted_by_receiver ON message (deleted_by_receiver);

  
CREATE INDEX fk_user_medic_instit_country_id ON "user" (medic_instit_country_id);
CREATE INDEX fk_user_medic_instit_address_place ON "user" (medic_instit_address_place);
CREATE INDEX fk_user_medic_instit_name ON "user" (medic_instit_name);
CREATE INDEX fk_user_medic_instit_address ON "user" (medic_instit_address);
CREATE INDEX fk_user_medic_instit_address_number ON "user" (medic_instit_address_number);

CREATE INDEX fk_user_activationcode ON "user" (activationcode);
CREATE INDEX fk_user_user_account_id ON "user" (user_account_id);
CREATE INDEX fk_user_surname ON "user" (surname);
CREATE INDEX fk_user_name ON "user" (name);

CREATE INDEX fk_user_connection_state ON user_connection (state);
CREATE INDEX fk_user_connection_owner_id ON user_connection (owner_id);
CREATE INDEX fk_user_user_id ON user_connection (user_id);

CREATE INDEX fk_user_disease_user_id ON user_disease (user_id);
CREATE INDEX fk_user_disease_disease_id ON user_disease (disease_id);
CREATE INDEX fk_user_weight_user_id ON user_weight (user_id);

CREATE INDEX fki_user_account_login ON user_account ("login");
CREATE INDEX fki_user_account_email ON user_account (email);

CREATE INDEX fki_user_granted_right_user_id ON user_granted_right (user_id);

INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (38085, 1127);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (38085, 1128);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (38085, 1129);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (38085, 1130);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (38085, 1131);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (38085, 1132);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (38085, 1133);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (38085, 1134);
INSERT INTO haq_chart_question(haq_chart_id, question_id) VALUES (38085, 1135);

update user_account set "password" = '' where id = 479948;

