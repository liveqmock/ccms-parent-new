--// update module data ranking
-- Migration SQL that makes the change goes here.
update module set ranking=1000 where id=54;
update module set ranking=1100 where id=51;
update module set ranking=1200 where id=52;
update module set ranking=1300 where id=55;
update module set ranking=1400 where id=53;
update module set ranking=1500 where id=56;

--//@UNDO
-- SQL to undo the change goes here.


