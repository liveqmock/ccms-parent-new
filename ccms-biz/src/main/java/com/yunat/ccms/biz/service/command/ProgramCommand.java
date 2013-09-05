package com.yunat.ccms.biz.service.command;

import com.yunat.ccms.biz.domain.Program;

public interface ProgramCommand {
	void saveProgram(Program program);

	void updateProgram(Program program);

	void deleteProgram(Program program);
}