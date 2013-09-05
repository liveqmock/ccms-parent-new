package com.yunat.ccms.biz.service.command.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunat.ccms.biz.domain.Campaign;
import com.yunat.ccms.biz.domain.Template;
import com.yunat.ccms.biz.repository.CampaignRepository;
import com.yunat.ccms.biz.service.command.CampaignCommand;
import com.yunat.ccms.core.support.statemachine.state.CampaignState;
import com.yunat.ccms.workflow.domain.WorkFlow;
import com.yunat.ccms.workflow.service.command.WorkFlowCommand;
import com.yunat.ccms.workflow.utils.MxGraphJsonUtils;

@Service
@Transactional(readOnly = true)
public class CampaignCommandImpl implements CampaignCommand {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CampaignRepository campaignRepository;

	@Autowired
	private WorkFlowCommand workflowCommand;

	@Override
	public void createCampaign(final Campaign campaign, final Template template) {
		WorkFlow workflow = null;
		if (null == template) {
			workflow = workflowCommand.createWorkflow(MxGraphJsonUtils.standardConvert());
		} else if (null != template.getWorkflow()) {
			workflow = workflowCommand.clone(template.getWorkflow().getWorkflowId());
		}

		if (null != workflow) {
			campaign.setWorkflow(workflow);
			campaignRepository.saveOrUpdate(campaign);
			return;
		} else {
			logger.info("create campaign failure");
		}
	}

	@Override
	public void saveCampaign(final Campaign campaign) {
		try {
			campaignRepository.saveOrUpdate(campaign);
		} catch (final Exception e) {
			logger.info("save campaign throws Exception, save action is failure. Exception : {}", e.getMessage());
		}
	}

	@Override
	public void saveCustomIndexCampaign(final Campaign campaign) {
		try {
			campaignRepository.saveOrUpdate(campaign);
		} catch (final Exception e) {
			logger.info("save custom-index campaign throws Exception, save action is failure, Exception : {}",
					e.getMessage());
		}
	}

	@Override
	public void saveRFMCampaign(final Campaign campaign) {
		try {
			campaignRepository.saveOrUpdate(campaign);
		} catch (final Exception e) {
			logger.info("save rfm campaign throws Exception, save action is failure, Exception : {}", e.getMessage());
		}
	}

	@Override
	public void saveMemberManageCampaign(final Campaign campaign) {
		try {
			campaignRepository.saveOrUpdate(campaign);
		} catch (final Exception e) {
			logger.info("save member-manage campaign throws Exception, save action is failure, Exception : {}",
					e.getMessage());
		}
	}

	@Override
	public void updateCampaign(final Campaign campaign) {
		try {
			campaignRepository.saveOrUpdate(campaign);
		} catch (final Exception e) {
			logger.info("update campaign throws Exception, update action is failure, Exception : {}", e.getMessage());
		}
	}

	@Override
	public void updateRFMCampaign(final Campaign campaign) {
		try {
			campaignRepository.saveOrUpdate(campaign);
		} catch (final Exception e) {
			logger.info("update rfm campaign throws Exception, update action is failure, Exception : {}",
					e.getMessage());
		}
	}

	@Override
	public void updateMemberManageCampaign(final Campaign campaign) {
		try {
			campaignRepository.saveOrUpdate(campaign);
		} catch (final Exception e) {
			logger.info("update mm campaign throws Exception, update action is failure, Exception : {}", e.getMessage());
		}
	}

	@Override
	public void updateCustomIndexCampaign(final Campaign campaign) {
		try {
			campaignRepository.saveOrUpdate(campaign);
		} catch (final Exception e) {
			logger.info("update custome index campaign throws Exception, update action is failure, Exception : {}",
					e.getMessage());
		}
	}

	@Override
	public void deleteCampaign(final Campaign campaign) {
		try {
			if (CampaignState.DESIGN.getCode().equals(campaign.getCampStatus().getStatusId())) {
				campaignRepository.delete(campaign);
			} else {
				hiddenCampaign(campaign);
			}
		} catch (final Exception e) {
			logger.info("delete campaign throws Exception, update action is failure, Exception : {}", e.getMessage());
		}
	}

	@Override
	public void deleteCampaign(final Iterable<Campaign> entities) {
		for (final Campaign campaign : entities) {
			deleteCampaign(campaign);
		}
	}

	@Override
	public void deleteRFMCampaign(final Campaign campaign) {
		try {
			campaignRepository.delete(campaign);
		} catch (final Exception e) {
			logger.info("delete rfm campaign throws Exception, update action is failure, Exception : {}",
					e.getMessage());
		}
	}

	@Override
	public void deleteMemberManageCampaign(final Campaign campaign) {
		try {
			campaignRepository.delete(campaign);
		} catch (final Exception e) {
			logger.info("delete mm campaign throws Exception, update action is failure, Exception : {}", e.getMessage());
		}
	}

	@Override
	public void deleteCustomIndexCampaign(final Campaign campaign) {
		try {
			campaignRepository.delete(campaign);
		} catch (final Exception e) {
			logger.info("delete custome index campaign throws Exception, update action is failure, Exception : {}",
					e.getMessage());
		}
	}

	@Override
	public void hiddenCampaign(final Campaign campaign) {
		try {
			campaign.setDisabled(true);
			campaignRepository.saveOrUpdate(campaign);
		} catch (final Exception e) {
			logger.info("hidden campaign throws Exception, update action is failure, Exception : {}", e.getMessage());
		}
	}

	@Override
	public void hiddenRFMCampaign(final Campaign campaign) {
		try {
			campaign.setDisabled(true);
			campaignRepository.saveOrUpdate(campaign);
		} catch (final Exception e) {
			logger.info("hidden rfm campaign throws Exception, update action is failure, Exception : {}",
					e.getMessage());
		}
	}

	@Override
	public void hiddenMemberManageCampaign(final Campaign campaign) {
		try {
			campaign.setDisabled(true);
			campaignRepository.saveOrUpdate(campaign);
		} catch (final Exception e) {
			logger.info("hidden mm campaign throws Exception, update action is failure, Exception : {}", e.getMessage());
		}
	}

	@Override
	public void hiddenCustomIndexCampaign(final Campaign campaign) {
		try {
			campaign.setDisabled(true);
			campaignRepository.saveOrUpdate(campaign);
		} catch (final Exception e) {
			logger.info("hidden custom index campaign throws Exception, update action is failure, Exception : {}",
					e.getMessage());
		}
	}

}
