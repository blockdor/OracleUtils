CREATE OR REPLACE PACKAGE sptest IS

  -- Author  : BLOCK
  -- Created : 08/01/2013 16:34:06
  -- Purpose : 

  PROCEDURE testPrimitives(o_errorcode OUT NUMBER,
                           o_errortext OUT VARCHAR2,
                           i_number    IN NUMBER,
                           i_varchar   IN VARCHAR2,
                           i_date      IN DATE);

  PROCEDURE testPrimitives(o_errorcode OUT NUMBER,
                           o_errortext OUT VARCHAR2,
                           i_number    IN NUMBER,
                           i_varchar   IN VARCHAR2,
                           i_timestamp IN TIMESTAMP);
                           
  PROCEDURE testObjects(o_errorcode OUT NUMBER,
                        o_errortext OUT VARCHAR2,
                        i_object  IN o_object);

  PROCEDURE testStringArray(o_errorcode   OUT NUMBER,
                            o_errortext   OUT VARCHAR2,
                            i_stringarray IN custom.t_string);
  PROCEDURE testNumberArray(o_errorcode   OUT NUMBER,
                            o_errortext   OUT VARCHAR2,
                            i_numberarray IN custom.t_number);
  PROCEDURE testObjectArray(o_errorcode   OUT NUMBER,
                            o_errortext   OUT VARCHAR2,
                            i_objectArray IN t_object);
  PROCEDURE testoddparams(o_errorcode OUT NUMBER,
                          o_errortext OUT VARCHAR2,
                          i_number    IN NUMBER,
                          o_number    OUT NUMBER,
                          i_string    IN STRING);
  PROCEDURE testoutputobject(o_errorcode OUT NUMBER,
                             o_errortext OUT VARCHAR2,
                             o_objectout OUT o_object);
  PROCEDURE testoutputArray(o_errorcode   OUT NUMBER,
                            o_errortext   OUT VARCHAR2,
                            o_numberarray OUT custom.t_number);
  PROCEDURE testoutputArrayDate(o_errorcode   OUT NUMBER,
                            o_errortext   OUT VARCHAR2,
                            o_array OUT t_date);
  PROCEDURE testoutputObjectArray(o_errorcode   OUT NUMBER,
                                  o_errortext   OUT VARCHAR2,
                                  o_objectarray OUT t_object);
END sptest;
/
CREATE OR REPLACE PACKAGE BODY sptest IS

  PROCEDURE testPrimitives(o_errorcode OUT NUMBER,
                           o_errortext OUT VARCHAR2,
                           i_number    IN NUMBER,
                           i_varchar   IN VARCHAR2,
                           i_date      IN DATE) AS
  BEGIN
    o_errorcode := 0;
    o_errortext := i_number || i_varchar || to_char(i_date,'dd\mm\yyyy hh:mi:ss');
  END;

  PROCEDURE testPrimitives(o_errorcode OUT NUMBER,
                           o_errortext OUT VARCHAR2,
                           i_number    IN NUMBER,
                           i_varchar   IN VARCHAR2,
                           i_timestamp IN TIMESTAMP) AS
  BEGIN
    o_errorcode := 0;
    o_errortext := i_number || i_varchar || i_timestamp||'TS';
  END;
  
  PROCEDURE testObjects(o_errorcode OUT NUMBER,
                        o_errortext OUT VARCHAR2,
                        i_object  IN o_object) AS
  BEGIN
    o_errorcode := 0;
    o_errortext := i_object.oname;
  END;

  PROCEDURE testStringArray(o_errorcode   OUT NUMBER,
                            o_errortext   OUT VARCHAR2,
                            i_stringarray IN custom.t_string) AS
  BEGIN
    o_errorcode := i_stringarray.count;
    IF (i_stringarray.count > 0) THEN
      o_errortext := i_stringarray(1);
    ELSE
      o_errortext := 'empty array';
    END IF;
  END;
  PROCEDURE testNumberArray(o_errorcode   OUT NUMBER,
                            o_errortext   OUT VARCHAR2,
                            i_numberarray IN custom.t_number) AS
  BEGIN
    o_errorcode := i_numberarray.count;
    IF (i_numberarray.count > 0) THEN
      o_errortext := i_numberarray(1);
    ELSE
      o_errortext := 'empty array';
    END IF;
  END;

  PROCEDURE testObjectArray(o_errorcode   OUT NUMBER,
                            o_errortext   OUT VARCHAR2,
                            i_objectArray IN t_object) AS
  BEGIN
    o_errorcode := i_objectarray.count;
    IF (i_objectarray.count > 0) THEN
      o_errortext := i_objectarray(1).oname;
    ELSE
      o_errortext := 'empty array';
    END IF;
  END;

  PROCEDURE testoddparams(o_errorcode OUT NUMBER,
                          o_errortext OUT VARCHAR2,
                          i_number    IN NUMBER,
                          o_number    OUT NUMBER,
                          i_string    IN STRING)
  
   AS
  BEGIN
    o_errorcode := i_number;
    o_number    := i_number;
    o_errortext := i_string;
  END;

  PROCEDURE testoutputobject(o_errorcode OUT NUMBER,
                             o_errortext OUT VARCHAR2,
                             o_objectout OUT o_object)
  
   AS
  BEGIN
    o_errorcode := 0;
    o_errortext := 'woop'
    o_objectout := o_object('oname',1,sysdate);
  END;

  PROCEDURE testoutputArray(o_errorcode   OUT NUMBER,
                            o_errortext   OUT VARCHAR2,
                            o_numberarray OUT custom.t_number) AS
  BEGIN
    o_numberarray := custom.t_number();
    o_numberarray.extend(2);
    o_numberarray(1) := 1;
    o_numberarray(2) := 2;
  END;
  PROCEDURE testoutputObjectArray(o_errorcode   OUT NUMBER,
                                  o_errortext   OUT VARCHAR2,
                                  o_objectarray OUT t_object) AS
  BEGIN
    o_objectarray := t_object();
    o_objectarray.extend(2);
    o_objectarray(1) := o_object(1,1,sysdate);
    o_objectarray(2) := o_object(2,2,sysdate+1);
    
  END;
  PROCEDURE testoutputArrayDate(o_errorcode   OUT NUMBER,
                            o_errortext   OUT VARCHAR2,
                            o_array OUT t_date)
                            AS
                            BEGIN
                            o_array := t_date();
                            o_array.extend(2);
                            o_array(1) := to_Date('01012001 11:01:01','ddmmyyyy hh:mi:ss');
                            o_array(2) := to_Date('02012001 11:01:01','ddmmyyyy hh:mi:ss');
                            END;
END sptest;
/
