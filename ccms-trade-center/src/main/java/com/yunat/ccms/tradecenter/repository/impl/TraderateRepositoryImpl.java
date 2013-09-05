package com.yunat.ccms.tradecenter.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunat.ccms.tradecenter.controller.vo.TradeMemoVO;
import com.yunat.ccms.tradecenter.domain.AffairDomain;
import com.yunat.ccms.tradecenter.repository.AffairRepository;
import com.yunat.ccms.tradecenter.service.TaobaoMemoService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.yunat.ccms.core.support.jdbc.JdbcPaginationHelper;
import com.yunat.ccms.tradecenter.controller.vo.TraderateVO;
import com.yunat.ccms.tradecenter.repository.TraderateRepository;
import com.yunat.ccms.tradecenter.urpay.filter.MemberGradeFilter;

@Repository
public class TraderateRepositoryImpl implements TraderateRepository {


    @Autowired
    private TaobaoMemoService taobaoMemoService;

    @Autowired
    private AffairRepository affairRepository;

	@Autowired
	private JdbcPaginationHelper helper;

	private Logger logger = LoggerFactory.getLogger(TraderateRepositoryImpl.class);

	private static final String unLimitMemberLevel = "-1";

	@Override
	public Page<Map<String, Object>> queryTraderateByCondition(TraderateVO traderateVO, Pageable page) {

		StringBuilder baseStr = this.createTraderateBaseStringBuilder(traderateVO);

		StringBuilder memberStr = new StringBuilder();
		StringBuilder regardStr = new StringBuilder();
		if (!unLimitMemberLevel.equals(traderateVO.getMemberLevel())) {
			memberStr = this.createTraderateMemberStringBuilder(traderateVO);
		}

		regardStr = this.createTraderateRegardStr(traderateVO);

		return this.targetResultDirector(baseStr.toString(), memberStr.toString(), regardStr.toString(), traderateVO,
				page);
	}

	/**
	 * 
	 * 关怀处理
	 * 
	 * @param traderateVO
	 * @return
	 */

	private StringBuilder createTraderateRegardStr(TraderateVO traderateVO) {
		StringBuilder targetRegardStr = new StringBuilder();

		StringBuilder selectRegardStr = new StringBuilder();
		StringBuilder fromRegardStr = new StringBuilder();
		StringBuilder whereRegardStr = new StringBuilder();

		StringBuilder selectUnRegardStr = new StringBuilder();
		StringBuilder fromUnRegardStr = new StringBuilder();
		StringBuilder whereUnRegardStr = new StringBuilder();

		if (traderateVO.getIsRegardFlag() == null) {
			StringBuilder targetRegardSb = getRegardString(selectRegardStr, fromRegardStr, whereRegardStr);
			StringBuilder targetUnRegardSb = getUnRegardString(selectUnRegardStr, fromUnRegardStr, whereUnRegardStr);
			targetRegardStr = targetRegardSb.append(" union all ").append(targetUnRegardSb);

		} else {
			if (traderateVO.getIsRegardFlag()) {
				targetRegardStr = getRegardString(selectRegardStr, fromRegardStr, whereRegardStr);
			} else {
				targetRegardStr = getUnRegardString(selectUnRegardStr, fromUnRegardStr, whereUnRegardStr);
			}
		}

		return targetRegardStr;
	}

	/**
	 * @param selectStr
	 * @param fromStr
	 * @param whereStr
	 */
	private StringBuilder getUnRegardString(StringBuilder selectStr, StringBuilder fromStr, StringBuilder whereStr) {
		selectStr.append(" select  t.tid,t.oid ,0 as regardFlag ");
		fromStr.append("   from    plt_taobao_traderate  t   ");
		whereStr.append("  where not  exists ( ")
				.append("  select 1  from  tb_tc_caring_detail  c where  t.tid =c.tid  and t.oid = c.oid   ")
				.append(")");
		return selectStr.append(fromStr).append(whereStr);
	}

	/**
	 * @param selectStr
	 * @param fromStr
	 * @param whereStr
	 */
	private StringBuilder getRegardString(StringBuilder selectStr, StringBuilder fromStr, StringBuilder whereStr) {
		selectStr.append(" select  t.tid,t.oid ,1 as regardFlag ");
		fromStr.append("   from    plt_taobao_traderate  t   ");
		whereStr.append("  where exists ( ")
				.append("  select 1  from  tb_tc_caring_detail  c where  t.tid =c.tid  and t.oid = c.oid   ")
				.append(")");
		return selectStr.append(fromStr).append(whereStr);
	}

	/**
	 * 
	 * 会员等级，筛选有评价的订单
	 * 
	 * @param traderateVO
	 *            页面传过来查询条件参数VO
	 * @return
	 */
	private StringBuilder createTraderateMemberStringBuilder(TraderateVO traderateVO) {

		StringBuilder selectStr = new StringBuilder();
		StringBuilder fromStr = new StringBuilder();
		StringBuilder whereStr = new StringBuilder();

		selectStr.append(" select  t.tid,t.oid ");
		fromStr.append("   from    plt_taobao_traderate  t , plt_taobao_crm_member m    ");
		whereStr.append("  where   1 =1 and  t.nick =  m.customerno  and m.dp_id = :shopId    ");

		if (traderateVO.getMemberLevel().equals(MemberGradeFilter.NEW_CUSTOMER)) {
			whereStr.append(" and m.grade  =0 and  m.trade_count  =0  ");

		} else if (traderateVO.getMemberLevel().equals(MemberGradeFilter.NO_CLASS)) {

			whereStr.append(" and m.grade  =0 and  m.trade_count  >0  ");

		} else {
			whereStr.append(" and m.grade  = :grade  ");
		}

		if (!traderateVO.getMemberLevel().equals(MemberGradeFilter.NEW_CUSTOMER)) {
			return selectStr.append(fromStr).append(whereStr);
		} else {
			return selectStr
					.append(fromStr)
					.append(whereStr)
					.append(" union all ")
					.append(" select  t1.tid,t1.oid   from plt_taobao_traderate  t1  "
							+ " where  not exists ( select 1 from plt_taobao_crm_member m1 where t1.nick =  m1.customerno )  and t1.dp_id = :shopId  ");
		}

	}

	private StringBuilder createTraderateBaseStringBuilder(TraderateVO traderateVO) {

		StringBuilder selectStr = new StringBuilder();
		StringBuilder fromStr = new StringBuilder();
		StringBuilder whereStr = new StringBuilder();

		selectStr
				.append("  select  t.tid,t.oid,  t.content , t.created, t.result, t.reply, "
						+ "  concat('http://detail.tmall.com/item.htm?id=',t.num_iid) as detail_url,  "
						+ "  p.pic_url,"
						+ "  t.item_title as title, t.item_price as price, t.nick as customerno, m.grade, m.trade_count,o.receiver_mobile ");
		fromStr.append("   from   plt_taobao_traderate  t left join  plt_taobao_crm_member  m  on   t.dp_id  =  m.dp_id  and   t.nick =  m.customerno,  ");
		fromStr.append("   plt_taobao_product p,  plt_taobao_order_tc  o  ");
		whereStr.append("  where   1 =1   and  t.tid =o.tid   and t.num_iid = p.num_iid   ");

		// 店铺
		if (!StringUtils.isEmpty(traderateVO.getShopId())) {
			whereStr.append("  and  t.dp_id  =  :shopId  ");
		}

		// 评价内容
		if (!StringUtils.isEmpty(traderateVO.getContent())) {
			whereStr.append("  and  t.content  like  :content  ");
		}

		// 解释
		if (traderateVO.getIsExplainFlag() != null) {
			if (traderateVO.getIsExplainFlag()) {
				whereStr.append("  and  t.reply  is not null ");
			} else {
				whereStr.append("  and  t.reply  is  null ");
			}
		}

		// 商品名称
		if (!StringUtils.isEmpty(traderateVO.getItemTitle())) {
			whereStr.append("  and  t.item_title  like  :itemTitle  ");
		}

		// 评价时间
		if (!StringUtils.isEmpty(traderateVO.getBeginCreated())) {
			whereStr.append("  and  t.created  >=  :beginCreated  ");
		}
		if (!StringUtils.isEmpty(traderateVO.getEndCreated())) {
			whereStr.append("  and  t.created  <=  :endCreated  ");
		}

		// 评价类型
		if (!StringUtils.isEmpty(traderateVO.getResult())) {
			whereStr.append("  and  t.result  =  :result  ");
		}

		// 评价客户昵称
		if (!StringUtils.isEmpty(traderateVO.getNick())) {
			whereStr.append("  and  t.nick  =  :nick  ");
		}
		return selectStr.append(fromStr).append(whereStr);
	}

	private Page<Map<String, Object>> targetResultDirector(String baseStr, String memberStr, String regardStr,
			TraderateVO traderateVO, Pageable page) {

		StringBuilder selectStr = new StringBuilder();
		StringBuilder fromStr = new StringBuilder();
		StringBuilder whereStr = new StringBuilder();

		selectStr.append("  select   base.tid,base.oid,  base.content , base.created,  base.result,  base.reply,");
		selectStr
				.append("  base.detail_url, base.pic_url, base.title, base.price, base.customerno, base.grade, base.trade_count,base.receiver_mobile,regard.regardFlag, '' as sendLogList ");
		fromStr.append("from").append("(").append(baseStr).append(") as base ").append(",").append("(")
				.append(regardStr).append(") as regard ");
		whereStr.append("where").append(" 1=1 ")
				.append(" and  base.tid   =  regard.tid  and  base.oid   =  regard.oid  ");

		if (!StringUtils.isEmpty(memberStr)) {
			fromStr.append(",").append("(").append(memberStr).append(") as member ");
			whereStr.append(" and  base.tid   =  member.tid  and  base.oid   =  member.oid  ");
		}

		String targetSql = selectStr.append(fromStr).append(whereStr).toString();
		logger.info("评价事务查询最总sql" + targetSql);

		Map<String, Object> paramMap = new HashMap<String, Object>();

		if (!StringUtils.isEmpty(traderateVO.getShopId())) {
			paramMap.put("shopId", traderateVO.getShopId());
		}

		if (!StringUtils.isEmpty(traderateVO.getContent())) {
			paramMap.put("content", "%" + traderateVO.getContent() + "%");
		}
		if (!StringUtils.isEmpty(traderateVO.getItemTitle())) {
			paramMap.put("itemTitle", "%" + traderateVO.getItemTitle() + "%");
		}
		if (!StringUtils.isEmpty(traderateVO.getBeginCreated())) {
			paramMap.put("beginCreated", traderateVO.getBeginCreated());
		}
		if (!StringUtils.isEmpty(traderateVO.getEndCreated())) {
			paramMap.put("endCreated", traderateVO.getEndCreated());
		}
		if (!StringUtils.isEmpty(traderateVO.getResult())) {
			paramMap.put("result", traderateVO.getResult());
		}
		if (!StringUtils.isEmpty(traderateVO.getNick())) {
			paramMap.put("nick", traderateVO.getNick());
		}
		if (!unLimitMemberLevel.equals(traderateVO.getMemberLevel())) {
			if (!traderateVO.getMemberLevel().equals(MemberGradeFilter.NEW_CUSTOMER)
					&& !traderateVO.getMemberLevel().equals(MemberGradeFilter.NO_CLASS)) {
				paramMap.put("grade", traderateVO.getMemberLevel());
			}
		}

		// return helper.queryForMap(targetSql, paramMap, page);

		Long total = 0L;

		List<Map<String, Object>> targetList = new ArrayList<Map<String, Object>>();
		Page<Map<String, Object>> tempList = helper.queryForMap(targetSql, paramMap, page);

		if (tempList.getSize() > 0) {

			// 当页总数
			total = tempList.getTotalElements();
		}

        List<String> tids = new ArrayList<String>();
        List<String> oids = new ArrayList<String>();

		for (Map<String, Object> map : tempList) {
            tids.add((String) map.get("tid"));
            oids.add((String) map.get("oid"));

			// 有关怀过的，再去获取关怀记录
			if ("1".equals(String.valueOf(map.get("regardFlag")))) {

				String sql = " SELECT "
						+ " s.send_user AS regardPerson, s.created AS regardTime, s.type AS regardType,s.sms_content AS regardContent  "
						+ " FROM  tb_tc_send_log  s  "
						+ " WHERE s.tid =:tid  AND s.oid =:oid  AND  s.type IN (27,28,29)  ";

				Map<String, String> p = new HashMap<String, String>();
				p.put("tid", (String) map.get("tid"));
				p.put("oid", (String) map.get("oid"));

				List<Map<String, Object>> sendLogList = helper.queryForList(sql.toString(), p);

				map.put("sendLogList", sendLogList);

			}
			targetList.add(map);

		}

        if (tids.size() > 0) {
            Map<String,TradeMemoVO> memoMap = taobaoMemoService.getTaobaoMemo(tids, traderateVO.getShopId());
            Map<String, AffairDomain> oidAffairDomains = affairRepository.getAffairDomainMapByOids(oids, 6);
            for (Map<String, Object> tarMap : targetList) {
                tarMap.put("memoVo", memoMap.get(tarMap.get("tid")));
                AffairDomain affairDomain = oidAffairDomains.get(tarMap.get("oid"));

                Integer followupStatus = null;
                Long followupId = null;
                if (affairDomain != null) {
                    followupStatus =  affairDomain.getStatus();
                    followupId =   affairDomain.getPkid();
                }

                tarMap.put("followupStatus", followupStatus);
                tarMap.put("followupId", followupId);
            }
        }


		return new PageImpl<Map<String, Object>>(targetList, page, total);

	}

	@Override
	public List<Map<String, Object>> getNotGoodListByDpId(String dpId, Date lastDealTime) {
		String sql = "select * from plt_taobao_traderate where dp_id = :dpId and created > :lastDealTime and result in ('neutral','bad')";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("dpId", dpId);
		paramMap.put("lastDealTime", lastDealTime);

		return helper.queryForList(sql, paramMap);
	}

	@Override
	public boolean findIfDealWithNotGood(String oid) {
		String sql = "select * from tb_tc_warn_status where oid = :oid and not_good_warn_status = '1'";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("oid", oid);
		return helper.queryForList(sql, paramMap).isEmpty();
	}
}
