-- 25-04-2023 Awadhesh

ALTER TABLE mdms."nature_of_Inspection"
    RENAME TO nature_of_inspection;

ALTER TABLE tdms.inote
    ADD COLUMN inote_type_id integer;
ALTER TABLE tdms.inote
    ADD CONSTRAINT inote_type_id_fkey FOREIGN KEY (inote_type_id)
    REFERENCES mdms.inote_type (id)
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;

ALTER TABLE tdms.inote
    ADD COLUMN emp_name character varying;

ALTER TABLE tdms.inote
    ADD COLUMN emp_designation character varying;

ALTER TABLE tdms.inote
    ADD COLUMN emp_role character varying;

ALTER TABLE tdms.inote
    ADD COLUMN emp_name_hindi character varying;

ALTER TABLE tdms.inote
    ADD COLUMN emp_designation_hindi character varying;

ALTER TABLE tdms.inote
    ADD COLUMN emp_role_hindi character varying;
	
ALTER TABLE mdms.emp_role
    ADD COLUMN emp_role_hindi character varying;
	
	
ALTER TABLE mdms.employee
    ADD COLUMN ename_hindi character varying;
	
	
ALTER TABLE mdms.emp_designation
    ADD COLUMN designation_hindi character varying;
	

ALTER TABLE tdms.inote
    ADD COLUMN description character varying;
	
-- 28-04-2023 Awadhesh

ALTER TABLE mdms.organization
    ADD COLUMN std_code character varying(7);

ALTER TABLE mdms.organization
    ADD COLUMN inote_code character varying(5);

ALTER TABLE mdms.organization
    ADD COLUMN inote_serial_strt integer;
	
ALTER TABLE tdms.inote
    ADD COLUMN inote_no character varying;
	
ALTER TABLE tdms.inote
    ADD COLUMN inote_date timestamp without time zone;





----- Manasa 
----- 11-05-2023
ALTER TABLE IF EXISTS tdms.workload
    ADD CONSTRAINT ukey_so UNIQUE (supply_order_id);
	
	
ALTER TABLE IF EXISTS tdms.workload_ol
    ADD CONSTRAINT ukey_ol_wkld UNIQUE (offer_letter_id);
	
-- 12-05-2023 Awadhesh

ALTER TABLE tdms.project DROP CONSTRAINT project_project_name_key;

ALTER TABLE tdms.inspection_note
    ADD COLUMN cqao_approval_date timestamp without time zone;


-- Shubham Anand
-- 13-05-2023
ALTER TABLE tdms.offer_letter DROP COLUMN order_qty;

ALTER TABLE tdms.offer_letter DROP COLUMN cleared_qty;

ALTER TABLE tdms.offer_letter DROP COLUMN issue_level;

ALTER TABLE tdms.offer_letter DROP COLUMN priority;

ALTER TABLE tdms.inote_line_item
    ADD COLUMN ol_line_item_id integer;
	
	
	
-- 08-06-2023
ALTER TABLE tdms.supply_order
    ADD COLUMN is_notified boolean;
	
	
-- 17-05-2023 Awadhesh
ALTER TABLE tdms.supply_order
    ALTER COLUMN order_no TYPE character varying(250) COLLATE pg_catalog."default";
	
ALTER TABLE tdms.supply_order DROP CONSTRAINT supply_order_order_no_key;
insert into mdms.ol_status values(4, 'Pending');
insert into mdms.ol_status values(5, 'In Progress');

-- 09-06-2023 Awadhesh

CREATE TABLE mdms.account
(
    id integer NOT NULL,
    acct_type character varying(20) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT acct_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE mdms.account
    OWNER to postgres;

GRANT DELETE, INSERT, SELECT, UPDATE ON TABLE mdms.account TO olms_user;

GRANT ALL ON TABLE mdms.account TO postgres;

INSERT INTO mdms.account (id, acct_type) VALUES (1, 'Govt');
INSERT INTO mdms.account (id, acct_type) VALUES (2, 'Private');
INSERT INTO mdms.account (id, acct_type) VALUES (3, 'PSU');
INSERT INTO mdms.account (id, acct_type) VALUES (4, 'DPSU');

ALTER TABLE tdms.supply_order
    ADD COLUMN acct_id integer;
ALTER TABLE tdms.supply_order
    ADD CONSTRAINT acct_id_fkey FOREIGN KEY (acct_id)
    REFERENCES mdms.acct (id)
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;

-- 27-06-2023 Awadhesh

ALTER TABLE IF EXISTS tdms.inote_line_item
    ADD COLUMN order_qty integer;

-- 05-07-2023 Awadhesh

ALTER TABLE tdms.ol_approval_details
    ALTER COLUMN remarks TYPE character varying(10000) COLLATE pg_catalog."default";

ALTER TABLE tdms.inspection_note
ALTER COLUMN total_inotes_cc_issued TYPE character varying(50);

ALTER TABLE tdms.inspection_note
ALTER COLUMN no_line_items_supplied TYPE character varying(50);

ALTER TABLE tdms.inspection_note
ALTER COLUMN lot_size_original TYPE character varying(500);

ALTER TABLE tdms.fcm
ALTER COLUMN no_of_items TYPE character varying(50);

ALTER TABLE tdms.fcm
ALTER COLUMN no_of_lots TYPE character varying(500);

-- 13-07-2023 Awadhesh

alter table mdms.employee add column seal_no character varying(10);


-- 01-08-2023 Manasa B


ALTER TABLE IF EXISTS mdms.emp_role
    ADD COLUMN sec_emp_role_id character varying;

UPDATE mdms.emp_role SET
sec_emp_role_id = 'SUPER_ADMIN'::character varying WHERE id = 1;

UPDATE mdms.emp_role SET
sec_emp_role_id = 'CQAE_ADMIN'::character varying WHERE id = 2;

UPDATE mdms.emp_role SET
sec_emp_role_id = 'CQAO'::character varying WHERE id = 3;

UPDATE mdms.emp_role SET
sec_emp_role_id = 'JCQAO'::character varying WHERE id = 4;

UPDATE mdms.emp_role SET
sec_emp_role_id = 'DCQAO'::character varying WHERE id = 5;

UPDATE mdms.emp_role SET
sec_emp_role_id = 'ACQAO'::character varying WHERE id = 6;

UPDATE mdms.emp_role SET
sec_emp_role_id = 'INSP'::character varying WHERE id = 7;

UPDATE mdms.emp_role SET
sec_emp_role_id = 'COMP_ADMIN'::character varying WHERE id = 8;

UPDATE mdms.emp_role SET
sec_emp_role_id = 'SBU_ADMIN'::character varying WHERE id = 9;

UPDATE mdms.emp_role SET
sec_emp_role_id = 'QA'::character varying WHERE id = 10;

UPDATE mdms.emp_role SET
sec_emp_role_id = 'SALES_ADMIN'::character varying WHERE id = 11;