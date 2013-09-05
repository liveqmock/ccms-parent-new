--// adjust property catalog
-- Migration SQL that makes the change goes here.

DELETE FROM tm_catalog_criteria where query_criteria_id in(5, 14, 24);

--//@UNDO
-- SQL to undo the change goes here.

INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (5,    1,		5,		NULL, 				15);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (14,   1,		14,		NULL, 				14);
INSERT INTO tm_catalog_criteria(id, catalog_id, query_criteria_id, show_name, show_order)  VALUES (24,   1,		24,		NULL, 				9);
