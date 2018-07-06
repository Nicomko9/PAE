/* schema pae */
/* drop schema pae cascade; */
CREATE SCHEMA pae;

/* séquences de pk */
/* drop sequence pae.pk_addresses; */
CREATE SEQUENCE pae.pk_addresses;
/* drop sequence pae.pk_users; */
CREATE SEQUENCE pae.pk_users;
/* drop sequence pae.pk_companies; */
CREATE SEQUENCE pae.pk_companies;
/* drop sequence pae.pk_contacts; */
CREATE SEQUENCE pae.pk_contacts;

/* table adresses */
/* drop table pae.addresses cascade; */
CREATE TABLE pae.addresses (
  address_number INT DEFAULT nextval('pae.pk_addresses'),
  street         VARCHAR(100),
  street_number  INT,
  box            INT NULL,
  zip_code       INT,
  commune        VARCHAR(100),
  CONSTRAINT adress_pkey PRIMARY KEY (address_number)
);

/* table utilisateurs */
/* drop table pae.users cascade; */
CREATE TABLE pae.users (
  user_number      INT DEFAULT nextval('pae.pk_users'),
  login            VARCHAR(50),
  pwd              VARCHAR(100),
  lastname         VARCHAR(50),
  firstname        VARCHAR(50),
  email            VARCHAR(50),
  inscription_date TIMESTAMP,
  responsible      BOOLEAN,
  CONSTRAINT utilisateur_pkey PRIMARY KEY (user_number)
);

/* insert utilisateurs pré-enregistrés */
INSERT INTO pae.users (user_number, login, pwd, lastname, firstname, email, inscription_date, responsible)
VALUES (default, 'benjam', 'azerty', 'Pierre', 'Benjamin', 'benjam@ipl.be', now(), TRUE);
INSERT INTO pae.users (user_number, login, pwd, lastname, firstname, email, inscription_date, responsible)
VALUES (default, 'nico', 'azerty', 'Chris', 'Nico', 'nico@ipl.be', now(), FALSE);
INSERT INTO pae.users (user_number, login, pwd, lastname, firstname, email, inscription_date, responsible)
VALUES (default, 'clem', 'azerty', 'Du Jardin', 'Clément', 'clem@ipl.be', now(), FALSE);
INSERT INTO pae.users (user_number, login, pwd, lastname, firstname, email, inscription_date, responsible)
VALUES (default, 'thom', 'azerty', 'Ronsmans', 'Thomas', 'thom@ipl.be', now(), FALSE);
INSERT INTO pae.users (user_number, login, pwd, lastname, firstname, email, inscription_date, responsible)
VALUES (default, 'zak', 'azerty', 'Lamrini', 'Zakaria', 'zak@ipl.be', now(), FALSE);

/* table entreprises */
/* drop table pae.companies cascade; */
CREATE TABLE pae.companies (
  company_number          INT DEFAULT nextval('pae.pk_companies'),
  company_name            VARCHAR(50),
  creator                 INT REFERENCES pae.users (user_number),
  address_number          INT REFERENCES pae.addresses (address_number),
  inscription_date        TIMESTAMP,
  last_participation_year INT,
  CONSTRAINT entreprise_pkey PRIMARY KEY (company_number)
);

/* table personnes contacts */
/* drop table pae.contacts cascade; */
CREATE TABLE pae.contacts (
  contact_number INT DEFAULT nextval('pae.pk_contacts'),
  company_number INT REFERENCES pae.companies (company_number),
  lastname       VARCHAR(50),
  firstname      VARCHAR(50),
  email          VARCHAR(50),
  phone          VARCHAR(20),
  active         BOOLEAN,
  CONSTRAINT contact_pkey PRIMARY KEY (contact_number)
);

/* table journees_entreprises */
/* drop table pae.company_days cascade; */
CREATE TABLE pae.company_days (
  day_year INT,
  day_date DATE,
  CONSTRAINT company_day_pkey PRIMARY KEY (day_year)
);

/* table participations */
/* drop table pae.participations cascade; */
CREATE TABLE pae.participations (
  participation_year INT,
  company_number     INT REFERENCES pae.companies (company_number),
  state              VARCHAR(10),
  last_state         VARCHAR(10) NULL,
  CONSTRAINT participation_pkey PRIMARY KEY (participation_year, company_number)
);

/* table présences */
/* drop table pae.presences cascade; */
CREATE TABLE pae.presences (
  participation_year INT,
  company_number     INT REFERENCES pae.companies (company_number),
  contact_number     INT REFERENCES pae.contacts (contact_number),
  CONSTRAINT presence_pkey PRIMARY KEY (participation_year, company_number, contact_number)
);

