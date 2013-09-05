package com.yunat.ccms.tradecenter.controller.vo;

/**
 * 买家备注信息
 *
 * @author shaohui.li
 * @version $Id: TradeMemoVO.java, v 0.1 2013-8-26 下午02:51:56 shaohui.li Exp $
 */
public class TradeMemoVO {

    /** 卖家备注 **/
    private String sellerMemo;

    /** 买家留言 **/
    private String buyerMemo;

    /** 旗帜 **/
    private Long flag;

    public String getSellerMemo() {
        return sellerMemo;
    }

    public void setSellerMemo(String sellerMemo) {
        this.sellerMemo = sellerMemo;
    }

    public String getBuyerMemo() {
        return buyerMemo;
    }

    public void setBuyerMemo(String buyerMemo) {
        this.buyerMemo = buyerMemo;
    }

    public Long getFlag() {
        return flag;
    }

    public void setFlag(Long flag) {
        this.flag = flag;
    }
}
