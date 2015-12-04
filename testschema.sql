----------------------------------------------
-- Export file for user BLOCK               --
-- Created by block on 03/12/2015, 12:31:08 --
----------------------------------------------

set define off
spool testschema.log

prompt
prompt Creating table LOCATIONTYPE
prompt ===========================
prompt
create table LOCATIONTYPE
(
  locationtypeid NUMBER not null,
  name           VARCHAR2(200) not null
)
;
alter table LOCATIONTYPE
  add constraint LT_PK primary key (LOCATIONTYPEID);
alter table LOCATIONTYPE
  add constraint LT_NAME_UK unique (NAME);

prompt
prompt Creating table LOCATION
prompt =======================
prompt
create table LOCATION
(
  locationid    NUMBER not null,
  name          VARCHAR2(100) not null,
  locationtype  NUMBER not null,
  someblob      BLOB,
  someclob      CLOB,
  someraw       RAW(100),
  somelong      LONG,
  somedate      DATE,
  sometimestamp TIMESTAMP(6)
)
;
alter table LOCATION
  add constraint LOC_PK primary key (LOCATIONID);
alter table LOCATION
  add constraint LOC_NAME_UK unique (NAME);
alter table LOCATION
  add constraint LOC_LT_FK foreign key (LOCATIONTYPE)
  references LOCATIONTYPE (LOCATIONTYPEID);

prompt
prompt Creating table RESIDENT
prompt =======================
prompt
create table RESIDENT
(
  residentid NUMBER not null,
  firstname  VARCHAR2(200),
  surname    VARCHAR2(200),
  birthday   DATE
)
;
alter table RESIDENT
  add constraint RES_ID primary key (RESIDENTID);

prompt
prompt Creating table RESIDENTLOCATION
prompt ===============================
prompt
create table RESIDENTLOCATION
(
  resident NUMBER not null,
  location NUMBER not null,
  type     VARCHAR2(10) not null
)
;
alter table RESIDENTLOCATION
  add constraint RES_LOC_PK primary key (RESIDENT, LOCATION);


spool off
