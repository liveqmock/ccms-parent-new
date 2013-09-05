package com.yunat.ccms.biz.service.command;

import org.springframework.security.access.annotation.Secured;

import com.yunat.ccms.biz.domain.Campaign;
import com.yunat.ccms.biz.domain.Template;
import com.yunat.ccms.core.support.auth.AuthCons;

public interface CampaignCommand {

	@Secured({ AuthCons.ADD_ACL_FOR_SECURED_ANNOTATION })
	public void createCampaign(Campaign campaign, Template template);

	@Secured({ AuthCons.SEC_ACL_WRITE_OR_ADMIN })
	public void saveCampaign(Campaign campaign);

	public void saveCustomIndexCampaign(Campaign campaign);

	public void saveRFMCampaign(Campaign campaign);

	public void saveMemberManageCampaign(Campaign campaign);

	public void updateCampaign(Campaign campaign);

	public void updateRFMCampaign(Campaign campaign);

	public void updateMemberManageCampaign(Campaign campaign);

	public void updateCustomIndexCampaign(Campaign campaign);

	@Secured({ AuthCons.SEC_ACL_DELETE_OR_ADMIN })
	public void deleteCampaign(Campaign campaign);

	public void deleteCampaign(Iterable<Campaign> entities);

	public void deleteRFMCampaign(Campaign campaign);

	public void deleteMemberManageCampaign(Campaign campaign);

	public void deleteCustomIndexCampaign(Campaign campaign);

	public void hiddenCampaign(Campaign campaign);

	public void hiddenRFMCampaign(Campaign campaign);

	public void hiddenMemberManageCampaign(Campaign campaign);

	public void hiddenCustomIndexCampaign(Campaign campaign);

}
