--// init_recentTime_for_plt_taobao_transitstepinfo_tc
-- Migration SQL that makes the change goes here.
UPDATE plt_taobao_transitstepinfo_tc ot
SET recently_time =
  case
  when signed_time is not null then signed_time
  when delivery_time is not null then delivery_time
  when arrived_time is not null then arrived_time
  else
    (
      SELECT
        o.consign_time
      FROM
        plt_taobao_order_tc o
      WHERE
        o.tid = ot.tid
    )
  end
WHERE
  recently_time IS NULL;


--//@UNDO
-- SQL to undo the change goes here.


