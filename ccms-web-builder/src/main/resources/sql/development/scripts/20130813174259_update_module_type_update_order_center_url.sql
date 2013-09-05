--// update module_type update order_center_url
-- Migration SQL that makes the change goes here.
update module_type set url='#/order/orderMonitor' where key_name='ordersCenter_link' and name='订单中心' and url='#/order/urpay?type=1'


--//@UNDO
-- SQL to undo the change goes here.
update module_type set url='#/order/urpay?type=1' where key_name='ordersCenter_link' and name='订单中心' and url='#/order/orderMonitor'

