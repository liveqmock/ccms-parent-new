--// update tb_app_properties update http to https
-- Migration SQL that makes the change goes here.
update tb_app_properties set prop_value=REPLACE(prop_value,'http://','https://') where prop_value like '%fenxibao.com%' and prop_value like '%yunat-report%';
update tb_app_properties set prop_value=REPLACE(prop_value,'http://','https://') where prop_value like '%fenxibao.com%' and prop_value like '%udp-web%';
update tb_app_properties set prop_value=REPLACE(prop_value,'http://','https://') where prop_value like '%fenxibao.com%' and prop_value like '%yunat-urpay-web%';

update tb_app_properties set prop_value=REPLACE(prop_value,'udp.ccms','udp-ccms') where prop_value like '%ccms.fenxibao.com%' and prop_value like '%udp-web%';
update tb_app_properties set prop_value=REPLACE(prop_value,'urpay.ccms','urpay-ccms') where prop_value like '%ccms.fenxibao.com%' and prop_value like '%yunat-urpay-web%';

--//@UNDO
-- SQL to undo the change goes here.
update tb_app_properties set prop_value=REPLACE(prop_value,'https://','http://') where prop_value like '%fenxibao.com%' and prop_value like '%yunat-report%';
update tb_app_properties set prop_value=REPLACE(prop_value,'https://','http://') where prop_value like '%fenxibao.com%' and prop_value like '%udp-web%';
update tb_app_properties set prop_value=REPLACE(prop_value,'https://','http://') where prop_value like '%fenxibao.com%' and prop_value like '%yunat-urpay-web%';

update tb_app_properties set prop_value=REPLACE(prop_value,'udp-ccms','udp.ccms') where prop_value like '%ccms.fenxibao.com%' and prop_value like '%udp-web%';
update tb_app_properties set prop_value=REPLACE(prop_value,'urpay-ccms','urpay.ccms') where prop_value like '%ccms.fenxibao.com%' and prop_value like '%yunat-urpay-web%';

