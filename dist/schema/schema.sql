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

--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: projecth; Type: COMMENT; Schema: -; Owner: projecth
--

COMMENT ON DATABASE projecth IS 'ver1.0';


--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- Name: parsearrayinitial(bigint, bigint, character varying, character varying, character varying); Type: FUNCTION; Schema: public; Owner: projecth
--

CREATE FUNCTION parsearrayinitial(id bigint, patientid bigint, arrayquestionparam character varying, arrayanswer character varying, separator character varying) RETURNS void
    LANGUAGE plpgsql
    AS $$

DECLARE separator_position INTEGER;
DECLARE separator_positionAnswer INTEGER;
DECLARE array_value VARCHAR(8000);
DECLARE ArrayQuestion VARCHAR(8000);
DECLARE array_valueAnswer VARCHAR(8000);
DECLARE initval INTEGER;

BEGIN
	ArrayQuestion := ArrayQuestionParam || separator;
	initval := 1;
	
	DELETE FROM PatientMedicineRelation WHERE FK_PatientID = PatientID;
	
END;
$$;


ALTER FUNCTION public.parsearrayinitial(id bigint, patientid bigint, arrayquestionparam character varying, arrayanswer character varying, separator character varying) OWNER TO projecth;

--
-- Name: resultssetcdai(timestamp without time zone, timestamp without time zone, integer); Type: FUNCTION; Schema: public; Owner: projecth
--

CREATE FUNCTION resultssetcdai(startdate timestamp without time zone, enddate timestamp without time zone, patientid integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$

DECLARE SUMHAQ1 float;
DECLARE SUMHAQ2 float;
DECLARE SUMHAQ4 float;
DECLARE Measures float;
DECLARE Result float;
DECLARE CDAI float;
DECLARE round_val INTEGER;

BEGIN
	round_val := 2;
	Select INTO Measures count(distinct LogDate) from PatientEntryResultSet where PatientID = PatientID AND Logdate Between StartDate AND EndDate;
	Select INTO SUMHAQ1 Count(answer_id) from	PatientEntryResultSet WHERE (HAQID = 1) AND answer_id = 1 and PatientID = PatientID AND Logdate Between StartDate AND EndDate;
	Select INTO SUMHAQ2 Count(answer_id) from	PatientEntryResultSet WHERE (HAQID = 2) AND answer_id = 1 and PatientID = PatientID AND Logdate Between StartDate AND EndDate;
	Select INTO SUMHAQ4 SUM(answer_id) from	PatientEntryResultSet WHERE (HAQID = 4) AND answer_id >= 0 and PatientID = PatientID AND Logdate Between StartDate AND EndDate;
		
		IF Measures = 0 THEN
			CDAI := 0;
		ELSE
			Result := (SUMHAQ1/Measures) + (SUMHAQ2/Measures) + (SUMHAQ4/Measures);
			CDAI := Round(Result, round_val);
		END IF;
		
	RETURN CDAI; 
END;
$$;


ALTER FUNCTION public.resultssetcdai(startdate timestamp without time zone, enddate timestamp without time zone, patientid integer) OWNER TO projecth;

--
-- Name: unix_date(double precision); Type: FUNCTION; Schema: public; Owner: projecth
--

CREATE FUNCTION unix_date(unixdate double precision) RETURNS timestamp without time zone
    LANGUAGE plpgsql
    AS $$
	BEGIN
		RETURN to_timestamp(unixdate);
        END;
$$;


ALTER FUNCTION public.unix_date(unixdate double precision) OWNER TO projecth;

--
-- Name: update_das28(); Type: FUNCTION; Schema: public; Owner: projecth
--

CREATE FUNCTION update_das28() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
    BEGIN
		-- calculate das28 column value
        NEW.das28 := (round((((0.28)*power(NEW.haq1sum,(0.5))+(0.56)*power(NEW.HAQ2SUM,(0.5)))+(0.7)*NEW.BSG )+((0.014)*NEW.NRS )*(10),(0)));
        RETURN NEW;
    END;
$$;


ALTER FUNCTION public.update_das28() OWNER TO projecth;

--
-- Name: update_pat_gender(); Type: FUNCTION; Schema: public; Owner: projecth
--

CREATE FUNCTION update_pat_gender() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
    BEGIN
		-- calculate das28 column value
        NEW.gender := case NEW.Sex  when (0) then 'Frau' when (1) then 'Herr'  end;
        RETURN NEW;
    END;
$$;


ALTER FUNCTION public.update_pat_gender() OWNER TO projecth;

--
-- Name: update_pat_time_sympt_diag(); Type: FUNCTION; Schema: public; Owner: projecth
--

CREATE FUNCTION update_pat_time_sympt_diag() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
    BEGIN
        NEW.timesymptomdiagnostic := CAST(NEW.initialdiagnosisdate - NEW.initialsymptomdate as int) / 30;
        RETURN NEW;
    END;
$$;


ALTER FUNCTION public.update_pat_time_sympt_diag() OWNER TO projecth;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: chart_type; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE chart_type (
    id bigint NOT NULL,
    description character varying(255),
    type integer NOT NULL
);


ALTER TABLE public.chart_type OWNER TO projecth;

--
-- Name: country; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE country (
    id bigint NOT NULL,
    code character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    locale character varying(255) NOT NULL
);


ALTER TABLE public.country OWNER TO projecth;

--
-- Name: disease; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE disease (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(255) NOT NULL,
    image_name character varying(255),
    disease_subgroup_id bigint NOT NULL,
    tag character varying(255)
);


ALTER TABLE public.disease OWNER TO projecth;

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: projecth
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO projecth;

--
-- Name: disease_group; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE disease_group (
    id bigint DEFAULT nextval('hibernate_sequence'::regclass) NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.disease_group OWNER TO projecth;

--
-- Name: disease_subgroup; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE disease_subgroup (
    id bigint DEFAULT nextval('hibernate_sequence'::regclass) NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(255) NOT NULL,
    disease_group_id bigint NOT NULL
);


ALTER TABLE public.disease_subgroup OWNER TO projecth;

--
-- Name: education; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE education (
    id bigint NOT NULL,
    code character varying(255) NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.education OWNER TO projecth;

--
-- Name: family_situation; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE family_situation (
    id bigint NOT NULL,
    code character varying(255) NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.family_situation OWNER TO projecth;

--
-- Name: haq; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE haq (
    id bigint DEFAULT nextval('hibernate_sequence'::regclass) NOT NULL,
    sortorder integer NOT NULL,
    "HAQ-Question" character varying(255) NOT NULL,
    explanation character varying(4000) NOT NULL,
    disease_id bigint NOT NULL,
    kind integer DEFAULT 0 NOT NULL,
    tag character varying(255)
);


ALTER TABLE public.haq OWNER TO projecth;

--
-- Name: haq_chart; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE haq_chart (
    id bigint NOT NULL,
    sort_order integer NOT NULL,
    subtitle character varying(255),
    tag character varying(255) NOT NULL,
    title character varying(255) NOT NULL,
    x_axis_name character varying(255),
    y_axis_name character varying(255),
    chart_type_id bigint NOT NULL,
    haq_id bigint NOT NULL,
    width double precision
);


ALTER TABLE public.haq_chart OWNER TO projecth;

--
-- Name: haq_chart_question; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE haq_chart_question (
    haq_chart_id bigint NOT NULL,
    question_id bigint NOT NULL
);


ALTER TABLE public.haq_chart_question OWNER TO projecth;

--
-- Name: key_performance_indicator_type; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE key_performance_indicator_type (
    id bigint DEFAULT nextval('hibernate_sequence'::regclass) NOT NULL,
    code character varying(255) NOT NULL,
    description character varying(255) NOT NULL,
    disease_id bigint NOT NULL,
    type integer NOT NULL
);


ALTER TABLE public.key_performance_indicator_type OWNER TO projecth;

--
-- Name: TABLE key_performance_indicator_type; Type: COMMENT; Schema: public; Owner: projecth
--

COMMENT ON TABLE key_performance_indicator_type IS 'Contains list of possible validation value types, which doctor can validate by patient, i.e. CDAI';


--
-- Name: medicine_class; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE medicine_class (
    id bigint DEFAULT nextval('hibernate_sequence'::regclass) NOT NULL,
    name character varying(255) NOT NULL,
    expireddate timestamp without time zone,
    createdatetime timestamp without time zone DEFAULT now() NOT NULL,
    pzn character varying(7),
    standard_unit_size character varying(255),
    locale character varying(255) NOT NULL
);


ALTER TABLE public.medicine_class OWNER TO projecth;

--
-- Name: message; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE message (
    id bigint DEFAULT nextval('hibernate_sequence'::regclass) NOT NULL,
    created timestamp without time zone DEFAULT now() NOT NULL,
    readed boolean DEFAULT false NOT NULL,
    spam boolean DEFAULT false NOT NULL,
    send_copy_to_sender boolean DEFAULT false NOT NULL,
    subject character varying(255) NOT NULL,
    body character varying(4000) NOT NULL,
    receiver_user_id bigint NOT NULL,
    sender_user_id bigint NOT NULL,
    in_reply_to_message_id bigint,
    deleted_by_sender boolean DEFAULT false NOT NULL,
    deleted_by_receiver boolean DEFAULT false NOT NULL
);


ALTER TABLE public.message OWNER TO projecth;

--
-- Name: patient_answer; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE patient_answer (
    id bigint DEFAULT nextval('hibernate_sequence'::regclass) NOT NULL,
    timestampcreated timestamp without time zone DEFAULT now() NOT NULL,
    logdate timestamp without time zone NOT NULL,
    fk_patient bigint NOT NULL,
    fk_questionaryid bigint NOT NULL,
    answer_id bigint NOT NULL
);


ALTER TABLE public.patient_answer OWNER TO projecth;

--
-- Name: question_answer; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE question_answer (
    fk_questionaryid bigint NOT NULL,
    answerid bigint NOT NULL,
    answertext character varying(255) NOT NULL,
    id bigint DEFAULT nextval('hibernate_sequence'::regclass) NOT NULL
);


ALTER TABLE public.question_answer OWNER TO projecth;

--
-- Name: questionary; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE questionary (
    id bigint DEFAULT nextval('hibernate_sequence'::regclass) NOT NULL,
    fk_haqid bigint,
    questiontext character varying(255)
);


ALTER TABLE public.questionary OWNER TO projecth;

--
-- Name: user; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE "user" (
    id bigint DEFAULT nextval('hibernate_sequence'::regclass) NOT NULL,
    user_type character(1) NOT NULL,
    name character varying(255),
    description character varying(4000),
    activation_uid character varying(255),
    user_account_id bigint,
    resultid character varying(50),
    activationcode character varying(50),
    sex integer,
    gender character varying(20),
    birthday integer,
    initialsymptomdate date,
    initialdiagnosisdate date,
    timesymptomdiagnostic integer,
    created timestamp without time zone DEFAULT now() NOT NULL,
    last_login timestamp without time zone DEFAULT now() NOT NULL,
    user_state character(1) NOT NULL,
    connection_requests_blocked boolean DEFAULT false NOT NULL,
    logins_count integer DEFAULT 0 NOT NULL,
    last_update timestamp without time zone DEFAULT now() NOT NULL,
    unsuccessful_logins_count integer DEFAULT 0 NOT NULL,
    surname character varying(255),
    title character varying(100),
    medic_instit_country_id bigint,
    medic_instit_name character varying(255),
    medic_instit_homepage_url character varying(255),
    medic_instit_fax_country_code character varying(100),
    medic_instit_fax_number character varying(100),
    medic_instit_phone_country_code character varying(100),
    medic_instit_phone_number character varying(100),
    medic_instit_address_postal_code character varying(100),
    medic_instit_address_number character varying(100),
    medic_instit_address character varying(255),
    date_of_birth date,
    country_of_living_id bigint,
    nationality_id bigint,
    family_situation_id bigint,
    race_id bigint,
    education_id bigint,
    height integer,
    photo character varying(255),
    medic_instit_address_place character varying(255),
    about_me character varying(4000),
    activation_state integer DEFAULT 1 NOT NULL
);


ALTER TABLE public."user" OWNER TO projecth;

--
-- Name: patient_answer_result_set; Type: VIEW; Schema: public; Owner: projecth
--

CREATE VIEW patient_answer_result_set AS
    SELECT patient_answer.timestampcreated, patient_answer.logdate, patient_answer.fk_patient AS patientid, u.resultid AS patientresultid, u.gender AS patientgender, u.name AS patientname, u.birthday AS patientbirthday, (date_part('year'::text, now()) - (u.birthday)::double precision) AS patientage, u.initialsymptomdate AS patientinitialsymptomdate, u.initialdiagnosisdate AS patientinitialdiagnosisdate, haq.id AS haqid, haq."HAQ-Question" AS haq, haq.explanation AS haqexplanation, patient_answer.fk_questionaryid AS questionid, questionary.questiontext, patient_answer.answer_id, question_answer.answertext FROM ((("user" u RIGHT JOIN (patient_answer LEFT JOIN question_answer ON (((patient_answer.answer_id = question_answer.answerid) AND (patient_answer.fk_questionaryid = question_answer.fk_questionaryid)))) ON (((u.id = patient_answer.fk_patient) AND (u.id = patient_answer.fk_patient)))) LEFT JOIN questionary ON ((patient_answer.fk_questionaryid = questionary.id))) LEFT JOIN haq ON (((questionary.fk_haqid = haq.id) AND (questionary.fk_haqid = haq.id)))) WHERE (u.user_type = 'P'::bpchar);


ALTER TABLE public.patient_answer_result_set OWNER TO projecth;

--
-- Name: time_dimension; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE time_dimension (
    timeid bigint DEFAULT nextval('hibernate_sequence'::regclass) NOT NULL,
    thedate date,
    theday character varying(10),
    themonth character varying(10),
    theyear smallint,
    dayofmonth smallint,
    weekofyear smallint,
    monthofyear smallint,
    quarterofyear character varying(2),
    fiscalperiod smallint
);


ALTER TABLE public.time_dimension OWNER TO projecth;

--
-- Name: patient_answer_result_set_timeline; Type: VIEW; Schema: public; Owner: projecth
--

CREATE VIEW patient_answer_result_set_timeline AS
    SELECT time_dimension.thedate AS logdate, time_dimension.theday, time_dimension.themonth, time_dimension.theyear, time_dimension.dayofmonth, time_dimension.weekofyear, time_dimension.monthofyear, time_dimension.quarterofyear, time_dimension.fiscalperiod, patient_answer_result_set.timestampcreated, patient_answer_result_set.patientid, patient_answer_result_set.patientresultid, patient_answer_result_set.patientgender, patient_answer_result_set.patientname, patient_answer_result_set.patientbirthday, patient_answer_result_set.patientage, patient_answer_result_set.patientinitialsymptomdate, patient_answer_result_set.patientinitialdiagnosisdate, patient_answer_result_set.haqid, patient_answer_result_set.haq, patient_answer_result_set.haqexplanation, patient_answer_result_set.questionid, patient_answer_result_set.questiontext, patient_answer_result_set.answer_id, patient_answer_result_set.answertext FROM (patient_answer_result_set RIGHT JOIN time_dimension ON ((patient_answer_result_set.logdate = time_dimension.thedate)));


ALTER TABLE public.patient_answer_result_set_timeline OWNER TO projecth;

--
-- Name: patient_group; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE patient_group (
    id bigint DEFAULT nextval('hibernate_sequence'::regclass) NOT NULL,
    groupname character varying(50) NOT NULL,
    groupdescription character varying(4000)
);


ALTER TABLE public.patient_group OWNER TO projecth;

--
-- Name: patient_group_relation; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE patient_group_relation (
    id bigint DEFAULT nextval('hibernate_sequence'::regclass) NOT NULL,
    fk_patientid bigint NOT NULL,
    fk_patientgroupid bigint NOT NULL
);


ALTER TABLE public.patient_group_relation OWNER TO projecth;

--
-- Name: patient_key_performance_indicator; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE patient_key_performance_indicator (
    id bigint NOT NULL,
    log_date timestamp without time zone NOT NULL,
    timestampcreated timestamp without time zone NOT NULL,
    kpi_value numeric(19,2) NOT NULL,
    validation_type_id bigint NOT NULL,
    patient_id bigint NOT NULL
);


ALTER TABLE public.patient_key_performance_indicator OWNER TO projecth;

--
-- Name: patient_key_performance_indicator_validation; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE patient_key_performance_indicator_validation (
    id bigint NOT NULL,
    log_date timestamp without time zone NOT NULL,
    kpi_value numeric(19,2) NOT NULL,
    doctor_id bigint NOT NULL,
    validation_type_id bigint NOT NULL,
    patient_id bigint NOT NULL
);


ALTER TABLE public.patient_key_performance_indicator_validation OWNER TO projecth;

--
-- Name: patient_kpi_validation_data; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE patient_kpi_validation_data (
    patient_kpi_id bigint NOT NULL,
    data numeric(19,2),
    idx integer NOT NULL
);


ALTER TABLE public.patient_kpi_validation_data OWNER TO projecth;

--
-- Name: patient_medication; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE patient_medication (
    id bigint NOT NULL,
    "timestamp" timestamp without time zone NOT NULL,
    patient_id bigint NOT NULL,
    disease_id bigint NOT NULL,
    medicine_id bigint NOT NULL,
    amount numeric(19,2) NOT NULL,
    comment character varying(1000),
    cumsuption_date timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.patient_medication OWNER TO projecth;

--
-- Name: patient_medicine_relation; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE patient_medicine_relation (
    id bigint DEFAULT nextval('hibernate_sequence'::regclass) NOT NULL,
    fk_patientid bigint NOT NULL,
    fk_medicineid bigint NOT NULL,
    "Value" character varying(255)
);


ALTER TABLE public.patient_medicine_relation OWNER TO projecth;

--
-- Name: patient_medicine; Type: VIEW; Schema: public; Owner: projecth
--

CREATE VIEW patient_medicine AS
    SELECT u.id, u.resultid, u.gender, u.birthday, u.initialsymptomdate, u.initialdiagnosisdate, patient_medicine_relation.fk_patientid, patient_medicine_relation.fk_medicineid, medicine_class.name, patient_medicine_relation."Value", CASE WHEN ((patient_medicine_relation."Value")::text = '0'::text) THEN 'Nein'::character varying WHEN ((patient_medicine_relation."Value")::text = '1'::text) THEN 'Ja'::character varying WHEN ((patient_medicine_relation."Value")::text = '-1'::text) THEN 'Keine Angabe'::character varying ELSE patient_medicine_relation."Value" END AS result FROM (("user" u LEFT JOIN patient_medicine_relation ON ((u.id = patient_medicine_relation.fk_patientid))) LEFT JOIN medicine_class ON ((patient_medicine_relation.fk_medicineid = medicine_class.id))) WHERE (u.user_type = 'P'::bpchar);


ALTER TABLE public.patient_medicine OWNER TO projecth;

--
-- Name: patient_question_answer; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE patient_question_answer (
    id bigint NOT NULL,
    kind integer DEFAULT 0 NOT NULL,
    custom_answer character varying(2500),
    date_taken timestamp without time zone,
    logdate timestamp without time zone NOT NULL,
    answer_id bigint NOT NULL,
    patient_id bigint NOT NULL,
    question_id bigint NOT NULL
);


ALTER TABLE public.patient_question_answer OWNER TO projecth;

--
-- Name: patient_scanner_data; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE patient_scanner_data (
    id bigint DEFAULT nextval('hibernate_sequence'::regclass) NOT NULL,
    patient_id bigint NOT NULL,
    medicine_name character varying(255),
    units_count integer NOT NULL,
    barcode character varying(255) NOT NULL
);


ALTER TABLE public.patient_scanner_data OWNER TO projecth;

--
-- Name: question; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE question (
    kind integer NOT NULL,
    id bigint NOT NULL,
    image_data character varying(4000),
    image_name character varying(4000),
    sort_order integer NOT NULL,
    text character varying(2500) NOT NULL,
    explanation character varying(2500),
    custom_question_type integer,
    haq_id bigint,
    question_type_id bigint NOT NULL,
    creator_id bigint,
    disease_id bigint,
    target_question_id bigint,
    user_id bigint,
    group_text character varying(255),
    extended_type integer,
    tag character varying(255)
);


ALTER TABLE public.question OWNER TO projecth;

--
-- Name: question_catalog; Type: VIEW; Schema: public; Owner: projecth
--

CREATE VIEW question_catalog AS
    SELECT haq.id AS haqid, haq.sortorder AS haqsortorder, haq."HAQ-Question" AS haq, haq.explanation AS haqexplanation, questionary.id AS questionid, questionary.questiontext, question_answer.answerid, question_answer.answertext FROM ((question_answer JOIN questionary ON ((question_answer.fk_questionaryid = questionary.id))) RIGHT JOIN haq ON ((questionary.fk_haqid = haq.id))) ORDER BY haq.sortorder, questionary.id LIMIT 100;


ALTER TABLE public.question_catalog OWNER TO projecth;

--
-- Name: question_type; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE question_type (
    id bigint NOT NULL,
    description character varying(255),
    type integer NOT NULL,
    answer_data_type integer NOT NULL,
    user_id bigint
);


ALTER TABLE public.question_type OWNER TO projecth;

--
-- Name: question_type_answer; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE question_type_answer (
    kind integer DEFAULT 0 NOT NULL,
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
    question_type_id bigint NOT NULL
);


ALTER TABLE public.question_type_answer OWNER TO projecth;

--
-- Name: question_type_tag; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE question_type_tag (
    questiontype_id bigint NOT NULL,
    tag character varying(255) NOT NULL
);


ALTER TABLE public.question_type_tag OWNER TO projecth;

--
-- Name: race; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE race (
    id bigint NOT NULL,
    code character varying(255) NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.race OWNER TO projecth;

--
-- Name: system; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE system (
    id bigint NOT NULL,
    model_version character varying(255) NOT NULL
);


ALTER TABLE public.system OWNER TO projecth;

--
-- Name: user_account; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE user_account (
    id bigint DEFAULT nextval('hibernate_sequence'::regclass) NOT NULL,
    password character varying(100),
    login character varying(100),
    created timestamp without time zone DEFAULT now() NOT NULL,
    email character varying(100) NOT NULL
);


ALTER TABLE public.user_account OWNER TO projecth;

--
-- Name: user_connection; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE user_connection (
    id bigint NOT NULL,
    state character(1) NOT NULL,
    owner_id bigint NOT NULL,
    user_id bigint NOT NULL,
    created timestamp without time zone DEFAULT now() NOT NULL
);


ALTER TABLE public.user_connection OWNER TO projecth;

--
-- Name: user_disease; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE user_disease (
    id bigint DEFAULT nextval('hibernate_sequence'::regclass) NOT NULL,
    user_id bigint NOT NULL,
    disease_id bigint NOT NULL
);


ALTER TABLE public.user_disease OWNER TO projecth;

--
-- Name: user_granted_right; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE user_granted_right (
    user_id bigint NOT NULL,
    "right" integer
);


ALTER TABLE public.user_granted_right OWNER TO projecth;

--
-- Name: user_weight; Type: TABLE; Schema: public; Owner: projecth; Tablespace: 
--

CREATE TABLE user_weight (
    user_id bigint NOT NULL,
    date date NOT NULL,
    weight numeric(19,2) NOT NULL
);


ALTER TABLE public.user_weight OWNER TO projecth;

--
-- Name: chart_type_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY chart_type
    ADD CONSTRAINT chart_type_pkey PRIMARY KEY (id);


--
-- Name: country_code_key; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY country
    ADD CONSTRAINT country_code_key UNIQUE (code);


--
-- Name: country_name_key; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY country
    ADD CONSTRAINT country_name_key UNIQUE (name);


--
-- Name: country_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY country
    ADD CONSTRAINT country_pkey PRIMARY KEY (id);


--
-- Name: disease_code_key; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY disease
    ADD CONSTRAINT disease_code_key UNIQUE (code);


--
-- Name: disease_group_code_key; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY disease_group
    ADD CONSTRAINT disease_group_code_key UNIQUE (code);


--
-- Name: disease_group_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY disease_group
    ADD CONSTRAINT disease_group_pkey PRIMARY KEY (id);


--
-- Name: disease_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY disease
    ADD CONSTRAINT disease_pkey PRIMARY KEY (id);


--
-- Name: disease_subgroup_code_key; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY disease_subgroup
    ADD CONSTRAINT disease_subgroup_code_key UNIQUE (code);


--
-- Name: disease_subgroup_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY disease_subgroup
    ADD CONSTRAINT disease_subgroup_pkey PRIMARY KEY (id);


--
-- Name: education_code_key; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY education
    ADD CONSTRAINT education_code_key UNIQUE (code);


--
-- Name: education_name_key; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY education
    ADD CONSTRAINT education_name_key UNIQUE (name);


--
-- Name: education_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY education
    ADD CONSTRAINT education_pkey PRIMARY KEY (id);


--
-- Name: family_situation_code_key; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY family_situation
    ADD CONSTRAINT family_situation_code_key UNIQUE (code);


--
-- Name: family_situation_name_key; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY family_situation
    ADD CONSTRAINT family_situation_name_key UNIQUE (name);


--
-- Name: family_situation_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY family_situation
    ADD CONSTRAINT family_situation_pkey PRIMARY KEY (id);


--
-- Name: haq_chart_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY haq_chart
    ADD CONSTRAINT haq_chart_pkey PRIMARY KEY (id);


--
-- Name: haq_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY haq
    ADD CONSTRAINT haq_pkey PRIMARY KEY (id);


--
-- Name: medicineclass_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY medicine_class
    ADD CONSTRAINT medicineclass_pkey PRIMARY KEY (id);


--
-- Name: message_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY message
    ADD CONSTRAINT message_pkey PRIMARY KEY (id);


--
-- Name: patient_group_relation_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY patient_group_relation
    ADD CONSTRAINT patient_group_relation_pkey PRIMARY KEY (id);


--
-- Name: patient_group_relation_ukey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY patient_group_relation
    ADD CONSTRAINT patient_group_relation_ukey UNIQUE (fk_patientid, fk_patientgroupid);


--
-- Name: patient_key_performance_indicator_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY patient_key_performance_indicator
    ADD CONSTRAINT patient_key_performance_indicator_pkey PRIMARY KEY (id);


--
-- Name: patient_key_performance_indicator_validation_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY patient_key_performance_indicator_validation
    ADD CONSTRAINT patient_key_performance_indicator_validation_pkey PRIMARY KEY (id);


--
-- Name: patient_kpi_validation_data_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY patient_kpi_validation_data
    ADD CONSTRAINT patient_kpi_validation_data_pkey PRIMARY KEY (patient_kpi_id, idx);


--
-- Name: patient_medication_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY patient_medication
    ADD CONSTRAINT patient_medication_pkey PRIMARY KEY (id);


--
-- Name: patient_medicine_ukey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY patient_medicine_relation
    ADD CONSTRAINT patient_medicine_ukey UNIQUE (fk_patientid, fk_medicineid);


--
-- Name: patient_question_answer_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY patient_question_answer
    ADD CONSTRAINT patient_question_answer_pkey PRIMARY KEY (id);


--
-- Name: patient_scanner_data_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY patient_scanner_data
    ADD CONSTRAINT patient_scanner_data_pkey PRIMARY KEY (id);


--
-- Name: patiententry_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY patient_answer
    ADD CONSTRAINT patiententry_pkey PRIMARY KEY (id);


--
-- Name: patientgroup_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY patient_group
    ADD CONSTRAINT patientgroup_pkey PRIMARY KEY (id);


--
-- Name: patientmedicinerelation_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY patient_medicine_relation
    ADD CONSTRAINT patientmedicinerelation_pkey PRIMARY KEY (id);


--
-- Name: question_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY question
    ADD CONSTRAINT question_pkey PRIMARY KEY (id);


--
-- Name: question_type_answer_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY question_type_answer
    ADD CONSTRAINT question_type_answer_pkey PRIMARY KEY (id);


--
-- Name: question_type_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY question_type
    ADD CONSTRAINT question_type_pkey PRIMARY KEY (id);


--
-- Name: questionanswer_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY question_answer
    ADD CONSTRAINT questionanswer_pkey PRIMARY KEY (id);


--
-- Name: questionary_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY questionary
    ADD CONSTRAINT questionary_pkey PRIMARY KEY (id);


--
-- Name: questionary_validation_type_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY key_performance_indicator_type
    ADD CONSTRAINT questionary_validation_type_pkey PRIMARY KEY (id);


--
-- Name: race_code_key; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY race
    ADD CONSTRAINT race_code_key UNIQUE (code);


--
-- Name: race_name_key; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY race
    ADD CONSTRAINT race_name_key UNIQUE (name);


--
-- Name: race_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY race
    ADD CONSTRAINT race_pkey PRIMARY KEY (id);


--
-- Name: system_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY system
    ADD CONSTRAINT system_pkey PRIMARY KEY (id);


--
-- Name: sysuser_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY user_account
    ADD CONSTRAINT sysuser_pkey PRIMARY KEY (id);


--
-- Name: timedimension_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY time_dimension
    ADD CONSTRAINT timedimension_pkey PRIMARY KEY (timeid);


--
-- Name: uk_medication_pzn; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY medicine_class
    ADD CONSTRAINT uk_medication_pzn UNIQUE (pzn);


--
-- Name: uk_owner_id_user_id; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY user_connection
    ADD CONSTRAINT uk_owner_id_user_id UNIQUE (owner_id, user_id);


--
-- Name: uk_questionary_validation_type_name; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY key_performance_indicator_type
    ADD CONSTRAINT uk_questionary_validation_type_name UNIQUE (code);


--
-- Name: uk_sysuser_login; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY user_account
    ADD CONSTRAINT uk_sysuser_login UNIQUE (login);


--
-- Name: uk_user_email_key; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY user_account
    ADD CONSTRAINT uk_user_email_key UNIQUE (email);


--
-- Name: user_connection_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY user_connection
    ADD CONSTRAINT user_connection_pkey PRIMARY KEY (id);


--
-- Name: user_disease_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY user_disease
    ADD CONSTRAINT user_disease_pkey PRIMARY KEY (id);


--
-- Name: user_granted_right_user_id_key; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY user_granted_right
    ADD CONSTRAINT user_granted_right_user_id_key UNIQUE (user_id, "right");


--
-- Name: user_pkey; Type: CONSTRAINT; Schema: public; Owner: projecth; Tablespace: 
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: fk_haq_disease_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_haq_disease_id ON haq USING btree (disease_id);


--
-- Name: fk_message_deleted_by_receiver; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_message_deleted_by_receiver ON message USING btree (deleted_by_receiver);


--
-- Name: fk_message_deleted_by_sender; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_message_deleted_by_sender ON message USING btree (deleted_by_sender);


--
-- Name: fk_message_receiver_user_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_message_receiver_user_id ON message USING btree (receiver_user_id);


--
-- Name: fk_message_sender_user_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_message_sender_user_id ON message USING btree (sender_user_id);


--
-- Name: fk_pat_quest_answ_answer_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_pat_quest_answ_answer_id ON patient_question_answer USING btree (answer_id);


--
-- Name: fk_pat_quest_answ_logdate; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_pat_quest_answ_logdate ON patient_question_answer USING btree (logdate);


--
-- Name: fk_pat_quest_answ_question_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_pat_quest_answ_question_id ON patient_question_answer USING btree (question_id);


--
-- Name: fk_patient_medication_cumsuption_date; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_patient_medication_cumsuption_date ON patient_medication USING btree (cumsuption_date);


--
-- Name: fk_patient_medication_disease_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_patient_medication_disease_id ON patient_medication USING btree (disease_id);


--
-- Name: fk_patient_medication_medicine_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_patient_medication_medicine_id ON patient_medication USING btree (medicine_id);


--
-- Name: fk_patient_medication_patient_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_patient_medication_patient_id ON patient_medication USING btree (patient_id);


--
-- Name: fk_question_haq_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_question_haq_id ON question USING btree (haq_id);


--
-- Name: fk_question_question_type_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_question_question_type_id ON question USING btree (question_type_id);


--
-- Name: fk_question_type_answer_question_type_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_question_type_answer_question_type_id ON question_type_answer USING btree (question_type_id);


--
-- Name: fk_user_activationcode; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_user_activationcode ON "user" USING btree (activationcode);


--
-- Name: fk_user_connection_owner_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_user_connection_owner_id ON user_connection USING btree (owner_id);


--
-- Name: fk_user_connection_state; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_user_connection_state ON user_connection USING btree (state);


--
-- Name: fk_user_disease_disease_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_user_disease_disease_id ON user_disease USING btree (disease_id);


--
-- Name: fk_user_disease_user_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_user_disease_user_id ON user_disease USING btree (user_id);


--
-- Name: fk_user_medic_instit_address; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_user_medic_instit_address ON "user" USING btree (medic_instit_address);


--
-- Name: fk_user_medic_instit_address_number; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_user_medic_instit_address_number ON "user" USING btree (medic_instit_address_number);


--
-- Name: fk_user_medic_instit_address_place; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_user_medic_instit_address_place ON "user" USING btree (medic_instit_address_place);


--
-- Name: fk_user_medic_instit_country_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_user_medic_instit_country_id ON "user" USING btree (medic_instit_country_id);


--
-- Name: fk_user_medic_instit_name; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_user_medic_instit_name ON "user" USING btree (medic_instit_name);


--
-- Name: fk_user_name; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_user_name ON "user" USING btree (name);


--
-- Name: fk_user_surname; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_user_surname ON "user" USING btree (surname);


--
-- Name: fk_user_user_account_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_user_user_account_id ON "user" USING btree (user_account_id);


--
-- Name: fk_user_user_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_user_user_id ON user_connection USING btree (user_id);


--
-- Name: fk_user_weight_user_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fk_user_weight_user_id ON user_weight USING btree (user_id);


--
-- Name: fki_kpi_type_disease_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fki_kpi_type_disease_id ON key_performance_indicator_type USING btree (disease_id);


--
-- Name: fki_medic_instit_country_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fki_medic_instit_country_id ON "user" USING btree (medic_instit_country_id);


--
-- Name: fki_user_account_email; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fki_user_account_email ON user_account USING btree (email);


--
-- Name: fki_user_account_login; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fki_user_account_login ON user_account USING btree (login);


--
-- Name: fki_user_country_of_living_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fki_user_country_of_living_id ON "user" USING btree (country_of_living_id);


--
-- Name: fki_user_education_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fki_user_education_id ON "user" USING btree (education_id);


--
-- Name: fki_user_family_situation_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fki_user_family_situation_id ON "user" USING btree (family_situation_id);


--
-- Name: fki_user_granted_right_user_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fki_user_granted_right_user_id ON user_granted_right USING btree (user_id);


--
-- Name: fki_user_nationality_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fki_user_nationality_id ON "user" USING btree (nationality_id);


--
-- Name: fki_user_race_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX fki_user_race_id ON "user" USING btree (race_id);


--
-- Name: idx_message_in_reply_to_message_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX idx_message_in_reply_to_message_id ON message USING btree (in_reply_to_message_id);


--
-- Name: idx_message_receiver_user_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX idx_message_receiver_user_id ON message USING btree (receiver_user_id);


--
-- Name: idx_message_sender_user_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX idx_message_sender_user_id ON message USING btree (sender_user_id);


--
-- Name: idx_patient_kpi_kpit_id; Type: INDEX; Schema: public; Owner: projecth; Tablespace: 
--

CREATE INDEX idx_patient_kpi_kpit_id ON patient_key_performance_indicator_validation USING btree (validation_type_id);


--
-- Name: update_pat_gender; Type: TRIGGER; Schema: public; Owner: projecth
--

CREATE TRIGGER update_pat_gender BEFORE INSERT OR UPDATE ON "user" FOR EACH ROW EXECUTE PROCEDURE update_pat_gender();


--
-- Name: update_pat_time_sympt_diag; Type: TRIGGER; Schema: public; Owner: projecth
--

CREATE TRIGGER update_pat_time_sympt_diag BEFORE INSERT OR UPDATE ON "user" FOR EACH ROW EXECUTE PROCEDURE update_pat_time_sympt_diag();


--
-- Name: fk29f0a224c44c83ad; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY user_granted_right
    ADD CONSTRAINT fk29f0a224c44c83ad FOREIGN KEY (user_id) REFERENCES "user"(id);


--
-- Name: fk_answer_question_type_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY question_type_answer
    ADD CONSTRAINT fk_answer_question_type_id FOREIGN KEY (question_type_id) REFERENCES question_type(id);


--
-- Name: fk_d_disease_subgroup_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY disease
    ADD CONSTRAINT fk_d_disease_subgroup_id FOREIGN KEY (disease_subgroup_id) REFERENCES disease_subgroup(id);


--
-- Name: fk_dsg_disease_group_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY disease_subgroup
    ADD CONSTRAINT fk_dsg_disease_group_id FOREIGN KEY (disease_group_id) REFERENCES disease_group(id);


--
-- Name: fk_haq_disease_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY haq
    ADD CONSTRAINT fk_haq_disease_id FOREIGN KEY (disease_id) REFERENCES disease(id);


--
-- Name: fk_haqchart_chart_type_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY haq_chart
    ADD CONSTRAINT fk_haqchart_chart_type_id FOREIGN KEY (chart_type_id) REFERENCES chart_type(id);


--
-- Name: fk_haqchart_haq_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY haq_chart
    ADD CONSTRAINT fk_haqchart_haq_id FOREIGN KEY (haq_id) REFERENCES haq(id);


--
-- Name: fk_haqchq_haq_chart_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY haq_chart_question
    ADD CONSTRAINT fk_haqchq_haq_chart_id FOREIGN KEY (haq_chart_id) REFERENCES haq_chart(id);


--
-- Name: fk_haqchq_haq_question_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY haq_chart_question
    ADD CONSTRAINT fk_haqchq_haq_question_id FOREIGN KEY (question_id) REFERENCES question(id);


--
-- Name: fk_kpi_type_disease_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY key_performance_indicator_type
    ADD CONSTRAINT fk_kpi_type_disease_id FOREIGN KEY (disease_id) REFERENCES disease(id);


--
-- Name: fk_medic_instit_country_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT fk_medic_instit_country_id FOREIGN KEY (medic_instit_country_id) REFERENCES country(id);


--
-- Name: fk_message_in_reply_to_message_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY message
    ADD CONSTRAINT fk_message_in_reply_to_message_id FOREIGN KEY (in_reply_to_message_id) REFERENCES message(id);


--
-- Name: fk_message_receiver_user_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY message
    ADD CONSTRAINT fk_message_receiver_user_id FOREIGN KEY (receiver_user_id) REFERENCES "user"(id);


--
-- Name: fk_message_sender_user_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY message
    ADD CONSTRAINT fk_message_sender_user_id FOREIGN KEY (sender_user_id) REFERENCES "user"(id);


--
-- Name: fk_pat_grp_rel_patient_group_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY patient_group_relation
    ADD CONSTRAINT fk_pat_grp_rel_patient_group_id FOREIGN KEY (fk_patientgroupid) REFERENCES patient_group(id);


--
-- Name: fk_pat_grp_rel_patient_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY patient_group_relation
    ADD CONSTRAINT fk_pat_grp_rel_patient_id FOREIGN KEY (fk_patientid) REFERENCES "user"(id);


--
-- Name: fk_pat_med_rel_medecine_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY patient_medicine_relation
    ADD CONSTRAINT fk_pat_med_rel_medecine_id FOREIGN KEY (fk_medicineid) REFERENCES medicine_class(id);


--
-- Name: fk_pat_med_rel_patient_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY patient_medicine_relation
    ADD CONSTRAINT fk_pat_med_rel_patient_id FOREIGN KEY (fk_patientid) REFERENCES "user"(id);


--
-- Name: fk_pat_q_answer_answer_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY patient_question_answer
    ADD CONSTRAINT fk_pat_q_answer_answer_id FOREIGN KEY (answer_id) REFERENCES question_type_answer(id);


--
-- Name: fk_pat_q_answer_patient_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY patient_question_answer
    ADD CONSTRAINT fk_pat_q_answer_patient_id FOREIGN KEY (patient_id) REFERENCES "user"(id);


--
-- Name: fk_pat_q_answer_question_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY patient_question_answer
    ADD CONSTRAINT fk_pat_q_answer_question_id FOREIGN KEY (question_id) REFERENCES question(id);


--
-- Name: fk_pat_scann_dat_patient_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY patient_scanner_data
    ADD CONSTRAINT fk_pat_scann_dat_patient_id FOREIGN KEY (patient_id) REFERENCES "user"(id);


--
-- Name: fk_patient_kpi_doctor_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY patient_key_performance_indicator_validation
    ADD CONSTRAINT fk_patient_kpi_doctor_id FOREIGN KEY (doctor_id) REFERENCES "user"(id);


--
-- Name: fk_patient_kpi_kpit_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY patient_key_performance_indicator
    ADD CONSTRAINT fk_patient_kpi_kpit_id FOREIGN KEY (validation_type_id) REFERENCES key_performance_indicator_type(id);


--
-- Name: fk_patient_kpi_kpit_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY patient_key_performance_indicator_validation
    ADD CONSTRAINT fk_patient_kpi_kpit_id FOREIGN KEY (validation_type_id) REFERENCES key_performance_indicator_type(id);


--
-- Name: fk_patient_kpi_patient_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY patient_key_performance_indicator
    ADD CONSTRAINT fk_patient_kpi_patient_id FOREIGN KEY (patient_id) REFERENCES "user"(id);


--
-- Name: fk_patient_kpi_patient_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY patient_key_performance_indicator_validation
    ADD CONSTRAINT fk_patient_kpi_patient_id FOREIGN KEY (patient_id) REFERENCES "user"(id);


--
-- Name: fk_patient_medication_disease_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY patient_medication
    ADD CONSTRAINT fk_patient_medication_disease_id FOREIGN KEY (disease_id) REFERENCES disease(id);


--
-- Name: fk_patient_medication_medicine_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY patient_medication
    ADD CONSTRAINT fk_patient_medication_medicine_id FOREIGN KEY (medicine_id) REFERENCES medicine_class(id);


--
-- Name: fk_patient_medication_patient_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY patient_medication
    ADD CONSTRAINT fk_patient_medication_patient_id FOREIGN KEY (patient_id) REFERENCES "user"(id);


--
-- Name: fk_patiententry_patient_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY patient_answer
    ADD CONSTRAINT fk_patiententry_patient_id FOREIGN KEY (fk_patient) REFERENCES "user"(id);


--
-- Name: fk_patiententry_questionary_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY patient_answer
    ADD CONSTRAINT fk_patiententry_questionary_id FOREIGN KEY (fk_questionaryid) REFERENCES questionary(id);


--
-- Name: fk_qtt_question_type_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY question_type_tag
    ADD CONSTRAINT fk_qtt_question_type_id FOREIGN KEY (questiontype_id) REFERENCES question_type(id);


--
-- Name: fk_question_creator_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY question
    ADD CONSTRAINT fk_question_creator_id FOREIGN KEY (creator_id) REFERENCES "user"(id);


--
-- Name: fk_question_disease_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY question
    ADD CONSTRAINT fk_question_disease_id FOREIGN KEY (disease_id) REFERENCES disease(id);


--
-- Name: fk_question_haq_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY question
    ADD CONSTRAINT fk_question_haq_id FOREIGN KEY (haq_id) REFERENCES haq(id);


--
-- Name: fk_question_question_type_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY question
    ADD CONSTRAINT fk_question_question_type_id FOREIGN KEY (question_type_id) REFERENCES question_type(id);


--
-- Name: fk_question_target_question_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY question
    ADD CONSTRAINT fk_question_target_question_id FOREIGN KEY (target_question_id) REFERENCES question(id);


--
-- Name: fk_question_type_user_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY question_type
    ADD CONSTRAINT fk_question_type_user_id FOREIGN KEY (user_id) REFERENCES "user"(id);


--
-- Name: fk_question_user_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY question
    ADD CONSTRAINT fk_question_user_id FOREIGN KEY (user_id) REFERENCES "user"(id);


--
-- Name: fk_questionanswer_questionary_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY question_answer
    ADD CONSTRAINT fk_questionanswer_questionary_id FOREIGN KEY (fk_questionaryid) REFERENCES questionary(id);


--
-- Name: fk_questionary_haq_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY questionary
    ADD CONSTRAINT fk_questionary_haq_id FOREIGN KEY (fk_haqid) REFERENCES haq(id);


--
-- Name: fk_user_connection_owner_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY user_connection
    ADD CONSTRAINT fk_user_connection_owner_id FOREIGN KEY (owner_id) REFERENCES "user"(id);


--
-- Name: fk_user_connection_user_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY user_connection
    ADD CONSTRAINT fk_user_connection_user_id FOREIGN KEY (user_id) REFERENCES "user"(id);


--
-- Name: fk_user_country_of_living_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT fk_user_country_of_living_id FOREIGN KEY (country_of_living_id) REFERENCES country(id);


--
-- Name: fk_user_disease_disease_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY user_disease
    ADD CONSTRAINT fk_user_disease_disease_id FOREIGN KEY (disease_id) REFERENCES disease(id);


--
-- Name: fk_user_disease_user_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY user_disease
    ADD CONSTRAINT fk_user_disease_user_id FOREIGN KEY (user_id) REFERENCES "user"(id);


--
-- Name: fk_user_education_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT fk_user_education_id FOREIGN KEY (education_id) REFERENCES education(id);


--
-- Name: fk_user_family_situation_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT fk_user_family_situation_id FOREIGN KEY (family_situation_id) REFERENCES family_situation(id);


--
-- Name: fk_user_nationality_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT fk_user_nationality_id FOREIGN KEY (nationality_id) REFERENCES country(id);


--
-- Name: fk_user_race_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT fk_user_race_id FOREIGN KEY (race_id) REFERENCES race(id);


--
-- Name: fk_user_sysuser_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT fk_user_sysuser_id FOREIGN KEY (user_account_id) REFERENCES user_account(id);


--
-- Name: fk_user_weight_user_id; Type: FK CONSTRAINT; Schema: public; Owner: projecth
--

ALTER TABLE ONLY user_weight
    ADD CONSTRAINT fk_user_weight_user_id FOREIGN KEY (user_id) REFERENCES "user"(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

