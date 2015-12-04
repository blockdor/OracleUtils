create or replace package PKGEXAMPLE is

  -- Author  : BLOCK
  -- Created : 03/12/2015 16:34:47
  -- Purpose : 
  procedure complicatedoutputs(out1 out t_number,
            out2 out t_number,
            out3 out t_object
             
  

end PKGEXAMPLE;
/
create or replace package body PKGEXAMPLE is

  -- Private type declarations
  type <TypeName> is <Datatype>;
  
  -- Private constant declarations
  <ConstantName> constant <Datatype> := <Value>;

  -- Private variable declarations
  <VariableName> <Datatype>;

  -- Function and procedure implementations
  function <FunctionName>(<Parameter> <Datatype>) return <Datatype> is
    <LocalVariable> <Datatype>;
  begin
    <Statement>;
    return(<Result>);
  end;

begin
  -- Initialization
  <Statement>;
end PKGEXAMPLE;
/
