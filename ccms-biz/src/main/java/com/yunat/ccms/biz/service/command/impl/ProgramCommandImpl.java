package com.yunat.ccms.biz.service.command.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunat.ccms.biz.domain.Program;
import com.yunat.ccms.biz.repository.ProgramRepository;
import com.yunat.ccms.biz.service.command.ProgramCommand;

@Service
public class ProgramCommandImpl implements ProgramCommand {

	@Autowired
	private ProgramRepository programRepository;

	@Override
	public void saveProgram(Program program) {
		programRepository.save(program);
	}

	@Override
	public void updateProgram(Program program) {
		programRepository.saveAndFlush(program);
	}

	@Override
	public void deleteProgram(Program program) {
		programRepository.delete(program);
	}
}