package com.study.attach.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.study.attach.service.IAttachDao;
import com.study.attach.service.IAttachservice;
import com.study.attach.vo.AttachVO;
import com.study.exception.BizException;
import com.study.exception.BizNotFoundException;

@Service
public class AttachServiceImpl implements IAttachservice {
	@Inject
	private IAttachDao attachDao;

	@Override
	public AttachVO getAttach(int atchNo) throws BizException {
		AttachVO vo = attachDao.getAttach(atchNo);
		if (vo == null) {
			throw new BizNotFoundException("첨부파일 [" + atchNo + "]에 대한 조회 실패");
		}
		return vo;
	}

	@Override
	public void increaseDownHit(int atchNo) throws BizException {
		attachDao.increaseDownHit(atchNo);
	}

}
