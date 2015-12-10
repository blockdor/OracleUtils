drop type t_object
/
create or replace type o_object as object(
oname varchar2(200),
otype number,
odate date)
/
create or replace type t_object as table of o_object
/
create or replace type t_date as table of date
/
create or replace type t_string as table of varchar2(4000)
/
create or replace type t_number as table of number
/
